package com.cixlabs.pshellse;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by ealonso on 08/04/2016.
 */
public class DemoKDB extends Dialog {

    private String kdbfiles[];
    public  int seleccionado;

    public DemoKDB(Context context, String kdbfiles [] ) {
        super(context);//,style);

        this.kdbfiles = kdbfiles;
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.content_kdb);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = this.getWindow();
        lp.copyFrom(window.getAttributes());
        //This makes the dialog take up the full width & height
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);

        seleccionado = -1;

    }

    @Override
    public void onCreate( Bundle savedInstanceState ) {

        ListView lvKDB = (ListView)findViewById(R.id.lv_kdb);

        lvKDB.removeAllViewsInLayout();

        String files[] = new String[] { "uno", "dos", "tres" };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
//                android.R.layout.simple_list_item_1, android.R.id.text1, kdbfiles);
                  R.layout.item_kdb, R.id.tv_kdbname, kdbfiles);
        lvKDB.setAdapter(adapter);
        lvKDB.setOnItemClickListener(listPairedClickItem);


        Button bt = (Button)findViewById(R.id.bt_okCargarDemo);
        bt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ShellActivity.onDialogKDBdemo(seleccionado);
                dismiss();
            }
        });

        bt = (Button)findViewById(R.id.bt_copiarDemo);
        bt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ShellActivity.onDialogKDBCopiardemo(seleccionado);
                dismiss();
            }
        });

    }

    private AdapterView.OnItemClickListener listPairedClickItem = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//            String info = ((TextView)(arg1.findViewById(android.R.id.text1))).getText().toString();
//            String info = ((TextView)(arg1.findViewById(R.id.tv_kdbname))).getText().toString();
//            Toast.makeText(getContext(), "Item " + arg2, Toast.LENGTH_LONG).show();
              seleccionado = arg2;
        }
    };


}
