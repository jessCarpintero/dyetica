package com.dyetica.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
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
import android.widget.TabHost;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.dyetica.app.fragments.BalancerPlusFragment;
import com.dyetica.app.fragments.BasesObjectivesFragment;
import com.dyetica.app.fragments.BlogFragment;
import com.dyetica.app.fragments.DieteticProfile2Fragment;
import com.dyetica.app.fragments.DieteticProfileFragment;
import com.dyetica.app.fragments.DyeticaFragment;
import com.dyetica.app.fragments.ForoFragment;
import com.dyetica.app.fragments.HelpFragment;
import com.dyetica.app.fragments.ProfileFragment;
import com.dyetica.app.model.DieteticProfile;
import com.dyetica.app.model.User;
import com.dyetica.app.persistence.ClientHTTP;
import com.dyetica.app.persistence.DBManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity
        implements BasesObjectivesFragment.OnFragmentInteractionListener, BlogFragment.OnFragmentInteractionListener,
        ForoFragment.OnFragmentInteractionListener, HelpFragment.OnFragmentInteractionListener,
        BalancerPlusFragment.OnFragmentInteractionListener, DyeticaFragment.OnFragmentInteractionListener,
        ProfileFragment.OnFragmentInteractionListener, NavigationView.OnNavigationItemSelectedListener,
        DieteticProfileFragment.OnFragmentInteractionListener,
        DieteticProfile2Fragment.OnFragmentInteractionListener{

    private static final String CURRENT_FRAGMENT_KEY = "current_fragment";
    private static final String TAB_BALANCER_PLUS = "tab_balancer_plus";
    private static final String TAB_DIETETIC_PROFILE = "tab_dietetic_profile";
    private static final String TAB_DIETETIC_PROFILE_2 = "tab_dietetic_profile_2";

    private TextView mUserName;
    private TextView mUserEmail;
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private ActionBarDrawerToggle toggle;
    private DBManager dbManager;
    private FragmentTabHost tabHost;
    private int currentFragment;
    private SharedPreferences prefs;
    private AttemptDieteticProfile mAuthTask = null;


    //Fragments
    private BalancerPlusFragment balancerPlusFragment;
    private DieteticProfileFragment dieteticProfileFragment;
    private DieteticProfile2Fragment dieteticProfile2Fragment;
    private ProfileFragment profileFragment;
    private DyeticaFragment dyeticaFragment;
    private BasesObjectivesFragment basesObjectivesFragment;
    private BlogFragment blogFragment;
    private ForoFragment foroFragment;
    private HelpFragment helpFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prefs = this.getSharedPreferences("myPreferences", Context.MODE_PRIVATE);
        dbManager = DBManager.getInstance(this);
        dbManager.getWritableDatabase();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_menu_manage));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //What to do on back clicked
            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        createTabHost();
        createIconAndTextUser();

        attemptDieteticProfile();

        if(savedInstanceState != null) {
           currentFragment = savedInstanceState.getInt(CURRENT_FRAGMENT_KEY);
           Log.d("MainActivity", "Dentro de savedInstanceState != null, valor de current_fragment: " + currentFragment);
       } else {
           Log.d("MainActivity", "valor de current_fragment PRIMERA VEZ: " + currentFragment);
           currentFragment = 0;
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

    private void createTabHost(){
        tabHost= (FragmentTabHost) findViewById(android.R.id.tabhost);
        tabHost.setup(this,
                getSupportFragmentManager(),android.R.id.tabcontent);
        tabHost.addTab(tabHost.newTabSpec(TAB_BALANCER_PLUS).setIndicator(getString(R.string.balancerPlus)),
                BalancerPlusFragment.class, null);
        tabHost.addTab(tabHost.newTabSpec(TAB_DIETETIC_PROFILE).setIndicator(getString(R.string.dietetic_profile)),
                DieteticProfileFragment.class, null);
        tabHost.addTab(tabHost.newTabSpec(TAB_DIETETIC_PROFILE_2).setIndicator(getString(R.string.dietetic_profile_2)),
                DieteticProfile2Fragment.class, null);



        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tab) {
                switch (tab){
                    case TAB_BALANCER_PLUS:
                        setFragment(0);
                        break;
                    case TAB_DIETETIC_PROFILE:
                        setFragment(7);
                        break;
                    case TAB_DIETETIC_PROFILE_2:
                        setFragment(8);
                        break;
                }
            }
        });
    }

    private void createIconAndTextUser(){
        User user = dbManager.getUser(prefs.getInt("idUser", 0));
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //@drawable/bg_menu_dyetica

        View headerLayout = navigationView.inflateHeaderView(R.layout.nav_header_main);
        headerLayout.setBackgroundResource(R.drawable.bg_menu_dyetica);

        //TODO: Revisar porque no recoge los elementos como toca
        mUserName = (TextView) headerLayout.findViewById(R.id.textViewUserName);
        mUserName.setText(user.getUsername());
        mUserEmail = (TextView) headerLayout.findViewById(R.id.textViewUserEmail);
        mUserEmail.setText(user.getEmail());

        TextDrawable.IShapeBuilder drawable1 = TextDrawable.builder();
        Log.d("MainActivity", "Valor de drawable1 " + drawable1.toString());
        TextDrawable drawable = drawable1.buildRound(user.getUsername().substring(0,1).toUpperCase(), Color.BLUE);


        Log.d("MainActivity", "Valor de drawable1 " + drawable.toString());

        ImageView image = (ImageView) headerLayout.findViewById(R.id.imageViewLogoUser);
        Log.d("MainActivity", "Valor de drawable1 " + image.toString());

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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        Log.d("MainAcrivity", "Demtrp de onCreateOptionsMenu");
        //TODO: Aqui iran diferentes if para los fragments que necesiten tener botones en el toolbar
        if (!drawer.isDrawerOpen(GravityCompat.START) && currentFragment == 0 ) {
            restoreActionBar(getString(R.string.app_name));
            tabHost.setVisibility(View.VISIBLE);
        } else if (!drawer.isDrawerOpen(GravityCompat.START) && currentFragment == 1) {
            restoreActionBar(getString(R.string.title_profile));
            tabHost.setVisibility(View.INVISIBLE);
        }  else if (!drawer.isDrawerOpen(GravityCompat.START) && currentFragment == 2) {
            restoreActionBar(getString(R.string.title_dyetica));
            tabHost.setVisibility(View.INVISIBLE);
        }  else if (!drawer.isDrawerOpen(GravityCompat.START) && currentFragment == 3) {
            restoreActionBar(getString(R.string.title_bases_and_objectives));
            tabHost.setVisibility(View.INVISIBLE);
        }  else if (!drawer.isDrawerOpen(GravityCompat.START) && currentFragment == 4) {
            restoreActionBar(getString(R.string.title_blog));
            tabHost.setVisibility(View.INVISIBLE);
        }  else if (!drawer.isDrawerOpen(GravityCompat.START) && currentFragment == 5) {
            restoreActionBar(getString(R.string.title_foro));
            tabHost.setVisibility(View.INVISIBLE);
        }  else if (!drawer.isDrawerOpen(GravityCompat.START) && currentFragment == 6) {
            restoreActionBar(getString(R.string.title_help));
            tabHost.setVisibility(View.INVISIBLE);
        }
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //TODO: Aqui se hace la lógica de los botones del toolbar
        switch (item.getItemId()) {
            case android.R.id.home:
                drawer.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
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

        switch (position) {
            case 0:
                if (null == balancerPlusFragment) balancerPlusFragment = new BalancerPlusFragment();
                replaceFragment(balancerPlusFragment, bundle, getString(R.string.app_name));
                tabHost.setVisibility(View.VISIBLE);
                break;
            case 1:
                if (null == profileFragment) profileFragment = new ProfileFragment();
                bundle.putInt("idUser", prefs.getInt("idUser", 0));
                replaceFragment(profileFragment, bundle, getString(R.string.title_profile));
                tabHost.setVisibility(View.INVISIBLE);
                break;
            case 2:
                if (null == dyeticaFragment) dyeticaFragment = new DyeticaFragment();
                replaceFragment(dyeticaFragment, bundle, getString(R.string.title_dyetica));
                tabHost.setVisibility(View.INVISIBLE);
                break;
            case 3:
                if (null == basesObjectivesFragment) basesObjectivesFragment = new BasesObjectivesFragment();
                replaceFragment(basesObjectivesFragment, bundle, getString(R.string.title_bases_and_objectives));
                tabHost.setVisibility(View.INVISIBLE);
                break;
            case 4:
                if (null == blogFragment) blogFragment = new BlogFragment();
                replaceFragment(blogFragment, bundle, getString(R.string.title_blog));
                tabHost.setVisibility(View.INVISIBLE);
                break;
            case 5:
                if (null == foroFragment) foroFragment = new ForoFragment();
                replaceFragment(foroFragment, bundle, getString(R.string.title_foro));
                tabHost.setVisibility(View.INVISIBLE);
                break;
            case 6:
                if (null == helpFragment) helpFragment = new HelpFragment();
                replaceFragment(helpFragment, bundle, getString(R.string.title_help));
                tabHost.setVisibility(View.INVISIBLE);
                break;
            case 7:
                if (null == dieteticProfileFragment) dieteticProfileFragment = new DieteticProfileFragment();
                Log.d("MainActivity", "Entrando por dieteticProfileFragment, VALOR de id_user: " +  prefs.getInt("idUser", 0));
                bundle.putInt("idUser", prefs.getInt("idUser", 0));
                replaceFragment(dieteticProfileFragment, bundle, getString(R.string.app_name));
                tabHost.setVisibility(View.VISIBLE);
                break;
            case 8:
                if (null == dieteticProfile2Fragment) dieteticProfile2Fragment = new DieteticProfile2Fragment();
                Log.d("MainActivity", "Entrando por dieteticProfileFragment2");
                bundle.putInt("idUser", prefs.getInt("idUser", 0));
                replaceFragment(dieteticProfile2Fragment, bundle, getString(R.string.app_name));
                tabHost.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void replaceFragment(Fragment newFragment, Bundle bundle, String titleToolbar) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (!newFragment.isVisible()) {
            Log.d("MainActivity", "REPLACE FRAGMENTE: " + newFragment.toString());

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
        Log.d("MainActivity", "MODIFICAMOS EL TOOLBAR: " + mTitle );
        toolbar.setTitle(mTitle);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CURRENT_FRAGMENT_KEY, currentFragment);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        currentFragment = savedInstanceState.getInt(CURRENT_FRAGMENT_KEY);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

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
                            Log.d("MainActivity", "Valor de idUser " + prefs.getInt("idUser", 0) +  "y de idPerfil " + i + 1);
                            if (dbManager.getDieteticProfile(prefs.getInt("idUser", 0), i + 1) == null) {
                                dbManager.addDieteticProfile(dieteticProfile);
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
