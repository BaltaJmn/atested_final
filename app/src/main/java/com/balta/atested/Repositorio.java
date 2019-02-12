package com.balta.atested;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Environment;
import android.util.Xml;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;

public class Repositorio {

    BaseDeDatesSQLiteHelper bd;

    public static boolean insertar(Pregunta p, Context contexto) {
        boolean valor = true;

        //Abrimos la base de datos 'DBUsuarios' en modo escritura
        BaseDeDatesSQLiteHelper bdsql =
                new BaseDeDatesSQLiteHelper(contexto, Constantes.nombreDB, null, 1);

        SQLiteDatabase db = bdsql.getWritableDatabase();

        //Si hemos abierto correctamente la base de datos
        if (db != null) {

            //Insertamos los datos en la tabla Usuarios
            db.execSQL("INSERT INTO Pregunta (enunciado, categoria, respuestaCorrecta, respuestaIncorrecta1 , respuestaIncorrecta2 , respuestaIncorrecta3, imagen ) " +
                    "VALUES ('" + p.getEnunciado() + "', '" + p.getCategoria() + "', '" + p.getRespuestaCorrecta() + "', '" + p.getRespuestaIncorrecta1() + "', '" + p.getRespuestaIncorrecta2() + "', '" + p.getRespuestaIncorrecta3() + "', '" + p.getImagen() + "')");

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
                new BaseDeDatesSQLiteHelper(contexto, Constantes.nombreDB, null, 1);

        SQLiteDatabase db = bdsql.getReadableDatabase();

        Cursor c = db.rawQuery(" SELECT * FROM Pregunta ", null);

        ArrayList<Pregunta> ArrayListPregunta = new ArrayList<Pregunta>();

        if (c.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                ArrayListPregunta.add(new Pregunta(c.getString(c.getColumnIndex("codigo")), c.getString(c.getColumnIndex("enunciado")), c.getString(c.getColumnIndex("categoria")), c.getString(c.getColumnIndex("respuestaCorrecta")), c.getString(c.getColumnIndex("respuestaIncorrecta1")), c.getString(c.getColumnIndex("respuestaIncorrecta2")), c.getString(c.getColumnIndex("respuestaIncorrecta3")), c.getString(c.getColumnIndex("imagen"))));
            } while (c.moveToNext());
        }

        db.close();

        return ArrayListPregunta;
    }

    public static Pregunta recuperarPreguntaSelec(Context contexto, int codigo) {

        //Abrimos la base de datos 'DBUsuarios' en modo lectura
        BaseDeDatesSQLiteHelper bdsql =
                new BaseDeDatesSQLiteHelper(contexto, Constantes.nombreDB, null, 1);

        SQLiteDatabase db = bdsql.getReadableDatabase();

        Cursor c = db.rawQuery(" SELECT * FROM Pregunta WHERE codigo = " + codigo, null);

        Pregunta preguntaSelec = new Pregunta(null, null, null, null, null, null, null);

        if (c.moveToFirst()) {
            preguntaSelec = new Pregunta(c.getString(c.getColumnIndex("codigo")), c.getString(c.getColumnIndex("enunciado")), c.getString(c.getColumnIndex("categoria")), c.getString(c.getColumnIndex("respuestaCorrecta")), c.getString(c.getColumnIndex("respuestaIncorrecta1")), c.getString(c.getColumnIndex("respuestaIncorrecta2")), c.getString(c.getColumnIndex("respuestaIncorrecta3")), c.getString(c.getColumnIndex("imagen")));
        }

        db.close();

        return preguntaSelec;
    }

    public static ArrayList<String> recuperarCategorias(Context contexto) {

        //Abrimos la base de datos 'DBUsuarios' en modo lectura
        BaseDeDatesSQLiteHelper bdsql =
                new BaseDeDatesSQLiteHelper(contexto, Constantes.nombreDB, null, 1);

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
                new BaseDeDatesSQLiteHelper(contexto, Constantes.nombreDB, null, 1);

        SQLiteDatabase db = bdsql.getReadableDatabase();

        db.execSQL(" DELETE FROM Pregunta WHERE codigo = " + codigo);

    }

    public static void actualizarPreguntaEditada(Context contexto, int codigo, Pregunta p) {

        //Abrimos la base de datos 'DBUsuarios' en modo lectura
        BaseDeDatesSQLiteHelper bdsql =
                new BaseDeDatesSQLiteHelper(contexto, Constantes.nombreDB, null, 1);

        SQLiteDatabase db = bdsql.getReadableDatabase();

        db.execSQL(" UPDATE Pregunta SET " +
                "enunciado = '" + p.getEnunciado() + "' , " +
                "categoria = '" + p.getCategoria() + "' , " +
                "respuestaCorrecta = '" + p.getRespuestaCorrecta() + "' , " +
                "respuestaIncorrecta1 = '" + p.getRespuestaIncorrecta1() + "' , " +
                "respuestaIncorrecta2 = '" + p.getRespuestaIncorrecta2() + "' , " +
                "respuestaIncorrecta3 = '" + p.getRespuestaIncorrecta3() + "' , " +
                "imagen = '" + p.getImagen() + "' WHERE codigo = " + codigo);
    }

