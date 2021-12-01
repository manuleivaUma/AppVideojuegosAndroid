package com.example.appvideojuegos;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

public class DbUsuario extends DbHelper {
    Context context;

    public DbUsuario (@Nullable Context context){
        super(context);
        this.context = context;
    }

    public long crearUsuario(String email, String password, String nombre, String apellidos){
        long id = -1;
        try {
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("username", email);
            values.put("password", password);
            values.put("nombre", nombre);
            values.put("apellidos", apellidos);

            id = db.insert(TABLE_USUARIO, null, values);
        } catch (Exception exception) {
            exception.toString();
        }
        return id;
    }
}
