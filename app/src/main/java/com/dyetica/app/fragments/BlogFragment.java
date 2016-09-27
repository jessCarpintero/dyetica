package com.dyetica.app.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.dyetica.app.R;
import com.dyetica.app.persistence.ClientHTTP;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BlogFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BlogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BlogFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    private WebView web;

    public BlogFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlogFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BlogFragment newInstance(String param1, String param2) {
        BlogFragment fragment = new BlogFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_blog, container, false);
        AttemptBlog attemptBlog = new AttemptBlog();
        try {
            web = (WebView) rootView.findViewById(R.id.webViewBlog);
            web.setInitialScale(1);
            web.getSettings().setUseWideViewPort(true);
            web.getSettings().setBuiltInZoomControls(true);
            web.setWebViewClient(new WebViewClient());

            String htmlData = null;

            htmlData = attemptBlog.execute((Void) null).get();

            if (("").equals(htmlData)){
                Log.d("NlogFragment", "Dentro de '' ");

                web.loadUrl("http://dyetica.com/blog");
            } else {
                Log.d("NlogFragment", "htmldata con valor ");

                web.loadDataWithBaseURL("file:///android_asset/.", htmlData, "text/html", "UTF-8", null);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return rootView;

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

    public class AttemptBlog extends AsyncTask<Void, Void, String> {
        private ProgressDialog pDialog;


        @Override
        protected String doInBackground(Void... urls) {
            Document doc;
            String html = "";
            try {
                doc = Jsoup.connect("http://dyetica.com/blog").get();
                doc.head().appendElement("link").attr("rel", "stylesheet").attr("type", "text/css").attr("href", "app-style.css");
                html = doc.outerHtml();
                Log.d("BlogFragment", "Dentro del attempBLOG: " + html);
                return html;
            } catch (IOException e) {
                Log.e("BlogFragment", "Error execution Attemp Blog");
                return html;
            }
        }

        }
}
