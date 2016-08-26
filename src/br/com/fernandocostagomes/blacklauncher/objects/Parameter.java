package br.com.fernandocostagomes.blacklauncher.objects;

/**
 * Classe que representa um Parâmetro.
 * 
 * Os são necessários para configurar algumas caracteristicas do AppLauncher.
 * 
 * @author fernando.gomes
 */
public class Parameter
{
   /** Número do Parâmetro. */
   private int m_PAR_NUM_Number;
   /** Valor do parâmetro */
   private String m_PAR_TXT_Value;
   /** Valor antigo do parâmetro */
   private String m_PAR_TXT_OldValue;

   /**
    * Retorna o Número do Parâmetro.
    * 
    * @return Retorna o número do parâmetro.
    */
   public int getPAR_NUM_Number()
   {
      return m_PAR_NUM_Number;
   }

   /**
    * Configura o número do parâmetro.
    * 
    * @param p_pAR_NUM_Number é o número do parâmetro.
    */
   public void setPAR_NUM_Number( int p_pAR_NUM_Number )
   {
      m_PAR_NUM_Number = p_pAR_NUM_Number;
   }

   /**
    * Retorna o valor do parâmetro.
    * 
    * @return Retorna o valor do parâmetro.
    */
   public String getPAR_TXT_Value()
   {
      return m_PAR_TXT_Value;
   }

   /**
    * Configura o valor do parâmetro.
    * 
    * @param p_pAR_TXT_Value é o valor do parâmetro.
    */
   public void setPAR_TXT_Value( String p_pAR_TXT_Value )
   {
      m_PAR_TXT_Value = p_pAR_TXT_Value;
   }

   /**
    * Retorna o valor antigo do parâmetro.
    * 
    * @return Retorna o valor antigo do parâmetro.
    */
   public String getPAR_TXT_OldValue()
   {
      return m_PAR_TXT_OldValue;
   }

   /**
    * Configura o valor antigo do parâmetro.
    * 
    * @param p_pAR_TXT_OldValue é o valor antigo do parâmetro.
    */
   public void setPAR_TXT_OldValue( String p_pAR_TXT_OldValue )
   {
      m_PAR_TXT_OldValue = p_pAR_TXT_OldValue;
   }

}
