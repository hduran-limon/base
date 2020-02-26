package com.udg.envio_de_fotos.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.udg.envio_de_fotos.Modelos.NumOrden;
import com.udg.envio_de_fotos.Modelos.OnABCCompleted;
import com.udg.envio_de_fotos.Modelos.OnCheckUpdateCompleted;
import com.udg.envio_de_fotos.R;
import com.udg.envio_de_fotos.Servicios.ABCNumOrden;
import com.udg.envio_de_fotos.Vistas.ClaveActivity;
import com.udg.envio_de_fotos.Vistas.MainActivity;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;


public class NumOrdenAdapter extends RecyclerView.Adapter<NumOrdenAdapter.ViewHolder> implements OnABCCompleted, OnCheckUpdateCompleted {
    private ABCNumOrden altaNumOrden;
    private final List<NumOrden> numOrdenes;
    private ItemClickListener itemClickListener;
    private  Context context;
    private boolean hayInternet;
    private AlertDialog alertDialog;
    public NumOrdenAdapter(List<NumOrden> numOrdenes, Context context) {
        this.numOrdenes=numOrdenes;
        this.context=context;
        altaNumOrden=new ABCNumOrden(context);
        hayInternet = false;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_num_orden, parent, false); //Se infla el layout item_num_orden, para acceder a sus elementos en onBindviewHolder
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        /*
            Se asignan los valores correspondientes
         */
        holder.mContentView.setText(numOrdenes.get(position).getNumOrden());
        holder.fecha.setText(numOrdenes.get(position).getFecha());
        holder.id_reg.setText(numOrdenes.get(position).getId());
        /*
            Valida la preferencia "Botón terminar"
            "0" Si esta desactivado. "1" si esta activado.
            Por defecto esta visible
            Si es igual a "0" se asigna la propidad Visibility a GONE.
         */
        String[] prefs=MainActivity.obtenerPreferencias(context);
        if(prefs[2].equals("0")){
            holder.terminar.setVisibility(View.GONE);
        }
        /*
            Evento del boton enviar
         */
        holder.enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                Se inicia ClaveActivity y se pasa el valor del número de orden.
                 */
                Intent enviar = new Intent(context, ClaveActivity.class);
                enviar.putExtra("clave", numOrdenes.get(position).getNumOrden());
                enviar.putExtra("id", numOrdenes.get(position).getId());
                context.startActivity(enviar);

            }
        });
        /*
        Evento del boton editar
         */
        holder.editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDatos(numOrdenes.get(position).getId());


            }
        });
        /*
        Evento del boton eliminar
         */
        holder.eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmarEliminar(numOrdenes.get(position).getId(), position);

            }
        });
        /*
            Evento del boton terminar
        */
        holder.terminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder confirmar= new AlertDialog.Builder(context);
                confirmar.setCancelable(false);
                confirmar.setView(null);
                confirmar.setTitle("Confirmar terminación");
                confirmar.setMessage("¿Estas seguro que quieres terminar el numero de orden: \""+numOrdenes.get(position).getNumOrden()+"\"?");
                confirmar.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        terminado(numOrdenes.get(position).getId());
                    }
                });
                confirmar.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                confirmar.show();

            }
        });



    }
    @Override
    public int getItemCount() {
        return numOrdenes.size();
    }

    /**
     * Abre un cuadro de dialogo para confirmar la eliminación, si se confirma se llama al metodo baja().
     * @param id
     * @param p
     */
    public void confirmarEliminar(final String id, final int p){

        final AlertDialog.Builder confirmar= new AlertDialog.Builder(context);
        confirmar.setView(null);
        confirmar.setCancelable(false);
        confirmar.setTitle("Confirmar eliminación");
        confirmar.setMessage("¿Estas seguro que quieres eliminar el numero: \""+numOrdenes.get(p).getNumOrden()+"\"?");
        confirmar.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
              baja(id, p);
            }
        });
        confirmar.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        confirmar.show();



    }

    /**
     * executa el metodo baja en ABCNumOrden
     * @param id
     * @param p
     */
    public void baja(String id, int p){



            ABCNumOrden.Baja baja= altaNumOrden.new Baja(this, context);
            String pos= String.valueOf(p);
            baja.execute(id, pos);


    }
    /**
    Ejecuta el metodo buscadatos en ABCNumOrden
     */
    public void mostrarDatos(final String id){
            ABCNumOrden.BuscaDatos buscaDatos= altaNumOrden.new BuscaDatos(this, context);
            buscaDatos.execute(id);
    }

    /**
     * Ejecuta el metodo terminado en ABCNumOrden
     * @param id
     */
    public void terminado(final String id){
            ABCNumOrden.Terminado terminado= altaNumOrden.new Terminado(this, context);
            terminado.execute(id);

    }

    @Override
    public void OnBajaCompleted(String[] res) {
        int p= Integer.parseInt(res[1]);
        try {
            JSONObject jsonObject= new JSONObject(res[0]);
            if(jsonObject.getInt("codigo")==-1)
                Toast.makeText(context, jsonObject.getString("mensaje"), Toast.LENGTH_SHORT).show();
            else {
                Toast.makeText(context, jsonObject.getString("mensaje"), Toast.LENGTH_SHORT).show();
                numOrdenes.remove(p);
                NumOrdenAdapter.this.notifyItemRemoved(p);
                NumOrdenAdapter.this.notifyItemRangeChanged(p, numOrdenes.size());

            }
        } catch (JSONException e) {
            if(res[0].equals("Network is unreachable")){
                MainActivity.showAlertDialog(context);
            }
        }

    }

    @Override
    public void OnActualizarCompleted(String res) {
        try {

            JSONObject jsonObject = new JSONObject(res);
            if(jsonObject.getString("codigo").equals("0")){
                alertDialog.dismiss();
            }
            Toast.makeText(context, jsonObject.getString("mensaje"), Toast.LENGTH_LONG).show();
        }
        catch (Exception e){
            if(res.equals("Network is unreachable")){
                MainActivity.showAlertDialog(context);
            }
        }
    }

    @Override
    public void OnBuscaDatosCompleted(String[] res) {
         /*
           Para editar los campos se reutiliza  la vista fragment_alta_num_orden con algunas modificaciones.
            Se infla la vista fragment_alta_num_orden para mostrarla en un AlertDialog
        */
        final AlertDialog.Builder dialog= new AlertDialog.Builder(context);

        dialog.setCancelable(false);
        dialog.setTitle("Datos");
        final   EditText c1, c3, cel, c5, c6;
        final String id=res[1];
        final ImageButton cancelar, ok;
        String[] links;                         //Array para guardar los urls.
        ArrayAdapter<String> adapter;           //Este adaptador adaptará el array "links" y se mostrará en el ListView "listView"
        Button alta;
        LayoutInflater inflater=LayoutInflater.from(context);
        View view =inflater.inflate(R.layout.layout_mostrar_datos, null);
        c1=(EditText)view.findViewById(R.id.num_orden);//
        c3=(EditText)view.findViewById(R.id.nCampo3);//
        cel=(EditText)view.findViewById(R.id.campo4);
        c5=(EditText)view.findViewById(R.id.nCampo5);
        c6=(EditText)view.findViewById(R.id.nCampo6);//

        cancelar=(ImageButton)view.findViewById(R.id.btnCancelar);
        ok=(ImageButton)view.findViewById(R.id.btnActualizar);
        dialog.setView(view);
        alertDialog=dialog.create();

        try
        {
            /*
              Se asignan a c1, c2,c3,c4 y c5 los valores devueltos por jsonObject.getString().

            */
            JSONObject jsonObject = new JSONObject(res[0]);
            c1.setText(jsonObject.getString("campo1"));
            c3.setText(jsonObject.getString("campo3"));
            cel.setText(jsonObject.getString("campo4"));
            c5.setText(jsonObject.getString("campo5"));
            c6.setText(jsonObject.getString("campo6"));
            links=jsonObject.getString("campo7").split(",");
            /*
                Se cambian los atributos editables de c1, que muestra el numOrden.
             */
            c1.setFocusable(false);
            c1.setEnabled(false);
            c1.setCursorVisible(false);
            c1.setKeyListener(null);
            c1.setBackgroundColor(Color.TRANSPARENT);
             /*
                Se ponen visibles los botones "ok" y "cancelar" y la lista de urls.
                Por defecto estan en GONE.
             */
            ok.setVisibility(View.VISIBLE);
            cancelar.setVisibility(View.VISIBLE);

        }
        catch (Exception e){
            if(res[0].equals("Network is unreachable")){
                MainActivity.showAlertDialog(context);
            }
        }
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String _c3=c3.getText().toString();
                String _cel=cel.getText().toString();
                String _c5=c5.getText().toString();
                String _c6=c6.getText().toString();
                boolean[] validos=validaCampos(_cel,_c3, _c5, _c6);
                if(validos[0]) {
                    if (validos[1]) {
                        if(validos[2]){
                            actualiza(id, _c3, _cel, _c5, _c6);
                        }else
                        {
                            Toast.makeText(context, "El telefono debe ser mayor o igual a 10 digitos", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                        Toast.makeText(context, "El campo 5 no admite \"*\"", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(context, "Por favor, llena todos los campos", Toast.LENGTH_SHORT).show();



            }
        });

     cancelar.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
          alertDialog.dismiss();                //Cierra el alertDialog
         }
     });
     alertDialog.show(); //Muestra el alertDialog con los datos devueltos por buscaDatos

    }

    private boolean[] validaCampos(String c2, String c3, String c4, String c5) {
    boolean[] validos=new boolean[3];
    validos[0]=true;
    validos[1]=true;
    validos[2]=true;
        if(c2.trim().isEmpty() || c3.isEmpty() || c4.trim().isEmpty() || c5.trim().isEmpty() )
            validos[0]= false;
        if(c4.contains("*"))
            validos[1]=false;
        if( !(c2.length() >= 10) ){
            validos[2]=false;
        }
        return  validos;
    }

    /**
     * Ejecuta el metodo actualiza en ABCNumOrden
     * @param id
     * @param c2
     * @param c3
     * @param c4
     * @param c5
     */
    public void actualiza(String id,String c2,String c3,String c4,String c5){

        ABCNumOrden.Actualizar actualizaDatos = altaNumOrden.new Actualizar(this, context);
        actualizaDatos.execute(id, c2, c3, c4, c5);


    }
    @Override
    public void OnTerminarCompleted(String res) {
        try {
            JSONObject jsonObject= new JSONObject(res);
            Toast.makeText(context, jsonObject.getString("mensaje"), Toast.LENGTH_LONG).show();
        } catch (JSONException e) {
            if(res.equals("Network is unreachable")){
                MainActivity.showAlertDialog(context);
            }
            else
                Toast.makeText(context, res, Toast.LENGTH_LONG);
        }

    }

    @Override
    public void onCheckCOmpleted(boolean itsUpdated) {

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final TextView mContentView;
        public final TextView fecha;
        public final TextView id_reg;
        public  final ImageButton eliminar;
        public  final ImageButton editar;
        public final ImageButton enviar;
        public final ImageButton terminar;
        public ViewHolder(View view) {
            super(view);
            mContentView = (TextView) view.findViewById(R.id.content);
            fecha=(TextView)view.findViewById(R.id.fechaNumOrden);
            id_reg=(TextView)view.findViewById(R.id.id_reg);
            eliminar=(ImageButton)view.findViewById(R.id.btnEliminarOrden);
            editar=(ImageButton)view.findViewById(R.id.btnEditarOrden);
            enviar=(ImageButton)view.findViewById(R.id.btnNuevo);
            terminar=(ImageButton)view.findViewById(R.id.btnTerminado);
            view.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            int id=v.getId();
            if(itemClickListener!=null){
                itemClickListener.onItemClick(v, getAdapterPosition());
            }
        }
    }
    public void setClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
    public interface ItemClickListener{
        void onItemClick(View view, int position);
    }


}
