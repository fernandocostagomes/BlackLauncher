package br.com.fernandocostagomes.blacklauncher.activities;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import br.com.fernandocostagomes.blacklauncher.R;
import br.com.fernandocostagomes.blacklauncher.objects.AppInfo;
import br.com.fernandocostagomes.blacklauncher.util.ClientConsts;
import br.com.fernandocostagomes.blacklauncher.util.DbConsts;
import br.com.fernandocostagomes.blacklauncher.util.DbHelper;

/**
 * Classe da Activity Settings do App ATMobileLauncher.
 * 
 * @author fernando.gomes
 * 
 */
public class SettingsActivity extends Activity
{
   /**
    * Constante usada para setar a permissão 0.
    */
   private String PERMISSION_0 = "0";

   /**
    * Constante usada para setar a permissão 1.
    */
   private String PERMISSION_1 = "1";

   /**
    * Constante usada para comparação.
    */
   private String EMPTY = "";

   /**
    * Button usado para limpar a base dados do Launcher ATMobile. *
    */
   private Button m_btClearDB;

   /**
    * Button usado para ativar e escolher o App no modo Single App. *
    */
   private Button m_btModeSingleApp;

   /**
    * TextView usado para mostrar o nome do App no modo Single App. *
    */
   private TextView m_txtModeSingleApp;

   /**
    * Button usado para habilitar/desabilitar o butão power na tela home. *
    */
   private Button m_btPower;

   /**
    * Button usado para a função Reboot Programado. *
    */
   private Button m_btRebootProgramed;

   /*
    * Váriavel para alimentar o texto do button poweroff.
    */
   private String m_valueParamButtonPower = "ini";

   /**
    * Classe instanciada que conecta no banco de dados.
    */
   private DbHelper m_dbh = new DbHelper( this );

   /**
    * Variaveis usadas no reboot programado para armazenar as horas e minutos.
    */
   private int m_hour = 0;
   private int m_minute = 0;

   /**
    * TextView usado para mostrar o horario do reboot programado. *
    */
   private TextView m_txtRebootProgramed;

   /**
    * Método que configura o button Power na tela.
    */
   private void checkPowerButton()
   {
      DbHelper dbh = new DbHelper( this );

      m_valueParamButtonPower = dbh.selectParameter( DbConsts.DbParameterIdValues.PARAM_POWER_BUTTON_HOME_SCREEN_ID );

      if ( m_valueParamButtonPower.equals( EMPTY ) )
      {
         dbh.insertParameter( DbConsts.DbParameterIdValues.PARAM_POWER_BUTTON_HOME_SCREEN_ID,
                  ClientConsts.PowerButtonHomeScreen.DESACTIVED );
         m_btPower.setText( R.string.desactived );
      }
      // Pesquisa o parâmetro e trata.
      if ( m_valueParamButtonPower.equals( ClientConsts.PowerButtonHomeScreen.ACTIVED ) )
      {
         m_btPower.setText( R.string.actived );
      }
      if ( m_valueParamButtonPower.equals( ClientConsts.PowerButtonHomeScreen.DESACTIVED ) )
      {
         m_btPower.setText( R.string.desactived );
      }
   }

   /**
    * Método que configura o button SingleApp na tela.
    */
   private void checkModeSingleAppButton()
   {
      DbHelper dbh = new DbHelper( this );

      String m_valueParamModeSingleApp = dbh.selectParameter( DbConsts.DbParameterIdValues.PARAM_MODE_SINGLE_APP_ID );

      if ( m_valueParamModeSingleApp.equals( EMPTY ) )
      {
         dbh.insertParameter( DbConsts.DbParameterIdValues.PARAM_MODE_SINGLE_APP_ID,
                  PERMISSION_0 );
      }
      if ( m_valueParamModeSingleApp.equals( PERMISSION_0 ) )
      {
         m_btModeSingleApp.setText( R.string.desactived );
         m_txtModeSingleApp.setText( R.string.txtModeSingleApp );
         m_txtModeSingleApp.setTextColor( Color.WHITE );
      }
      else
      {
         m_btModeSingleApp.setText( R.string.actived );
         m_txtModeSingleApp.setText( getString( R.string.mode_single_app_dot ) + " " + m_valueParamModeSingleApp );
         m_txtModeSingleApp.setTextColor( Color.RED );
      }
   }

