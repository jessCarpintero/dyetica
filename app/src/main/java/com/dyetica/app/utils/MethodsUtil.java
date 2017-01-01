package com.dyetica.app.utils;

import android.util.Log;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Jess on 05/09/2016.
 */
public class MethodsUtil {

    public static boolean convertBrithayDayToTimeStamp(String dateBirthday) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date parsedDate = null;
        try {
            parsedDate = dateFormat.parse(dateBirthday);
        } catch (ParseException e) {
            Log.e("MethodsUtil", "Error " + dateBirthday + "incorrect format");
            return false;
        }
        return true;
    }

    public static Timestamp convertStringToTimeStamp(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
        SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy");
        return  format1.format(cal.getTime());
    }

    public static String getDateNowFormatT(){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        return  format1.format(cal.getTime());
    }

}
