/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package psexp;

/**
 *
 * @author ealonso
 */

import android.os.Environment;

import java.io.*;
import java.net.*;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

import jxl.*;
import jxl.read.biff.BiffException;


public class Analizador {
    
    Workbook w;
    Sheet sheet;
    int sh_row, sh_col;
    int obj_row;
    int estado;
    boolean primero;
    
  // private attributes
    static final int  TK_REGLA	= 1;
    static final int TK_ENT	= 2;
    static final int TK_TREGLA =	3;
    static final int TK_IG	=	4;
    static final int TK_AND	=	5;
    static final int TK_OR	=	6;
    static final int TK_VALOR	= 7;
    static final int TK_ATRIB	= 8;
    static final int TK_PIZQ	=	9;
    static final int TK_PDER	=	10;
    static final int TK_PREG	=	11;
    static final int TK_COMA	=	12;
    static final int TK_MAY	=	13;
    static final int TK_MEN	=	14;
    static final int TK_MAYIG	= 15;
    static final int TK_MENIG	= 16;
    static final int TK_DIF	=	17;
    static final int TK_END	=	18;

    int	token;
    int	nl;
    int	err;
    String	valor;

    String  fich;
    String  linea;
    FileReader fh;
    InputStream in;
    BufferedReader bf;

    Exp  ultter;
    Exp  ultexp;
    Exp  ultant;
    Exp  ultcons;

    Shell sh;

  // constructor
    public Analizador()
    {
    }

    //este es el que se está usando
    int Cargar( Shell s, File archivo ) throws IOException
    {
        sh = s;

        nl = 1;
        err = 0;
        linea = " ";
        ultter = null;
        ultexp = null;
        ultant = null;
        ultcons = null;

        try {
            WorkbookSettings ws = new WorkbookSettings();
            ws.setEncoding("Cp1252");
            w = Workbook.getWorkbook(archivo,ws);
            int k = bc();
            return k == 0 ? 1 : 0;
        }
        catch (BiffException e) {
            Error(3);
        }
        catch (IOException e) {
            Error(20);
        }
        return 0;
    }

    //este es el que se está usando
    int Cargar( Shell s, InputStream archivo ) throws IOException
    {
        sh = s;

        nl = 1;
        err = 0;
        linea = " ";
        ultter = null;
        ultexp = null;
        ultant = null;
        ultcons = null;

        try {
            WorkbookSettings ws = new WorkbookSettings();
            ws.setEncoding("Cp1252");
            w = Workbook.getWorkbook(archivo,ws);
            int k = bc();
            return k == 0 ? 1 : 0;
        }
        catch (BiffException e) {
            Error(3);
        }
        catch (IOException e) {
            Error(20);
        }
        return 0;
    }

    
    void Error( int e )
    {
/*      static char * msg[] = {
  NULL,
  "Esperase el caracter >",
  "Valor sin terminar, Esperase el caracter '",
  "Imposible abrir el archivo con la Base de Conociminentos",
  "Esperase -> entre antecedentes y consecuentes",
  "Esperase el caracter ; al terminar la regla",
  "Esperase el caracter )",
  "Las expresiones deben ser todas de tipo Y o de tipo O",
  "Esperase Atributo",
  "Esperase el operador =,<,>,<=,>=,<> despues del atributo",
  "Esperase el caracter ' que inicia el valor",
  "Esperase una coma",
 };*/
        if ( err != 0 ) return;
//        System.out.println("Error numero: " + e + "  en la linea " + nl);
        MuestraInfo inf;
        String msg = "Error numero: " + e + "  fila = " + (sh_row+1) + " columna = " + (sh_col+1);
        inf = new MuestraInfo(msg,"ERROR", 1 );
        err = e;
    }

