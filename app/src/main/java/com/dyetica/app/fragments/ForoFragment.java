package com.dyetica.app.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.dyetica.app.R;
import com.dyetica.app.utils.MethodsUtil;
import com.dyetica.app.utils.MyWebViewClient;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ForoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ForoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ForoFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    private WebView web;
    private String urlInit;

    public ForoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ForoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ForoFragment newInstance(String param1, String param2) {
        ForoFragment fragment = new ForoFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_foro, container, false);
        setHasOptionsMenu(true);
        AttemptForo attemptForo = new AttemptForo();
        try {
            String urlBase = "http://dyetica.com/foro";
            urlInit = "http://dyetica.com/foro/categorias";
            web = (WebView) rootView.findViewById(R.id.webViewForo);
            web.setInitialScale(1);
            web.getSettings().setUseWideViewPort(true);
            web.getSettings().setBuiltInZoomControls(true);
            web.setWebViewClient(new MyWebViewClient(urlBase));


            StringBuilder htmlData = new StringBuilder();
            htmlData.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"app-style.css\" />");
            htmlData.append("<base href=\"http://dyetica.com/foro/categorias\"");
            htmlData.append(attemptForo.execute((Void) null).get());
            if (MethodsUtil.isConnected(getActivity().getApplicationContext())) {
                if (("").equals(htmlData)) {
                    web.loadUrl(urlInit);
                } else {
                    web.loadDataWithBaseURL("file:///android_asset/.", htmlData.toString(), "text/html", "UTF-8", null);
                }
            }  else {
                Toast.makeText(getActivity(), getString(R.string.error_connection), Toast.LENGTH_LONG).show();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return rootView;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.getItem(0).setEnabled(false);
        menu.getItem(0).setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }

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

    public class AttemptForo extends AsyncTask<Void, Void, String> {
        private ProgressDialog pDialog;


        @Override
        protected String doInBackground(Void... urls) {
            Document doc;
            String html = "";
            try {
                doc = Jsoup.connect(urlInit).get();
                html = doc.outerHtml();
                Log.d("ForoFragment", "Dentro del attempFORO: ");
                return html;
            } catch (IOException e) {
                Log.e("ForoFragment", "Error execution Attemp Foro");
                return html;
            }
        }

    }
}
