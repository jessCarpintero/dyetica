package com.dyetica.app;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.dyetica.app.dialogs.AddFoodDialogFragment;
import com.dyetica.app.dialogs.BalanceDialogFragment;
import com.dyetica.app.dialogs.CreateFoodDialogFragment;
import com.dyetica.app.dialogs.CustomDialogFragment;
import com.dyetica.app.dialogs.ShowInfoDialogFragment;
import com.dyetica.app.fragments.BalancerPlusFragment;
import com.dyetica.app.fragments.BasesObjectivesFragment;
import com.dyetica.app.fragments.BlogFragment;
import com.dyetica.app.fragments.DieteticsProfileFragment;
import com.dyetica.app.fragments.DyeticaFragment;
import com.dyetica.app.fragments.ForoFragment;
import com.dyetica.app.fragments.HelpFragment;
import com.dyetica.app.fragments.ProfileFragment;
import com.dyetica.app.model.DieteticProfile;
import com.dyetica.app.model.User;
import com.dyetica.app.persistence.ClientHTTP;
import com.dyetica.app.persistence.DBManager;
import com.dyetica.app.utils.MethodsUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity
        implements BasesObjectivesFragment.OnFragmentInteractionListener, BlogFragment.OnFragmentInteractionListener,
        ForoFragment.OnFragmentInteractionListener, HelpFragment.OnFragmentInteractionListener,
        BalancerPlusFragment.OnFragmentInteractionListener, DyeticaFragment.OnFragmentInteractionListener,
        ProfileFragment.OnFragmentInteractionListener, NavigationView.OnNavigationItemSelectedListener,
        AddFoodDialogFragment.OnAddFoodDialogSelectedListener, BalanceDialogFragment.OnBalanceDialogSelectedListener,
        CreateFoodDialogFragment.OnCreateFoodDialogSelectedListener, CustomDialogFragment.OnCustomDialogSelectedListener{

    private static final String CURRENT_FRAGMENT_KEY = "current_fragment";
    private static final String PROFILE = "PROFILE";
    private static final String ID_USER = "idUser";
    private static final String KCAL = "kcal";
    private static final String FOOD_ID = "id_food";
    private static final String OLD_FOOD_ID = "id_food_old";
    private static final String FOOD_LIST = "food_list";
    private static final String PORTION = "portion";
    private static final String GRAMS = "grams";
    private static final String OIL = "oil";
    private static final String IS_PERSONAL_FOOD = "isPersonalFood";
    private static final String IS_UPDATED_FOOD = "is_updated_food";

    private boolean replaceFragmentZero = false;
    private TextView mUserName;
    private TextView mUserEmail;
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private ActionBarDrawerToggle toggle;
    private DBManager dbManager;
    private int currentFragment;
    private SharedPreferences prefs;
    private AttemptDieteticProfile mAuthTask = null;
    private int idDieteticProfile;
    private long idFood;
    private Long idFoodOld;
    private int grams, portion;
    private ArrayList<Parcelable> foodList;
    private float kcalPortion;

    //Fragments
    private BalancerPlusFragment balancerPlusFragment;
    private ProfileFragment profileFragment;
    private DyeticaFragment dyeticaFragment;
    private DieteticsProfileFragment dieteticsProfileFragment;
    private BasesObjectivesFragment basesObjectivesFragment;
    private BlogFragment blogFragment;
    private ForoFragment foroFragment;
    private HelpFragment helpFragment;
    private boolean isPersonalFood;
    private float oil;
    private Bundle savedInstanceState;
    private boolean isUpdateFood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
        setContentView(R.layout.activity_main);
        prefs = this.getSharedPreferences("myPreferences", Context.MODE_PRIVATE);
        dbManager = DBManager.getInstance(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        restoreActionBar(getString(R.string.title_balancer_plus));
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        createIconAndTextUser();
        attemptDieteticProfile();

        Intent intent = getIntent();
        idDieteticProfile = intent.getIntExtra(PROFILE, 0);
        if (0 != idDieteticProfile) {
            currentFragment = 9;
            setFragment(currentFragment);
        } else {
            if (savedInstanceState != null) {
                currentFragment = savedInstanceState.getInt(CURRENT_FRAGMENT_KEY);
            } else {
                currentFragment = 0;
            }
            idFood = intent.getLongExtra(FOOD_ID, 0);
            idFoodOld = intent.getLongExtra(OLD_FOOD_ID, 0);
            grams = intent.getIntExtra(GRAMS, 0);

            setFragment(currentFragment);
        }
    }

    private void attemptDieteticProfile() {
        mAuthTask = new AttemptDieteticProfile();
        try {
            mAuthTask.execute((Void) null).get();
        } catch (InterruptedException e) {
            Log.e("MainActivity", "Interrupted AttemptDieteticProfile");
        } catch (ExecutionException e) {
            Log.e("MainActivity", "Error execution in AttemptDieteticProfile");
        }
    }

    private void createIconAndTextUser(){
        User user = dbManager.getUser(prefs.getInt(ID_USER, 0));
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerLayout = navigationView.getHeaderView(0);

        mUserName = (TextView) headerLayout.findViewById(R.id.textViewUserName);
        mUserName.setText(user.getUsername());
        mUserEmail = (TextView) headerLayout.findViewById(R.id.textViewUserEmail);
        mUserEmail.setText(user.getEmail());

        TextDrawable.IShapeBuilder drawable1 = TextDrawable.builder();
        TextDrawable drawable = drawable1.buildRound(user.getUsername().substring(0,1).toUpperCase(), Color.argb(230,95,143,0));

        ImageView image = (ImageView) headerLayout.findViewById(R.id.imageViewLogoUser);
        image.setImageDrawable(drawable);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (drawer.isDrawerOpen(GravityCompat.START) && currentFragment == 0 ) {
            menu.getItem(0).setEnabled(true);
            menu.getItem(0).setVisible(true);
            restoreActionBar(getString(R.string.app_name));
        } else if (drawer.isDrawerOpen(GravityCompat.START) && currentFragment == 1) {
            menu.getItem(0).setEnabled(false);
            menu.getItem(0).setVisible(false);
            restoreActionBar(getString(R.string.title_profile));
        }  else if (drawer.isDrawerOpen(GravityCompat.START) && currentFragment == 2) {
            menu.getItem(0).setEnabled(false);
            menu.getItem(0).setVisible(false);
            restoreActionBar(getString(R.string.title_dyetica));
        }  else if (drawer.isDrawerOpen(GravityCompat.START) && currentFragment == 3) {
            menu.getItem(0).setEnabled(false);
            menu.getItem(0).setVisible(false);
            restoreActionBar(getString(R.string.title_bases_and_objectives));
        }  else if (drawer.isDrawerOpen(GravityCompat.START) && currentFragment == 4) {
            menu.getItem(0).setEnabled(false);
            menu.getItem(0).setVisible(false);
            restoreActionBar(getString(R.string.title_blog));
        }  else if (drawer.isDrawerOpen(GravityCompat.START) && currentFragment == 5) {
            menu.getItem(0).setEnabled(false);
            menu.getItem(0).setVisible(false);
            restoreActionBar(getString(R.string.title_foro));
        }  else if (drawer.isDrawerOpen(GravityCompat.START) && currentFragment == 6) {
            menu.getItem(0).setEnabled(false);
            menu.getItem(0).setVisible(false);
            restoreActionBar(getString(R.string.title_help));
        }   else if (drawer.isDrawerOpen(GravityCompat.START) && currentFragment == 6) {
            menu.getItem(0).setEnabled(true);
            menu.getItem(0).setVisible(true);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!drawer.isDrawerOpen(GravityCompat.START) && currentFragment == 0 ) {
            restoreActionBar(getString(R.string.app_name));
        } else if (!drawer.isDrawerOpen(GravityCompat.START) && currentFragment == 1) {
            restoreActionBar(getString(R.string.title_profile));
        }  else if (!drawer.isDrawerOpen(GravityCompat.START) && currentFragment == 2) {
            restoreActionBar(getString(R.string.title_dyetica));
        }  else if (!drawer.isDrawerOpen(GravityCompat.START) && currentFragment == 3) {
            restoreActionBar(getString(R.string.title_bases_and_objectives));
        }  else if (!drawer.isDrawerOpen(GravityCompat.START) && currentFragment == 4) {
            restoreActionBar(getString(R.string.title_blog));
        }  else if (!drawer.isDrawerOpen(GravityCompat.START) && currentFragment == 5) {
            restoreActionBar(getString(R.string.title_foro));
        }  else if (!drawer.isDrawerOpen(GravityCompat.START) && currentFragment == 6) {
            restoreActionBar(getString(R.string.title_help));
        }
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        // Aqui se hace la lógica de los botones del toolbar
        switch (item.getItemId()) {
            case R.id.action_link:
                if (currentFragment == 0){
                    openHelp(getString(R.string.link_balance));
                } else if (currentFragment == 9) {
                    openHelp(getString(R.string.link_dietetic_profile));
                }
                return true;
        }
        return super.onOptionsItemSelected(item);

    }

    private void openHelp(String link) {
        if (MethodsUtil.isConnected(getApplicationContext())) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
            startActivity(intent);
        } else {
            Toast.makeText(this, getString(R.string.error_connection), Toast.LENGTH_LONG).show();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        // Handle navigation view item clicks here.
        switch(menuItem.getItemId()){
            case R.id.nav_balancer_plus:
                menuItem.setChecked(true);
                setFragment(0);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            case R.id.nav_profile:
                menuItem.setChecked(true);
                setFragment(1);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            case R.id.nav_dyetica:
                menuItem.setChecked(true);
                setFragment(2);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            case R.id.nav_bases_and_obj:
                menuItem.setChecked(true);
                setFragment(3);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            case R.id.nav_blog:
                menuItem.setChecked(true);
                setFragment(4);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            case R.id.nav_foro:
                menuItem.setChecked(true);
                setFragment(5);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            case R.id.nav_help:
                menuItem.setChecked(true);
                setFragment(6);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            case R.id.nav_dietetic_profile:
                menuItem.setChecked(true);
                setFragment(9);
                drawer.closeDrawer(GravityCompat.START);
                return true;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setFragment(int position) {
        Bundle bundle = getIntent().getExtras();
        Log.d("MainActivity", "Valor de position: " + position);

        if (null == bundle) {
            bundle = new Bundle();
        }

        //Necessary to save the state
        currentFragment = position;

        if (0 != position) {
            grams = -1;
        }

        switch (position) {
            case 0:
                Log.d("MainActivity", "Dentro de setFragment 0");
                if (null == balancerPlusFragment) balancerPlusFragment = new BalancerPlusFragment();
                bundle.putLong(FOOD_ID, idFood);
                bundle.putLong(OLD_FOOD_ID, null != idFoodOld ? idFoodOld:0);
                bundle.putInt(GRAMS, grams);
                bundle.putInt(PORTION, portion);
                bundle.putBoolean(IS_PERSONAL_FOOD, isPersonalFood);
                bundle.putParcelableArrayList(FOOD_LIST, foodList);
                bundle.putFloat(KCAL, kcalPortion);
                bundle.putFloat(OIL, oil);
                bundle.putBoolean(IS_UPDATED_FOOD, isUpdateFood);
                if (savedInstanceState == null || replaceFragmentZero) {
                    replaceFragment(balancerPlusFragment, bundle, getString(R.string.title_balancer_plus));
                }
                break;
            case 1:
                if (null == profileFragment) profileFragment = new ProfileFragment();
                bundle.putInt(ID_USER, prefs.getInt(ID_USER, 0));
                savedInstanceState = null;
                replaceFragment(profileFragment, bundle, getString(R.string.title_profile));
                break;
            case 2:
                if (null == dyeticaFragment) dyeticaFragment = new DyeticaFragment();
                savedInstanceState = null;
                replaceFragment(dyeticaFragment, bundle, getString(R.string.title_dyetica));
                break;
            case 3:
                if (null == basesObjectivesFragment) basesObjectivesFragment = new BasesObjectivesFragment();
                savedInstanceState = null;
                replaceFragment(basesObjectivesFragment, bundle, getString(R.string.title_bases_and_objectives));
                break;
            case 4:
                if (null == blogFragment) blogFragment = new BlogFragment();
                savedInstanceState = null;
                replaceFragment(blogFragment, bundle, getString(R.string.title_blog));
                break;
            case 5:
                if (null == foroFragment) foroFragment = new ForoFragment();
                savedInstanceState = null;
                replaceFragment(foroFragment, bundle, getString(R.string.title_foro));
                break;
            case 6:
                if (null == helpFragment) helpFragment = new HelpFragment();
                savedInstanceState = null;
                replaceFragment(helpFragment, bundle, getString(R.string.title_help));
                break;
            case 9:
                if (null == dieteticsProfileFragment) dieteticsProfileFragment = new DieteticsProfileFragment();
                bundle.putInt(PROFILE, idDieteticProfile);
                savedInstanceState = null;
                replaceFragment(dieteticsProfileFragment, bundle, getString(R.string.title_perfiles_dieteticos));
                break;
        }
    }

    private void replaceFragment(Fragment newFragment, Bundle bundle, String titleToolbar) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            if (!newFragment.isVisible()) {

                if (bundle != null) {
                    newFragment.setArguments(bundle);
                    Bundle clearBundle = null;
                    getIntent().replaceExtras(clearBundle);
                }
                fragmentTransaction.replace(R.id.all_fragments, newFragment);
                fragmentTransaction.commit();
                restoreActionBar(titleToolbar);
        }
    }

    /**
     * Resets the status bar
     */
    public void restoreActionBar(String mTitle) {
       getSupportActionBar().setTitle(mTitle);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CURRENT_FRAGMENT_KEY, currentFragment);
        if (currentFragment != 0) {
            outState.putParcelableArrayList(FOOD_LIST, foodList);
            outState.putInt(PORTION, portion);
            outState.putLong(FOOD_ID, idFood);
            outState.putLong(OLD_FOOD_ID,  null != idFoodOld ? idFoodOld:0);
            outState.putInt(GRAMS, -1);
            outState.putBoolean(IS_PERSONAL_FOOD, isPersonalFood);
        }
        replaceFragmentZero = false;
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        currentFragment = savedInstanceState.getInt(CURRENT_FRAGMENT_KEY);
        if (currentFragment != 0) {
            foodList = savedInstanceState.getParcelableArrayList(FOOD_LIST);
            portion = savedInstanceState.getInt(PORTION);
            idFood = savedInstanceState.getLong(FOOD_ID);
            idFoodOld = savedInstanceState.getLong(OLD_FOOD_ID);
            grams = savedInstanceState.getInt(GRAMS);
            isPersonalFood = savedInstanceState.getBoolean(IS_PERSONAL_FOOD);
        }
        replaceFragmentZero = false;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
    @Override
    public void onPortion(int portion) {
        this.portion = portion;
    }

    @Override
    public void onFoodAndGramsSelected(Long idFood, Long idFoodOld, int grams, boolean isPersonalFood, int portion, float oil, boolean isUpdateFood) {
        Log.d("MainActivity", "VAlor de idFood: " + idFood);
        this.idFood = idFood;
        this.idFoodOld = idFoodOld;
        this.grams = grams;
        this.isPersonalFood = isPersonalFood;
        this.portion = portion;
        this.oil = oil;
        this.isUpdateFood = isUpdateFood;
        balancerPlusFragment = null;
        replaceFragmentZero = true;
        setFragment(0);
    }

    @Override
    public void saveFoodList(ArrayList<Parcelable> foodList, int portion, int grams, long idFood, long idFoodOld, boolean isPersonalFood, float oil) {
        Log.d("MainActivity", "Valor de foodList: " + foodList.size());
        this.foodList = foodList;
        this.portion = portion;
        this.idFood = idFood;
        this.idFoodOld = idFoodOld;
        this.grams = -1;
        this.isPersonalFood = isPersonalFood;
        replaceFragmentZero = false;
    }
    @Override
    public void onPortionAndKcal(int portion, float kcal, ArrayList<Parcelable> foodList, float oil) {
        this.portion = portion;
        this.foodList = foodList;
        this.kcalPortion = kcal;
        this.oil = oil;
        this.grams = -1;
        balancerPlusFragment = null;
        replaceFragmentZero = true;
        setFragment(0);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitByBackKey();

            //moveTaskToBack(false);

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void exitByBackKey() {

        AlertDialog alertbox = new AlertDialog.Builder(this)
                .setMessage(getString(R.string.do_you_want_to_exit))
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {

                    // do something when the button is clicked
                    public void onClick(DialogInterface arg0, int arg1) {
                        finish();
                        //close();
                    }
                })
                .setNegativeButton(getString(R.string.not), new DialogInterface.OnClickListener() {

                    // do something when the button is clicked
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                })
                .show();

    }

    class AttemptDieteticProfile extends AsyncTask<Void, Void, Map<String, String>> {
        private String error, message, statusCode;
        private Map<String, String> success = new HashMap<>();

        @Override
        protected Map<String, String> doInBackground(Void... args) {
            // Check for success tag
            try {
                // getting dietetic profile details by making HTTP request
                Map<String, String> response = ClientHTTP.makeHttpRequest(new URL(getString(R.string.url_dietetic_profile) + "0"), "GET",  prefs.getString("apiKey", ""), null);

                // check your log for json response
                statusCode = response.get(getString(R.string.status));
                success.put(getString(R.string.status), statusCode);
                if (!getString(R.string.status_200).equals(statusCode)){
                    success.put(getString(R.string.error), "true");
                    success.put(getString(R.string.message), getString(R.string.error_connection));
                } else {
                    error = response.get("error");
                    message = response.get("message");
                    if (error == "false") {
                        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                        JSONArray jsonArray = new JSONArray(message);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            DieteticProfile dieteticProfile = gson.fromJson(jsonArray.getString(i), DieteticProfile.class);
                            DieteticProfile dieteticProfileOld = dbManager.getDieteticProfile(prefs.getInt(ID_USER, 0), i + 1);
                            if (dieteticProfileOld == null) {
                                dbManager.addDieteticProfile(dieteticProfile);
                            } else {
                                dbManager.updateDieteticProfile(dieteticProfile, dieteticProfileOld.getId());
                            }
                        }
                    }
                    success.put(getString(R.string.error), error);
                    success.put(getString(R.string.message), message);
                }
            } catch (MalformedURLException e) {
               Log.e("MainActivity", "Url " + getString(R.string.url_dietetic_profile) + "0"  +  " is malformed");
            } catch (JSONException e) {
                Log.e("MainActivity", "Json exception in AttemptDieteticProfile");
            }
            return success;
        }

    }

}
