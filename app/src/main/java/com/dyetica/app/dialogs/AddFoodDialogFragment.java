package com.dyetica.app.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jess on 02/01/2017.
 */

public class AddFoodDialogFragment extends DialogFragment {
    private static final String ID_EXTENSIONS_BALANCER_PLUS = "idExtensionsBalancerPlus";
    private static final String PORTION = "portion";
    private static final String PARCEABLE_FOOD = "parceable_food";
    private static final String FOOD = "food_selectSearch";
    private static final int THE_SIZE = 20;
    private List<Parcelable> foodListOld;
    private Spinner mSpinnerCategories;
    private Spinner mSpinnerFoods;
    private DBManager dbManager;
    private ExtensionsBalancerPlus extensionsBalancerPlus;
    private List<String> listCategories;
    private EditText mGrams;
    private AutoCompleteTextView mFoodAllAutoComplete;
    private Food foodSelectSearch = null;
    private Map<String, String> categories;
    private ArrayAdapter mArrayAdapterCategories;
    private Parcelable parcelableFood;
    private boolean isUpdate = false;
    private int portion;
    private float oil;
    private boolean isUpdateFood;

    OnAddFoodDialogSelectedListener mCallback;
    private TabHost tabs;
    private EditText mGramsByName;

    public AddFoodDialogFragment(Parcelable parcelable, int portion, float oil, List<Parcelable> foodListOld, boolean isUpdateFood) {
        this.parcelableFood = parcelable;
        this.portion = portion;
        this.oil = oil;
        this.foodListOld = foodListOld;
        this.isUpdateFood = isUpdateFood;
    }

    public AddFoodDialogFragment() {
    }

    // Container Activity must implement this interface
    public interface OnAddFoodDialogSelectedListener {
        void onFoodAndGramsSelected(Long idFood, Long idFoodOld, int grams, boolean isPersonalFood, int portion, float oil, boolean isUpdateFood);
    }

