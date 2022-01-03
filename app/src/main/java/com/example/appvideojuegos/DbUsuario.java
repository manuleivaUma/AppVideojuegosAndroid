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

    public long crearUsuario(String email, String password, String nombre, String apellido){
        long id = -1;
        try {
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            int random_int = (int)Math.floor(Math.random()*(999999-(0+1))+0);
            ContentValues values = new ContentValues();
            values.put("id", random_int);
            values.put("email", email);
            values.put("password", password);
            values.put("nombre", nombre);
            values.put("apellido", apellido);

            id = db.insert(TABLE_USUARIO, null, values);
        } catch (Exception exception) {
            exception.toString();
        }
        return id;
    }
}
