package com.dyetica.app.fragments;

import android.content.Context;
import android.content.Intent;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dyetica.app.CreateNewDieteticProfile;
import com.dyetica.app.R;
import com.dyetica.app.model.DieteticProfile;
import com.dyetica.app.model.ExtensionsBalancerPlus;
import com.dyetica.app.persistence.DBManager;
import com.dyetica.app.utils.MethodsUtil;

import java.text.DecimalFormat;

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
    private static final String PROFILE = "PROFILE";
    private static final String ID_EXTENSIONS_PROFILE = "idExtensionsProfile";
    private static final String ID_EXTENSIONS_BALANCER_PLUS = "idExtensionsBalancerPlus";

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
    private Button updateProfileDietetic;
    private DBManager dbManager;
    private EditText breakfast;
    private EditText lunch;
    private EditText food;
    private EditText snack;
    private EditText dinner;
    private LinearLayout linearCreateContainer;

    private OnFragmentInteractionListener mListener;
    private DieteticProfile dieteticProfile;
    private ExtensionsBalancerPlus extensionsBalancerPlus;

    private long idExtensionsProfile;
    private long idExtensionsBalancerPlus;

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
            idExtensionsProfile =  getArguments().getLong(ID_EXTENSIONS_PROFILE);
            idExtensionsBalancerPlus = getArguments().getLong(ID_EXTENSIONS_BALANCER_PLUS);
        }
      }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dietetic_profile2, container, false);

        linearCreateContainer = (LinearLayout) rootView.findViewById(R.id.layoutCreateContainer);
        Log.d("DieteticProfileFragment", "DENTRO DE  ONCREATEVIEW 2: " );
        createProfileDietetic = (Button) rootView.findViewById(R.id.button_profile_2_dietetic_new);
        updateProfileDietetic = (Button) rootView.findViewById(R.id.button_profile_dietetic_2_update);

        if (getArguments() != null) {
            Log.d("DieteticProfileFragment", " getArguments() ONCREATEVIEW 2 " +  getArguments().getInt(ID_USER));
            idUser = getArguments().getInt(ID_USER);
            dbManager = DBManager.getInstance(getActivity());
            dieteticProfile = dbManager.getDieteticProfile(idUser, ID_PROFILE);
            extensionsBalancerPlus =  dbManager.getExtensionsBalancerPlus(idExtensionsBalancerPlus);
            DecimalFormat df = new DecimalFormat("###.##");
            df.setDecimalFormatSymbols(MethodsUtil.setDecimalSeparator());

            if (dieteticProfile != null  ){
                linearCreateContainer.setVisibility(View.INVISIBLE);
                ViewGroup.LayoutParams params = linearCreateContainer.getLayoutParams();
                params.height = 0;
                linearCreateContainer.setLayoutParams(params);

                Log.d("DieteticProfileFragment", "dentro del IF 2 " + dieteticProfile.getUser_id());

                title1 = (TextView) rootView.findViewById(R.id.dietetic_profile_2_text_1);
                title2 = (TextView) rootView.findViewById(R.id.dietetic_profile_2_text_2);
                title1.setText(getString(R.string.text_1_dietetic_profile));
                title2.setText(getString(R.string.text_2_dietetic_profile));
                lastUpdateDate = (TextView) rootView.findViewById(R.id.last_update_date_profile_2);
                lastUpdateDate.setText(getString(R.string.date_dietetic_profile) + " " + dieteticProfile.getActualiza());
                createProfileDietetic.setVisibility(View.INVISIBLE);
                kcal = (EditText) rootView.findViewById(R.id.kcal_profile_2);
                cGrasa = (EditText) rootView.findViewById(R.id.c_grasa_profile_2);
                kcalHint = (TextInputLayout) rootView.findViewById(R.id.kcal_hint_profile_2);
                cGrasaHint = (TextInputLayout) rootView.findViewById(R.id.c_grasa_hint_profile_2);
                kcalHint.setVisibility(View.VISIBLE);
                cGrasaHint.setVisibility(View.VISIBLE);
                lastUpdateDate.setVisibility(View.VISIBLE);
                kcal.setEnabled(false);
                kcal.setFocusable(false);
                cGrasa.setEnabled(false);
                cGrasa.setFocusable(false);

                breakfast = (EditText) rootView.findViewById(R.id.breakfast_profile_2);
                lunch = (EditText) rootView.findViewById(R.id.lunch_profile_2);
                food = (EditText) rootView.findViewById(R.id.food_profile_2);
                snack = (EditText) rootView.findViewById(R.id.snack_profile_2);
                dinner = (EditText) rootView.findViewById(R.id.dinner_profile_2);

                breakfast.setFocusable(false);
                lunch.setFocusable(false);
                food.setFocusable(false);
                snack.setFocusable(false);
                dinner.setFocusable(false);

                breakfast.setText(df.format(dieteticProfile.getKcaldia() * (extensionsBalancerPlus.getDesayuno_necesidades()/100)) + " Kcal (20%)");
                lunch.setText(df.format(dieteticProfile.getKcaldia() * (extensionsBalancerPlus.getAlmuerzo_necesidades()/100)) + " Kcal (5%)");
                food.setText(df.format(dieteticProfile.getKcaldia() * (extensionsBalancerPlus.getComida_necesidades()/100)) + " Kcal (40%)");
                snack.setText(df.format(dieteticProfile.getKcaldia() * (extensionsBalancerPlus.getMerienda_necesidades()/100)) + " Kcal (5%)");
                dinner.setText(df.format(dieteticProfile.getKcaldia() * (extensionsBalancerPlus.getCena_necesidades()/100)) + " Kcal (30%)");

                kcal.setText(df.format(dieteticProfile.getKcaldia()));
                cGrasa.setText(df.format(dieteticProfile.getCg()));

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

                createProfileDietetic.setVisibility(View.INVISIBLE);
                updateProfileDietetic.setVisibility(View.VISIBLE);

                updateProfileDietetic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(view.getContext(), CreateNewDieteticProfile.class);
                        intent.putExtra(ID_USER, idUser);
                        intent.putExtra(PROFILE, ID_PROFILE);
                        startActivity(intent);
                        getActivity().finish();
                    }
                });

            } else {
                Log.d("DieteticProfileFragment", "dentro del ELSE 2" );
                linearCreateContainer.setVisibility(View.VISIBLE);

                titleNew = (TextView) rootView.findViewById(R.id.dietetic_profile_2_title_new);
                titleNew.setVisibility(View.VISIBLE);
                contentNew = (TextView) rootView.findViewById(R.id.dietetic_profile_2_new_content);
                contentNew.setVisibility(View.VISIBLE);
                createProfileDietetic.setVisibility(View.VISIBLE);
                updateProfileDietetic.setVisibility(View.INVISIBLE);
                titleNew.setText(getString(R.string.title_new_dietetic_profile));
                contentNew.setText(getString(R.string.content_new_dietetic_profile));

                createProfileDietetic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(view.getContext(), CreateNewDieteticProfile.class);
                        intent.putExtra(ID_USER, idUser);
                        intent.putExtra(PROFILE, ID_PROFILE);
                        startActivity(intent);
                        getActivity().finish();
                    }
                });
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
