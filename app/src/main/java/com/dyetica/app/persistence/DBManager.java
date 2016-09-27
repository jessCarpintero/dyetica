package com.dyetica.app.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.dyetica.app.R;
import com.dyetica.app.model.Information;
import com.dyetica.app.model.User;
import com.dyetica.app.utils.MethodsUtil;

import java.sql.Timestamp;

/**
 * Created by Jess on 04/09/2016.
 */
public class DBManager extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    //Path Database
    private static String DATABASE_PATH = "/data/data/com.dyetica.app/";

    // Database Name
    private static final String DATABASE_NAME = "dyetica.db";

    //Constants tables
    private static final String TABLE_USERS = "user";
    private static final String TABLE_INFORMATION = "information";

    private final Context myContext;
    private static DBManager sInstance;


    private DBManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.myContext = context;
    }

    public static synchronized DBManager getInstance(Context context) {
        Log.d("DBManager", "getInstance all");
        if (sInstance == null) {
            Log.d("DBManager", "getInstance" );
            sInstance = new DBManager(context.getApplicationContext());
        }
        return sInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_USERS + " (_id INTEGER PRIMARY KEY, " +
                    "name TEXT NOT NULL, " +
                    "username TEXT NOT NULL, " +
                    "email TEXT NOT NULL, " +
                    "block INTEGER(1), " +
                    "registerDate TIMESTAMP, " +
                    "lastvisitDate TIMESTAMP, " +
                    "apiKey TEXT, " +
                    "firstSurname TEXT, " +
                    "secondSurname TEXT, " +
                    "dateBirthday TEXT, " +
                    "postalCode TEXT);");
            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_INFORMATION + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "html TEXT NOT NULL, " +
                    "screen TEXT NOT NULL UNIQUE);");

        } catch (SQLiteException sqlError) {
            Toast toast = Toast.makeText(this.myContext, R.string.error_create, Toast.LENGTH_SHORT);
            toast.show();
            Log.d("DBManager", "Error creating tables");
            db.close();
            sqlError.getStackTrace();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        try {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_INFORMATION);

            onCreate(db);
        } catch (SQLiteException sqlError) {
            Log.d("DBManager", "Erro drop tables");
            db.close();
        }
    }

    // Add new User
    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        try{
            ContentValues values = new ContentValues();
            values.put("_id", user.getId());
            values.put("name", user.getName());
            values.put("username", user.getUsername());
            values.put("email", user.getEmail());
            values.put("block", user.getBlock());
            values.put("registerDate", user.getRegisterDate().getTime());
            values.put("lastvisitDate", user.getLastvisitDate().getTime());
            values.put("apiKey", user.getApiKey());
            values.put("firstSurname", user.getFirstSurname());
            values.put("secondSurname", user.getSecondSurname());
            values.put("dateBirthday", user.getDateBirthday());
            values.put("postalCode", user.getPostalCode());
            // Inserting Row
            db.insert(TABLE_USERS, null, values);
        } catch (SQLiteException sqlError){
            Log.e("DBManager", "Error creating table " + TABLE_USERS);
        } finally{
            db.close(); // Closing database connection
        }
    }

    // Get User
    public User getUser(int idUser) {
        SQLiteDatabase db = this.getReadableDatabase();
        User user = null;
        Cursor cursor = null;
        try{
            cursor = db.rawQuery("SELECT _id, name, username, email, firstSurname, secondSurname, " +
                    "dateBirthday, postalCode, block, registerDate, " +
                    "lastvisitDate, apiKey  FROM " + TABLE_USERS + " WHERE _id = " + idUser + ";", null);

            if (cursor.moveToFirst()) {
                user = User.getInstance(user);
                user.setId(cursor.getInt(0));
                user.setName(cursor.getString(1));
                user.setUsername(cursor.getString(2));
                user.setEmail(cursor.getString(3));
                user.setFirstSurname(cursor.getString(4));
                user.setSecondSurname(cursor.getString(5));
                user.setDateBirthday(cursor.getString(6));
                user.setPostalCode(cursor.getString(7));
                user.setBlock(cursor.getInt(8));
                user.setRegisterDate(new Timestamp(cursor.getLong(9)));
                user.setLastvisitDate(new Timestamp(cursor.getLong(10)));
                user.setApiKey(cursor.getString(11));
            }
        } catch (SQLiteException sqlError){
            Log.e("DBManager", "Error select from " + TABLE_USERS);
        } finally{
            db.close(); // Closing database connection
            return user;
        }
    }

    public boolean updateUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        String[] args = {String.valueOf(user.getId())};
        try{
            ContentValues values = new ContentValues();
            values.put("_id", user.getId());
            values.put("name", user.getName());
            values.put("username", user.getUsername());
            values.put("email", user.getEmail());
            values.put("block", user.getBlock());
            values.put("registerDate", user.getRegisterDate().getTime());
            values.put("lastvisitDate", user.getLastvisitDate().getTime());
            values.put("apiKey", user.getApiKey());
            values.put("firstSurname", user.getFirstSurname());
            values.put("secondSurname", user.getSecondSurname());
            values.put("dateBirthday", user.getDateBirthday());
            values.put("postalCode", user.getPostalCode());

            db.update(TABLE_USERS, values, "_id = ?", args);
            return true;
        } catch (SQLiteException sqlError){
            Log.e("DBManager", "Error updating table " + TABLE_USERS);
            return false;
        } finally{
            db.close(); // Closing database connection
        }
    }


    // Add new User
    public void addInformation(Information information) {
        SQLiteDatabase db = this.getWritableDatabase();
        try{
            ContentValues values = new ContentValues();
            values.put("html", information.getHtml());
            values.put("screen", information.getScreen());
            // Inserting Row
            db.insert(TABLE_INFORMATION, null, values);
        } catch (SQLiteException sqlError){
            Log.e("DBManager", "Error creating table " + TABLE_INFORMATION);
        } finally{
            db.close(); // Closing database connection
        }
    }

    // Get User
    public Information getInformation(String screen) {
        SQLiteDatabase db = this.getReadableDatabase();
        Information information = null;
        Cursor cursor = null;
        try{
            cursor = db.rawQuery("SELECT _id, html, screen FROM " + TABLE_INFORMATION + " WHERE screen = '" + screen + "';", null);

            if (cursor.moveToFirst()) {
                information = Information.getInstance(information);
                information.setId(cursor.getInt(0));
                information.setHtml(cursor.getString(1));
                information.setScreen(cursor.getString(2));
            }

        } catch (SQLiteException sqlError){
            Log.e("DBManager", "Error select from " + TABLE_INFORMATION);
        } finally{
            db.close(); // Closing database connection
            return information;
        }
    }

    public void updateInformation(Information information){
        SQLiteDatabase db = this.getWritableDatabase();
        String[] args = {information.getScreen()};
        try{
            ContentValues values = new ContentValues();
            values.put("html", information.getHtml());

            db.update(TABLE_INFORMATION, values, "screen = ?", args);
        } catch (SQLiteException sqlError){
            Log.e("DBManager", "Error updating table " + TABLE_INFORMATION);
        } finally{
            db.close(); // Closing database connection
        }
    }

    private void messageToast(int message){
        Toast toast = Toast.makeText(this.myContext, message, Toast.LENGTH_SHORT);
        toast.show();
    }
}
