package com.example.appvideojuegos;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
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
import java.util.Locale;
import java.util.Map;

public class ListaUsuario extends AppCompatActivity implements DialogFiltro.DialogFiltroListener {

    private Integer id;
    private ListView listaJuegos;
    private Button boton, buttonFiltrar;
    private AdaptadorItemsLista adaptador;
    String nombre, puntuacion, estadoselec;
    private Switch idioma;

    ArrayList<String> nombres;
    ArrayList<String> fotos;
    ArrayList<String> estado;
    ArrayList<Integer> puntuaciones;
    ArrayList<Integer> valoracion_personal;
    ArrayList<Integer> id_juegos;

    // Objetos compartidos
    private Boolean switchActivo;
    private Map<String,String> mapaid;

    @Override
    public void applyTexts(String n, String p,String s) {
        //Guardamos los filtros recibidos del dialogo en variables globales
        nombre = n;
        puntuacion = p;
        estadoselec = s;

        if(estadoselec.equals("Sin filtro estado")){
            estadoselec = "";
        }

        cargarInfo();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_lista_usuario);

        nombre = "";
        puntuacion = "";
        estadoselec = "";

        // Objetos compartidos
        switchActivo = (Boolean) SingletonMap.getInstance().get(LoginUsuario.SHAREOBJ_ingles);
        mapaid = (Map<String, String>) SingletonMap.getInstance().get(LoginUsuario.SHAREOBJ_mapa);

        listaJuegos = this.findViewById(R.id.listaJuegos);
        id = Integer.parseInt(mapaid.get("id"));
        boton = this.findViewById(R.id.buttonfiltrar);
        buttonFiltrar = this.findViewById(R.id.buttonfiltrar);

        // Cargar info
        cargarInfo();

        buttonFiltrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                openDialog(switchActivo);
            }
        });
    }

    private void openDialog(Boolean switchActivo) {
        //Abrimos la ventana de filtrado
        DialogFiltro dialogFiltro = new DialogFiltro(switchActivo);
        dialogFiltro.show(getSupportFragmentManager(),"dialogFiltro");
    }

    public void cargarInfo(){
        // Obtenemos la informaci??n del usuario
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
            if(switchActivo) {
                Toast.makeText(this, "Error al cargar la lista de juegos", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "Error loading the games list", Toast.LENGTH_SHORT).show();
            }
        }

        TextView text=(TextView)findViewById(R.id.nombreusuario);
        text.setText(m.get("nombre") + " " + m.get("apellido"));
    }

    private void tratarJuegos(Map<String, ArrayList<String>> m){
        nombres = new ArrayList<>();
        fotos = new ArrayList<>();
        estado = m != null ? m.get("listaEstado") : new ArrayList<>();
        puntuaciones = new ArrayList<>();
        valoracion_personal = new ArrayList<>();
        id_juegos = new ArrayList<>();

        if (m != null){
            Log.i("Juegos", m.toString());
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
            // Buscamos la informaci??n de los juegos
            Map<Integer, ArrayList<String>> res = new HashMap<>();
            // Id - {Nombre - Foto - Valoracion}
            for (Integer id : id_juegos){
                // B??squeda as??ncrona (no mantiene el orden)
                buscarInformaci??n(id, new CallBack(){
                    @Override
                    public void onSuccess(ArrayList<String> detalles){
                        ArrayList<String> listaInfo = new ArrayList<>();
                        Log.d("Res", detalles.toString());
                        // Nombre - Foto - Valoraci??n
                        listaInfo.add(detalles.get(0));
                        listaInfo.add(detalles.get(1));
                        listaInfo.add(detalles.get(2));
                        res.put(id, listaInfo);
                        // Se ha cargado el ??ltimo elemento, actualizamos la listView
                        if (res.size() == nJuegos){
                            for (Integer id : id_juegos){
                                // A??adimos a la lista en orden de id
                                nombres.add(res.get(id).get(0));
                                fotos.add(res.get(id).get(1));
                                puntuaciones.add(Integer.parseInt(res.get(id).get(2)));
                            }

                            int contador = 0;
                            int tamanio = nombres.size();

                            boolean ok1 = false;
                            boolean ok2 = false;
                            boolean ok3 = false;

                            if(!nombre.equals("")){
                                ok1 = true;
                            }

                            if(!puntuacion.equals("")){
                                ok2 = true;
                            }

                            if(!estadoselec.equals("")){
                                ok3 = true;
                            }

                               while(contador < tamanio){
                                    if(ok1 && !nombres.get(contador).toLowerCase(Locale.ROOT).contains(nombre.toLowerCase(Locale.ROOT))){
                                        borrarlistas(contador);
                                        tamanio--;
                                        contador--;
                                    }else if(ok2 && valoracion_personal.get(contador) < Integer.parseInt(puntuacion)){
                                        borrarlistas(contador);
                                        tamanio--;
                                        contador--;
                                    }else if (ok3 && !estado.get(contador).equals(estadoselec)){
                                        borrarlistas(contador);
                                        tamanio--;
                                        contador--;
                                    }
                                    contador++;
                                }

                            adaptador.notifyDataSetChanged();

                        }
                    }

                });
            }


        }
        // Actualizar la listView
        adaptador = new AdaptadorItemsLista(this, nombres, fotos, estado,
                puntuaciones, valoracion_personal, id_juegos, id, switchActivo);
        listaJuegos.setAdapter(adaptador);
    }

    private void borrarlistas(int contador) {
        nombres.remove(contador);
        puntuaciones.remove(contador);
        valoracion_personal.remove(contador);
        fotos.remove(contador);
        id_juegos.remove(contador);
        estado.remove(contador);
    }


    private ArrayList<String> buscarInformaci??n(Integer id, final CallBack onCallBack){
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

    //******************* INTERFAZ Buscar Informaci??n *******************//
    public interface CallBack {
        void onSuccess(ArrayList<String> detalles);
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
                Intent intent = new Intent(ListaUsuario.this, ListaUsuario.class);
                SingletonMap.getInstance().put(LoginUsuario.SHAREOBJ_ingles, ingles);
                SingletonMap.getInstance().put(LoginUsuario.SHAREOBJ_mapa, mapaid);
                startActivity(intent);
                finish();
            }
        });
        return true;
    }

    // Opciones del menu
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
        SingletonMap.getInstance().put(LoginUsuario.SHAREOBJ_ingles, switchActivo);
        SingletonMap.getInstance().put(LoginUsuario.SHAREOBJ_mapa, mapaid);
        startActivity(intent);
        finish();
    }

    private void mostrarLista(){
        Intent intent = new Intent(this, ListaUsuario.class);
        SingletonMap.getInstance().put(LoginUsuario.SHAREOBJ_ingles, switchActivo);
        SingletonMap.getInstance().put(LoginUsuario.SHAREOBJ_mapa, mapaid);
        startActivity(intent);
        finish();
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
