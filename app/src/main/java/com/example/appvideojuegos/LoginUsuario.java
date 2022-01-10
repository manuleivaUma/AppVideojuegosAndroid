package com.example.appvideojuegos;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.util.Locale;
import java.util.Map;

public class LoginUsuario extends AppCompatActivity {

    private EditText txPassword, txEmail;
    private Button button1, button2;
    private Switch idioma;

    // Objetos compartidos
    static String SHAREOBJ_ingles = "Ingles";
    static String SHAREOBJ_mapa = "Mapa";
    private Boolean switchActivo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_login_usuario);

        txPassword = this.findViewById(R.id.introducirContrasenia);
        txEmail = this.findViewById(R.id.introducirEmail);
        button1 = this.findViewById(R.id.Registrarse);
        button2 = this.findViewById(R.id.Aceptar);

        // Objetos compartidos
        switchActivo = (Boolean) SingletonMap.getInstance().get(LoginUsuario.SHAREOBJ_ingles);
        if (switchActivo == null){
            switchActivo = false;
            SingletonMap.getInstance().put(LoginUsuario.SHAREOBJ_ingles, switchActivo);
        }

        // Creamos BD si no existe
        DbHelper db = new DbHelper(LoginUsuario.this);

        // Botón Registrarse
        button1.setOnClickListener(v -> {
            Intent i = new Intent(this,RegistroUsuario.class);
            startActivity(i);
        });

        // Botón Login
        button2.setOnClickListener(v -> {
            DbUsuario dbUsuario = new DbUsuario(LoginUsuario.this);
            Map<String,String> mapaid = dbUsuario.buscarUsuario(txEmail.getText().toString(),txPassword.getText().toString());
            if (!mapaid.get("id").equals("-1")){
                if(!switchActivo) {
                    Toast.makeText(LoginUsuario.this, "Usuario válido", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(LoginUsuario.this, "Correct user", Toast.LENGTH_SHORT).show();
                }
                Intent i2 = new Intent(this,ListaUsuario.class);
                Log.d("Id", mapaid.toString());
                SingletonMap.getInstance().put(LoginUsuario.SHAREOBJ_mapa, mapaid);
                startActivity(i2);
            }else{
                if(!switchActivo) {
                    Toast.makeText(LoginUsuario.this, "Usuario no válido o contraseña incorrecta", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(LoginUsuario.this, "User does not exist or incorrect password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Switch elegir idioma
        if (!switchActivo){
            // Idioma español por defecto
            SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
            editor.putString("Lang", "es");
            editor.apply();
        }
    }

    //******************* MENU *******************//
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
                Intent intent = new Intent(LoginUsuario.this, LoginUsuario.class);
                SingletonMap.getInstance().put(LoginUsuario.SHAREOBJ_ingles, ingles);
                startActivity(intent);
                finish();
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    //******************* IDIOMAS *******************//
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
