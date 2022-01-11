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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class DialogFiltro extends AppCompatDialogFragment {
    private EditText editTextTitulo;
    private EditText editTextPuntuacion;
    private DialogFiltroListener listener;
    private Spinner spinner;
    private boolean switchidioma;

    public DialogFiltro(Boolean switchActivo) {
        switchidioma = switchActivo;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.popup_filtro,null);

        String atras = "Atras";
        String filtrar = "Filtrar";

        String[] arraySpinner = new String[] {
                "Sin filtro estado", "Completado", "Jugando", "Deseado"
        };

        if(switchidioma){
            arraySpinner = new String[]{"No filter", "Finished", "Playing", "Wished"};
            atras = "Cancel";
            filtrar = "Filter";
        }

        builder.setView(view)

                .setNegativeButton(atras, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton(filtrar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String nombre = editTextTitulo.getText().toString();
                        String puntuacion = editTextPuntuacion.getText().toString();
                        String[] estados = new String[]{"Sin filtro estado", "Completado", "Jugando", "Deseado"};
                        String estado = estados[spinner.getSelectedItemPosition()];

                        int puntos = 0;

                        if(!puntuacion.equals("")){
                            puntos = Integer.parseInt(puntuacion);
                        }

                        if(puntos <= 100) {
                            listener.applyTexts(nombre,puntuacion,estado);
                        }else{
                            if(!switchidioma) {
                                Toast.makeText(getContext(), "El filtro de valoración personal debe estar entre 0 y 100", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(getContext(), "The user rating filter must be a value between 0 and 100", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                });
        editTextTitulo = view.findViewById(R.id.filtroNombre);
        editTextPuntuacion = view.findViewById(R.id.filtroPuntuacion);
        spinner = view.findViewById(R.id.spinner2);

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
