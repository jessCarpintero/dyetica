package com.dyetica.app.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dyetica.app.LoginActivity;
import com.dyetica.app.R;
import com.dyetica.app.model.Information;
import com.dyetica.app.persistence.ClientHTTP;
import com.dyetica.app.persistence.DBManager;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DyeticaFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DyeticaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DyeticaFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TAG_MESSAGE = "message";
    private ProgressDialog pDialog;

    private String mResultDyetica;

    private OnFragmentInteractionListener mListener;

    private DBManager dbManager;

    public DyeticaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment DyeticaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DyeticaFragment newInstance() {
        DyeticaFragment fragment = new DyeticaFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Set fragment_attractions.xml to be the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_dyetica, container, false);
        dbManager = DBManager.getInstance(getActivity());

        mResultDyetica = getHtmlDyetica();

        TextView mTextDyetica = (TextView) rootView.findViewById(R.id.text_dyetica);
        mTextDyetica.setText(Html.fromHtml(mResultDyetica));

        // Inflate the layout for this fragment
        return rootView;
    }

    /** Get results dyetica from server or DB
     *
     * @return result
     */
    private String getHtmlDyetica(){
        String html = "", statusCode, message;
        Boolean error;
        Map<String, String> resultBasesObjectives;
        try {
            resultBasesObjectives = new AttemptDyetica().execute(new URL(getString(R.string.url_dyetica))).get();
            statusCode = resultBasesObjectives.get(getString(R.string.status));
            message = resultBasesObjectives.get(getString(R.string.message));
            error = Boolean.parseBoolean(resultBasesObjectives.get(getString(R.string.error)));

            if (getString(R.string.status_400).equals(statusCode) || getString(R.string.status_401).equals(statusCode)){
                Toast.makeText(getActivity(), getString(R.string.error_authetication), Toast.LENGTH_LONG).show();
                Intent i = new Intent(getActivity(), LoginActivity.class);
                startActivity(i);
            } else if (getString(R.string.status_200).equals(statusCode)) {
                if (error){
                    Information information = dbManager.getInformation(getString(R.string.constant_dyetica));
                    if (null == information){
                        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                    } else {
                        Log.e("DyeticaFragment", "Valor de base de datos, error de servidor: " + information.getHtml());
                        html = information.getHtml();
                    }
                } else {
                    html = new String(message.getBytes(),  "UTF-8");
                    saveDyeticaInDB(html);
                }
            }
        } catch (MalformedURLException e) {
            Log.e("DyeticaFragment", "Error Malformed URL: " + getString(R.string.url_dyetica));
        } catch (InterruptedException e) {
            Log.e("DyeticaFragment", "Interrupted AttempDyetica: " + e.getMessage());
        } catch (ExecutionException e) {
            Log.e("DyeticaFragment", "Error execution AttempDyetica: " + e.getMessage());
        } catch (UnsupportedEncodingException e) {
            Log.e("DyeticaFragment", "Error unsupported encoding in AttempDyetica: " + e.getMessage());
        }
        return html.trim();
    }

    private void saveDyeticaInDB(String result) {
        Log.d("DyeticaFragment", "Entrando de saveDyeticaInDB: " );
        String screen = getString(R.string.constant_dyetica);
        Information information = dbManager.getInformation(screen);
        if (null == information){
            information = Information.getInstance(information);
            information.setHtml(result);
            information.setScreen(screen);
            dbManager.addInformation(information);
        } else {
            information.setHtml(result);
            dbManager.updateInformation(information);
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onPause() {
        super.onPause();
        if ((pDialog != null) && pDialog.isShowing())
            pDialog.dismiss();
        pDialog = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public class AttemptDyetica extends AsyncTask<URL, Void, Map<String, String>> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getContext());
            pDialog.setMessage(getString(R.string.dialog_get_data));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Map<String, String> doInBackground(URL... urls) {
            SharedPreferences prefs = getActivity().getSharedPreferences("myPreferences", Context.MODE_PRIVATE);
            Map<String, String> response = ClientHTTP.makeHttpRequest(urls[0], "GET", prefs.getString("apiKey", ""), null);
            return response;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        @Override
        protected void onPostExecute(Map<String, String> result) {
            // dismiss the dialog once product deleted
            if ((pDialog != null) && pDialog.isShowing()) {
                Log.d("DyeticaFragment", "Entry onPostExecute");
                pDialog.dismiss();
            }

        }
    }
}
