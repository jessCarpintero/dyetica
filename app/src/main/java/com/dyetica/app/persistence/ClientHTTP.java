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

    private static final String TAG_MESSAGE = "message";

    public Map<String, String> makeHttpRequest(URL url, String method, String authorization, Uri.Builder params){
        JSONObject jsonObject;
        HttpURLConnection con = null;
        String error = "", message = "";
        Map<String, String> reponse =  new HashMap<String, String>();
        try {
            con = getConnection(url, method, authorization);

            // Get the state of the resource
            int statusCode = con.getResponseCode();

            if (statusCode != 200){
                Log.e("ClientHTTP", "Error server with status code: " + statusCode);
            } else {
                if ("POST".equals(method)){
                    OutputStream os = con.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                    writer.write(params.build().getEncodedQuery());
                    writer.flush();
                    writer.close();
                    os.close();
                    Log.d("ClientHTTP", "Closed WRITE and OS");

                }

                InputStream inputStream = con.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                inputStream.close();

                Log.d("ClientHTTP", "Closed inputStream");


                // try parse the string to a JSON object
                jsonObject = new JSONObject(sb.toString());

                // json error element
                error = jsonObject.getString("error");
                if (error == "false") {
                    Log.d("Text BAndO Success!", jsonObject.toString());
                    message = jsonObject.getString(TAG_MESSAGE);
                } else {
                    Log.d("Text BAndO Failure!", jsonObject.getString(TAG_MESSAGE));
                    message = jsonObject.getString(TAG_MESSAGE);
                }
            }
            reponse.put("status", String.valueOf(statusCode));
            reponse.put("error", error);
            reponse.put("message", message);
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

    private HttpURLConnection getConnection(URL url, String method, String authorization) throws ProtocolException {
        //Establishing a connection
        HttpURLConnection con = null;
        try {
            con = (HttpURLConnection) url.openConnection();

            con.setConnectTimeout(10000);
            con.setReadTimeout(15000);
            con.setRequestProperty("X-Authorization", authorization);
            switch (method) {
                case "POST":
                    con.setRequestMethod("POST");
                    con.setDoInput(true);
                    con.setDoOutput(true);
                    con.setUseCaches(false);
                    break;
                case "GET":
                    con.setRequestMethod("GET");
                    break;
            }
        } catch (IOException e) {
            Log.e("ClientHTTP", "Error initializing the connection");
        }
        return con;
    }


}
