package com.dyetica.app.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dyetica.app.R;
import com.dyetica.app.model.User;
import com.dyetica.app.persistence.ClientHTTP;
import com.dyetica.app.persistence.DBManager;
import com.dyetica.app.utils.MethodsUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    private static final String ID_USER = "idUser";

    private EditText mNameView;
    private EditText mSurname1View;
    private EditText mSurname2View;
    private EditText mBirthayDayView;
    private EditText mPostalCodeView;
    private EditText mUserNameView;
    private EditText mEmailView;
    private Button mUpdateButton;
    private int idUser;

    private DBManager dbManager;
    //private SharedPreferences prefs;
    private User userCurrent;
    private OnFragmentInteractionListener mListener;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            idUser = getArguments().getInt(ID_USER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        dbManager = DBManager.getInstance(getActivity());
        userCurrent = User.getInstance(dbManager.getUser(idUser));

        mNameView = (EditText) rootView.findViewById(R.id.editTextName);
        mSurname1View = (EditText) rootView.findViewById(R.id.editTextSurname1);
        mSurname2View = (EditText) rootView.findViewById(R.id.editTextSurname2);
        mBirthayDayView = (EditText) rootView.findViewById(R.id.editTextbirthDay);
        mPostalCodeView = (EditText) rootView.findViewById(R.id.editTextPostalCode);
        mUserNameView = (EditText) rootView.findViewById(R.id.editTextUsername);
        mUserNameView.setEnabled(false);
        mEmailView = (EditText) rootView.findViewById(R.id.editTextEmail);
        mEmailView.setEnabled(false);

        fillFields();

        mUpdateButton = (Button) rootView.findViewById(R.id.buttonProfile);
        mUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requiredFields();
            }
        });
        


        return rootView;
    }

    private User generateUser() {
        User user = null;
        user = User.getInstance(user);
        user.setId(idUser);
        user.setUsername(mUserNameView.getText().toString());
        user.setDateBirthday(mBirthayDayView.getText().toString());
        user.setPostalCode(mPostalCodeView.getText().toString());
        user.setEmail(mEmailView.getText().toString());
        user.setFirstSurname(mSurname1View.getText().toString());
        user.setSecondSurname(mSurname2View.getText().toString());
        user.setName(mNameView.getText().toString());
        user.setRegisterDate(userCurrent.getRegisterDate());
        user.setLastvisitDate(userCurrent.getLastvisitDate());
        user.setApiKey(userCurrent.getApiKey());
        Log.d("ProfileFragment","Valor de userUpdate: " + user);
        return user;
    }

    private void saveDataDB() {
        Log.d("ProfileFragment","Valor de userCurrent: " + userCurrent);
        User userUpdate =  generateUser();
        try {
            if (!userCurrent.equals(userUpdate)) {
                Log.d("ProfileFragment","userUpdate es distinto de userCurrent");
                if (dbManager.updateUser(userUpdate)) {
                    Log.d("ProfileFragment","userUpdate MODIFCADO OK");
                    AttemptProfile attemptProfile = new AttemptProfile(userUpdate.getName(), userUpdate.getFirstSurname(),
                            userUpdate.getSecondSurname(), userUpdate.getDateBirthday(), userUpdate.getPostalCode());
                    Toast toast = Toast.makeText(getContext(), R.string.update_successful, Toast.LENGTH_SHORT);
                    toast.show();
                    if (attemptProfile.execute((Void) null).get()){
                        Log.d("ProfileFragment","userUpdate MODIFCADO OK");
                        toast = Toast.makeText(getContext(), R.string.error_sync_server, Toast.LENGTH_SHORT);
                        toast.show();
                    }
                } else {
                    Log.d("ProfileFragment","userUpdate MODIFCADO KO");

                    Toast toast = Toast.makeText(getContext(), R.string.update_profile_error, Toast.LENGTH_SHORT);
                    toast.show();
                }
                Log.d("ProfileFragment","Valor de USER DESPUES DE MODIFICADO: " + dbManager.getUser(idUser));
            }
        } catch (InterruptedException e) {
            Log.e("ProfileFragment", "Interrupted AttempProfile: " + e.getMessage());
        } catch (ExecutionException e) {
            Log.e("ProfileFragment", "Execution AttempProfile: " + e.getMessage());
        }
    }

    private void requiredFields() {
        boolean error = true;

        // Reset errors.
        mNameView.setError(null);
        mSurname1View.setError(null);
        mSurname2View.setError(null);
        mBirthayDayView.setError(null);
        mPostalCodeView.setError(null);

        // Store values at the time of the login attempt.
        String nameView = mNameView.getText().toString().trim();
        String surname1View = mSurname1View.getText().toString().trim();
        String surname2View = mSurname2View.getText().toString().trim();
        String birthayDayView = mBirthayDayView.getText().toString().trim();
        String postalCodeView = mPostalCodeView.getText().toString().trim();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(nameView)) {
            mNameView.setError(getString(R.string.error_field_required));
            focusView = mNameView;
            cancel = true;
        }

        if (TextUtils.isEmpty(surname1View)) {
            mSurname1View.setError(getString(R.string.error_field_required));
            focusView = mSurname1View;
            cancel = true;
        }

        if (TextUtils.isEmpty(surname2View)) {
            mSurname2View.setError(getString(R.string.error_field_required));
            focusView = mSurname2View;
            cancel = true;
        }

        if (TextUtils.isEmpty(birthayDayView)) {
            mBirthayDayView.setError(getString(R.string.error_field_required));
            focusView = mBirthayDayView;
            cancel = true;
        } else  if (!isBirthayDayValid(birthayDayView)){
            mBirthayDayView.setError(getString(R.string.error_incorrect_date));
            focusView = mBirthayDayView;
            cancel = true;
        }

        if (TextUtils.isEmpty(postalCodeView)) {
            mPostalCodeView.setError(getString(R.string.error_field_required));
            focusView = mPostalCodeView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't required Fields and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            saveDataDB();
        }
    }


    private boolean isBirthayDayValid(String birthayDay) {
        return MethodsUtil.convertBrithayDayToTimeStamp(birthayDay);
    }

    private void fillFields() {
        Log.d("ProfileFragment", "Valor de User de BD local: " + userCurrent.toString());
        mNameView.setText(userCurrent.getName());
        mSurname1View.setText(userCurrent.getFirstSurname());
        mSurname2View.setText(userCurrent.getSecondSurname());
        mBirthayDayView.setText(userCurrent.getDateBirthday());
        mPostalCodeView.setText(userCurrent.getPostalCode());
        mUserNameView.setText(userCurrent.getUsername());
        mEmailView.setText(userCurrent.getEmail());
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

    class AttemptProfile extends AsyncTask<Void, Void, Boolean> {
        private final String mName, mFirstSurname, mSecondSurname, mDateBirthday, mPostalCode;
        private String statusCode;
        private boolean error;
        private StringBuilder params = new StringBuilder();

        AttemptProfile(String name, String firstSurname, String secondSurname, String dateBirthday, String postalCode) {
            mName = name;
            mFirstSurname = firstSurname;
            mSecondSurname = secondSurname;
            mDateBirthday = dateBirthday;
            mPostalCode = postalCode;
        }


        @Override
        protected Boolean doInBackground(Void... args) {
            SharedPreferences prefs = getActivity().getSharedPreferences("myPreferences", Context.MODE_PRIVATE);

            try {
                // Building Parameters
                params.append("name=").append(mName).append("&firstsurname=").append(mFirstSurname)
                        .append("&secondsurname=").append(mSecondSurname)
                        .append("&datebirthday=").append(mDateBirthday)
                        .append("&postalcode=").append(mPostalCode);

                Log.d("ProfileFragment", "Valor de params PROFILE: " +  params.toString());

                // getting product details by making HTTP request
                Map<String, String> response = ClientHTTP.makeHttpRequest(new URL(getString(R.string.url_update_profile)), "POST", prefs.getString("apiKey", ""), params.toString());

                // check your log for json response
                Log.d("VERBOSE", response.toString());
                statusCode = response.get(getString(R.string.status));
                if (!getString(R.string.status_200).equals(statusCode)){
                    error = true;
                } else {
                    error = false;
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return error;
        }
    }
}
