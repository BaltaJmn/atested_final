package com.balta.atested;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class AnyadirActivity extends AppCompatActivity {

    final private int CODE_WRITE_EXTERNAL_STORAGE_PERMISSION = 123;
    private Context myContext;
    private ConstraintLayout constraintLayoutAnyadirActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anyadir);

        myContext = this;
        constraintLayoutAnyadirActivity = findViewById(R.id.anyadir);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Spinner spinner = (Spinner) findViewById(R.id.spinnerCategoria);
        String[] letra = {"A", "B", "C", "D", "E"};
        spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, letra));

        Button guardar = findViewById(R.id.buttonGuardar);
        final EditText enunciado = (EditText) findViewById(R.id.editTextEnunciado);
        final EditText resp1 = (EditText) findViewById(R.id.editTextCorrecta);
        final EditText resp2 = (EditText) findViewById(R.id.editTextIncorrecta1);
        final EditText resp3 = (EditText) findViewById(R.id.editTextIncorrecta2);
        final EditText resp4 = (EditText) findViewById(R.id.editTextIncorrecta3);
        final Spinner spinner1 = (Spinner) findViewById(R.id.spinnerCategoria);

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int WriteExternalStoragePermission = ContextCompat.checkSelfPermission(myContext, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                Log.d("MainActivity", "WRITE_EXTERNAL_STORAGE Permission: " + WriteExternalStoragePermission);

                if (WriteExternalStoragePermission != PackageManager.PERMISSION_GRANTED) {
                    // Permiso denegado
                    // A partir de Marshmallow (6.0) se pide aceptar o rechazar el permiso en tiempo de ejecución
                    // En las versiones anteriores no es posible hacerlo
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        ActivityCompat.requestPermissions(AnyadirActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, CODE_WRITE_EXTERNAL_STORAGE_PERMISSION);
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

                if (enunciado.getText().toString().isEmpty() ||
                        resp1.getText().toString().isEmpty() ||
                        resp2.getText().toString().isEmpty() ||
                        resp3.getText().toString().isEmpty() ||
                        resp4.getText().toString().isEmpty() ) {

                    view.clearFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }

                    Snackbar.make(view, "Rellenar todos los campos", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {

                    Pregunta nuevaPregunta = new Pregunta(enunciado.getText().toString(), spinner1.getSelectedItem().toString(), resp1.getText().toString(), resp2.getText().toString(), resp3.getText().toString(), resp4.getText().toString());
                    Repositorio.insertar(nuevaPregunta, myContext);
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


}
