package br.com.fernandocostagomes.blacklauncher.util;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import br.com.fernandocostagomes.blacklauncher.objects.AppInfo;

/**
 * Classe responsável por conectar no bd do ATMobile Launcher.
 * 
 * @author fernando.gomes *
 */
public class DbHelper extends SQLiteOpenHelper
{
   /**
    * Constante com o nome do banco de Dados.
    */
   private static final String NOME_BANCO = "BlackLauncher";

   /**
    * Constante com a versão do Banco de Dados.
    */
   private static final int VERSAO_BASE = 1;

   /**
    * Array de string com as querys para criar as tabelas do banco de dados.
    */
   private static final String[] SCRIPT_DATABASE_CREATE = new String[] {
            "CREATE TABLE AppInfo_APP(APP_TXT_Name VARCHAR(30) NOT NULL, APP_TXT_Label VARCHAR(30) NOT NULL, APP_TXT_Permission VARCHAR(1) NOT NULL);",
            "CREATE TABLE Password_PSW(PSW_TXT_Password VARCHAR(20) NOT NULL, PSW_TXT_OldPassword VARCHAR(20) NOT NULL);",
            "CREATE TABLE Parameter_PAR(PAR_NUM_Number VARCHAR(20) NOT NULL, PAR_TXT_Value VARCHAR(30), PAR_TXT_OldValue VARCHAR(30));"
   };

   /**
    * Construtor padrão que recebe um contexto.
    * 
    * @param p_context
    */
   public DbHelper( Context p_context )
   {
      super( p_context, NOME_BANCO, null, VERSAO_BASE );
   }

   /**
    * Metódo onCreate para criar as tabelas no banco de dados.
    */
   @Override
   public void onCreate( SQLiteDatabase p_db )
   {
      String SqlCreateTableAppInfo = "CREATE TABLE AppInfo_APP(APP_TXT_Name VARCHAR(30) NOT NULL, APP_TXT_Label VARCHAR(30) NOT NULL, APP_TXT_Permission VARCHAR(1) NOT NULL);";
      p_db.execSQL( SqlCreateTableAppInfo );
      String SqlCreateTablePassword = "CREATE TABLE Password_PSW(PSW_TXT_Password VARCHAR(20) NOT NULL, PSW_TXT_OldPassword VARCHAR(20) NOT NULL);";
      p_db.execSQL( SqlCreateTablePassword );
      String SqlCreateTablePaarameter = "CREATE TABLE Parameter_PAR(PAR_NUM_Number VARCHAR(20) NOT NULL, PAR_TXT_Value VARCHAR(30), PAR_TXT_OldValue VARCHAR(30));";
      p_db.execSQL( SqlCreateTablePaarameter );
   }

   /**
    * Metódo que atualiza o banco de dados.
    */
   @Override
   public void onUpgrade( SQLiteDatabase p_db, int p_oldVersion, int p_newVersion )
   {
      String SqlCreateTableAppInfo = "DROP TABLE AppInfo_APP";
      p_db.execSQL( SqlCreateTableAppInfo );
      onCreate( p_db );

      String SqlCreateTablePassword = "DROP TABLE Password_PSW";
      p_db.execSQL( SqlCreateTablePassword );
      onCreate( p_db );

      String SqlCreateTableParameter = "DROP TABLE Parameter_PAR";
      p_db.execSQL( SqlCreateTableParameter );
      onCreate( p_db );
   }

   /**
    * Metódo que insere no banco os nomes dos Apps.
    * 
    * @param objeto AppInfo.
    */
   public void insertAppInfo( AppInfo p_appinfo )
   {
      SQLiteDatabase db = getWritableDatabase();

      ContentValues cv = new ContentValues();

      cv.put( "APP_TXT_Name", p_appinfo.getM_APP_TXT_Name().toString() );
      cv.put( "APP_TXT_Label", p_appinfo.getM_APP_TXT_Label().toString() );
      cv.put( "APP_TXT_Permission", p_appinfo.getM_APP_TXT_Permission().toString() );

      db.insert( "AppInfo_APP", null, cv );

      db.close();
   }

   /*
    * Metodo que inseri no banco o parâmetro inexistênte.
    */
   public boolean insertParameter( int p_parameter, String p_parameterValue )
   {
      boolean result = false;

      SQLiteDatabase db = getWritableDatabase();

      try
      {
         ContentValues cv = new ContentValues();
         cv.put( "PAR_NUM_Number", p_parameter );
         cv.put( "PAR_TXT_Value", p_parameterValue );
         cv.put( "PAR_TXT_OldValue", "0" );

         db.insert( "Parameter_PAR", null, cv );

         db.close();

         result = true;
      }
      catch ( Exception p_e )
      {
         p_e.printStackTrace();
      }

      return result;
   }

