package com.example.appvideojuegos;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import java.util.Locale;

public class RegistroUsuario extends AppCompatActivity {
    private EditText txNombre, txPassword, txEmail, txApellido;
    private Button button;
    Switch idioma;
    Boolean switchActivo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_registro_usuario);

        button = this.findViewById(R.id.crear);
        txNombre = this.findViewById(R.id.editTextTextPersonName);
        txPassword = this.findViewById(R.id.editTextTextPassword);
        txEmail = this.findViewById(R.id.editTextTextPersonEmail);
        txApellido = this.findViewById(R.id.editTextTextPersonApellido);

        switchActivo = getIntent().getBooleanExtra("Ingles", false);

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
        txApellido.setText("");
        txEmail.setText("");
        Intent i = new Intent(this,LoginUsuario.class);
        i.putExtra("Ingles", switchActivo);
        startActivity(i);
    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        idioma = menu.findItem(R.id.app_bar_switch).getActionView().findViewById(R.id.action_switch);
        idioma.setChecked(switchActivo);
        idioma.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Boolean ingles;
                if (isChecked){
                    cambiarIdioma("en");
                    ingles = true;

                } else {
                    cambiarIdioma("es");
                    ingles = false;
                }
                Intent intent = new Intent(RegistroUsuario.this, RegistroUsuario.class);
                intent.putExtra("Ingles", ingles);
                startActivity(intent);
                finish();
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void cambiarIdioma(String lang){
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("Lang", lang);
        Log.d("Lang_cambiar", lang);
        editor.apply();
    }

    public void loadLocale(){
        SharedPreferences prefs = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String lang = prefs.getString("Lang", "");
        Log.d("Lang_cargar", lang);
        cambiarIdioma(lang);
    }
}