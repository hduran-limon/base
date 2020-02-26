package com.udg.envio_de_fotos.Vistas;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import com.udg.envio_de_fotos.Modelos.NumOrden;
import com.udg.envio_de_fotos.Adapters.NumOrdenAdapter;
import com.udg.envio_de_fotos.Modelos.OnCheckUpdateCompleted;
import com.udg.envio_de_fotos.R;
import com.udg.envio_de_fotos.Servicios.ABCNumOrden;
import com.udg.envio_de_fotos.Modelos.OnABCCompleted;

import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class NumOrdenes extends AppCompatActivity implements NumOrdenAdapter.ItemClickListener, OnABCCompleted, OnCheckUpdateCompleted {

    RecyclerView listNumOrden;            //Mostrara cada item de la lista numOrdenList
    NumOrdenAdapter adapter ;             //Adaptador
    String[] numOrdenes, fechas,  id;    //Arrays para separar numero de orden, fecha e id por ","
    List<NumOrden> numOrdenList = new ArrayList<NumOrden>();        //Lista para imprimir los datos en el recyclerView

    int i;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_num_ordenes);
        listNumOrden=(RecyclerView)findViewById(R.id.listNumOrdenes);
        ABCNumOrden altaNumOrden= new ABCNumOrden(this);
        listNumOrden.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        Intent extra= getIntent();                                          //Se obtienen los extras pasados por BuscarFragment o AltaNumOrdenFragment.
        i=extra.getIntExtra("i", 0);                    //Se obtiene el valor de i.
        String clave= extra.getStringExtra("clave");                //Se obtiene el valor de clave

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /*
        BuscaCualquierDia
         */
        if(i==1)
        {
                ABCNumOrden.BuscaCualquierDia buscar = altaNumOrden.new BuscaCualquierDia(this, this);
                buscar.execute(clave);
        }
        /*
        BuscaHoy
         */
        else if(i==0)
        {
                ABCNumOrden.BuscarHo getNumOrdenHoy = altaNumOrden.new BuscarHo(this, this);
                getNumOrdenHoy.execute();


        }
        else {
            Toast.makeText(this, "Error desconocido", Toast.LENGTH_SHORT).show();
        }
    }

    /*
        Se llama cuando se selecciona un elemento de la lista.
     */
    @Override
    public void onItemClick(View view, int position) {

        ABCNumOrden altaNumOrden=new ABCNumOrden(this);
        ABCNumOrden.BuscaDatos buscaDatos= altaNumOrden.new BuscaDatos(this, this);
        buscaDatos.execute(numOrdenList.get(position).getId());

    }

