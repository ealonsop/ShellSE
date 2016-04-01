package com.cixlabs.pshellse;

import android.app.Application;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;

import ar.com.daidalos.afiledialog.FileChooserDialog;
import psexp.Atrib;
import psexp.MuestraInfo;
import psexp.Shell;
import psexp.Vals;

public class ShellActivity extends AppCompatActivity {

    private static ShellActivity theShellActivity;
    private static File farchivo;
    private static Shell sh;
    private static Atrib objetivo;
    private static Atrib ingresando;
    private Button bt_iniciar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shell);

        Intent intent = getIntent();
        bt_iniciar = (Button)findViewById(R.id.bt_iniciar);
        bt_iniciar.setEnabled(false);

        Button buttonDialog1 = (Button)this.findViewById(R.id.bt_cargar);
        buttonDialog1.setOnClickListener(btnDialogSimpleOpen);

        theShellActivity = this;

    }

    public void buttonSalirOnClick(View v) {
        finish();
    }

    public void buttonIniciarOnClick(View v) {
        String res;
        sh = new Shell(theShellActivity);
        if ( sh.Cargar(farchivo) != 0 ) {
            sh.Ejecutar();
            String aux;
            Atrib atr;
            atr = sh.busatrib("OBJETIVO");
            aux = atr.obtvalor();
            objetivo = sh.busatrib(aux);
            if (objetivo.obtval() == Vals.VAL_SI) {
                new MuestraInfo(objetivo.nom + " = " + objetivo.obtvalor(),
                        "OBJETIVO", 20);
            }
        }
        else {
            bt_iniciar.setEnabled(false);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            /*if (requestCode == 3) {
                sh.Ejecutar();
                String aux;
                Atrib atr;
                atr = sh.busatrib("OBJETIVO");
                aux = atr.obtvalor();
                objetivo = sh.busatrib(aux);
                if (objetivo.obtval() == Vals.VAL_SI) {
                    new MuestraInfo(objetivo.nom + " = " + objetivo.obtvalor(),
                            "OBJETIVO", 20);
                }

            }
            */
            if (requestCode == 20) {
                ingresando = null;
                sh.menu();
            }

            if (requestCode == 5 || requestCode == 6 ) {
                String aux,aux2;
                String sres = data.getStringExtra("respuesta");
                String atrib = data.getStringExtra("atributo");
                Atrib at = sh.busatrib(atrib);
                if ( ( requestCode == 5 && sres.equals("0") ) ||
                     ( requestCode == 6 && sres.equals("?") )  ) {
                    ingresando = at;
                    sh.menu();
                }
                else {
                    int i;
                    if ( requestCode == 5 ) {
                        int res = Integer.parseInt(sres);
                        int pa, p, n, n1;
                        n = at.valores.codePointAt(0);
                        pa = p = n + 1;
                        for (i = 1; i <= res; i++) {
                            n1 = at.valores.codePointAt(i);
                            pa = p;
                            p += n1;
                        }
                        aux = at.valores.substring(pa, p);
                    }
                    else {
                        aux = sres;
                    }
                    aux = aux.toUpperCase();
                    at.cambvalor( aux, Vals.VAL_USR );
                    aux2 = "";
                    for ( i = 0; i < sh.nap; i++ )
                         aux2 = aux2 + sh.ap[i].a.nom + " ";
                    Log.i("tt", at.nom + " = " + aux + " pend = "+ sh.nap + " = " + aux2);
                    sh.resetReglasEval();

                    sh.nap--;
                    while ( sh.nap > 0 ) {
                        sh.nap--;
                        at = sh.ap[sh.nap].a;
                        if (at.obtval() == Vals.VAL_BUS ) {
                            sh.nap++;
                            break;
                        }
                        at.cambval(Vals.VAL_NO);
                        Log.i("tt now", at.nom + " pend = " + sh.nap);
                        if ( sh.evalua(at) == Vals.VAL_BUS )
                            break;
                        if ( at.obtval() == Vals.VAL_SI )
                            sh.nap--;
                        else
                            ;
                    }
                    if (objetivo.obtval() == Vals.VAL_SI) {
                        new MuestraInfo(objetivo.nom + " = " + objetivo.obtvalor(),
                                "OBJETIVO", 20);
                    }

                }
            }

            if ( requestCode == 10 ) {
                int res = Integer.parseInt(data.getStringExtra("respuesta"));
                switch ( res ) {
                    case 1:
                        finish();
                        break;
                    case 2:
                        sh.shmenu.muestraReglas(1);
                        break;
                    case 3:
                        sh.shmenu.muestraReglas(0);
                        break;
                    case 4:
                        sh.shmenu.muestraReglas(2);
                        break;
                    case 5:
                        sh.shmenu.muestraReglas(3);
                        break;
                    case 6:
                        sh.shmenu.muestraAtributos(0);
                        break;
                    case 7:
                        sh.shmenu.muestraAtributos(1);
                        break;
                    case 8:
                        sh.shmenu.muestraAtributos(2);
                        break;
                    case 0:
                    default:
                        if ( ingresando != null )
                           ingresando.leervalor(sh);
                }

            }

            if ( requestCode == 15 ) {
                sh.menu();
            }
        }
    }

    private View.OnClickListener btnDialogSimpleOpen = new View.OnClickListener() {
        public void onClick(View v) {
            FileChooserDialog dialog = new FileChooserDialog(ShellActivity.this,
                    Environment.getExternalStorageDirectory().toString()
            );
            dialog.addListener(ShellActivity.this.onFileSelectedListener);
            dialog.show();
        }
    };


    private FileChooserDialog.OnFileSelectedListener onFileSelectedListener =
            new FileChooserDialog.OnFileSelectedListener() {
                public void onFileSelected(Dialog source, File file) {
                    source.hide();
                     Toast toast = Toast.makeText(ShellActivity.this, "File selected: " +
                            file.getPath(), // file.getName()
                            Toast.LENGTH_LONG);
                    toast.show();
                    //archivo = file.getPath();
                    farchivo = file;
                    bt_iniciar.setEnabled(true);
                }
                public void onFileSelected(Dialog source, File folder, String name) {
                    source.hide();
                    Toast toast = Toast.makeText(ShellActivity.this, "File created: " + folder.getName() + "/" + name,
                            Toast.LENGTH_LONG);
                    toast.show();
                }
            };



}