    public static AddFoodDialogFragment newInstance(Parcelable parcelable, int portion, float oil, List<Parcelable> foodListOld, boolean isUpdateFood){
        AddFoodDialogFragment fragment = new AddFoodDialogFragment(parcelable, portion, oil, foodListOld, isUpdateFood);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnAddFoodDialogSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }


    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        final View rootViewDialog = inflater.inflate(R.layout.dialog_add_food, null);
        if (null != savedInstanceState) {
            this.portion = savedInstanceState.getInt(PORTION);
            this.parcelableFood = savedInstanceState.getParcelable(PARCEABLE_FOOD);
            this.foodSelectSearch = savedInstanceState.getParcelable(FOOD);
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
        mSpinnerCategories = (Spinner) rootViewDialog.findViewById(R.id.spinner_categories_food);
        mSpinnerFoods =  (Spinner) rootViewDialog.findViewById(R.id.spinner_food);
        mArrayAdapterCategories = initSpinnerListCategories(extensionsBalancerPlus);
        mSpinnerCategories.setAdapter(mArrayAdapterCategories);
        mGrams = (EditText) rootViewDialog.findViewById(R.id.edit_text_grams);
        mGrams.clearFocus();
        mGramsByName = (EditText) rootViewDialog.findViewById(R.id.edit_text_grams2);
        mFoodAllAutoComplete = (AutoCompleteTextView) rootViewDialog.findViewById(R.id.foodAllAutoComplete);
        mFoodAllAutoComplete.setAdapter(initFoodAutoComplete());

        if (null != parcelableFood){
            updateElements(parcelableFood, extensionsBalancerPlus);
        }

        mSpinnerCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String categorySelected = parent.getItemAtPosition(position).toString();
                if (!isUpdate) {
                    if (position < mSpinnerCategories.getAdapter().getCount() - 4) {
                        mSpinnerFoods.setAdapter(initSpinnerListFoods(extensionsBalancerPlus, categorySelected));
                    } else {
                        mSpinnerFoods.setAdapter(initSpinnerListPersonalFoods(extensionsBalancerPlus, position));
                    }
                } else {
                    isUpdate = false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        tabs.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if ("AddFoodByCategory".equals(tabs.getCurrentTabTag())) {
                    mGrams.clearFocus();
                }
            }
        });

        mFoodAllAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                foodSelectSearch = (Food) mFoodAllAutoComplete.getAdapter().getItem(position);
            }
        });

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(rootViewDialog).setTitle((null != parcelableFood)? R.string.update_food:R.string.add_food).setIcon(R.drawable.icon_dyetica)
                // Add action buttons
                .setPositiveButton((null != parcelableFood)? R.string.update:R.string.add, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Food foodSelectedOld = null;
                        PersonalFood personalFoodOld =  null;
                        mGrams.clearFocus();

                        if (parcelableFood instanceof Food) {
                            foodSelectedOld = (Food) parcelableFood;
                        } else {
                            personalFoodOld = (PersonalFood) parcelableFood;
                        }
                        Long idFood = null;
                        if (null != foodSelectedOld){
                            idFood =  foodSelectedOld.getId();
                        } else if (null != personalFoodOld) {
                            idFood = personalFoodOld.getId();
                        }
                        if ("AddFoodByCategory".equals(tabs.getCurrentTabTag())) {
                            if (!mGrams.getText().toString().equals("") && !mSpinnerFoods.getSelectedItem().toString().equals("No hay alimentos")) {
                                if (mSpinnerFoods.getSelectedItem() instanceof Food) {
                                    Food foodSelected = (Food) mSpinnerFoods.getSelectedItem();
                                    if (foodListOld != null && foodListOld.contains(foodSelected)) {
                                        idFood = foodSelected.getId();
                                    }
                                    mCallback.onFoodAndGramsSelected(foodSelected.getId(),
                                            idFood,
                                            Integer.parseInt(mGrams.getText().toString()),
                                            false,
                                            portion,
                                            oil,
                                            isUpdateFood);
                                } else {
                                    PersonalFood personalFoodSelected = (PersonalFood) mSpinnerFoods.getSelectedItem();
                                    if (foodListOld != null && foodListOld.contains(personalFoodSelected)) {
                                        idFood = personalFoodSelected.getId();
                                    }
                                    mCallback.onFoodAndGramsSelected(personalFoodSelected.getId(),
                                            idFood,
                                            Integer.parseInt(mGrams.getText().toString()),
                                            true,
                                            portion,
                                            oil,
                                            isUpdateFood);
                                }
                            } else {
                                Toast.makeText(getActivity(), getString(R.string.error_dialog_add_food), Toast.LENGTH_LONG).show();
                            }
                        } else {
                            if (!"".equals(mGramsByName.getText().toString()) && foodSelectSearch != null && !"".equals(mFoodAllAutoComplete.getText().toString())) {
                                mCallback.onFoodAndGramsSelected(foodSelectSearch.getId(), idFood, Integer.parseInt(mGramsByName.getText().toString()), false, portion, oil, isUpdateFood);
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

    private ArrayAdapter initFoodAutoComplete() {
        List<Food> foods;
        foods = dbManager.getFoods();
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

    private void updateElements(Parcelable parcelableFood, ExtensionsBalancerPlus extensionsBalancerPlus) {
        if (parcelableFood instanceof Food) {
            Food foodSelected = (Food) parcelableFood;
            String category = categories.get(foodSelected.getGrupo_alimentos());
            mSpinnerCategories.setSelection(mArrayAdapterCategories.getPosition(category));
            ArrayAdapter mArrayAdapterFoods = initSpinnerListFoods(extensionsBalancerPlus, category);
            mSpinnerFoods.setAdapter(mArrayAdapterFoods);
            mSpinnerFoods.setSelection(mArrayAdapterFoods.getPosition(foodSelected));
            mGrams.setText(String.valueOf(foodSelected.getGramos()));
            isUpdate = true;
        } else {
            PersonalFood personalFoodSelected = (PersonalFood) parcelableFood;
            int location = personalFoodSelected.getUbicacion();
            mSpinnerCategories.setSelection(location + 17);
            ArrayAdapter mArrayAdapterFoods = initSpinnerListPersonalFoods(extensionsBalancerPlus, location + 17);
            mSpinnerFoods.setAdapter(mArrayAdapterFoods);
            mSpinnerFoods.setSelection(mArrayAdapterFoods.getPosition(personalFoodSelected));
            mGrams.setText(String.valueOf(personalFoodSelected.getPeso_neto()));
            isUpdate = true;
        }
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

        listCategories.add(getString(R.string.complements));
        listCategories.add(getString(R.string.precooked));
        listCategories.add(getString(R.string.my_combined));
        listCategories.add(getString(R.string.desserts));

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

        foods = dbManager.getFoodByCategory((null != category) ? categoriesString.substring(0, categoriesString.length() - 3) : category);
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

    private ArrayAdapter initSpinnerListPersonalFoods(ExtensionsBalancerPlus extensionsBalancerPlus, int location) {
        List<PersonalFood> personalFoods;
        personalFoods = dbManager.getPersonalFoodByLocation(location - 17);
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

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(PORTION, portion);
        outState.putParcelable(PARCEABLE_FOOD , parcelableFood);
        outState.putParcelable(FOOD, foodSelectSearch);
    }

}
