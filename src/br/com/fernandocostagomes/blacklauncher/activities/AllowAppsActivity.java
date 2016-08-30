package br.com.fernandocostagomes.blacklauncher.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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

   DbHelper m_dbh = new DbHelper( this );

   /**
    * View customizada para cobrir a barra de notificações. *
    */
   private customViewGroup m_view;

   /**
    * PacketManager.
    */
   private PackageManager m_pm;

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
      m_lvAppsOn = ( GridView )findViewById( R.id.lvAllowAppsActivity );

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

      try
      {
         if ( configuration.orientation == Configuration.ORIENTATION_LANDSCAPE )
         {
            m_lvAppsOn.setNumColumns( 5 );
         }
         else
         {
            m_lvAppsOn.setNumColumns( 3 );
         }
      }
      catch ( Exception p_e )
      {
         p_e.printStackTrace();
      }

      m_lvAppsOn.setAdapter( adapter );
   }

   private void checkBlockBarNotifications()
   {

      if ( m_dbh.selectParameter( DbConsts.DbParameterIdValues.PARAM_BAR_BLOCK_UNBLOCK_ID )
               .equals( ClientConsts.BarBlockUnblock.BLOCK ) )
      {
         preventStatusBarExpansion( this );
      }
      {
         m_dbh.updateParameter( DbConsts.DbParameterIdValues.PARAM_BAR_BLOCK_UNBLOCK_ID,
                  ClientConsts.BarBlockUnblock.BLOCK );
      }

   }

   @Override
   protected void onCreate( Bundle savedInstanceState )
   {
      // TODO Auto-generated method stub
      super.onCreate( savedInstanceState );
      setContentView( R.layout.activity_allow_apps );

      loadAppsPermissionOn();

      loadListView();

      checkBlockBarNotifications();
   }

   @Override
   public boolean onCreateOptionsMenu( Menu p_menu )
   {
      getMenuInflater().inflate( R.menu.allow_apps_menu, p_menu );

      return true;
   }

   /**
    * Método que recebe o clique nos itens do Menu.
    */
   @Override
   public boolean onOptionsItemSelected( final MenuItem p_item )
   {
      int id = p_item.getItemId();

      if ( id == R.id.settings )
      {
         startActivity( new Intent( getApplicationContext(), SettingsActivity.class ) );
      }

      if ( id == R.id.allApps )
      {
         startActivity( new Intent( getApplicationContext(), ShowAppsActivity.class ) );
      }

      return super.onOptionsItemSelected( p_item );
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

   private void removeStatusBarExpansion()
   {

   }

   @Override
   public void onBackPressed()
   {
      finish();
      super.onBackPressed();
   }

   @Override
   protected void onResume()
   {
      loadAppsPermissionOn();
      loadListView();
   }

}