   /**
    * Método que configura o button de RebootProgramado.
    */
   private void checkBtRebootProgramed()
   {
      DbHelper dbh = new DbHelper( this );

      String m_valueParamRebootProgramed = dbh
               .selectParameter( DbConsts.DbParameterIdValues.PARAM_REBOOT_PROGRAMED_ID );

      if ( m_valueParamRebootProgramed.equals( EMPTY ) )
      {
         dbh.insertParameter( DbConsts.DbParameterIdValues.PARAM_REBOOT_PROGRAMED_ID,
                  "0" );
      }
      if ( m_valueParamRebootProgramed.equals( "0" ) )
      {
         m_btRebootProgramed.setText( R.string.desactived );
         m_txtRebootProgramed.setText( getString( R.string.reboot_programed ) );
         m_txtRebootProgramed.setTextColor( Color.WHITE );
      }
      else
      {
         m_btRebootProgramed.setText( R.string.actived );
         m_txtRebootProgramed.setText( getString( R.string.reboot_programed ) + ": " + m_valueParamRebootProgramed );
         m_txtRebootProgramed.setTextColor( Color.RED );
      }
   }

   /**
    * Metódo que carrega a lista de Apps do Tipo Launcher.
    * 
    * @return resultNamePackage retorna o name do pacote de instalação do Launcher ATMobile.
    */
   private String loadApps()
   {
      PackageManager m_pm = getPackageManager();
      Intent i = new Intent( Intent.ACTION_MAIN, null );
      i.addCategory( Intent.CATEGORY_HOME );

      // LoadApps
      List<ResolveInfo> availableActivities = m_pm.queryIntentActivities( i, 0 );
      String resultNamePackage = EMPTY;
      for ( ResolveInfo ri : availableActivities )
      {
         String labelLauncherATM = String.valueOf( ri.loadLabel( m_pm ) );
         if ( labelLauncherATM.contains( getString( R.string.label_atmobile ) ) )
         {
            resultNamePackage = ri.activityInfo.packageName;
         }
      }
      return resultNamePackage;
   }

   /**
    * Método que agenda o Reboot Programado.
    * 
    * @param p_hour através de um TimePicker é gerado a hora desejada.
    * @param p_minute através de um TimePicker é gerado o minuto desejado.
    */
   private void scheduleRebootProgramed( int p_hour, int p_minute )
   {
      // Várivel usada para guardar a diferença entre os horários atual e o agendado.
      Long long_seconds = ( long )0;

      // Classe Date para resgatar a hora atual.
      Date data = new Date();

      // Padronizar a data.
      Calendar cal = Calendar.getInstance();
      cal.setTime( data );

      // Data atual, instante que foi chamado o evento.
      Date data_atual = cal.getTime();
      long longDateNowSeconds = data_atual.getTime();

      // Data que foi agendada.
      Date dateSchedule = data_atual;
      dateSchedule.setHours( p_hour );
      dateSchedule.setMinutes( p_minute );
      long longDateSchedule = dateSchedule.getTime();

      // Calculando a diferença entre as datas.
      if ( longDateSchedule > longDateNowSeconds )
      {
         long_seconds = longDateSchedule - longDateNowSeconds;
      }
      else
      {
         long_seconds = longDateNowSeconds - longDateSchedule;
      }

      // Convertendo de millisegundos para segundos.
      Long testesegundos = long_seconds / 600;

      // Intent para disparar o broadcast
      Intent it = new Intent( "EXECUTAR_ALARME" );
      PendingIntent pi = PendingIntent.getBroadcast( SettingsActivity.this, 0, it, 0 );

      // Para disparar o alarme a partir de x segundos a partir da chamada.
      Calendar ca = Calendar.getInstance();
      ca.setTimeInMillis( System.currentTimeMillis() );
      Integer teste = Integer.valueOf( testesegundos.toString() );
      ca.add( Calendar.SECOND, teste );
      Long oneday = ( long )( 86400 * 600 );

      // Agenda o alarme
      AlarmManager al = ( AlarmManager )getSystemService( ALARM_SERVICE );
      long time = ca.getTimeInMillis();
      al.setRepeating( AlarmManager.RTC_WAKEUP, time, oneday, pi );
      Log.i( "CATEGORIA", "Alarme agendado para daqui a " + testesegundos + " segundos." );
   }

