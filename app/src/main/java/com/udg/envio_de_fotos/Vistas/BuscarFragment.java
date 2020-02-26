package com.udg.envio_de_fotos.Vistas;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.udg.envio_de_fotos.R;

public class BuscarFragment extends Fragment {
    EditText claveB;
    Button buscar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        final View view=inflater.inflate(R.layout.fragment_buscar, container, false);
        claveB = (EditText) view.findViewById(R.id.claveToSearch1);
        buscar=(Button)view.findViewById(R.id.buscar1);
        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String[] pref=MainActivity.obtenerPreferencias(getContext());
                 /*
                    Se valida que el usuario tenga configuradas las preferencias.
                 */
                if(pref[0].isEmpty() || pref[1].isEmpty()) {
                    Toast.makeText(getContext(), "Por favor configura las preferencias del " +
                            "servidor", Toast.LENGTH_SHORT).show();
                    Intent p = new Intent(getContext(), PreferenciasActivity.class);
                    startActivity(p);
                }
                /*
                    1.- Si las preferencias estan configuradas se toma el texto de "clave".
                    2.- Si "clave" esta vacio se asinga a i el valor 0, si no, se asinga 1.
                    2.- Se abre "NumOrdenes.class" y se pasa el valor de i.

                 */
                else {
                   // validaRed();
                    String clave = claveB.getText().toString();
                    int i = 0;
                    Intent lista = new Intent(getContext(), NumOrdenes.class);
                    if (!clave.isEmpty()) {
                        i = 1;
                        lista.putExtra("clave", clave);
                    }
                    lista.putExtra("i", i);
                    startActivity(lista);
                }


            }
        });
        return view;
    }

}
