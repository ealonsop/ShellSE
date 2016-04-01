package com.cixlabs.pshellse;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class PreguntaActivity extends AppCompatActivity {

    private RadioGroup rg;
    private EditText pt;

    private String atributo;
    private String pregunta;
    private String resp[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pregunta);

        Intent intent;
        intent = getIntent();
        Bundle extras = intent.getExtras();
        pregunta = extras.getString("pregunta");
        resp = extras.getStringArray("respuestas");
        atributo = extras.getString("atributo");

        TextView tv = (TextView)findViewById(R.id.tv_pregunta);
        tv.setText(pregunta);
        rg = (RadioGroup)findViewById(R.id.rg_preguntas);

        if ( resp.length == 1) { //ingresar texto
            pt = new EditText(this);
            pt.setInputType(InputType.TYPE_CLASS_TEXT);
            rg.addView(pt);
        }
        else {
            for (int i = 0; i < resp.length; i++) {
                RadioButton button = new RadioButton(this);
                button.setTextColor(0xFFFFFFFF);
                button.setId(i);
                button.setText(resp[i]);
                button.setChecked(i == 0);
                rg.addView(button);
            }
        }

    }


    public void buttonBackPregOnClick(View v) {
        String respuesta;

        Intent intent = new Intent();

        if ( resp.length == 1 ) {
            respuesta = pt.getText().toString().toUpperCase();
        }
        else {
            respuesta = String.valueOf(rg.getCheckedRadioButtonId());
        }
        intent.putExtra("atributo", atributo);
        intent.putExtra("respuesta", respuesta );
        setResult(RESULT_OK, intent);

        finish();

    }

    @Override
    public void onBackPressed() {
    }

}
