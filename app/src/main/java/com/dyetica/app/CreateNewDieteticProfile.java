package com.dyetica.app;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.icu.text.StringPrepParseException;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.dyetica.app.model.DieteticProfile;
import com.dyetica.app.model.ExtensionsProfile;
import com.dyetica.app.model.User;
import com.dyetica.app.persistence.ClientHTTP;
import com.dyetica.app.persistence.DBManager;
import com.dyetica.app.utils.MethodsUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class CreateNewDieteticProfile extends AppCompatActivity {
    private static final String ID_USER = "idUser";
    private static final String PROFILE = "PROFILE";
    private static final String ID_EXTENSIONS_PROFILE = "idExtensionsProfile";

    private EditText mNameView;
    private EditText mBirthayDayView;
    private EditText mKcalDayView;
    private EditText mCGView;

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
    private int idProfiile, idUser;
    private long idExtensionsProfile;
    private EditText mRate;
    private Spinner objectiveSpinner;
    private RadioGroup mQuestion1;
    private RadioGroup mQuestion2;
    private RadioGroup mQuestion3;
    private ExtensionsProfile extensionsProfile;
    private AttemptIdealWeight mAuthTaskIdelWeight;
    private int mIdealWeight;
    private float mkcalAdjusted;
    private float mcgAdjusted;
    private Button mCalculate;
    private DecimalFormat df;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_dietetic_profile);
        toolbar = (Toolbar) findViewById(R.id.toolbarCreate);
        createProfileDietetic = (Button) findViewById(R.id.button_create_profile_dietetic);
        mCalculate = (Button) findViewById(R.id.button_calculate);
        setSupportActionBar(toolbar);
        dieteticProfile =  new DieteticProfile();
        df = new DecimalFormat("###.##");
        df.setDecimalFormatSymbols(MethodsUtil.setDecimalSeparator());

        Intent intent = getIntent();
        idUser = intent.getIntExtra(ID_USER, 0);
        idProfiile = intent.getIntExtra(PROFILE, 1);
        idExtensionsProfile = intent.getLongExtra(ID_EXTENSIONS_PROFILE, 0);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MainActivity.class);
                intent.putExtra(PROFILE, idProfiile);
                finish();
                startActivity(intent);
            }
        });
        dbManager = DBManager.getInstance(this);
        user = User.getInstance(dbManager.getUser(idUser));
        dieteticProfile = dbManager.getDieteticProfile(idUser, idProfiile);

        extensionsProfile = dbManager.getExtensionsProfile(idExtensionsProfile);

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

        mRate = (EditText) findViewById(R.id.editTextRate);

        objectiveSpinner = (Spinner)findViewById(R.id.spinner_objective);

        mQuestion1 = (RadioGroup) findViewById(R.id.radio_question1);
        mQuestion2 = (RadioGroup) findViewById(R.id.radio_question2);
        mQuestion3 = (RadioGroup) findViewById(R.id.radio_question3);

        mKcalDayView = (EditText) findViewById(R.id.editTextKcal);
        mKcalDayView.setEnabled(false);
        mCGView = (EditText) findViewById(R.id.editTextCG);
        mCGView.setEnabled(false);

        if( null != dieteticProfile){
            loadDieteticProfile();
            getSupportActionBar().setTitle(getString(R.string.edit_dietetic_profile_name));
            createProfileDietetic.setText(getString(R.string.edit_profile_dietetic));
        } else {
            getSupportActionBar().setTitle(getString(R.string.create_new_dietetic_profile_name));
            createProfileDietetic.setText(getString(R.string.create_profile_dietetic));
            dieteticProfile = new DieteticProfile();
        }

        createProfileDietetic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!requiredFields(false)) {
                    Intent i = new Intent(CreateNewDieteticProfile.this, MainActivity.class);
                    i.putExtra(PROFILE, idProfiile);
                    finish();
                    startActivity(i);
                }
            }
        });

        mCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requiredFields(true);
            }
        });
    }

    private float getCorrectionFactor(int actividad) {
        return dbManager.getTypeActivity(actividad).getFactor();
    }

    private boolean attemptIdealWeight() {
        Map<String, String> success = new HashMap<>();
        boolean error;
        mAuthTaskIdelWeight = new AttemptIdealWeight(String.valueOf(dieteticProfile.getSexo()),String.valueOf(dieteticProfile.getConstitucion()),String.valueOf(dieteticProfile.getTalla()));
        try {
            success = mAuthTaskIdelWeight.execute((Void) null).get();
        } catch (InterruptedException e) {
            Log.e("LoginActivity", "Interrupted attemptExtensionsProfile");
        } catch (ExecutionException e) {
            Log.e("LoginActivity", "Error execution in attemptExtensionsProfile");
        }
        error = Boolean.parseBoolean(success.get(getString(R.string.error)));
        if (!error) {
            mIdealWeight = Integer.parseInt(success.get(getString(R.string.message)));
        } else {
            Toast.makeText(this, success.get(getString(R.string.message)), Toast.LENGTH_LONG).show();
        }
        return error;
    }

    private float kcalTheoretical() {
        if (1 == dieteticProfile.getSexo()) {
            return Math.round((66.5 + (13.8 * mIdealWeight) + (5 * dieteticProfile.getTalla()) - (6.8 * getAgeUser(dieteticProfile.getF_nac()))) * getCorrectionFactor(dieteticProfile.getActividad()));
        } else {
            return Math.round((655.1 + (9.6 * mIdealWeight) + (1.9 * dieteticProfile.getTalla()) - (4.7 * getAgeUser(dieteticProfile.getF_nac()))) * getCorrectionFactor(dieteticProfile.getActividad()));
        }
    }

    private int getAgeUser(String birthdateString) {
        Calendar birthdate = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        try {
            birthdate.setTime(sdf.parse(birthdateString));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar today = Calendar.getInstance();
        int diffYear = today.get(Calendar.YEAR) - birthdate.get(Calendar.YEAR);
        int diffMonth = today.get(Calendar.MONTH) - birthdate.get(Calendar.MONTH);
        int diffDay = today.get(Calendar.DAY_OF_MONTH) - birthdate.get(Calendar.DAY_OF_MONTH);
        if (diffMonth < 0 || (diffMonth == 0 && diffDay < 0)) {
           diffYear = diffYear - 1;
        }
        return diffYear;
    }

    private void regulateParams() {
        float kcalTheoretical = kcalTheoretical();
        switch (dieteticProfile.getObjetivo()){
            case 1:
                mkcalAdjusted = Math.round(kcalTheoretical - (dieteticProfile.getRitmo() * extensionsProfile.getFormulas_gr_sem_kilo()));
                mcgAdjusted = Float.parseFloat(df.format(extensionsProfile.getFormulas_cg_base()-((extensionsProfile.getFormulas_cg_varible1()*(dieteticProfile.getRitmo()*extensionsProfile.getFormulas_gr_sem_kilo())/(extensionsProfile.getFormulas_cg_varible2()*kcalTheoretical)))));
                break;
            case 2:
                mkcalAdjusted = Math.round(kcalTheoretical + (dieteticProfile.getRitmo() * extensionsProfile.getFormulas_gr_sem_kilo()));
                mcgAdjusted = Float.parseFloat(df.format(extensionsProfile.getFormulas_cg_base()+((extensionsProfile.getFormulas_cg_varible1()*(dieteticProfile.getRitmo()*extensionsProfile.getFormulas_gr_sem_kilo())/(extensionsProfile.getFormulas_cg_varible2()*kcalTheoretical)))));
                break;
            case 0:
                mkcalAdjusted = kcalTheoretical;
                mcgAdjusted = extensionsProfile.getFormulas_cg_base();
                break;
        }
    }

    private void getResultKcalAndCg(){
        if (mcgAdjusted < extensionsProfile.getFormulas_cg_min() || mcgAdjusted > extensionsProfile.getFormulas_cg_max()){
            Toast.makeText(this, "NO ES FACTIBLE", Toast.LENGTH_LONG).show();
        } else {
            dieteticProfile.setKcaldia(mkcalAdjusted);
            dieteticProfile.setCg(mcgAdjusted);
            mKcalDayView.setText(df.format(dieteticProfile.getKcaldia()));
            mCGView.setText(df.format(dieteticProfile.getCg()));
        }
    }

    private void loadDieteticProfile() {
        mNameView.setText(dieteticProfile.getNombre());
        mBirthayDayView.setText(dieteticProfile.getF_nac());
        mRate.setText(String.valueOf(dieteticProfile.getRitmo()));
        mKcalDayView.setText(String.valueOf(dieteticProfile.getKcaldia()));
        mCGView.setText(String.valueOf(dieteticProfile.getCg()));

        if (dieteticProfile.getSexo() == 1) {
            sexSpinner.setSelection(0);
        } else {
            sexSpinner.setSelection(1);
        }

        switch (dieteticProfile.getConstitucion()){
            case 1:
                complexionSpinner.setSelection(0);
                break;
            case 2:
                complexionSpinner.setSelection(1);
                break;
            case 3:
                complexionSpinner.setSelection(2);
                break;
        }

        switch (dieteticProfile.getActividad()){
            case 0:
                activitySpinner.setSelection(0);
                break;
            case 1:
                activitySpinner.setSelection(1);
                break;
            case 2:
                activitySpinner.setSelection(2);
                break;
            case 3:
                activitySpinner.setSelection(3);
                break;
            case 4:
                activitySpinner.setSelection(4);
                break;
            case 5:
                activitySpinner.setSelection(5);
                break;
        }

        switch (dieteticProfile.getEtapa()){
            case 0:
                stageSpinner.setSelection(0);
                break;
            case 1:
                stageSpinner.setSelection(1);
                break;
            case 2:
                stageSpinner.setSelection(2);
                break;
        }

        heightSpinner.setSelection(dieteticProfile.getTalla() - 100);
        weightSpinner.setSelection(dieteticProfile.getPeso() - 10);


        switch (dieteticProfile.getObjetivo()){
            case 0:
                objectiveSpinner.setSelection(0);
                break;
            case 1:
                objectiveSpinner.setSelection(1);
                break;
            case 2:
                objectiveSpinner.setSelection(2);
                break;
        }

        switch (dieteticProfile.getPregunta1()){
            case 1:
                mQuestion1.check(R.id.radio_pre1_opc1);
                break;
            case 2:
                mQuestion1.check(R.id.radio_pre1_opc2);
                break;
            case 3:
                mQuestion1.check(R.id.radio_pre1_opc3);
                break;
        }

        switch (dieteticProfile.getPregunta2()){
            case 1:
                mQuestion2.check(R.id.radio_pre2_opc1);
                break;
            case 2:
                mQuestion2.check(R.id.radio_pre2_opc2);
                break;
        }

        switch (dieteticProfile.getPregunta3()){
            case 1:
                mQuestion3.check(R.id.radio_pre3_opc1);
                break;
            case 2:
                mQuestion3.check(R.id.radio_pre3_opc2);
                break;
        }
    }

    private void saveDieteticProfile() {
        dieteticProfile.setF_nac(mBirthayDayView.getText().toString());
        dieteticProfile.setNombre(mNameView.getText().toString());
        dieteticProfile.setTalla(Integer.parseInt(heightSpinner.getSelectedItem().toString()));
        dieteticProfile.setPeso(Integer.parseInt(weightSpinner.getSelectedItem().toString()));
        dieteticProfile.setSexo(("Hombre".equals(sexSpinner.getSelectedItem().toString()))? 1:2);
        if (0 == dieteticProfile.getUser_id()) dieteticProfile.setUser_id(user.getId());
        if (0 == dieteticProfile.getPerfil_id()) dieteticProfile.setPerfil_id(idProfiile);

        if (!TextUtils.isEmpty(mRate.getText()))
            dieteticProfile.setRitmo(Float.parseFloat(mRate.getText().toString()));

        dieteticProfile.setActualiza(MethodsUtil.getDateNow());

        switch (complexionSpinner.getSelectedItem().toString()){
            case "Delgada":
                dieteticProfile.setConstitucion(1);
                break;
            case "Media":
                dieteticProfile.setConstitucion(2);
                break;
            case "Robusta":
                dieteticProfile.setConstitucion(3);
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

        switch (objectiveSpinner.getSelectedItem().toString()){
            case "Mantener":
                dieteticProfile.setObjetivo(0);
                break;
            case "Perder":
                dieteticProfile.setObjetivo(1);
                break;
            case "Ganar":
                dieteticProfile.setObjetivo(2);
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

    private boolean requiredFields(boolean calculate) {
        boolean error = true;

        // Reset errors.
        mNameView.setError(null);
        mBirthayDayView.setError(null);
        mRate.setError(null);
        int idQuestion1 = mQuestion1.getCheckedRadioButtonId();
        int idQuestion2 = mQuestion2.getCheckedRadioButtonId();
        int idQuestion3 = mQuestion3.getCheckedRadioButtonId();

        // Store values at the time of the login attempt.
        String nameProfile = mNameView.getText().toString().trim();
        String birthayDayView = mBirthayDayView.getText().toString().trim();
        String rate = mRate.getText().toString().trim();

        boolean cancel = false;
        View focusView = null;

        if (-1 == idQuestion1){
            Toast.makeText(this, getString(R.string.error_questions_1), Toast.LENGTH_LONG).show();
            focusView = mQuestion1;
            cancel = true;
        }

        if (-1 == idQuestion2){
            Toast.makeText(this, getString(R.string.error_questions_2), Toast.LENGTH_LONG).show();
            focusView = mQuestion2;
            cancel = true;
        }

        if (-1 == idQuestion3){
            Toast.makeText(this, getString(R.string.error_questions_3), Toast.LENGTH_LONG).show();
            focusView = mQuestion3;
            cancel = true;
        }

        if (TextUtils.isEmpty(rate)) {
            mRate.setError(getString(R.string.error_field_required));
            focusView = mRate;
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

        if (TextUtils.isEmpty(nameProfile)) {
            mNameView.setError(getString(R.string.error_field_required));
            focusView = mNameView;
            cancel = true;
        }

        if (calculate && !cancel){
            focusView = mKcalDayView;
        }

        if (cancel) {
            // There was an error; don't required Fields and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            saveDieteticProfile();
            if (!attemptIdealWeight()) {
                regulateParams();
                getResultKcalAndCg();
                if (!calculate) {
                    saveDieteticProfileDB();
                } else {
                    focusView.requestFocus();
                }
                error = false;
            }
        }
        return error;
    }

    private boolean isBirthayDayValid(String birthayDay) {
        return MethodsUtil.convertBrithayDayToTimeStamp(birthayDay);
    }

    private void saveDieteticProfileDB() {
        long idDieteticProfile = -1;
        DieteticProfile dieteticProfileOld = dbManager.getDieteticProfile(dieteticProfile.getUser_id(), dieteticProfile.getPerfil_id());
        if (null != dieteticProfileOld) {
            idDieteticProfile = dbManager.updateDieteticProfile(dieteticProfile, dieteticProfileOld.getId());
        } else {
            idDieteticProfile = dbManager.addDieteticProfile(dieteticProfile);
        }
        if (idDieteticProfile > -1) {
            CreateNewDieteticProfile.AttemptCreateProfile attemptCreateProfile = new CreateNewDieteticProfile.AttemptCreateProfile(dieteticProfile.getNombre(),
                    String.valueOf(dieteticProfile.getEtapa()), String.valueOf(dieteticProfile.getPerfil_id()), String.valueOf(dieteticProfile.getUser_id()),
                    String.valueOf(dieteticProfile.getSexo()), String.valueOf(dieteticProfile.getF_nac()), String.valueOf(dieteticProfile.getTalla()),
                    String.valueOf(dieteticProfile.getPeso()), String.valueOf(dieteticProfile.getActividad()), String.valueOf(dieteticProfile.getConstitucion()),
                    String.valueOf(dieteticProfile.getPregunta1()), String.valueOf(dieteticProfile.getPregunta2()), String.valueOf(dieteticProfile.getPregunta3()),
                    String.valueOf(dieteticProfile.getObjetivo()), String.valueOf(dieteticProfile.getRitmo()), String.valueOf(dieteticProfile.getState()),
                    dieteticProfile.getActualiza(), String.valueOf(dieteticProfile.getKcaldia()), String.valueOf(dieteticProfile.getCg()));
            Toast toast = Toast.makeText(getApplicationContext(), R.string.update_successful, Toast.LENGTH_SHORT);
            toast.show();
            try {
                if (attemptCreateProfile.execute((Void) null).get()){
                    dbManager.deleteDieteticProfile(idDieteticProfile);
                    toast = Toast.makeText(getApplicationContext(), R.string.error_sync_server, Toast.LENGTH_SHORT);
                    toast.show();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), R.string.update_profile_error, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_pre1_opc1:
                if (checked)
                    dieteticProfile.setPregunta1(1);
                break;
            case R.id.radio_pre1_opc2:
                if (checked)
                    dieteticProfile.setPregunta1(2);
                break;
            case R.id.radio_pre1_opc3:
                if (checked)
                    dieteticProfile.setPregunta1(3);
                break;
            case R.id.radio_pre2_opc1:
                if (checked)
                    dieteticProfile.setPregunta2(1);
                break;
            case R.id.radio_pre2_opc2:
                if (checked)
                    dieteticProfile.setPregunta2(2);
                break;
            case R.id.radio_pre3_opc1:
                if (checked)
                    dieteticProfile.setPregunta3(1);
                break;
            case R.id.radio_pre3_opc2:
                if (checked)
                    dieteticProfile.setPregunta3(2);
                break;
        }
    }
    class AttemptCreateProfile extends AsyncTask<Void, Void, Boolean> {
        private String mNameProfile, mStage, mPerfilId, mUserId, mSex, mBirthday, mHeight, mWeight, mActivity, mComplexion, mQuestion1, mQuestion2, mQuestion3, mObjective, mRate,
                mState, mUpdate, mKcaldia, mCg;
        private String statusCode;
        private boolean error;
        private StringBuilder params = new StringBuilder();

        AttemptCreateProfile(String nameProfile, String stage, String perfilId, String userId, String sex, String birthday, String height,
                             String weight, String activity, String complexion, String question1, String question2, String question3, String objective, String rate,
                             String state, String update, String kcaldia, String cg) {

            mNameProfile = nameProfile;
            mStage = stage;
            mPerfilId = perfilId;
            mUserId = userId;
            mSex = sex;
            mBirthday = birthday;
            mHeight = height;
            mWeight = weight;
            mActivity = activity;
            mComplexion = complexion;
            mQuestion1 = question1;
            mQuestion2 = question2;
            mQuestion3 = question3;
            mObjective = objective;
            mRate = rate;
            mState = state;
            mUpdate = update;
            mKcaldia = kcaldia;
            mCg = cg;
        }


        @Override
        protected Boolean doInBackground(Void... args) {
            SharedPreferences prefs = getSharedPreferences("myPreferences", Context.MODE_PRIVATE);

            try {
                // Building Parameters
                params.append("etapa=").append(mStage).append("&perfil_id=").append(mPerfilId)
                        .append("&user_id=").append(mUserId).append("&nombre=").append(mNameProfile)
                        .append("&sexo=").append(mSex).append("&f_nac=").append(mBirthday).append("&talla=").append(mHeight).append("&peso=").append(mWeight).
                        append("&actividad=").append(mActivity).append("&constitucion=").append(mComplexion)
                        .append("&pregunta1=").append(mQuestion1).append("&pregunta2=").append(mQuestion2).append("&pregunta3=").append(mQuestion3).
                        append("&objetivo=").append(mObjective).append("&ritmo=").append(mRate).append("&state=").append(mState).append("&actualiza=").append(mUpdate)
                        .append("&kcaldia=").append(mKcaldia).append("&cg=").append(mCg);

                // getting product details by making HTTP request
                Map<String, String> response = ClientHTTP.makeHttpRequest(new URL(getString(R.string.url_dietetic_profile_create)), "POST", prefs.getString("apiKey", ""), params.toString());

                // check your log for json response
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

    class AttemptIdealWeight extends AsyncTask<Void, Void, Map<String, String>> {
        private String mSex, mHeight, mComplexion;
        private StringBuilder params = new StringBuilder();
        private String statusCode;
        private Map<String, String> success = new HashMap<>();

        AttemptIdealWeight(String sex, String complexion, String height) {
            mHeight = height;
            mComplexion = complexion;
            mSex = sex;
        }

        @Override
        protected Map<String, String> doInBackground(Void... args) {
            SharedPreferences prefs = getSharedPreferences("myPreferences", Context.MODE_PRIVATE);

            try {
                // Building Parameters
                params.append("sexo=").append(mSex).append("&talla=").append(mHeight).append("&tipoconstitucion=").append(mComplexion);

                // getting product details by making HTTP request
                Map<String, String> response = ClientHTTP.makeHttpRequest(new URL(getString(R.string.url_ideal_weight)), "POST", prefs.getString("apiKey", ""), params.toString());

                statusCode = response.get(getString(R.string.status));
                success.put(getString(R.string.status), statusCode);
                if (!getString(R.string.status_200).equals(statusCode)){
                    success.put(getString(R.string.error), "true");
                    success.put(getString(R.string.message), getString(R.string.error_connection));
                } else {
                    success.put(getString(R.string.error), response.get("error"));
                    success.put(getString(R.string.message), response.get("message"));
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return success;
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitByBackKey();

            //moveTaskToBack(false);

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void exitByBackKey() {

        AlertDialog alertbox = new AlertDialog.Builder(this)
                .setMessage(getString(R.string.do_you_want_to_exit))
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {

                    // do something when the button is clicked
                    public void onClick(DialogInterface arg0, int arg1) {
                        finish();
                        //close();
                    }
                })
                .setNegativeButton(getString(R.string.not), new DialogInterface.OnClickListener() {

                    // do something when the button is clicked
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                })
                .show();

    }

}
