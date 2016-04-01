/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package psexp;

/**
 *
 * @author ealonso
 */
public class ExpAnd extends Exp {
  // private attributes
  Exp e[];
  int ce;

  // constructor
  public ExpAnd( Shell s, Exp e1, Exp e2 )
  {
      super(s);
      e = new Exp[2];
      e[0] = e1;
      e[1] = e2;
      ce = 2;
  }

  public int tieneValor()
  {
    for ( int i = 0; i < ce; i++ )
      if ( e[i].tieneValor() != Vals.VAL_SI )
          return Vals.VAL_NO;
    return Vals.VAL_SI;
  }

  public int evalua()
  {
   int r;
   for ( int i = 0; i < ce; i++ ) {
      r = e[i].evalua();
      if ( r == Vals.VAL_NO )
         return Vals.VAL_NO;
      if ( r == Vals.VAL_SI )
         if ( e[i].esVerd() == Vals.EXP_VERD )
	         continue;
         else
	         return Vals.VAL_SI;
      if ( r == Vals.VAL_NOCALC || r == Vals.VAL_BUS )
         return Vals.VAL_NOCALC;
   }
   return Vals.VAL_SI;
  }

  public int esVerd()
  {
    int r;
    for ( int i = 0; i < ce; i++ ) {
      r = e[i].esVerd();
      if ( r == Vals.EXP_FALSA )
         return Vals.EXP_FALSA;
      if ( r == Vals.EXP_VERD )
         continue;
      if ( r == Vals.EXP_NOCALC )
         return Vals.EXP_NOCALC;
    }
    return Vals.EXP_VERD;
  }

  public int esUn()
  {
    return Vals.TE_AND;
  }

  public String imp()
  {
    //printf("(");
    String res;
    res = "";
    for (int i = 0; i < ce; i++ ) {
      res = res +  e[i].imp();
      if ( i < ce-1 )
        res = res + " Y ";
      //printf( " y " );
    }
    //printf(")");
    return res;
  }

  public Exp busatrib( String va )
  {
   Exp aux;
   for (int i = 0; i < ce; i++ ) {
      aux = e[i].busatrib( va );
      if ( aux != null )
         return aux;
   }
   return null;
  }

  public void situarvalor( int ddonde, Regla rr )
  {
    for (int i = 0; i < ce; i++ )
      e[i].situarvalor( ddonde, rr );
  }

}


