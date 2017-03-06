package com.dyetica.app.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dyetica.app.R;
import com.dyetica.app.adapters.FoodsAdpter;
import com.dyetica.app.dialogs.AddFoodDialogFragment;
import com.dyetica.app.dialogs.BalanceDialogFragment;
import com.dyetica.app.dialogs.CreateFoodDialogFragment;
import com.dyetica.app.dialogs.CustomDialogFragment;
import com.dyetica.app.dialogs.ShowInfoDialogFragment;
import com.dyetica.app.model.ExtensionsBalancerPlus;
import com.dyetica.app.model.Food;
import com.dyetica.app.model.PersonalFood;
import com.dyetica.app.persistence.DBManager;
import com.dyetica.app.utils.MethodsUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static com.dyetica.app.R.color.colorGreenApp;
import static com.dyetica.app.R.color.colorRed;
import static com.dyetica.app.R.color.colorWhite;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BalancerPlusFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BalancerPlusFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BalancerPlusFragment extends Fragment {
    private static final String ID_EXTENSIONS_BALANCER_PLUS = "idExtensionsBalancerPlus";
    private static final String FOOD_ID = "id_food";
    private static final String OLD_FOOD_ID = "id_food_old";
    private static final String FOOD_LIST = "food_list";
    private static final String PORTION = "portion";
    private static final String GRAMS = "grams";
    private static final String OIL = "oil";
    private static final String IS_UPDATED_FOOD = "is_updated_food";
    private static final String IS_PERSONAL_FOOD = "isPersonalFood";
    private ExtensionsBalancerPlus extensionsBalancerPlus;
    private TextView textViewBalance;
    private TextView mCompositionHydrates;
    private TextView mCompositionProtein;
    private TextView mCompositionFats;
    private TextView mRelationHydratesProtein;
    private TextView mRelationFats;
    private TextView mOilValue;
    private EditText mEditTextKcal;
    private EditText mPortion;
    private ListView mListViewFoods;
    private DBManager dbManager;
    private Button mCustom;
    private Button mBalance;
    private Button mAddFood;
    private Button mCreateFood;
    private Button mMore;
    private long idFood;
    private Long idFoodOld;
    private int grams, portion;
    private boolean isPersonalFood;
    private ArrayList<Parcelable> foodList;
    private float hydrates = 0, protein = 0, fats = 0, quantity = 0, vdie1 = 0, vdie2 = 0, calory = 0, fatsTotal = 0, proteinTotal = 0, oil = 0;
    private boolean isBalance, isUpdateFood;
    private DecimalFormat df;
    private OnFragmentInteractionListener mListener;

    public BalancerPlusFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment BalancerPlusFragment.
     * */
    public static BalancerPlusFragment newInstance(ArrayList<Food> listFood) {
        BalancerPlusFragment fragment = new BalancerPlusFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(FOOD_LIST, listFood);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null && savedInstanceState == null) {
            grams= getArguments().getInt(GRAMS);
            idFood =  getArguments().getLong(FOOD_ID);
            idFoodOld =  getArguments().getLong(OLD_FOOD_ID);
            foodList = getArguments().getParcelableArrayList(FOOD_LIST);
            isPersonalFood = getArguments().getBoolean(IS_PERSONAL_FOOD);
            portion = getArguments().getInt(PORTION);
            oil = getArguments().getFloat(OIL);
            isUpdateFood = getArguments().getBoolean(IS_UPDATED_FOOD);
        } else {
            foodList = savedInstanceState.getParcelableArrayList(FOOD_LIST);
            grams = savedInstanceState.getInt(GRAMS);
            idFood =  savedInstanceState.getLong(FOOD_ID);
            idFoodOld =  savedInstanceState.getLong(OLD_FOOD_ID);
            isPersonalFood = savedInstanceState.getBoolean(IS_PERSONAL_FOOD);
            portion = savedInstanceState.getInt(PORTION);
            oil = savedInstanceState.getFloat(OIL);
            isUpdateFood = getArguments().getBoolean(IS_UPDATED_FOOD);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_balancer_plus, container, false);
        SharedPreferences prefs = getActivity().getSharedPreferences("myPreferences", Context.MODE_PRIVATE);
        df = new DecimalFormat("####.##");
        df.setDecimalFormatSymbols(MethodsUtil.setDecimalSeparator());
        dbManager = DBManager.getInstance(getActivity());
        extensionsBalancerPlus = dbManager.getExtensionsBalancerPlus(prefs.getLong(ID_EXTENSIONS_BALANCER_PLUS, 0));
        mAddFood = (Button) rootView.findViewById(R.id.button_add_food);
        mCreateFood = (Button) rootView.findViewById(R.id.button_create_food);
        mBalance = (Button) rootView.findViewById(R.id.button_balance);
        mCustom = (Button) rootView.findViewById(R.id.button_customize);
        mMore = (Button) rootView.findViewById(R.id.button_more);
        mMore.setBackgroundResource(R.drawable.ic_info_outline_white_48dp);
        mEditTextKcal = (EditText) rootView.findViewById(R.id.kcal_balance);
        mEditTextKcal.setEnabled(false);
        mEditTextKcal.setText("0");
        textViewBalance = (TextView) rootView.findViewById(R.id.text_view_balance);
        textViewBalance.setText(getString(R.string.empty_ingredients));
        mCompositionHydrates = (TextView) rootView.findViewById(R.id.composition_hydrates);
        mCompositionProtein = (TextView) rootView.findViewById(R.id.composition_protein);
        mCompositionFats = (TextView) rootView.findViewById(R.id.composition_fats);
        mPortion = (EditText) rootView.findViewById(R.id.portion);
        mRelationHydratesProtein = (TextView) rootView.findViewById(R.id.relation_hydrates_protein_value);
        mRelationFats = (TextView) rootView.findViewById(R.id.relation_fats_value);
        mOilValue = (TextView) rootView.findViewById(R.id.oil_value);
        mOilValue.setVisibility(View.INVISIBLE);
        if (0 != oil) {
            mOilValue.setVisibility(View.VISIBLE);
            mOilValue.setText(getString(R.string.oil_message_1) + " " + oil + " " + getString(R.string.oil_message_2));
        }

        mListViewFoods = (ListView) rootView.findViewById(R.id.list_view_foods);
        if (foodList == null ){
            foodList = new ArrayList<>();
        }
        if (0 == portion) {
            portion = 1;
        }
        mPortion.setText(String.valueOf(portion));
        if (-1 != grams) {
            Food foodOld = null;
            PersonalFood personalFoodOld = null;
            if (null != idFoodOld && 0 != idFoodOld ) {
                foodOld = dbManager.getFood(idFoodOld);
                personalFoodOld = dbManager.getPersonalFood(idFoodOld);
                if (foodOld != null && personalFoodOld != null) {
                    if (!foodList.contains(foodOld)) {
                        foodOld = null;
                    } else {
                        personalFoodOld = null;
                    }
                }
            }
            if (!isPersonalFood) {
                Food food = dbManager.getFood(idFood);
                if (food != null && (foodOld != null || personalFoodOld != null) && !isUpdateFood ) {
                    if ( foodOld != null) {
                        int gramsTotal = 0;
                        if (!foodList.contains(food)) {
                            gramsTotal = grams;
                        } else {
                            foodList.remove(food);
                            gramsTotal = food.getGramos() + grams;
                        }
                        food.setValorQp(Float.parseFloat(MethodsUtil.getValorQP(food, extensionsBalancerPlus)));
                        food.setValorQr(Float.parseFloat(MethodsUtil.getValorQR(food, extensionsBalancerPlus)));
                        food.setGramos(gramsTotal);
                        dbManager.updateFood(food);
                        foodList.add(food);
                    }
                } else if (food != null && (foodOld != null || personalFoodOld != null) && isUpdateFood) {
                    if (foodOld != null) {
                        if (food.equals(foodOld)) {
                            if (foodList.contains(food)) {
                                foodList.remove(food);
                            }
                            food.setValorQp(Float.parseFloat(MethodsUtil.getValorQP(food, extensionsBalancerPlus)));
                            food.setValorQr(Float.parseFloat(MethodsUtil.getValorQR(food, extensionsBalancerPlus)));
                            food.setGramos(grams);
                            dbManager.updateFood(food);
                            foodList.add(food);
                        } else {
                            if (foodList.contains(foodOld)) {
                                foodList.remove(foodOld);
                            }
                            int gramsTotal = 0;
                            if (!foodList.contains(food)) {
                                gramsTotal = grams;
                            } else {
                                foodList.remove(food);
                                gramsTotal = food.getGramos() + grams;
                            }
                            food.setValorQp(Float.parseFloat(MethodsUtil.getValorQP(food, extensionsBalancerPlus)));
                            food.setValorQr(Float.parseFloat(MethodsUtil.getValorQR(food, extensionsBalancerPlus)));
                            food.setGramos(gramsTotal);
                            dbManager.updateFood(food);
                            foodList.add(food);
                        }
                    } else if (personalFoodOld != null) {
                        if (foodList.contains(personalFoodOld)) {
                            foodList.remove(personalFoodOld);
                        }
                        int gramsTotal = 0;
                        if (!foodList.contains(food)) {
                            gramsTotal = grams;
                        } else {
                            foodList.remove(food);
                            gramsTotal = food.getGramos() + grams;
                        }
                        food.setValorQp(Float.parseFloat(MethodsUtil.getValorQP(food, extensionsBalancerPlus)));
                        food.setValorQr(Float.parseFloat(MethodsUtil.getValorQR(food, extensionsBalancerPlus)));
                        food.setGramos(gramsTotal);
                        dbManager.updateFood(food);
                        foodList.add(food);
                    }
                } else if (food != null && foodOld == null && personalFoodOld == null) {
                        if (foodList.contains(food)) {
                            foodList.remove(food);
                        }
                        food.setValorQp(Float.parseFloat(MethodsUtil.getValorQP(food, extensionsBalancerPlus)));
                        food.setValorQr(Float.parseFloat(MethodsUtil.getValorQR(food, extensionsBalancerPlus)));
                        food.setGramos(grams);
                        dbManager.updateFood(food);
                        foodList.add(food);
                }
            } else {
                PersonalFood personalFood = dbManager.getPersonalFood(idFood);
                if (personalFood != null && (foodOld != null || personalFoodOld != null) && !isUpdateFood ) {
                    int gramsTotal = 0;
                    if (!foodList.contains(personalFood)) {
                        gramsTotal = grams;
                    } else {
                        foodList.remove(personalFood);
                        gramsTotal = personalFood.getPeso_neto() + grams;
                    }
                    personalFood.setValorQp(Float.parseFloat(MethodsUtil.getValorQP(personalFood, extensionsBalancerPlus)));
                    personalFood.setValorQr(Float.parseFloat(MethodsUtil.getValorQR(personalFood, extensionsBalancerPlus)));
                    personalFood.setPeso_neto(gramsTotal);
                    dbManager.updatePersonalFood(personalFood);
                    foodList.add(personalFood);
                } else if (personalFood != null && (foodOld != null || personalFoodOld != null) && isUpdateFood) {
                    if (personalFoodOld != null) {
                        if (personalFood.equals(personalFoodOld)) {
                            if (foodList.contains(personalFood)) {
                                foodList.remove(personalFood);
                            }
                            personalFood.setValorQp(Float.parseFloat(MethodsUtil.getValorQP(personalFood, extensionsBalancerPlus)));
                            personalFood.setValorQr(Float.parseFloat(MethodsUtil.getValorQR(personalFood, extensionsBalancerPlus)));
                            personalFood.setPeso_neto(grams);
                            dbManager.updatePersonalFood(personalFood);
                            foodList.add(personalFood);
                        } else {
                            if (foodList.contains(personalFoodOld)) {
                                foodList.remove(personalFoodOld);
                            }
                            int gramsTotal = 0;
                            if (!foodList.contains(personalFood)) {
                                gramsTotal = grams;
                            } else {
                                foodList.remove(personalFood);
                                gramsTotal = personalFood.getPeso_neto() + grams;
                            }
                            personalFood.setValorQp(Float.parseFloat(MethodsUtil.getValorQP(personalFood, extensionsBalancerPlus)));
                            personalFood.setValorQr(Float.parseFloat(MethodsUtil.getValorQR(personalFood, extensionsBalancerPlus)));
                            personalFood.setPeso_neto(gramsTotal);
                            dbManager.updatePersonalFood(personalFood);
                            foodList.add(personalFood);
                        }
                    } else if (foodOld != null) {
                        if (foodList.contains(foodOld)) {
                            foodList.remove(foodOld);
                        }
                        int gramsTotal = 0;
                        if (!foodList.contains(personalFood)) {
                            gramsTotal = grams;
                        } else {
                            foodList.remove(personalFood);
                            gramsTotal = personalFood.getPeso_neto() + grams;
                        }
                        personalFood.setValorQp(Float.parseFloat(MethodsUtil.getValorQP(personalFood, extensionsBalancerPlus)));
                        personalFood.setValorQr(Float.parseFloat(MethodsUtil.getValorQR(personalFood, extensionsBalancerPlus)));
                        personalFood.setPeso_neto(gramsTotal);
                        dbManager.updatePersonalFood(personalFood);
                        foodList.add(personalFood);
                    }
                } else if (personalFood != null && personalFoodOld == null && foodOld == null) {
                    if (foodList.contains(personalFood)) {
                        foodList.remove(personalFood);
                    }
                    personalFood.setValorQp(Float.parseFloat(MethodsUtil.getValorQP(personalFood, extensionsBalancerPlus)));
                    personalFood.setValorQr(Float.parseFloat(MethodsUtil.getValorQR(personalFood, extensionsBalancerPlus)));
                    personalFood.setPeso_neto(grams);
                    dbManager.updatePersonalFood(personalFood);
                    foodList.add(personalFood);
                }
            }
        }

        if (foodList.size() > 0) {
            saveFoodList(foodList, Integer.parseInt(mPortion.getText().toString()), grams, idFood, idFoodOld, isPersonalFood, oil);
            calcComposition(foodList);
            loadComposition(extensionsBalancerPlus);
            loadBalance(extensionsBalancerPlus);
            loadRelations(extensionsBalancerPlus);
        } else {
            textViewBalance.setText(getString(R.string.empty_ingredients));
            mEditTextKcal.setText("0");
        }

        mPortion.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!("").equals(mPortion.getText().toString())) {
                    resetParams();
                    calcComposition(foodList);
                    loadComposition(extensionsBalancerPlus);
                    loadBalance(extensionsBalancerPlus);
                    loadRelations(extensionsBalancerPlus);
                }
            }
        });

        // Sets the data behind this ListView
        mListViewFoods.setAdapter(new FoodsAdpter(getActivity(), foodList, getActivity(), portion));

        mAddFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddFoodDialogFragment dialogAddFood = AddFoodDialogFragment.newInstance(null, Integer.parseInt((mPortion.getText().toString().equals(""))? "1":mPortion.getText().toString()), 0, foodList, false);
                dialogAddFood.show(getActivity().getSupportFragmentManager(), "dialog");
            }
        });

        mCreateFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateFoodDialogFragment dialogCreateFood = CreateFoodDialogFragment.newInstance(Integer.parseInt((mPortion.getText().toString().equals(""))? "1":mPortion.getText().toString()));
                dialogCreateFood.show(getActivity().getSupportFragmentManager(), "dialog");
            }
        });

        mMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (foodList.size() > 0) {
                    ShowInfoDialogFragment dialogShowInfo = ShowInfoDialogFragment.newInstance(hydrates, protein, fats, foodList, Integer.parseInt((mPortion.getText().toString().equals(""))? "1":mPortion.getText().toString()));
                    dialogShowInfo.show(getActivity().getSupportFragmentManager(), "dialog");
                } else {
                    Toast.makeText(getActivity(), getString(R.string.empty_ingredients), Toast.LENGTH_LONG).show();
                }

            }
        });

        mBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (foodList.size() > 0) {
                    if (!isBalance) {
                        BalanceDialogFragment dialogBalance = BalanceDialogFragment.newInstance(Integer.parseInt(mPortion.getText().toString()), proteinTotal, protein, hydrates, 0, false);
                        dialogBalance.show(getActivity().getSupportFragmentManager(), "dialog");
                    } else {
                        Toast.makeText(getActivity(), getString(R.string.is_balance), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getActivity(), getString(R.string.empty_ingredients), Toast.LENGTH_LONG).show();
                }
            }
        });
        mCustom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (foodList.size() > 0) {
                    CustomDialogFragment dialogCustom = CustomDialogFragment.newInstance(Integer.parseInt(mPortion.getText().toString()), Float.parseFloat(mEditTextKcal.getText().toString()), foodList);
                    dialogCustom.show(getActivity().getSupportFragmentManager(), "dialog");
                } else {
                    Toast.makeText(getActivity(), getString(R.string.empty_ingredients), Toast.LENGTH_LONG).show();
                }
            }
        });

        return rootView;
    }

    public void saveFoodList(ArrayList<Parcelable> foodList, int portion, int grams, long idFood, long idFoodOld, boolean isPersonalFood, float oil) {
        if (mListener != null) {
            mListener.saveFoodList(foodList, portion, grams, idFood, idFoodOld, isPersonalFood, oil);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        mListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
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
        void saveFoodList(ArrayList<Parcelable> foodList, int i, int portion, long idFood, long idFoodOld, boolean isPersonalFood, float oil);
    }

    private void resetParams() {
        quantity = 0;
        hydrates = 0;
        protein = 0;
        fats = 0;
        calory = 0;
        proteinTotal = 0;
        fatsTotal = 0;
        vdie1 = 0;
        vdie2 = 0;
    }


    private void calcComposition (List<Parcelable> parcelableList) {
        float hundred = 100;
        for (Parcelable parcelable : parcelableList){
            if (parcelable instanceof Food) {
                Food food = (Food) parcelable;
                quantity = food.getGramos() / hundred;
                hydrates += food.getHidratos_carbono() * quantity;
                protein += food.getProteinas() * quantity;
                fats += food.getGrasas() * quantity;
                calory += food.getCalorias() * quantity;
            } else {
                PersonalFood personalFood = (PersonalFood) parcelable;
                quantity = personalFood.getPeso_neto() / hundred;
                hydrates += personalFood.getHidratos_carbono() * quantity;
                protein += personalFood.getProteinas() * quantity;
                fats += personalFood.getGrasas() * quantity;
                calory += personalFood.getCalorias() * quantity;
            }
        }

        hydrates = hydrates / Float.parseFloat(mPortion.getText().toString());
        protein = protein / Float.parseFloat(mPortion.getText().toString());
        fats = fats / Float.parseFloat(mPortion.getText().toString());
        calory = calory / Float.parseFloat(mPortion.getText().toString());
    }

    private void loadComposition(ExtensionsBalancerPlus extensionsBalancerPlus) {
        float hydratesValues = 0;

        if (hydrates > 0){
            hydratesValues = (hydrates * extensionsBalancerPlus.getMultiplica()) / (hydrates + protein);
        }
        if (protein > 0) {
            proteinTotal = (protein * extensionsBalancerPlus.getMultiplica()) / (hydrates + protein);
        }
        if (fats > 0) {
            fatsTotal = (fats * extensionsBalancerPlus.getMultiplica()) / (hydrates + protein);
        }

        mCompositionHydrates.setText(df.format(hydratesValues));
        mCompositionProtein.setText(df.format(proteinTotal));
        mCompositionFats.setText(df.format(fatsTotal));
        mEditTextKcal.setText(df.format(calory));
    }

    private void loadBalance(ExtensionsBalancerPlus extensionsBalancerPlus) {
        vdie1 = MethodsUtil.roundDecimals((protein * extensionsBalancerPlus.getMultiplica()) / ((hydrates + protein) + 0.01F), 2);
        if (foodList.size() > 0) {
            if (vdie1 >= extensionsBalancerPlus.getLimite_hidratos() && vdie1 <= extensionsBalancerPlus.getLimite_proteinas()) {
                textViewBalance.setText(getString(R.string.balance_text));
                textViewBalance.setBackgroundColor(getResources().getColor(colorGreenApp));
                textViewBalance.setTextColor(getResources().getColor(colorWhite));
                isBalance = true;
            } else {
                textViewBalance.setText(getString(R.string.unbalanced));
                textViewBalance.setBackgroundColor(getResources().getColor(colorRed));
                textViewBalance.setTextColor(getResources().getColor(colorWhite));
                isBalance = false;
            }
        }
    }

    private void loadRelations(ExtensionsBalancerPlus extensionsBalancerPlus) {
        if (calory > 0) {
            vdie2 = MethodsUtil.roundDecimals((fats * extensionsBalancerPlus.getMultiplica()) / (hydrates + protein + 0.01F), 2);
            if (vdie1 >= 2.85 && vdie1 <= 3.15) {
                mRelationHydratesProtein.setText(getString(R.string.balance_balance));
            } else if (vdie1 >= 2.55 && vdie1 <= 2.84) {
                mRelationHydratesProtein.setText(getString(R.string.light_protein_deficit));
            } else if (vdie1 >= 2.10 && vdie1 <= 2.54) {
                mRelationHydratesProtein.setText(getString(R.string.medium_protein_deficit));
            } else if (vdie1 <= 2.09) {
                mRelationHydratesProtein.setText(getString(R.string.severe_protein_deficit));
            } else if (vdie1 >= 3.16 && vdie1 <= 3.45) {
                mRelationHydratesProtein.setText(getString(R.string.light_protein_excess));
            } else if (vdie1 >= 3.46 && vdie1 <= 3.90) {
                mRelationHydratesProtein.setText(getString(R.string.medium_protein_excess));
            } else if (vdie1 >= 3.91) {
                mRelationHydratesProtein.setText(getString(R.string.severe_protein_excess));
            }

            if (vdie2 >= 1.26 && vdie2 <= 1.40) {
                mRelationFats.setText(getString(R.string.balance_fast));
                mCompositionFats.setText(df.format(fatsTotal) + getString(R.string.equal_symbol));
            } else if (vdie2 >= 1.13 && vdie2 <= 1.25) {
                mRelationFats.setText(getString(R.string.light_fats_deficit));
                mCompositionFats.setText(df.format(fatsTotal) + getString(R.string.minus_symbol));
            } else if (vdie2 >= 0.93 && vdie2 <= 1.12) {
                mRelationFats.setText(getString(R.string.medium_fats_deficit));
                mCompositionFats.setText(df.format(fatsTotal) + getString(R.string.minus_symbol));
            } else if (vdie2 <= 0.92) {
                mRelationFats.setText(getString(R.string.severe_fats_deficit));
                mCompositionFats.setText(df.format(fatsTotal) + getString(R.string.minus_symbol));
            } else if (vdie2 >= 1.41 && vdie2 <= 1.53) {
                mRelationFats.setText(getString(R.string.light_fats_excess));
                mCompositionFats.setText(df.format(fatsTotal) + getString(R.string.plus_symbol));
            } else if (vdie2 >= 1.54 && vdie2 <= 1.73) {
                mRelationFats.setText(getString(R.string.medium_fats_excess));
                mCompositionFats.setText(df.format(fatsTotal) + getString(R.string.plus_symbol));
            } else if (vdie2 >= 1.74) {
                mRelationFats.setText(getString(R.string.severe_fats_excess));
                mCompositionFats.setText(df.format(fatsTotal) + getString(R.string.plus_symbol));
            }
        }
    }

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(FOOD_ID, idFood);
        outState.putLong(OLD_FOOD_ID, idFoodOld);
        outState.putBoolean(IS_PERSONAL_FOOD, isPersonalFood);
        outState.putInt(PORTION, portion);
        outState.putFloat(OIL, oil);
        outState.putParcelableArrayList(FOOD_LIST, foodList);
        outState.putInt(GRAMS, -1);
        outState.putBoolean(IS_UPDATED_FOOD, isUpdateFood);
    }
}
