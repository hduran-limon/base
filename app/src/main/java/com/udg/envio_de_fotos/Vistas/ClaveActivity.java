package com.udg.envio_de_fotos.Vistas;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.udg.envio_de_fotos.R;

public class ClaveActivity extends AppCompatActivity {
    private Button btnAceptar;
    private EditText nombre;
    private  TextView clave;
    String id;
    private static final int SOLICITUD_PERMISO_ALMACENAMIENTO = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clave);
        clave=(TextView)findViewById(R.id.claveS);
        btnAceptar = (Button) findViewById(R.id.btnAceptar);
        Intent extras= getIntent();
        clave.setText(extras.getStringExtra("clave"));
        id = extras.getStringExtra("id");
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**
                 * Valida que se tenga el permiso para acceder al almacenamiento.
                 * Si no se tiene el permiso se le pide al usuario.
                 */

                    if(ContextCompat.checkSelfPermission(ClaveActivity.this, Manifest.permission.CAMERA)==PackageManager.PERMISSION_GRANTED){
                        lanzaPrep();
                    }
                    else{
                        MainActivity.pedirPermiso(Manifest.permission.CAMERA, "Sin el permiso"+
                                "no se podrá tomar fotografias.", SOLICITUD_PERMISO_ALMACENAMIENTO, ClaveActivity.this);
                    }
            }
        });

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==SOLICITUD_PERMISO_ALMACENAMIENTO){
            if(grantResults.length==1 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                lanzaPrep();
            }
        }
        else{
            Toast.makeText(this, "Sin permiso", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Lanza la actividad PreEnviarActivity y se pasa el num de orden y el nombre de la fotogaleria.
     *
     */
    public void lanzaPrep(){
        Intent i= new Intent(this, PreEnviarActivity.class);
        nombre=(EditText)findViewById(R.id.nombre);
        if( nombre.getText().toString().isEmpty() ) {
            i.putExtra("nombre", getString(R.string.tituloFotogaleria));
        }
        else
            i.putExtra("nombre", nombre.getText().toString());
        i.putExtra("clave", id);

        finish();
        startActivity(i);
    }
}
