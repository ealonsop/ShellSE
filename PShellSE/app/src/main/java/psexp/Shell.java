/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package psexp;

/**
 *
 * @author ealonso
 */

import android.content.Intent;
import android.util.Log;

import com.cixlabs.pshellse.ShellActivity;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class Shell {
  // private attributes

    public static ShellActivity theShellActivity;

  public class AtP {
      public Atrib a;
      public int   r;
  };

  public Atrib a[];
  public Regla r[];
  public AtP ap[];
  public int na, nr, nap;
  public Atrib ppalatrib;
  public String archivo;
  public SMenu shmenu;
  public int salir;
  public int leeratributo;

  public int forward;
  // constructor
  public Shell(ShellActivity theActivity)
  {
    theShellActivity = theActivity;
    a = new Atrib[Vals.MAXATRIB];
    ap = new AtP[Vals.MAXATRIB];
    r = new Regla[Vals.MAXREGLAS];
    for (int i = 0; i< Vals.MAXATRIB; i++)
        ap[i] = new AtP();
    shmenu = new DefMenu(this);
    inicializar();
/*    MuestraInfo inf;
    inf = new MuestraInfo("Shell para Sistemas Expertos\nMéxico-1995 (C++)\nPerú-2011-2015 (Java)\nE.Alonso",
                           "SISTEMA EXPERTO - JAVA", 3 );*/

  }

  public void inicializar()
  {
    na = nr = nap = 0;
    ppalatrib = null;
    archivo = "";
    salir = 0;
    leeratributo = 0;
  }

  void terminar()
  {
      inicializar();
  }

  public Atrib busatrib( String va )
  {
     for (int i=0; i < na; i++ )
       if ( a[i].obtnom().equalsIgnoreCase(va ) )
          return a[i];
     return nuevoatrib( va );
  }

  public Atrib nuevoatrib( String n )
  {
     return ( a[na++] = new Atrib( n ) );
  }

  public Regla nuevaregla( Exp aa, Exp cc )
  {
     Regla newr = new Regla( aa, cc, nr );
     return ( r[nr++] = newr );
  }

  public int    evalua( String na )
  {
      Atrib va = busatrib( na );
      return evalua(va);
  }

  public int	evalua( Atrib va )
  {
     return evaluabwd(va);
  }

  public void resetReglasEval() {
      int i;
      for ( i = 0; i < nr; i++ ) {
          if ( r[i].estado == Vals.REG_EVAL ) {
            r[i].estado = Vals.REG_SINEV;
            Log.i("tt", "reinic regla: " + i);
          }
      }
  }

  public int evaluabwd( Atrib va )
  {
    int i, rr;
    String nom;
    int primeraSV, aEvaluar;

    nom = va.obtnom();
    rr = va.obtval();
    if ( rr == Vals.VAL_SI ) return Vals.VAL_SI;
    if ( rr == Vals.VAL_BUS ) return Vals.VAL_BUS;
    ap[nap++].a = va;
    Log.i("tt","entranado a evaluar: " + va.nom  );

    if ( salir != 0 ) return Vals.VAL_NO;
    aEvaluar = -1;
    primeraSV = -1;
    for ( i = 0; i < nr && aEvaluar == -1; i++ ) {
		   if ( (r[i].busatrib( nom ) != null) && ( ((r[i].obtestado() & Vals.REG_YAEVAL) == 0) ||
                   (r[i].obtestado() == Vals.REG_EVAL && r[i].tieneValorAnt() == Vals.VAL_SI ) )
             )
			 if ( r[i].tieneValorAnt() == Vals.VAL_SI )
			 {
			   aEvaluar = i;
			   ///ojo printf("Encontrada regla sin evaluar y con valor %d (%d) \n", aEvaluar, r[i]->obtestado());
			 }
		     else
				 if ( r[i].obtestado() == Vals.REG_SINEV && primeraSV == -1 )
				     primeraSV = i;
	   }
    if ( aEvaluar == -1 && primeraSV != -1 ) {
		  aEvaluar = primeraSV;
	      ///ojo printf("Encontrada regla sin evaluar y sin valor %d: (%d)\n", aEvaluar, r[aEvaluar]->obtestado());
	   }
	if ( aEvaluar != -1 ) {
        Log.i("tt", "evaluando = " + va.nom + " regla A evaluar " + aEvaluar + " " + r[aEvaluar].imp());
        ap[nap-1].r = aEvaluar;
        rr = r[aEvaluar].evalua();
        if ( rr == Vals.EXP_VERD ) {
		     r[aEvaluar].situarvalor();
             Log.i("tt", "evaluada OK ");
             resetReglasEval();
		    // nap--;
		     return Vals.VAL_SI;
        }
        else {
            Log.i("tt", "fallo " + r[aEvaluar].obtestado());
            return Vals.VAL_NOCALC;
        }
        ///ojo printf("Resultado de evaluar regla %d = %d (%d)\n",  aEvaluar,rr, r[aEvaluar]->obtestado() );
    }
    else {
        Log.i("tt"," a leer " + va.nom );
        va.cambval(Vals.VAL_BUS);
        va.leervalor(this);
        return Vals.VAL_BUS;
    }
  }

  public int CargarHechos( String hechos, char sep )
  {
      Atrib atr;
      String atributo, valor;

      while ( ! hechos.isEmpty() )
      {
          atributo = "";
          do {
              atributo = atributo + hechos.substring(0, 1);
              hechos = hechos.substring(1);
          }
          while ( hechos.charAt(0) != sep );
          hechos = hechos.substring(1);

          valor = "";
          do {
              valor = valor + hechos.substring(0, 1);
              hechos = hechos.substring(1);
          }
          while ( !hechos.isEmpty() && hechos.charAt(0) != sep );
          if ( !hechos.isEmpty() )
              hechos = hechos.substring(1);
          atr = this.busatrib(atributo);
          atr.cambvalor(valor, Vals.VAL_HECHO,null,this);
      }
      return 1;
  }

  public int CargarHechos( String hechos )
  {
      return CargarHechos( hechos, '/' );
  }

  public String ObtenerAtributos( char sep )
  {
      String hechos, separador;
      hechos = "";
      separador = String.valueOf(sep);
      for (int i = 0; i < na; i++ )
      {
          if ( a[i].obtval() != Vals.VAL_NO && ! a[i].obtvalor().isEmpty() )
          {
              String atributo = a[i].obtnom();
              String valor = a[i].obtvalor();
              hechos = hechos + atributo + separador + valor + separador;
          }
      }
      return hechos;
  }

  public String ObtenerAtributos()
  {
      return ObtenerAtributos('/');
  }
  
  public int Ejecutar()
  {
        String aux;
        Atrib atr;
        atr = busatrib( "OBJETIVO");

        if ( atr.obtval() == Vals.VAL_NO ) {
           new MuestraInfo("Falta el OBJETIVO", "ERROR", 0);
           return 1;
        }
        else {
            aux = atr.obtvalor();
            atr = busatrib(aux);
            evalua(atr);
            return salir;
        }
  }



    //este es el q se usa
    public  int Cargar( File bc )
    {
        Analizador A;

        A = new Analizador();
        try {
            return A.Cargar(this, bc);
        }
        catch ( IOException e) {
            return 0;
        }
    }

    //este es el q se usa
    public  int Cargar( InputStream bc )
    {
        Analizador A;

        A = new Analizador();
        try {
            return A.Cargar(this, bc);
        }
        catch ( IOException e) {
            return 0;
        }
    }

  public int	menu() {
      return shmenu.Menu();
  }

}
