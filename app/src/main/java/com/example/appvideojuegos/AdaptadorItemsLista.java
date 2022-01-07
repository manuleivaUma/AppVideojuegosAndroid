package com.example.appvideojuegos;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdaptadorItemsLista extends ArrayAdapter<String> {
    Context context;
    Dialog dialogo;
    ArrayList<String> fotos;
    ArrayList<String> nombres;
    ArrayList<String> estados;
    ArrayList<Integer> puntuaciones;
    ArrayList<Integer> val_personales;
    ArrayList<Integer> id_juegos;
    Integer id_usuario;
    Boolean switchActivo; // Idioma


    public AdaptadorItemsLista(Context context, ArrayList<String> nombres, ArrayList<String> fotos,
                               ArrayList<String> estados, ArrayList<Integer> puntuaciones,
                               ArrayList<Integer> val_personal, ArrayList<Integer> id_juegos,
                               Integer id_usuario, Boolean switchActivo){
        super(context, R.layout.videojuego_lista_item, R.id.textViewNombre, nombres);
        this.context = context;
        this.fotos = fotos;
        this.nombres = nombres;
        this.estados = estados;
        this.puntuaciones = puntuaciones;
        this.val_personales = val_personal;
        this.id_juegos = id_juegos;
        this.id_usuario = id_usuario;
        this.switchActivo = switchActivo;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View videojuegoItem = convertView;
        ViewHolderItemsLista holder = null;
        if (videojuegoItem == null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            videojuegoItem = layoutInflater.inflate(R.layout.videojuego_lista_item, parent, false);
            holder = new ViewHolderItemsLista(videojuegoItem);
            videojuegoItem.setTag(holder);
        } else {
            holder = (ViewHolderItemsLista) videojuegoItem.getTag();
        }
        // Definimos los valores
        Picasso.get()
                .load(fotos.get(position))
                .fit()
                .centerCrop()
                .error(R.mipmap.ic_launcher_round)
                .into(holder.foto);
        holder.nombre.setText(nombres.get(position));
        holder.puntuacion.setText(puntuaciones.get(position).toString());
        holder.val_personal.setText(val_personales.get(position).toString());
        // Soporte multi-idioma
        if (switchActivo){
            switch (estados.get(position)){
                case "Jugando" : holder.estado.setText("Playing");
                                    break;
                case "Completado" : holder.estado.setText("Finished");
                                    break;
                case "Deseado" : holder.estado.setText("Wished");
                                    break;
            }
        } else {
            holder.estado.setText(estados.get(position));
        }
        videojuegoItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Popup(v, nombres.get(position), fotos.get(position), estados.get(position),
                        puntuaciones.get(position), val_personales.get(position), id_juegos.get(position));
            }
        });
        return videojuegoItem;
    }

    public void Popup(View v, String nombre, String imagen, String estado, Integer puntuacion,
                      Integer val_personal, Integer id){
        TextView cerrar;
        Button eliminar;
        Button actualizar;
        ImageView foto;
        TextView txNombre;
        Spinner spEstado;
        TextView txPuntuacion;
        EditText editVal_personal;

        dialogo = new Dialog(this.getContext());
        dialogo.setContentView(R.layout.popup_lista);
        foto = dialogo.findViewById(R.id.imageViewPopup);
        txNombre = dialogo.findViewById(R.id.NombrePopup);
        spEstado = dialogo.findViewById(R.id.spinnerPopup);
        txPuntuacion = dialogo.findViewById(R.id.puntuacionPopup);
        editVal_personal = dialogo.findViewById(R.id.puntuacionPersonalPopup);
        cerrar = dialogo.findViewById(R.id.txtclose);
        eliminar = dialogo.findViewById(R.id.botonPopup);
        actualizar = dialogo.findViewById(R.id.botonActualizarPopup);

        txNombre.setText(nombre);
        txPuntuacion.setText(puntuacion.toString());
        editVal_personal.setText(val_personal.toString());
        Picasso.get()
                .load(imagen)
                .error(R.mipmap.ic_launcher_round)
                .into(foto);
        // Spinner
        String[] estados = new String[]{"Completado", "Jugando", "Deseado"};
        if (switchActivo) {
            estados = new String[]{"Finished", "Playing", "Wished"};
        }

        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(context,
                R.layout.spinner_item, estados);
        adaptador.setDropDownViewResource(R.layout.spinner_item);
        spEstado.setAdapter(adaptador);
        int posAdapter = 0;
        switch (estado) {
            case "Jugando":
                posAdapter = 1;
                break;
            case "Completado":
                posAdapter = 0;
                break;
            case "Deseado":
                posAdapter = 2;
                break;
        }
        spEstado.setSelection(posAdapter);


        // Cerrar popup
        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogo.dismiss();
            }
        });

        // Boton actualizar
        actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer numero = Integer.parseInt(editVal_personal.getText().toString());
                if (numero >= 0 && numero <= 100){
                    // Actualizar juego
                    DbJuego db = new DbJuego(context);
                    String[] estados = new String[]{"Completado", "Jugando", "Deseado"};
                    String estado = estados[spEstado.getSelectedItemPosition()];
                    long idQuery = db.editarJuego(id_usuario, id, estado, numero);
                    Toast.makeText(context, "¡Juego actualizado!", Toast.LENGTH_SHORT).show();
                    if (context instanceof ListaUsuario){
                        ((ListaUsuario)context).cargarInfo();
                    }
                } else {
                    // Número no válido
                    Toast.makeText(context, "El número debe estar entre los valores 0 y 100",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Boton eliminar
        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Borrar juego
                DbJuego db = new DbJuego(context);
                long idQuery = db.borrarJuego(id_usuario, id);
                if (idQuery < 0) {
                    Toast.makeText(context, "Error al borrar el juego", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Juego eliminado", Toast.LENGTH_SHORT).show();
                    if (context instanceof ListaUsuario){
                        ((ListaUsuario)context).cargarInfo();
                    }
                    dialogo.dismiss();
                }
            }
        });

        dialogo.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogo.show();
    }
}
