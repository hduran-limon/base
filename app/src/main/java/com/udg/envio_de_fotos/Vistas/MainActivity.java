package com.udg.envio_de_fotos.Vistas;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.udg.envio_de_fotos.R;
import com.udg.envio_de_fotos.Servicios.UpdateApp;

public class MainActivity extends AppCompatActivity {
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       setSupportActionBar(toolbar);
       toolbar.setOverflowIcon(getResources().getDrawable(R.drawable.ic_action_menu));
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

    }

    static public void showAlertDialog(Context context){
        final AlertDialog.Builder mensaje=new AlertDialog.Builder(context);
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


    @Override
    protected void onPause(){
       super.onPause();


    }

    /**
     * Abre las preferencias de usuario.
     * */
    public void lanzarPreferencias(View view) {
        Intent i = new Intent(this, PreferenciasActivity.class);
        startActivity(i);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {//Menu
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true; // true -> el menú ya está visible
    }

    /**
        Metodo para realizar x accion cuando se pulse sobre un item del menu.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.btnPreferencias) {
            lanzarPreferencias(null);
            return true;
        }
        else if (id == R.id.btnAcercaDe) {
            acercaDe();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }
    /**
        Valida que el usuario tenga configuradas las preferencias
    */

    /**
        Devuelve un array con los valores de las preferencias de usuario
     */
    public  static String[] obtenerPreferencias(Context contexto){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(contexto);
        boolean btnTerminar = false;
        if(contexto.getString(R.string.botonTerminar).equals("1")){
            btnTerminar = true;
        }
        String [] prefe= new String[3];
        String user= pref.getString("userServidor", "");
        String passS=pref.getString("passServidor", "");
        Boolean btn=pref.getBoolean("terminacion", btnTerminar);

        if(btn){
            prefe[2]="1";
        }
        else
            prefe[2]="0";

        prefe[0]=user;
        prefe[1]=passS;
        return prefe;
    }


    /**
     * Muestra un dialogo con info de la version
     */
    private void acercaDe(){
        String version = "";
        try {
            version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        new AlertDialog.Builder(MainActivity.this).setTitle("Acerca de.").setMessage("\n\nv"+version).setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).show();
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {
                case 0:
                    AltaNumOrdenFragment altaNumOrdenFragment= new AltaNumOrdenFragment();
                    return  altaNumOrdenFragment;
                case 1:
                    BuscarFragment buscarFragment=new BuscarFragment();
                    return buscarFragment;

            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Alta Orden";
                case 1:
                    return "Buscar";
            }
            return null;
        }
    }

    /**
     * Muestra un cuadro de dialogo para solicitar permisos al usuario
     */
    public static void pedirPermiso(final String permiso, String justificion, final int requestCode, final Activity actividad){
        if (ActivityCompat.shouldShowRequestPermissionRationale(actividad, permiso)){
            new AlertDialog.Builder(actividad).setTitle("Solicitud de permiso").setMessage(justificion)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int whichButton) {
                            ActivityCompat.requestPermissions(actividad, new String[]{permiso}, requestCode);
                        }
                    })
                    .show();
        }
        else {
            ActivityCompat.requestPermissions(actividad, new String[]{permiso}, requestCode);
        }

    }



    @Override
    public void onBackPressed() {
        AlertDialog.Builder mensaje=new AlertDialog.Builder(this);
        mensaje.setTitle("¿Desea Salir de la Aplicacion?");
        mensaje.setCancelable(false);
        mensaje.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        mensaje.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        mensaje.show();
    }


}
