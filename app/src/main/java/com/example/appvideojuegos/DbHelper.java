package com.example.appvideojuegos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "AppVideojuegosDB";
    public static final String TABLE_USUARIO = "t_usuario";
    public static final String TABLE_JUEGO = "t_juego";

    public DbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_USUARIO + "(" +
                "id INTEGER NOT NULL UNIQUE," +
                "username TEXT NOT NULL UNIQUE," +
                "password TEXT NOT NULL," +
                "PRIMARY KEY" + "(id,username) )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + TABLE_USUARIO);
        onCreate(db);
    }
}
