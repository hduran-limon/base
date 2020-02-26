package com.udg.envio_de_fotos.Vistas;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import com.udg.envio_de_fotos.Modelos.OnAOCompleted;
import com.udg.envio_de_fotos.R;
import com.udg.envio_de_fotos.Servicios.ABCNumOrden;

import org.json.JSONException;
import org.json.JSONObject;

public class PreferenciasActivity extends AppCompatActivity implements OnAOCompleted {
    TextInputEditText editText;             //Para introducir el usuario y contrraseña
    String[] pref;                          //Arreglo para obtener las preferencias
    CheckBox checkBox;                       // CheckBox para habilitar o deshabilitar el boton de terminar
    SharedPreferences preferences;          //Clase para acceder a las pref
    SharedPreferences.Editor editor;        //Para editar las prefs
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preferences);

        getPref();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        checkBox = (CheckBox)findViewById(R.id.opcBtnTerminar);
        editor  = preferences.edit();
        if(pref[2] == "1"){     //Si boton terminar esta activo
            checkBox.setChecked(true);
        }
        //Escucha si el estado del checkbox cambia
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    editor.putBoolean("terminacion", true);
                    editor.commit();

                }
                else{
                    editor.putBoolean("terminacion", false);
                    editor.commit();
                }
            }
        });
    }

    private void getPref(){
        pref= MainActivity.obtenerPreferencias(this);

    }
    //Se llama al pulsar sobre usuario
    public void usuario(View view){
        showAlertDialog("Usuario", 0, "userServidor");
    }
    //Se llama al pulsar sobre contraseña
    public void contrasena(View view){
        showAlertDialog("Contraseña", 1, "passServidor");
    }
    public void btnTerminar(){


    }
    private void showAlertDialog(String titulo, final int num_preferencia, final String key){
        TextInputLayout textInputLayout;
        final AlertDialog.Builder dialog= new AlertDialog.Builder(this);
        dialog.setTitle(titulo);
        LayoutInflater inflater=LayoutInflater.from(this);
        View view =inflater.inflate(R.layout.edittext_preferencias, null);
        editText = (TextInputEditText)view.findViewById(R.id.edtPass);
        textInputLayout = (TextInputLayout)view.findViewById(R.id.edtPassText);
        if(num_preferencia == 0){
            textInputLayout.setPasswordVisibilityToggleEnabled(false);
            editText.setInputType(InputType.TYPE_CLASS_TEXT);
        }
        dialog.setView(view);
        editText.setText(pref[num_preferencia]);
        dialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                editor.putString(key, editText.getText().toString());
                editor.commit();
                getPref();

                if(num_preferencia == 1){
                    validarPrefs();
                }
            }
        });
        dialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }
    public void validarPrefs(){

            ABCNumOrden altaNumOrden = new ABCNumOrden(this);
            ABCNumOrden.ObtenerC1 getCampo1 = altaNumOrden.new ObtenerC1(this, this);
            getCampo1.execute();


    }

    @Override
    public void onBackPressed() {
        btnTerminar();
        super.onBackPressed();

    }

    /*
    Se tenia que implementar :v
     */
    @Override
    public void OnAltaCompleted(String respuesta) {

    }


    @Override
    public void OnObtenerCampoCompleted(String campo_1) {

        try {
            JSONObject jsonObject = new JSONObject(campo_1);
            if(jsonObject.getInt("codigo") == 0) {
                Toast.makeText(this , "Conexión Exitosa", Toast.LENGTH_SHORT).show();
            }
        }
        catch (JSONException e){
            if(campo_1.equals("Network is unreachable")){
                MainActivity.showAlertDialog(this);
            }
                else if(!campo_1.isEmpty()) {
                    Toast.makeText(this , "Usuario o Contraseña invalidos", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (e.getMessage() == null)
                        Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

        }
    }

}
