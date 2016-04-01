/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package psexp;

/**
 *
 * @author ealonso
 */
public class Ter extends Exp {
  // private attributes
  protected String valor;
  protected Atrib a;

  // constructor
  public Ter(Shell s, Atrib va, String vvalor )
  {
      super(s);
      a = va;
      valor = vvalor + "";
  }

  public Ter(Shell s, String na, String vvalor )
  {
      super(s);
      a = sh.busatrib(na);
      valor = vvalor + "";
  }

  public int tieneValor()
  {
      return a.obtval();
  }

  public int esVerd()
  {
    if ( a.obtval() == Vals.VAL_SI )
       return( a.obtvalor().compareToIgnoreCase(valor) == 0 ?
                Vals.EXP_VERD : Vals.EXP_FALSA );
    return Vals.EXP_NOCALC;
  }

  public int evalua()
  {
    switch ( a.obtval() ) {
      case Vals.VAL_SI: return Vals.VAL_SI;
      case Vals.VAL_NO: return sh.evalua( a );
      case Vals.VAL_BUS: return Vals.VAL_NOCALC;
    }
    return Vals.EXP_NOCALC;
  }

  public int esUn()
  {
    return Vals.TE_TER;
  }

  public String imp()
  {
    return a.obtnom() + Oper() +  valor;
    //System.out.print(a.obtnom() + Oper() +  valor );
  }
  
  public String Oper()
  {
      return "=";
  }

  public Exp busatrib( String va )
  {
    if ( a.obtnom().equalsIgnoreCase(va) )
       return this;
    else
       return null;
  }

  public void situarvalor( int ddonde, Regla rr )
  {
    a.cambvalor( valor, ddonde, rr, sh);
  }

}
