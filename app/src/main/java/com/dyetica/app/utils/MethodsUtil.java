package com.dyetica.app.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.dyetica.app.R;
import com.dyetica.app.model.ExtensionsBalancerPlus;
import com.dyetica.app.model.Food;
import com.dyetica.app.model.PersonalFood;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Jess on 05/09/2016.
 */
public class MethodsUtil {
    public static boolean convertBrithayDayToTimeStamp(String dateBirthday) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", new Locale("es", "ES"));
        Date parsedDate = null;
        try {
            parsedDate = dateFormat.parse(dateBirthday);
        } catch (ParseException e) {
            Log.e("MethodsUtil", "Error " + dateBirthday + "incorrect format");
            return false;
        }
        return formatCorrect(dateBirthday);
    }
    private static boolean formatCorrect(String dateBirthday) {
        String[] aux = dateBirthday.split("-");
        String day = aux[0];
        String month = aux[1];
        String year = aux[2];
        if (day.length() == 2 && month.length() == 2 && year.length() == 4) {
            return true;
        } else {
            return false;
        }
    }


    public static Timestamp convertStringToTimeStamp(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", new Locale("es", "ES"));
        Date parsedDate = null;
        Log.d("MethodsUtil", "Valor de date" + date);
        try {
            parsedDate = dateFormat.parse(date);
        } catch (ParseException e) {
            Log.e("MethodsUtil", "Error " + date + " incorrect format");
        }

        Log.d("MethodsUtil", "Valor de date despues de modificarlo: " + parsedDate.toString());

        return new Timestamp(parsedDate.getTime());
    }

    public static String getDateNow(){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy", new Locale("es", "ES"));
        return  format1.format(cal.getTime());
    }

    public static String getDateNowFormatT(){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", new Locale("es", "ES"));
        return  format1.format(cal.getTime());
    }

    public static String getValorQP(Food food, ExtensionsBalancerPlus extensionsBalancerPlus){
        return String.valueOf(roundDecimals(food.getProteinas() - (extensionsBalancerPlus.getValor_qp() * food.getHidratos_carbono()), 1));
    }

    public static  String getValorQR(Food food, ExtensionsBalancerPlus extensionsBalancerPlus){
        return String.valueOf(roundDecimals(food.getGrasas() - (extensionsBalancerPlus.getValor_qr() * (food.getHidratos_carbono() + food.getProteinas())), 1));
    }

    public static String getValorQP(PersonalFood food, ExtensionsBalancerPlus extensionsBalancerPlus){
        return String.valueOf(roundDecimals(food.getProteinas() - (extensionsBalancerPlus.getValor_qp() * food.getHidratos_carbono()), 1));
    }

    public static  String getValorQR(PersonalFood food, ExtensionsBalancerPlus extensionsBalancerPlus){
        return String.valueOf(roundDecimals(food.getGrasas() - (extensionsBalancerPlus.getValor_qr() * (food.getHidratos_carbono() + food.getProteinas())), 1));
    }

    public static DecimalFormatSymbols setDecimalSeparator(){
        DecimalFormatSymbols custom = new DecimalFormatSymbols();
        custom.setDecimalSeparator('.');
        return custom;
    }

    public static int calcCalories (PersonalFood personalFood){
        return Math.round(((personalFood.getHidratos_carbono() * 4) + (personalFood.getProteinas() * 4) + (personalFood.getGrasas() * 9)) * (personalFood.getPeso_neto()/100F));
    }

    public static float roundDecimals(float initialValue, int numDecimals) {
        BigDecimal bd = new BigDecimal(Float.toString(initialValue));
        bd = bd.setScale(numDecimals, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }

    public static  boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE));
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
    }

    public static boolean isTablet(Context context) {
        boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
        boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
        return (xlarge || large);
    }

}
