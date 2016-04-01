/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package psexp;

/**
 *
 * @author ealonso
 */
public class TerMen extends Ter {
  // private attributes

  // constructor
  public TerMen(Shell s, Atrib va, String vvalor )
  {
      super(s,va,vvalor);
  }

  public TerMen(Shell s, String na, String vvalor )
  {
      super(s,na,vvalor);
  }

  public int esVerd()
  {
    if ( a.obtval() == Vals.VAL_SI ) {
	   double v1, v2;
	   v1 = Double.valueOf(a.obtvalor());
	   v2 = Double.valueOf(valor);
	   return( v1 < v2 ? Vals.EXP_VERD : Vals.EXP_FALSA );
    }
    else
       return Vals.EXP_NOCALC;
  }

  public int esUn()
  {
     return Vals.TE_TERMEN;
  }

  public String Oper()
  {
      return "<";
  }
}
