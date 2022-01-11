package com.example.appvideojuegos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DbUsuario extends DbHelper {

    Context context;

    public DbUsuario (@Nullable Context context){
        super(context);
        this.context = context;
    }

    public Map<String, String> buscarUsuario(String email, String password){

        int id = -1;
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String[] columns = {DbContract.UsuarioEntry.COLUMN_id};
        String where = DbContract.UsuarioEntry.COLUMN_email + " = ? and " +
                DbContract.UsuarioEntry.COLUMN_password + " = ?";
        String[] whereArgs = {
                email.toString(),
                password.toString()
        };

        Cursor fila = db.query(DbContract.UsuarioEntry.TABLE_NAME, columns, where, whereArgs, null,
                null, null);

        if(fila.moveToFirst()){
            id = fila.getInt(0);
            System.out.println(id);
        }

        Map<String, String>
                map = Collections
                .singletonMap("id", Integer.toString(id));

        return map;
    };

    public Map<String, String> getDatosUsuario(String id){
        Map<String,String> m = new HashMap<>();

        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String[] columns = {DbContract.UsuarioEntry.COLUMN_email, DbContract.UsuarioEntry.COLUMN_nombre,DbContract.UsuarioEntry.COLUMN_apellido};
        String where = DbContract.UsuarioEntry.COLUMN_id + " = ? ";
        String[] whereArgs = {
                id.toString()
        };

        Cursor fila = db.query(DbContract.UsuarioEntry.TABLE_NAME, columns, where, whereArgs, null,
                null, null);

        fila.moveToFirst();

        m.put("email",fila.getString(0));
        m.put("nombre",fila.getString(1));
        m.put("apellido",fila.getString(2));

        return m;
    };

    public long crearUsuario(String email, String password, String nombre, String apellido){
        long id = -1;
        try {

            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put("email", email);
            values.put("password", password);
            values.put("nombre", nombre);
            values.put("apellido", apellido);

            id = db.insert(DbContract.UsuarioEntry.TABLE_NAME, null, values);
        } catch (Exception exception) {
            exception.toString();
        }
        return id;
    }
}
