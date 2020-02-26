package com.udg.envio_de_fotos.Vistas;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.View;
import android.widget.Toast;
import com.udg.envio_de_fotos.Adapters.EnviarAdapter;
import com.udg.envio_de_fotos.Modelos.OnTaskCompleted;
import com.udg.envio_de_fotos.R;
import com.udg.envio_de_fotos.Servicios.ABCNumOrden;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class PreEnviarActivity extends AppCompatActivity implements OnTaskCompleted {
    private String clave;                       //Id_registro
    private String nombre;                      //Nombre de la galeria
    private List<String> rutaImg;               //Ruta de las fotoso en memoria secundaria
    private final int RESULTADO_CAMARA = 0;        //Constante para tomar fotos, con Intent explicito
    private RecyclerView recyclerView;         //Muestra las miniaturas de las fotos
    private EnviarAdapter adaptador;            //Adapter para recyclerview
    List<Bitmap> bitmaps;                        //Lista que guarda las miniaturas



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enviar);
        initViews();
        takePhoto(null);

    }


    //Inicializa las views
    public void initViews() {
        rutaImg = new ArrayList<String>();
        bitmaps = new ArrayList<Bitmap>();
        //Inicializa el reyclerview donde se colocan las fotos que se van tomando
        recyclerView = (RecyclerView) findViewById(R.id.listFotos);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        adaptador = new EnviarAdapter(bitmaps);
        recyclerView.setAdapter(adaptador);
        /*obtener clave y nombre de la actividad anterior

         */
        Intent datos = getIntent();
        clave = datos.getStringExtra("clave");
        nombre = datos.getStringExtra("nombre");
    }


    public void takePhoto(View view) {
        Intent takePictureIntent = new Intent(this, CameraActivity.class);
        startActivityForResult(takePictureIntent, RESULTADO_CAMARA);

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULTADO_CAMARA) {
            if (resultCode == RESULT_OK) {
                //Uri photoUri = data.getDataString();
                ///sdcard/Pictures/SquareCamera/IMG_20190718_120043.jpp

                //File file = new File();
                //bmp = BitmapFactory.decodeByteArray(data.ge)data.getData();
                // fotoFull = data.getDataString();
                //     Log.d("photoUri", fotoFull);
                /*Imagen completa*/
                Bitmap bmp = null;
                byte[] byteArray = data.getByteArrayExtra("data");
                bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

                bitmaps.add(bmp);
                //    Bitmap thumbImage = ThumbnailUtils.extractThumbnail(bmp, 64, 64);
                //thumbnails.add(thumbImage);
                //rutaImg.add(fotoFull);

                //Actualiza la lista de fotos
                recyclerView.setAdapter(adaptador);

            } else {
                Toast.makeText(this, "Error en captura", Toast.LENGTH_LONG).show();
                cancelar(null);
            }
        }

    }


    /**
     * ejecuta el metodo sendEmail en SenEmail
     */
    public void enviarFoto(View view) {
        ABCNumOrden abcNumOrden = new ABCNumOrden(this, bitmaps);
        ABCNumOrden.SubirFotos subirFotos = abcNumOrden.new SubirFotos(this, this);
        subirFotos.execute(clave, nombre);
    }

    @Override
    public void onSendCompleted(String enviado) {
        try {
            JSONObject jsonObject = new JSONObject(enviado);
            if (jsonObject.getInt("codigo") == 0) {
                cancelar(null);
            }

            Toast.makeText(this, jsonObject.getString("mensaje"), Toast.LENGTH_SHORT).show();

        } catch (JSONException e) {
            if (enviado.equals("Network is unreachable")) {
                MainActivity.showAlertDialog(this);
            } else
                Toast.makeText(this, enviado, Toast.LENGTH_LONG).show();


        }
    }

    /**
     * Este metodo regresa al MainActivity y elimina las imagenes tomadas.
     *
     * @param view
     */
    public void cancelar(View view) {
        finish();


    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();

    }


}

