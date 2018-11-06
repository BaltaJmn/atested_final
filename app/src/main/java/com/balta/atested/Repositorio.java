package com.balta.atested;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class Repositorio {

    BaseDeDatesSQLiteHelper bd;

    public static boolean insertar(Pregunta p, Context contexto) {
        boolean valor = true;

        //Abrimos la base de datos 'DBUsuarios' en modo escritura
        BaseDeDatesSQLiteHelper bdsql =
                new BaseDeDatesSQLiteHelper(contexto, "DBUsuarios", null, 1);

        SQLiteDatabase db = bdsql.getWritableDatabase();

        //Si hemos abierto correctamente la base de datos
        if (db != null) {

                //Insertamos los datos en la tabla Usuarios
            db.execSQL("INSERT INTO Pregunta (enunciado, categoria, respuestaCorrecta, respuestaIncorrecta1 , respuestaIncorrecta2 , respuestaIncorrecta3 ) " +
                    "VALUES ('" + p.getEnunciado() + "', '" + p.getCategoria() + "', '"+ p.getRespuestaCorrecta()+"', '"+ p.getRespuestaIncorrecta1()+"', '"+p.getRespuestaIncorrecta2()+"', '"+p.getRespuestaIncorrecta3()+"')");

            //Cerramos la base de datos
            db.close();
        }else{valor=false;}
            return valor;


    }
}