   /**
    * Metódo que seleciona um App pelo "name" pesquisado.
    * 
    * @param name a ser pesquisado.
    * 
    * @return resultado com o name pesquisado.
    */
   public String selectNameApp( String p_nameapp )
   {
      String nameApp = "";

      SQLiteDatabase db = getReadableDatabase();

      String SqlSelectOneAppInfo = "SELECT * FROM AppInfo_APP WHERE APP_TXT_Name = '"
               + p_nameapp + "';";

      Cursor c = db.rawQuery( SqlSelectOneAppInfo, null );

      if ( c.moveToFirst() )
      {
         nameApp = c.getString( 0 );
      }
      else
      {
         nameApp = "no";
      }
      c.close();

      return nameApp;
   }

   /**
    * Metódo que seleciona um App pelo label ou pelo name..
    * 
    * @param name a ser pesquisado: label; name;
    * 
    * @param p_column resultado que deseja. 1 name- 2 label - 3 permission.
    * 
    * @return resultado com o name pesquisado.
    */
   public String selectAppForType( String p_type, String p_value, int p_column )
   {
      String resultApp = "";

      SQLiteDatabase db = getReadableDatabase();

      String SqlSelectOneAppInfo = "SELECT * FROM AppInfo_APP WHERE " + p_type + " = '" + p_value + "';";

      Cursor c = db.rawQuery( SqlSelectOneAppInfo, null );

      // Verifica se a requisição é label ou name.
      if ( p_type.equals( "label" ) )
      {
         p_column = 0;
      }
      if ( p_type.equals( "name" ) )
      {
         p_column = 1;
      }
      if ( p_type.equals( "permission" ) )
      {
         p_column = 2;
      }

      // Pega o item requisitado.
      if ( c.moveToFirst() )
      {
         resultApp = c.getString( p_column );
      }
      else
      {
         resultApp = "no";
      }
      c.close();

      return resultApp;
   }

   /**
    * Metódo que seleciona a permissão de um determinado App.
    * 
    * @param p_nameapp nome do App que deseja saber sua permissão.
    * 
    * @return permissionApp retorna a permissão do App pesquisado.
    */
   public String selectPermissionApp( String p_nameapp )
   {
      String permissionApp = null;

      SQLiteDatabase db = getReadableDatabase();

      String SqlSelectOneAppInfo = "SELECT * FROM AppInfo_APP permission WHERE APP_TXT_name = '"
               + p_nameapp + "';";

      Cursor c = db.rawQuery( SqlSelectOneAppInfo, null );

      if ( c.moveToFirst() )
      {
         permissionApp = c.getString( 2 );
      }
      else
      {
         permissionApp = "0";
      }
      c.close();

      return permissionApp;
   }

   /**
    * Método que busca a senha no banco de dados.
    * 
    * @param String "password" ou "oldPassword".
    * 
    * @return retorna a senha ou a senha antiga.
    * 
    */
   public String selecPassWord( String p_column )
   {
      String returnPasswordColumn = null;
      int requestCoumn = 0;

      if ( p_column.equals( "password" ) )
      {
         requestCoumn = 0;
      }
      if ( p_column.equals( "oldPassword" ) )
      {
         requestCoumn = 1;
      }

      SQLiteDatabase db = getReadableDatabase();

      String SqlSelectPassword = "SELECT * FROM Password_PSW;";

      Cursor c = db.rawQuery( SqlSelectPassword, null );

      if ( c.moveToFirst() )
      {
         returnPasswordColumn = c.getString( requestCoumn );
      }
      c.close();

      return returnPasswordColumn;
   }

   /**
    * Metódo que seleciona o valor de um parâmetro no banco de dados..
    * 
    * @return valueParameter retorna o valor do parâmetro.
    */
   public String selectParameter( int p_numberParameter )
   {
      String valueParameter = "";

      SQLiteDatabase db = getReadableDatabase();

      String SqlSelectParameter = "SELECT * FROM Parameter_PAR WHERE PAR_NUM_Number = '"
               + p_numberParameter + "';";

      Cursor c = db.rawQuery( SqlSelectParameter, null );

      // String nomeString = c.getString( c.getColumnIndex( "PAR_TXT_Value" ) );

      if ( c.moveToFirst() )
      {
         valueParameter = c.getString( 1 );
         if ( valueParameter == null )
         {
            valueParameter = "nulo";
         }
      }
      return valueParameter;
   }

   /*
    * Metódo da lista de objetos App.
    * 
    * @return listAppInfo retorna a lista de objetos AppInfo.
    */
   public List<AppInfo> selectAllAppInfo()
   {
      List<AppInfo> listAppInfo = new ArrayList<AppInfo>();

      SQLiteDatabase db = getReadableDatabase();

      String SqlSelectAppAppInfo = "SELECT * FROM AppInfo_APP";

      Cursor c = db.rawQuery( SqlSelectAppAppInfo, null );

      if ( c.moveToFirst() )
      {
         do
         {
            AppInfo appInfo = new AppInfo();

            appInfo.setM_APP_TXT_Name( c.getString( 0 ) );
            appInfo.setM_APP_TXT_Label( c.getString( 1 ) );
            appInfo.setM_APP_TXT_Permission( c.getString( 2 ) );

            listAppInfo.add( appInfo );
         } while ( c.moveToNext() );
      }
      db.close();

      return listAppInfo;
   }

