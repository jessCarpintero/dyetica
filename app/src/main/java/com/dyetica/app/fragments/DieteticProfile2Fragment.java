package com.dyetica.app.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

    // TODO: Rename and change types of parameters  private TextView title;
    private TextInputLayout kcalHint;
    private TextInputLayout cGrasaHint;
    private TextInputLayout breakfastHint;
    private TextInputLayout lunchHint;
    private TextInputLayout foodHint;
    private TextInputLayout snackHint;
    private TextInputLayout dinnerHint;
    private TextView title1;
    private TextView title2;
    private EditText kcal;
    private EditText cGrasa;
    private TextView titleNew;
    private TextView lastUpdateDate;
    private TextView contentNew;
    private Button createProfileDietetic;
    private DBManager dbManager;
    private DieteticProfile dieteticProfile;
    private EditText breakfast;
    private EditText lunch;
    private EditText food;
    private EditText snack;
    private EditText dinner;

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

        Log.d("DieteticProfileFragment", "DENTRO DE  ONCREATE 2: " );

        if (getArguments() != null) {
            Log.d("DieteticProfileFragment", " getArguments() ONCREATE 2 " + getArguments().getInt(ID_USER));
            idUser = getArguments().getInt(ID_USER);
        }
      }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dietetic_profile2, container, false);


        Log.d("DieteticProfileFragment", "DENTRO DE  ONCREATEVIEW 2: " );
        createProfileDietetic = (Button) rootView.findViewById(R.id.button_profile_2_dietetic_new);



        if (getArguments() != null) {
            Log.d("DieteticProfileFragment", " getArguments() ONCREATEVIEW 2 " +  getArguments().getInt(ID_USER));
            idUser = getArguments().getInt(ID_USER);
            dbManager = DBManager.getInstance(getActivity());
            dieteticProfile = dbManager.getDieteticProfile(idUser, ID_PROFILE);

            if (dieteticProfile != null  ){

                Log.d("DieteticProfileFragment", "dentro del IF 2 " + dieteticProfile.getUser_id());

                title1 = (TextView) rootView.findViewById(R.id.dietetic_profile_2_text_1);
                title2 = (TextView) rootView.findViewById(R.id.dietetic_profile_2_text_2);
                title1.setText(getString(R.string.text_1_dietetic_profile));
                title2.setText(getString(R.string.text_2_dietetic_profile));
                lastUpdateDate = (TextView) rootView.findViewById(R.id.last_update_date_profile_2);
                lastUpdateDate.setText(getString(R.string.date_dietetic_profile) + dieteticProfile.getActualiza());
                createProfileDietetic.setVisibility(View.INVISIBLE);
                kcal = (EditText) rootView.findViewById(R.id.kcal_profile_2);
                cGrasa = (EditText) rootView.findViewById(R.id.c_grasa_profile_2);
                kcalHint = (TextInputLayout) rootView.findViewById(R.id.kcal_hint_profile_2);
                cGrasaHint = (TextInputLayout) rootView.findViewById(R.id.c_grasa_hint_profile_2);
                kcal.setText(String.valueOf(dieteticProfile.getKcaldia()));
                cGrasa.setText(String.valueOf(dieteticProfile.getCg()));
                kcalHint.setVisibility(View.VISIBLE);
                cGrasaHint.setVisibility(View.VISIBLE);
                kcal.setEnabled(false);
                cGrasa.setEnabled(false);


                breakfast = (EditText) rootView.findViewById(R.id.breakfast_profile_2);
                lunch = (EditText) rootView.findViewById(R.id.lunch_profile_2);
                food = (EditText) rootView.findViewById(R.id.food_profile_2);
                snack = (EditText) rootView.findViewById(R.id.snack_profile_2);
                dinner = (EditText) rootView.findViewById(R.id.dinner_profile_2);

                breakfast.setText(String.valueOf(dieteticProfile.getKcaldia() * 0.2) + " (20%)");
                lunch.setText(String.valueOf(dieteticProfile.getKcaldia() * 0.05) + " (5%)");
                food.setText(String.valueOf(dieteticProfile.getKcaldia() * 0.4) + " (40%)");
                snack.setText(String.valueOf(dieteticProfile.getKcaldia() * 0.05) + " (5%)");
                dinner.setText(String.valueOf(dieteticProfile.getKcaldia() * 0.3) + " (30%)");


                breakfast.setEnabled(false);
                lunch.setEnabled(false);
                food.setEnabled(false);
                snack.setEnabled(false);
                dinner.setEnabled(false);

                breakfastHint = (TextInputLayout) rootView.findViewById(R.id.breakfast_hint_profile_2);
                lunchHint = (TextInputLayout) rootView.findViewById(R.id.lunch_hint_profile_2);
                foodHint = (TextInputLayout) rootView.findViewById(R.id.food_hint_profile_2);
                snackHint = (TextInputLayout) rootView.findViewById(R.id.snack_hint_profile_2);
                dinnerHint = (TextInputLayout) rootView.findViewById(R.id.dinner_hint_profile_2);


                breakfastHint.setVisibility(View.VISIBLE);
                lunchHint.setVisibility(View.VISIBLE);
                foodHint.setVisibility(View.VISIBLE);
                snackHint.setVisibility(View.VISIBLE);
                dinnerHint.setVisibility(View.VISIBLE);

            } else {
                Log.d("DieteticProfileFragment", "dentro del ELSE 2" );

                titleNew = (TextView) rootView.findViewById(R.id.dietetic_profile_2_title_new);
                contentNew = (TextView) rootView.findViewById(R.id.dietetic_profile_2_new_content);
                createProfileDietetic.setVisibility(View.VISIBLE);
                titleNew.setText(getString(R.string.title_new_dietetic_profile));
                contentNew.setText(getString(R.string.content_new_dietetic_profile));
            }
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
