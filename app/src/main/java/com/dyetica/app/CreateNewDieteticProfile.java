package com.dyetica.app;

import android.content.Intent;
import android.icu.text.StringPrepParseException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.dyetica.app.model.DieteticProfile;
import com.dyetica.app.model.User;
import com.dyetica.app.persistence.DBManager;
import com.dyetica.app.utils.MethodsUtil;

import java.util.ArrayList;
import java.util.List;

public class CreateNewDieteticProfile extends AppCompatActivity {
    private static final String ID_USER = "idUser";
    private static final String SEX = "SEXO";
    private static final String COMPLEXION = "COMPLEXION";
    private static final String ACTIVITY = "ACTIVITY";
    private static final String STAGE = "STAGE";
    private static final String HEIGHT = "HEIGHT";
    private static final String WEIGHT = "WEIGHT";
    private static final String NAME_PROFILE = "NAME_PROFILE";
    private static final String BIRTHDAY = "BIRTHDAY";
    private static final String PROFILE = "PROFILE";

    private EditText mNameView;
    private EditText mBirthayDayView;

    private DBManager dbManager;
    private User user;
    private Toolbar toolbar;
    private Spinner heightSpinner;
    private Spinner weightSpinner;
    private Spinner sexSpinner;
    private Spinner complexionSpinner;
    private Spinner activitySpinner;
    private Spinner stageSpinner;
    private Button createProfileDietetic;
    private DieteticProfile dieteticProfile;
    private int idProfiñile, idUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_dietetic_profile);
        toolbar = (Toolbar) findViewById(R.id.toolbarCreate);
        setSupportActionBar(toolbar);
        dieteticProfile =  new DieteticProfile();

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_menu_send));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        //TODO no lo muestra
        toolbar.setTitle("Prueba");

        Intent intent = getIntent();
        idUser = intent.getIntExtra(ID_USER, 0);
        idProfiñile = intent.getIntExtra(PROFILE, 1);

        Log.d("CreateNewDiete", "Valor de idUser: " + idUser);

        dbManager = DBManager.getInstance(this);
        user = User.getInstance(dbManager.getUser(idUser));

        mNameView = (EditText) findViewById(R.id.editTextNameProfile);
        mBirthayDayView = (EditText) findViewById(R.id.editTextbirthDay);

        heightSpinner = (Spinner)findViewById(R.id.spinner_height);
        heightSpinner.setAdapter(initSppinerAdapter(100, 260));

        weightSpinner = (Spinner)findViewById(R.id.spinner_weight);
        weightSpinner.setAdapter(initSppinerAdapter(10, 300));

        sexSpinner = (Spinner)findViewById(R.id.spinner_sex);
        complexionSpinner = (Spinner)findViewById(R.id.spinner_complexion);

        activitySpinner = (Spinner)findViewById(R.id.spinner_physical_activity);

        stageSpinner = (Spinner)findViewById(R.id.spinner_stage);

        createProfileDietetic = (Button) findViewById(R.id.button_next);

        createProfileDietetic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requiredFields();
            }
        });

    }

    private void saveDieteticProfile() {
        dieteticProfile.setTalla(Integer.parseInt(heightSpinner.getSelectedItem().toString()));
        dieteticProfile.setPeso(Integer.parseInt(weightSpinner.getSelectedItem().toString()));
        dieteticProfile.setSexo(("Hombre".equals(sexSpinner.getSelectedItem().toString()))? 1:2);

        switch (complexionSpinner.getSelectedItem().toString()){
            case "Delgada":
                dieteticProfile.setConstitucion(0);
                break;
            case "Media":
                dieteticProfile.setConstitucion(1);
                break;
            case "Robusta":
                dieteticProfile.setConstitucion(2);
                break;
        }

        switch (activitySpinner.getSelectedItem().toString()){
            case "Basal":
                dieteticProfile.setActividad(0);
                break;
            case "Sedentaria":
                dieteticProfile.setActividad(1);
                break;
            case "Baja":
                dieteticProfile.setActividad(2);
                break;
            case "Media":
                dieteticProfile.setActividad(3);
                break;
            case "Alta":
                dieteticProfile.setActividad(4);
                break;
            case "Muy alta":
                dieteticProfile.setActividad(5);
                break;
        }

        switch (stageSpinner.getSelectedItem().toString()){
            case "S/Etapa":
                dieteticProfile.setEtapa(0);
                break;
            case "1":
                dieteticProfile.setEtapa(1);
                break;
            case "2":
                dieteticProfile.setEtapa(2);
                break;
        }

    }

    public ArrayAdapter initSppinerAdapter(int ini, int fin){
        List list = new ArrayList();

        int listSize = 0;
        for (int i = ini; i <= fin; i++){
            list.add(i);
            listSize++;
        }

        ArrayAdapter adapterSppiner = new ArrayAdapter(this,
                android.R.layout.simple_spinner_dropdown_item, list);

        return adapterSppiner;
    }

    private void requiredFields() {
        boolean error = true;

        // Reset errors.
        mNameView.setError(null);
        mBirthayDayView.setError(null);

        // Store values at the time of the login attempt.
        String nameProfile = mNameView.getText().toString().trim();
        String birthayDayView = mBirthayDayView.getText().toString().trim();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(nameProfile)) {
            mNameView.setError(getString(R.string.error_field_required));
            focusView = mNameView;
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

        if (cancel) {
            // There was an error; don't required Fields and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            dieteticProfile.setF_nac(birthayDayView);
            dieteticProfile.setNombre(nameProfile);
            saveDieteticProfile();
            Intent intent = new Intent(getApplicationContext(), CreateNewDieteticProfile2Activity.class);
            intent.putExtra(SEX, dieteticProfile.getSexo());
            intent.putExtra(COMPLEXION, dieteticProfile.getConstitucion());
            intent.putExtra(ACTIVITY, dieteticProfile.getActividad());
            intent.putExtra(STAGE, dieteticProfile.getEtapa());
            intent.putExtra(HEIGHT, dieteticProfile.getTalla());
            intent.putExtra(WEIGHT, dieteticProfile.getPeso());
            intent.putExtra(BIRTHDAY, dieteticProfile.getF_nac());
            intent.putExtra(NAME_PROFILE, dieteticProfile.getNombre());
            intent.putExtra(PROFILE, idProfiñile);
            intent.putExtra(ID_USER, idUser);
            startActivity(intent);
        }
    }

    private boolean isBirthayDayValid(String birthayDay) {
        return MethodsUtil.convertBrithayDayToTimeStamp(birthayDay);
    }

}
