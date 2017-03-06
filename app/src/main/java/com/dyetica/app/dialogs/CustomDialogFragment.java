package com.dyetica.app.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.dyetica.app.R;
import com.dyetica.app.model.DieteticProfile;
import com.dyetica.app.model.ExtensionsBalancerPlus;
import com.dyetica.app.model.ExtensionsProfile;
import com.dyetica.app.model.Food;
import com.dyetica.app.model.PersonalFood;
import com.dyetica.app.persistence.DBManager;
import com.dyetica.app.utils.MethodsUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CustomDialogFragment extends DialogFragment {
    private static final String ID_EXTENSIONS_BALANCER_PLUS = "idExtensionsBalancerPlus";
    private static final String ID_EXTENSIONS_PROFILE = "idExtensionsProfile";
    private static final String ID_USER = "idUser";
    private static final String FOOD_LIST = "food_list";
    private static final String PORTION = "portion";
    private static final String KCAL_PORTION = "kcal_portion";

    private ArrayList<Parcelable> foodList;
    private Spinner mSpinnerMyProfile;
    private Spinner mSpinnerLocationDisk;
    private Spinner mSpinnerPortions;
    private DBManager dbManager;
    private ExtensionsBalancerPlus extensionsBalancerPlus;
    private ExtensionsProfile extensionsProfile;
    private List<String> listMyProfiles;
    private EditText mKcalPortion;
    private float kcalPortion, kcalLocation;
    private int portion;
    private int idUser, numProfileDietetic;
    private ArrayAdapter mArrayAdapterMyProfile;
    private float cgBase, cgMulti, oil;
    private DieteticProfile dieteticProfile;
    private Map<String, Float> mealPercentages;
    private Map<Integer, String> locationDiskList;

    OnCustomDialogSelectedListener mCallback;

    public CustomDialogFragment(int portion, float kcalPortion, ArrayList<Parcelable> foodList) {
        this.portion = portion;
        this.kcalPortion = kcalPortion;
        this.foodList = foodList;
    }

    public CustomDialogFragment() {
        foodList = new ArrayList<>();
    }

    public interface OnCustomDialogSelectedListener {
        void onPortionAndKcal(int portion, float kcal, ArrayList<Parcelable> foodList, float oil);
    }

    public static CustomDialogFragment newInstance(int portion, float kcalPortion, ArrayList<Parcelable> foodList) {
        CustomDialogFragment fragment = new CustomDialogFragment(portion, kcalPortion, foodList);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnCustomDialogSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }


    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        final View rootViewDialog = inflater.inflate(R.layout.dialog_custom, null);
        if (null != savedInstanceState) {
            this.portion = savedInstanceState.getInt(PORTION);
            this.kcalPortion = savedInstanceState.getFloat(KCAL_PORTION);
            this.foodList = savedInstanceState.getParcelableArrayList(FOOD_LIST);
        }
        SharedPreferences prefs = getActivity().getSharedPreferences("myPreferences", Context.MODE_PRIVATE);
        dbManager = DBManager.getInstance(getActivity());
        extensionsBalancerPlus = dbManager.getExtensionsBalancerPlus(prefs.getLong(ID_EXTENSIONS_BALANCER_PLUS, 0));
        extensionsProfile = dbManager.getExtensionsProfile(prefs.getLong(ID_EXTENSIONS_PROFILE, 0));
        cgBase = extensionsProfile.getFormulas_cg_varible1();
        cgMulti = extensionsProfile.getFormulas_cg_varible2();
        idUser = prefs.getInt(ID_USER, 0);
        numProfileDietetic = dbManager.countDieteticProfileByIdUser(idUser);
        mSpinnerMyProfile = (Spinner) rootViewDialog.findViewById(R.id.spinner_my_profile);
        mArrayAdapterMyProfile = initAdapterMyProfile(numProfileDietetic);
        mSpinnerMyProfile.setAdapter(mArrayAdapterMyProfile);
        initLocationDiskList();
        mSpinnerLocationDisk = (Spinner) rootViewDialog.findViewById(R.id.spinner_location_disk);
        mSpinnerPortions = (Spinner) rootViewDialog.findViewById(R.id.spinner_portion);
        mSpinnerPortions.setSelection(portion-1);
        mKcalPortion = (EditText) rootViewDialog.findViewById(R.id.kcal_balance_text);
        mKcalPortion.setText(String.valueOf(kcalPortion));

        if ("Sin Perfil".equals(mSpinnerMyProfile.getSelectedItem().toString())) {
            mSpinnerLocationDisk.setEnabled(false);
            mKcalPortion.setEnabled(true);
        } else {
            mSpinnerLocationDisk.setEnabled(true);
            mKcalPortion.setEnabled(false);
        }

        mSpinnerMyProfile.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String myProfileSelected = parent.getItemAtPosition(position).toString();
                if ("Sin Perfil".equals(myProfileSelected)) {
                    mSpinnerLocationDisk.setEnabled(false);
                    mKcalPortion.setEnabled(true);
                } else {
                    mSpinnerLocationDisk.setEnabled(true);
                    mKcalPortion.setEnabled(false);
                    dieteticProfile = dbManager.getDieteticProfile(idUser, position);
                    if (null != dieteticProfile) {
                        getPercentages(dieteticProfile, extensionsProfile);
                        kcalLocation = MethodsUtil.roundDecimals((mealPercentages.get("desayuno").floatValue() * dieteticProfile.getKcaldia()) / 100, 2);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mSpinnerLocationDisk.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (null != mealPercentages) {
                    kcalLocation = MethodsUtil.roundDecimals((mealPercentages.get(locationDiskList.get(position)).floatValue() * dieteticProfile.getKcaldia()) / 100, 2);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(rootViewDialog).setTitle(R.string.custom_title).setIcon(R.drawable.icon_dyetica)
                // Add action buttons
                .setPositiveButton(R.string.calculate, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        ArrayList<Parcelable> foodListNew;
                        if (null != dieteticProfile) {
                            foodListNew = setGramsFoodList(kcalLocation);
                            oil = getOil(foodListNew);
                        } else {
                            oil = 0;
                            foodListNew = setGramsFoodList(Float.parseFloat(mKcalPortion.getText().toString()));
                        }
                        mCallback.onPortionAndKcal(Integer.parseInt(mSpinnerPortions.getSelectedItem().toString()), Float.parseFloat(mKcalPortion.getText().toString()), foodListNew, oil);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        return builder.create();
    }

    private void initLocationDiskList() {
        locationDiskList = new LinkedHashMap<>();

        locationDiskList.put(0, "desayuno");
        locationDiskList.put(1, "almuerzo");
        locationDiskList.put(2, "plato1_comida");
        locationDiskList.put(3, "plato2_comida");
        locationDiskList.put(4, "plato12_comida");
        locationDiskList.put(5, "postre_comida");
        locationDiskList.put(6, "merienda");
        locationDiskList.put(7, "plato1_cena");
        locationDiskList.put(8, "plato2_cena");
        locationDiskList.put(9, "plato12_cena");
        locationDiskList.put(10, "postre_cena");
    }

    private float getOil(ArrayList<Parcelable> foodListNew) {
        float hydrates = 0, protein = 0, fats = 0, oilAll = 0, oil = 0;
        for (Parcelable parcelable : foodListNew) {
            if (parcelable instanceof Food) {
                Food food = (Food) parcelable;
                hydrates += food.getHidratos_carbono() * food.getGramos() / 100F;
                protein += food.getProteinas() * food.getGramos() / 100F;
                fats += food.getGrasas() * food.getGramos() / 100F;

            } else {
                PersonalFood personalFood = (PersonalFood) parcelable;
                hydrates += personalFood.getHidratos_carbono() * personalFood.getPeso_neto() / 100F;
                protein += personalFood.getProteinas() * personalFood.getPeso_neto() / 100F;
                fats += personalFood.getGrasas() * personalFood.getPeso_neto() / 100F;
            }
        }

        if (fats >= (hydrates + protein)) {
            oil = MethodsUtil.roundDecimals((fats * extensionsBalancerPlus.getMultiplica()) / ((hydrates + protein) * extensionsProfile.getFormulas_cg_varible1()), 2);
        } else {
            oil = MethodsUtil.roundDecimals((fats * extensionsBalancerPlus.getMultiplica()) / (hydrates + protein), 3);
        }
        oilAll = MethodsUtil.roundDecimals((((((oil - dieteticProfile.getCg()) * extensionsProfile.getFormulas_cg_varible2() *  kcalLocation) / extensionsProfile.getFormulas_cg_varible1()) / 9) * -1), 2);

        return oilAll;
    }

    private ArrayList<Parcelable> setGramsFoodList(float kcal) {
        ArrayList<Parcelable> foodListNew = new ArrayList<>();
        int portionSpinner = Integer.parseInt(mSpinnerPortions.getSelectedItem().toString());
        float kcalOld = 0;
        float portionFactor = 0;
        if (portion != portionSpinner && kcal == 0 ) {
            portionFactor = portionSpinner / portion;
            for (Parcelable parcelable : foodList) {
                if (parcelable instanceof Food) {
                    Food food = (Food) parcelable;
                    int gramsNew = Math.round(food.getGramos() * portionFactor);
                    food.setGramos(gramsNew);
                    dbManager.updateFood(food);
                    foodListNew.add(food);
                } else {
                    PersonalFood personalFood = (PersonalFood) parcelable;
                    int gramsNew = Math.round(personalFood.getPeso_neto() * portionFactor);
                    personalFood.setPeso_neto(gramsNew);
                    dbManager.updatePersonalFood(personalFood);
                    foodListNew.add(personalFood);
                }
            }
        } else if (portion == portionSpinner && kcal != 0) {
            portionFactor = portion;
            kcalOld = getDataBalancePlus();
            for (Parcelable parcelable : foodList) {
                if (parcelable instanceof Food) {
                    Food food = (Food) parcelable;
                    int gramsNew = Math.round(((food.getGramos() * portionFactor) * kcal) / kcalOld );
                    food.setGramos(gramsNew);
                    dbManager.updateFood(food);
                    foodListNew.add(food);
                } else {
                    PersonalFood personalFood = (PersonalFood) parcelable;
                    int gramsNew = Math.round(((personalFood.getPeso_neto() * portionFactor) * kcal) / kcalOld );
                    personalFood.setPeso_neto(gramsNew);
                    dbManager.updatePersonalFood(personalFood);
                    foodListNew.add(personalFood);
                }
            }
        } else if (portion != portionSpinner && kcal != 0) {
            portionFactor = portion;
            kcalOld = getDataBalancePlus() / portion;
            for (Parcelable parcelable : foodList) {
                if (parcelable instanceof Food) {
                    Food food = (Food) parcelable;
                    int gramsNew = Math.round((((food.getGramos() / portionFactor) * kcal) / kcalOld)) * portionSpinner;
                    food.setGramos(gramsNew);
                    dbManager.updateFood(food);
                    foodListNew.add(food);
                } else {
                    PersonalFood personalFood = (PersonalFood) parcelable;
                    int gramsNew = Math.round((((personalFood.getPeso_neto() / portionFactor) * kcal) / kcalOld)) * portionSpinner;
                    personalFood.setPeso_neto(gramsNew);
                    dbManager.updatePersonalFood(personalFood);
                    foodListNew.add(personalFood);
                }
            }
        }
        return foodListNew;
    }


    public float getDataBalancePlus() {
        float kcalOld = 0;
        //TODO falta un if al principio pero no se si tiene que ir
        for (Parcelable parcelable : foodList) {
            float grams = 0;
            if (parcelable instanceof Food) {
                Food food = (Food) parcelable;
                grams = food.getGramos() / 100F;
                kcalOld += (food.getCalorias() * grams);
            } else {
                PersonalFood personalFood = (PersonalFood) parcelable;
                grams = personalFood.getPeso_neto() / 100F;
                kcalOld += (((personalFood.getHidratos_carbono() * 4) + (personalFood.getProteinas() * 4) + (personalFood.getGrasas() * 9)) * grams);
            }
        }
        return kcalOld;
    }

    private void getPercentages(DieteticProfile dieteticProfile, ExtensionsProfile extensionsProfile) {
        mealPercentages = new LinkedHashMap<>();
        if (dieteticProfile.getPregunta1() == 1) {
            mealPercentages.put("desayuno", extensionsProfile.getManana_desayuno_fuerte());
            mealPercentages.put("almuerzo", extensionsProfile.getManana_almuerzo_ligero());
        } else if (dieteticProfile.getPregunta1() == 2) {
            mealPercentages.put("desayuno", extensionsProfile.getManana_desayuno_mediano());
            mealPercentages.put("almuerzo", extensionsProfile.getManana_almuerzo_mediano());
        } else {
            mealPercentages.put("desayuno", extensionsProfile.getManana_desayuno_ligero());
            mealPercentages.put("almuerzo", extensionsProfile.getManana_almuerzo_fuerte());
        }

        if (dieteticProfile.getPregunta2() == 1) {
            mealPercentages.put("plato1_comida", extensionsProfile.getMediodia_priomeroligero());
            mealPercentages.put("plato2_comida", extensionsProfile.getMediodia_segundofuerte());
            mealPercentages.put("plato12_comida", extensionsProfile.getMediodia_priomeroligero() + extensionsProfile.getMediodia_segundofuerte());
            mealPercentages.put("postre_comida", extensionsProfile.getMediodia_postre());
            mealPercentages.put("merienda", extensionsProfile.getMerienda());
        } else {
            mealPercentages.put("plato1_comida", extensionsProfile.getMediodia_primeromedio());
            mealPercentages.put("plato2_comida", extensionsProfile.getMediodia_segundomedio());
            mealPercentages.put("plato12_comida", extensionsProfile.getMediodia_primeromedio() + extensionsProfile.getMediodia_segundomedio());
            mealPercentages.put("postre_comida", extensionsProfile.getMediodia_postre());
            mealPercentages.put("merienda", extensionsProfile.getMerienda());
        }

        if (dieteticProfile.getPregunta3() == 1) {
            mealPercentages.put("plato1_cena", extensionsProfile.getNoche_priomeroligero());
            mealPercentages.put("plato2_cena", extensionsProfile.getNoche_segundofuerte());
            mealPercentages.put("plato12_cena", extensionsProfile.getNoche_priomeroligero() + extensionsProfile.getNoche_segundofuerte());
            mealPercentages.put("postre_cena", extensionsProfile.getNoche_postre());
        } else {
            mealPercentages.put("plato1_cena", extensionsProfile.getNoche_primeromedio());
            mealPercentages.put("plato2_cena", extensionsProfile.getNoche_segundomedio());
            mealPercentages.put("plato12_cena", extensionsProfile.getNoche_primeromedio() + extensionsProfile.getNoche_segundomedio());
            mealPercentages.put("postre_cena", extensionsProfile.getNoche_postre());
        }

    }

    private ArrayAdapter initAdapterMyProfile(int numProfileDietetic) {
        listMyProfiles = new ArrayList<>();
        listMyProfiles.add("Sin Perfil");
        for (int i = 0; i < numProfileDietetic; i++){
            listMyProfiles.add("Nº " + (i + 1));
        }
        ArrayAdapter adapterSppiner = new ArrayAdapter(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, listMyProfiles);
        return  adapterSppiner;
    }

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(PORTION, portion);
        outState.putFloat(KCAL_PORTION, kcalPortion);
        outState.putParcelableArrayList(FOOD_LIST, foodList);
    }

}
