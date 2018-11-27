package com.balta.atested;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class PreguntaAdapter
        extends RecyclerView.Adapter<PreguntaAdapter.PreguntaViewHolder>
        implements View.OnClickListener {

    private ArrayList<Pregunta> preguntas;
    private View.OnClickListener listener;

    // Clase interna:
    // Se implementa el ViewHolder que se encargará
    // de almacenar la vista del elemento y sus datos
    public static class PreguntaViewHolder
            extends RecyclerView.ViewHolder {

        private TextView TextView_enunciado;
        private TextView TextView_categoria;
        private int contador;

        public PreguntaViewHolder(View itemView) {
            super(itemView);
            TextView_enunciado = (TextView) itemView.findViewById(R.id.TextView_enunciado);
            TextView_categoria = (TextView) itemView.findViewById(R.id.TextView_categoria);
        }

        public void PreguntaBind(Pregunta pregunta) {
            contador = pregunta.getEnunciado().length();

            //Trunca el enunciado de la pregunta
            if (contador > 20) {
                TextView_enunciado.setText(pregunta.getEnunciado().substring(0, 10) + "...");
            } else {
                TextView_enunciado.setText(pregunta.getEnunciado());
            }


            TextView_categoria.setText(pregunta.getCategoria());
        }
    }

    // Contruye el objeto adaptador recibiendo la lista de datos
    public PreguntaAdapter(@NonNull ArrayList<Pregunta> preguntas) {
        this.preguntas = preguntas;
    }

    // Se encarga de crear los nuevos objetos ViewHolder necesarios
    // para los elementos de la colección.
    // Infla la vista del layout, crea y devuelve el objeto ViewHolder
    @Override
    public PreguntaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row, parent, false);
        row.setOnClickListener(this);

        PreguntaViewHolder avh = new PreguntaViewHolder(row);
        return avh;
    }

    // Se encarga de actualizar los datos de un ViewHolder ya existente.
    @Override
    public void onBindViewHolder(PreguntaViewHolder viewHolder, int position) {
        Pregunta pregunta = preguntas.get(position);
        viewHolder.PreguntaBind(pregunta);
    }

    // Indica el número de elementos de la colección de datos.
    @Override
    public int getItemCount() {
        return preguntas.size();
    }

    // Asigna un listener al elemento
    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        if (listener != null)
            listener.onClick(view);
    }
}
