package com.example.appvideojuegos;

import android.provider.BaseColumns;

public final class DbContract {
    private DbContract(){}

    public static abstract class UsuarioEntry implements BaseColumns {
        public static final String TABLE_NAME = "t_usuario";

        public static final String COLUMN_id = "id";
        public static final String COLUMN_email = "email";
        public static final String COLUMN_password = "password";
        public static final String COLUMN_nombre = "nombre";
        public static final String COLUMN_apellido = "apellido";
    }

    public static abstract class ListaEntry implements BaseColumns {
        public static final String TABLE_NAME = "t_lista";

        public static final String COLUMN_usuario = "usuario_id";
        public static final String COLUMN_juego = "juego_id";
        public static final String COLUMN_estado = "estado";
        public static final String COLUMN_valoracion = "valoracion";
    }
}
