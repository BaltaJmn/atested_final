package com.balta.atested;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ListadoActivity extends AppCompatActivity {

    private static final String LOGTAG = "ListadoActivity";
    private ArrayList<Pregunta> preguntas;
    private Context myContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado);

        myContext = this;

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

            // Crea el Adaptador con los datos de la lista anterior
            PreguntaAdapter adaptador = new PreguntaAdapter(preguntas);

            // Asocia el elemento de la lista con una acción al ser pulsado
            adaptador.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Acción al pulsar el elemento
                    int position = recyclerView.getChildAdapterPosition(v);
                    /*Toast.makeText(ListadoActivity.this, "Posición: " + String.valueOf(position) + " Enunciado: " + preguntas.get(position).getEnunciado() + " Nombre: " + preguntas.get(position).getCategoria(), Toast.LENGTH_SHORT)
                            .show();*/

                    Intent editarpregunta= new Intent(ListadoActivity.this, AnyadirEditarActivity.class);

                    //Creamos la información a pasar entre actividades
                    Bundle b = new Bundle();
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
}

