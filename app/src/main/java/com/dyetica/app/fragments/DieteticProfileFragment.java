package com.dyetica.app.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dyetica.app.R;
import com.dyetica.app.model.DieteticProfile;
import com.dyetica.app.persistence.DBManager;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DieteticProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DieteticProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DieteticProfileFragment extends Fragment {
    private static final String ID_USER = "idUser";
    private static final int ID_PROFILE = 1;

    private int idUser;
    private TextView title;
    private TextView titleNew;
    private TextView lastUpdateDate;
    private TextView contentNew;
    private Button createProfileDietetic;

    private DBManager dbManager;
    private DieteticProfile dieteticProfile;

    private OnFragmentInteractionListener mListener;

    public DieteticProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DieteticProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DieteticProfileFragment newInstance(String param1, String param2) {
        DieteticProfileFragment fragment = new DieteticProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("DieteticProfileFragment", "DENTRO DE  ONCREATE: " );

        if (getArguments() != null) {
            Log.d("DieteticProfileFragment", " getArguments() ONCREATE " + getArguments().getInt(ID_USER));
            idUser = getArguments().getInt(ID_USER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dietetic_profile, container, false);


        Log.d("DieteticProfileFragment", "DENTRO DE  ONCREATEVIEW: " );

        title = (TextView) rootView.findViewById(R.id.dietetic_profile_text);
        lastUpdateDate = (TextView) rootView.findViewById(R.id.last_update_date);
        titleNew = (TextView) rootView.findViewById(R.id.dietetic_profile_title_new);
        contentNew = (TextView) rootView.findViewById(R.id.dietetic_profile_title_new);
        createProfileDietetic = (Button) rootView.findViewById(R.id.button_profile_dietetic_new);



        if (getArguments() != null) {
            Log.d("DieteticProfileFragment", " getArguments() ONCREATEVIEW " +  getArguments().getInt(ID_USER));
            idUser = getArguments().getInt(ID_USER);
            dbManager = DBManager.getInstance(getActivity());
            dieteticProfile = dbManager.getDieteticProfile(idUser, ID_PROFILE);

            Log.d("DieteticProfileFragment", "Valor de getDieteticProfile ONCREATE: " + dieteticProfile.getUser_id());


            if (dieteticProfile != null  ){

                Log.d("DieteticProfileFragment", "dentro del IF " + dieteticProfile.getUser_id());


                title.setText("Dietetic profile");

                lastUpdateDate.setText("Fecha de la última actualización: "+ " PONER FECHA");

                lastUpdateDate.setVisibility(View.INVISIBLE);

                disableComponentsProfileDietetic();

            }
        } else {

            titleNew.setText("Dietetic profile NEW");

            contentNew.setText("Crear perfil NEW");


            enableComponentsProfileDietetic();

        }
        return rootView;
    }

    private void disableComponentsProfileDietetic() {
        titleNew.setVisibility(View.INVISIBLE);
        contentNew.setVisibility(View.INVISIBLE);
        createProfileDietetic.setVisibility(View.INVISIBLE);
        title.setVisibility(View.VISIBLE);
        lastUpdateDate.setVisibility(View.VISIBLE);
    }

    private void enableComponentsProfileDietetic() {
        titleNew.setVisibility(View.VISIBLE);
        contentNew.setVisibility(View.VISIBLE);
        createProfileDietetic.setVisibility(View.VISIBLE);
        title.setVisibility(View.INVISIBLE);
        lastUpdateDate.setVisibility(View.INVISIBLE);
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
