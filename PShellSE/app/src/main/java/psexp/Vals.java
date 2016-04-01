/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package psexp;

/**
 *
 * @author ealonso
 */
public class Vals {
  // private attributes

    public static final int VAL_SI	= 1 ;
    public static final int  VAL_NO	=0;
    public static final int  VAL_BUS	=-1;
    public static final int  VAL_NOCALC =-2;

    public static final int  VAL_REGLA   =1;
    public static final int  VAL_USR		=2;
    public static final int  VAL_HECHO	=3;

    public static final int MAXATRIB	=1000;
    public static final int  MAXREGLAS	=2000;

    public static final int EXP_VERD	=1;
    public static final int  EXP_FALSA	=0;
    public static final int EXP_NOCALC	=-2;

    public static final int  TE_TER		=1;
    public static final int  TE_AND		=2;
    public static final int  TE_OR		=3;

    public static final int  TE_TERIG	=1;
    public static final int  TE_TERMAY	=4;
    public static final int  TE_TERMEN	=5;
    public static final int  TE_TERMENI	=6;
    public static final int  TE_TERMAYI	=7;
    public static final int  TE_TERDIF	=8;

    public static final int REG_OK		=1;
    public static final int REG_SINEV	=2;
    public static final int REG_EVAL	=3;
    public static final int REG_YAEVAL	=0x0100;

  // constructor
    public Vals()
    {
    }
}
