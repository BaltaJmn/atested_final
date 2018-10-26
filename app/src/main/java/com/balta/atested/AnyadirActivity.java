package com.balta.atested;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class AnyadirActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anyadir);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Spinner spinner = (Spinner) findViewById(R.id.spinnerCategoria);
        String[] letra = {"A","B","C","D","E"};
        spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, letra));

        Button guardar = findViewById(R.id.buttonGuardar);
        final EditText enunciado = (EditText) findViewById(R.id.editTextEnunciado);
        final EditText resp1 = (EditText) findViewById(R.id.editTextCorrecta);
        final EditText resp2 = (EditText) findViewById(R.id.editTextIncorrecta1);
        final EditText resp3 = (EditText) findViewById(R.id.editTextIncorrecta2);
        final EditText resp4 = (EditText) findViewById(R.id.editTextIncorrecta3);

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if( enunciado.getText().toString().isEmpty() ||
                    resp1.getText().toString().isEmpty() ||
                    resp2.getText().toString().isEmpty() ||
                    resp3.getText().toString().isEmpty() ||
                    resp4.getText().toString().isEmpty() ){

                    view.clearFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }

                    Snackbar.make(view, "Rellenar todos los campos", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {
                    finish();
                }

            }
        });


    }

}
