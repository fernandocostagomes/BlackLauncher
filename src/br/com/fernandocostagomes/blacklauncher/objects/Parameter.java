package br.com.fernandocostagomes.blacklauncher.objects;

/**
 * Classe que representa um Par�metro.
 * 
 * Os s�o necess�rios para configurar algumas caracteristicas do AppLauncher.
 * 
 * @author fernando.gomes
 */
public class Parameter
{
   /** N�mero do Par�metro. */
   private int m_PAR_NUM_Number;
   /** Valor do par�metro */
   private String m_PAR_TXT_Value;
   /** Valor antigo do par�metro */
   private String m_PAR_TXT_OldValue;

   /**
    * Retorna o N�mero do Par�metro.
    * 
    * @return Retorna o n�mero do par�metro.
    */
   public int getPAR_NUM_Number()
   {
      return m_PAR_NUM_Number;
   }

   /**
    * Configura o n�mero do par�metro.
    * 
    * @param p_pAR_NUM_Number � o n�mero do par�metro.
    */
   public void setPAR_NUM_Number( int p_pAR_NUM_Number )
   {
      m_PAR_NUM_Number = p_pAR_NUM_Number;
   }

   /**
    * Retorna o valor do par�metro.
    * 
    * @return Retorna o valor do par�metro.
    */
   public String getPAR_TXT_Value()
   {
      return m_PAR_TXT_Value;
   }

   /**
    * Configura o valor do par�metro.
    * 
    * @param p_pAR_TXT_Value � o valor do par�metro.
    */
   public void setPAR_TXT_Value( String p_pAR_TXT_Value )
   {
      m_PAR_TXT_Value = p_pAR_TXT_Value;
   }

   /**
    * Retorna o valor antigo do par�metro.
    * 
    * @return Retorna o valor antigo do par�metro.
    */
   public String getPAR_TXT_OldValue()
   {
      return m_PAR_TXT_OldValue;
   }

   /**
    * Configura o valor antigo do par�metro.
    * 
    * @param p_pAR_TXT_OldValue � o valor antigo do par�metro.
    */
   public void setPAR_TXT_OldValue( String p_pAR_TXT_OldValue )
   {
      m_PAR_TXT_OldValue = p_pAR_TXT_OldValue;
   }

}
