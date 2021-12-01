package com.example.appvideojuegos;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegistroUsuario extends AppCompatActivity {
    private EditText txNombre, txPassword, txEmail, txApellido;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_usuario);

        button = this.findViewById(R.id.crear);
        txNombre = this.findViewById(R.id.editTextTextPersonName);
        txPassword = this.findViewById(R.id.editTextTextPassword);
        txEmail = this.findViewById(R.id.editTextTextPersonEmail);
        txApellido = this.findViewById(R.id.editTextTextPersonApellido);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DbUsuario dbUsuario = new DbUsuario(RegistroUsuario.this);
                long id = dbUsuario.crearUsuario(txEmail.getText().toString(), txPassword.getText().toString(), txNombre.getText().toString(), txApellido.getText().toString());
                
                if (id > 0){
                    Toast.makeText(RegistroUsuario.this, "Usuario registrado", Toast.LENGTH_SHORT).show();
                    limpiar();
                } else {
                    Toast.makeText(RegistroUsuario.this, "Error al registrar el usuario", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void limpiar(){
        txPassword.setText("");
        txNombre.setText("");
    }
}