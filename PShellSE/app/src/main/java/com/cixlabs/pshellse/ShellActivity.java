package com.cixlabs.pshellse;

import android.app.Application;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import ar.com.daidalos.afiledialog.FileChooserDialog;
import psexp.Atrib;
import psexp.MuestraInfo;
import psexp.Shell;
import psexp.Vals;

public class ShellActivity extends AppCompatActivity {

    public static ShellActivity theShellActivity;
    private static File farchivo;
    private static InputStream ifarchivo;
    private static Shell sh;
    private static Atrib objetivo;
    private static Atrib ingresando;
    private static String kdbfiles[];
    private static AssetManager assetManager;
    public static int seleccionado;

    private static Button bt_iniciar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shell);

        Intent intent = getIntent();
        bt_iniciar = (Button) findViewById(R.id.bt_iniciar);
        bt_iniciar.setEnabled(false);

        Button buttonDialog1 = (Button) this.findViewById(R.id.bt_cargar);
        buttonDialog1.setOnClickListener(btnDialogSimpleOpen);

        assetManager = getAssets();
        kdbfiles = null;
        try {
            kdbfiles = assetManager.list("kdb");
        } catch (IOException e) {
            Log.e("tag", "Failed to get asset file list.", e);
        }

//        for(String filename : files) {
//        }

        farchivo = null;
        ifarchivo = null;

        theShellActivity = this;
        seleccionado = -1;

    }

    public void buttonSalirOnClick(View v) {
        finish();
    }

    public void buttonIniciarOnClick(View v) {
        String res;
        sh = new Shell(theShellActivity);
        if ( farchivo != null ) {
            if (sh.Cargar(farchivo) == 0) {
                bt_iniciar.setEnabled(false);
                return;
            }
        }
        else
        if ( seleccionado >= 0 ) {

            ifarchivo = null;
            //assetManager  = getAssets();
            try {
                ifarchivo = assetManager.open("kdb/"+kdbfiles[seleccionado]);
            }
            catch (IOException e) {

            }

            if ( ifarchivo != null ) {
                if (sh.Cargar(ifarchivo) == 0) {
                    Toast.makeText(ShellActivity.theShellActivity, "Error de cargar: ",
                            Toast.LENGTH_LONG).show();
                    seleccionado = -1;
                    bt_iniciar.setEnabled(false);
                    return;
                }
            }
            else {
                Toast.makeText(ShellActivity.theShellActivity, "Error de asset: ",
                        Toast.LENGTH_LONG).show();
                seleccionado = -1;
                bt_iniciar.setEnabled(false);
                return;
            }
        }
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



    public void buttonCargarDemoOnClick(View v) {
        seleccionado  = -1;
        farchivo = null;
        bt_iniciar.setEnabled(false);
        DemoKDB kdb = new DemoKDB(this, kdbfiles);
        kdb.show();

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

            if (requestCode == 5 || requestCode == 6) {
                String aux, aux2;
                String sres = data.getStringExtra("respuesta");
                String atrib = data.getStringExtra("atributo");
                Atrib at = sh.busatrib(atrib);
                if ((requestCode == 5 && sres.equals("0")) ||
                        (requestCode == 6 && sres.equals("?"))) {
                    ingresando = at;
                    sh.menu();
                } else {
                    int i;
                    if (requestCode == 5) {
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
                    } else {
                        aux = sres;
                    }
                    aux = aux.toUpperCase();
                    at.cambvalor(aux, Vals.VAL_USR);
                    aux2 = "";
                    for (i = 0; i < sh.nap; i++)
                        aux2 = aux2 + sh.ap[i].a.nom + " ";
                    Log.i("tt", at.nom + " = " + aux + " pend = " + sh.nap + " = " + aux2);
                    sh.resetReglasEval();

                    sh.nap--;
                    while (sh.nap > 0) {
                        sh.nap--;
                        at = sh.ap[sh.nap].a;
                        if (at.obtval() == Vals.VAL_BUS) {
                            sh.nap++;
                            break;
                        }
                        at.cambval(Vals.VAL_NO);
                        Log.i("tt now", at.nom + " pend = " + sh.nap);
                        if (sh.evalua(at) == Vals.VAL_BUS)
                            break;
                        if (at.obtval() == Vals.VAL_SI)
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

            if (requestCode == 10) {
                int res = Integer.parseInt(data.getStringExtra("respuesta"));
                switch (res) {
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
                        if (ingresando != null)
                            ingresando.leervalor(sh);
                }

            }

            if (requestCode == 15) {
                sh.menu();
            }
        }
    }

    private View.OnClickListener btnDialogSimpleOpen = new View.OnClickListener() {
        public void onClick(View v) {
            farchivo = null;
            seleccionado = -1;
            bt_iniciar.setEnabled(false);
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
                    Toast toast = Toast.makeText(ShellActivity.this, "Archivo: " +
                                    file.getPath(), // file.getName()
                            Toast.LENGTH_LONG);
                    toast.show();
                    //archivo = file.getPath();
                    farchivo = file;
                    seleccionado = -1;
                    bt_iniciar.setEnabled(true);
                }

                public void onFileSelected(Dialog source, File folder, String name) {
                    source.hide();
                    Toast toast = Toast.makeText(ShellActivity.this, "File created: " + folder.getName() + "/" + name,
                            Toast.LENGTH_LONG);
                    toast.show();
                }
            };

    public static void onDialogKDBdemo(int item) {

        seleccionado = item;
        if (seleccionado >= 0) {
            farchivo = null;
            Toast.makeText(ShellActivity.theShellActivity, "Archivo: " +
                            kdbfiles[seleccionado],
                    Toast.LENGTH_LONG).show();
            bt_iniciar.setEnabled(true);
        }
    }


    public static void onDialogKDBCopiardemo(int item) {

        seleccionado = item;
        if (seleccionado >= 0) {
            farchivo = null;
            copiar(kdbfiles[seleccionado]);
            bt_iniciar.setEnabled(true);
        }
    }

    private static void copiar(String filename) {

        InputStream in = null;
        OutputStream out = null;
        File outFile = null;
        try {
            in = assetManager.open("kdb/" + filename);
            outFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),
                filename);
            out = new FileOutputStream(outFile);
            copyFile(in, out);
            in.close();
            out.flush();
            out.close();
        } catch(IOException e) {
            Toast.makeText(ShellActivity.theShellActivity, "Error al copiar " +
                            filename,
                    Toast.LENGTH_LONG).show();
        }
        if ( in != null && out != null )
             Toast.makeText(ShellActivity.theShellActivity, "Copiado: " +
                        outFile.getPath(),
                Toast.LENGTH_LONG).show();
        in = null;
        out = null;
    }

    private static void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }

}
