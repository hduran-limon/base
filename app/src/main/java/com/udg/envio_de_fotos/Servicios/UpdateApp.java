package com.udg.envio_de_fotos.Servicios;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.udg.envio_de_fotos.Modelos.OnCheckUpdateCompleted;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.SocketTimeoutException;

public class UpdateApp extends AsyncTask<Void, String, String> {
    Context context;
    OnCheckUpdateCompleted res;
    ProgressDialog progressDialog;
    public UpdateApp(Context context, OnCheckUpdateCompleted res){
        this.context = context;
        this.res = res;
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);

    }
    private String validaVersion(){
        String newVersion = "";
        try {
            Document document = Jsoup.connect("https://play.google.com/store/apps/details?id=" + context.getPackageName()  + "&hl=en")
                    .timeout(10000)
                    .referrer("http://www.google.com")
                    .get();
            if (document != null) {
                //Log.d("updateAndroid", "Document: " + document);
                Elements element = document.getElementsContainingOwnText("Current Version");
                for (Element ele : element) {
                    if (ele.siblingElements() != null) {
                        Elements sibElemets = ele.siblingElements();
                        for (Element sibElemet : sibElemets) {
                            newVersion = sibElemet.text();
                        }
                    }
                }
            }

        }
        catch (SocketTimeoutException e2){
            Log.d("timeout", "Tiempo de espera agotado en checkUpdate");
            newVersion = "timeout";
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (Exception e3){
            Log.d("timeout", e3.getLocalizedMessage());
        }

        return newVersion;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        progressDialog.show();
    }

    @Override
    protected String doInBackground(Void... voids) {
        String newVersion = null;
        Log.d("timeout", "Intento checkUpdate 1");
        newVersion = validaVersion();
        if(newVersion.equals("timeout")){
            Log.d("timeout", "Intento checkUpdate 2");
            newVersion = validaVersion();
            if(newVersion.equals("timeout")){
                Log.d("timeout", "Intento checkUpdate 3");
                newVersion = validaVersion();
                if(newVersion.equals("timeout")){
                    Toast.makeText(context, "El dispositivo no cuenta con una conexion de red activa, intentelo más tarde", Toast.LENGTH_LONG);
                    newVersion = null;
                }
            }
        }
        return newVersion;

    }

    @Override
    protected void onPostExecute(String onlineVersion) {
        super.onPostExecute(onlineVersion);
        progressDialog.dismiss();
        String currentVersion = getVersionActual();
        //Log.d("updateAndroid", "Current version: " + currentVersion + " PlayStore version: " + onlineVersion);
        if (onlineVersion != null && !onlineVersion.isEmpty()) {
            res.onCheckCOmpleted(isUpdateRequired(currentVersion, onlineVersion));
        }
        else{
            res.onCheckCOmpleted(false);
        }

    }


    public boolean isUpdateRequired(String versionActual, String versionNueva) {
        boolean result = false;
        int[] versiones = new int[6];
        int i = 0, anterior = 0, orden = 0;
        if(versionActual != null && versionNueva != null){
            try{
                for(i = 0; i < 6; i++){
                    versiones[i] = 0;
                }
                i = 0;
                do{
                    i = versionActual.indexOf('.', anterior);
                    if(i > 0){
                        versiones[orden] = Integer.parseInt(versionActual.substring(anterior, i));
                    }else{
                        versiones[orden] = Integer.parseInt(versionActual.substring(anterior));
                    }
                    anterior = i + 1;
                    orden++;
                }while(i != -1);
                anterior = 0;
                orden = 3;
                i = 0;
                do{
                    i = versionNueva.indexOf('.', anterior);
                    if(i > 0){
                        versiones[orden] = Integer.parseInt(versionNueva.substring(anterior, i));
                    }else{
                        versiones[orden] = Integer.parseInt(versionNueva.substring(anterior));
                    }
                    anterior = i + 1;
                    orden++;
                }while(i != -1 && orden < 6);
                if(versiones[0] < versiones[3]){
                    result = true;
                }else if(versiones[1] < versiones[4] && versiones[0] == versiones[3]){
                    result = true;
                }else if(versiones[2] < versiones[5] && versiones[0] == versiones[3] && versiones[1] == versiones[4]){
                    result = true;
                }
            }catch (NumberFormatException e){
               e.printStackTrace();
            }catch (Exception e){
               e.printStackTrace();
            }
        }
        return result;
    }
    public String getVersionActual(){
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }
}