   /**
    * Método usado para cancelar o Reboot Programado.
    * 
    * @return returnCancell - Retorno do sucesso ou não do cancelamento.
    */
   private boolean cancelRebootProgramed()
   {
      // Váriavel usada para retornar o sucesso ou não do cancelamento.
      boolean returnCancell = false;

      Log.i( "ALARME", "Cancelando o Alarme para reiniciar!" );

      try
      {
         Intent it = new Intent( "EXECUTAR_ALARME" );
         PendingIntent pi = PendingIntent.getBroadcast( SettingsActivity.this, 0, it, 0 );
         // Cancela o alarme
         AlarmManager am = ( AlarmManager )getSystemService( ALARM_SERVICE );
         am.cancel( pi );

         returnCancell = true;
      }
      catch ( Exception p_e )
      {
         p_e.printStackTrace();
         returnCancell = false;
      }
      return returnCancell;
   }

   /**
    * Método Oncreate.
    */
   @Override
   protected void onCreate( Bundle p_savedInstanceState )
   {
      // TODO Auto-generated method stub
      super.onCreate( p_savedInstanceState );
      setContentView( R.layout.activity_settings );

      // Button para limpar a base de dados.
      m_btClearDB = ( Button )findViewById( R.id.btClearDB );

      // Button para Habilitar/Desabilitar.
      m_btPower = ( Button )findViewById( R.id.btPower );

      // Button para Ativar o modo App único.
      m_btModeSingleApp = ( Button )findViewById( R.id.btModeSingleApp );

      // Button para o Reboot Programed
      m_btRebootProgramed = ( Button )findViewById( R.id.btRebootProgramed );

      //
      m_txtRebootProgramed = ( TextView )findViewById( R.id.rebootProgramed );

      // TextView para o modo App único.
      m_txtModeSingleApp = ( TextView )findViewById( R.id.txtModeSingleApp );

      // Método que configura o button power.
      checkPowerButton();

      // Método que configura o button Single App.
      checkModeSingleAppButton();

      // Método que configura o button Reboot Programed.
      checkBtRebootProgramed();
   }

   /**
    * Evento de clique que altera a senha de configurações/Listar Apps. *
    */
   public void evtAlterPassWord( View p_view )
   {
      LayoutInflater factory = LayoutInflater.from( this );

      // text_entry is an Layout XML file containing two text field to display in alert dialog
      final View textEntryView = factory.inflate( R.layout.activity_update_password, null );

      final EditText oldPassword = ( EditText )textEntryView.findViewById( R.id.etOldPassword );
      final EditText newPassword = ( EditText )textEntryView.findViewById( R.id.etNewPassword );
      final EditText newPasswordConfirm = ( EditText )textEntryView.findViewById( R.id.etNewPasswordConfirm );

      // oldPassword.setText( "Antiga senha", TextView.BufferType.EDITABLE );
      // newPassword.setText( "Nova Senha", TextView.BufferType.EDITABLE );
      // newPasswordConfirm.setText( "Confirme Nova Senha", TextView.BufferType.EDITABLE );

      AlertDialog.Builder alert = new AlertDialog.Builder( this );
      alert
               .setIcon( R.drawable.ic_launcher )
               .setTitle( R.string.alter_psw )
               .setView( textEntryView )
               .setPositiveButton( R.string.alter, new DialogInterface.OnClickListener()
               {
                  public void onClick( DialogInterface p_dialog,
                           int p_whichButton )
                  {
                     String textOldPassword = oldPassword.getText().toString();
                     String textNewPassword = newPassword.getText().toString();
                     String textNewPasswordConfirm = newPasswordConfirm.getText().toString();

                     if ( textOldPassword.equals( m_dbh.selecPassWord( "PSW_TXT_Password" ) ) )
                     {
                        if ( textNewPassword.equals( textNewPasswordConfirm ) )
                        {
                           // Verifica se os campos estão em branco.
                           if ( !textNewPassword.equals( "" ) )
                           {
                              // Grava no banco a senha atual na antiga.
                              m_dbh.updatePassword( "PSW_TXT_OldPassword", textOldPassword );

                              // Grava no banco a nova senha.
                              m_dbh.updatePassword( "PSW_TXT_Password", textNewPassword );

                              Toast.makeText( SettingsActivity.this, R.string.alter_psw_sucess, Toast.LENGTH_LONG )
                                       .show();
                           }
                           else
                           {
                              Toast.makeText( SettingsActivity.this, R.string.alter_psw_not_null, Toast.LENGTH_LONG )
                                       .show();
                           }
                        }
                        else
                        {
                           Toast.makeText( SettingsActivity.this,
                                    R.string.alter_psw_not_equals, Toast.LENGTH_LONG )
                                    .show();
                        }
                     }
                     else
                     {
                        Toast.makeText( SettingsActivity.this, R.string.alter_psw_not_confirm_old_psw,
                                 Toast.LENGTH_LONG )
                                 .show();
                     }

                  }
               } ).setNegativeButton( R.string.cancell,
                        new DialogInterface.OnClickListener()
                        {
                           public void onClick( DialogInterface p_dialog,
                                    int p_whichButton )
                           {
                              /*
                               * User clicked cancel so do some stuff
                               */
                           }
                        } );
      alert.show();
   }

