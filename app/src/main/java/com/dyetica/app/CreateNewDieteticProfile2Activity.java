package com.dyetica.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.dyetica.app.fragments.ProfileFragment;
import com.dyetica.app.model.DieteticProfile;
import com.dyetica.app.persistence.ClientHTTP;
import com.dyetica.app.persistence.DBManager;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class CreateNewDieteticProfile2Activity extends AppCompatActivity {

    private EditText rate;
    private Spinner weightSpinner;
    private DieteticProfile dieteticProfile;
    private static final String SEX = "SEXO";
    private static final String COMPLEXION = "COMPLEXION";
    private static final String ACTIVITY = "ACTIVITY";
    private static final String STAGE = "STAGE";
    private static final String HEIGHT = "HEIGHT";
    private static final String WEIGHT = "WEIGHT";
    private static final String NAME_PROFILE = "NAME_PROFILE";
    private static final String BIRTHDAY = "BIRTHDAY";
    private static final String PROFILE = "PROFILE";
    private static final String ID_USER = "idUser";


    private Spinner objectiveSpinner;
    private Button createProfileDietetic;
    private DBManager dbManager;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_dietetic_profile2);
        toolbar = (Toolbar) findViewById(R.id.toolbarCreate2);

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_menu_send));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        //TODO no lo muestra
        toolbar.setTitle("Prueba2");

        dieteticProfile =  new DieteticProfile();
        rate = (EditText) findViewById(R.id.editTextRate);
        objectiveSpinner = (Spinner)findViewById(R.id.spinner_objective);

        getIntentExtras();

        createProfileDietetic = (Button) findViewById(R.id.button_create_profile_dietetic);

        createProfileDietetic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveDieteticProfile();
                saveDieteticProfileDB();
                //TODO: Falta hacer sincronización con BD
                Intent intent = new Intent(view.getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void saveDieteticProfileDB() {
        //TODO PROBAR cuando haya error al guartar en server borrar en la bd local
        if (dbManager.addDieteticProfile(dieteticProfile)) {
            Log.d("CreateDieteticProfile","dieteticProfile CREATE OK");
            CreateNewDieteticProfile2Activity.AttemptCreateProfile attemptCreateProfile = new CreateNewDieteticProfile2Activity.AttemptCreateProfile(dieteticProfile.getNombre(),
                    String.valueOf(dieteticProfile.getEtapa()), String.valueOf(dieteticProfile.getPerfil_id()), String.valueOf(dieteticProfile.getUser_id()),
                    String.valueOf(dieteticProfile.getSexo()), String.valueOf(dieteticProfile.getF_nac()), String.valueOf(dieteticProfile.getTalla()),
                    String.valueOf(dieteticProfile.getPeso()), String.valueOf(dieteticProfile.getActividad()), String.valueOf(dieteticProfile.getConstitucion()),
                    String.valueOf(dieteticProfile.getPregunta1()), String.valueOf(dieteticProfile.getPregunta2()), String.valueOf(dieteticProfile.getPregunta3()),
                    String.valueOf(dieteticProfile.getObjetivo()), String.valueOf(dieteticProfile.getRitmo()), String.valueOf(dieteticProfile.getState()),
                    String.valueOf(dieteticProfile.getPublish_up()), String.valueOf(dieteticProfile.getPublish_down()), String.valueOf(dieteticProfile.getChecked_out()),
                    String.valueOf(dieteticProfile.getChecked_out_time()), dieteticProfile.getActualiza(), String.valueOf(dieteticProfile.getKcaldia()), String.valueOf(dieteticProfile.getCg()));
            Toast toast = Toast.makeText(getApplicationContext(), R.string.update_successful, Toast.LENGTH_SHORT);
            toast.show();
            try {
                if (attemptCreateProfile.execute((Void) null).get()){
                    Log.d("CreateDieteticProfile","dieteticProfile CREATE IN SERVER KO");
                    toast = Toast.makeText(getApplicationContext(), R.string.error_sync_server, Toast.LENGTH_SHORT);
                    toast.show();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        } else {
            Log.d("ProfileFragment","userUpdate MODIFCADO KO");

            Toast toast = Toast.makeText(getApplicationContext(), R.string.update_profile_error, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void saveDieteticProfile() {
        if (!TextUtils.isEmpty(rate.getText()))
            dieteticProfile.setRitmo(Float.parseFloat(rate.getText().toString()));

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

    private void getIntentExtras() {
        Intent intent = getIntent();
        dieteticProfile.setUser_id(intent.getIntExtra(ID_USER, 0));
        dieteticProfile.setNombre(intent.getStringExtra(NAME_PROFILE));
        dieteticProfile.setF_nac(intent.getStringExtra(BIRTHDAY));
        dieteticProfile.setSexo(intent.getIntExtra(SEX, 1));
        dieteticProfile.setConstitucion(intent.getIntExtra(COMPLEXION, 0));
        dieteticProfile.setActividad(intent.getIntExtra(ACTIVITY, 0));
        dieteticProfile.setEtapa(intent.getIntExtra(STAGE, 0));
        dieteticProfile.setTalla(intent.getIntExtra(HEIGHT, 100));
        dieteticProfile.setPeso(intent.getIntExtra(WEIGHT, 10));
        dieteticProfile.setPerfil_id(intent.getIntExtra(PROFILE, 1));
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
        mState, mPublishUp, mPublishDown, mCheckedOut, mCheckedOutTime, mUpdate, mKcaldia, mCg;
        private String statusCode;
        private boolean error;
        private StringBuilder params = new StringBuilder();

        AttemptCreateProfile(String nameProfile, String stage, String perfilId, String userId, String sex, String birthday, String height,
                             String weight, String activity, String complexion, String question1, String question2, String question3, String objective, String rate,
                             String state, String publishUp, String publishDown, String checkedOut, String checkedOutTime, String update, String kcaldia, String cg) {

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
            mPublishUp = publishUp;
            mPublishDown = publishDown;
            mCheckedOut = checkedOut;
            mCheckedOutTime = checkedOutTime;
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
                        append("&objetivo=").append(mObjective).append("&ritmo=").append(mRate).append("&state=").append(mState).
                        append("&publish_up=").append(mPublishUp).append("&publish_down=").append(mPublishDown).append("&checked_out=").append(mCheckedOut)
                        .append("&checked_out_time=").append(mCheckedOutTime).append("&actualiza=").append(mUpdate)
                        .append("&kcaldia=").append(mKcaldia).append("&cg=").append(mCg);

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
