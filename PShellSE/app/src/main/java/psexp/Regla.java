package psexp;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import android.util.Log;

/**
 *
 * @author ealonso
 */
public class Regla {
  // private attributes
  public Exp ant, cons;
  public int estado;
  public int ir;

  // constructor
  public Regla( Exp aa, Exp cc, int i )
  {
    ant = aa;
    cons = cc;
    ir = i;
    estado = Vals.REG_SINEV;
  }

  public String imp()
  {
// printf("Regla: ");
    String res;
    res = ant.imp() + " -> " + cons.imp();
/*    ant.imp();
    System.out.print(" -> ");
    cons.imp();
    System.out.print("\n");*/
    return res;
}

   public Exp  busatrib( String va )
   {
     return cons.busatrib( va );
   }

   public void situarvalor()
   {
     cons.situarvalor( Vals.VAL_REGLA, this );
   }

   public int obtestado()
   {
      return estado;
   }

   public void situarestado( int nest )
   {
     estado = nest;
   }

   public int tieneValorAnt()
   {
     return ant.tieneValor();
   }

   public int evalua()
   {
     int r;

//     if ( estado == Vals.REG_EVAL ) {        // evaluando esta regla
  //       return Vals.EXP_NOCALC;
    // }
     if ( ant == null )
        return Vals.EXP_VERD;

//     if ( estado == Vals.REG_SINEV ) {
//        estado = Vals.REG_EVAL;
        if ( cons.tieneValor() == Vals.VAL_SI ) {
            return ant.esVerd();
        }
        else {
            r = Vals.VAL_NOCALC;
            if ( estado == Vals.REG_SINEV ) {
              estado = Vals.REG_EVAL;
              r = ant.evalua();
          }
          else
            if ( estado == Vals.REG_EVAL && ant.tieneValor() == Vals.VAL_SI ) {
                r = ant.evalua();
            }

          if ( r == Vals.VAL_NOCALC || r == Vals.VAL_BUS ) {
             //estado = Vals.REG_SINEV;
             estado = Vals.REG_EVAL;
             return Vals.EXP_NOCALC;
          }
            else {
               Log.i("tt","valor de r = "+r);
               estado = Vals.REG_OK | Vals.REG_YAEVAL;
               return ant.esVerd();
            }
        }
    }

}