    public static String contarCategoria(Context contexto) {
        int contador = 0;

        //Abrimos la base de datos 'DBUsuarios' en modo lectura
        BaseDeDatesSQLiteHelper bdsql =
                new BaseDeDatesSQLiteHelper(contexto, Constantes.nombreDB, null, 1);

        SQLiteDatabase db = bdsql.getReadableDatabase();

        Cursor c = db.rawQuery(" SELECT DISTINCT categoria FROM Pregunta ", null);

        if (c.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                contador++;
            } while (c.moveToNext());
        }

        db.close();

        return Integer.toString(contador);
    }

    public static String contarPregunta(Context contexto) {
        int contador = 0;

        //Abrimos la base de datos 'DBUsuarios' en modo lectura
        BaseDeDatesSQLiteHelper bdsql =
                new BaseDeDatesSQLiteHelper(contexto, Constantes.nombreDB, null, 1);

        SQLiteDatabase db = bdsql.getReadableDatabase();

        Cursor c = db.rawQuery(" SELECT * FROM Pregunta ", null);

        if (c.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                contador++;
            } while (c.moveToNext());
        }

        db.close();

        return Integer.toString(contador);
    }

    public static String CreateXMLString(Context contexto) throws IllegalArgumentException, IllegalStateException, IOException
    {
        ArrayList<Pregunta> ListaPreguntas = new ArrayList<Pregunta>();
        ListaPreguntas= Repositorio.recuperarDatos(contexto);


        XmlSerializer xmlSerializer = Xml.newSerializer();
        StringWriter writer = new StringWriter();

        xmlSerializer.setOutput(writer);

        //Start Document
        xmlSerializer.startDocument("UTF-8", true);
        xmlSerializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);




        //Open Tag <file>
        xmlSerializer.startTag("", "quiz");

        for (Pregunta p: ListaPreguntas) {
            //Categoria de cada pregunta

            xmlSerializer.startTag("", "question");
            xmlSerializer.attribute("", "type", p.getCategoria());

            xmlSerializer.startTag("", "category");
            xmlSerializer.text(p.getCategoria());
            xmlSerializer.endTag("", "category");

            xmlSerializer.endTag("", "question");

            //Pregunta de eleccion multiple

            xmlSerializer.startTag("", "question");
            xmlSerializer.attribute("", "type", "multichoice");

            xmlSerializer.startTag("", "name");
            xmlSerializer.text(p.getEnunciado());
            xmlSerializer.endTag("", "name");

            xmlSerializer.startTag("","questiontext");
            xmlSerializer.attribute("", "format", "html");
            xmlSerializer.text(p.getEnunciado());
            xmlSerializer.startTag("","file");
            xmlSerializer.attribute("", "name", "imagen_pregunta.jpg");
            xmlSerializer.attribute("", "path", "/");
            xmlSerializer.attribute("", "encoding", "base64");
            xmlSerializer.endTag("", "file");
            xmlSerializer.endTag("", "questiontext");

            xmlSerializer.startTag("","answernumbering");
            xmlSerializer.endTag("", "answernumbering");

            xmlSerializer.startTag("","answer");
            xmlSerializer.attribute("","fraction", "100");
            xmlSerializer.attribute("", "format", "html");
            xmlSerializer.text(p.getRespuestaCorrecta());
            xmlSerializer.endTag("", "answer");

            xmlSerializer.startTag("","answer");
            xmlSerializer.attribute("","fraction", "0");
            xmlSerializer.attribute("", "format", "html");
            xmlSerializer.text(p.getRespuestaIncorrecta1());
            xmlSerializer.endTag("", "answer");

            xmlSerializer.startTag("","answer");
            xmlSerializer.attribute("","fraction", "0");
            xmlSerializer.attribute("", "format", "html");
            xmlSerializer.text(p.getRespuestaIncorrecta2());
            xmlSerializer.endTag("", "answer");

            xmlSerializer.startTag("","answer");
            xmlSerializer.attribute("","fraction", "0");
            xmlSerializer.attribute("", "format", "html");
            xmlSerializer.text(p.getRespuestaIncorrecta3());
            xmlSerializer.endTag("", "answer");

            xmlSerializer.endTag("","question");
        }

        //end tag <file>
        xmlSerializer.endTag("","quiz");



        xmlSerializer.endDocument();

        return writer.toString();


    }
}