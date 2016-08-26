package br.com.fernandocostagomes.blacklauncher.util;

/**
 * Classe que cont�m as defini��es de constantes utilizadas no banco de dados.
 * 
 * @author fernando.gomes
 */
public final class DbConsts
{

   public static final class DbParameterIdValues
   {
      /** Desabilita o bot�o power na tela home. (=0 normal, =1 desativado). */
      public static final int PARAM_POWER_BUTTON_HOME_SCREEN_ID = 10;

      /** Par�metro para ativar o Modo App �nico - se v�zio = Desativado - Preenchido dever� ter o nome do App.). */
      public static final int PARAM_MODE_SINGLE_APP_ID = 11;

      /** Par�metro para ativar o Modo App Autotrac Mobile sempre na Tela Principal). */
      public static final int PARAM_MODE_EVER_SHORTCUT_ATMOBILE_ID = 12;

      /** Par�metro para configurar o valor do tempo para o Modo App �nico. */
      public static final int PARAM_TIME_SIGLE_APP_TIME_ID = 13;

      /** Par�metro para configurar o hor�rio escolhido para o reboot programado. */
      public static final int PARAM_REBOOT_PROGRAMED_ID = 14;

      /** Par�metro com a senha padr�o escolhida pelo cliente. */
      public static final int PARAM_PASSWORD_DEFAULT_ID = 15;

      /** Par�metro com a senha padr�o escolhida pelo cliente. */
      public static final int PARAM_BAR_BLOCK_UNBLOCK_ID = 16;
   }

}
