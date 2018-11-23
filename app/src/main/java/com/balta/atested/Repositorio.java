package com.balta.atested;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class Repositorio {

    BaseDeDatesSQLiteHelper bd;

    public static boolean insertar(Pregunta p, Context contexto) {
        boolean valor = true;

        //Abrimos la base de datos 'DBUsuarios' en modo escritura
        BaseDeDatesSQLiteHelper bdsql =
                new BaseDeDatesSQLiteHelper(contexto, "DBPreguntas", null, 1);

        SQLiteDatabase db = bdsql.getWritableDatabase();

        //Si hemos abierto correctamente la base de datos
        if (db != null) {

            //Insertamos los datos en la tabla Usuarios
            db.execSQL("INSERT INTO Pregunta (enunciado, categoria, respuestaCorrecta, respuestaIncorrecta1 , respuestaIncorrecta2 , respuestaIncorrecta3 ) " +
                    "VALUES ('" + p.getEnunciado() + "', '" + p.getCategoria() + "', '" + p.getRespuestaCorrecta() + "', '" + p.getRespuestaIncorrecta1() + "', '" + p.getRespuestaIncorrecta2() + "', '" + p.getRespuestaIncorrecta3() + "')");

            //Cerramos la base de datos
            db.close();
        } else {
            valor = false;
        }
        return valor;


    }

    public static ArrayList<Pregunta> recuperarDatos(Context contexto) {

        //Abrimos la base de datos 'DBUsuarios' en modo lectura
        BaseDeDatesSQLiteHelper bdsql =
                new BaseDeDatesSQLiteHelper(contexto, "DBPreguntas", null, 1);

        SQLiteDatabase db = bdsql.getReadableDatabase();

        Cursor c = db.rawQuery(" SELECT * FROM Pregunta ", null);

        ArrayList<Pregunta> ArrayListPregunta = new ArrayList<Pregunta>();

        if (c.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                ArrayListPregunta.add(new Pregunta(c.getString(c.getColumnIndex("codigo")), c.getString(c.getColumnIndex("enunciado")), c.getString(c.getColumnIndex("categoria")), c.getString(c.getColumnIndex("respuestaCorrecta")), c.getString(c.getColumnIndex("respuestaIncorrecta1")), c.getString(c.getColumnIndex("respuestaIncorrecta2")), c.getString(c.getColumnIndex("respuestaIncorrecta3"))));
            } while (c.moveToNext());
        }

        db.close();

        return ArrayListPregunta;
    }

    public static Pregunta recuperarPreguntaSelec(Context contexto, int codigo) {

        //Abrimos la base de datos 'DBUsuarios' en modo lectura
        BaseDeDatesSQLiteHelper bdsql =
                new BaseDeDatesSQLiteHelper(contexto, "DBPreguntas", null, 1);

        SQLiteDatabase db = bdsql.getReadableDatabase();

        Cursor c = db.rawQuery(" SELECT * FROM Pregunta WHERE codigo = " + codigo, null);

        Pregunta preguntaSelec = new Pregunta(null, null, null, null, null, null);

        if (c.moveToFirst()) {
            preguntaSelec = new Pregunta(c.getString(c.getColumnIndex("codigo")), c.getString(c.getColumnIndex("enunciado")), c.getString(c.getColumnIndex("categoria")), c.getString(c.getColumnIndex("respuestaCorrecta")), c.getString(c.getColumnIndex("respuestaIncorrecta1")), c.getString(c.getColumnIndex("respuestaIncorrecta2")), c.getString(c.getColumnIndex("respuestaIncorrecta3")));
        }

        db.close();

        return preguntaSelec;
    }

    public static ArrayList<String> recuperarCategorias(Context contexto) {

        //Abrimos la base de datos 'DBUsuarios' en modo lectura
        BaseDeDatesSQLiteHelper bdsql =
                new BaseDeDatesSQLiteHelper(contexto, "DBPreguntas", null, 1);

        SQLiteDatabase db = bdsql.getReadableDatabase();

        Cursor c = db.rawQuery(" SELECT DISTINCT categoria FROM Pregunta ", null);

        ArrayList<String> ArrayListCategoria = new ArrayList<String>();

        if (c.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                ArrayListCategoria.add(c.getString(0));
            } while (c.moveToNext());
        }

        db.close();

        return ArrayListCategoria;
    }

    public static void borrarPreguntaEditada(Context contexto, int codigo) {

        //Abrimos la base de datos 'DBUsuarios' en modo lectura
        BaseDeDatesSQLiteHelper bdsql =
                new BaseDeDatesSQLiteHelper(contexto, "DBPreguntas", null, 1);

        SQLiteDatabase db = bdsql.getReadableDatabase();

        db.execSQL(" DELETE FROM Pregunta WHERE codigo = " + codigo);

    }

    public static void actualizarPreguntaEditada(Context contexto, int codigo, Pregunta p) {

        //Abrimos la base de datos 'DBUsuarios' en modo lectura
        BaseDeDatesSQLiteHelper bdsql =
                new BaseDeDatesSQLiteHelper(contexto, "DBPreguntas", null, 1);

        SQLiteDatabase db = bdsql.getReadableDatabase();

        db.execSQL(" UPDATE Pregunta SET " +
                "enunciado = '" + p.getEnunciado() + "' , " +
                "categoria = '" + p.getCategoria() + "' , " +
                "respuestaCorrecta = '" + p.getRespuestaCorrecta() + "' , " +
                "respuestaIncorrecta1 = '" + p.getRespuestaIncorrecta1() + "' , " +
                "respuestaIncorrecta2 = '" + p.getRespuestaIncorrecta2() + "' , " +
                "respuestaIncorrecta3 = '" + p.getRespuestaIncorrecta3()  +"' WHERE codigo = " + codigo);
    }
}