package com.example.appvideojuegos;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class DialogFiltro extends AppCompatDialogFragment {
    private EditText editTextTitulo;
    private EditText editTextPuntuacion;
    private DialogFiltroListener listener;
    private Spinner spinner;

    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.popup_filtro,null);

        builder.setView(view)
                .setTitle("Filtro")
                .setNegativeButton("Atras", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Filtrar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String nombre = editTextTitulo.getText().toString();
                        String puntuacion = editTextPuntuacion.getText().toString();
                        String estado = spinner.getSelectedItem().toString();
                        listener.applyTexts(nombre,puntuacion,estado);
                    }
                });
        editTextTitulo = view.findViewById(R.id.filtroNombre);
        editTextPuntuacion = view.findViewById(R.id.filtroPuntuacion);
        spinner = view.findViewById(R.id.spinner2);

        String[] arraySpinner = new String[] {
                "Sin filtro estado", "Completado", "Jugando", "Deseado"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_spinner_dropdown_item,arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (DialogFiltroListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString() + "must implement DialogFiltroListener");
        }
    }

    public interface DialogFiltroListener{
        void applyTexts(String n, String p, String s);
    }
}
