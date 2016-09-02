package com.dyetica.app.fragments;

import android.app.ProgressDialog;
import android.content.Context;
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

import com.dyetica.app.R;
import com.dyetica.app.persistence.ClientHTTP;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
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
    private static final String URL_SERVER_DYETICA = "http://www.probasmar.es/dyetica3/app/restapi/dyetica";
    private static final String TAG_MESSAGE = "message";
    private ProgressDialog pDialog;

    private String mResultDyetica;

    private OnFragmentInteractionListener mListener;

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

        mResultDyetica = getResultDyetica();

        TextView mTextDyetica = (TextView) rootView.findViewById(R.id.text_dyetica);
        mTextDyetica.setText(Html.fromHtml(mResultDyetica));

        // Inflate the layout for this fragment
        return rootView;
    }

    private String getResultDyetica(){
        String resultDyetica = "";
        try {
            resultDyetica = new String(new AttemptDyetica().execute(new URL(URL_SERVER_DYETICA)).get().getBytes(), "UTF-8");
        } catch (MalformedURLException e) {
            Log.e("DyeticaFragment", "Error Malformed URL: " + URL_SERVER_DYETICA);
        } catch (InterruptedException e) {
            Log.e("DyeticaFragment", "Interrupted AttempDyetica: " + e.getMessage());
        } catch (ExecutionException e) {
            Log.e("DyeticaFragment", "Error execution AttempDyetica: " + e.getMessage());
        } catch (UnsupportedEncodingException e) {
            Log.e("DyeticaFragment", "Error unsupported encoding in AttempDyetica: " + e.getMessage());
        }
        return resultDyetica;
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

    public class AttemptDyetica extends AsyncTask<URL, Void, String> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getContext());
            pDialog.setMessage("Getting dyetica...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(URL... urls) {

            String success, message = "";
            JSONObject jsonObject;
            ClientHTTP clientHTTP = new ClientHTTP();
            try {
                jsonObject = clientHTTP.makeHttpRequest(urls[0]);

                // json success element
                success = jsonObject.getString("error");
                if (success == "false") {
                    Log.d("Text Dyetica Succes!", jsonObject.toString());
                    message = jsonObject.getString(TAG_MESSAGE);
                    return message;
                } else {
                    Log.d("Text Dyetica Failure!", jsonObject.getString(TAG_MESSAGE));
                    message = jsonObject.getString(TAG_MESSAGE);
                    return message;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return message;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product deleted
            if ((pDialog != null) && pDialog.isShowing()) {
                pDialog.dismiss();
            }

        }
    }
}
