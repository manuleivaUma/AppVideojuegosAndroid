package com.example.appvideojuegos;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewHolderItems {
    ImageView foto;
    TextView nombre;
    TextView fecha;
    TextView puntuacion;

    ViewHolderItems(View v){
        foto = v.findViewById(R.id.imageViewFoto);
        nombre = v.findViewById(R.id.textViewNombre);
        fecha = v.findViewById(R.id.textViewFecha);
        puntuacion = v.findViewById(R.id.textViewPuntuacion);
    }
}
