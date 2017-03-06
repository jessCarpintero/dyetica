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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.dyetica.app.R;
import com.dyetica.app.persistence.ClientHTTP;
import com.dyetica.app.utils.MethodsUtil;
import com.dyetica.app.utils.MyWebViewClient;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class BlogFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    private WebView web;
    private String urlInit;

    public BlogFragment() {
        // Required empty public constructor
    }

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
        setHasOptionsMenu(true);
        AttemptBlog attemptBlog = new AttemptBlog();
        try {
            urlInit = "http://dyetica.com/blog";
            web = (WebView) rootView.findViewById(R.id.webViewBlog);
            web.setInitialScale(1);
            web.getSettings().setUseWideViewPort(true);
            web.getSettings().setBuiltInZoomControls(true);
            web.getSettings().setJavaScriptEnabled(true);
            web.getSettings().setDomStorageEnabled(true);
            web.setWebViewClient(new MyWebViewClient(urlInit));

            StringBuilder htmlData = new StringBuilder();
            htmlData.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"app-style.css\" />");
            htmlData.append(attemptBlog.execute((Void) null).get());
            if (MethodsUtil.isConnected(getActivity().getApplicationContext())) {
                if (("").equals(htmlData)) {
                    web.loadUrl(urlInit);
                } else {
                    web.loadDataWithBaseURL("file:///android_asset/.", htmlData.toString(), "text/html", "UTF-8", null);
                }
            } else {
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
                doc = Jsoup.connect(urlInit).get();
                html = doc.outerHtml();
                return html;
            } catch (IOException e) {
                Log.e("BlogFragment", "Error execution Attemp Blog");
                return html;
            }
        }

        }
}
