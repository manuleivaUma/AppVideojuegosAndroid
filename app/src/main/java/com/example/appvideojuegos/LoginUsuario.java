package com.example.appvideojuegos;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.util.Map;

public class LoginUsuario extends AppCompatActivity {

    private EditText txPassword, txEmail;
    Button button1, button2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_usuario);

        txPassword = this.findViewById(R.id.introducirContrasenia);
        txEmail = this.findViewById(R.id.introducirEmail);

        button1 = this.findViewById(R.id.Registrarse);
        button2 = this.findViewById(R.id.Aceptar);

        button1.setOnClickListener(v -> {
            Intent i = new Intent(this,RegistroUsuario.class);
            startActivity(i);
        });

        button2.setOnClickListener(v -> {
            DbUsuario dbUsuario = new DbUsuario(LoginUsuario.this);
            Map<String,String> mapaid = dbUsuario.buscarUsuario(txEmail.getText().toString(),txPassword.getText().toString());
            if (!mapaid.get("id").equals("-1")){
                Toast.makeText(LoginUsuario.this, "Usuario válido", Toast.LENGTH_SHORT).show();
                Intent i2 = new Intent(this,ListaUsuario.class);
                i2.putExtra("Mapa", (Serializable) mapaid);
                startActivity(i2);
            }else{
                Toast.makeText(LoginUsuario.this, "Usuario no válido", Toast.LENGTH_SHORT).show();
            }
        });

        // Idioma español por defecto
        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("Lang", "es");
        editor.apply();
    }
}