   /**
    * Evento de clique que limpa o Banco de dados do Launcher ATMobile. *
    */
   public void evtClearDB( View p_view )
   {
      m_btClearDB.setEnabled( false );
      final DbHelper dbh = new DbHelper( this );

      AlertDialog alertDialog = new AlertDialog.Builder( SettingsActivity.this )
               .setIcon( android.R.drawable.ic_dialog_alert )
               .setTitle( R.string.app_name )
               .setMessage( R.string.askConfirmClearDB )
               .setPositiveButton( android.R.string.ok, new DialogInterface.OnClickListener()
               {
                  @Override
                  public void onClick( DialogInterface p_dialog, int p_which )
                  {
                     if ( dbh.dropDB() )
                     {
                        Toast.makeText( SettingsActivity.this, R.string.bd_clean_sucess,
                                 Toast.LENGTH_LONG ).show();
                     }
                     else
                     {
                        Toast.makeText( SettingsActivity.this, R.string.bd_clean_no_sucess,
                                 Toast.LENGTH_LONG ).show();
                     }
                     m_btClearDB.setEnabled( true );
                  }
               } ).setNegativeButton( android.R.string.no, new DialogInterface.OnClickListener()
               {
                  @Override
                  public void onClick( DialogInterface dialog, int which )
                  {
                     dialog.dismiss();
                     m_btClearDB.setEnabled( true );
                  }
               } )
               .show();
   }

   /**
    * Evento de clique que envia para a activity com os apps instalados no tablet. *
    */
   public void evtShowApps( View p_view )
   {
      startActivity( new Intent( getApplicationContext(), ShowAppsActivity.class ) );
   }

   /**
    * Evento de clique que conecta a um sevidor html com as versões do app Autotrac Mobile. *
    */
   public void evtDownloadATMobile( View p_view )
   {

   }