/*
    No se usa aquí.
 */
    @Override
    public void OnBajaCompleted(String[] res) {
        //No se usa.
    }

    /*
        se llama cuando termina la ejecucion del metodo buscaHoy.
     */
    @Override
    public void OnActualizarCompleted(String res) {
        try {

            JSONObject jsonObject = new JSONObject(res);
            numOrdenes= jsonObject.getString("ordenes").split(",");         //Devuelve array con los numeros de ordenes.
            id=jsonObject.getString("ids_registros").split(",");            //Devuelve array con los ids.
            for(int j=0; j<numOrdenes.length; j++)
                numOrdenList.add(new NumOrden(numOrdenes[j], id[j], null));        //Se crea una lista de  tipo "NumOrden"  con los datos de los arrays.
        }
        catch (Exception e){

            if(res.equals("Network is unreachable")){
                MainActivity.showAlertDialog(this);
            }
        }

        adapter= new NumOrdenAdapter(numOrdenList, this);
        /*
            Si la lista esta vacia.
         */
        if(numOrdenList.size()>0){
            if (numOrdenList.get(0).getNumOrden().isEmpty())
                Toast.makeText(this, "No hay altas hoy", Toast.LENGTH_LONG).show();
        /*
        Si no, se muestran los datos.
         */
            else {
                adapter.setClickListener(this);
                listNumOrden.setAdapter(adapter);
            }

        }
        else
            Toast.makeText(this, "Sin conexión", Toast.LENGTH_LONG).show();

    }

    @Override
    public void OnBuscaDatosCompleted(String[] res) {
        /*
           Para mostrar los datos  se reutiliza  la vista fragment_alta_num_orden con algunas modificaciones.
            Se infla la vista fragment_alta_num_orden para mostrarla en un AlertDialog
        */
        final AlertDialog.Builder dialog= new AlertDialog.Builder(this);
        dialog.setTitle("Datos");
        dialog.setCancelable(false);
        final String[] links;                        //Arrays para guardar los urls.
        ArrayAdapter<String> adapter;                //Este adaptador adaptará el array "links" y se mostrará en el ListView "listView"
        final EditText c1,  c3, cel, c5, c6;
        final ListView listView;                    //ListView para mostrar los urls
        Button alta;
        LayoutInflater inflater=LayoutInflater.from(this);
        View v =inflater.inflate(R.layout.layout_mostrar_datos, null);
        /*
            Se castean los elementos de fragment_alta_num_orden.
         */
        c1=(EditText)v.findViewById(R.id.num_orden);
        c3=(EditText)v.findViewById(R.id.nCampo3);
        cel=(EditText)v.findViewById(R.id.campo4);
        c5=(EditText)v.findViewById(R.id.nCampo5);
        c6=(EditText)v.findViewById(R.id.nCampo6);
        listView=(ListView)v.findViewById(R.id.links);
        dialog.setView(v);
        try
        {
  /*
              Se asingnan a c1, c2,c3,c4 y c5 los valores devueltos por jsonObject.getString().

            */
            JSONObject jsonObject = new JSONObject(res[0]);
            c1.setText(jsonObject.getString("campo1"));
            c3.setText(jsonObject.getString("campo3"));
            cel.setText(jsonObject.getString("campo4"));
            c5.setText(jsonObject.getString("campo5"));
            c6.setText(jsonObject.getString("campo6"));
            links=jsonObject.getString("campo7").split(",");            //Se obtiene el array de urls
            adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, links); //Instancia del adapter
            listView.setAdapter(adapter);                                                               //Muestra el listView
            /*
            Se cambian los atributos de los editText para que no sean editables
             */
            c1.setFocusable(false);
            c1.setEnabled(false);
            c1.setCursorVisible(false);
            c1.setKeyListener(null);
            c1.setBackgroundColor(Color.TRANSPARENT);
            c3.setFocusable(false);
            c3.setEnabled(false);
            c3.setCursorVisible(false);
            c3.setKeyListener(null);
            c3.setBackgroundColor(Color.TRANSPARENT);
            cel.setFocusable(false);
            cel.setEnabled(false);
            cel.setCursorVisible(false);
            cel.setKeyListener(null);
            cel.setBackgroundColor(Color.TRANSPARENT);
            c5.setFocusable(false);
            c5.setEnabled(false);
            c5.setCursorVisible(false);
            c5.setKeyListener(null);
            c5.setBackgroundColor(Color.TRANSPARENT);
            listView.setVisibility(View.VISIBLE);
            c6.setFocusable(false);
            c6.setEnabled(false);
            c6.setCursorVisible(false);
            c6.setKeyListener(null);
            /*
               Abre el url en el navegador cuando se presiona un url.
             */
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                    String link=links[position];
                    Uri uri =Uri.parse(link);
                    if(!link.isEmpty()){
                        Intent lanzaLink= new Intent(Intent.ACTION_VIEW, uri);
                        try {
                            startActivity(lanzaLink);
                        }catch (Exception e){
                            Toast.makeText(view.getContext(), "Navegador no disponible", Toast.LENGTH_LONG).show();
                        }
                    }
                }

            });
        }
        catch (Exception e){
            if(res.equals("Network is unreachable")){
                MainActivity.showAlertDialog(this);
            }

        }
        dialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }
    /*
       se llama cuando termina la ejecucion del metodo buscaCualquierDia.
    */
    @Override
    public void OnTerminarCompleted(String res)     {

        try {
            JSONObject jsonObject = new JSONObject(res);
            numOrdenes= jsonObject.getString("ordenes").split(",");
            fechas=jsonObject.getString("fechas").split(",");
            id=jsonObject.getString("ids_registros").split(",");
            for(int j=0; j<numOrdenes.length; j++)
                numOrdenList.add(new NumOrden(numOrdenes[j], id[j], fechas[j].substring(2)));

        }
        catch (Exception e){
            if(res.equals("Network is unreachable")){
                MainActivity.showAlertDialog(this);
            }
        }
        adapter= new NumOrdenAdapter(numOrdenList, this);       //Instancia de NumOrdenAdapter
        /*
          Si la lista esta vacia.
         */
        if(numOrdenList.size()>0) {

            if (numOrdenList.get(0).getNumOrden().isEmpty())
                Toast.makeText(this, "Sin coincidencias", Toast.LENGTH_LONG).show();
        /*
        Si no esta vacia se muestran los datos.
         */
            else {
                adapter.setClickListener(this);
                listNumOrden.setAdapter(adapter);
            }
        }
        else
            Toast.makeText(this, "Sin coincidencias", Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }

    @Override
    public void onCheckCOmpleted(boolean itsUpdated) {

    }
}
