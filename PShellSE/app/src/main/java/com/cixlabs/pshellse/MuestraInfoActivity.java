package com.cixlabs.pshellse;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class MuestraInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_muestra_info);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String titulo = extras.getString("titulo");
        TextView tv = (TextView)findViewById(R.id.tv_tituloinfo);
        tv.setText(titulo);
        String msg = extras.getString("mensaje");
        tv = (TextView)findViewById(R.id.tv_textoinfo);
        tv.setText(msg);
    }

    public void buttonBackMuestraInfoOnClick(View v) {

        Intent intent = new Intent();
        intent.putExtra("respuesta", 1);
        setResult(RESULT_OK, intent);

        finish();
        //super.onBackPressed();
    }

    @Override
    public void onBackPressed() {
    }

}
