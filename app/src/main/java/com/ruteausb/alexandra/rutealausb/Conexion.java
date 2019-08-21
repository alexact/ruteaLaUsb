package com.ruteausb.alexandra.rutealausb;

import android.os.AsyncTask;
import android.text.Html;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class Conexion extends AsyncTask<String, Void, String> {

    //Atributos de clase
    private final String DOMINIO = "https://softneosas.com/sebas/rutea-la-usb/";
    private String JSON_STRING;
    private String resConsulta;
    private String campo;

//------------------------------------------

    //Constructor
    public Conexion() {
    }
//----------------------------------


//Métodos getters and Setters

    public String getDOMINIO() {
        return DOMINIO;
    }

    public String getResConsulta() {
        return this.resConsulta;
    }

//----------------------------------


    @Override
    protected String doInBackground(String... urls) {
// .execute() llama los parametros: donde parametro[0] es la url.
        try {
            return crearConexion(urls[0]);
        } catch (IOException e) {
            return "Unable to retrieve web page. URL may be invalid.";
        }
    }


    // onPostExecute Muestra el resultado de el AsyncTask
    @Override
    protected void onPostExecute(String result) {
        Log.d("Terminado:", "consulta finalizada");
    }

    @Override
    protected void onPreExecute() {
        Log.d("Iniciando:", "iniciando consulta");
    }

    //Método que crea la conexión teniendo como parametro una url
    //Método que crea la conexión teniendo como parametro una url
    private String crearConexion(String myUrl) throws IOException {
        //quitamos los espacios en la durección
        Log.i("URL", "" + myUrl);
        myUrl = myUrl.replace(" ", "%20");
        System.out.println("RESULTADO " + myUrl);
        InputStream is = null;

        try {
            //hacemos la conexión
            URL url = new URL(myUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);// / milliseconds /);
            conn.setConnectTimeout(15000);// / milliseconds /);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);

            // ejecuta el query
            conn.connect();
            int response = conn.getResponseCode();
            Log.d("respuesta", "The response is: " + response);// si es 200 se conectó
            is = conn.getInputStream();

            //pasamos el InputStream a String
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
            StringBuilder stringBuilder = new StringBuilder();
            while ((JSON_STRING = bufferedReader.readLine()) != null) {
                stringBuilder.append(JSON_STRING + "\n");
            }
            try {
                JSONObject jsonObject = new JSONObject(stringBuilder.toString().trim());
                JSONArray jsonArray = jsonObject.getJSONArray("server_response");
                JSONObject temp;
                this.resConsulta = "";
                //  Toast.makeText(getApplicationContext(), "Entró :\n ", Toast.LENGTH_SHORT).show();
                if (campo != null) {
                    if (campo.equals("1")) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            temp = jsonArray.getJSONObject(i);
                            String id = temp.get("id_nodo").toString();
                            if (id.equals("null1")) {
                                id = id.replace("null1", "1");
                            }
                            this.resConsulta += id + "," + temp.get("ruta_fotos").toString() +
                                    "," + temp.get("coordenadas").toString() + "," + temp.get("id_nodo").toString() + ";";
                        }
                    }

                    if (campo.equals("2")) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            temp = jsonArray.getJSONObject(i);
                            this.resConsulta += temp.get("id_nodo_origen").toString() + "," + temp.get("id_nodo_destino").toString() +
                                    "," + temp.get("peso_nodos").toString() + ";";
                        }
                    }
                    if (campo.equals("3")) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            temp = jsonArray.getJSONObject(i);
                            this.resConsulta += temp.get("id_lugar_interno").toString() + "," + temp.get("nombre_lugar").toString() +
                                    "," + temp.get("id_nodo").toString() + ";";
                        }
                    }

                } else {

                    for (int i = 0; i < jsonArray.length(); i++) {
                        temp = jsonArray.getJSONObject(i);
                        this.resConsulta += temp.get("id_solicitud").toString() + "," + temp.get("solicitud").toString() +
                                "," + temp.get("coordenadas").toString() + "," + temp.get("descripcion").toString() +
                                "," + temp.get("id_nodo").toString() + ";";
                    }

                }
                System.out.println("Entro a resConsulta: " + getResConsulta());

            } catch (JSONException e) {
                e.printStackTrace();
            }
            //cerramos las conexiones y readers
            bufferedReader.close();
            is.close();
            conn.disconnect();
            //guardamos el resultado de la consulta
            return resConsulta;

            // se asegura de que InputStream sea cerrado
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }


//------------------------------------------------------------



    public String urlConsultarSolicitud() {
        this.campo = null;
        return (DOMINIO + "php/consulta-solicitud.php");
    }
//----------------------------------------------------------------

    public String urlConsultarNodos() {
        this.campo = "1";
        return (DOMINIO + "php/consulta-nodo.php");
    }

    //Método que consulta todas las aristas de la base de datos
    public String urlConsultarAristas() {
        this.campo = "2";
        return (DOMINIO + "php/consulta-grafo.php");
    }

    //----------------------------------------------------------------
    //Método que consulta todos los lugares internos registrados la base de datos
    public String urlConsultarLugaresInternos() {
        this.campo = "3";
        return (DOMINIO + "php/consulta-lugares-internos.php");
    }
    //----------------------------------------------------------------
}
