package com.dyetica.app.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.dyetica.app.LoginActivity;
import com.dyetica.app.MainActivity;
import com.dyetica.app.R;
import com.dyetica.app.model.ExtensionsBalancerPlus;
import com.dyetica.app.model.Food;
import com.dyetica.app.model.PersonalFood;
import com.dyetica.app.persistence.ClientHTTP;
import com.dyetica.app.persistence.DBManager;
import com.dyetica.app.utils.MethodsUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class CreateFoodDialogFragment extends DialogFragment {
    private static final String ID_EXTENSIONS_BALANCER_PLUS = "idExtensionsBalancerPlus";
    private static final String PORTION = "portion";
    private static final int THE_SIZE = 20;

    private Spinner mSpinnerLocationFood;
    private DBManager dbManager;
    private ExtensionsBalancerPlus extensionsBalancerPlus;
    private List<String> listLocations;
    private EditText mNetWeight;
    private EditText mProteins;
    private EditText mFats;
    private EditText mHydrates;
    private EditText mDescriptionFood;
    private Map<String, String> categories;
    private ArrayAdapter mArrayAdapterLocationFood, mArrayAdapterLocationFoodUpdate;
    private int portion;
    private DecimalFormat df;

    //TAB UPDATE
    private Spinner mSpinnerLocationFoodUpdate;
    private EditText mNetWeightUpdate;
    private EditText mProteinsUpdate;
    private EditText mFatsUpdate;
    private EditText mHydratesUpdate;
    private Spinner mSpinnerFoods;
    private PersonalFood personalFoodSelected;

    OnCreateFoodDialogSelectedListener mCallback;
    private TabHost tabs;


    private AttemptAddPersonalFoods attemptAddPersonalFoods;
    private AttemptDeletePersonalFoods attemptDeletePersonalFoods;


    public CreateFoodDialogFragment(int portion) {
        this.portion = portion;
    }

    // Container Activity must implement this interface
    public interface OnCreateFoodDialogSelectedListener {
        void onPortion(int portion);
    }

    public CreateFoodDialogFragment() {
    }

    public static CreateFoodDialogFragment newInstance(int portion){
        CreateFoodDialogFragment fragment = new CreateFoodDialogFragment(portion);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnCreateFoodDialogSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }


    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        final View rootViewDialog = inflater.inflate(R.layout.fragment_create_food_dialog, null);
        if (null != savedInstanceState) {
            this.portion = savedInstanceState.getInt(PORTION);
        }
        SharedPreferences prefs = getActivity().getSharedPreferences("myPreferences", Context.MODE_PRIVATE);
        tabs= (TabHost) rootViewDialog.findViewById(R.id.tabHostCreate);

        tabs.setup();

        TabHost.TabSpec tabpage1 = tabs.newTabSpec("CreatePersonalFood");
        tabpage1.setContent(R.id.create_personal_food);
        tabpage1.setIndicator(getString(R.string.create_ingredient), getResources().getDrawable(R.drawable.ic_create_black_48dp));

        TabHost.TabSpec tabpage2 = tabs.newTabSpec("UpdatePersonalFood");
        tabpage2.setContent(R.id.update_personal_food);
        tabpage2.setIndicator(getString(R.string.update_ingredient), getResources().getDrawable(R.drawable.ic_create_black_48dp));

        tabs.addTab(tabpage2);
        tabs.addTab(tabpage1);

        if (!MethodsUtil.isTablet(getContext())) {
            tabs.getTabWidget().getChildAt(0).getLayoutParams().height = (int) (35 * this.getResources().getDisplayMetrics().density);
            tabs.getTabWidget().getChildAt(0).setPadding(0, 0, 0, 0);

            tabs.getTabWidget().getChildAt(1).getLayoutParams().height = (int) (35 * this.getResources().getDisplayMetrics().density);
            tabs.getTabWidget().getChildAt(1).setPadding(0, 0, 0, 0);
        } else {
            TextView title =(TextView) tabs.getTabWidget().getChildAt(0).findViewById(android.R.id.title);
            title.setTextSize(17);

            TextView title2 =(TextView) tabs.getTabWidget().getChildAt(1).findViewById(android.R.id.title);
            title2.setTextSize(17);
        }

        categories = new HashMap<>();
        dbManager = DBManager.getInstance(getActivity());
        extensionsBalancerPlus = dbManager.getExtensionsBalancerPlus(prefs.getLong(ID_EXTENSIONS_BALANCER_PLUS, 0));

        //TAB CREATE
        mSpinnerLocationFood = (Spinner) rootViewDialog.findViewById(R.id.spinner_location_food);
        mArrayAdapterLocationFood = initSpinnerListLocation();
        mSpinnerLocationFood.setAdapter(mArrayAdapterLocationFood);
        mNetWeight = (EditText) rootViewDialog.findViewById(R.id.edit_text_net_weight);
        mNetWeight.clearFocus();
        mProteins = (EditText) rootViewDialog.findViewById(R.id.edit_text_protein);
        mHydrates = (EditText) rootViewDialog.findViewById(R.id.edit_text_hydrates);
        mFats = (EditText) rootViewDialog.findViewById(R.id.edit_text_fats);
        mDescriptionFood = (EditText) rootViewDialog.findViewById(R.id.edit_text_name_food);

        //TAB UPDATE
        mSpinnerLocationFoodUpdate = (Spinner) rootViewDialog.findViewById(R.id.spinner_location_food_update);
        mArrayAdapterLocationFoodUpdate = initSpinnerListLocation();
        mSpinnerLocationFoodUpdate.setAdapter(mArrayAdapterLocationFoodUpdate);
        mNetWeightUpdate = (EditText) rootViewDialog.findViewById(R.id.edit_text_net_weight_update);
        mNetWeightUpdate.clearFocus();
        mProteinsUpdate = (EditText) rootViewDialog.findViewById(R.id.edit_text_protein_update);
        mHydratesUpdate = (EditText) rootViewDialog.findViewById(R.id.edit_text_hydrates_update);
        mFatsUpdate = (EditText) rootViewDialog.findViewById(R.id.edit_text_fats_update);
        mSpinnerFoods = (Spinner) rootViewDialog.findViewById(R.id.spinner_food);

        mSpinnerLocationFoodUpdate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 mSpinnerFoods.setAdapter(initSpinnerListPersonalFoods(extensionsBalancerPlus, position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mSpinnerFoods.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                personalFoodSelected = (PersonalFood) mSpinnerFoods.getSelectedItem();
                mNetWeightUpdate.setText(String.valueOf(personalFoodSelected.getPeso_neto()));
                mProteinsUpdate.setText(String.valueOf(personalFoodSelected.getProteinas()));
                mHydratesUpdate.setText(String.valueOf(personalFoodSelected.getHidratos_carbono()));
                mFatsUpdate.setText(String.valueOf(personalFoodSelected.getGrasas()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(rootViewDialog).setTitle(R.string.personal_food_title).setIcon(R.drawable.icon_dyetica)
            // Add action buttons
               .setPositiveButton(R.string.update, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        if ("CreatePersonalFood".equals(tabs.getCurrentTabTag())) {
                            if (!mDescriptionFood.getText().toString().equals("") && !mNetWeight.getText().toString().toString().equals("") &&
                                    !mHydrates.getText().toString().equals("") && !mProteins.getText().toString().equals("") && !mFats.getText().toString().equals("")) {
                                attemptAddPersonalFoods = new AttemptAddPersonalFoods(0, mDescriptionFood.getText().toString(), mNetWeight.getText().toString(),
                                        String.valueOf(mArrayAdapterLocationFood.getPosition(mSpinnerLocationFood.getSelectedItem())), mHydrates.getText().toString(),
                                        mProteins.getText().toString(), mFats.getText().toString());
                                executeAttempt(attemptAddPersonalFoods);
                            } else {
                                Toast.makeText(getActivity(), getString(R.string.message_error_empty_fields), Toast.LENGTH_LONG).show();
                            }
                        } else {
                            if (!mNetWeightUpdate.getText().toString().toString().equals("") &&
                                    !mHydratesUpdate.getText().toString().equals("") && !mProteinsUpdate.getText().toString().equals("") && !mFatsUpdate.getText().toString().equals("")) {
                                attemptAddPersonalFoods = new AttemptAddPersonalFoods(personalFoodSelected.getId(), personalFoodSelected.getDescripcion_alimento(), mNetWeightUpdate.getText().toString(),
                                        String.valueOf(mArrayAdapterLocationFoodUpdate.getPosition(mSpinnerLocationFoodUpdate.getSelectedItem())),
                                        mHydratesUpdate.getText().toString(), mProteinsUpdate.getText().toString(), mFatsUpdate.getText().toString());
                                executeAttempt(attemptAddPersonalFoods);
                            } else {
                                Toast.makeText(getActivity(), getString(R.string.message_error_empty_fields), Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    }).setNeutralButton(R.string.delete, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Map<String, String> successAttempDeletePersonalFoods = new HashMap<>();
                            boolean error;
                            attemptDeletePersonalFoods = new AttemptDeletePersonalFoods(personalFoodSelected.getId());
                            try {
                                successAttempDeletePersonalFoods = attemptDeletePersonalFoods.execute((Void) null).get();
                            } catch (InterruptedException e) {
                                Log.e("LoginActivity", "Attempt login have been interripted: " + e.getMessage());
                            } catch (ExecutionException e) {
                                Log.e("VERBOSE", "Attempt login have been in execution error: " + e.getMessage());
                            }

                            error = Boolean.parseBoolean(successAttempDeletePersonalFoods.get(getString(R.string.error)));
                            if (error) {
                                Toast.makeText(getActivity(), successAttempDeletePersonalFoods.get(getString(R.string.message)), Toast.LENGTH_LONG).show();
                            } else {
                                mCallback.onPortion(portion);
                            }                        }
                });

        final AlertDialog dialog = builder.create();
        dialog.show();

        tabs.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if ("CreatePersonalFood".equals(tabId)) {
                    dialog.getButton(DialogInterface.BUTTON_POSITIVE).setText(getString(R.string.created));
                    dialog.getButton(DialogInterface.BUTTON_NEUTRAL).setVisibility(View.INVISIBLE);
                    mNetWeight.clearFocus();
                    mDescriptionFood.clearFocus();
                } else {
                    dialog.getButton(DialogInterface.BUTTON_POSITIVE).setText(getString(R.string.update));
                    dialog.getButton(DialogInterface.BUTTON_NEUTRAL).setVisibility(View.VISIBLE);
                    mNetWeightUpdate.clearFocus();
                }
            }
        });

        if (MethodsUtil.isTablet(getContext())) {
            // Positive
            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextSize(THE_SIZE);
            // Negative
            dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextSize(THE_SIZE);
            // Neutral
            dialog.getButton(DialogInterface.BUTTON_NEUTRAL).setTextSize(THE_SIZE);

        }
        return dialog;

    }

    private ArrayAdapter initSpinnerListPersonalFoods(ExtensionsBalancerPlus extensionsBalancerPlus, int location) {
        List<PersonalFood> personalFoods;
        personalFoods = dbManager.getPersonalFoodByLocation(location);
        if (!personalFoods.isEmpty()){
            for (PersonalFood personalFood : personalFoods) {
                personalFood.setValorQp(Float.parseFloat(MethodsUtil.getValorQP(personalFood, extensionsBalancerPlus)));
                personalFood.setValorQr(Float.parseFloat(MethodsUtil.getValorQR(personalFood, extensionsBalancerPlus)));
            }
        } else {
            PersonalFood personalFood = new PersonalFood();
            personalFood.setDescripcion_alimento(getString(R.string.not_food));
            personalFoods.add(personalFood);
        }

        ArrayAdapter adapterSppiner = new ArrayAdapter(getActivity(),
                R.layout.spinner_item, personalFoods);
        return  adapterSppiner;
    }

    private void executeAttempt(AttemptAddPersonalFoods attemptAddPersonalFoods) {
        Map<String, String> successAttempPersonalFood = new HashMap<>();
        boolean error;
        try {
            successAttempPersonalFood = attemptAddPersonalFoods.execute((Void) null).get();
        } catch (InterruptedException e) {
            Log.e("LoginActivity", "Attempt login have been interripted: " + e.getMessage());
        } catch (ExecutionException e) {
            Log.e("VERBOSE", "Attempt login have been in execution error: " + e.getMessage());
        }

        error = Boolean.parseBoolean(successAttempPersonalFood.get(getString(R.string.error)));
        if (error) {
            Toast.makeText(getActivity(), successAttempPersonalFood.get(getString(R.string.message)), Toast.LENGTH_LONG).show();
        } else {
            mCallback.onPortion(portion);
        }
    }

    private ArrayAdapter initSpinnerListLocation() {
        listLocations =  new ArrayList<>();
        listLocations.add(getString(R.string.complements));
        listLocations.add(getString(R.string.precooked));
        listLocations.add(getString(R.string.my_combined));
        listLocations.add(getString(R.string.desserts));

        ArrayAdapter adapterSppiner = new ArrayAdapter(getActivity(),
                R.layout.spinner_item, listLocations);
        return  adapterSppiner;
    }

    class AttemptAddPersonalFoods extends AsyncTask<Void, Void, Map<String, String>> {
        private String error, message, statusCode;
        private Map<String, String> success = new HashMap<>();
        private Map<String, String> response;
        private final long id;
        private final String descriptionFood, netWeight, location, hydrates, protein, fats;
        private StringBuilder params = new StringBuilder();

        AttemptAddPersonalFoods(long id, String descriptionFood, String netWeight, String location, String hydrates, String protein, String fats) {
            this.id = id;
            this.descriptionFood = descriptionFood;
            this.netWeight = netWeight;
            this.location = location;
            this.hydrates = hydrates;
            this.protein = protein;
            this.fats = fats;
        }

        @Override
        protected Map<String, String> doInBackground(Void... args) {
            SharedPreferences prefs = getActivity().getSharedPreferences("myPreferences", Context.MODE_PRIVATE);
            // Check for success tag
            try {
                // Building Parameters
                params.append("id=").append(id)
                        .append("&descripcion_alimento=").append(descriptionFood)
                        .append("&peso_neto=").append(netWeight)
                        .append("&hidratos_carbono=").append(hydrates)
                        .append("&proteinas=").append(protein)
                        .append("&grasas=").append(fats)
                        .append("&ubicacion=").append(location);

                Log.d("ProfileFragment", "Valor de params PROFILE: " +  params.toString());
                // getting dietetic profile details by making HTTP request
                response = ClientHTTP.makeHttpRequest(new URL(getString(R.string.url_add_personal_food)), "POST", prefs.getString("apiKey", ""),  params.toString());
                // check your log for json response
                statusCode = response.get(getString(R.string.status));
                success.put(getString(R.string.status), statusCode);
                if (!getString(R.string.status_200).equals(statusCode)){
                    success.put(getString(R.string.error), "true");
                    success.put(getString(R.string.message), getString(R.string.error_connection));
                } else {
                    error = response.get("error");
                    message = response.get("message");
                    if (error == "false") {
                        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                        JSONObject jsonObject = new JSONObject(message);
                        PersonalFood personalFood = gson.fromJson(jsonObject.toString(), PersonalFood.class);
                        PersonalFood personalFoodOld = dbManager.getPersonalFood(personalFood.getId());
                        personalFood.setCalorias(MethodsUtil.calcCalories(personalFood));
                        if (null == personalFoodOld) {
                            dbManager.addPersonalFoods(personalFood);
                        } else {
                            dbManager.updatePersonalFood(personalFood);
                        }
                    }
                    success.put(getString(R.string.error), error);
                    success.put(getString(R.string.message), message);
                }
            } catch (MalformedURLException e) {
                Log.e("MainActivity", "Url " + getString(R.string.url_add_personal_food) +  " is malformed");
            } catch (JSONException e) {
                Log.e("MainActivity", "Json exception in AttemptPersonalFoods");
            }
            return success;
        }

    }

    class AttemptDeletePersonalFoods extends AsyncTask<Void, Void, Map<String, String>> {
        private String error, message, statusCode;
        private Map<String, String> success = new HashMap<>();
        private Map<String, String> response;
        private final long id;
        private StringBuilder params = new StringBuilder();

        AttemptDeletePersonalFoods(long id) {
            this.id = id;
        }

        @Override
        protected Map<String, String> doInBackground(Void... args) {
            SharedPreferences prefs = getActivity().getSharedPreferences("myPreferences", Context.MODE_PRIVATE);
            // Check for success tag
            try {
                // Building Parameters
                params.append("id=").append(id);

                Log.d("ProfileFragment", "Valor de params PROFILE: " +  params.toString());
                // getting dietetic profile details by making HTTP request
                response = ClientHTTP.makeHttpRequest(new URL(getString(R.string.url_delete_personal_food)), "POST", prefs.getString("apiKey", ""),  params.toString());
                // check your log for json response
                statusCode = response.get(getString(R.string.status));
                success.put(getString(R.string.status), statusCode);
                if (!getString(R.string.status_200).equals(statusCode)){
                    success.put(getString(R.string.error), "true");
                    success.put(getString(R.string.message), getString(R.string.error_connection));
                } else {
                    error = response.get("error");
                    message = response.get("message");
                    if (error == "false") {
                        dbManager.deletePersonalFood(id);
                    }
                    success.put(getString(R.string.error), error);
                    success.put(getString(R.string.message), message);
                }
            } catch (MalformedURLException e) {
                Log.e("MainActivity", "Url " + getString(R.string.url_add_personal_food) +  " is malformed");
            }
            return success;
        }

    }

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(PORTION, portion);
    }

}
