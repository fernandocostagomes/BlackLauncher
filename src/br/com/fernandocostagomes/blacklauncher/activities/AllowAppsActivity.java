package br.com.fernandocostagomes.blacklauncher.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import br.com.fernandocostagomes.blacklauncher.R;
import br.com.fernandocostagomes.blacklauncher.objects.AppInfo;
import br.com.fernandocostagomes.blacklauncher.util.ClientConsts;
import br.com.fernandocostagomes.blacklauncher.util.DbConsts;
import br.com.fernandocostagomes.blacklauncher.util.DbHelper;

public class AllowAppsActivity extends Activity
{

   /**
    * ListView de apps com permissão 1(Desbloqueado para ser aberto).
    */
   private GridView m_lvAppsOn;

   /**
    * Lista de Apps com permissão 1(Desbloqueado para ser aberto).
    */
   private List<AppInfo> m_listAppInfo;

   /*
    * Váriavel para guardar o nome do App que estiver sendo usado para o Mode Single App.
    */
   private String m_nameSingleModeApp = "";

   /**
    * PacketManager.
    */
   private PackageManager m_pm;

   /**
    * Método que lê e carrega os Apps que estão desbloqueados(1 - Mostrados na tela home).
    */
   private void loadAppsPermissionOn()
   {
      DbHelper dbh = new DbHelper( this );

      m_pm = getPackageManager();
      m_listAppInfo = new ArrayList<AppInfo>();

      Intent i = new Intent( Intent.ACTION_MAIN, null );
      i.addCategory( Intent.CATEGORY_LAUNCHER );

      // LoadApps
      List<ResolveInfo> availableActivities = m_pm.queryIntentActivities( i, 0 );

      for ( ResolveInfo ri : availableActivities )
      {
         AppInfo appinfo = new AppInfo();

         // Confere se tem o App ATMobile instalado e já dá a permissão de mostrar na tela principal.
         if ( ri.loadLabel( m_pm ).equals( "Autotrac M." ) )
         {
            if ( dbh.selectParameter( DbConsts.DbParameterIdValues.PARAM_MODE_EVER_SHORTCUT_ATMOBILE_ID ).equals(
                     ClientConsts.ShortCutEverAtMobile.ACTIVED ) )
               dbh.updatePermissionAppInfo( ri.activityInfo.packageName, "1" );
         }

         try
         {
            if ( dbh.selectPermissionApp( ri.activityInfo.packageName ).equals( "1" ) )
            {
               appinfo.m_APP_TXT_Label = ri.loadLabel( m_pm );
               appinfo.m_APP_TXT_Name = ri.activityInfo.packageName;
               appinfo.m_APP_IMG_Icon = ri.activityInfo.loadIcon( m_pm );
               m_listAppInfo.add( appinfo );
            }
         }
         catch ( Exception p_e )
         {
            p_e.printStackTrace();
         }
      }
   }

   /**
    * Método que lê a lista de Aplicativos desbloqueados e carrega no listView.
    */
   private void loadListView()
   {
      // m_lvAppsOn = ( GridView )findViewById( R.id.lvAllowAppsActivity );

      ArrayAdapter<AppInfo> adapter = new ArrayAdapter<AppInfo>( this, R.layout.activity_list_apps_permission_on,
               m_listAppInfo )
      {
         @Override
         public View getView( int p_position, View p_convertView, ViewGroup p_parent )
         {
            // Use view holder patern to better performance with list view.
            ViewHolderItem viewHolder = null;

            if ( p_convertView == null )
            {
               p_convertView = getLayoutInflater().inflate( R.layout.activity_list_apps_permission_on, p_parent,
                        false );

               viewHolder = new ViewHolderItem();
               viewHolder.iconAppsOn = ( ImageView )p_convertView.findViewById( R.id.iconAppsOn );
               viewHolder.labelAppsOn = ( TextView )p_convertView.findViewById( R.id.labelAppsOn );

               // store holder with view.
               p_convertView.setTag( viewHolder );
            }
            // get saved holder
            else
            {
               viewHolder = ( ViewHolderItem )p_convertView.getTag();
            }

            AppInfo appinfo = m_listAppInfo.get( p_position );
            // Display appInfo
            if ( appinfo != null )
            {
               viewHolder.iconAppsOn.setImageDrawable( appinfo.m_APP_IMG_Icon );
               viewHolder.labelAppsOn.setText( appinfo.m_APP_TXT_Label );

               viewHolder.iconAppsOn.setMinimumHeight( 80 );
               viewHolder.iconAppsOn.setMinimumWidth( 80 );
               viewHolder.iconAppsOn.setMaxHeight( 80 );
               viewHolder.iconAppsOn.setMaxWidth( 80 );
            }
            return p_convertView;
         }

         final class ViewHolderItem
         {
            ImageView iconAppsOn;
            TextView labelAppsOn;

         }
      };
      Configuration configuration = getResources().getConfiguration();

      if ( configuration.orientation == Configuration.ORIENTATION_LANDSCAPE )
      {
         m_lvAppsOn.setNumColumns( 5 );
      }
      else
      {
         m_lvAppsOn.setNumColumns( 3 );
      }
      m_lvAppsOn.setAdapter( adapter );
   }

}
