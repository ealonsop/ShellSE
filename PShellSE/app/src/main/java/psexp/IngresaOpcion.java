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
import android.os.Bundle;

import com.cixlabs.pshellse.PreguntaActivity;

/**
 *
 * @author ealonso
 */
public class  IngresaOpcion   {

  // constructor
    public IngresaOpcion( String atrib, String pregunta, String opciones[], int rqcode )
    {
        Intent intent = new Intent(Shell.theShellActivity, PreguntaActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("pregunta", pregunta);
        bundle.putStringArray("respuestas", opciones);
        bundle.putString("atributo", atrib);
        intent.putExtras(bundle);
        Shell.theShellActivity.startActivityForResult(intent, rqcode);
    }

}
