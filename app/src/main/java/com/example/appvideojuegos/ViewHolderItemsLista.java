package com.example.appvideojuegos;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewHolderItemsLista {
    ImageView foto;
    TextView nombre;
    TextView estado;
    TextView puntuacion;
    TextView val_personal;

    ViewHolderItemsLista(View v){
        foto = v.findViewById(R.id.imageViewFoto);
        nombre = v.findViewById(R.id.textViewNombre);
        estado = v.findViewById(R.id.textViewEstado);
        puntuacion = v.findViewById(R.id.textViewPuntuacion);
        val_personal = v.findViewById(R.id.textViewPuntuacionPersonal);
    }
}
