/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package psexp;

/**
 *
 * @author ealonso
 */

public class DefMenu extends SMenu {
  // private attributes
    //private String opciones;
    private String options[];


  // constructor
    public DefMenu( Shell sh )
    {
      super(sh);

      options = new String[9];
      options[0] =   "0: Regresar";
      options[1] =   "1: Salir del SE";
      options[2] =   "2: Por qué la pregunta (reglas evaluándose)";
      options[3] =   "3: Todas las reglas de la Base de Conocimientos";
      options[4] =   "4: Reglas evaluadas (se han cumplido)";
      options[5] =   "5: Reglas evaluadas (se han cumplido o no)";
      options[6] =   "6: Valores de todos los atributos";
      options[7] =   "7: Atributos con valor" ;
      options[8] =   "8: Atributo evaluándose";

    }

  public int Menu() {

      IngresaOpcion Ing;
      int res;
      Ing = new IngresaOpcion("consulta", "CONSULTAS", options, 10);
      return 0;
  }

  public void Reglas(String titulo, int cuales) {
      String texto, sr;
      int res;
      texto = "";
      Regla r;

      for ( int i = 0; i < sh.nr; i++ ) {
          r = sh.r[i];
          sr = "R"+(sh.r[i].ir+1) + ": " + sh.r[i].imp() + "\n\n";
          if ( cuales == 0 )
              texto  = texto + sr;
          else
              if ( cuales == 1 )
                  if ( r.estado == Vals.REG_EVAL )
                      texto  = texto + sr;
                  else
                      ;
              else
                  if ( (r.estado & Vals.REG_YAEVAL ) != 0 ) {
                      res = r.ant.esVerd();
                      if ( cuales == 3 || res == Vals.EXP_VERD )
                          texto  = texto + sr;
                  }

      }
      Muestra( titulo, texto);
  }

  @Override
  public void	muestraAtributos( int cuales ) {
      if ( cuales == 0 )
          Atributos("Todos los Atributos", cuales);
      else
      if ( cuales == 1 )
          Atributos("Atributos con valor", cuales);
      else
          Atributos("Atributo evaluándose", cuales);
  }

    @Override
    public void	muestraReglas( int cuales ) {
        if ( cuales == 0 )
            Reglas("Todas las Reglas", cuales);
        else
        if ( cuales == 1 )
            Reglas("Reglas evaluándose", cuales);
        else
        if ( cuales == 2 )
            Reglas("Reglas que se han cumplido", cuales);
        else
            Reglas("Reglas evaluadas", cuales);

    }

  public void Atributos( String titulo, int cuales)
  {
      Atrib atr;
      String texto, st;
      String at, valor;
      int val, dde;

      texto = "";
      for ( int i = 0; i < sh.na; i++ ) {
        atr = sh.a[i];
        at = atr.obtnom();
        valor = atr.obtvalor();
        val = atr.obtval();
        dde = atr.obtdde();
        st = at + " = " +
                 (val == Vals.VAL_SI ? valor : val == Vals.VAL_NO ? "sin valor" : "evaluandose") +
                  " ("+
                  (val == Vals.VAL_SI ? ( dde == Vals.VAL_REGLA ? ("de regla "+(atr.r.ir+1)) :
                          (dde == Vals.VAL_USR ? "del usuario" : "de un hecho")) : "")+
                  ")" + "\n\n";
        if ( cuales == 0 )
            texto = texto + st;
        else
            if ( cuales == 1 )
                if ( val == Vals.VAL_SI )
                    texto = texto + st;
                else
                    ;
            else
                if ( val == Vals.VAL_BUS )
                    texto = texto + st;
      }
      Muestra( titulo, texto);
  }

  public void Muestra( String titulo, String texto )
  {
      MuestraTexto t;
      t = new MuestraTexto(titulo,texto, 15);
  }

}
