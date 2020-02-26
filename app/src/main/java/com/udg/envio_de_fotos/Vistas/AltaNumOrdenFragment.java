package com.udg.envio_de_fotos.Vistas;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.udg.envio_de_fotos.Modelos.OnAOCompleted;
import com.udg.envio_de_fotos.Modelos.OnCheckUpdateCompleted;
import com.udg.envio_de_fotos.R;
import com.udg.envio_de_fotos.Servicios.ABCNumOrden;
import com.udg.envio_de_fotos.Servicios.UpdateApp;

import org.json.JSONException;
import org.json.JSONObject;

public class AltaNumOrdenFragment extends Fragment implements OnAOCompleted, View.OnClickListener, OnCheckUpdateCompleted {
    private Button btnAlta;
    private EditText c1, c2, c3, c4, c5, c6;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_alta_num_orden, container, false);
        btnAlta=(Button)view.findViewById(R.id.btnAlta);
        btnAlta.setOnClickListener(this);
        c1=(EditText)view.findViewById(R.id.campo1);
        c2=(EditText)view.findViewById(R.id.campo2);
        c3=(EditText)view.findViewById(R.id.campo3);
        c4=(EditText)view.findViewById(R.id.campo4);
        c5=(EditText)view.findViewById(R.id.campo5);
        c6=(EditText)view.findViewById(R.id.campo6);




        validaActualizaciones();
     //   obtenerCampo1();
        return view;
    }

    /**
     *  Instancia la clase ABCNumOrden y ejecuta el metodo obtenCampo1
     *  */
    public void obtenerCampo1(){
        ABCNumOrden altaNumOrden= new ABCNumOrden(getContext());
        ABCNumOrden.ObtenerC1 getCampo1 = altaNumOrden.new ObtenerC1(this, getContext());
        getCampo1.execute();
    }


    @Override
    public void onClick(View v) {
        int id=v.getId();
        if(id==R.id.btnAlta){

            boolean[] validos=validaCampos();
            if(!validos[0])
                Toast.makeText(getContext(), "Por favor, llena todos los campos.", Toast.LENGTH_SHORT).show();
            else {
                if (!validos[2])
                    Toast.makeText(getContext(), "El celular debe ser mayor o igual a 10 digitos", Toast.LENGTH_SHORT).show();
                else {
                    if(!validos[1]){
                        Toast.makeText(getContext(), "El campo 5 no admite \" *\"", Toast.LENGTH_SHORT).show();
                    }else{
                        darDeAlta();
                    }

                }
            }

        }

    }

    private boolean[] validaCampos() {

        boolean[] valido= new boolean[3];
        valido[0]=true;
        valido[1]=true;
        valido[2] = true;
        String _c1=c1.getText().toString();
        String _c2=c2.getText().toString();
        String _c3=c3.getText().toString();
        String _c4=c4.getText().toString();
        String _c5=c5.getText().toString();
        String _c6=c6.getText().toString();
        if(_c1.isEmpty() || _c2.isEmpty() || _c3.trim().isEmpty() || _c4.isEmpty() ||
                _c5.trim().isEmpty() || _c6.trim().isEmpty())
            valido[0]= false;
        if(_c5.contains("*"))
            valido[1]=false;
        if( !(_c4.length() >= 10) ){
            valido[2]=false;
        }
        return valido;
    }


    @Override
    public void OnAltaCompleted(String respuesta) {
        try {

            JSONObject jsonObject = new JSONObject(respuesta);
            Toast.makeText(getContext(), jsonObject.getString("mensaje"), Toast.LENGTH_SHORT).show();
            if(jsonObject.getInt("codigo")==0){
                Intent i= new Intent(getContext(), NumOrdenes.class);
                i.putExtra("i", 0);
                limpiarCampos();
                startActivity(i);
            }
        }
        catch (JSONException e){
            if(respuesta.equals("Network is unreachable") || respuesta.equals("timeout")){
                mostrarNoHayConexion();
            }
            else if(!respuesta.isEmpty())
                Toast.makeText(getContext(), respuesta, Toast.LENGTH_SHORT).show();
            else {
                if (e.getMessage() == null)
                    Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void OnObtenerCampoCompleted(String campo_1) {
        if(campo_1.isEmpty()){
            c1.setText("campo 1");
        }
        else{
            try {
                JSONObject jsonObject = new JSONObject(campo_1);
                c1.setText(jsonObject.getString("mensaje"));
            }
            catch (JSONException e){

                if(campo_1.equals("Network is unreachable")){
                   mostrarNoHayConexion();
                }
                else {
                    if (e.getMessage() == null)
                        Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }


    }

    /**
     * Valida que las preferencias esten rellenadas. Si lo estan ejecuta el metodo alta si no,
     * abre las preferencias.
     */
    public void darDeAlta(){
        String[] pref=MainActivity.obtenerPreferencias(getContext());
        if(pref[0].isEmpty() || pref[1].isEmpty()) {
            Toast.makeText(getContext(), "Por favor configura las preferencias del " +
                    "servidor", Toast.LENGTH_SHORT).show();
          Intent p = new Intent(getContext(), PreferenciasActivity.class);
          startActivity(p);
        }
        else {
                ABCNumOrden altaNumOrden = new ABCNumOrden(c1.getText().toString(), c2.getText().toString(),
                        c3.getText().toString(), c4.getText().toString(), c5.getText().toString(), c6.getText().toString(), getContext());
                ABCNumOrden.Alta alta = altaNumOrden.new Alta(this, getContext());
                alta.execute();
        }
    }

    private void validaPrefs(){

        String[] pref=MainActivity.obtenerPreferencias(getContext());
        if(pref[0].isEmpty() || pref[1].isEmpty()) {
            Toast.makeText(getContext(), "Por favor configura las preferencias del " +
                    "servidor", Toast.LENGTH_SHORT).show();
            Intent p = new Intent(getContext(), PreferenciasActivity.class);
            startActivity(p);
        }
        else{
            obtenerCampo1();
        }

    }
    public void validaActualizaciones(){
        UpdateApp checa = new UpdateApp(getContext(), this);
        checa.execute();
    }
    private void mostrarNoHayConexion() {
        final AlertDialog.Builder mensaje=new AlertDialog.Builder(getContext());
        mensaje.setMessage("El dispositivo no cuenta con una conexión de red disponible");
        mensaje.setCancelable(false);
        mensaje.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        mensaje.show();
    }

    /**
     *
     */
    public void confirmarUpdate(){
        AlertDialog.Builder confirmar= new AlertDialog.Builder(getContext());
        confirmar.setTitle("Actualización disponible");
        confirmar.setCancelable(false);
        confirmar.setMessage("Esta usando una versión antigua de la aplicación, por favor descargue la nueva versión. ");
        confirmar.setPositiveButton("Ir a Google Play", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                openPlayStore();
            }
        });
        confirmar.setNegativeButton("Cerrar App", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getActivity().finish();
            }
        });
        confirmar.show();
    }

    private void openPlayStore(){
        final String appPackageName = getContext().getPackageName();
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }


    @Override
    public void onCheckCOmpleted(boolean itsUpdated) {
        if(itsUpdated)
            confirmarUpdate();
        else
            validaPrefs();
    }

    public void limpiarCampos(){
        c2.setText("");
        c3.setText("");
        c4.setText("");
        c5.setText("");
        c6.setText("");
    }
}