    int Scanner()
    {
       char c;

       if ( !SaltaEspacio() )
          return (token = TK_END);
       valor = Character.toString(Primero());
       switch (  Primero() ) {
           case ';':
               AvanzaCaracter();
               return (token=TK_TREGLA);
           case '-':
               AvanzaCaracter();
               if ( Primero() == '>' ) {
                   valor = valor + ">";
                   AvanzaCaracter();
		           return (token=TK_ENT);
	           }
	           Error( 1 );
	           return (token = 0);
           case '(' :
               AvanzaCaracter();
               return (token=TK_PIZQ);
           case ')' :
               AvanzaCaracter();
               return (token=TK_PDER);
           case '%' :
               AvanzaCaracter();
               return (token=TK_PREG);
           case '=' :
               AvanzaCaracter();
               return (token=TK_IG);
           case ',' :
               AvanzaCaracter();
               return (token=TK_COMA);
           case '<' :
               AvanzaCaracter();
               c = Primero();
	           if ( c == '=' || c == '>' ) {
                   AvanzaCaracter();
                   valor = valor + Character.toString(c);
		           return(token= c == '=' ? TK_MENIG: TK_DIF );
	           }
	           else
		           return (token=TK_MEN);
            case '>' :
               AvanzaCaracter();
               if ( Primero() == '=') {
                  AvanzaCaracter();
                  valor = valor + "=";
		          return(token=TK_MAYIG);
	           }
	           else
		          return (token=TK_MAY);
           case '\'':
               AvanzaCaracter();
               c = Primero();
               valor = "";
               while ( ! Vacia() && (c =Primero()) != '\'' ) {
                   valor = valor + Character.toString(c);
                   AvanzaCaracter();
               }
               if ( c == '\'' ) {
                   AvanzaCaracter();
                   return (token=TK_VALOR);
               }
               Error( 2 );
	           return (token = 0);

           default:
               valor = "";
               String fin;
               fin = ";<>=-\' \t";
               c = Primero();
               while ( ! Vacia() && fin.indexOf((int)c) == -1 ) {
                   valor = valor + Character.toString(c);
                   AvanzaCaracter();
                   c = Primero();
               }
               if ( valor.equals("Y") )
                  return (token=TK_AND);
               /*if ( valor.equals("O") )
                  return (token=TK_OR);*/
               return (token=TK_ATRIB);

	      }
    }

    /*
    int bc1() {
        boolean l;
        do {
          l = LeeLinea();
          if ( !linea.equals(" ") )
             System.out.println(linea);
        }
        while ( l );
        return err;
    }
    */
    int bc()
    {
        Scanner();
        Una_Regla();
        while ( err == 0 && token != TK_END && token != TK_PREG )
            Una_Regla();
        if ( token == TK_PREG )
            Preg();
        if ( token == TK_PREG )
            Hechos();
        return err;
    }

    int Una_Regla()
    {
        Ant();
        if ( err != 0 ) return err;
        if ( token != TK_ENT ) {
            Error( 4 );
            return err;
        }
        Scanner();
        Cons();
        if ( err != 0 ) return err;
        if ( token != TK_TREGLA ) {
            Error( 5 );
            return err;
        }
        Scanner();
        if ( err == 0 )
            sh.nuevaregla(ultant, ultcons);
        return err;
    }

    int Ant( )
    {
        Expresion();
        if ( err == 0 )
            ultant = ultexp;
        return err;
    }

    int Cons()
    {
        Expresion();
        if ( err == 0 )
            ultcons = ultexp;
        return err;
    }

    int Preg()
    {
        Scanner();
        Una_Preg();
        while (err == 0 && token != TK_END && token != TK_PREG )
            Una_Preg();
        return err;
    }

    int Hechos()
    {
        Scanner();
        if ( token == TK_END )
            return err;
        Un_Hecho();
        while ( err == 0 && token != TK_END )
            Un_Hecho();
        return err;
    }

    int Un_Hecho()
    {
        String aux1, aux2;

        if ( token != TK_ATRIB ) {
            Error( 8 );
            return err;
        }
        aux1 = valor + "";
        Scanner();
        if ( token != TK_IG ) {
            Error( 9 );
            return err;
        }
        Scanner();
        if ( token != TK_VALOR ) {
            Error( 10 );
            return err;
        }
        aux2 = valor + "";
        Atrib a;
        a = sh.busatrib( aux1 );
        a.cambvalor(aux2, Vals.VAL_HECHO);
        Scanner();
        return err;
    }
    
