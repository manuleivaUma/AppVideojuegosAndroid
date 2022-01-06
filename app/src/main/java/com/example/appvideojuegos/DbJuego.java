package com.example.appvideojuegos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DbJuego extends DbHelper{

    Context context;

    public DbJuego(@Nullable Context context) {
        super(context);
        this.context = context;
    }

    public long crearJuego(Integer id_usuario, Integer id_juego, String estado, Integer valoracion){
        long idQuery = -1;
        try {
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("usuario_id", id_usuario);
            values.put("juego_id", id_juego);
            values.put("estado", estado);
            values.put("valoracion", valoracion);

            idQuery = db.insert(TABLE_JUEGO, null, values);
        } catch (Exception e){
            e.printStackTrace();
        }
        return idQuery;
    }

    public long comprobarJuego(Integer id_usuario, Integer id_juego){
        long idQuery = -1;
        try {
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Cursor fila = db.rawQuery("SELECT usuario_id FROM " + TABLE_JUEGO +
                    " WHERE usuario_id = '" + id_usuario + "' and juego_id = '" + id_juego +
                    "'", null);

            fila.moveToFirst();
            idQuery = fila.getInt(0);
        } catch (Exception e){
            e.printStackTrace();
        }
        return idQuery;
    }

    public Map<String, ArrayList<String>> getJuegosEstadoValoracion(Integer id_usuario){
        ArrayList<String> listaId = new ArrayList<>();
        ArrayList<String> listaEstado = new ArrayList<>();
        ArrayList<String> listaValoracion = new ArrayList<>();
        Map<String, ArrayList<String>> m = new HashMap<>();

        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT juego_id,estado,valoracion FROM " + TABLE_JUEGO +
                " WHERE usuario_id = '" + id_usuario + "'", null);

        if (!cursor.moveToFirst()){
            return null;
        } else {
            // Recorremos los resultados
            cursor.moveToFirst();
            do {
                Integer id = cursor.getInt(0);
                listaId.add(id.toString());
                listaEstado.add(cursor.getString(1));
                Integer valoracion = cursor.getInt(2);
                listaValoracion.add(valoracion.toString());
            } while (cursor.moveToNext());
        }
        m.put("listaId", listaId);
        m.put("listaEstado", listaEstado);
        m.put("listaValoracion", listaValoracion);
        return m;
    }
}
