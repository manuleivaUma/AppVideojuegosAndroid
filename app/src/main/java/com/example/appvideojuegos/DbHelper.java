package com.example.appvideojuegos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 5;
    private static final String DATABASE_NAME = "AppVideojuegosDB";

    public DbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + DbContract.UsuarioEntry.TABLE_NAME + "(" +
                DbContract.UsuarioEntry.COLUMN_id + " INTEGER PRIMARY KEY ," +
                DbContract.UsuarioEntry.COLUMN_email + " TEXT NOT NULL UNIQUE," +
                DbContract.UsuarioEntry.COLUMN_password + " TEXT NOT NULL," +
                DbContract.UsuarioEntry.COLUMN_nombre + " TEXT NOT NULL," +
                DbContract.UsuarioEntry.COLUMN_apellido + " TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + DbContract.ListaEntry.TABLE_NAME + "(" +
                DbContract.ListaEntry.COLUMN_usuario + " INTEGER NOT NULL, " +
                DbContract.ListaEntry.COLUMN_juego + " INTEGER NOT NULL, " +
                DbContract.ListaEntry.COLUMN_estado + " TEXT NOT NULL, " +
                DbContract.ListaEntry.COLUMN_valoracion + " NUMERIC NOT NULL, " +
                "PRIMARY KEY(" + DbContract.ListaEntry.COLUMN_usuario +
                "," + DbContract.ListaEntry.COLUMN_juego + "))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DbContract.UsuarioEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DbContract.ListaEntry.TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DbContract.UsuarioEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DbContract.ListaEntry.TABLE_NAME);
        onCreate(db);
    }
}