    int Una_Preg()
    {
        String aux1, aux2;

        if ( token != TK_ATRIB ) {
            Error( 8 );
            return err;
        }

        aux1 = valor + "";
        Scanner();
        if ( token != TK_IG ) {
            Error( 9 );
            return err;
        }
        Scanner();
        if ( token != TK_VALOR ) {
            Error( 10 );
            return err;
        }
        aux2 = valor + "";
        Atrib a;
        a = sh.busatrib( aux1 );
        a.cambpreg(aux2);
        Scanner();
        if ( token == TK_PIZQ )
        {
            String aux, valores;
            int n;
            //*p;
            n = 0;
            aux = "";
            Scanner();
            valores = "";
            while ( err == 0 && token != TK_PDER && token != TK_END )
            {
                if ( token != TK_VALOR )
                {
                    Error( 10 );
                    return err;
                }
                valores = valores + Character.toString((char)(valor.length()));
                aux = aux + valor;
                n++;
        		Scanner();
                if ( token != TK_PDER )
                    if ( token != TK_COMA )
                    {
                        Error( 11 );
                        return err;
                    }
                    else
                        Scanner();
                else
                    ;
            }
            valores = Character.toString((char)n) + valores + aux;
            a.cambvalores( valores );
            //	 printf("\nValores %d '%s'",n,valores);
            if ( token != TK_PDER )
            {
                Error( 6 );
                return err;
            }
            Scanner();
        }
        return err;
    }

    int Expresion()
    {
       int tipo = 0;
       Exp aux;

       if ( token == TK_PIZQ ) {
          Scanner();
          Expresion();
          if ( token != TK_PDER ) {
            Error( 6 );
            return err;
          }
          Scanner();
       }
       else {
         Termino();
         ultexp = ultter;
       }
       aux = ultexp;
       if ( err  == 0 && (token == TK_AND /*|| token == TK_OR*/ ) ) {
          tipo = token;
          Scanner();
          Expresion();
          if ( err == 0 )
             if ( tipo==TK_AND )
	            ultexp = new ExpAnd( sh, aux, ultexp );
             /*else
	            ultexp = new expOr( aux, ultexp, NULL );*/
       }
       return err;
    }

    int Termino()
    {
      String aux;
      int tokenOp;
      if ( token != TK_ATRIB ) {
         Error( 8 );
         return err;
      }
      aux = valor + "";
      Scanner();
      tokenOp = token;
      if ( token != TK_IG
        && token != TK_MEN
        && token != TK_MAY
        && token != TK_MENIG
        && token != TK_MAYIG
        && token != TK_DIF
        ) {
        Error( 9 );
        return err;
      }
      Scanner();
      if ( token != TK_VALOR ) {
         Error( 10 );
         return err;
      }
      switch ( tokenOp )
      {
        case TK_MAY:  ultter = new TerMay( sh, aux, valor ); break;
        case TK_MEN:  ultter = new TerMen( sh, aux, valor ); break;
        case TK_MENIG:  ultter = new TerMenIg( sh, aux, valor ); break;
        case TK_MAYIG:  ultter = new TerMayIg( sh, aux, valor ); break;
        case TK_DIF: ultter = new TerDif( sh, aux, valor ); break;
        case TK_IG: ultter = new Ter( sh, aux, valor ); break;
      }
      Scanner();
      return err;
    }

    public static String stringCell(Sheet sh, int c, int r) {
        
        if ( c < sh.getColumns() && r < sh.getRows() )
            if ( sh.getCell(c, r).getType() != CellType.EMPTY ) {
                String val;
                val = sh.getCell(c, r).getContents();
                return val;
            }
        return null;
    }

    void AvanzaCaracter()
    {
       linea = linea.substring(1);
    }
    
    char Primero()
    {
        if ( !Vacia() )
            return linea.charAt(0);
        else
            return '\0';
    }
    
    boolean Vacia()
    {
        return linea.isEmpty();
    }

    boolean SaltaEspacio()
    {
        do {
            if ( Vacia() )
                if ( LeeLinea() == false )
                    return false;
            char c;
            do {
                c = Primero();
                if ( c == ' ' || c == '\t' )
                   AvanzaCaracter();
                else
                    break;
            }
            while ( ! Vacia() );
        }
        while ( Vacia() );
        return true;
    }
    
