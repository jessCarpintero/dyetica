package com.dyetica.app.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.dyetica.app.R;
import com.dyetica.app.model.ExtensionsBalancerPlus;
import com.dyetica.app.model.Food;
import com.dyetica.app.model.PersonalFood;
import com.dyetica.app.persistence.DBManager;
import com.dyetica.app.utils.MethodsUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BalanceDialogFragment extends DialogFragment {
    private static final String ID_EXTENSIONS_BALANCER_PLUS = "idExtensionsBalancerPlus";
    private static final String PORTION = "portion";
    private static final String PROTEIN_TOTAL = "protein_total";
    private static final String PROTEIN = "protein";
    private static final String HYDRATES = "hydrates";
    private static final String FOOD = "food_selectSearch";
    private static final int THE_SIZE = 20;
    private boolean isUpdateFood;
    private Spinner mSpinnerCategories;
    private Spinner mSpinnerFoods;
    private long idExtensionsBalancerPlus;
    private DBManager dbManager;
    private ExtensionsBalancerPlus extensionsBalancerPlus;
    private List<String> listCategories;
    private EditText mGrams;
    private Map<String, String> categories;
    private ArrayAdapter mArrayAdapterCategories;
    private boolean isUpdate = false;
    private float proteinTotal;
    private float protein;
    private float hydrates;
    private float oil;
    private int portion;
    private Food foodSelectSearch = null;

    OnBalanceDialogSelectedListener mCallback;
    private TabHost tabs;
    private EditText mGramsByName;
    private AutoCompleteTextView mFoodAllAutoComplete;

    // Container Activity must implement this interface
    public interface OnBalanceDialogSelectedListener {
        void onFoodAndGramsSelected(Long idFood, Long idFoodOld, int grams, boolean isPersonalFood, int portion, float oil, boolean isUpdateFood);
    }

    public BalanceDialogFragment() {
    }

    public BalanceDialogFragment(int portion, float proteinTotal, float protein, float hydrates, float oil, boolean isUpdateFood) {
        this.portion = portion;
        this.proteinTotal = proteinTotal;
        this.protein = protein;
        this.hydrates = hydrates;
        this.oil = oil;
        this.isUpdateFood = isUpdateFood;
    }

    public static BalanceDialogFragment newInstance(int portion, float proteinTotal, float protein, float hydrates, float oil, boolean isUpdateFood){
        BalanceDialogFragment fragment = new BalanceDialogFragment(portion, proteinTotal, protein, hydrates, oil, isUpdateFood);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnBalanceDialogSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }



    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        final View rootViewDialog = inflater.inflate(R.layout.dialog_add_food, null);
        if (null != savedInstanceState) {
            this.portion = savedInstanceState.getInt(PORTION);
            this.proteinTotal = savedInstanceState.getFloat(PROTEIN_TOTAL);
            this.protein = savedInstanceState.getFloat(PROTEIN);
            this.foodSelectSearch = savedInstanceState.getParcelable(FOOD);
            this.hydrates = savedInstanceState.getFloat(HYDRATES);
        }
        SharedPreferences prefs = getActivity().getSharedPreferences("myPreferences", Context.MODE_PRIVATE);
        tabs= (TabHost) rootViewDialog.findViewById(R.id.tabHost);

        tabs.setup();

        TabHost.TabSpec tabpage1 = tabs.newTabSpec("AddFoodByCategory");
        tabpage1.setContent(R.id.add_food_by_category);
        tabpage1.setIndicator("Por categoria", getResources().getDrawable(R.drawable.ic_create_black_48dp));

        TabHost.TabSpec tabpage2 = tabs.newTabSpec("AddFoodByName");
        tabpage2.setContent(R.id.add_food_by_name);
        tabpage2.setIndicator("Por nombre", getResources().getDrawable(R.drawable.ic_create_black_48dp));

        tabs.addTab(tabpage1);
        tabs.addTab(tabpage2);

        if (!MethodsUtil.isTablet(getContext())) {
            tabs.getTabWidget().getChildAt(0).getLayoutParams().height = (int) (35 * this.getResources().getDisplayMetrics().density);
            tabs.getTabWidget().getChildAt(0).setPadding(0,0,0,0);

            tabs.getTabWidget().getChildAt(1).getLayoutParams().height = (int) (35 * this.getResources().getDisplayMetrics().density);
            tabs.getTabWidget().getChildAt(1).setPadding(0,0,0,0);

        } else {
            TextView title =(TextView) tabs.getTabWidget().getChildAt(0).findViewById(android.R.id.title);
            title.setTextSize(17);

            TextView title2 =(TextView) tabs.getTabWidget().getChildAt(1).findViewById(android.R.id.title);
            title2.setTextSize(17);
        }

        categories = new HashMap<>();
        dbManager = DBManager.getInstance(getActivity());
        extensionsBalancerPlus = dbManager.getExtensionsBalancerPlus(prefs.getLong(ID_EXTENSIONS_BALANCER_PLUS, 0));
        mSpinnerCategories = (Spinner) rootViewDialog.findViewById(R.id.spinner_categories_food);
        mSpinnerFoods =  (Spinner) rootViewDialog.findViewById(R.id.spinner_food);
        mArrayAdapterCategories = initSpinnerListCategories(extensionsBalancerPlus);
        mSpinnerCategories.setAdapter(mArrayAdapterCategories);
        mGrams = (EditText) rootViewDialog.findViewById(R.id.edit_text_grams);
        mGrams.clearFocus();
        mGramsByName = (EditText) rootViewDialog.findViewById(R.id.edit_text_grams2);
        mFoodAllAutoComplete = (AutoCompleteTextView) rootViewDialog.findViewById(R.id.foodAllAutoComplete);
        mFoodAllAutoComplete.setAdapter(initFoodAutoComplete(extensionsBalancerPlus));

        tabs.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if ("AddFoodByCategory".equals(tabs.getCurrentTabTag())) {
                    mGrams.clearFocus();
                }
            }
        });


        mSpinnerCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String categorySelected = parent.getItemAtPosition(position).toString();
                mSpinnerFoods.setAdapter(initSpinnerListFoods(extensionsBalancerPlus, categorySelected));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mSpinnerFoods.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Food foodSelected = (Food) parent.getSelectedItem();
                mGrams.setText(calcGramsFood(foodSelected, extensionsBalancerPlus));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mFoodAllAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                foodSelectSearch = (Food) mFoodAllAutoComplete.getAdapter().getItem(position);
                mGramsByName.setText(calcGramsFood(foodSelectSearch, extensionsBalancerPlus));
            }
        });

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(rootViewDialog).setTitle(R.string.balance_ingredients).setIcon(R.drawable.icon_dyetica)
                // Add action buttons
                .setPositiveButton(R.string.balance, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if ("AddFoodByCategory".equals(tabs.getCurrentTabTag())) {
                            if (!mGrams.getText().toString().equals("") && !mSpinnerFoods.getSelectedItem().toString().equals("No hay alimentos")) {
                                if (mSpinnerFoods.getSelectedItem() instanceof Food) {
                                    Food foodSelected = (Food) mSpinnerFoods.getSelectedItem();
                                    mCallback.onFoodAndGramsSelected(foodSelected.getId(), null, Integer.parseInt(mGrams.getText().toString()), false, portion, oil, isUpdateFood);
                                }
                            } else {
                                Toast.makeText(getActivity(), getString(R.string.error_dialog_add_food), Toast.LENGTH_LONG).show();
                            }
                        } else {
                            if (!"".equals(mGramsByName.getText()) && foodSelectSearch != null && !"".equals(mFoodAllAutoComplete.getText().toString())) {
                                mCallback.onFoodAndGramsSelected(foodSelectSearch.getId(), null, Integer.parseInt(mGramsByName.getText().toString()), false, portion, oil, isUpdateFood);
                            } else {
                                Toast.makeText(getActivity(), getString(R.string.error_dialog_add_food), Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
        if (MethodsUtil.isTablet(getContext())) {
            // Positive
            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextSize(THE_SIZE);
            // Negative
            dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextSize(THE_SIZE);
        }
        return dialog;
    }


    private ArrayAdapter initFoodAutoComplete(ExtensionsBalancerPlus extensionsBalancerPlus) {
        List<Food> foods;
        foods = dbManager.getFoodByBalance(extensionsBalancerPlus, proteinTotal);
        if (!foods.isEmpty()){
            for (Food food : foods) {
                food.setValorQp(Float.parseFloat(MethodsUtil.getValorQP(food, extensionsBalancerPlus)));
                food.setValorQr(Float.parseFloat(MethodsUtil.getValorQR(food, extensionsBalancerPlus)));
            }
        } else {
            Food food = new Food();
            food.setDescripcion_alimento(getString(R.string.not_food));
            foods.add(food);
        }

        ArrayAdapter adapterSppiner = new ArrayAdapter(getActivity(),
                R.layout.spinner_item, foods);
        return adapterSppiner;

    }

    private void addCategoryInCategories(String category, String categoryName){
        category = category.replace(" ", "");
        String[] categoriesAux = category.split(",");
        for (int i = 0; i < categoriesAux.length; i++){
            categories.put(categoriesAux[i], categoryName);
        }
    }

    private ArrayAdapter initSpinnerListCategories(ExtensionsBalancerPlus extensionsBalancerPlus) {
        listCategories =  new ArrayList<>();
        listCategories.add(getString(R.string.fruit));
        addCategoryInCategories(extensionsBalancerPlus.getFrutas(), getString(R.string.fruit));
        listCategories.add(getString(R.string.drinks));
        addCategoryInCategories(extensionsBalancerPlus.getBebidas(), getString(R.string.drinks));
        listCategories.add(getString(R.string.vegetables));
        addCategoryInCategories(extensionsBalancerPlus.getVerduras(), getString(R.string.vegetables));
        listCategories.add(getString(R.string.pulse));
        addCategoryInCategories(extensionsBalancerPlus.getLegumbres(), getString(R.string.pulse));
        listCategories.add(getString(R.string.estate));
        addCategoryInCategories(extensionsBalancerPlus.getRaices(), getString(R.string.estate));
        listCategories.add(getString(R.string.cereals));
        addCategoryInCategories(extensionsBalancerPlus.getCereales(), getString(R.string.cereals));
        listCategories.add(getString(R.string.fats));
        addCategoryInCategories(extensionsBalancerPlus.getGrasas(), getString(R.string.fats));
        listCategories.add(getString(R.string.sauces));
        addCategoryInCategories(extensionsBalancerPlus.getSalsas(), getString(R.string.sauces));
        listCategories.add(getString(R.string.dairy_products));
        addCategoryInCategories(extensionsBalancerPlus.getLacteos(), getString(R.string.dairy_products));
        listCategories.add(getString(R.string.eggs));
        addCategoryInCategories(extensionsBalancerPlus.getHuevos(), getString(R.string.eggs));
        listCategories.add(getString(R.string.meats));
        addCategoryInCategories(extensionsBalancerPlus.getCarnes(), getString(R.string.meats));
        listCategories.add(getString(R.string.fish));
        addCategoryInCategories(extensionsBalancerPlus.getPescados(), getString(R.string.fish));
        listCategories.add(getString(R.string.seafood));
        addCategoryInCategories(extensionsBalancerPlus.getMariscos(), getString(R.string.seafood));
        listCategories.add(getString(R.string.sweet));
        addCategoryInCategories(extensionsBalancerPlus.getDulces(), getString(R.string.sweet));
        listCategories.add(getString(R.string.nuts));
        addCategoryInCategories(extensionsBalancerPlus.getFrutos_secos(), getString(R.string.nuts));
        listCategories.add(getString(R.string.sausages));
        addCategoryInCategories(extensionsBalancerPlus.getEmbutidos(), getString(R.string.sausages));
        listCategories.add(getString(R.string.other));
        addCategoryInCategories(extensionsBalancerPlus.getOtros(), getString(R.string.other));

        ArrayAdapter adapterSppiner = new ArrayAdapter(getActivity(),
                R.layout.spinner_item, listCategories);
        return  adapterSppiner;
    }

    private ArrayAdapter initSpinnerListFoods(ExtensionsBalancerPlus extensionsBalancerPlus, String categorySelection) {
        String category = getCategory(extensionsBalancerPlus, categorySelection);
        List<Food> foods;
        String categoriesString = "\"";
        if (category != null) {
            category = category.replace(" ", "");
            String[] categories = category.split(",");
            for (int i = 0; i < categories.length; i++){
                categoriesString += categories[i] + "\", \"";
            }
        }

        foods = dbManager.getFoodByCategoryAndBalance((null != category) ? categoriesString.substring(0, categoriesString.length() - 3) : category, extensionsBalancerPlus, proteinTotal);
        if (!foods.isEmpty()){
            for (Food food : foods) {
                food.setValorQp(Float.parseFloat(MethodsUtil.getValorQP(food, extensionsBalancerPlus)));
                food.setValorQr(Float.parseFloat(MethodsUtil.getValorQR(food, extensionsBalancerPlus)));
            }
        } else {
            Food food = new Food();
            food.setDescripcion_alimento(getString(R.string.not_food));
            foods.add(food);
        }

        ArrayAdapter adapterSppiner = new ArrayAdapter(getActivity(),
                R.layout.spinner_item, foods);
        return  adapterSppiner;
    }

    private String getCategory(ExtensionsBalancerPlus extensionsBalancerPlus, String categorySelection){
        String category = null;

        final String OTHER = getString(R.string.other);
        final String FRUIT = getString(R.string.fruit);
        final String FATS = getString(R.string.fats);
        final String VEGETABLES = getString(R.string.vegetables);
        final String ESTATE = getString(R.string.estate);
        final String MEATS = getString(R.string.meats);
        final String SAUSAGES = getString(R.string.sausages);
        final String SEAFOOD = getString(R.string.seafood);
        final String PULSE = getString(R.string.pulse);
        final String FISH = getString(R.string.fish);
        final String CEREALS = getString(R.string.cereals);
        final String SWEET = getString(R.string.sweet);
        final String DRINKS = getString(R.string.drinks);
        final String NUTS = getString(R.string.nuts);
        final String DAIRY_PRODUCTS = getString(R.string.dairy_products);
        final String SAUCES = getString(R.string.sauces);
        final String EGGS = getString(R.string.eggs);

        if (OTHER.equals(categorySelection)) {
            category = extensionsBalancerPlus.getOtros();
        } else if (FRUIT.equals(categorySelection)) {
            category = extensionsBalancerPlus.getFrutas();
        } else if (FATS.equals(categorySelection)) {
            category = extensionsBalancerPlus.getGrasas();
        } else if (VEGETABLES.equals(categorySelection)) {
            category = extensionsBalancerPlus.getVerduras();
        } else if (ESTATE.equals(categorySelection)) {
            category = extensionsBalancerPlus.getRaices();
        } else if (MEATS.equals(categorySelection)) {
            category = extensionsBalancerPlus.getCarnes();
        } else if (SAUSAGES.equals(categorySelection)) {
            category = extensionsBalancerPlus.getEmbutidos();
        } else if (SEAFOOD.equals(categorySelection)) {
            category = extensionsBalancerPlus.getMariscos();
        } else if (PULSE.equals(categorySelection)) {
            category = extensionsBalancerPlus.getLegumbres();
        } else if (FISH.equals(categorySelection)) {
            category = extensionsBalancerPlus.getPescados();
        } else if (CEREALS.equals(categorySelection)) {
            category = extensionsBalancerPlus.getCereales();
        } else if (SWEET.equals(categorySelection)) {
            category = extensionsBalancerPlus.getDulces();
        } else if (DRINKS.equals(categorySelection)) {
            category = extensionsBalancerPlus.getBebidas();
        } else if (NUTS.equals(categorySelection)) {
            category = extensionsBalancerPlus.getFrutos_secos();
        } else if (DAIRY_PRODUCTS.equals(categorySelection)) {
            category = extensionsBalancerPlus.getLacteos();
        } else if (SAUCES.equals(categorySelection)) {
            category = extensionsBalancerPlus.getSalsas();
        } else if (EGGS.equals(categorySelection)) {
            category = extensionsBalancerPlus.getHuevos();
        }
        return category;
    }

    private String calcGramsFood(Food foodSelected, ExtensionsBalancerPlus extensionsBalancerPlus) {
        DecimalFormat df = new DecimalFormat("###");
        df.setDecimalFormatSymbols(MethodsUtil.setDecimalSeparator());
        float grams = 0, dh = 0, eh = 0, hydratesTotal = 0, proteinFoodsTotal = 0, dp = 0, ep = 0;
        hydratesTotal = hydrates * portion;
        proteinFoodsTotal = protein * portion;
        if (proteinTotal > extensionsBalancerPlus.getLimite_proteinas()) {
            dh = ((4 * proteinFoodsTotal) / 3) - hydratesTotal;
            eh = foodSelected.getHidratos_carbono() - 4 * foodSelected.getProteinas() / 3;
            grams = dh / eh * 100;
        } else {
            dp = ((3 * hydratesTotal) / 4) - proteinFoodsTotal;
            ep = foodSelected.getProteinas() - 3 * foodSelected.getHidratos_carbono() / 4;
            grams = dp / ep * 100;
        }

        return df.format(grams);
    }

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(PORTION, portion);
        outState.putFloat(PROTEIN_TOTAL, proteinTotal);
        outState.putFloat(PROTEIN, protein);
        outState.putFloat(HYDRATES, hydrates);
        outState.putParcelable(FOOD, foodSelectSearch);
    }
}
