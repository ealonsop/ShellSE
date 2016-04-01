/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package psexp;

import android.content.Intent;

import com.cixlabs.pshellse.MuestraInfoActivity;

/**
 *
 * @author laboratorio
 */
public class MuestraInfo {
  // private attributes
    public static int opcion;

  // constructor
  public MuestraInfo( String info, String titulo, int rqcode )
  {
      Intent intent = new Intent(Shell.theShellActivity, MuestraInfoActivity.class);
      intent.putExtra("titulo",titulo );
      intent.putExtra("mensaje",info);
      opcion = -1;
      Shell.theShellActivity.startActivityForResult(intent, rqcode);
  }

}
