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

public class ShowInfoDialogFragment extends DialogFragment {
    private static final String ID_EXTENSIONS_BALANCER_PLUS = "idExtensionsBalancerPlus";
    private static final String FOOD_LIST = "food_list";
    private static final String PROTEIN = "protein";
    private static final String HYDRATES = "hydrates";
    private static final String PORTION = "portion";
    private static final String FATS = "fats";
    public static final int THE_SIZE = 20;
    private int portion;
    private ArrayList<Parcelable> foodList;
    private DBManager dbManager;
    private ExtensionsBalancerPlus extensionsBalancerPlus;
    private float fats;
    private float protein;
    private float hydrates;
    private float fibers;
    private float iron;
    private float calcium;
    private float vitamin_a;
    private float vitamin_e;
    private float vitamin_b1;
    private float vitamin_c;
    private float rivoflavin;
    private float niacin;
    private TextView mHydrates;
    private TextView mProtein;
    private TextView mFats;
    private TextView mFibers;
    private TextView mIron;
    private TextView mCalcium;
    private TextView mVitamin_a;
    private TextView mVitamin_e;
    private TextView mVitamin_b1;
    private TextView mVitamin_c;
    private TextView mRivoflavin;
    private TextView mNiacin;

    public ShowInfoDialogFragment( float hydrates, float protein, float fats, ArrayList<Parcelable> foodList, int portion) {
        this.hydrates = hydrates;
        this.protein = protein;
        this.fats = fats;
        this.foodList = foodList;
        this.portion = portion;
    }

    public ShowInfoDialogFragment() {
    }

    public static ShowInfoDialogFragment newInstance(float hydrates, float protein, float fats, ArrayList<Parcelable> foodList, int portion){
        ShowInfoDialogFragment fragment = new ShowInfoDialogFragment(hydrates, protein, fats, foodList, portion);
        return fragment;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        if (null != savedInstanceState) {
            this.hydrates = savedInstanceState.getFloat(HYDRATES);
            this.protein = savedInstanceState.getFloat(PROTEIN);
            this.fats = savedInstanceState.getFloat(FATS);
            this.foodList = savedInstanceState.getParcelableArrayList(FOOD_LIST);
            this.portion = savedInstanceState.getInt(PORTION);
        }

        final View rootViewDialog = inflater.inflate(R.layout.dialog_show_info, null);
        SharedPreferences prefs = getActivity().getSharedPreferences("myPreferences", Context.MODE_PRIVATE);
        dbManager = DBManager.getInstance(getActivity());
        extensionsBalancerPlus = dbManager.getExtensionsBalancerPlus(prefs.getLong(ID_EXTENSIONS_BALANCER_PLUS, 0));

        mHydrates = (TextView) rootViewDialog.findViewById(R.id.hydrates_text);
        mProtein = (TextView) rootViewDialog.findViewById(R.id.protein_text);
        mFats = (TextView) rootViewDialog.findViewById(R.id.fats_text);
        mFibers = (TextView) rootViewDialog.findViewById(R.id.fibers_text);
        mIron = (TextView) rootViewDialog.findViewById(R.id.iron_text);
        mCalcium = (TextView) rootViewDialog.findViewById(R.id.calcium_text);
        mVitamin_a = (TextView) rootViewDialog.findViewById(R.id.vitamin_a_text);
        mVitamin_e = (TextView) rootViewDialog.findViewById(R.id.vitamin_e_text);
        mVitamin_b1 = (TextView) rootViewDialog.findViewById(R.id.vitamin_b1_text);
        mVitamin_c = (TextView) rootViewDialog.findViewById(R.id.vitamin_c_text);
        mRivoflavin = (TextView) rootViewDialog.findViewById(R.id.rivoflavin_text);
        mNiacin = (TextView) rootViewDialog.findViewById(R.id.niacin_text);

        mHydrates.setText(getString(R.string.prompt_hydrates) + ": " + String.valueOf(MethodsUtil.roundDecimals(hydrates, 2)) + " grs  ");
        mProtein.setText(getString(R.string.prompt_protein) + ": " + String.valueOf(MethodsUtil.roundDecimals(protein, 2)) + " grs");
        mFats.setText(getString(R.string.prompt_fats) + ": " + String.valueOf(MethodsUtil.roundDecimals(fats, 2)) + " grs  ");
        calcComposition();
        mFibers.setText(getString(R.string.fibers) + ": " + String.valueOf(MethodsUtil.roundDecimals(fibers, 2)) + " grs");
        mIron.setText(getString(R.string.iron) + ": " + String.valueOf(MethodsUtil.roundDecimals(iron, 2)) + " mgs  ");
        mCalcium.setText(getString(R.string.calcium) + ": " + String.valueOf(MethodsUtil.roundDecimals(calcium, 2)) + " mgs");
        mVitamin_a.setText(getString(R.string.vitamin_a) + ": " + String.valueOf(MethodsUtil.roundDecimals(vitamin_a, 2)) + " ug  ");
        mVitamin_e.setText(getString(R.string.vitamin_e) + ": " + String.valueOf(MethodsUtil.roundDecimals(vitamin_e, 2)) + " ug");
        mVitamin_b1.setText(getString(R.string.prompt_vitamin_b1) + ": " + String.valueOf(MethodsUtil.roundDecimals(vitamin_b1, 2)) + " mgs  ");
        mRivoflavin.setText(getString(R.string.rivoflavin) + ": " + String.valueOf(MethodsUtil.roundDecimals(rivoflavin, 2)) + " mgs");
        mNiacin.setText(getString(R.string.niacin) + ": " + String.valueOf(MethodsUtil.roundDecimals(niacin, 2)) + " mgs  ");
        mVitamin_c.setText(getString(R.string.prompt_vitamin_c) + ": " + String.valueOf(MethodsUtil.roundDecimals(vitamin_c, 2)) + " mgs");

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout

        builder.setView(rootViewDialog).setTitle(R.string.composition_pro).setIcon(R.drawable.icon_dyetica)
                // Add action buttons
                .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
        if (MethodsUtil.isTablet(getContext())) {
            // Positive
            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextSize(THE_SIZE);
        }
        return dialog;
    }

    private void calcComposition() {
        for (Parcelable parcelable : foodList) {
            if (parcelable instanceof Food) {
                Food food = (Food) parcelable;
                float quantity = food.getGramos() / 100F;
                fibers += food.getFibras() * quantity;
                iron += food.getHierro() * quantity;
                calcium += food.getCalcio() * quantity;
                vitamin_a += food.getVitamina_a() * quantity;
                vitamin_e += food.getVitamina_e() * quantity;
                vitamin_b1 += food.getVitamina_b1() * quantity;
                vitamin_c += food.getVitamina_c() * quantity;
                rivoflavin += food.getRivoflavina() * quantity;
                niacin += food.getNiacina() * quantity;
            }
        }
        fibers = fibers / portion;
        iron = iron / portion;
        calcium = calcium / portion;
        vitamin_a = vitamin_a / portion;
        vitamin_e = vitamin_e / portion;
        vitamin_b1 = vitamin_b1 / portion;
        vitamin_c = vitamin_c / portion;
        rivoflavin = rivoflavin / portion;
        niacin = niacin / portion;
    }

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putFloat(FATS, fats);
        outState.putFloat(PROTEIN, protein);
        outState.putFloat(HYDRATES, hydrates);
        outState.putInt(PORTION, portion);
        outState.putParcelableArrayList(FOOD_LIST, foodList);
    }
}
