package com.example.appvideojuegos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 4;
    private static final String DATABASE_NAME = "AppVideojuegosDB";
    public static final String TABLE_USUARIO = "t_usuario";
    public static final String TABLE_JUEGO = "t_lista";

    public DbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_USUARIO + "(" +
                "id INTEGER PRIMARY KEY ," +
                "email TEXT NOT NULL UNIQUE," +
                "password TEXT NOT NULL," +
                "nombre TEXT NOT NULL," +
                "apellido TEXT NOT NULL)");
        db.execSQL("CREATE TABLE " + TABLE_JUEGO + "(" +
                "usuario_id	INTEGER NOT NULL, " +
                "juego_id	INTEGER NOT NULL, " +
                "estado	TEXT NOT NULL, " +
                "valoracion	NUMERIC NOT NULL, " +
                "PRIMARY KEY(usuario_id,juego_id))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USUARIO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_JUEGO);
        onCreate(db);
    }
}
