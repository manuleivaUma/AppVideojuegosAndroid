package com.example.appvideojuegos;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Collections;
import java.util.Map;

public class ListaUsuario extends AppCompatActivity {
    Map<String,String> mapaid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_usuario);

         mapaid= Collections
                .singletonMap("key", "Value");
        mapaid = (Map) getIntent().getSerializableExtra("Mapa");
        Toast.makeText(ListaUsuario.this, mapaid.get("id") , Toast.LENGTH_SHORT).show();




        DbUsuario dbUsuario = new DbUsuario(ListaUsuario.this);
        Map<String,String> m = dbUsuario.getDatosUsuario(mapaid.get("id"));

        TextView text=(TextView)findViewById(R.id.nombreusuario);
        text.setText(m.get("nombre") + m.get("apellido"));
    }
}
