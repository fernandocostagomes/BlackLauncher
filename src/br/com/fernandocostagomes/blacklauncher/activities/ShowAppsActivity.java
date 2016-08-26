package br.com.fernandocostagomes.blacklauncher.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import br.com.fernandocostagomes.blacklauncher.R;
import br.com.fernandocostagomes.blacklauncher.objects.AppInfo;
import br.com.fernandocostagomes.blacklauncher.util.DbHelper;

/**
 * Activity Show Apps, que mostra os aplicativos instalados no Equipamento.
 * 
 * @author fernando.gomes
 */
public class ShowAppsActivity extends Activity
{
   /**
    * Váriavel package manager.
    */
   private PackageManager m_pm;

   /**
    * Lista do Objeto que recebe os apps instalados.
    */
   private List<AppInfo> m_apps;

   /**
    * ListView que exibirá os Apps instalados.
    */
   private ListView m_lv1;

   /**
    * Váriável necessária para alterar a permissão de Bloqueio / Desbloqueio dos Apps.
    */
   private String m_permission = "";

   /**
    * Metódo que pega a lista de Apps e insere no banco com a permissão 0 = Bloqueado.
    */
   private void insertAppInfoPermission()
   {
      DbHelper dbh = new DbHelper( this );

      for ( AppInfo c : m_apps )
      {
         try
         {
            if ( dbh.selectNameApp( c.getM_APP_TXT_Name().toString() ).equals( getString( R.string.permission_no ) ) )
            {
               AppInfo insertappinfo = new AppInfo();

               insertappinfo.m_APP_TXT_Name = c.getM_APP_TXT_Name();
               insertappinfo.m_APP_TXT_Label = c.getM_APP_TXT_Label();
               insertappinfo.m_APP_TXT_Permission = getString( R.string.permission_zero );

               dbh.insertAppInfo( insertappinfo );
            }
            else
            {
            }
         }
         catch ( Exception p_e )
         {
            p_e.printStackTrace();
         }
      }
   }

   /**
    * Metódo que carrega o ListView customizado.
    */
   private void loadListView()
   {
      m_lv1 = ( ListView )findViewById( R.id.lvShowApps1 );
      final DbHelper dbh = new DbHelper( this );

      ArrayAdapter<AppInfo> adapter = new ArrayAdapter<AppInfo>( this, R.layout.activity_list_apps, m_apps )
      {
         @Override
         public View getView( int position, View convertView, ViewGroup parent )
         {

            // Use view holder patern to better performance with list view.
            ViewHolderItem viewHolder = null;

            if ( convertView == null )
            {
               convertView = getLayoutInflater().inflate( R.layout.activity_list_apps, parent, false );

               viewHolder = new ViewHolderItem();
               viewHolder.icon = ( ImageView )convertView.findViewById( R.id.icon );
               viewHolder.label = ( TextView )convertView.findViewById( R.id.label );
               viewHolder.name = ( TextView )convertView.findViewById( R.id.name );
               viewHolder.visibility = ( TextView )convertView.findViewById( R.id.visibility );

               // store holder with view.
               convertView.setTag( viewHolder );
            }
            // get saved holder
            else
            {
               viewHolder = ( ViewHolderItem )convertView.getTag();
            }

            AppInfo appinfo = m_apps.get( position );
            // Display appInfo
            if ( appinfo != null )
            {
               viewHolder.icon.setImageDrawable( appinfo.m_APP_IMG_Icon );
               viewHolder.label.setText( appinfo.m_APP_TXT_Label );
               viewHolder.name.setText( appinfo.m_APP_TXT_Name );
               // busca a permissão de cada app no banco.
               String nameVisibility = dbh.selectPermissionApp( appinfo.m_APP_TXT_Name.toString() );
               if ( nameVisibility.equals( getString( R.string.permission_zero ) ) )
               {
                  viewHolder.visibility.setText( getString( R.string.block ) );
                  viewHolder.visibility.setTextColor( Color.WHITE );
               }
               else
               {
                  viewHolder.visibility.setText( getString( R.string.unblock ) );
                  viewHolder.visibility.setTextColor( Color.RED );
               }
            }

            return convertView;

         }

         final class ViewHolderItem
         {
            ImageView icon;
            TextView label;
            TextView name;
            TextView visibility;
         }
      };

      m_lv1.setAdapter( adapter );
   }

   /**
    * Metódo que carrega a lista de Apps.
    */
   private void loadApps()
   {
      m_pm = getPackageManager();
      m_apps = new ArrayList<AppInfo>();

      Intent i = new Intent( Intent.ACTION_MAIN, null );
      i.addCategory( Intent.CATEGORY_LAUNCHER );

      // LoadApps
      List<ResolveInfo> availableActivities = m_pm.queryIntentActivities( i, 0 );

      for ( ResolveInfo ri : availableActivities )
      {
         AppInfo appinfo = new AppInfo();

         appinfo.m_APP_TXT_Label = ri.loadLabel( m_pm );
         appinfo.m_APP_TXT_Name = ri.activityInfo.packageName;
         appinfo.m_APP_IMG_Icon = ri.activityInfo.loadIcon( m_pm );
         m_apps.add( appinfo );
      }
   }

