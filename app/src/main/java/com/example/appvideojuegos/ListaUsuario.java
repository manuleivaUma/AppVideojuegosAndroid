package com.example.appvideojuegos;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import java.time.Clock;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ListaUsuario extends AppCompatActivity implements DialogFiltro.DialogFiltroListener {
    Map<String,String> mapaid;
    Integer id;
    ListView listaJuegos;
    Button boton, buttonFiltrar;
    AdaptadorItemsLista adaptador;
    Dialog dialogo;
    String nombre, puntuacion, estadoselec;

    TextView txtViewnombre,txtViewpuntuacion;

    @Override
    public void applyTexts(String n, String p,String s) {
        //Guardamos los filtros recibidos del dialogo en variables globales
        nombre = n;
        puntuacion = p;
        estadoselec = s;

        if(estadoselec.equals("Sin filtro estado")){
            estadoselec = "";
        }

        System.out.println("Nombre:" + nombre);
        cargarInfo();
    }

    DbJuego dbjuego;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_usuario);

        nombre = "";
        puntuacion = "";
        estadoselec = "";

        mapaid= Collections
                .singletonMap("key", "Value");
        mapaid = (Map) getIntent().getSerializableExtra("Mapa");
        Toast.makeText(ListaUsuario.this, mapaid.get("id") , Toast.LENGTH_SHORT).show();

        listaJuegos = this.findViewById(R.id.listaJuegos);
        id = Integer.parseInt(mapaid.get("id"));
        boton = this.findViewById(R.id.buttonfiltrar);
        buttonFiltrar = this.findViewById(R.id.buttonfiltrar);

        // Cargar info
        cargarInfo();

        buttonFiltrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                openDialog();
            }
        });
    }

    private void openDialog() {
        //Abrimos la ventana de filtrado
        DialogFiltro dialogFiltro = new DialogFiltro();
        dialogFiltro.show(getSupportFragmentManager(),"dialogFiltro");
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

    /*public void Popup(View v) {
        EditText txNombre, txPuntuacion;
        Button buttonPopup;
        TextView cerrar;

        dialogo = new Dialog(this);
        dialogo.setContentView(R.layout.popup_filtro);

        txNombre = dialogo.findViewById(R.id.filtroNombre);
        txPuntuacion = dialogo.findViewById(R.id.filtroPuntuacion);
        buttonPopup = dialogo.findViewById(R.id.botonPopup);
        cerrar = dialogo.findViewById(R.id.txt_close2);

        buttonPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbjuego.filtrarJuegos(txNombre.getText().toString(),txPuntuacion.getText().toString());
            }
        });

        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogo.dismiss();
            }
        });

    }*/

    private void tratarJuegos(Map<String, ArrayList<String>> m){
        ArrayList<String> nombres = new ArrayList<>();
        ArrayList<String> fotos = new ArrayList<>();
        ArrayList<String> estado = m != null ? m.get("listaEstado") : new ArrayList<>();
        ArrayList<Integer> puntuaciones = new ArrayList<>();
        ArrayList<Integer> valoracion_personal = new ArrayList<>();
        ArrayList<Integer> id_juegos = new ArrayList<>();

        if (m != null){
            Log.i("Juegos", m.toString());
            //estado = m.get("listaEstado");
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

            ArrayList<String> estado2 = new ArrayList<>();
            estado2 = estado;
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

                            int contador = 0;
                            int tamanio = nombres.size();

                            if(!nombre.equals("")){
                               while(contador < tamanio){
                                    if(!nombres.get(contador).toLowerCase(Locale.ROOT).contains(nombre.toLowerCase(Locale.ROOT))){
                                        System.out.println("Salida" + contador);
                                        System.out.println("Borrando juego" + nombres.get(contador));
                                        nombres.remove(contador);
                                        puntuaciones.remove(contador);
                                        valoracion_personal.remove(contador);
                                        fotos.remove(contador);
                                        id_juegos.remove(contador);
                                        estado.remove(contador);
                                        tamanio--;
                                        contador--;
                                    }
                                    contador++;
                                }
                            }

                            int contador2 = 0;
                            int tamanio2 = puntuaciones.size();

                            if(!puntuacion.equals("")){
                                int punt = Integer.parseInt(puntuacion);
                                if(punt > 100){
                                    punt = 100;
                                }
                                while(contador2 < tamanio2){
                                    System.out.println("Salida" + contador2);
                                    if(puntuaciones.get(contador2) < punt){
                                        System.out.println("Salida" + contador2);
                                        System.out.println("Borrando puntuacion" + nombres.get(contador2));
                                        nombres.remove(contador2);
                                        puntuaciones.remove(contador2);
                                        valoracion_personal.remove(contador2);
                                        fotos.remove(contador2);
                                        id_juegos.remove(contador2);
                                        estado.remove(contador2);
                                        contador2--;
                                        tamanio2--;
                                    }
                                    contador2++;
                                }
                            }

                            int contador3 = 0;
                            int tamanio3 = estado.size();

                            if(!estadoselec.equals("")){
                                while(contador3 < tamanio3){
                                    if(!estado.get(contador3).equals(estadoselec)){
                                        nombres.remove(contador3);
                                        puntuaciones.remove(contador3);
                                        valoracion_personal.remove(contador3);
                                        fotos.remove(contador3);
                                        id_juegos.remove(contador3);
                                        estado.remove(contador3);
                                        contador3--;
                                        tamanio3--;
                                    }
                                    contador3++;
                                }
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
