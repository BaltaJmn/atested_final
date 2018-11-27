package com.balta.atested;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

import static com.balta.atested.R.color.rellenaCampo;

public class AnyadirEditarActivity extends AppCompatActivity {

    private static final String LOGTAG = "AnyadirEditarActivity";
    final private int CODE_WRITE_EXTERNAL_STORAGE_PERMISSION = 123;
    private Context myContext;
    private ConstraintLayout constraintLayoutAnyadirActivity;
    private ArrayAdapter<String> adapter;
    private Spinner spinner;
    private int codigo;
    private Pregunta p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anyadir);

        myContext = this;
        constraintLayoutAnyadirActivity = findViewById(R.id.anyadir);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Definición de la lista de opciones
        ArrayList<String> items = Repositorio.recuperarCategorias(myContext);

        // Definición del Adaptador que contiene la lista de opciones
        adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, items);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        // Definición del Spinner
        spinner = (Spinner) findViewById(R.id.spinnerCategoria);
        spinner.setAdapter(adapter);

        // Definición de la acción del botón
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Recuperación de la vista del AlertDialog a partir del layout de la Actividad
                LayoutInflater layoutActivity = LayoutInflater.from(myContext);
                View viewAlertDialog = layoutActivity.inflate(R.layout.alert_dialog, null);

                // Definición del AlertDialog
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(myContext);

                // Asignación del AlertDialog a su vista
                alertDialog.setView(viewAlertDialog);

                // Recuperación del EditText del AlertDialog
                final EditText dialogInput = (EditText) viewAlertDialog.findViewById(R.id.dialogInput);

                // Configuración del AlertDialog
                alertDialog
                        .setCancelable(false)
                        // Botón Añadir
                        .setPositiveButton(getResources().getString(R.string.add),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogBox, int id) {
                                        adapter.add(dialogInput.getText().toString());
                                        spinner.setSelection(adapter.getPosition(dialogInput.getText().toString()));
                                    }
                                })
                        // Botón Cancelar
                        .setNegativeButton(getResources().getString(R.string.cancel),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogBox, int id) {
                                        dialogBox.cancel();
                                    }
                                })
                        .create()
                        .show();
            }
        });


        Button guardar = findViewById(R.id.buttonGuardar);
        final EditText enunciado = (EditText) findViewById(R.id.editTextEnunciado);
        final EditText resp1 = (EditText) findViewById(R.id.editTextCorrecta);
        final EditText resp2 = (EditText) findViewById(R.id.editTextIncorrecta1);
        final EditText resp3 = (EditText) findViewById(R.id.editTextIncorrecta2);
        final EditText resp4 = (EditText) findViewById(R.id.editTextIncorrecta3);
        final Spinner spinner1 = (Spinner) findViewById(R.id.spinnerCategoria);

        //Si de la anterior ventana, nos han pasado un intent al querer editarla, esto coloca cada texto en su lugar
        if (this.getIntent().getExtras() != null) {
            Bundle bundle = this.getIntent().getExtras();

            codigo = bundle.getInt("codigo");

            p = Repositorio.recuperarPreguntaSelec(myContext, codigo);
            enunciado.setText(p.getEnunciado());
            resp1.setText(p.getRespuestaCorrecta());
            resp2.setText(p.getRespuestaIncorrecta1());
            resp3.setText(p.getRespuestaIncorrecta2());
            resp4.setText(p.getRespuestaIncorrecta3());

            //Repositorio.actualizarPreguntaEditada(myContext, codigo, p);
            //Repositorio.borrarPreguntaEditada(myContext, codigo);

        } else {
            codigo = -1;
        }

        guardar.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {

                int WriteExternalStoragePermission = ContextCompat.checkSelfPermission(myContext, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                Log.d("MainActivity", "WRITE_EXTERNAL_STORAGE Permission: " + WriteExternalStoragePermission);

                if (WriteExternalStoragePermission != PackageManager.PERMISSION_GRANTED) {
                    // Permiso denegado
                    // A partir de Marshmallow (6.0) se pide aceptar o rechazar el permiso en tiempo de ejecución
                    // En las versiones anteriores no es posible hacerlo
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        ActivityCompat.requestPermissions(AnyadirEditarActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, CODE_WRITE_EXTERNAL_STORAGE_PERMISSION);
                        // Una vez que se pide aceptar o rechazar el permiso se ejecuta el método "onRequestPermissionsResult" para manejar la respuesta
                        // Si el usuario marca "No preguntar más" no se volverá a mostrar este diálogo
                    } else {
                        Snackbar.make(constraintLayoutAnyadirActivity, getResources().getString(R.string.write_permission_denied), Snackbar.LENGTH_LONG)
                                .show();
                    }
                } else {
                    // Permiso aceptado
                    Snackbar.make(constraintLayoutAnyadirActivity, getResources().getString(R.string.write_permission_granted), Snackbar.LENGTH_LONG)
                            .show();
                }

                //Comprueba que ninguno está vacío
                if (enunciado.getText().toString().isEmpty() ||
                        spinner1.getSelectedItem() == null ||
                        resp1.getText().toString().isEmpty() ||
                        resp2.getText().toString().isEmpty() ||
                        resp3.getText().toString().isEmpty() ||
                        resp4.getText().toString().isEmpty()) {

                    //Cambia el fondo del campo vacío
                    if (enunciado.getText().toString().isEmpty()) {
                        enunciado.setBackgroundColor(R.color.rellenaCampo);

                        Snackbar.make(view, "Rellena el enunciado", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }

                    //Cambia el fondo del campo vacío
                    if (spinner1.getSelectedItem() == null) {
                        spinner1.setBackgroundColor(R.color.rellenaCampo);

                        Snackbar.make(view, "Rellena la categoria", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }

                    //Cambia el fondo del campo vacío
                    if (resp1.getText().toString().isEmpty()) {
                        resp1.setBackgroundColor(R.color.rellenaCampo);

                        Snackbar.make(view, "Rellena la respuesta 1", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }

                    //Cambia el fondo del campo vacío
                    if (resp2.getText().toString().isEmpty()) {
                        resp2.setBackgroundColor(R.color.rellenaCampo);

                        Snackbar.make(view, "Rellena la respuesta 2", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }

                    //Cambia el fondo del campo vacío
                    if (resp3.getText().toString().isEmpty()) {
                        resp3.setBackgroundColor(R.color.rellenaCampo);

                        Snackbar.make(view, "Rellena la respuesta 3", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }

                    //Cambia el fondo del campo vacío
                    if (resp4.getText().toString().isEmpty()) {
                        resp4.setBackgroundColor(R.color.rellenaCampo);

                        Snackbar.make(view, "Rellena la respuesta 4", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }

                    view.clearFocus();

                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }

                    /*
                    Snackbar.make(view, "Rellenar todos los campos", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                            */
                } else {


                    //Si no es de edición, la crea, sino, la actualiza
                    if (codigo != -1) {
                        Pregunta nuevaPregunta = new Pregunta(Integer.toString(codigo), enunciado.getText().toString(), spinner1.getSelectedItem().toString(), resp1.getText().toString(), resp2.getText().toString(), resp3.getText().toString(), resp4.getText().toString());
                        Repositorio.actualizarPreguntaEditada(myContext, codigo, nuevaPregunta);

                    } else {
                        Pregunta nuevaPregunta = new Pregunta(enunciado.getText().toString(), spinner1.getSelectedItem().toString(), resp1.getText().toString(), resp2.getText().toString(), resp3.getText().toString(), resp4.getText().toString());
                        Repositorio.insertar(nuevaPregunta, myContext);
                    }


                    finish();
                }


            }
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case CODE_WRITE_EXTERNAL_STORAGE_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permiso aceptado
                    Snackbar.make(constraintLayoutAnyadirActivity, getResources().getString(R.string.write_permission_accepted), Snackbar.LENGTH_LONG)
                            .show();
                } else {
                    // Permiso rechazado
                    Snackbar.make(constraintLayoutAnyadirActivity, getResources().getString(R.string.write_permission_not_accepted), Snackbar.LENGTH_LONG)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onStart() {
        MyLog.d(LOGTAG, Constantes.start);
        super.onStart();
    }

    @Override
    protected void onResume() {
        MyLog.d(LOGTAG, Constantes.resume);
        super.onResume();
    }

    @Override
    protected void onPause() {
        MyLog.d(LOGTAG, Constantes.pause);
        super.onPause();
    }

    @Override
    protected void onStop() {
        MyLog.d(LOGTAG, Constantes.stop);
        super.onStop();
    }

    @Override
    protected void onRestart() {
        MyLog.d(LOGTAG, Constantes.restart);
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        MyLog.d(LOGTAG, Constantes.destroy);
        super.onDestroy();
    }


}
