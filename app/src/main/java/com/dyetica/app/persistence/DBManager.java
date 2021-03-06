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
import com.dyetica.app.model.ExtensionsBalancerPlus;
import com.dyetica.app.model.ExtensionsProfile;
import com.dyetica.app.model.Food;
import com.dyetica.app.model.Information;
import com.dyetica.app.model.PersonalFood;
import com.dyetica.app.model.Statistics;
import com.dyetica.app.model.TypeActivity;
import com.dyetica.app.model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

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
    private static final String TABLE_TYPE_ACTIVITY = "estandares_tipos_de_actividad";
    private static final String TABLE_EXTENSIONS_PROFILE = "extensions_profile";
    private static final String TABLE_FOODS = "foods";
    private static final String TABLE_PERSONAL_FOODS = "personal_foods";
    private static final String TABLE_STATISTICS = "statistics";
    private static final String TABLE_EXTENSIONS_BALANCER_PLUS = "extensions_balancer_plus";

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

            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_TYPE_ACTIVITY + " (_id INTEGER PRIMARY KEY, " +
                    "activity TEXT NOT NULL, " +
                    "factor REAL NOT NULL);");

            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_FOODS + " (_id INTEGER PRIMARY KEY, " +
                    "descripcion_alimento  TEXT NOT NULL, " +
                    "grupo_alimentos  TEXT NOT NULL, " +
                    "hidratos_carbono  REAL NOT NULL DEFAULT '0.0', " +
                    "proteinas  REAL NOT NULL DEFAULT '0.0', " +
                    "grasas  REAL NOT NULL DEFAULT '0.0', " +
                    "gramos INTEGER(6) NOT NULL DEFAULT '0'," +
                    "calorias INTEGER(6) NOT NULL DEFAULT '0'," +
                    "state INTEGER(1) NOT NULL DEFAULT '0'," +
                    "tipo_alimento INTEGER(1) NOT NULL DEFAULT '0'," +
                    "fibras  REAL NOT NULL DEFAULT '0.0', " +
                    "hierro  REAL NOT NULL DEFAULT '0.0', " +
                    "calcio  REAL NOT NULL DEFAULT '0.0', " +
                    "vitamina_a  REAL NOT NULL DEFAULT '0.0', " +
                    "vitamina_b1  REAL NOT NULL DEFAULT '0.0', " +
                    "vitamina_c  REAL NOT NULL DEFAULT '0.0', " +
                    "rivoflavina  REAL NOT NULL DEFAULT '0.0', " +
                    "niacina  REAL NOT NULL DEFAULT '0.0', " +
                    "vitamina_e  REAL NOT NULL DEFAULT '0.0');");

            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_EXTENSIONS_PROFILE + " (_id INTEGER PRIMARY KEY, " +
                    "formulas_gr_sem_kilo REAL NOT NULL DEFAULT '0.0', " +
                    "formulas_cg_base  REAL NOT NULL DEFAULT '0.0', " +
                    "formulas_cg_varible1  REAL NOT NULL DEFAULT '0.0', " +
                    "formulas_cg_varible2  REAL NOT NULL DEFAULT '0.0', " +
                    "formulas_cg_min  REAL NOT NULL DEFAULT '0.0', " +
                    "formulas_cg_max  REAL NOT NULL DEFAULT '0.0', " +
                    "manana_desayuno_fuerte  REAL NOT NULL DEFAULT '0.0', " +
                    "manana_almuerzo_ligero  REAL NOT NULL DEFAULT '0.0', " +
                    "manana_desayuno_mediano  REAL NOT NULL DEFAULT '0.0', " +
                    "manana_almuerzo_mediano  REAL NOT NULL DEFAULT '0.0', " +
                    "manana_desayuno_ligero  REAL NOT NULL DEFAULT '0.0', " +
                    "manana_almuerzo_fuerte  REAL NOT NULL DEFAULT '0.0', " +
                    "mediodia_priomeroligero  REAL NOT NULL DEFAULT '0.0', " +
                    "mediodia_segundofuerte  REAL NOT NULL DEFAULT '0.0', " +
                    "mediodia_primeromedio  REAL NOT NULL DEFAULT '0.0', " +
                    "mediodia_segundomedio  REAL NOT NULL DEFAULT '0.0', " +
                    "mediodia_postre  REAL NOT NULL DEFAULT '0.0', " +
                    "merienda  REAL NOT NULL DEFAULT '0.0', " +
                    "noche_priomeroligero  REAL NOT NULL DEFAULT '0.0', " +
                    "noche_segundofuerte  REAL NOT NULL DEFAULT '0.0', " +
                    "noche_primeromedio  REAL NOT NULL DEFAULT '0.0', " +
                    "noche_segundomedio  REAL NOT NULL DEFAULT '0.0', " +
                    "noche_postre  REAL NOT NULL DEFAULT '0.0', " +
                    "menu24_perder  REAL NOT NULL DEFAULT '0.0', " +
                    "menu24_aumentar  REAL NOT NULL DEFAULT '0.0', " +
                    "link_perfil  TEXT NOT NULL, " +
                    "link_menu24h TEXT NOT NULL);");

            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_STATISTICS + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "user_id INTEGER(11) NOT NULL, " +
                    "last_access TIMESTAMP NOT NULL DEFAULT '0000-00-00 00:00:00');");

            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_EXTENSIONS_BALANCER_PLUS + " (_id INTEGER PRIMARY KEY, " +
                    "activado INTEGER(2) NOT NULL DEFAULT '0' ," +
                    "num_alimentos INTEGER(2) NOT NULL DEFAULT '0' ," +
                    "raciones INTEGER(2) NOT NULL DEFAULT '0' ," +
                    "otros TEXT NOT NULL ," +
                    "multiplica INTEGER(2) NOT NULL DEFAULT '0' ," +
                    "limite_hidratos REAL NOT NULL DEFAULT '0.0' ," +
                    "limite_proteinas REAL NOT NULL DEFAULT '0.0' ," +
                    "valor_qp REAL NOT NULL DEFAULT '0.0' ," +
                    "valor_qr REAL NOT NULL DEFAULT '0.0' ," +
                    "grasas TEXT NOT NULL ," +
                    "frutas TEXT NOT NULL ," +
                    "verduras TEXT NOT NULL ," +
                    "raices TEXT NOT NULL ," +
                    "carnes TEXT NOT NULL ," +
                    "embutidos TEXT NOT NULL ," +
                    "mariscos TEXT NOT NULL ," +
                    "legumbres TEXT NOT NULL ," +
                    "pescados TEXT NOT NULL ," +
                    "cereales TEXT NOT NULL ," +
                    "dulces TEXT NOT NULL ," +
                    "bebidas TEXT NOT NULL ," +
                    "frutos_secos TEXT NOT NULL ," +
                    "lacteos TEXT NOT NULL ," +
                    "salsas TEXT NOT NULL ," +
                    "huevos TEXT NOT NULL ," +
                    "codigo_personal TEXT NOT NULL ," +
                    "maximo_alimentos INTEGER(2) NOT NULL DEFAULT '0' ," +
                    "lacteos_quesos_huevos_compensa2 TEXT NOT NULL ," +
                    "pescados_mariscos_compensa2 TEXT NOT NULL ," +
                    "carnes_compensa2 TEXT NOT NULL ," +
                    "embutidos_compensa2 TEXT NOT NULL ," +
                    "nueces_compensa2 TEXT NOT NULL ," +
                    "legumbres_semillas_raices_compensa2 TEXT NOT NULL ," +
                    "cereales_pastas_compensa2 TEXT NOT NULL ," +
                    "frutas_bebidas_compensa2 TEXT NOT NULL ," +
                    "dulces_compensa2 TEXT NOT NULL ," +
                    "patatas_compensa2 TEXT NOT NULL ," +
                    "exceso_hidratos_compensa2 REAL NOT NULL DEFAULT '0.0' ," +
                    "exceso_proteinas_compensa2 REAL NOT NULL DEFAULT '0.0' ," +
                    "link TEXT NOT NULL ," +
                    "link_compensa TEXT NOT NULL ," +
                    "link_equilibrador TEXT NOT NULL ," +
                    "link_equilibrador_plus TEXT NOT NULL ," +
                    "desayuno_necesidades REAL NOT NULL DEFAULT '0' ," +
                    "almuerzo_necesidades REAL NOT NULL DEFAULT '0' ," +
                    "comida_necesidades REAL NOT NULL DEFAULT '0' ," +
                    "merienda_necesidades REAL NOT NULL DEFAULT '0' ," +
                    "cena_necesidades REAL NOT NULL DEFAULT '0');");

            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_PERSONAL_FOODS + " (_id INTEGER PRIMARY KEY, " +
                    "id_user INTEGER NOT NULL, " +
                    "descripcion_alimento  TEXT NOT NULL, " +
                    "peso_neto INTEGER(6) NOT NULL DEFAULT '0', " +
                    "hidratos_carbono  REAL NOT NULL DEFAULT '0.0', " +
                    "proteinas  REAL NOT NULL DEFAULT '0.0', " +
                    "grasas  REAL NOT NULL DEFAULT '0.0', " +
                    "ubicacion INTEGER(4) NOT NULL DEFAULT '0'," +
                    "calorias INTEGER(6) NOT NULL DEFAULT '0');");
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
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TYPE_ACTIVITY);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXTENSIONS_PROFILE);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOODS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_STATISTICS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXTENSIONS_BALANCER_PLUS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_PERSONAL_FOODS);

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

    //Add dietetic profile
    public long addDieteticProfile(DieteticProfile dieteticProfile){
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
            values.put("actualiza", dieteticProfile.getActualiza());
            values.put("kcaldia", dieteticProfile.getKcaldia());
            values.put("cg", dieteticProfile.getCg());

            // Inserting Row
            return db.insert(TABLE_DIETETIC_PROFILE, null, values);
        } catch (SQLiteException sqlError){
            Log.e("DBManager", "Error creating table " + TABLE_DIETETIC_PROFILE);
            return -1;
        } finally{
            db.close(); // Closing database connection
        }
    }

    //Update DieteticProfile
    public long updateDieteticProfile(DieteticProfile dieteticProfile, int idDieteticProfile){
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("DBManager", "Entrando para modificar dieteticPROFILE");
        try{
            ContentValues values = new ContentValues();
            values.put("_id", dieteticProfile.getId());
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
            values.put("actualiza", dieteticProfile.getActualiza());
            values.put("kcaldia", dieteticProfile.getKcaldia());
            values.put("cg", dieteticProfile.getCg());

            // Inserting Row
            return db.update(TABLE_DIETETIC_PROFILE, values," _id = " + idDieteticProfile, null);
        } catch (SQLiteException sqlError){
            Log.e("DBManager", "Error creating table " + TABLE_DIETETIC_PROFILE);
            return -1;
        } finally{
            db.close(); // Closing database connection
        }
    }

    // Get Dietetic profile
    public DieteticProfile getDieteticProfile(int idUser, int idProfile) {
        SQLiteDatabase db = this.getReadableDatabase();
        DieteticProfile dieteticProfile = null;
        Cursor cursor = null, cursor2 = null;
        try{
            cursor = db.rawQuery("SELECT _id, etapa, perfil_id, user_id, nombre, sexo, f_nac, talla, peso, actividad, constitucion, " +
                   "pregunta1, pregunta2, pregunta3, objetivo, ritmo, state, publish_up, publish_down, checked_out, " +
                    "checked_out_time, actualiza, kcaldia, cg  FROM " + TABLE_DIETETIC_PROFILE + " WHERE perfil_id = '" + idProfile + "' AND user_id = '" + idUser + "' ;", null);

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

    // Get Dietetic profile
    public int countDieteticProfileByIdUser(int idUser) {
        SQLiteDatabase db = this.getReadableDatabase();
        DieteticProfile dieteticProfile = null;
        Cursor cursor = null;
        try{

            cursor = db.rawQuery("SELECT _id, etapa, perfil_id, user_id, nombre, sexo, f_nac, talla, peso, actividad, constitucion, " +
                    "pregunta1, pregunta2, pregunta3, objetivo, ritmo, state, publish_up, publish_down, checked_out, " +
                    "checked_out_time, actualiza, kcaldia, cg  FROM " + TABLE_DIETETIC_PROFILE + " WHERE user_id = '" + idUser + "' ;", null);

            Log.d("DBManager", "Valor de cursor " +  cursor.getCount());
        } catch (SQLiteException sqlError){
            Log.e("DBManager", "Error select from " + TABLE_DIETETIC_PROFILE);
        } finally{
            db.close(); // Closing database connection
            return cursor.getCount();
        }
    }

    // Delete Category
    public void deleteDieteticProfile(long idDieteticProfile) {
        SQLiteDatabase db = this.getWritableDatabase();
        try{
            assert db != null;
            db.delete(TABLE_DIETETIC_PROFILE, "_id = " + idDieteticProfile, null);
        } catch (SQLiteException sqlError){
            Toast toast = Toast.makeText(this.myContext, R.string.deleteError, Toast.LENGTH_SHORT);
            toast.show();
        } finally{
            // db.close(); // Closing database connection
        }
    }

    // Add new Type activity
    public void addTypeActivity(TypeActivity activity) {
        SQLiteDatabase db = this.getWritableDatabase();
        try{
            ContentValues values = new ContentValues();
            values.put("_id", activity.getId());
            values.put("activity", activity.getActivity());
            values.put("factor", activity.getFactor());
            // Inserting Row
            db.insert(TABLE_TYPE_ACTIVITY, null, values);
        } catch (SQLiteException sqlError){
            Log.e("DBManager", "Error creating table " + TABLE_TYPE_ACTIVITY);
        } finally{
            db.close(); // Closing database connection
        }
    }

    public TypeActivity getTypeActivity(int activity) {
        SQLiteDatabase db = this.getReadableDatabase();
        TypeActivity typeActivity = null;
        Cursor cursor = null;
        try{

            Log.d("DBMAnager", "VAlor de query " + "SELECT _id, activity, factor FROM " + TABLE_TYPE_ACTIVITY + " WHERE _id = '" + activity  + "' ;");

            cursor = db.rawQuery("SELECT _id, activity, factor FROM " + TABLE_TYPE_ACTIVITY + " WHERE _id = '" + activity  + "' ;", null);

            Log.d("DBManager", "Valor de cursor " +  cursor.getCount());

            if (cursor.moveToFirst()) {
                typeActivity = new TypeActivity();
                typeActivity.setId(cursor.getInt(0));
                typeActivity.setActivity(cursor.getString(1));
                typeActivity.setFactor(cursor.getFloat(2));
            }

            Log.d("DBMAnager", "VAlor de typeActivity " + typeActivity.toString());

        } catch (SQLiteException sqlError){
            Log.e("DBManager", "Error select from " + TABLE_TYPE_ACTIVITY);
        } finally{
            db.close(); // Closing database connection
            return typeActivity;
        }
    }

    // Add new ExtensionsProfile
    public long addExtensionsProfile(ExtensionsProfile extensionsProfile) {
        SQLiteDatabase db = this.getWritableDatabase();
        long id;
        try{
            ContentValues values = new ContentValues();
            values.put("_id", extensionsProfile.get_id());
            values.put("formulas_gr_sem_kilo", extensionsProfile.getFormulas_gr_sem_kilo());
            values.put("formulas_cg_base", extensionsProfile.getFormulas_cg_base());
            values.put("formulas_cg_varible1", extensionsProfile.getFormulas_cg_varible1());
            values.put("formulas_cg_varible2", extensionsProfile.getFormulas_cg_varible2());
            values.put("formulas_cg_min", extensionsProfile.getFormulas_cg_min());
            values.put("formulas_cg_max", extensionsProfile.getFormulas_cg_max());
            values.put("manana_desayuno_fuerte", extensionsProfile.getManana_desayuno_fuerte());
            values.put("manana_almuerzo_ligero", extensionsProfile.getManana_almuerzo_ligero());
            values.put("manana_desayuno_mediano", extensionsProfile.getManana_desayuno_mediano());
            values.put("manana_almuerzo_mediano", extensionsProfile.getManana_almuerzo_mediano());
            values.put("manana_desayuno_ligero", extensionsProfile.getManana_desayuno_ligero());
            values.put("manana_almuerzo_fuerte", extensionsProfile.getManana_almuerzo_fuerte());
            values.put("mediodia_priomeroligero", extensionsProfile.getMediodia_priomeroligero());
            values.put("mediodia_segundofuerte", extensionsProfile.getMediodia_segundofuerte());
            values.put("mediodia_primeromedio", extensionsProfile.getMediodia_primeromedio());
            values.put("mediodia_segundomedio", extensionsProfile.getMediodia_segundomedio());
            values.put("mediodia_postre", extensionsProfile.getMediodia_postre());
            values.put("merienda", extensionsProfile.getMerienda());
            values.put("noche_priomeroligero", extensionsProfile.getNoche_priomeroligero());
            values.put("noche_segundofuerte", extensionsProfile.getNoche_segundofuerte());
            values.put("noche_primeromedio", extensionsProfile.getNoche_primeromedio());
            values.put("noche_segundomedio", extensionsProfile.getNoche_segundomedio());
            values.put("noche_postre", extensionsProfile.getNoche_postre());
            values.put("menu24_perder", extensionsProfile.getMenu24_perder());
            values.put("menu24_aumentar", extensionsProfile.getMenu24_aumentar());
            values.put("link_perfil", extensionsProfile.getLink_perfil());
            values.put("link_menu24h", extensionsProfile.getLink_menu24h());

            // Inserting Row
           id = db.insert(TABLE_EXTENSIONS_PROFILE, null, values);
            return id;
        } catch (SQLiteException sqlError){
            Log.e("DBManager", "Error creating table " + TABLE_EXTENSIONS_PROFILE);
            return -1;
        } finally{
            db.close(); // Closing database connection
        }
    }

    // Get ExtensionsProfile
    public ExtensionsProfile getExtensionsProfile(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        ExtensionsProfile extensionsProfile = null;
        Cursor cursor = null;
        try{
            cursor = db.rawQuery("SELECT _id, formulas_gr_sem_kilo, " +
                    "formulas_cg_base, " +
                    "formulas_cg_varible1, " +
                    "formulas_cg_varible2, " +
                    "formulas_cg_min, " +
                    "formulas_cg_max, " +
                    "manana_desayuno_fuerte, " +
                    "manana_almuerzo_ligero, " +
                    "manana_desayuno_mediano, " +
                    "manana_almuerzo_mediano, " +
                    "manana_desayuno_ligero, " +
                    "manana_almuerzo_fuerte, " +
                    "mediodia_priomeroligero, " +
                    "mediodia_segundofuerte, " +
                    "mediodia_primeromedio, " +
                    "mediodia_segundomedio, " +
                    "mediodia_postre, " +
                    "merienda, " +
                    "noche_priomeroligero, " +
                    "noche_segundofuerte, " +
                    "noche_primeromedio, " +
                    "noche_segundomedio, " +
                    "noche_postre, " +
                    "menu24_perder, " +
                    "menu24_aumentar, " +
                    "link_perfil, " +
                    "link_menu24h FROM " + TABLE_EXTENSIONS_PROFILE + " WHERE _id = '" + id + "';", null);

            if (cursor.moveToFirst()) {
                extensionsProfile = ExtensionsProfile.getInstance(extensionsProfile);
                extensionsProfile.set_id(cursor.getLong(0));
                extensionsProfile.setFormulas_gr_sem_kilo(cursor.getFloat(1));
                extensionsProfile.setFormulas_cg_base(cursor.getFloat(2));
                extensionsProfile.setFormulas_cg_varible1(cursor.getFloat(3));
                extensionsProfile.setFormulas_cg_varible2(cursor.getFloat(4));
                extensionsProfile.setFormulas_cg_min(cursor.getFloat(5));
                extensionsProfile.setFormulas_cg_max(cursor.getFloat(6));
                extensionsProfile.setManana_desayuno_fuerte(cursor.getFloat(7));
                extensionsProfile.setManana_almuerzo_ligero(cursor.getFloat(8));
                extensionsProfile.setManana_desayuno_mediano(cursor.getFloat(9));
                extensionsProfile.setManana_almuerzo_mediano(cursor.getFloat(10));
                extensionsProfile.setManana_desayuno_ligero(cursor.getFloat(11));
                extensionsProfile.setManana_almuerzo_fuerte(cursor.getFloat(12));
                extensionsProfile.setMediodia_priomeroligero(cursor.getFloat(13));
                extensionsProfile.setMediodia_segundofuerte(cursor.getFloat(14));
                extensionsProfile.setMediodia_primeromedio(cursor.getFloat(15));
                extensionsProfile.setMediodia_segundomedio(cursor.getFloat(16));
                extensionsProfile.setMediodia_postre(cursor.getFloat(17));
                extensionsProfile.setMerienda(cursor.getFloat(18));
                extensionsProfile.setNoche_priomeroligero(cursor.getFloat(19));
                extensionsProfile.setNoche_segundofuerte(cursor.getFloat(20));
                extensionsProfile.setNoche_primeromedio(cursor.getFloat(21));
                extensionsProfile.setNoche_segundomedio(cursor.getFloat(22));
                extensionsProfile.setNoche_postre(cursor.getFloat(23));
                extensionsProfile.setMenu24_perder(cursor.getFloat(24));
                extensionsProfile.setMenu24_aumentar(cursor.getFloat(25));
                extensionsProfile.setLink_perfil(cursor.getString(26));
                extensionsProfile.setLink_menu24h(cursor.getString(27));
            }

        } catch (SQLiteException sqlError){
            Log.e("DBManager", "Error select from " + TABLE_EXTENSIONS_PROFILE);
        } finally{
            db.close(); // Closing database connection
            return extensionsProfile;
        }
    }

    // Add or update Food
    public void addOrUpdateFoods(JSONArray jsonArray) {
        SQLiteDatabase db = this.getWritableDatabase();
        Gson gson = new GsonBuilder().create();
        try{
            db.beginTransaction();
            for (int i = 0; i < jsonArray.length(); i++) {
                Food food = gson.fromJson(jsonArray.getString(i), Food.class);
                String[] args = {String.valueOf(food.getId())};
                ContentValues values = new ContentValues();
                values.put("_id", food.getId());
                values.put("descripcion_alimento", food.getDescripcion_alimento());
                values.put("grupo_alimentos", food.getGrupo_alimentos());
                values.put("hidratos_carbono", food.getHidratos_carbono());
                values.put("proteinas", food.getProteinas());
                values.put("grasas", food.getGrasas());
                values.put("gramos", food.getGramos());
                values.put("calorias", food.getCalorias());
                values.put("state", food.getState());
                values.put("tipo_alimento", food.getTipo_alimento());
                values.put("fibras", food.getFibras());
                values.put("hierro", food.getHierro());
                values.put("calcio", food.getCalcio());
                values.put("vitamina_a", food.getVitamina_a());
                values.put("vitamina_b1", food.getVitamina_b1());
                values.put("vitamina_c", food.getVitamina_c());
                values.put("rivoflavina", food.getRivoflavina());
                values.put("niacina", food.getNiacina());
                values.put("vitamina_e", food.getVitamina_e());

                Cursor cursor = null;
                cursor = db.rawQuery("SELECT _id, descripcion_alimento, " +
                        "grupo_alimentos, " +
                        "hidratos_carbono, " +
                        "proteinas, " +
                        "grasas, " +
                        "gramos, " +
                        "calorias, " +
                        "state, " +
                        "tipo_alimento, " +
                        "fibras, " +
                        "hierro, " +
                        "calcio, " +
                        "vitamina_a, " +
                        "vitamina_b1, " +
                        "vitamina_c, " +
                        "rivoflavina, " +
                        "niacina, " +
                        "vitamina_e " +
                        "FROM " + TABLE_FOODS + " WHERE _id = '" + food.getId() + "';", null);
                if (cursor.getCount() <= 0) {
                    db.insert(TABLE_FOODS, null, values);
                } else {
                    db.update(TABLE_FOODS, values, " _id = ?", args);
                }
            }
            db.setTransactionSuccessful();
        } catch (SQLiteException sqlError){
            Log.e("DBManager", "Error creating table " + TABLE_FOODS);
        } catch (JSONException e) {
            e.printStackTrace();
        } finally{
            db.endTransaction();
        }
    }

    // Add new Food
    public void addFoods(JSONArray jsonArray) {
        SQLiteDatabase db = this.getWritableDatabase();
        Gson gson = new GsonBuilder().create();
        try{
            db.beginTransaction();
            for (int i = 0; i < jsonArray.length(); i++) {
                Food food = gson.fromJson(jsonArray.getString(i), Food.class);
                ContentValues values = new ContentValues();
                values.put("_id", food.getId());
                values.put("descripcion_alimento", food.getDescripcion_alimento());
                values.put("grupo_alimentos", food.getGrupo_alimentos());
                values.put("hidratos_carbono", food.getHidratos_carbono());
                values.put("proteinas", food.getProteinas());
                values.put("grasas", food.getGrasas());
                values.put("gramos", food.getGramos());
                values.put("calorias", food.getCalorias());
                values.put("state", food.getState());
                values.put("tipo_alimento", food.getTipo_alimento());
                values.put("fibras", food.getFibras());
                values.put("hierro", food.getHierro());
                values.put("calcio", food.getCalcio());
                values.put("vitamina_a", food.getVitamina_a());
                values.put("vitamina_b1", food.getVitamina_b1());
                values.put("vitamina_c", food.getVitamina_c());
                values.put("rivoflavina", food.getRivoflavina());
                values.put("niacina", food.getNiacina());
                values.put("vitamina_e", food.getVitamina_e());
                db.insert(TABLE_FOODS, null, values);
            }
            db.setTransactionSuccessful();
        } catch (SQLiteException sqlError){
            Log.e("DBManager", "Error creating table " + TABLE_FOODS);
        } catch (JSONException e) {
            e.printStackTrace();
        } finally{
            db.endTransaction();
        }
        db.close();
    }

    // Get ExtensionsProfile
    public Food getFood(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Food food = null;
        Cursor cursor = null;
        try{
            cursor = db.rawQuery("SELECT _id, descripcion_alimento, " +
                    "grupo_alimentos, " +
                    "hidratos_carbono, " +
                    "proteinas, " +
                    "grasas, " +
                    "gramos," +
                    "calorias, " +
                    "state, " +
                    "tipo_alimento, " +
                    "fibras, " +
                    "hierro, " +
                    "calcio, " +
                    "vitamina_a, " +
                    "vitamina_b1, " +
                    "vitamina_c, " +
                    "rivoflavina, " +
                    "niacina, " +
                    "vitamina_e " +
                    "FROM " + TABLE_FOODS + " WHERE _id = '" + id + "';", null);

            if (cursor.moveToFirst()) {
                food = new Food();
                food.setId(cursor.getLong(0));
                food.setDescripcion_alimento(cursor.getString(1));
                food.setGrupo_alimentos(cursor.getString(2));
                food.setHidratos_carbono(cursor.getFloat(3));
                food.setProteinas(cursor.getFloat(4));
                food.setGrasas(cursor.getFloat(5));
                food.setGramos(cursor.getInt(6));
                food.setCalorias(cursor.getInt(7));
                food.setState(cursor.getInt(8));
                food.setTipo_alimento(cursor.getInt(9));
                food.setFibras(cursor.getFloat(10));
                food.setHierro(cursor.getFloat(11));
                food.setCalcio(cursor.getFloat(12));
                food.setVitamina_a(cursor.getFloat(13));
                food.setVitamina_b1(cursor.getFloat(14));
                food.setVitamina_c(cursor.getFloat(15));
                food.setRivoflavina(cursor.getFloat(16));
                food.setNiacina(cursor.getFloat(17));
                food.setVitamina_e(cursor.getFloat(18));

            }
        } catch (SQLiteException sqlError){
            Log.e("DBManager", "Error select from " + TABLE_FOODS);
        } finally{
            db.close(); // Closing database connection
            return food;
        }
    }

    // Get Food by category
    public List<Food> getFoodByCategory(String categories) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Food> foodList = new ArrayList<>();
        Cursor cursor = null;
        try{
            db.beginTransaction();
            cursor = db.rawQuery("SELECT _id, descripcion_alimento, " +
                    "grupo_alimentos, " +
                    "hidratos_carbono, " +
                    "proteinas, " +
                    "grasas, " +
                    "gramos, " +
                    "calorias, " +
                    "state, " +
                    "tipo_alimento, " +
                    "fibras, " +
                    "hierro, " +
                    "calcio, " +
                    "vitamina_a, " +
                    "vitamina_b1, " +
                    "vitamina_c, " +
                    "rivoflavina, " +
                    "niacina, " +
                    "vitamina_e " +
                    "FROM " + TABLE_FOODS + " WHERE grupo_alimentos IN ( " + categories + ") ORDER BY descripcion_alimento;", null);

            if (cursor .moveToFirst()) {
                while (cursor.isAfterLast() == false) {
                    Food food = new Food();
                    food.setId(cursor.getLong(0));
                    food.setDescripcion_alimento(cursor.getString(1));
                    food.setGrupo_alimentos(cursor.getString(2));
                    food.setHidratos_carbono(cursor.getFloat(3));
                    food.setProteinas(cursor.getFloat(4));
                    food.setGrasas(cursor.getFloat(5));
                    food.setGramos(cursor.getInt(6));
                    food.setCalorias(cursor.getInt(7));
                    food.setState(cursor.getInt(8));
                    food.setTipo_alimento(cursor.getInt(9));
                    food.setFibras(cursor.getFloat(10));
                    food.setHierro(cursor.getFloat(11));
                    food.setCalcio(cursor.getFloat(12));
                    food.setVitamina_a(cursor.getFloat(13));
                    food.setVitamina_b1(cursor.getFloat(14));
                    food.setVitamina_c(cursor.getFloat(15));
                    food.setRivoflavina(cursor.getFloat(16));
                    food.setNiacina(cursor.getFloat(17));
                    food.setVitamina_e(cursor.getFloat(18));
                    foodList.add(food);
                    cursor.moveToNext();
                }
            }
            db.setTransactionSuccessful();
            Log.d("DBManager", "Valor de count: " + cursor.getCount() + " valor de size " + foodList.size());
        } catch (SQLiteException sqlError){
            Log.e("DBManager", "Error get foods by category from " + TABLE_FOODS);
        } finally{
            db.endTransaction();
            db.close(); // Closing database connection
            return foodList;
        }
    }

    // Get All Foods
    public List<Food> getFoods() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Food> foodList = new ArrayList<>();
        Cursor cursor = null;
        try{
            db.beginTransaction();
            cursor = db.rawQuery("SELECT _id, descripcion_alimento, " +
                    "grupo_alimentos, " +
                    "hidratos_carbono, " +
                    "proteinas, " +
                    "grasas, " +
                    "gramos, " +
                    "calorias, " +
                    "state, " +
                    "tipo_alimento, " +
                    "fibras, " +
                    "hierro, " +
                    "calcio, " +
                    "vitamina_a, " +
                    "vitamina_b1, " +
                    "vitamina_c, " +
                    "rivoflavina, " +
                    "niacina, " +
                    "vitamina_e " +
                    "FROM " + TABLE_FOODS + ";", null);

            if (cursor .moveToFirst()) {
                while (cursor.isAfterLast() == false) {
                    Food food = new Food();
                    food.setId(cursor.getLong(0));
                    food.setDescripcion_alimento(cursor.getString(1));
                    food.setGrupo_alimentos(cursor.getString(2));
                    food.setHidratos_carbono(cursor.getFloat(3));
                    food.setProteinas(cursor.getFloat(4));
                    food.setGrasas(cursor.getFloat(5));
                    food.setGramos(cursor.getInt(6));
                    food.setCalorias(cursor.getInt(7));
                    food.setState(cursor.getInt(8));
                    food.setTipo_alimento(cursor.getInt(9));
                    food.setFibras(cursor.getFloat(10));
                    food.setHierro(cursor.getFloat(11));
                    food.setCalcio(cursor.getFloat(12));
                    food.setVitamina_a(cursor.getFloat(13));
                    food.setVitamina_b1(cursor.getFloat(14));
                    food.setVitamina_c(cursor.getFloat(15));
                    food.setRivoflavina(cursor.getFloat(16));
                    food.setNiacina(cursor.getFloat(17));
                    food.setVitamina_e(cursor.getFloat(18));
                    foodList.add(food);
                    cursor.moveToNext();
                }
            }
            db.setTransactionSuccessful();
            Log.d("DBManager", "Valor de count: " + cursor.getCount() + " valor de size " + foodList.size());
        } catch (SQLiteException sqlError){
            Log.e("DBManager", "Error get foods by category from " + TABLE_FOODS);
        } finally{
            db.endTransaction();
            db.close(); // Closing database connection
            return foodList;
        }
    }

    // Get Food by category and balance
    public List<Food> getFoodByCategoryAndBalance(String categories, ExtensionsBalancerPlus extensionsBalancerPlus, float protein) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Food> foodList = new ArrayList<>();
        String query = "";
        Cursor cursor = null;
        try{
            db.beginTransaction();
            query = "SELECT _id, descripcion_alimento, " +
                    "grupo_alimentos, " +
                    "hidratos_carbono, " +
                    "proteinas, " +
                    "grasas, " +
                    "gramos, " +
                    "calorias, " +
                    "state, " +
                    "tipo_alimento, " +
                    "fibras, " +
                    "hierro, " +
                    "calcio, " +
                    "vitamina_a, " +
                    "vitamina_b1, " +
                    "vitamina_c, " +
                    "rivoflavina, " +
                    "niacina, " +
                    "vitamina_e " +
                    "FROM " + TABLE_FOODS;
            if (protein > extensionsBalancerPlus.getLimite_proteinas()) {
                query += " WHERE ((proteinas * " + extensionsBalancerPlus.getMultiplica() + " )/(hidratos_carbono + proteinas)) < "  + extensionsBalancerPlus.getLimite_hidratos();
            } else if (protein < extensionsBalancerPlus.getLimite_hidratos()) {
                query += " WHERE ((proteinas * " + extensionsBalancerPlus.getMultiplica() + ")/(hidratos_carbono + proteinas)) > "  + extensionsBalancerPlus.getLimite_proteinas();
            }
            query +=  " AND grupo_alimentos IN ( " + categories + ")  AND state = 1 AND tipo_alimento = 0 ORDER BY descripcion_alimento;";
            cursor = db.rawQuery(query, null);

            if (cursor .moveToFirst()) {
                while (cursor.isAfterLast() == false) {
                    Food food = new Food();
                    food.setId(cursor.getLong(0));
                    food.setDescripcion_alimento(cursor.getString(1));
                    food.setGrupo_alimentos(cursor.getString(2));
                    food.setHidratos_carbono(cursor.getFloat(3));
                    food.setProteinas(cursor.getFloat(4));
                    food.setGrasas(cursor.getFloat(5));
                    food.setGramos(cursor.getInt(6));
                    food.setCalorias(cursor.getInt(7));
                    food.setState(cursor.getInt(8));
                    food.setTipo_alimento(cursor.getInt(9));
                    food.setFibras(cursor.getFloat(10));
                    food.setHierro(cursor.getFloat(11));
                    food.setCalcio(cursor.getFloat(12));
                    food.setVitamina_a(cursor.getFloat(13));
                    food.setVitamina_b1(cursor.getFloat(14));
                    food.setVitamina_c(cursor.getFloat(15));
                    food.setRivoflavina(cursor.getFloat(16));
                    food.setNiacina(cursor.getFloat(17));
                    food.setVitamina_e(cursor.getFloat(18));
                    foodList.add(food);
                    cursor.moveToNext();
                }
            }
            db.setTransactionSuccessful();
            Log.d("DBManager", "Valor de count: " + cursor.getCount() + " valor de size " + foodList.size());
        } catch (SQLiteException sqlError){
            Log.e("DBManager", "Error get foods by category from " + TABLE_FOODS);
        } finally{
            db.endTransaction();
            db.close(); // Closing database connection
            return foodList;
        }
    }

    // Get Food by balance
    public List<Food> getFoodByBalance(ExtensionsBalancerPlus extensionsBalancerPlus, float protein) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Food> foodList = new ArrayList<>();
        String query = "";
        Cursor cursor = null;
        try{
            db.beginTransaction();
            query = "SELECT _id, descripcion_alimento, " +
                    "grupo_alimentos, " +
                    "hidratos_carbono, " +
                    "proteinas, " +
                    "grasas, " +
                    "gramos, " +
                    "calorias, " +
                    "state, " +
                    "tipo_alimento, " +
                    "fibras, " +
                    "hierro, " +
                    "calcio, " +
                    "vitamina_a, " +
                    "vitamina_b1, " +
                    "vitamina_c, " +
                    "rivoflavina, " +
                    "niacina, " +
                    "vitamina_e " +
                    "FROM " + TABLE_FOODS;
            if (protein > extensionsBalancerPlus.getLimite_proteinas()) {
                query += " WHERE ((proteinas * " + extensionsBalancerPlus.getMultiplica() + " )/(hidratos_carbono + proteinas)) < "  + extensionsBalancerPlus.getLimite_hidratos();
            } else if (protein < extensionsBalancerPlus.getLimite_hidratos()) {
                query += " WHERE ((proteinas * " + extensionsBalancerPlus.getMultiplica() + ")/(hidratos_carbono + proteinas)) > "  + extensionsBalancerPlus.getLimite_proteinas();
            }
            query +=  " AND state = 1 AND tipo_alimento = 0 ORDER BY descripcion_alimento;";
            cursor = db.rawQuery(query, null);

            if (cursor .moveToFirst()) {
                while (cursor.isAfterLast() == false) {
                    Food food = new Food();
                    food.setId(cursor.getLong(0));
                    food.setDescripcion_alimento(cursor.getString(1));
                    food.setGrupo_alimentos(cursor.getString(2));
                    food.setHidratos_carbono(cursor.getFloat(3));
                    food.setProteinas(cursor.getFloat(4));
                    food.setGrasas(cursor.getFloat(5));
                    food.setGramos(cursor.getInt(6));
                    food.setCalorias(cursor.getInt(7));
                    food.setState(cursor.getInt(8));
                    food.setTipo_alimento(cursor.getInt(9));
                    food.setFibras(cursor.getFloat(10));
                    food.setHierro(cursor.getFloat(11));
                    food.setCalcio(cursor.getFloat(12));
                    food.setVitamina_a(cursor.getFloat(13));
                    food.setVitamina_b1(cursor.getFloat(14));
                    food.setVitamina_c(cursor.getFloat(15));
                    food.setRivoflavina(cursor.getFloat(16));
                    food.setNiacina(cursor.getFloat(17));
                    food.setVitamina_e(cursor.getFloat(18));
                    foodList.add(food);
                    cursor.moveToNext();
                }
            }
            db.setTransactionSuccessful();
            Log.d("DBManager", "Valor de count: " + cursor.getCount() + " valor de size " + foodList.size());
        } catch (SQLiteException sqlError){
            Log.e("DBManager", "Error get foods by category from " + TABLE_FOODS);
        } finally{
            db.endTransaction();
            db.close(); // Closing database connection
            return foodList;
        }
    }

    public int getFoodCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Food food = null;
        Cursor cursor = null;
        try{
            cursor = db.rawQuery("SELECT _id, descripcion_alimento, " +
                    "grupo_alimentos, " +
                    "hidratos_carbono, " +
                    "proteinas, " +
                    "grasas, " +
                    "gramos, " +
                    "calorias, " +
                    "state, " +
                    "tipo_alimento, " +
                    "fibras, " +
                    "hierro, " +
                    "calcio, " +
                    "vitamina_a, " +
                    "vitamina_b1, " +
                    "vitamina_c, " +
                    "rivoflavina, " +
                    "niacina, " +
                    "vitamina_e " +
                    " FROM " + TABLE_FOODS + ";", null);
            return cursor.getCount();
        } catch (SQLiteException sqlError){
            Log.e("DBManager", "Error get count from " + TABLE_FOODS);
            return -1;
        } finally{
            db.close(); // Closing database connection
        }
    }

        public void updateFood(Food food){
            SQLiteDatabase db = this.getWritableDatabase();
            String[] args = {String.valueOf(food.getId())};
            try{
                ContentValues values = new ContentValues();
                values.put("gramos", String.valueOf(food.getGramos()));

                db.update(TABLE_FOODS, values, "_id = ?", args);
            } catch (SQLiteException sqlError){
                Log.e("DBManager", "Error updating table " + TABLE_FOODS);
            } finally{
                db.close(); // Closing database connection
            }
        }

     // Get Statistics
    public Statistics getStatistics(int idUser) {
        SQLiteDatabase db = this.getReadableDatabase();
        Statistics statistics = null;
        Cursor cursor = null;
        try{
            cursor = db.rawQuery("SELECT _id, user_id, last_access FROM " + TABLE_STATISTICS + " WHERE user_id = '" + idUser + "';", null);

            if (cursor.moveToFirst()) {
                statistics = Statistics.getInstance(statistics);
                statistics.set_id(cursor.getLong(0));
                statistics.setUser_id(cursor.getInt(1));
                statistics.setLast_access(cursor.getString(2));
            }

        } catch (SQLiteException sqlError){
            Log.e("DBManager", "Error select from " + TABLE_STATISTICS);
        } finally{
            db.close(); // Closing database connection
            return statistics;
        }
    }

    public void updateStatistics(Statistics statistics){
        SQLiteDatabase db = this.getWritableDatabase();
        String[] args = {String.valueOf(statistics.getUser_id())};
        try{
            ContentValues values = new ContentValues();
            values.put("last_access", String.valueOf(statistics.getLast_access()));

            db.update(TABLE_STATISTICS, values, "user_id = ?", args);
        } catch (SQLiteException sqlError){
            Log.e("DBManager", "Error updating table " + TABLE_STATISTICS);
        } finally{
            db.close(); // Closing database connection
        }
    }

    //TODO Generar metodos para Extension balancer plus

    // Add new ExtensionsBalancerPlus
    public long addExtensionsBalancerPlus(ExtensionsBalancerPlus extensionsBalancerPlus) {
        SQLiteDatabase db = this.getWritableDatabase();
        long id;
        try{
            ContentValues values = new ContentValues();
            values.put("_id", extensionsBalancerPlus.get_id());
            values.put("activado", extensionsBalancerPlus.getActivado());
            values.put("num_alimentos", extensionsBalancerPlus.getNum_alimentos());
            values.put("raciones", extensionsBalancerPlus.getRaciones());
            values.put("otros", extensionsBalancerPlus.getOtros());
            values.put("multiplica", extensionsBalancerPlus.getMultiplica());
            values.put("limite_hidratos", extensionsBalancerPlus.getLimite_hidratos());
            values.put("limite_proteinas", extensionsBalancerPlus.getLimite_proteinas());
            values.put("valor_qp", extensionsBalancerPlus.getValor_qp());
            values.put("valor_qr", extensionsBalancerPlus.getValor_qr());
            values.put("grasas", extensionsBalancerPlus.getGrasas());
            values.put("frutas", extensionsBalancerPlus.getFrutas());
            values.put("verduras", extensionsBalancerPlus.getVerduras());
            values.put("raices", extensionsBalancerPlus.getRaices());
            values.put("carnes", extensionsBalancerPlus.getCarnes());
            values.put("embutidos", extensionsBalancerPlus.getEmbutidos());
            values.put("mariscos", extensionsBalancerPlus.getMariscos());
            values.put("legumbres", extensionsBalancerPlus.getLegumbres());
            values.put("pescados", extensionsBalancerPlus.getPescados());
            values.put("cereales", extensionsBalancerPlus.getCereales());
            values.put("dulces", extensionsBalancerPlus.getDulces());
            values.put("bebidas", extensionsBalancerPlus.getBebidas());
            values.put("frutos_secos", extensionsBalancerPlus.getFrutos_secos());
            values.put("lacteos", extensionsBalancerPlus.getLacteos());
            values.put("salsas", extensionsBalancerPlus.getSalsas());
            values.put("huevos", extensionsBalancerPlus.getHuevos());
            values.put("codigo_personal", extensionsBalancerPlus.getCodigo_personal());
            values.put("maximo_alimentos", extensionsBalancerPlus.getMaximo_alimentos());
            values.put("lacteos_quesos_huevos_compensa2", extensionsBalancerPlus.getLacteos_quesos_huevos_compensa2());
            values.put("pescados_mariscos_compensa2", extensionsBalancerPlus.getPescados_mariscos_compensa2());
            values.put("carnes_compensa2", extensionsBalancerPlus.getCarnes_compensa2());
            values.put("embutidos_compensa2", extensionsBalancerPlus.getEmbutidos_compensa2());
            values.put("nueces_compensa2", extensionsBalancerPlus.getNueces_compensa2());
            values.put("legumbres_semillas_raices_compensa2", extensionsBalancerPlus.getLegumbres_semillas_raices_compensa2());
            values.put("cereales_pastas_compensa2", extensionsBalancerPlus.getCereales_pastas_compensa2());
            values.put("frutas_bebidas_compensa2", extensionsBalancerPlus.getFrutas_bebidas_compensa2());
            values.put("dulces_compensa2", extensionsBalancerPlus.getDulces_compensa2());
            values.put("patatas_compensa2", extensionsBalancerPlus.getPatatas_compensa2());
            values.put("exceso_hidratos_compensa2", extensionsBalancerPlus.getExceso_hidratos_compensa2());
            values.put("exceso_proteinas_compensa2", extensionsBalancerPlus.getExceso_proteinas_compensa2());
            values.put("link", extensionsBalancerPlus.getLink());
            values.put("link_compensa", extensionsBalancerPlus.getLink_compensa());
            values.put("link_equilibrador", extensionsBalancerPlus.getLink_equilibrador());
            values.put("link_equilibrador_plus", extensionsBalancerPlus.getLink_equilibrador_plus());
            values.put("desayuno_necesidades", extensionsBalancerPlus.getDesayuno_necesidades());
            values.put("almuerzo_necesidades", extensionsBalancerPlus.getAlmuerzo_necesidades());
            values.put("comida_necesidades", extensionsBalancerPlus.getComida_necesidades());
            values.put("merienda_necesidades", extensionsBalancerPlus.getMerienda_necesidades());
            values.put("cena_necesidades", extensionsBalancerPlus.getCena_necesidades());

            // Inserting Row
            id = db.insert(TABLE_EXTENSIONS_BALANCER_PLUS, null, values);
            return id;
        } catch (SQLiteException sqlError){
            Log.e("DBManager", "Error creating table " + TABLE_EXTENSIONS_BALANCER_PLUS);
            return -1;
        } finally{
            db.close(); // Closing database connection
        }
    }

    // Get ExtensionsBalancerPlus
    public ExtensionsBalancerPlus getExtensionsBalancerPlus(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        ExtensionsBalancerPlus extensionsBalancerPlus = null;
        Cursor cursor = null;
        try{
            cursor = db.rawQuery("SELECT _id," +
                    "activado," +
                    "num_alimentos," +
                    "raciones," +
                    "otros," +
                    "multiplica, " +
                    "limite_hidratos, " +
                    "limite_proteinas," +
                    "valor_qp," +
                    "valor_qr," +
                    "grasas, " +
                    "frutas, " +
                    "verduras, " +
                    "raices, " +
                    "carnes, " +
                    "embutidos, " +
                    "mariscos," +
                    "legumbres, " +
                    "pescados, " +
                    "cereales, " +
                    "dulces, " +
                    "bebidas, " +
                    "frutos_secos, " +
                    "lacteos, " +
                    "salsas, " +
                    "huevos, " +
                    "codigo_personal, " +
                    "maximo_alimentos, " +
                    "lacteos_quesos_huevos_compensa2, " +
                    "pescados_mariscos_compensa2, " +
                    "carnes_compensa2, " +
                    "embutidos_compensa2, " +
                    "nueces_compensa2, " +
                    "legumbres_semillas_raices_compensa2, " +
                    "cereales_pastas_compensa2, " +
                    "frutas_bebidas_compensa2, " +
                    "dulces_compensa2, " +
                    "patatas_compensa2, " +
                    "exceso_hidratos_compensa2, " +
                    "exceso_proteinas_compensa2," +
                    "link, " +
                    "link_compensa, " +
                    "link_equilibrador, " +
                    "link_equilibrador_plus, " +
                    "desayuno_necesidades, " +
                    "almuerzo_necesidades, " +
                    "comida_necesidades, " +
                    "merienda_necesidades, " +
                    "cena_necesidades " +
                    "FROM " + TABLE_EXTENSIONS_BALANCER_PLUS + " WHERE _id = '" + id + "';", null);

            if (cursor.moveToFirst()) {
                extensionsBalancerPlus = ExtensionsBalancerPlus.getInstance(extensionsBalancerPlus);
                extensionsBalancerPlus.set_id(cursor.getLong(0));
                extensionsBalancerPlus.setActivado(cursor.getInt(1));
                extensionsBalancerPlus.setNum_alimentos(cursor.getInt(2));
                extensionsBalancerPlus.setRaciones(cursor.getInt(3));
                extensionsBalancerPlus.setOtros(cursor.getString(4));
                extensionsBalancerPlus.setMultiplica(cursor.getInt(5));
                extensionsBalancerPlus.setLimite_hidratos(cursor.getFloat(6));
                extensionsBalancerPlus.setLimite_proteinas(cursor.getFloat(7));
                extensionsBalancerPlus.setValor_qp(cursor.getFloat(8));
                extensionsBalancerPlus.setValor_qr(cursor.getFloat(9));
                extensionsBalancerPlus.setGrasas(cursor.getString(10));
                extensionsBalancerPlus.setFrutas(cursor.getString(11));
                extensionsBalancerPlus.setVerduras(cursor.getString(12));
                extensionsBalancerPlus.setRaices(cursor.getString(13));
                extensionsBalancerPlus.setCarnes(cursor.getString(14));
                extensionsBalancerPlus.setEmbutidos(cursor.getString(15));
                extensionsBalancerPlus.setMariscos(cursor.getString(16));
                extensionsBalancerPlus.setLegumbres(cursor.getString(17));
                extensionsBalancerPlus.setPescados(cursor.getString(18));
                extensionsBalancerPlus.setCereales(cursor.getString(19));
                extensionsBalancerPlus.setDulces(cursor.getString(20));
                extensionsBalancerPlus.setBebidas(cursor.getString(21));
                extensionsBalancerPlus.setFrutos_secos(cursor.getString(22));
                extensionsBalancerPlus.setLacteos(cursor.getString(23));
                extensionsBalancerPlus.setSalsas(cursor.getString(24));
                extensionsBalancerPlus.setHuevos(cursor.getString(25));
                extensionsBalancerPlus.setCodigo_personal(cursor.getString(26));
                extensionsBalancerPlus.setMaximo_alimentos(cursor.getInt(27));
                extensionsBalancerPlus.setLacteos_quesos_huevos_compensa2(cursor.getString(28));
                extensionsBalancerPlus.setPescados_mariscos_compensa2(cursor.getString(29));
                extensionsBalancerPlus.setCarnes_compensa2(cursor.getString(30));
                extensionsBalancerPlus.setEmbutidos_compensa2(cursor.getString(31));
                extensionsBalancerPlus.setNueces_compensa2(cursor.getString(32));
                extensionsBalancerPlus.setLegumbres_semillas_raices_compensa2(cursor.getString(33));
                extensionsBalancerPlus.setCereales_pastas_compensa2(cursor.getString(34));
                extensionsBalancerPlus.setFrutas_bebidas_compensa2(cursor.getString(35));
                extensionsBalancerPlus.setDulces_compensa2(cursor.getString(36));
                extensionsBalancerPlus.setPatatas_compensa2(cursor.getString(37));
                extensionsBalancerPlus.setExceso_hidratos_compensa2(cursor.getFloat(38));
                extensionsBalancerPlus.setExceso_proteinas_compensa2(cursor.getFloat(39));
                extensionsBalancerPlus.setLink(cursor.getString(40));
                extensionsBalancerPlus.setLink_compensa(cursor.getString(41));
                extensionsBalancerPlus.setLink_equilibrador(cursor.getString(42));
                extensionsBalancerPlus.setLink_equilibrador_plus(cursor.getString(43));
                extensionsBalancerPlus.setDesayuno_necesidades(cursor.getFloat(44));
                extensionsBalancerPlus.setAlmuerzo_necesidades(cursor.getFloat(45));
                extensionsBalancerPlus.setComida_necesidades(cursor.getFloat(46));
                extensionsBalancerPlus.setMerienda_necesidades(cursor.getFloat(47));
                extensionsBalancerPlus.setCena_necesidades(cursor.getFloat(48));
            }

        } catch (SQLiteException sqlError){
            Log.e("DBManager", "Error select from " + TABLE_EXTENSIONS_BALANCER_PLUS);
        } finally{
            db.close(); // Closing database connection
            return extensionsBalancerPlus;
        }
    }

    // Add Statistics
    public void addStatistics(Statistics statistics) {
        SQLiteDatabase db = this.getWritableDatabase();
        try{
            ContentValues values = new ContentValues();
            values.put("user_id", statistics.getUser_id());
            values.put("last_access", String.valueOf(statistics.getLast_access()));
            // Inserting Row
            db.insert(TABLE_STATISTICS, null, values);
        } catch (SQLiteException sqlError){
            Log.e("DBManager", "Error creating table " + TABLE_STATISTICS);
        } finally{
            db.close(); // Closing database connection
        }
    }

    // Add new personal food
    public void addPersonalFoods(PersonalFood personalfood) {
        SQLiteDatabase db = this.getWritableDatabase();
        try{
            ContentValues values = new ContentValues();
            values.put("_id", personalfood.getId());
            values.put("id_user", personalfood.getId_user());
            values.put("descripcion_alimento", personalfood.getDescripcion_alimento());
            values.put("peso_neto", personalfood.getPeso_neto());
            values.put("hidratos_carbono", personalfood.getHidratos_carbono());
            values.put("proteinas", personalfood.getProteinas());
            values.put("grasas", personalfood.getGrasas());
            values.put("ubicacion", personalfood.getUbicacion());
            values.put("calorias", personalfood.getCalorias());
            db.insert(TABLE_PERSONAL_FOODS, null, values);
        } catch (SQLiteException sqlError){
            Log.e("DBManager", "Error creating table " + TABLE_PERSONAL_FOODS);
        } finally{
            db.close();
        }
    }

    // Get ExtensionsProfile
    public PersonalFood getPersonalFood(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        PersonalFood personalFood = null;
        Cursor cursor = null;
        try{
            cursor = db.rawQuery("SELECT _id, id_user, descripcion_alimento, " +
                    "peso_neto, " +
                    "hidratos_carbono, " +
                    "proteinas, " +
                    "grasas, " +
                    "ubicacion, " +
                    "calorias " +
                    "FROM " + TABLE_PERSONAL_FOODS + " WHERE _id = '" + id + "';", null);

            if (cursor.moveToFirst()) {
                personalFood = new PersonalFood();
                personalFood.setId(cursor.getLong(0));
                personalFood.setId_user(cursor.getLong(1));
                personalFood.setDescripcion_alimento(cursor.getString(2));
                personalFood.setPeso_neto(cursor.getInt(3));
                personalFood.setHidratos_carbono(cursor.getFloat(4));
                personalFood.setProteinas(cursor.getFloat(5));
                personalFood.setGrasas(cursor.getFloat(6));
                personalFood.setUbicacion(cursor.getInt(7));
                personalFood.setCalorias(cursor.getInt(8));
            }
        } catch (SQLiteException sqlError){
            Log.e("DBManager", "Error select from " + TABLE_PERSONAL_FOODS);
        } finally{
            db.close(); // Closing database connection
            return personalFood;
        }
    }

    // Get personal food by location
    public List<PersonalFood> getPersonalFoodByLocation(int location) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<PersonalFood> personalFoodsList = new ArrayList<>();
        Cursor cursor = null;
        try{
            db.beginTransaction();
            cursor = db.rawQuery("SELECT _id, id_user, descripcion_alimento, " +
                    "peso_neto, " +
                    "hidratos_carbono, " +
                    "proteinas, " +
                    "grasas, " +
                    "ubicacion, " +
                    "calorias " +
                    "FROM " + TABLE_PERSONAL_FOODS + " WHERE ubicacion = " + location + ";", null);

            if (cursor .moveToFirst()) {
                while (cursor.isAfterLast() == false) {
                    PersonalFood personalFood = new PersonalFood();
                    personalFood.setId(cursor.getLong(0));
                    personalFood.setId_user(cursor.getLong(1));
                    personalFood.setDescripcion_alimento(cursor.getString(2));
                    personalFood.setPeso_neto(cursor.getInt(3));
                    personalFood.setHidratos_carbono(cursor.getFloat(4));
                    personalFood.setProteinas(cursor.getFloat(5));
                    personalFood.setGrasas(cursor.getFloat(6));
                    personalFood.setUbicacion(cursor.getInt(7));
                    personalFood.setCalorias(cursor.getInt(8));
                    personalFoodsList.add(personalFood);
                    cursor.moveToNext();
                }
            }
            db.setTransactionSuccessful();
            Log.d("DBManager", "Valor de count: " + cursor.getCount() + " valor de size " + personalFoodsList.size());
        } catch (SQLiteException sqlError){
            Log.e("DBManager", "Error get foods by category from " + TABLE_PERSONAL_FOODS);
        } finally{
            db.endTransaction();
            db.close(); // Closing database connection
            return personalFoodsList;
        }
    }

    public void updatePersonalFood(PersonalFood personalFood){
        SQLiteDatabase db = this.getWritableDatabase();
        String[] args = {String.valueOf(personalFood.getId())};
        try{
            ContentValues values = new ContentValues();
            values.put("descripcion_alimento", personalFood.getDescripcion_alimento());
            values.put("peso_neto", String.valueOf(personalFood.getPeso_neto()));
            values.put("hidratos_carbono", String.valueOf(personalFood.getHidratos_carbono()));
            values.put("proteinas", String.valueOf(personalFood.getProteinas()));
            values.put("grasas", personalFood.getGrasas());
            values.put("ubicacion", personalFood.getUbicacion());
            values.put("calorias", personalFood.getCalorias());

            db.update(TABLE_PERSONAL_FOODS, values, "_id = ?", args);
        } catch (SQLiteException sqlError){
            Log.e("DBManager", "Error updating table " + TABLE_PERSONAL_FOODS);
        } finally{
            db.close(); // Closing database connection
        }
    }

    // Delete Category
    public void deletePersonalFood(long idPersonalFood) {
        SQLiteDatabase db = this.getWritableDatabase();
        try{
            assert db != null;
            db.delete(TABLE_PERSONAL_FOODS, "_id = " + idPersonalFood, null);
        } catch (SQLiteException sqlError){
            Toast toast = Toast.makeText(this.myContext, R.string.deleteError, Toast.LENGTH_SHORT);
            toast.show();
        } finally{
            db.close(); // Closing database connection
        }
    }
}
