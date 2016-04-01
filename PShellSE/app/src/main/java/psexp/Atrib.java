

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package psexp;

/**
 *
 * @author ealonso
 */
public class Atrib {
  // private attributes
  public String nom, valor, preg, valores;
  int val, donde;
  Regla r;
  // constructor
  public Atrib( String n )
  {
     nom =  n + "";
     valor = "";
     preg = "Valor para '" + nom + "' ";
     valores = "";
     val = Vals.VAL_NO;
     r = null;
  }

  public void	cambval( int nval ) {
       val = nval;
  }

  public int	obtval() {
      return val;
  }

  public int	obtdde() {
       return donde;
  }

  public int	cambvalor( String nval, int ddonde )
  {
      return cambvalor(nval,ddonde,null,null);
  }

  public String obtvalor() {
      return valor;
  }
  public String obtnom() {
      return nom;
  }

  public String obtpreg() {
      return preg;
  }
  public void   cambpreg( String p ) {
      int i, max;
      String aux;
      preg = p + "";
      aux = "";
      max = 60;
      while ( preg.length() > max ) {
          i = 0;
          while ( preg.charAt(max-i) != ' ' )
              i++;
          aux = aux + preg.substring(0, max-i) + "\n";
          preg = preg.substring(max-i);
      }
      aux = aux+preg+"\n ";
      preg = aux;
  }

  public String obtvalores() {
      return valores;
  }
  public void   cambvalores( String v ) {
      valores = v + "";
  }

  public int cambvalor( String nval, int ddonde, Regla rr, Shell sh )
  {
      char c;

      if ( nval.isEmpty() )
         c = '\0';
      else
         c = nval.charAt(0);

      if ( c == '#' || c == '@' )
      {
	      if ( c == '@' ) {
	         double vr = Double.valueOf(nval.substring(1)).doubleValue();
             double v;
	         if ( valor.isEmpty() )
                 v = 0;
             else
                 v = Double.valueOf(valor).doubleValue();
             valor = String.valueOf(vr+v);
	      }
	      else {
	         int vr = Integer.valueOf(nval.substring(1)).intValue();
             int v;
	         if ( valor.isEmpty() )
                 v = 0;
             else
                 v = Integer.valueOf(valor).intValue();
             valor = String.valueOf(vr+v);
	      }
      }
      else
          if ( c == '=' ) {
              String natrib = nval.substring(1);
              Atrib aa = sh.busatrib(natrib);
              valor = aa.obtvalor() + "";
          }
          else
             valor = nval + "";
      if ( nval.isEmpty() )
         val = Vals.VAL_NO;
      else 
         val = Vals.VAL_SI;
      donde = ddonde;
      r = rr;
      return val;
   }

   public int leervalor(Shell sh)
   {
   	 //BufferedReader br;
     String options[];

     if ( sh.salir != 0 ) return Vals.VAL_NO;
     //br = new BufferedReader(new InputStreamReader(System.in));
     String aux;
      //  printf("\nValor para '%s':", nom );
        //System.out.println("\n\n" + preg+"\n");

        if ( ! valores.isEmpty() )
        {
	       int n = valores.codePointAt(0);
           int p;
	       String aux2;
	       int n1, i, res;
	  
           p = n+1;
           options = new String[n+1];
           options[0] = "0 .- ?";
           //System.out.println("0 .- ?");
	       for (i = 1; i <= n; i++ )
	       {
		     n1 = valores.codePointAt(i);
             aux2 = valores.substring(p, p+n1);
		     p += n1;
             options[i] = i + " .- " + aux2;
	       }
           new IngresaOpcion( nom, preg, options, 5);
        }
        else {
           options = new String[1];
           options[0] = "";
           new IngresaOpcion( nom, preg, options, 6 );
        }
     return Vals.VAL_NO;
   }

}

