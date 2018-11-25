package com.balta.atested;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ListadoActivity extends AppCompatActivity {

    private static final String LOGTAG = "ListadoActivity";
    private ArrayList<Pregunta> preguntas;
    private Context myContext;
    private Bundle b;
    private Intent restart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado);

        myContext = this;
        b = new Bundle();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent listadoIntent = new Intent(ListadoActivity.this, AnyadirEditarActivity.class);
                startActivity(listadoIntent);
            }
        });
    }


    @Override
    protected void onResume() {
        MyLog.d(LOGTAG, "Iniciando OnResume...");
        super.onResume();

        // Crea una lista con los elementos a mostrar
        preguntas = Repositorio.recuperarDatos(myContext);

        final TextView noPregunta = (TextView) findViewById(R.id.textViewNoPreguntas);

        if (preguntas.isEmpty()) {
            noPregunta.setVisibility(View.VISIBLE);
        } else {
            noPregunta.setVisibility(View.INVISIBLE);

            // Inicializa el RecyclerView
            final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

            ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {

                @Override
                public boolean onMove(RecyclerView recyclerView,RecyclerView.ViewHolder viewHolder,RecyclerView.ViewHolder target){
                    return false;
                }

                @Override
                public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
                    final int position = viewHolder.getAdapterPosition();

                    if(direction == ItemTouchHelper.LEFT){
                        // Recuperación de la vista del AlertDialog a partir del layout de la Actividad
                        LayoutInflater layoutActivity = LayoutInflater.from(myContext);
                        View viewAlertDialog = layoutActivity.inflate(R.layout.alert_dialog_borrar_pregunta, null);

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
                                .setPositiveButton(getResources().getString(R.string.delete_pregunta),
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialogBox, int id) {
                                                Repositorio.borrarPreguntaEditada(myContext, preguntas.get(position).getCodigo());
                                                restart = getIntent();
                                                finish();
                                                startActivity(restart);
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

                    if(direction == ItemTouchHelper.RIGHT){
                        // Recuperación de la vista del AlertDialog a partir del layout de la Actividad
                        LayoutInflater layoutActivity = LayoutInflater.from(myContext);
                        View viewAlertDialog = layoutActivity.inflate(R.layout.alert_dialog_borrar_pregunta, null);

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
                                .setPositiveButton(getResources().getString(R.string.delete_pregunta),
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialogBox, int id) {
                                                Repositorio.borrarPreguntaEditada(myContext, preguntas.get(position).getCodigo());
                                                restart = getIntent();
                                                finish();
                                                startActivity(restart);
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
                }
            };

            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
            itemTouchHelper.attachToRecyclerView(recyclerView);




            // Crea el Adaptador con los datos de la lista anterior
            PreguntaAdapter adaptador = new PreguntaAdapter(preguntas);

            // Asocia el elemento de la lista con una acción al ser pulsado
            adaptador.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Acción al pulsar el elemento
                    int position = recyclerView.getChildAdapterPosition(v);

                    Intent editarpregunta= new Intent(ListadoActivity.this, AnyadirEditarActivity.class);

                    //Creamos la información a pasar entre actividades
                    b.putInt("codigo", preguntas.get(position).getCodigo());

                    //Añadimos la información al intent
                    editarpregunta.putExtras(b);

                    //Iniciamos la nueva actividad
                    startActivity(editarpregunta);
                }
            });

            // Asocia el Adaptador al RecyclerView
            recyclerView.setAdapter(adaptador);

            // Muestra el RecyclerView en vertical
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
    }

    @Override
    protected void onStart() {
        MyLog.d(LOGTAG, "Iniciando OnStart...");
        super.onStart();
    }

    @Override
    protected void onRestart() {
        MyLog.d(LOGTAG, "Iniciando OnRestart...");
        super.onRestart();
    }

    @Override
    protected void onStop() {
        MyLog.d(LOGTAG, "Iniciando OnStop...");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        MyLog.d(LOGTAG, "Iniciando OnDestroy...");
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        MyLog.d(LOGTAG, "Iniciando OnPause...");
        super.onPause();
    }
}

