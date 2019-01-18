package com.balta.atested;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.balta.atested.R.color.rellenaCampo;

public class AnyadirEditarActivity extends AppCompatActivity {

    private static final String LOGTAG = "AnyadirEditarActivity";
    final private int CODE_WRITE_EXTERNAL_STORAGE_PERMISSION_CAMERA = 1;
    final private int CODE_WRITE_EXTERNAL_STORAGE_PERMISSION_GALLERY = 2;
    private Context myContext;
    private ConstraintLayout constraintLayoutAnyadirActivity;
    private ArrayAdapter<String> adapter;
    private Spinner spinner;
    private int codigo;
    private Pregunta p;

    //Imágenes
    private String TAG = "AnyadirEditarActivity";
    private static final int REQUEST_CAPTURE_IMAGE = 200;
    private static final int REQUEST_SELECT_IMAGE = 201;
    final String pathFotos = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/demoAndroidImages/";
    private Uri uri;
    private Bitmap bitmap = null;

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
        final ImageView imageViewPregunta = (ImageView) findViewById(R.id.imageView);

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
            imageViewPregunta.setImageBitmap(PreguntaAdapter.ConversorBase64aImagen(p.getImagen()));


            //Repositorio.actualizarPreguntaEditada(myContext, codigo, p);
            //Repositorio.borrarPreguntaEditada(myContext, codigo);

        } else {
            codigo = -1;
        }

        guardar.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {

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

                        //Bitmap

                        ImageView imageView = findViewById(R.id.imageView);
                        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
                        bitmap = drawable.getBitmap();

                        Pregunta nuevaPregunta = new Pregunta(Integer.toString(codigo), enunciado.getText().toString(), spinner1.getSelectedItem().toString(), resp1.getText().toString(), resp2.getText().toString(), resp3.getText().toString(), resp4.getText().toString(), camaraImagen64(bitmap));
                        Repositorio.actualizarPreguntaEditada(myContext, codigo, nuevaPregunta);

                    } else {


                        Pregunta nuevaPregunta = new Pregunta(enunciado.getText().toString(), spinner1.getSelectedItem().toString(), resp1.getText().toString(), resp2.getText().toString(), resp3.getText().toString(), resp4.getText().toString(), camaraImagen64(bitmap));
                        Repositorio.insertar(nuevaPregunta, myContext);
                    }


                    finish();
                }


            }
        });

        //======== codigo para imágenes ========
        Button buttonCamera = findViewById(R.id.buttonCamera);
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            buttonCamera.setEnabled(false);
        } else {
            buttonCamera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int WriteExternalStoragePermission = ContextCompat.checkSelfPermission(myContext, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    Log.d("MainActivity", "WRITE_EXTERNAL_STORAGE Permission: " + WriteExternalStoragePermission);

                    if (WriteExternalStoragePermission != PackageManager.PERMISSION_GRANTED) {
                        // Permiso denegado
                        // A partir de Marshmallow (6.0) se pide aceptar o rechazar el permiso en tiempo de ejecución
                        // En las versiones anteriores no es posible hacerlo
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            ActivityCompat.requestPermissions(AnyadirEditarActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, CODE_WRITE_EXTERNAL_STORAGE_PERMISSION_CAMERA);
                            // Una vez que se pide aceptar o rechazar el permiso se ejecuta el método "onRequestPermissionsResult" para manejar la respuesta
                            // Si el usuario marca "No preguntar más" no se volverá a mostrar este diálogo
                        } else {
                            Snackbar.make(constraintLayoutAnyadirActivity, getResources().getString(R.string.write_permission_denied), Snackbar.LENGTH_LONG)
                                    .show();
                        }
                    } else {
                        // Permiso aceptado

                        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
                            takePicture();
                        }
                    }

                }
            });
        }

        Button buttonGallery = (Button) findViewById(R.id.buttonGallery);
        buttonGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int WriteExternalStoragePermission = ContextCompat.checkSelfPermission(myContext, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                Log.d("MainActivity", "WRITE_EXTERNAL_STORAGE Permission: " + WriteExternalStoragePermission);

                if (WriteExternalStoragePermission != PackageManager.PERMISSION_GRANTED) {
                    // Permiso denegado
                    // A partir de Marshmallow (6.0) se pide aceptar o rechazar el permiso en tiempo de ejecución
                    // En las versiones anteriores no es posible hacerlo
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        ActivityCompat.requestPermissions(AnyadirEditarActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, CODE_WRITE_EXTERNAL_STORAGE_PERMISSION_CAMERA);
                        // Una vez que se pide aceptar o rechazar el permiso se ejecuta el método "onRequestPermissionsResult" para manejar la respuesta
                        // Si el usuario marca "No preguntar más" no se volverá a mostrar este diálogo
                    } else {
                        Snackbar.make(constraintLayoutAnyadirActivity, getResources().getString(R.string.write_permission_denied), Snackbar.LENGTH_LONG)
                                .show();
                    }
                } else {
                    // Permiso aceptado
                    selectPicture();
                }


            }
        });
        //====== codigo importar imágenes:end ======

        Button buttonEliminar = (Button) findViewById(R.id.buttonEliminar);
        buttonEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageViewPregunta.setImageDrawable(null);
            }
        });


    }

    private void takePicture() {
        try {
            // Se crea el directorio para las fotografías
            File dirFotos = new File(pathFotos);
            dirFotos.mkdirs();

            // Se crea el archivo para almacenar la fotografía
            File fileFoto = File.createTempFile(getFileCode(), ".jpg", dirFotos);

            // Se crea el objeto Uri a partir del archivo
            // A partir de la API 24 se debe utilizar FileProvider para proteger
            // con permisos los archivos creados
            // Con estas funciones podemos evitarlo
            // https://stackoverflow.com/questions/42251634/android-os-fileuriexposedexception-file-jpg-exposed-beyond-app-through-clipdata
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
            uri = Uri.fromFile(fileFoto);
            Log.d(TAG, uri.getPath().toString());

            // Se crea la comunicación con la cámara
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // Se le indica dónde almacenar la fotografía
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            // Se lanza la cámara y se espera su resultado
            startActivityForResult(cameraIntent, REQUEST_CAPTURE_IMAGE);

        } catch (IOException ex) {

            Log.d(TAG, "Error: " + ex);
            CoordinatorLayout coordinatorLayout = findViewById(R.id.coordinatorLayout);
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, getResources().getString(R.string.error_files), Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }

    private void selectPicture() {
        // Se le pide al sistema una imagen del dispositivo
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(intent, getResources().getString(R.string.choose_picture)),
                REQUEST_SELECT_IMAGE);
    }

    private String getFileCode() {
        // Se crea un código a partir de la fecha y hora
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss", java.util.Locale.getDefault());
        String date = dateFormat.format(new Date());
        // Se devuelve el código
        return "pic_" + date;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {

            case (REQUEST_CAPTURE_IMAGE):
                if (resultCode == Activity.RESULT_OK) {
                    // Se carga la imagen desde un objeto URI al imageView
                    ImageView imageView = findViewById(R.id.imageView);
                    imageView.setImageURI(uri);
                    imageView.setRotation(90);

                    //Bitmap
                    BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
                    bitmap = drawable.getBitmap();

                    // Se le envía un broadcast a la Galería para que se actualice
                    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    mediaScanIntent.setData(uri);
                    sendBroadcast(mediaScanIntent);
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    // Se borra el archivo temporal
                    File file = new File(uri.getPath());
                    file.delete();
                }
                break;

            case (REQUEST_SELECT_IMAGE):
                if (resultCode == Activity.RESULT_OK) {
                    // Se carga la imagen desde un objeto Bitmap
                    Uri selectedImage = data.getData();
                    String selectedPath = selectedImage.getPath();

                    if (selectedPath != null) {
                        // Se leen los bytes de la imagen
                        InputStream imageStream = null;
                        try {
                            imageStream = getContentResolver().openInputStream(selectedImage);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }

                        // Se transformam los bytes de la imagen a un Bitmap
                        Bitmap bmp = BitmapFactory.decodeStream(imageStream);

                        // Se carga el Bitmap en el ImageView
                        ImageView imageView = findViewById(R.id.imageView);
                        imageView.setImageBitmap(bmp);

                        //Bitmap
                        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
                        bitmap = drawable.getBitmap();
                    }
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case CODE_WRITE_EXTERNAL_STORAGE_PERMISSION_CAMERA:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permiso aceptado
                    Snackbar.make(constraintLayoutAnyadirActivity, getResources().getString(R.string.write_permission_accepted), Snackbar.LENGTH_LONG)
                            .show();
                    if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
                        takePicture();
                    }
                } else {
                    // Permiso rechazado
                    Snackbar.make(constraintLayoutAnyadirActivity, getResources().getString(R.string.write_permission_not_accepted), Snackbar.LENGTH_LONG)
                            .show();
                }
                break;

            case CODE_WRITE_EXTERNAL_STORAGE_PERMISSION_GALLERY:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permiso aceptado
                    Snackbar.make(constraintLayoutAnyadirActivity, getResources().getString(R.string.write_permission_accepted), Snackbar.LENGTH_LONG)
                            .show();
                    if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
                        selectPicture();
                    }
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

    public static String camaraImagen64(Bitmap bm) {

        String encodedImage = "";

        if (bm == null) {
            return encodedImage;
        } else {
            Bitmap resized = Bitmap.createScaledBitmap(bm, 200, 200, true);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            resized.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] b = baos.toByteArray();
            encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
            return encodedImage;
        }
    }

    public static String galeriaImagen64(Bitmap bm) {
        String encodedImage = null;
        if (bm != null) {
            Bitmap resized = Bitmap.createScaledBitmap(bm, 200, 200, true);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            resized.compress(Bitmap.CompressFormat.JPEG, 100, baos);//bmisthebitmapobject
            byte[] b = baos.toByteArray();
            encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
        } else {
            return "";
        }
        return encodedImage;
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
