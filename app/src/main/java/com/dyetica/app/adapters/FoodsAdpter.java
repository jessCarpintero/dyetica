package com.dyetica.app.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dyetica.app.R;
import com.dyetica.app.dialogs.AddFoodDialogFragment;
import com.dyetica.app.fragments.BalancerPlusFragment;
import com.dyetica.app.model.ExtensionsBalancerPlus;
import com.dyetica.app.model.Food;
import com.dyetica.app.model.PersonalFood;
import com.dyetica.app.persistence.DBManager;
import com.dyetica.app.utils.MethodsUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jess on 01/01/2017.
 */

public class FoodsAdpter extends BaseAdapter {

    private static final String ID_EXTENSIONS_BALANCER_PLUS = "idExtensionsBalancerPlus";
    private static final String FOOD_LIST = "food_list";
    private static final String PORTION = "portion";

    private Context context;
    private List<Parcelable> foodsList;
    private long idExtensionsBalancerPlus;
    private DBManager dbManager;
    private ExtensionsBalancerPlus extensionsBalancerPlus;
    private TextView textViewGrams;
    private FragmentActivity mActivity;
    private int portion;

    public FoodsAdpter(Context context, List<Parcelable> foodsList, FragmentActivity activity, int portion) {
        this.context = context;
        this.foodsList = foodsList;
        this.portion = portion;
        SharedPreferences prefs = context.getSharedPreferences("myPreferences", Context.MODE_PRIVATE);
        idExtensionsBalancerPlus = prefs.getLong(ID_EXTENSIONS_BALANCER_PLUS, 0);
        dbManager = DBManager.getInstance(context);
        extensionsBalancerPlus = dbManager.getExtensionsBalancerPlus(idExtensionsBalancerPlus);
        mActivity = activity;
    }

    @Override
    public int getCount() {
        return this.foodsList.size();
    }

    @Override
    public Parcelable getItem(int position) {
        return this.foodsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        View rowView = convertView;

        if (convertView == null) {
            // Create a new view into the list.
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.list_item, parent, false);
        }
        if (0 != (position % 2)){
            rowView.setBackgroundColor(rowView.getResources().getColor(R.color.colorLightGrey));
        }


        // Set data into the view.
        final TextView foodTitle = (TextView) rowView.findViewById(R.id.food_title);
        TextView textViewQp = (TextView) rowView.findViewById(R.id.text_view_qp);
        textViewGrams = (TextView) rowView.findViewById(R.id.text_view_grams);
        ImageButton buttonUpdate = (ImageButton) rowView.findViewById(R.id.button_update);
        ImageButton buttonDelete = (ImageButton) rowView.findViewById(R.id.button_delete);


        Drawable drawableUpdate = context.getResources().getDrawable(R.drawable.ic_create_black_48dp);
        drawableUpdate.setAlpha(138);
        buttonUpdate.setBackground(drawableUpdate);
        Drawable drawableDelete = context.getResources().getDrawable(R.drawable.ic_delete_black_48dp);
        drawableDelete.setAlpha(138);
        buttonDelete.setBackground(drawableDelete);
        if (this.foodsList.get(position) instanceof Food) {
            final Food food = (Food) this.foodsList.get(position);
            if (null != food) {
                foodTitle.setText(food.getDescripcion_alimento());

                if (null != extensionsBalancerPlus) {
                    textViewQp.setText("(Qp: " + MethodsUtil.getValorQP(food, extensionsBalancerPlus) +  "/ Qg: " + MethodsUtil.getValorQR(food, extensionsBalancerPlus) + ") ");
                }

                textViewGrams.setText(String.valueOf(food.getGramos()) + " grs ");

                buttonUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AddFoodDialogFragment dialogAddFood = AddFoodDialogFragment.newInstance(food, portion, 0, null, true);
                        dialogAddFood.show(mActivity.getSupportFragmentManager(), "dialog");
                        foodsList.remove(food);
                        foodsList.add(food);
                    }
                });

                buttonDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        food.setGramos(0);
                        dbManager.updateFood(food);
                        foodsList.remove(food);
                        notifyDataSetChanged();

                        Bundle bundle = new Bundle();
                        BalancerPlusFragment balancerPlusFragment = new BalancerPlusFragment();
                        bundle.putParcelableArrayList(FOOD_LIST, (ArrayList<? extends Parcelable>) foodsList);
                        bundle.putInt(PORTION, portion);
                        balancerPlusFragment.setArguments(bundle);
                        FragmentManager fragmentManager = mActivity.getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.all_fragments, balancerPlusFragment);
                        fragmentTransaction.commit();
                    }
                });
            }
        } else  {
            final PersonalFood personalFood = (PersonalFood) this.foodsList.get(position);
            if (null != personalFood) {
                foodTitle.setText(personalFood.getDescripcion_alimento());

                if (null != extensionsBalancerPlus) {
                    textViewQp.setText( "(Qp: " + MethodsUtil.getValorQP(personalFood, extensionsBalancerPlus) + " / Qg: " + MethodsUtil.getValorQR(personalFood, extensionsBalancerPlus) + ") ");
                }

                textViewGrams.setText(String.valueOf(personalFood.getPeso_neto()) + " grs ");

                buttonUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Los dos ultimos campos hacen referencia a la foodList y si es un alimento a modificar
                        AddFoodDialogFragment dialogAddFood = AddFoodDialogFragment.newInstance(personalFood, portion, 0, null, true);
                        dialogAddFood.show(mActivity.getSupportFragmentManager(), "dialog");
                        foodsList.remove(personalFood);
                        foodsList.add(personalFood);
                    }
                });

                buttonDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        personalFood.setPeso_neto(0);
                        dbManager.updatePersonalFood(personalFood);
                        foodsList.remove(personalFood);
                        notifyDataSetChanged();

                        Bundle bundle = new Bundle();
                        BalancerPlusFragment balancerPlusFragment = new BalancerPlusFragment();
                        bundle.putParcelableArrayList(FOOD_LIST, (ArrayList<? extends Parcelable>) foodsList);
                        bundle.putInt(PORTION, portion);
                        balancerPlusFragment.setArguments(bundle);
                        FragmentManager fragmentManager = mActivity.getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.all_fragments, balancerPlusFragment);
                        fragmentTransaction.commit();
                    }
                });
            }
        }
        return rowView;
    }
}