    boolean LeeLinea()
    {
        String atrib, aux, aux2;
        switch ( estado ) {
            case 0: //inicio TABLA
                sheet = w.getSheet("TABLA");
                sh_row = 1;
                sh_col = 1;
                linea  = " ";
                estado = 1;
                return true;
            case 1: //INICIO REGLA
                aux = stringCell(sheet, sh_col, 0);
                if ( aux == null ) {
                    linea = "%";
                    estado = 5;
                    return true;
                }
                else {
                    linea = " ";
                    estado = 2;
                    primero = true;
                    return true;
                }
            case 2: //BUSCAR ATRIB ANTECEDENTE
                atrib = stringCell(sheet, 0, sh_row );
                if ( atrib != null ) {
                    if  ( atrib.equals("CONCLUSIONES") ) {
                        linea = "->";
                        estado = 3;
                        sh_row++;
                        primero = true;
                        return true;
                    }
                    else {
                        aux2 = stringCell(sheet, sh_col, sh_row );
                        if ( aux2 == null ) {
                            linea = " ";
                            estado = 2;//mismo; seguir buscando
                            sh_row++;
                            return true;
                        }
                        else {
                            if ( primero ) {
                                linea = "";
                                primero = false;
                            }
                            else {
                                linea = "Y ";
                            }
                            char ch = aux2.charAt(0);
                            if ( ch != '<' && ch != '>' ) {
                                atrib = atrib + " = ";
                            }
                            else {
                                atrib = atrib + Character.toString(ch);
                                aux2 = aux2.substring(1);
                                ch = aux2.charAt(0);
                                if ( ch == '>' || ch == '=' ) {
                                   atrib = atrib + Character.toString(ch);
                                   aux2 = aux2.substring(1);
                                }
                            }
                            linea = linea + atrib + "'"+aux2+"'";
                            sh_row++;
                            estado = 2;//seguir
                            return true;
                        }
                    }
                }
                else {
                    linea = " ";
                    estado = 2;
                    sh_row++;
                    return true;
                }
            case 3: //CONSECUENTE
                atrib = stringCell(sheet, 0, sh_row );
                if ( atrib != null ) {
                    if  ( atrib.equals("OBJETIVO") ) {
                        linea = ";";
                        obj_row = sh_row+1;
                        estado = 1;
                        sh_row = 1;
                        sh_col++; 
                        return true;
                    }
                    else {
                        aux2 = stringCell(sheet, sh_col, sh_row );
                        if ( aux2 == null ) {
                            linea = " ";
                            estado = 3;//mismo; seguir buscando
                            sh_row++;
                            return true;
                        }
                        else {
                            if ( primero ) {
                                linea = "";
                                primero = false;
                            }
                            else {
                                linea = "Y ";
                            }
                            atrib = atrib + " = ";
                            linea = linea + atrib + "'"+aux2+"'";
                            sh_row++;
                            estado = 3;//seguir
                            return true;
                        }
                    }
                }
                else {
                    linea = " ";
                    estado = 3;
                    sh_row++;
                    return true;
                }
            case 5: //inicio ATRIBUTOS
                sheet = w.getSheet("ATRIBUTOS");
                sh_row = 1;
                sh_col = 0;
                linea  = " ";
                estado = 6;
                return true;
            case 6:
                atrib = stringCell(sheet, 0, sh_row );
                if ( atrib != null ) {
                    aux2 = stringCell(sheet, 1, sh_row );
                    if ( aux2 == null ) {
                        aux2 = "VALOR PARA EL ATRIBUTO " + atrib + " = ";
                    }
                    linea = atrib + " = '" + atrib + ": " + aux2 + "'";
                    estado = 7;
                    return true;
                }
                else {
                    linea = "%";
                    estado = 10;
                    return true;
                }
            case 7:
                sh_col = 3;
                aux2 = stringCell(sheet, sh_col, sh_row );
                if ( aux2 != null ) {
                    linea = "(";
                    primero = true;
                    do {
                        aux2 = "'"+aux2+"'";
                        if ( primero ) {
                            linea = linea + aux2;
                            primero = false;
                        }
                        else {
                            linea = linea + "," + aux2;
                        }
                        sh_col++;
                        aux2 = stringCell(sheet, sh_col, sh_row );
                    }
                    while ( aux2 != null);
                    linea = linea + ")";
                    sh_row++;
                    estado = 6;
                    return true;
                }
                else {
                    linea = " ";
                    sh_row++;
                    estado = 6;
                    return true;
                }
            case 10: //OBJETIVO; ESTÁ EN TABLA
                sheet = w.getSheet("TABLA");
                sh_row = obj_row;
                sh_col = 0;
                atrib = stringCell(sheet, sh_col, sh_row );
                linea  = "OBJETIVO = '"+atrib+"'";
                estado = 15;
                return true;
                
            case 15:
                linea = " ";
                return false;
                
        }

        return false;
    }

    
    
}
