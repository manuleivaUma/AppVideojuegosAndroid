package com.example.appvideojuegos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DbJuego extends DbHelper{
    // TODO : Poner solo un DBHelper

    Context context;

    public DbJuego(@Nullable Context context) {
        super(context);
        this.context = context;
    }

    public long crearJuego(Integer id_usuario, Integer id_juego, String estado, Integer valoracion){
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("usuario_id", id_usuario);
        values.put("juego_id", id_juego);
        values.put("estado", estado);
        values.put("valoracion", valoracion);
        long idQuery = -1;
        try {
            idQuery = db.insert(DbContract.ListaEntry.TABLE_NAME, null, values);
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            db.close();
        }
        return idQuery;
    }

    public long comprobarJuego(Integer id_usuario, Integer id_juego){
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] columns = {DbContract.ListaEntry.COLUMN_usuario};
        String where = DbContract.ListaEntry.COLUMN_usuario + " = ? and " +
                        DbContract.ListaEntry.COLUMN_juego + " = ?";
        String[] whereArgs = {
                id_usuario.toString(),
                id_juego.toString()
        };
        long idQuery = -1;
        Cursor cursor = db.query(DbContract.ListaEntry.TABLE_NAME, columns, where, whereArgs, null,
                null, null);
        try {
            if (cursor.moveToNext()){
                idQuery = cursor.getInt(0);
            } else {
                return -1;
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            cursor.close();
            db.close();
        }
        return idQuery;
    }

    public Map<String, ArrayList<String>> getJuegosEstadoValoracion(Integer id_usuario){
        ArrayList<String> listaId = new ArrayList<>();
        ArrayList<String> listaEstado = new ArrayList<>();
        ArrayList<String> listaValoracion = new ArrayList<>();
        Map<String, ArrayList<String>> res = new HashMap<>();

        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] columns = {DbContract.ListaEntry.COLUMN_juego, DbContract.ListaEntry.COLUMN_estado,
                                DbContract.ListaEntry.COLUMN_valoracion};
        String where = DbContract.ListaEntry.COLUMN_usuario + " = ?";
        String[] whereArgs = {id_usuario.toString()};
        Cursor cursor = db.query(DbContract.ListaEntry.TABLE_NAME, columns, where, whereArgs, null,
                null, null);
        try {
            while (cursor.moveToNext()){
                Integer id = cursor.getInt(0);
                listaId.add(id.toString());
                listaEstado.add(cursor.getString(1));
                Integer valoracion = cursor.getInt(2);
                listaValoracion.add(valoracion.toString());
            }
            res.put("listaId", listaId);
            res.put("listaEstado", listaEstado);
            res.put("listaValoracion", listaValoracion);
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            cursor.close();
            db.close();
        }
        return res;
    }

    public long editarJuego(Integer id_usuario, Integer id_juego, String estado, Integer valoracion){
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("estado", estado);
        values.put("valoracion", valoracion);
        String where = DbContract.ListaEntry.COLUMN_usuario + " = ? and " +
                        DbContract.ListaEntry.COLUMN_juego + " = ?";
        String[] whereArgs = {id_usuario.toString(), id_juego.toString()};
        long idQuery = -1;
        try {
            idQuery = db.update(DbContract.ListaEntry.TABLE_NAME, values, where, whereArgs);
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            db.close();
        }
        return idQuery;
    }

    public long borrarJuego(Integer id_usuario, Integer id_juego){
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String where = DbContract.ListaEntry.COLUMN_usuario + " = ? and " +
                        DbContract.ListaEntry.COLUMN_juego + " = ?";
        String[] whereArgs = {id_usuario.toString(), id_juego.toString()};
        long idQuery = -1;
        try {
            idQuery = db.delete(DbContract.ListaEntry.TABLE_NAME, where, whereArgs);
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            db.close();
        }
        return idQuery;
    }
}
