package com.example.appvideojuegos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.Nullable;

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
}
