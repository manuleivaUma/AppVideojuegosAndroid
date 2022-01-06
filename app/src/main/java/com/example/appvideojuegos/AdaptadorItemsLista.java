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

    public AdaptadorItemsLista(Context context, ArrayList<String> nombres, ArrayList<String> fotos,
                               ArrayList<String> estados, ArrayList<Integer> puntuaciones,
                               ArrayList<Integer> val_personal, ArrayList<Integer> id_juegos,
                               Integer id_usuario){
        super(context, R.layout.videojuego_lista_item, R.id.textViewNombre, nombres);
        this.context = context;
        this.fotos = fotos;
        this.nombres = nombres;
        this.estados = estados;
        this.puntuaciones = puntuaciones;
        this.val_personales = val_personal;
        this.id_juegos = id_juegos;
        this.id_usuario = id_usuario;
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
        holder.estado.setText(estados.get(position));
        videojuegoItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Seleccionado " + id_juegos.get(position), Toast.LENGTH_SHORT).show();
            }
        });
        return videojuegoItem;
    }
}