   /**
    * Evento de clique que programa um reboot programado. *
    */
   public void evtRebootProgramed( View p_view )
   {
      final DbHelper dbh = new DbHelper( this );
      String txtButton = ( String )m_btRebootProgramed.getText();
      if ( txtButton.equals( getString( R.string.desactived ) ) )
      {
         LayoutInflater factory = LayoutInflater.from( this );
         final View entryView = factory.inflate( R.layout.activity_reboot_programed, null );
         final TimePicker timerPick = ( TimePicker )entryView.findViewById( R.id.timePickerRebootProgramed );

         final AlertDialog.Builder alert = new AlertDialog.Builder( this );
         alert
                  .setIcon( R.drawable.ic_launcher )
                  .setTitle( R.string.reboot_programed_tittle )
                  .setView( entryView )
                  .setPositiveButton( R.string.reboot_programed_set, new DialogInterface.OnClickListener()
                  {
                     public void onClick( DialogInterface p_dialog,
                              int p_whichButton )
                     {
                        m_hour = timerPick.getCurrentHour();
                        m_minute = timerPick.getCurrentMinute();

                        String tempo = String.valueOf( m_hour ) + ":" + String.valueOf( m_minute ) + ":00";

                        scheduleRebootProgramed( m_hour, m_minute );

                        // Alterando ao textView o horário escolhido para o reboot programado.
                        m_txtRebootProgramed.setText( "Reboot programado: " + tempo );
                        // Alterando o button para Ativado.
                        m_btRebootProgramed.setText( getString( R.string.actived ) );
                        try
                        {
                           dbh.updateParameter( DbConsts.DbParameterIdValues.PARAM_REBOOT_PROGRAMED_ID, tempo );
                        }
                        catch ( Exception p_e )
                        {
                           p_e.printStackTrace();
                        }
                        checkBtRebootProgramed();
                     }
                  } )
                  .setNegativeButton( R.string.cancell, new DialogInterface.OnClickListener()
                  {
                     public void onClick( DialogInterface p_dialog,
                              int p_whichButton )
                     {
                        p_dialog.dismiss();
                     }
                  } );

         alert.show();
      }
      else
      {
         if ( cancelRebootProgramed() )
         {
            Toast.makeText( SettingsActivity.this, "O Reboot programado foi cancelado com sucesso!", Toast.LENGTH_LONG )
                     .show();
            try
            {
               dbh.updateParameter( DbConsts.DbParameterIdValues.PARAM_REBOOT_PROGRAMED_ID, "0" );
               checkBtRebootProgramed();
            }
            catch ( Exception p_e )
            {
               p_e.printStackTrace();
            }
         }
         else
         {
            Toast.makeText( SettingsActivity.this, "Não foi possível cancelar o Reboot programado!", Toast.LENGTH_LONG )
                     .show();
         }
         checkBtRebootProgramed();
      }
   }

   /**
    * Evento de clique que habilita/desabilita button power na tela home. *
    */
   public void evtBtPower( View p_view )
   {
      DbHelper dbh = new DbHelper( this );

      String valueParamButtonPowerEvt = dbh
               .selectParameter( DbConsts.DbParameterIdValues.PARAM_POWER_BUTTON_HOME_SCREEN_ID );
      if ( valueParamButtonPowerEvt.equals( PERMISSION_1 ) )
      {
         dbh.updateParameter( DbConsts.DbParameterIdValues.PARAM_POWER_BUTTON_HOME_SCREEN_ID,
                  ClientConsts.PowerButtonHomeScreen.DESACTIVED );
         onCreate( null );
      }
      if ( valueParamButtonPowerEvt.equals( PERMISSION_0 ) )
      {
         dbh.updateParameter( DbConsts.DbParameterIdValues.PARAM_POWER_BUTTON_HOME_SCREEN_ID,
                  ClientConsts.PowerButtonHomeScreen.ACTIVED );
         onCreate( null );
      }
   }

