package com.balta.atested;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import static com.balta.atested.R.id.contador1;

public class ResumenActivity extends AppCompatActivity {

    private static final String LOGTAG = "ResumenActivity";
    private Context myContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resumen);

        myContext = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        importandoXML();
    }

    @Override
    protected void onResume() {
        MyLog.d(LOGTAG, Constantes.resume);
        super.onResume();

        TextView contador1 = findViewById(R.id.contador1);
        TextView contador2 = findViewById(R.id.contador2);
        TextView contador3 = findViewById(R.id.contador3);
        TextView contador4 = findViewById(R.id.contador4);

        contador1.setText(Repositorio.contarPregunta(myContext));
        contador2.setText(Repositorio.contarCategoria(myContext));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_resumen, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_listado:
                Log.i("ActionBar", "Listado de Pregunta");
                Intent listadoIntent = new Intent(ResumenActivity.this, ListadoActivity.class);
                startActivity(listadoIntent);
                return true;

            case R.id.action_configuracion:
                Log.i("ActionBar", "Configuracion");
                ;
                return true;

            case R.id.action_acercade:
                Log.i("ActionBar", "Acerca De");
                Intent acercadeIntent = new Intent(ResumenActivity.this, AcercaActivity.class);
                startActivity(acercadeIntent);
                return true;

            case R.id.action_importar:
                Log.i("ActionBar", "Acerca De");
                return true;

            case R.id.action_exportar:
                Log.i("ActionBar", "Acerca De");
                exportarXML();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStart() {
        MyLog.d(LOGTAG, Constantes.start);
        super.onStart();
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

    private void exportarXML() {
        String ruta = Environment.getExternalStorageDirectory().toString();
        File directorio = new File(ruta + "/preguntasExportar");
        String fname = "listadopreguntas.xml";
        File file = new File(directorio, fname);
        try {


            if (!directorio.exists()) {
                directorio.mkdirs();

            }
            if (file.exists())
                file.delete();


            FileWriter fw = new FileWriter(file);
            //Escribimos en el fichero un String
            fw.write(Repositorio.CreateXMLString(myContext));


            //Cierro el stream
            fw.close();

        } catch (Exception ex) {
            MyLog.e("Ficheros", "Error al escribir fichero a memoria interna");
        }


        String cadena = directorio.getAbsolutePath() + "/" + fname;
        Uri path = Uri.parse("file://" + cadena);

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", "abjimenez@iesfranciscodelosrios.es", null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Exportación de preguntas XML");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Preguntas XML para Moodle.");
        emailIntent.putExtra(Intent.EXTRA_STREAM, path);
        startActivity(Intent.createChooser(emailIntent, "Send email..."));
    }

    public void importandoXML() {

        //Creamos las variables de la pregunta que vamos a importar
        Pregunta p;
        String enunciado = null;
        String categoria = null;
        String respuestacorrecta = null;
        String respuestaincorrecta1 = null;
        String respuestaincorrecta2 = null;
        String respuestaincorrecta3 = null;
        String imagen = null;

        String text = null;
        int contador = 0;

        //Recibimos el intent
        Intent receivedIntent = getIntent();

        //Si el intent es distinto de null
        if (receivedIntent != null) {
            //Recogemos la accion del intent
            String receivedAction = receivedIntent.getAction();

            //Si la accion del inten es igual a android.intent.action.SEND
            //Se ejecutará la lectura del archivo
            if (receivedAction == "android.intent.action.SEND") {

                Uri data = receivedIntent.getParcelableExtra(Intent.EXTRA_STREAM);

                try {

                    InputStream fis = getContentResolver().openInputStream(data);
                    XmlPullParserFactory xppf = XmlPullParserFactory.newInstance();
                    xppf.setNamespaceAware(false);
                    XmlPullParser parser = xppf.newPullParser();
                    parser.setInput(fis, null);

                    parser.nextTag();
                    parser.require(XmlPullParser.START_TAG, null, "quiz");

                    //Leyendo el documento

                    int act;
                    String tag = "";
                    boolean enterQuestion = false;
                    int contadorRespuestas = 0;


                    while ((act = parser.next()) != XmlPullParser.END_DOCUMENT) {

                        switch (act) {
                            case XmlPullParser.START_TAG:

                                tag = parser.getName();
                                Log.d("CLIENTE RSS: ", tag);
                                if (tag.equals("question")) {
                                    enterQuestion = true;
                                }

                                if (tag.equals("file")) {
                                    imagen = parser.getAttributeValue(null, "name");
                                    //Mientras se arregla lo de la imagen la guardo como cadena vacia
                                    //imagen= "";
                                    System.out.println("Imagen: " + imagen);

                                }
                                break;

                            case XmlPullParser.TEXT:
                                if (tag.equals("category")) {
                                    categoria = parser.getText();
                                    System.out.println("Categoria: " + categoria);

                                }
                                if (tag.equals("name")) {
                                    enunciado = parser.getText();
                                    System.out.println("Enunciado: " + enunciado);

                                }
                                /*
                                if(tag.equals("file"))
                                {
                                    imagen= parser.getAttributeValue(null,"encoding");
                                    //Mientras se arregla lo de la imagen la guardo como cadena vacia
                                    imagen= "";
                                    System.out.println("Imagen: "+ imagen);

                                }
                                */
                                if (tag.equals("answer")) {
                                    //System.out.println("CONTADOR: "+ contadorRespuestas);

                                    if (contadorRespuestas == 0) {
                                        respuestacorrecta = parser.getText();
                                        System.out.println("Correcta: " + respuestacorrecta);
                                        contadorRespuestas++;

                                    } else if (contadorRespuestas == 1) {
                                        respuestaincorrecta1 = parser.getText();
                                        System.out.println("Incorrecta1: " + respuestaincorrecta1);
                                        contadorRespuestas++;

                                    } else if (contadorRespuestas == 2) {
                                        respuestaincorrecta2 = parser.getText();
                                        System.out.println("Incorrecta2: " + respuestaincorrecta2);
                                        contadorRespuestas++;

                                    } else if (contadorRespuestas == 3) {
                                        respuestaincorrecta3 = parser.getText();
                                        System.out.println("Incorrecta3: " + respuestaincorrecta3);
                                        contadorRespuestas++;

                                        //Como es el último dato que recuperamos de la pregunta la añadimos a la base de datos
                                        Pregunta nuevaPregunta = new Pregunta(enunciado, categoria, respuestacorrecta, respuestaincorrecta1, respuestaincorrecta2, respuestaincorrecta3, imagen);
                                        Repositorio.insertar(nuevaPregunta, myContext);
                                        System.out.println("Pregunta Añadida correctamente a la base de datos......................................");

                                    } else {
                                        System.out.println("Error al leer");
                                    }


                                }

                                tag = "";
                                break;

                            case XmlPullParser.END_TAG:
                                if (parser.getName().equals("question")) {
                                    //System.out.println("terminado");
                                    contadorRespuestas = 0;
                                    //System.out.println("cONTADOR EN TERMINADO: "+ contadorRespuestas);
                                }
                                break;
                        }
                    }

                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }
            }
        }


    }
}
