package com.dyetica.app.persistence;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Jess on 02/09/2016.
 */
public class ClientHTTP {

    private InputStream inputStream = null;
    private JSONObject jsonObject = null;
    private String json = "";
    private HttpURLConnection con;

    public JSONObject makeHttpRequest(URL url){

        try {

            //Establishing a connection
            con = (HttpURLConnection) url.openConnection();

            // Get the state of the resource
            int statusCode = con.getResponseCode();

            if(statusCode!=200) {
                Log.e("ClientHTTP", "Error connecting to server with status code  " + statusCode);
            } else {
                inputStream = con.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                inputStream.close();
                json = sb.toString();

                // try parse the string to a JSON object
                jsonObject = new JSONObject(json);
            }
        } catch (JSONException e) {
            Log.e("ClientHTTP", "Error parsing data " + json);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            con.disconnect();
        }
        return jsonObject;
    }

}