   /**
    * Metódo que recebe o toque em um App do ListView.
    */
   private void addClickListener()
   {
      final DbHelper db = new DbHelper( this );
      m_lv1.setOnItemClickListener( new OnItemClickListener()
      {

         public void onItemClick( AdapterView<?> parent, View view, final int position, long id )
         {
            m_permission = db.selectPermissionApp( m_apps.get( position ).m_APP_TXT_Name.toString() );

            if ( m_permission.equals( "0" ) )
            {
               m_permission = getString( R.string.unblock_app );
            }
            else
            {
               m_permission = getString( R.string.block_app );
            }

            AlertDialog.Builder dialogo = new AlertDialog.Builder(
                     ShowAppsActivity.this );
            dialogo.setTitle( getString( R.string.config_app ) + m_apps.get( position ).m_APP_TXT_Label.toString() );
            dialogo.setPositiveButton( m_permission,
                     new DialogInterface.OnClickListener()
                     {
                        @Override
                        public void onClick( DialogInterface p_dialog,
                                 int p_which )
                        {
                           // Bloquear APP ou Desbloquear APP.
                           if ( m_permission.equals( getString( R.string.block_app ) ) )
                           {
                              // mandar 1 na permissao do app
                              db.updatePermissionAppInfo( m_apps.get( position ).getM_APP_TXT_Name().toString(),
                                       getString( R.string.permission_zero ) );
                           }
                           else
                           {
                              db.updatePermissionAppInfo( m_apps.get( position ).getM_APP_TXT_Name().toString(),
                                       getString( R.string.permission_one ) );
                           }
                           addClickListener();
                           loadListView();
                        }
                     } );

            dialogo.setNegativeButton( getString( R.string.open ), new DialogInterface.OnClickListener()
            {
               @Override
               public void onClick( DialogInterface p_dialog, int p_which )
               {
                  Intent i = m_pm.getLaunchIntentForPackage( m_apps.get( position ).m_APP_TXT_Name.toString() );
                  ShowAppsActivity.this.startActivity( i );
                  finish();
               }
            } );
            dialogo.show();
         }

      } );
   }

   /**
    * Metódo OnCreate
    */
   @Override
   protected void onCreate( Bundle p_savedInstanceState )
   {
      // TODO Auto-generated method stub
      super.onCreate( p_savedInstanceState );
      setContentView( R.layout.activity_showapps );

      loadApps();
      insertAppInfoPermission();
      loadListView();
      addClickListener();
   }

   /**
    * Ciclo de vida OnResume.
    */
   @Override
   public void onResume()
   {
      super.onResume();
   }

   /**
    * Método que carrega o Menu com as opções.
    */
   @Override
   public boolean onCreateOptionsMenu( Menu p_menu )
   {
      // Inflate the menu; this adds items to the action bar if it is present.
      getMenuInflater().inflate( R.menu.showappsactivity, p_menu );

      return true;
   }

   /**
    * Método que recebe o clique nos itens do Menu.
    */
   @Override
   public boolean onOptionsItemSelected( final MenuItem p_item )
   {
      int id = p_item.getItemId();
      DbHelper dbh = new DbHelper( this );

      // Itemm do Menu para bloquear todos os Apps..
      if ( id == R.id.block_all )
      {
         if ( dbh.updateAllAppPermission( getString( R.string.permission_zero ) ) )
         {
            Toast.makeText( ShowAppsActivity.this, getString( R.string.all_apps_was_block ), Toast.LENGTH_LONG ).show();
         }
         else
         {
            Toast.makeText( ShowAppsActivity.this, getString( R.string.error_try_block_apps ), Toast.LENGTH_LONG )
                     .show();
         }
         ;
         loadListView();
      }

      // Itemm do Menu para desbloquear todos os Apps..
      if ( id == R.id.unblock_all )
      {
         if ( dbh.updateAllAppPermission( getString( R.string.permission_one ) ) )
         {
            Toast.makeText( ShowAppsActivity.this, getString( R.string.all_apps_was_unblock ), Toast.LENGTH_LONG )
                     .show();
         }
         else
         {
            Toast.makeText( ShowAppsActivity.this, getString( R.string.error_try_unblock_apps ), Toast.LENGTH_LONG )
                     .show();
         }
         loadListView();
      }

      if ( id == R.id.unblock_all )
      {
         startActivity( new Intent( getApplicationContext(), SettingsActivity.class ) );
      }

      return super.onOptionsItemSelected( p_item );
   }

   // Metodo que aciona o botão voltar.
   @Override
   public void onBackPressed()
   {
      finish();
      super.onBackPressed();
   }
}
