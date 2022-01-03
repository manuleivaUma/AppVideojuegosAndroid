package com.example.appvideojuegos;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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


    }
}
