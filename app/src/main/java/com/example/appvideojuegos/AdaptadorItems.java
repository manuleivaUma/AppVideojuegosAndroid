package com.example.appvideojuegos;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdaptadorItems extends ArrayAdapter<String> {
    Context context;
    Dialog dialogo;
    ArrayList<String> fotos;
    ArrayList<String> nombres;
    ArrayList<String> fechas;
    ArrayList<Integer> puntuaciones;
    ArrayList<Integer> id_juegos;
    Integer id_usuario;
    boolean switchActivo;

    public AdaptadorItems(Context context, ArrayList<String> nombres, ArrayList<String> fotos,
                          ArrayList<Integer> puntuaciones, ArrayList<String> fechas,
                          ArrayList<Integer> id_juegos, Integer id_usuario, boolean switchActivo) {
        super(context, R.layout.videojuego_item, R.id.textViewNombre, nombres);
        this.context = context;
        this.fotos = fotos;
        this.nombres = nombres;
        this.puntuaciones = puntuaciones;
        this.fechas = fechas;
        this.id_juegos = id_juegos;
        this.id_usuario = id_usuario;
        this.switchActivo = switchActivo;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View videojuegoItem = convertView;
        ViewHolderItems holder;
        if (videojuegoItem == null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            videojuegoItem = layoutInflater.inflate(R.layout.videojuego_item, parent, false);
            holder = new ViewHolderItems(videojuegoItem);
            videojuegoItem.setTag(holder);
        } else {
            holder = (ViewHolderItems) videojuegoItem.getTag();
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
        holder.fecha.setText(fechas.get(position));
        videojuegoItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Popup(v, nombres.get(position), fotos.get(position), fechas.get(position),
                        puntuaciones.get(position), id_juegos.get(position));
            }
        });
        return videojuegoItem;
    }

    public void Popup(View v, String nombre, String imagen, String fecha, Integer puntuacion,
                      Integer id){
        TextView cerrar;
        Button añadir;
        Boolean añadido = false;

        ImageView foto;
        TextView txNombre;
        TextView txFecha;
        TextView txPuntuacion;

        dialogo = new Dialog(this.getContext());
        dialogo.setContentView(R.layout.popup_videojuego);
        foto = dialogo.findViewById(R.id.imageViewPopup);
        txNombre = dialogo.findViewById(R.id.NombrePopup);
        txFecha = dialogo.findViewById(R.id.FechaPopup);
        txPuntuacion = dialogo.findViewById(R.id.puntuacionPopup);
        cerrar = dialogo.findViewById(R.id.txtclose);
        añadir = dialogo.findViewById(R.id.botonPopup);

        // Comprobamos si el juego esta en la lista para desactivar el boton
        DbJuego dbJuego = new DbJuego(context);
        long idQuery = dbJuego.comprobarJuego(id_usuario, id);
        if (idQuery > 0){
            añadido = true;
        }
        txNombre.setText(nombre);
        txFecha.setText(fecha);
        txPuntuacion.setText(puntuacion.toString());
        Picasso.get()
                .load(imagen)
                .error(R.mipmap.ic_launcher_round)
                .into(foto);

        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogo.dismiss();
            }
        });

        // Botón Añadir
        if (!añadido){
            // Añadir objeto a lista
            añadir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    añadirJuego(id_usuario, id, "Jugando", 0, dialogo);
                }
            });
        } else {
            // Desactivamos el botón
            añadir.setEnabled(false);
        }
        dialogo.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogo.show();
    }

    private void añadirJuego(Integer id_usuario, Integer id_juego, String estado,
                             Integer valoracion, Dialog dialogo){
        DbJuego dbJuego = new DbJuego(context);
        long idQuery = dbJuego.crearJuego(id_usuario, id_juego, estado, valoracion);
        if (idQuery > 0){
            if(!switchActivo) {
                Toast.makeText(context, "¡Juego añadido!", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(context, "Game added!", Toast.LENGTH_SHORT).show();
            }
            dialogo.dismiss();
        } else {
            if(!switchActivo) {
                Toast.makeText(context, "Error al registrar el juego", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(context, "Error registering the game", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