   /**
    * Evento de clique que habilita/desabilita Modo Single App. *
    */
   public void evtBtModeSingleApp( View p_view )
   {
      final DbHelper dbh = new DbHelper( this );

      if ( m_btModeSingleApp.getText().equals( getString( R.string.actived ) ) )
      {
         try
         {
            dbh.updateParameter( DbConsts.DbParameterIdValues.PARAM_MODE_SINGLE_APP_ID, PERMISSION_0 );
            checkModeSingleAppButton();
            Toast.makeText( SettingsActivity.this, R.string.mode_single_app_actived, Toast.LENGTH_LONG ).show();
            dbh.updateAllAppPermission( PERMISSION_0 );
         }
         catch ( Exception e )
         {
            e.printStackTrace();
         }

      }
      else
      {
         // Array com os apps do bancoo de dados.
         ArrayList<String> dir = new ArrayList<String>( dbh.selectLabelAllAppsDB() );
         // Fazendo um cast para um Charsequence.
         ArrayList<String> listing = new ArrayList<String>();
         String item;
         for ( int i = 0; i < dir.size(); i++ )
         {
            item = dir.get( i );
            listing.add( item.toString() );
         }

         // Criando uma dialog / janela com o nome dos Apps para ser escolhido um.
         AlertDialog dialog;
         // Criando o Charsequence.
         final CharSequence[] items = listing.toArray( new CharSequence[ listing.size() ] );

         AlertDialog.Builder builder = new AlertDialog.Builder( this );
         // Titulo do da janela.
         builder.setTitle( R.string.txtModeSingleApp );
         builder.setSingleChoiceItems( items, 0,
                  new DialogInterface.OnClickListener()
                  {
                     @Override
                     public void onClick( DialogInterface p_dialog, int p_which )
                     {
                        // Passando o valor do item selecionado para uma váriavel, para ser usada na próxima janela.
                        final String itemSelected = items[ p_which ].toString();

                        // Finaliza a janela com os apps.
                        p_dialog.dismiss();

                        // Outra janela informando que no Modo App único, somente um app terá permissão de mostrar na
                        // Home.
                        AlertDialog alertDialog = new AlertDialog.Builder( SettingsActivity.this )
                                 .setIcon( android.R.drawable.ic_dialog_alert )
                                 .setTitle( R.string.notice )
                                 .setMessage(
                                          R.string.question_single_app )
                                 .setPositiveButton( android.R.string.ok,
                                          new DialogInterface.OnClickListener()
                                          {
                                             @Override
                                             public void onClick( DialogInterface p_dialog, int p_which )
                                             {
                                                List<AppInfo> appinfo = new ArrayList<AppInfo>( dbh
                                                         .selectAllAppInfo() );

                                                for ( AppInfo appinfo2 : appinfo )
                                                {
                                                   if ( appinfo2.getM_APP_TXT_Label().equals( itemSelected ) )
                                                   {
                                                      try
                                                      {
                                                         // Altera a permissão do App para 1.
                                                         dbh.updatePermissionAppInfo( appinfo2
                                                                  .getM_APP_TXT_Name().toString(),
                                                                  PERMISSION_1 );
                                                         // Altera o value do Parametro para o nome do App
                                                         // selecionado.
                                                         dbh.updateParameter(
                                                                  DbConsts.DbParameterIdValues.PARAM_MODE_SINGLE_APP_ID,
                                                                  appinfo2.getM_APP_TXT_Label().toString() );
                                                         // Mensagem de Sucesso Toast.
                                                         Toast.makeText( SettingsActivity.this,
                                                                  R.string.noticeModeSingleAppSucess,
                                                                  Toast.LENGTH_LONG ).show();
                                                         // Atualiza o Texto do Button.
                                                         checkModeSingleAppButton();
                                                      }
                                                      catch ( Exception e )
                                                      {
                                                         e.printStackTrace();
                                                      }
                                                   }
                                                   else
                                                   {
                                                      try
                                                      {
                                                         dbh.updatePermissionAppInfo( appinfo2
                                                                  .getM_APP_TXT_Name().toString(),
                                                                  PERMISSION_0 );
                                                      }
                                                      catch ( Exception e )
                                                      {
                                                         e.printStackTrace();
                                                      }
                                                   }

                                                }

                                             }
                                          } )
                                 .setNegativeButton( android.R.string.no,
                                          new DialogInterface.OnClickListener()
                                          {
                                             @Override
                                             public void onClick( DialogInterface dialog, int which )
                                             {
                                                dialog.dismiss();
                                             }
                                          } )
                                 .show();
                     }
                  } );

         dialog = builder.create();// AlertDialog dialog; create like this outside onClick
         dialog.show();
      }
   }

   /**
    * Evento de clique que desinstala o app Launcher ATMobile. *
    */
   public void evtBtUninstall( View p_view )
   {
      Intent intent = new Intent( Intent.ACTION_UNINSTALL_PACKAGE );
      intent.setData( Uri.parse( getString( R.string.name_package ) + loadApps() ) );
      startActivity( intent );
   }
}