   /**
    * Metódo que seleciona o Label de todos os Apps.
    * 
    * @return labelAllAppinfoDB retorna uma lista com todos os labels.
    */
   public List<String> selectLabelAllAppsDB()
   {
      List<String> labelAllAppinfoDB = new ArrayList<String>();
      String label = "";

      SQLiteDatabase db = getReadableDatabase();

      String SqlSelectAllApps = "SELECT * FROM AppInfo_APP";

      Cursor c = db.rawQuery( SqlSelectAllApps, null );

      if ( c.moveToFirst() )
      {
         do
         {
            label = c.getString( 1 );
            labelAllAppinfoDB.add( label );
         } while ( c.moveToNext() );
      }
      db.close();
      return labelAllAppinfoDB;
   }

   /**
    * Metódo que altera as senhas de configuração do Launcher ATMobile.
    * 
    * @param p_typePassword tipo de senha - PSW_TXT_password / PSW_TXT_oldPassword.
    * 
    * @param p_password novo valor.
    */
   public void updatePassword( String p_typePassword, String p_password )
   {
      SQLiteDatabase db = getWritableDatabase();

      String SqlUpdatePassword = "UPDATE Password_PSW set " + p_typePassword + "  = '" + p_password + "';";

      db.execSQL( SqlUpdatePassword );

      db.close();
   }

   /**
    * Metódo que altera a permissão dos Apps.
    * 
    * @param p_name Nome do App.
    * 
    * @param p_permission Novo valor de permissão - 0 / 1.
    */
   public void updatePermissionAppInfo( String p_name, String p_permission )
   {
      SQLiteDatabase db = getWritableDatabase();

      String SqlUpdatePermissionAppInfo = "UPDATE AppInfo_APP set APP_TXT_Permission = '" + p_permission
               + "' WHERE APP_TXT_Name = '"
               + p_name + "';";

      db.execSQL( SqlUpdatePermissionAppInfo );

      db.close();
   }

   /**
    * Método que altera a permissão de todos os Apps de uma só vez.
    * 
    * @param p_valuePermission valor para qual quer alterar 0;1;
    * 
    * @return resultUpdateAllPermissionApp booleando para caso true: sucesso ou false: erro.
    */
   public boolean updateAllAppPermission( String p_valuePermission )
   {
      boolean resultUpdateAllPermissionApp = false;
      SQLiteDatabase db = getWritableDatabase();

      try
      {
         String SqlUpdateAllPermissionApp = "UPDATE AppInfo_APP set APP_TXT_Permission = '" + p_valuePermission + "';";

         db.execSQL( SqlUpdateAllPermissionApp );

         db.close();
         resultUpdateAllPermissionApp = true;
      }
      catch ( Exception e )
      {
         e.printStackTrace();
      }
      return resultUpdateAllPermissionApp;
   }

   /**
    * Metódo que altera parametro.
    * 
    * @param p_name Nome do App.
    * 
    * @param p_permission Novo valor de permissão - 0 / 1.
    */
   public void updateParameter( int p_parameter, String p_valueParameter )
   {
      SQLiteDatabase db = getWritableDatabase();

      // Seleciona o valor atual do parâmetro e inseri no oldValue do parâmetro.
      String valueForOldValue = selectParameter( p_parameter );

      String SqlUpdateParameterOldValue = "UPDATE Parameter_PAR set PAR_TXT_OldValue = '" + valueForOldValue
               + "' WHERE PAR_NUM_Number = '"
               + p_parameter + "';";

      db.execSQL( SqlUpdateParameterOldValue );

      String SqlUpdateParameter = "UPDATE Parameter_PAR set PAR_TXT_Value = '" + p_valueParameter
               + "' WHERE PAR_NUM_Number = '"
               + p_parameter + "';";

      db.execSQL( SqlUpdateParameter );

      db.close();
   }

   /**
    * Metódo que faz um limpa nas tabelas do banco de dados.
    * 
    * @return true / false.
    */
   public boolean dropDB()
   {
      boolean confirmDeleteTable = false;
      try
      {
         SQLiteDatabase db = getWritableDatabase();

         String SqlDropDbAppInfo = "DELETE FROM AppInfo_APP";
         String SqlUpdatePassword = "UPDATE Password_PSW set PSW_TXT_password  = 'blacklauncher2016';";
         String SqlUpdateOldPassword = "UPDATE Password_PSW set PSW_TXT_OldPassword  = 'blacklauncher2016';";

         db.execSQL( SqlDropDbAppInfo );
         db.execSQL( SqlUpdatePassword );
         db.execSQL( SqlUpdateOldPassword );

         db.close();
         confirmDeleteTable = true;
      }
      catch ( Exception p_e )
      {
         p_e.printStackTrace();
      }
      return confirmDeleteTable;
   }
}
