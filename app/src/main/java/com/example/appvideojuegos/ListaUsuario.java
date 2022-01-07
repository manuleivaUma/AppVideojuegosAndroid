package com.example.appvideojuegos;

import android.content.Intent;
import android.os.Bundle;
import android.telecom.Call;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ListaUsuario extends AppCompatActivity {
    Map<String,String> mapaid;
    Integer id;
    ListView listaJuegos;
    Button boton;
    AdaptadorItemsLista adaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_usuario);

        mapaid= Collections
                .singletonMap("key", "Value");
        mapaid = (Map) getIntent().getSerializableExtra("Mapa");
        Toast.makeText(ListaUsuario.this, mapaid.get("id") , Toast.LENGTH_SHORT).show();

        listaJuegos = this.findViewById(R.id.listaJuegos);
        id = Integer.parseInt(mapaid.get("id"));
        boton = this.findViewById(R.id.button2);

        // Cargar info
        cargarInfo();
    }

    public void cargarInfo(){
        // Obtenemos la información del usuario
        DbUsuario dbUsuario = new DbUsuario(ListaUsuario.this);
        Map<String,String> m = dbUsuario.getDatosUsuario(id.toString());

        // Obtenemos la lista de juegos del usuario
        DbJuego dbJuego = new DbJuego(this);
        Map<String, ArrayList<String>> juegos;
        juegos = dbJuego.getJuegosEstadoValoracion(id);
        if (m != null){
            // Juegos obtenidos
            tratarJuegos(juegos);
        } else {
            // Error
            Toast.makeText(this, "Error al cargar la lista de juegos", Toast.LENGTH_SHORT).show();
        }

        TextView text=(TextView)findViewById(R.id.nombreusuario);
        text.setText(m.get("nombre") + " " + m.get("apellido"));
    }

    private void tratarJuegos(Map<String, ArrayList<String>> m){
        ArrayList<String> nombres = new ArrayList<>();
        ArrayList<String> fotos = new ArrayList<>();
        ArrayList<String> estado = new ArrayList<>();
        ArrayList<Integer> puntuaciones = new ArrayList<>();
        ArrayList<Integer> valoracion_personal = new ArrayList<>();
        ArrayList<Integer> id_juegos = new ArrayList<>();

        if (m != null){
            Log.i("Juegos", m.toString());
            estado = m.get("listaEstado");
            final int nJuegos = estado.size();
            // Tratamos los ArrayList<String>
            ArrayList<String> idString = m.get("listaId");
            ArrayList<String> val_personalString = m.get("listaValoracion");
            for (String id : idString) {
                id_juegos.add(Integer.parseInt(id));
            }
            for (String val : val_personalString) {
                valoracion_personal.add(Integer.parseInt(val));
            }

            // Buscamos la información de los juegos
            Map<Integer, ArrayList<String>> res = new HashMap<>();
            // Id - {Nombre - Foto - Valoracion}
            for (Integer id : id_juegos){
                // Búsqueda asíncrona (no mantiene el orden)
                buscarInformación(id, new CallBack(){
                    @Override
                    public void onSuccess(ArrayList<String> detalles){
                        ArrayList<String> listaInfo = new ArrayList<>();
                        Log.d("Res", detalles.toString());
                        // Nombre - Foto - Valoración
                        listaInfo.add(detalles.get(0));
                        listaInfo.add(detalles.get(1));
                        listaInfo.add(detalles.get(2));
                        res.put(id, listaInfo);
                        // Se ha cargado el último elemento, actualizamos la listView
                        if (res.size() == nJuegos){
                            for (Integer id : id_juegos){
                                // Añadimos a la lista en orden de id
                                nombres.add(res.get(id).get(0));
                                fotos.add(res.get(id).get(1));
                                puntuaciones.add(Integer.parseInt(res.get(id).get(2)));
                            }
                            adaptador.notifyDataSetChanged();
                        }
                    }
                });
            }
        }
        // Actualizar la listView
        adaptador = new AdaptadorItemsLista(this, nombres, fotos, estado,
                puntuaciones, valoracion_personal, id_juegos, id);
        listaJuegos.setAdapter(adaptador);
    }

    private ArrayList<String> buscarInformación(Integer id, final CallBack onCallBack){
        ArrayList<String> res = new ArrayList<>(); // nombre - foto - puntuacion
        String url = "https://api.rawg.io/api/games/"+ id +"?key=1dbefa6d583c4d62a1be47db82ff82ec&";
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String name = jsonObject.getString("name");
                            String background_image = jsonObject.getString("background_image");
                            String metacritic = jsonObject.getString("metacritic") == "null" ? "0" : jsonObject.getString("metacritic");
                            res.add(name);
                            res.add(background_image);
                            res.add(metacritic);
                            onCallBack.onSuccess(res);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, error -> Log.d("error", error.toString()));
        Volley.newRequestQueue(this).add(postRequest);
        return res;
    }

    public void Refrescar(View view){
        this.cargarInfo();
    }

    public interface CallBack {
        void onSuccess(ArrayList<String> detalles);
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
