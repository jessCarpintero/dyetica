package com.dyetica.app.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dyetica.app.R;
import com.dyetica.app.model.DieteticProfile;
import com.dyetica.app.persistence.DBManager;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DieteticProfile2Fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DieteticProfile2Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DieteticProfile2Fragment extends Fragment {
    private static final String ID_USER = "idUser";
    private static final int ID_PROFILE = 2;

    private int idUser;

    // TODO: Rename and change types of parameters
    private TextView dietetic_profile2_text;
    private TextView lastUpdateDateProfile2;
    private DBManager dbManager;
    private DieteticProfile dieteticProfile;

    private OnFragmentInteractionListener mListener;

    public DieteticProfile2Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DieteticProfile2Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DieteticProfile2Fragment newInstance(String param1, String param2) {
        DieteticProfile2Fragment fragment = new DieteticProfile2Fragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = null;
          if (getArguments() != null) {
            Log.d("DieteticProfileFragment", " getArguments() 2 " +  getArguments().getInt(ID_USER));
            idUser = getArguments().getInt(ID_USER);
            dbManager = DBManager.getInstance(getActivity());
            dieteticProfile = dbManager.getDieteticProfile(idUser, ID_PROFILE);

            if (dieteticProfile != null  ){

                Log.d("DieteticProfileFragment", "dentro del IF 2 " + dieteticProfile.getUser_id());

                rootView = inflater.inflate(R.layout.fragment_dietetic_profile2, container, false);


                dietetic_profile2_text = (TextView) rootView.findViewById(R.id.dietetic_profile2_text);
                dietetic_profile2_text.setText("Dietetic profile nº2");

                lastUpdateDateProfile2 = (TextView) rootView.findViewById(R.id.last_update_date_profile2);
                lastUpdateDateProfile2.setText("Fecha de la última actualización: "+ " PONER FECHA PROFILE 2");
            } else {
                Log.d("DieteticProfileFragment", "dentro del ELSE 2 INTERNO");

                rootView = inflater.inflate(R.layout.fragment_new_profile_dietetic, container, false);
            }
        } else {
            Log.d("DieteticProfileFragment", "dentro del ELSE 2 ");

            rootView = inflater.inflate(R.layout.fragment_new_profile_dietetic, container, false);
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
}
