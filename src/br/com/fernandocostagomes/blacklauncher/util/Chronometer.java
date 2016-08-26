package br.com.fernandocostagomes.blacklauncher.util;

/**
 * Classe que gerencia o Cronometro que desbloqueia a tela principal.
 * @author fernando.gomes
 *
 */
public class Chronometer
{
   private long m_start = 0;

   // Construtor - tamb�m ativa o cronometro.
   public void startChonometer()
   {
      m_start = System.currentTimeMillis();
   }

   // retorna tempo em segundos
   // n�o interrompe o cronometro, pode ser chamado v�rias vezes
   public int getChonometer()
   {
      long mili = System.currentTimeMillis() - m_start;
      return ( int )Math.round( mili / 1000.0 );
   }
}
