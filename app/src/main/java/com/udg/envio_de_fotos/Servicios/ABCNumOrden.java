package com.udg.envio_de_fotos.Servicios;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.udg.envio_de_fotos.Modelos.OnAOCompleted;
import com.udg.envio_de_fotos.Modelos.OnABCCompleted;
import com.udg.envio_de_fotos.Modelos.OnCheckUpdateCompleted;
import com.udg.envio_de_fotos.Modelos.OnTaskCompleted;
import com.udg.envio_de_fotos.Vistas.MainActivity;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.cert.X509Certificate;
import java.util.List;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class ABCNumOrden {
    String usuario;
    String contrasena;
    String campo1;
    String campo2;
    String campo3;
    String campo4;
    String campo5;
    String campo6 ;
    URL url;
    Context context;
    HttpsURLConnection conexion;
    DataOutputStream request;
    final String boundary =  "*****";
    final String crlf = "\r\n";
    final String twoHyphens = "--";
    int intentos;

    Bitmap[] bitmaps;

    public ABCNumOrden(String campo1, String campo2, String campo3, String campo4,
                       String campo5, String campo6, Context context) {
        String[] pref= MainActivity.obtenerPreferencias(context);
        this.usuario=pref[0];
        this.contrasena=pref[1];
        this.campo1 = campo1;
        this.campo2 = campo2;
        this.campo3 = campo3;
        this.campo4 = campo4;
        this.campo5 = campo5;
        this.campo6 = campo6;
        conexion=null;
        this.intentos = 0;
    }
    public ABCNumOrden(Context context){
        String[] pref= MainActivity.obtenerPreferencias(context);
        this.usuario=pref[0];
        this.contrasena=pref[1];
        this.context=context;
        this.intentos = 0;
    }
    public ABCNumOrden(Context context, List<Bitmap> bitmaps){
        String[] pref= MainActivity.obtenerPreferencias(context);
        this.usuario=pref[0];
        this.contrasena=pref[1];
        this.context=context;
        this.bitmaps = new Bitmap[bitmaps.size()];
        this.bitmaps = bitmaps.toArray(this.bitmaps);
        this.intentos = 0;
    }

    public String obten_campo1() throws Exception {
        String respuesta = "ninguna respuesta";
        setSelfSignedCertificate();
        String comando="FOTOGALERIA_OBTEN_CAMPO1";
        String urlRequest="https://201.131.126.178/bulkSMS/ProcesaHttpReq";
        String urlParametros =
                "usuario=" + URLEncoder.encode(usuario, "UTF-8") +
                        "&contrasena=" + URLEncoder.encode(contrasena, "UTF-8") +
                        "&comando=" + URLEncoder.encode(comando, "UTF-8");

        respuesta = hacerRequest(urlRequest, urlParametros);

        return respuesta;
    }
    public String buscaHoy() throws Exception {
        String respuesta = "ninguna respuesta";
        setSelfSignedCertificate();
        String comando="FOTOGALERIA_BUSCA_HOY";
      String urlRequest="https://201.131.126.178/bulkSMS/ProcesaHttpReq";
        String urlParametros =
                "usuario=" + URLEncoder.encode(usuario, "UTF-8") +
                        "&contrasena=" + URLEncoder.encode(contrasena, "UTF-8") +
                        "&comando=" + URLEncoder.encode(comando, "UTF-8");

        respuesta = hacerRequest(urlRequest, urlParametros);



        return respuesta;
    }
    public String buscaCualquierDia(String orden) throws Exception {
        String respuesta = "ninguna respuesta";
        setSelfSignedCertificate();
        String comando="FOTOGALERIA_BUSCA_CUALQUIER_DIA";
        String urlRequest="https://201.131.126.178/bulkSMS/ProcesaHttpReq";
        String urlParametros =
                "usuario=" + URLEncoder.encode(usuario, "UTF-8") +
                        "&contrasena=" + URLEncoder.encode(contrasena, "UTF-8") +
                        "&orden=" + URLEncoder.encode(orden, "UTF-8") +
                        "&comando=" + URLEncoder.encode(comando, "UTF-8");

        respuesta = hacerRequest(urlRequest, urlParametros);

        return respuesta;
    }
    public String buscaDatos(String id_registro) throws Exception {
        String respuesta = "ninguna respuesta";
        setSelfSignedCertificate();
        String comando="FOTOGALERIA_BUSCA_DATOS";
        String urlRequest="https://201.131.126.178/bulkSMS/ProcesaHttpReq";
        String urlParametros =
                "usuario=" + URLEncoder.encode(usuario, "UTF-8") +
                        "&contrasena=" + URLEncoder.encode(contrasena, "UTF-8") +
                        "&id_registro=" + URLEncoder.encode(id_registro, "UTF-8") +
                        "&comando=" + URLEncoder.encode(comando, "UTF-8");

        respuesta = hacerRequest(urlRequest, urlParametros);

        return respuesta;
    }
    public String baja( String id_registro) throws Exception {
        String respuesta = "ninguna respuesta";
        setSelfSignedCertificate();
        String comando = "FOTOGALERIA_BAJA";
        //String urlRequest="https://localhost:8443/bulkSMS/ProcesaHttpReq";
        String urlRequest="https://201.131.126.178/bulkSMS/ProcesaHttpReq";
        String urlParametros =
                "usuario=" + URLEncoder.encode(usuario, "UTF-8") +
                        "&contrasena=" + URLEncoder.encode(contrasena, "UTF-8") +
                        "&id_registro=" + URLEncoder.encode(id_registro, "UTF-8") +
                        "&comando=" + URLEncoder.encode(comando, "UTF-8");

        respuesta = hacerRequest(urlRequest, urlParametros);

        return respuesta;
    }
    public String actualiza(String id_registro, String campo1, String campo2, String campo3, String campo4) throws Exception {
        String respuesta = "ninguna respuesta";
        setSelfSignedCertificate();
        String comando="FOTOGALERIA_ACTUALIZA";
        String urlRequest="https://201.131.126.178/bulkSMS/ProcesaHttpReq";
        String urlParametros =
                "usuario=" + URLEncoder.encode(usuario, "UTF-8") +
                        "&contrasena=" + URLEncoder.encode(contrasena, "UTF-8") +
                        "&id_registro=" + URLEncoder.encode(id_registro, "UTF-8") +
                        "&campo1=" + URLEncoder.encode(campo1, "UTF-8") +
                        "&campo2=" + URLEncoder.encode(campo2, "UTF-8") +
                        "&campo3=" + URLEncoder.encode(campo3, "UTF-8") +
                        "&campo4=" + URLEncoder.encode(campo4, "UTF-8") +
                        "&comando=" + URLEncoder.encode(comando, "UTF-8");

        respuesta = hacerRequest(urlRequest, urlParametros);

        return respuesta;
    }
    public String alta() throws Exception {
        String respuesta = "";
        String comando= "FOTOGALERIA_ALTA";
        setSelfSignedCertificate();
        String urlRequest="https://201.131.126.178/bulkSMS/ProcesaHttpReq";
        String urlParametros =
                "usuario=" + URLEncoder.encode(usuario, "UTF-8") +
                        "&contrasena=" + URLEncoder.encode(contrasena, "UTF-8") +
                        "&comando=" + URLEncoder.encode(comando, "UTF-8") +
                        "&campo1=" + URLEncoder.encode(campo1, "UTF-8") +
                        "&campo2=" + URLEncoder.encode(campo2, "UTF-8") +
                        "&campo3=" + URLEncoder.encode(campo3, "UTF-8") +
                        "&campo4=" + URLEncoder.encode(campo4, "UTF-8") +
                        "&campo5=" + URLEncoder.encode(campo5, "UTF-8") +
                        "&campo6=" + URLEncoder.encode(campo6, "UTF-8");

       respuesta = hacerRequest(urlRequest, urlParametros);


        return respuesta;
    }

    public void setSelfSignedCertificate() throws Exception {

        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }
            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }
            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        }
        };

        // Install the all-trusting trust manager
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        /*// Create all-trusting host name verifier
        HostnameVerifier hostnameVerifier = new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                HostnameVerifier hv = HttpsURLConnection.getDefaultHostnameVerifier();
                return hv.verify("https://201.131.126.178.com", session);
            }
        };*/

        // Create all-trusting host name verifier
        HostnameVerifier hostnameVerifier = new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                //return true;
                if(hostname.equalsIgnoreCase("201.131.126.178")){
                    return true;
                }else{
                    return false;
                }
            }
        };

        // Install the all-trusting host verifier
        HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);

    }

    public String hacerRequest(String urlRequest, String urlParametros)  {
        String respuesta = "ninguna_respuesta";
        int TIMEOUT = 10000;
        conexion = null;
        try {
            //-----------------------crear conexion----------------------------
            url = new URL(urlRequest);
            conexion = (HttpsURLConnection)url.openConnection();
            conexion.setRequestMethod("GET");
            conexion.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            conexion.setRequestProperty("Content-Length", "" + Integer.toString(urlParametros.getBytes().length));
            conexion.setRequestProperty("Content-Language", "en-US");
            conexion.setUseCaches (false);
            conexion.setDoInput(true);
            conexion.setDoOutput(true);
            conexion.setConnectTimeout(TIMEOUT);
            conexion.setReadTimeout(TIMEOUT);
            //-----------------------------------------------------------------


            //--------------------Enviar Peticion------------------------------
            DataOutputStream wr = new DataOutputStream( conexion.getOutputStream() );
            wr.writeBytes(urlParametros);
            //wr.write();
            wr.flush();
            wr.close();
            //-----------------------------------------------------------------


            //-----------------------Obtener Respuesta-------------------------
            InputStream is = conexion.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is,"UTF-8"));
            String line;
            StringBuffer responsee = new StringBuffer();
            while((line = rd.readLine()) != null) {
                responsee.append(line);
                responsee.append('\r');
            }
            rd.close();
            respuesta = responsee.toString();
            //-----------------------------------------------------------------


        }catch (SocketTimeoutException e) {
            Log.d("timeout", "Tiempo de espera agotado");
            respuesta = "timeout";

        }
        catch (Exception e3){
            Log.d("timeout", e3.toString());
            respuesta = "Network is unreachable";
           // respuesta = e3.getLocalizedMessage();
        }
        finally {
            if(conexion != null) conexion.disconnect();
            Log.d("timeout", "Desconectado");
        }
        return respuesta;
    }


    public String sube_foto(String id_registro, String titulo) throws Exception {
        String respuesta = "ninguna respuesta";
        setSelfSignedCertificate();
        String comando = "FOTOGALERIA_SUBE_FOTO";
        crearRequestMultiPart();

        addFormField("usuario", usuario);
        addFormField("contrasena", contrasena);
        addFormField("comando", comando);
        addFormField("id_registro", id_registro);
        addFormField("titulo", titulo);

        for(int i = 0; i<bitmaps.length; i++)
            addFilePart("miFoto"+(i+1), bitmaps[i]);

        respuesta = enviarRequestMultiPart();

        return respuesta;
    }



    public void crearRequestMultiPart()  {
        conexion = null;
        String urlRequest="https://201.131.126.178/bulkSMS/ProcesaHttpReq";
        try {
            //-----------------------crear conexion----------------------------
            url = new URL(urlRequest);
            conexion = (HttpsURLConnection)url.openConnection();
            conexion.setUseCaches(false);
            conexion.setDoOutput(true); // indicates POST method
            conexion.setDoInput(true);
            conexion.setReadTimeout(20000);
            conexion.setConnectTimeout(20000);
            conexion.setRequestMethod("POST");
            conexion.setRequestProperty("Connection", "Keep-Alive");
            conexion.setRequestProperty("Cache-Control", "no-cache");
            conexion.setRequestProperty(
                    "Content-Type", "multipart/form-data;boundary=" + this.boundary);

            request = new DataOutputStream ( conexion.getOutputStream() );
            //-----------------------------------------------------------------
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String enviarRequestMultiPart() throws Exception {
        String respuesta = "ninguna respuesta";
        try {
            //--------------------Enviar Peticion------------------------------
            request.writeBytes(this.crlf);
            request.writeBytes(this.twoHyphens + this.boundary + this.twoHyphens + this.crlf);

            request.flush();
            request.close();
            //-----------------------------------------------------------------


            //-----------------------Obtener Respuesta-------------------------
            InputStream is = conexion.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is,"UTF-8"));
            String line;
            StringBuffer responsee = new StringBuffer();
            while((line = rd.readLine()) != null) {
                responsee.append(line);
                responsee.append('\r');
            }
            rd.close();
            respuesta = responsee.toString();
            //-----------------------------------------------------------------

        }catch (SocketTimeoutException e) {
            Log.d("timeout", "Tiempo de espera agotado");
            respuesta = "timeout";

        }
        catch (Exception e2){
            Log.d("timeout", e2.toString());
            respuesta = "Network is unreachable";
        }
        finally {
            if(conexion != null) conexion.disconnect();
        }

        return respuesta;
    }


    /**
     * Adds a form field to the request
     *
     * @param name  field name
     * @param value field value
     */
    public void addFormField(String name, String value)throws IOException  {
        request.writeBytes(this.twoHyphens + this.boundary + this.crlf);
        request.writeBytes("Content-Disposition: form-data; name=\"" + name + "\""+ this.crlf);
        request.writeBytes("Content-Type: text/plain; charset=UTF-8" + this.crlf);
        request.writeBytes(this.crlf);
        request.writeBytes(value+ this.crlf);
        request.flush();
    }


    /**
     * Adds a upload file section to the request
     *
     * @param fieldName  name attribute in <input type="file" name="..." />
     * @param uploadFile a Bitmap to be uploaded
     * @throws IOException
     */


    public void addFilePart(String fieldName, Bitmap uploadFile)
            throws IOException {
        String fileName = "foto";
        request.writeBytes(this.twoHyphens + this.boundary + this.crlf);
        request.writeBytes("Content-Disposition: form-data; name=\"" +
                fieldName + "\";filename=\"" +
                fileName + "\"" + this.crlf);
        request.writeBytes(this.crlf);

        byte[] bytes;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        uploadFile.compress(Bitmap.CompressFormat.JPEG, 100, stream);           //Se comprime el bitmap
        bytes = stream.toByteArray();
        request.write(bytes);
        request.writeBytes(this.crlf);
        request.flush();
    }



    public String terminado(String id_registro) throws Exception {
        String respuesta = "ninguna respuesta";
        String comando="FOTOGALERIA_TERMINADO";
        setSelfSignedCertificate();
        String urlRequest="https://201.131.126.178/bulkSMS/ProcesaHttpReq";
        String urlParametros =
                "usuario=" + URLEncoder.encode(usuario, "UTF-8") +
                        "&contrasena=" + URLEncoder.encode(contrasena, "UTF-8") +
                        "&id_registro=" + URLEncoder.encode(id_registro, "UTF-8") +
                        "&comando=" + URLEncoder.encode(comando, "UTF-8");

        respuesta = hacerRequest(urlRequest, urlParametros);
        return respuesta;
    }

    /*
        Clases ejecutan los metodos en un hilo secundario.
        -------------------------------------------------
     */

    public class SubirFotos extends  AsyncTask<String, String, String>
    {
        OnTaskCompleted respuesta;
        ProgressDialog progressDialog;
        public SubirFotos(OnTaskCompleted respuesta, Context contex){
            this.respuesta = respuesta;
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Enviando...");
            progressDialog.setCancelable(false);
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }


        @Override
        protected String doInBackground(String... strings) {
            String res = "";
            Log.d("timeout", "Subir Foto: ");
            try{
                res = sube_foto(strings[0], strings[1]);
                Log.d("timeout", "respuesta: "+res);
                if(res.equals("timeout")){
                    while (intentos != 3){
                        intentos++;
                        Log.d("timeout", "intentos: "+intentos);
                        res = sube_foto(strings[0], strings[1]);
                        if(!res.equals("timeout"))
                            break;
                    }
                }
                if(intentos == 3 || res =="timeout"){
                    Log.d("timeout", "Sin conexion");
                    res = "Network is unreachable";
                }
            }catch (Exception e){
               e.printStackTrace();
            }
            return  res;

        }
        @Override
        protected void onPostExecute(String s) {
            respuesta.onSendCompleted(s);
            progressDialog.dismiss();
        }

    }
    public  class Baja extends AsyncTask<String, String, String[]>{
        OnABCCompleted respuesta;
        ProgressDialog progressDialog;
        public Baja(OnABCCompleted respuesta, Context context){
            this.respuesta=respuesta;
            progressDialog= new ProgressDialog(context);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Eliminando...");
        }
        @Override
        protected void onPostExecute(String[] s) {
            respuesta.OnBajaCompleted(s);
            progressDialog.dismiss();
        }

        @Override
        protected String[] doInBackground(String... strings) {
            String[] res= new String[2];
            res[1]=strings[1];
            try {
                res[0]=baja(strings[0]);
                Log.d("timeout", "respuesta: "+res[0]);
                if(res[0].equals("timeout")){
                    while (intentos != 3){
                        intentos++;
                        Log.d("timeout", "intentos: "+intentos);
                        res[0] = baja(strings[0]);
                        if(!res[0].equals("timeout"))
                            break;
                    }
                }
                if(intentos == 3 || res[0].equals("timeout")){
                    Log.d("timeout", "Sin conexion");
                    res[0] = "Network is unreachable";
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return res;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }
    }
    public class Actualizar extends AsyncTask<String, String, String>{
        OnABCCompleted respuesta;
        ProgressDialog progressDialog;
        public Actualizar(OnABCCompleted respuesta, Context context){
            this.respuesta=respuesta;
            progressDialog= new ProgressDialog(context);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Actualizando...");
        }

        @Override
        protected void onPreExecute() {
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            respuesta.OnActualizarCompleted(s);
            progressDialog.dismiss();
        }

        @Override
        protected String doInBackground(String... strings) {
            String res="";
            try {
                res=actualiza(strings[0], strings[1], strings[2], strings[3], strings[4]);
                Log.d("timeout", "respuesta: "+res);
                if(res.equals("timeout")){
                    while (intentos != 3){
                        intentos++;
                        Log.d("timeout", "intentos: "+intentos);
                        res=actualiza(strings[0], strings[1], strings[2], strings[3], strings[4]);
                        if(!res.equals("timeout"))
                            break;
                    }
                }
                if(intentos == 3 || res =="timeout"){
                    Log.d("timeout", "Sin conexion");
                    res = "Network is unreachable";
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return res;
        }
    }
    public  class BuscaDatos extends AsyncTask<String, String, String[]>{
        OnABCCompleted respuesta;
        ProgressDialog progressDialog;
        public BuscaDatos(OnABCCompleted respuesta, Context context){
            this.respuesta= respuesta;
            progressDialog= new ProgressDialog(context);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Buscando...");
        }
        @Override
        protected void onPreExecute() {
            progressDialog.show();
            super.onPreExecute();

        }

        @Override
        protected String[] doInBackground(String... strings) {
            String[] res= new String[2];
            res[1]=strings[0];
            try {
                Log.d("timeout", "Busca Datos:");
                res[0]=buscaDatos(strings[0]);
                Log.d("timeout", "respuesta: "+res);
                if(res[0].equals("timeout")){
                    while (intentos != 3){
                        intentos++;
                        Log.d("timeout", "intentos: "+intentos);
                        res[0]=buscaDatos(strings[0]);
                        if(!res[0].equals("timeout"))
                            break;
                    }
                }
                if(intentos == 3 || res[0].equals("timeout")){
                    Log.d("timeout", "Sin conexion");
                    res[0] = "Network is unreachable";
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }

            return res;
        }
        @Override
        protected void onPostExecute(String[] s) {
          respuesta.OnBuscaDatosCompleted(s);
          progressDialog.dismiss();
        }
    }
    public class BuscaCualquierDia extends AsyncTask<String, String, String>{
        OnABCCompleted respuesta;
        ProgressDialog progressDialog;
    public BuscaCualquierDia(OnABCCompleted respuesta, Context context) {
        this.respuesta = respuesta;
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Buscando...");
    }
        @Override
        protected void onPreExecute() {
            progressDialog.show();
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... strings) {
            String res="";
            try{
                res=buscaCualquierDia( strings[0]);
                Log.d("timeout", "respuesta: "+res);

                if(res.equals("timeout")){
                    while (intentos != 3){
                        intentos++;
                        Log.d("timeout", "intentos: "+intentos);
                        res=buscaCualquierDia(strings[0]);
                        if(!res.equals("timeout"))
                            break;
                    }
                }

                if(intentos == 3 || res =="timeout"){
                    Log.d("timeout", "Sin conexion");
                    res = "Network is unreachable";
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
            return res;
        }
        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            respuesta.OnTerminarCompleted(s);
            //super.onPostExecute(s);
        }

    }
    public class BuscarHo extends AsyncTask<Void, Void, String>{
        OnABCCompleted respuesta;
        ProgressDialog progressDialog;
        public BuscarHo(OnABCCompleted respuesta , Context context){
            progressDialog= new ProgressDialog(context);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Buscando...");
            this.respuesta= respuesta;
        }
        @Override
        protected void onPreExecute() {
            progressDialog.show();
            super.onPreExecute();
        }


        @Override
        protected String doInBackground(Void... voids) {
          String res="";
          Log.d("timeout", "Intento buscarHoy 1");
          try
          {
              res=buscaHoy();
              Log.d("timeout", "respuesta: "+res);

              if(res.equals("timeout")){
                  while (intentos != 3){
                      intentos++;
                      Log.d("timeout", "intentos: "+intentos);
                      res=buscaHoy();
                      if(!res.equals("timeout"))
                          break;
                  }
              }

              if(intentos == 3 || res =="timeout"){
                  Log.d("timeout", "Sin conexion");
                  res = "Network is unreachable";
              }
          }
          catch (Exception e){
              Log.d("timeout", "TIMEOUT");
          }
          return res;
        }
        @Override
        protected void onPostExecute(String s) {
            respuesta.OnActualizarCompleted(s);
            progressDialog.dismiss();
        }

    }

    public class ObtenerC1 extends  AsyncTask<Void, Void, String>{
        OnAOCompleted campo_1;
        ProgressDialog progressDialog;
        public ObtenerC1(OnAOCompleted campo_1, Context context)
        {
            this.campo_1=campo_1;
            progressDialog= new ProgressDialog(context);
            progressDialog.setCancelable(false);
            progressDialog.setCancelable(false);
        }



        @Override
        protected void onPreExecute() {
            progressDialog.show();
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(Void... voids) {
            String respuesta="";
            Log.d("timeout", "Get Campo 1:");
            try {
                respuesta=obten_campo1();
                Log.d("timeout", "respuesta: "+respuesta);
                if(respuesta.equals("timeout")){
                    while (intentos != 3){
                        intentos++;
                        Log.d("timeout", "intentos: "+intentos);
                        respuesta=obten_campo1();
                        if(!respuesta.equals("timeout"))
                            break;
                    }
                }

                if(intentos == 3 || respuesta.equals("No route to host")){
                    Log.d("timeout", "Sin conexion");
                    respuesta = "Network is unreachable";
                }
            }
            catch (Exception e){
                e.printStackTrace();
                if(e.getLocalizedMessage().equals("No route to host")){
                    respuesta = "Network is unreachable";
                }
            }
            return respuesta;
        }
        @Override
        protected void onPostExecute(String s) {
            campo_1.OnObtenerCampoCompleted(s);
            progressDialog.dismiss();

        }
    }

    public class Alta extends AsyncTask<String, String, String> {
        OnAOCompleted respuesta;
        ProgressDialog progressDialog;
        public Alta(OnAOCompleted respuesta, Context context){
            this.respuesta=respuesta;
            progressDialog= new ProgressDialog(context);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Conectando con el servidor...");

        }
        @Override
        protected void onPreExecute() {
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String res="";
            try {
                Log.d("timeout", "Alta: ");
                res=alta();
                Log.d("timeout", "respuesta: "+res);
                if(res.equals("timeout")){
                    while (intentos != 3){
                        intentos++;
                        Log.d("timeout", "intentos: "+intentos);
                        res=alta();
                        if(!res.equals("timeout"))
                            break;
                    }
                }
                if(intentos == 3 || res.equals("timeout")){
                    Log.d("timeout", "Sin conexion");
                    res = "Network is unreachable";
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
            return res;
        }

        @Override
        protected void onPostExecute(String s) {
            respuesta.OnAltaCompleted(s);
            progressDialog.dismiss();
        }
    }
    public class Terminado extends AsyncTask<String, String, String> {
        OnABCCompleted respuesta;
        ProgressDialog progressDialog;
        public Terminado(OnABCCompleted respuesta, Context context){
            this.respuesta=respuesta;
            progressDialog= new ProgressDialog(context);
            progressDialog.setMessage("Conectando con el servidor...");
            progressDialog.setCancelable(false);
        }
        @Override
        protected void onPreExecute() {
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String res="";
            try {
                res=terminado(strings[0]);
                Log.d("timeout", "respuesta: "+res);
                if(res.equals("timeout")){
                    while (intentos != 3){
                        intentos++;
                        Log.d("timeout", "intentos: "+intentos);
                        res=terminado(strings[0]);
                        if(!res.equals("timeout"))
                            break;
                    }
                }
                if(intentos == 3 || res.equals("timeout")){
                    Log.d("timeout", "Sin conexion");
                    res = "Network is unreachable";
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
            return res;
        }

        @Override
        protected void onPostExecute(String s) {
            respuesta.OnTerminarCompleted(s);
            progressDialog.dismiss();
        }
    }


}


