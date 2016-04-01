/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package psexp;

/**
 *
 * @author ealonso
 */

public abstract class Exp {

   public static Shell sh;

   public Exp( Shell s ) {
       sh = s;
   }

   abstract public int   tieneValor() ;
   abstract public int   evalua();
   abstract public int	 esVerd();
   abstract public int	 esUn();
   abstract public String imp();
   abstract public Exp   busatrib( String a );
   abstract public void  situarvalor( int ddonde, Regla rr );
   public void situarvalor( int ddonde ) {
       situarvalor( ddonde, null );
   }

}
