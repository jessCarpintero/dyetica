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
import com.dyetica.app.model.DieteticProfile;
import com.dyetica.app.model.Information;
import com.dyetica.app.model.User;

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
    private static final String TABLE_DIETETIC_PROFILE = "dietetic_profile";


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

            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_DIETETIC_PROFILE + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "etapa INTEGER(1) NOT NULL, " +
                    "perfil_id TINYINT(1) NOT NULL DEFAULT '1', " +
                    "user_id INTEGER(11) NOT NULL, " +
                    "nombre TEXT NOT NULL, " +
                    "sexo TINYINT(1) NOT NULL DEFAULT '0', " +
                    "f_nac TEXT NOT NULL, " +
                    "talla INTEGER(3) NOT NULL DEFAULT '0', " +
                    "peso INTEGER(3) NOT NULL, " +
                    "actividad SMALLINT(2) NOT NULL, " +
                    "constitucion SMALLINT(2) NOT NULL, " +
                    "pregunta1 TINYINT(1) DEFAULT NULL, " +
                    "pregunta2 TINYINT(1) DEFAULT NULL, " +
                    "pregunta3 TINYINT(1) DEFAULT NULL, " +
                    "objetivo INTEGER(1) NOT NULL DEFAULT '0', " +
                    "ritmo REAL NOT NULL DEFAULT '0.0', " +
                    "state TINYINT(1) NOT NULL DEFAULT '0', " +
                    "publish_up TIMESTAMP NOT NULL DEFAULT '0000-00-00 00:00:00', " +
                    "publish_down TIMESTAMP NOT NULL DEFAULT '0000-00-00 00:00:00', " +
                    "checked_out INTEGER(10) NOT NULL DEFAULT '0', " +
                    "checked_out_time TIMESTAMP NOT NULL DEFAULT '0000-00-00 00:00:00', " +
                    "actualiza TEXT NOT NULL, " +
                    "kcaldia REAL NOT NULL DEFAULT '0.0'," +
                    "cg REAL NOT NULL DEFAULT '0.0');");


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
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_DIETETIC_PROFILE);

            onCreate(db);
        } catch (SQLiteException sqlError) {
            Log.d("DBManager", "Error drop tables");
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

    public void addDieteticProfile(DieteticProfile dieteticProfile){
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("DBManager", "Entrando para crear dieteticPROFILE");
        try{
            ContentValues values = new ContentValues();
            values.put("etapa", dieteticProfile.getEtapa());
            values.put("perfil_id", dieteticProfile.getPerfil_id());
            values.put("user_id", dieteticProfile.getUser_id());
            values.put("nombre", dieteticProfile.getNombre());
            values.put("sexo", dieteticProfile.getSexo());
            values.put("f_nac", dieteticProfile.getF_nac());
            values.put("talla", dieteticProfile.getTalla());
            values.put("peso", dieteticProfile.getPeso());
            values.put("actividad", dieteticProfile.getActividad());
            values.put("constitucion", dieteticProfile.getConstitucion());
            values.put("pregunta1", dieteticProfile.getPregunta1());
            values.put("pregunta2", dieteticProfile.getPregunta2());
            values.put("pregunta3", dieteticProfile.getPregunta3());
            values.put("objetivo", dieteticProfile.getObjetivo());
            values.put("ritmo", dieteticProfile.getRitmo());
            values.put("state", dieteticProfile.getState());
            values.put("publish_up", dieteticProfile.getPublish_up().getTime());
            values.put("publish_down", dieteticProfile.getPublish_down().getTime());
            values.put("checked_out", dieteticProfile.getChecked_out());
            values.put("checked_out_time", dieteticProfile.getChecked_out_time().getTime());
            values.put("actualiza", dieteticProfile.getActualiza());
            values.put("kcaldia", dieteticProfile.getKcaldia());
            values.put("cg", dieteticProfile.getCg());

            // Inserting Row
            db.insert(TABLE_DIETETIC_PROFILE, null, values);
        } catch (SQLiteException sqlError){
            Log.e("DBManager", "Error creating table " + TABLE_DIETETIC_PROFILE);
        } finally{
            db.close(); // Closing database connection
        }
    }

    // Get User
    public DieteticProfile getDieteticProfile(int idUser, int idProfile) {
        SQLiteDatabase db = this.getReadableDatabase();
        DieteticProfile dieteticProfile = null;
        Cursor cursor = null;
        try{

            Log.d("DBMAnager", "VAlor de query " + "SELECT _id, etapa, perfil_id, user_id, nombre, sexo, f_nac, talla, peso, actividad, constitucion, " +
                    "pregunta1, pregunta2, pregunta3, objetivo, ritmo, state, publish_up, publish_down, checked_out, " +
                    "checked_out_time, actualiza, kcaldia, cg  FROM " + TABLE_DIETETIC_PROFILE + " WHERE perfil_id = '" + idProfile + "' AND user_id = '" + idUser + "' ;");

            cursor = db.rawQuery("SELECT _id, etapa, perfil_id, user_id, nombre, sexo, f_nac, talla, peso, actividad, constitucion, " +
                   "pregunta1, pregunta2, pregunta3, objetivo, ritmo, state, publish_up, publish_down, checked_out, " +
                    "checked_out_time, actualiza, kcaldia, cg  FROM " + TABLE_DIETETIC_PROFILE + " WHERE perfil_id = '" + idProfile + "' AND user_id = '" + idUser + "' ;", null);

            Log.d("DBManager", "Valor de cursor " +  cursor.getCount());

            if (cursor.moveToFirst()) {
                dieteticProfile = new DieteticProfile();
                dieteticProfile.setId(cursor.getInt(0));
                dieteticProfile.setEtapa(cursor.getInt(1));
                dieteticProfile.setPerfil_id(cursor.getInt(2));
                dieteticProfile.setUser_id(cursor.getInt(3));
                dieteticProfile.setNombre(cursor.getString(4));
                dieteticProfile.setSexo(cursor.getInt(5));
                dieteticProfile.setF_nac(cursor.getString(6));
                dieteticProfile.setTalla(cursor.getInt(7));
                dieteticProfile.setPeso(cursor.getInt(8));
                dieteticProfile.setActividad(cursor.getInt(9));
                dieteticProfile.setConstitucion(cursor.getInt(10));
                dieteticProfile.setPregunta1(cursor.getInt(11));
                dieteticProfile.setPregunta2(cursor.getInt(12));
                dieteticProfile.setPregunta3(cursor.getInt(13));
                dieteticProfile.setObjetivo(cursor.getInt(14));
                dieteticProfile.setRitmo(cursor.getFloat(15));
                dieteticProfile.setState(cursor.getInt(16));
                dieteticProfile.setPublish_up(new Timestamp(cursor.getLong(17)));
                dieteticProfile.setPublish_down(new Timestamp(cursor.getLong(18)));
                dieteticProfile.setChecked_out(cursor.getInt(19));
                dieteticProfile.setChecked_out_time(new Timestamp(cursor.getLong(20)));
                dieteticProfile.setActualiza(cursor.getString(21));
                dieteticProfile.setKcaldia(cursor.getFloat(22));
                dieteticProfile.setCg(cursor.getFloat(23));
            }

            Log.d("DBMAnager", "VAlor de dieteticProfile " + dieteticProfile.toString());

        } catch (SQLiteException sqlError){
            Log.e("DBManager", "Error select from " + TABLE_DIETETIC_PROFILE);
        } finally{
            db.close(); // Closing database connection
            return dieteticProfile;
        }
    }
}
