package psexp;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ealonso
 */
public abstract class SMenu {
    public Shell sh;
    
    public SMenu( Shell sh ) { 
        this.sh = sh; 
    }

    public abstract int Menu();

    public void	muestraAtributos( int todos ) {} //0 todos; 1 valor; 2 evaluando
    public void	muestraReglas( int todos ) {}
    //0 todos; 1 evaluandose; 2 si; 3; si/no

}
