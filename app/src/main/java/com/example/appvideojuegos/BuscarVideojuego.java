package com.example.appvideojuegos;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BuscarVideojuego extends AppCompatActivity {

    Dialog dialogo;
    Button button;
    TextView nombre;
    ListView listView;
    ListView listViewRes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_videojuego);

        listView = this.findViewById(R.id.listaBuscar);
        listViewRes = this.findViewById(R.id.listaRes);
        button = this.findViewById(R.id.button);
        nombre = this.findViewById(R.id.editTextBuscarJuego);

        // MOSTRAR JUEGOS FAMOSOS
        String url = "https://api.rawg.io/api/games?key=1dbefa6d583c4d62a1be47db82ff82ec&dates=2021-01-01,2021-12-31&ordering=-added&page_size=5";
        StringRequest postRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
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

                    // Añadimos a las listas correspondientes
                    nombres.add(name);
                    fotos.add(background_image);
                    puntuaciones.add(metacritic);
                    fechas.add(released);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // Actualizar la listView
        AdaptadorItems adaptador = new AdaptadorItems(this, nombres, fotos, puntuaciones, fechas);
        listView.setAdapter(adaptador);
    }

    public void BuscarVideojuego(android.view.View view){
        String nombre_juego = this.nombre.getText().toString();

        if (nombre_juego == ""){
            Toast.makeText(this, "El nombre del juego está en blanco", Toast.LENGTH_SHORT).show();
        } else {
            // TODO: Comprobar la entrada

            String url = "https://api.rawg.io/api/games?key=1dbefa6d583c4d62a1be47db82ff82ec&search="+nombre_juego;
            StringRequest postRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
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

                    // Añadimos a las listas correspondientes
                    nombresRes.add(name);
                    fotosRes.add(background_image);
                    puntuacionesRes.add(metacritic);
                    fechasRes.add(released);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // Actualizar la listViewRes
        AdaptadorItems adaptador = new AdaptadorItems(this, nombresRes, fotosRes, puntuacionesRes, fechasRes);
        listViewRes.setAdapter(adaptador);
    }
}