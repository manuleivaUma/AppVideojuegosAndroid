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
        Cursor fila = db.rawQuery("Select id from t_usuario where email = '" + email + "' and password= '"+ password +"'",null);

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
        int idint = Integer.parseInt(id);
        //System.out.println(idint);
        Cursor fila = db.rawQuery("Select email,nombre,apellido from t_usuario where id = '" + idint + "'" ,null);

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
            int random_int = (int)Math.floor(Math.random()*(999999-(0+1))+0);
            ContentValues values = new ContentValues();
            values.put("id", random_int);
            values.put("email", email);
            values.put("password", password);
            values.put("nombre", nombre);
            values.put("apellido", apellido);

            id = db.insert(DbContract.UsuarioEntry.TABLE_NAME, null, values);
            //Cursor c = db.rawQuery("INSERT INTO t_usuario (email,password,nombre,s) VALUES ('" + email + "' , '"+ password+ "' , '"+ nombre +"' , '"+ apellido +"')",null);
            //id = c.getInt(0);
        } catch (Exception exception) {
            exception.toString();
        }
        return id;
    }
}
