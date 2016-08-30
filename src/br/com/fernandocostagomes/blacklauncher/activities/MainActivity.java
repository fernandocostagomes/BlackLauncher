package br.com.fernandocostagomes.blacklauncher.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import br.com.fernandocostagomes.blacklauncher.R;
import br.com.fernandocostagomes.blacklauncher.util.Chronometer;
import br.com.fernandocostagomes.blacklauncher.util.ClientConsts;
import br.com.fernandocostagomes.blacklauncher.util.DbConsts;
import br.com.fernandocostagomes.blacklauncher.util.DbHelper;

public class MainActivity extends Activity
{

   // Variavel int para contar os toques na tela.
   private int m_numberTouch = 0;
   // Contador de segundos para o desbloqueio da tela.
   private long m_seconds = 0;

   /**
    * View customizada para cobrir a barra de notificações. *
    */
   private customViewGroup m_view;

   DbHelper dbh = new DbHelper( this );

   Chronometer m_chrono = new Chronometer();

   /*
    * Valor do Parâmetro do bloqueio da tecla Power.
    */
   private boolean m_valueParamPowerKey;

   /**
    * Classe interna com um grupo de view customizado para fazer o bloqueio da barra de notificações.
    */
   public class customViewGroup extends ViewGroup
   {
      @Override
      protected void onLayout( boolean changed, int p_l, int p_t, int p_r, int p_b )
      {

      }

      public customViewGroup( Context p_context )
      {
         super( p_context );
      }

      @Override
      public boolean onInterceptTouchEvent( MotionEvent p_ev )
      {
         Log.v( "customViewGroup", "**********Intercepted" );
         return true;
      }
   }

   /**
    * Metódo que verifica se a barra de notificações tem que ser bloqueada ou não.
    * @return
    */
   private boolean isBarBlock()
   {
      boolean result = false;

      // Copnsulta no banco se a barra de notificações está bloqueada ou desbloqueada.
      String paramValueBarBlock = dbh.selectParameter( DbConsts.DbParameterIdValues.PARAM_BAR_BLOCK_UNBLOCK_ID );

      if ( paramValueBarBlock.equals( ClientConsts.BarBlockUnblock.BLOCK ) )
      {
         preventStatusBarExpansion( this );
         result = true;
      }
      if ( paramValueBarBlock.equals( "" ) )
      {
         dbh.insertParameter( DbConsts.DbParameterIdValues.PARAM_BAR_BLOCK_UNBLOCK_ID,
                  ClientConsts.BarBlockUnblock.BLOCK );
      }

      return result;
   }

   /*
    * Método que verifica o bloqueio da tecla Power.
    */
   private void checkPowerKey()
   {
      DbHelper dbh = new DbHelper( this );

      if ( dbh.selectParameter( DbConsts.DbParameterIdValues.PARAM_POWER_BUTTON_HOME_SCREEN_ID ).equals( "" ) )
      {
         m_valueParamPowerKey = false;
         dbh.insertParameter( DbConsts.DbParameterIdValues.PARAM_POWER_BUTTON_HOME_SCREEN_ID,
                  ClientConsts.PowerButtonHomeScreen.DESACTIVED );
      }

      // Pesquisa o parâmetro e trata.
      if ( dbh.selectParameter( DbConsts.DbParameterIdValues.PARAM_POWER_BUTTON_HOME_SCREEN_ID ).equals(
               ClientConsts.PowerButtonHomeScreen.ACTIVED ) )
      {
         m_valueParamPowerKey = true;
      }
      else
      {
         m_valueParamPowerKey = false;
      }
   }

   @Override
   protected void onCreate( Bundle p_savedInstanceState )
   {
      super.onCreate( p_savedInstanceState );
      setContentView( R.layout.activity_main );

      // Método para bloquear a barra de notificações.
      isBarBlock();

      // Metódo que verifica se a tecla power está bloqueada.
      checkPowerKey();

   }

   /**
    * Método que faz o bloqueio da barra de notificações criando uma barra invisível por cima no instante que ela é
    * chamada.
    */
   public void preventStatusBarExpansion( Context p_context )
   {
      WindowManager manager = ( ( WindowManager )p_context.getApplicationContext()
               .getSystemService( Context.WINDOW_SERVICE ) );

      Activity activity = ( Activity )p_context;
      WindowManager.LayoutParams localLayoutParams = new WindowManager.LayoutParams();
      localLayoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
      localLayoutParams.gravity = Gravity.TOP;
      localLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |

      // O objectivo é permitir a notificação para receber eventos de toque de janelas
               WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |

               WindowManager.LayoutParams.FLAG_FULLSCREEN |

               // Chama sobre estatuto bar
               WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;

      localLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
      // http://stackoverflow.com/questions/1016896/get-screen-dimensions-in-pixels
      int resId = activity.getResources().getIdentifier( "status_bar_height", "dimen", "android" );
      int result = 0;
      if ( resId > 0 )
      {
         result = activity.getResources().getDimensionPixelSize( resId );
      }

      localLayoutParams.height = result;

      localLayoutParams.format = PixelFormat.TRANSPARENT;

      m_view = new customViewGroup( p_context );

      manager.addView( m_view, localLayoutParams );
   }

   // Evento de toques na tela para abrir a tela de apps.

   public boolean onTouchEvent( MotionEvent p_event )
   {
      if ( m_numberTouch == 0 )
      {
         m_chrono.startChonometer();
      }
      if ( m_numberTouch == 9 )
      {
         m_seconds = m_chrono.getChonometer();
         if ( m_seconds < 5 )
         {
            startActivity( new Intent( getApplicationContext(), AllowAppsActivity.class ) );
         }
         else
         {
            m_numberTouch = 0;
         }
      }
      else
      {
         if ( m_seconds > 5 )
         {
            m_seconds = 0;
            m_numberTouch = 0;
         }
         else
         {
            m_numberTouch++;
         }
      }
      return false;
   }

   /**
    * Ação do botão voltar, ao pressionar não irá deixar sair da Aplicação.
    *
    */
   @Override
   public void onBackPressed()
   {

   }

   /*
    * Método que itercepta quando a tecla power for acionada.
    * (non-Javadoc)
    * 
    * @see android.app.Activity#dispatchKeyEvent(android.view.KeyEvent)
    */
   @Override
   public boolean dispatchKeyEvent( KeyEvent p_event )
   {
      if ( m_valueParamPowerKey == true )
      {
         if ( p_event.getKeyCode() == KeyEvent.KEYCODE_POWER )
         {
            Log.i( "", "Dispath evento power" );

            Intent closeDialog = new Intent( Intent.ACTION_CLOSE_SYSTEM_DIALOGS );
            sendBroadcast( closeDialog );
            return true;
         }
      }
      return super.dispatchKeyEvent( p_event );
   }
}
