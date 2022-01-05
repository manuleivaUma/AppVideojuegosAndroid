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


    public AdaptadorItems(Context context, ArrayList<String> nombres, ArrayList<String> fotos,
                          ArrayList<Integer> puntuaciones, ArrayList<String> fechas) {
        super(context, R.layout.videojuego_item, R.id.textViewNombre, nombres);
        this.context = context;
        this.fotos = fotos;
        this.nombres = nombres;
        this.puntuaciones = puntuaciones;
        this.fechas = fechas;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View videojuegoItem = convertView;
        ViewHolderItems holder = null;
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
                .error(R.mipmap.ic_launcher_round)
                .into(holder.foto);
        holder.nombre.setText(nombres.get(position));
        holder.puntuacion.setText(puntuaciones.get(position).toString());
        holder.fecha.setText(fechas.get(position));
        videojuegoItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Popup(v, nombres.get(position), fotos.get(position), fechas.get(position), puntuaciones.get(position));
            }
        });
        return videojuegoItem;
    }

    public void Popup(View v, String nombre, String imagen, String fecha, Integer puntuacion){
        TextView cerrar;
        Button añadir;

        ImageView foto;
        TextView txNombre;
        TextView txFecha;
        TextView txPuntuacion;

        dialogo = new Dialog(this.getContext());
        dialogo.setContentView(R.layout.popup_videojuego);
        foto = (ImageView) dialogo.findViewById(R.id.imageViewPopup);
        txNombre = (TextView) dialogo.findViewById(R.id.NombrePopup);
        txFecha = (TextView) dialogo.findViewById(R.id.FechaPopup);
        txPuntuacion = (TextView) dialogo.findViewById(R.id.puntuacionPopup);
        cerrar = (TextView) dialogo.findViewById(R.id.txtclose);
        añadir = (Button) dialogo.findViewById(R.id.botonPopup);

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
        dialogo.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogo.show();
    }
}
