package com.dyetica.app.persistence;

import android.net.Uri;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jess on 02/09/2016.
 */
public class ClientHTTP {

    //private static ClientHTTP clientHTTP;
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_ERROR = "error";
    private static final String TAG_STATUS = "status";

   // public static ClientHTTP getInstance(){
     //   if (null == clientHTTP){
       //     clientHTTP = new ClientHTTP();
        //}
        //return clientHTTP;
    //}

    private ClientHTTP(){

    }


    public static Map<String, String> makeHttpRequest(URL url, String method, String authorization, String params) {
        //Establishing a connection
        HttpURLConnection con = null;
        try {
            con = (HttpURLConnection) url.openConnection();
            Log.d("ClientHttp", "Creando conexión");
            con.setConnectTimeout(10000);
            con.setReadTimeout(15000);
            if (authorization != "")
                con.setRequestProperty("X-Authorization", authorization);
            switch (method) {
                case "POST":
                    con.setRequestMethod("POST");
                    con.setDoInput(true);
                    con.setDoOutput(true);
                    con.setUseCaches(false);
                    Log.d("ClientHttp", "Dentro de POST");
                    if (params != null)
                        paramsUrl(con, params);
                    break;
                case "GET":
                    Log.d("ClientHttp", "Dentro de GET");
                    con.setRequestMethod("GET");
                    if (params != null)
                        paramsUrl(con, params);
                    break;
            }
        } catch (ProtocolException e) {
            Log.e("ClientHTTP", "Error in protocol the connection");
        } catch (IOException e) {
            Log.e("ClientHTTP", "Error initializing the connection");
        }
        return getResponse(con);
    }


    private static Map<String, String> getResponse(HttpURLConnection con){
        JSONObject jsonObject;
        String error = "", message = "";
        Map<String, String> reponse =  new HashMap<String, String>();
        try {
            // Get the state of the resource
            int statusCode = con.getResponseCode();

            if (statusCode != 200){
                Log.e("ClientHTTP", "Error server with status code: " + statusCode);
            } else {
                InputStream inputStream = con.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                inputStream.close();

                // try parse the string to a JSON object
                jsonObject = new JSONObject(sb.toString());

                // json error element
                error = jsonObject.getString(TAG_ERROR);

                if (error == "false") {
                    message = jsonObject.getString(TAG_MESSAGE);
                } else {
                    message = jsonObject.getString(TAG_MESSAGE);
                }
            }
            reponse.put(TAG_STATUS, String.valueOf(statusCode));
            reponse.put(TAG_ERROR, error);
            reponse.put(TAG_MESSAGE, message);
        } catch (JSONException e) {
            Log.e("HTTPClient", "Error parsing data ");
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            con.disconnect();
            Log.d("ClientHTTP", "Closed CONNECTION");
        }
        return reponse;
    }

    private static void paramsUrl(HttpURLConnection con, String params){
        try {
            OutputStream os = con.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(params);
            writer.flush();
            writer.close();
            os.close();
        } catch (IOException e) {
           Log.e("ClientHTTP","Error ");
        }
    }

}
