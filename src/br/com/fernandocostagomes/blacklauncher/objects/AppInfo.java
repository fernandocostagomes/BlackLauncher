package br.com.fernandocostagomes.blacklauncher.objects;

import android.graphics.drawable.Drawable;

/**
 * Classe que representa um App.
 * 
 * Os Apps serão scaneados e inseridos na base de dados do Launcher.
 * 
 * @author fernando.gomes
 */
public class AppInfo
{
   /** Label do pacote do App. */
   public CharSequence m_APP_TXT_Label;
   /** Nome do pacote do App. */
   public CharSequence m_APP_TXT_Name;
   /** Icone do App. */
   public Drawable m_APP_IMG_Icon;
   /** Permissão dada ao App no Launcher. */
   public CharSequence m_APP_TXT_Permission;

   /**
    * Retorna o label do App.
    * 
    * @return m_APP_TXT_Label Retorna o label do App.
    */
   public CharSequence getM_APP_TXT_Label()
   {
      return m_APP_TXT_Label;
   }

   /**
    * Configura o label do App.
    * 
    * @param m_APP_TXT_Label é o label do App.
    */
   public void setM_APP_TXT_Label( CharSequence m_APP_TXT_Label )
   {
      this.m_APP_TXT_Label = m_APP_TXT_Label;
   }

   /**
    * Retorna o name do App.
    * 
    * @return m_APP_TXT_Name Retorna o name do App.
    */
   public CharSequence getM_APP_TXT_Name()
   {
      return m_APP_TXT_Name;
   }

   /**
    * Configura o name do App.
    * 
    * @param m_APP_TXT_Name é o name do App.
    */
   public void setM_APP_TXT_Name( CharSequence m_APP_TXT_Name )
   {
      this.m_APP_TXT_Name = m_APP_TXT_Name;
   }

   /**
    * Retorna o Icone do App.
    * 
    * @return m_APP_IMG_Icon Retorna o icone do App.
    */
   public Drawable getM_APP_IMG_Icon()
   {
      return m_APP_IMG_Icon;
   }

   /**
    * Configura o Icone do App.
    * 
    * @param m_APP_IMG_Icon é o Icone do App.
    */
   public void setM_APP_IMG_Icon( Drawable m_APP_IMG_Icon )
   {
      this.m_APP_IMG_Icon = m_APP_IMG_Icon;
   }

   /**
    * Retorna a permissão do App.
    * 
    * @return m_APP_TXT_Permission Retorna a permissão do App.
    */
   public CharSequence getM_APP_TXT_Permission()
   {
      return m_APP_TXT_Permission;
   }

   /**
    * Configura a permissão do App.
    * 
    * @param m_APP_TXT_Permission é a permissão para o App.
    */
   public void setM_APP_TXT_Permission( CharSequence m_APP_TXT_Permission )
   {
      this.m_APP_TXT_Permission = m_APP_TXT_Permission;
   }
}
