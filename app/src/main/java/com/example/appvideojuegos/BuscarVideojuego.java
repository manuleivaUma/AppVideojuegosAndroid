package com.example.appvideojuegos;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BuscarVideojuego extends AppCompatActivity {
    Map<String,String> mapaid;
    Integer id;

    Button button;
    TextView nombre;
    ListView listView;
    ListView listViewRes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_videojuego);

        mapaid= Collections
                .singletonMap("key", "Value");
        mapaid = (Map) getIntent().getSerializableExtra("Mapa");
        Toast.makeText(this, mapaid.get("id") , Toast.LENGTH_SHORT).show();
        id = Integer.parseInt(mapaid.get("id"));

        listView = this.findViewById(R.id.listaBuscar);
        listViewRes = this.findViewById(R.id.listaRes);
        button = this.findViewById(R.id.button);
        nombre = this.findViewById(R.id.editTextBuscarJuego);

        // MOSTRAR JUEGOS FAMOSOS
        String url = "https://api.rawg.io/api/games?key=1dbefa6d583c4d62a1be47db82ff82ec&dates=2021-01-01,2021-12-31&ordering=-added&page_size=5";
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    tratarJson(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, error -> Log.d("error", error.toString()));
        Volley.newRequestQueue(this).add(postRequest);
    }

    private void tratarJson(JSONObject listaJuegos){
        ArrayList<String> nombres = new ArrayList<>();
        ArrayList<String> fotos = new ArrayList<>();
        ArrayList<String> fechas = new ArrayList<>();
        ArrayList<Integer> puntuaciones = new ArrayList<>();
        ArrayList<Integer> id_juegos = new ArrayList<>();

        if (listaJuegos != null) {
            // Accedemos a los resultados
            JSONArray resultados = null;
            try {
                resultados = listaJuegos.getJSONArray("results");
                for (int i = 0; i < resultados.length(); i++){
                    // Objeto Videojuego
                    JSONObject jsonVideojuego = resultados.getJSONObject(i);
                    String name = jsonVideojuego.getString("name");
                    String released = jsonVideojuego.getString("released");
                    String background_image = jsonVideojuego.getString("background_image");
                    int metacritic = jsonVideojuego.getString("metacritic") == "null" ? 0 : jsonVideojuego.getInt("metacritic");
                    int id = jsonVideojuego.getInt("id");

                    // Añadimos a las listas correspondientes
                    nombres.add(name);
                    fotos.add(background_image);
                    puntuaciones.add(metacritic);
                    fechas.add(released);
                    id_juegos.add(id);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // Actualizar la listView
        AdaptadorItems adaptador = new AdaptadorItems(this, nombres, fotos, puntuaciones,
                fechas, id_juegos, id);
        listView.setAdapter(adaptador);
    }

    public void BuscarVideojuego(android.view.View view){
        String nombre_juego = this.nombre.getText().toString();

        if (nombre_juego == ""){
            Toast.makeText(this, "El nombre del juego está en blanco",
                    Toast.LENGTH_SHORT).show();
        } else {
            // TODO: Comprobar la entrada

            String url = "https://api.rawg.io/api/games?key=1dbefa6d583c4d62a1be47db82ff82ec&search="+nombre_juego;
            StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        tratarJuego(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, error -> Log.d("error", error.toString()));
            Volley.newRequestQueue(this).add(postRequest);
        }
    }

    private void tratarJuego(JSONObject juegos) {
        ArrayList<String> nombresRes = new ArrayList<>();
        ArrayList<String> fotosRes = new ArrayList<>();
        ArrayList<String> fechasRes = new ArrayList<>();
        ArrayList<Integer> puntuacionesRes = new ArrayList<>();
        ArrayList<Integer> id_juegosRes = new ArrayList<>();

        if (juegos != null) {
            // Accedemos a los resultados
            JSONArray resultados = null;
            try {
                resultados = juegos.getJSONArray("results");
                for (int i = 0; i < resultados.length(); i++){
                    // Objeto Videojuego
                    JSONObject jsonVideojuego = resultados.getJSONObject(i);
                    String name = jsonVideojuego.getString("name");
                    String released = jsonVideojuego.getString("released");
                    String background_image = jsonVideojuego.getString("background_image");
                    int metacritic = jsonVideojuego.getString("metacritic") == "null" ? 0 : jsonVideojuego.getInt("metacritic");
                    int id = jsonVideojuego.getInt("id");

                    // Añadimos a las listas correspondientes
                    nombresRes.add(name);
                    fotosRes.add(background_image);
                    puntuacionesRes.add(metacritic);
                    fechasRes.add(released);
                    id_juegosRes.add(id);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // Actualizar la listViewRes
        AdaptadorItems adaptador = new AdaptadorItems(this, nombresRes, fotosRes,
                puntuacionesRes, fechasRes, id_juegosRes, id);
        listViewRes.setAdapter(adaptador);
    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.buscarVideojuego:
                mostrarBuscar();
                return true;
            case R.id.listaVideojuegos:
                mostrarLista();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void mostrarBuscar(){
        Intent intent = new Intent(this, BuscarVideojuego.class);
        intent.putExtra("Mapa", (Serializable) mapaid);
        startActivity(intent);
        finish();
    }

    private void mostrarLista(){
        Intent intent = new Intent(this, ListaUsuario.class);
        intent.putExtra("Mapa", (Serializable) mapaid);
        startActivity(intent);
        finish();
    }
}