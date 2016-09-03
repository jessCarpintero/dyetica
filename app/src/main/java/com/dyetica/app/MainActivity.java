package com.dyetica.app;

import android.app.ActionBar;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.AutoText;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.dyetica.app.fragments.BalancerPlusFragment;
import com.dyetica.app.fragments.BasesObjectivesFragment;
import com.dyetica.app.fragments.BlogFragment;
import com.dyetica.app.fragments.DyeticaFragment;
import com.dyetica.app.fragments.ForoFragment;
import com.dyetica.app.fragments.HelpFragment;
import com.dyetica.app.fragments.ProfileFragment;

public class MainActivity extends AppCompatActivity
        implements BasesObjectivesFragment.OnFragmentInteractionListener, BlogFragment.OnFragmentInteractionListener,
        ForoFragment.OnFragmentInteractionListener, HelpFragment.OnFragmentInteractionListener,
        BalancerPlusFragment.OnFragmentInteractionListener, DyeticaFragment.OnFragmentInteractionListener,
        ProfileFragment.OnFragmentInteractionListener, NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer;
    Toolbar toolbar;
    ActionBarDrawerToggle toggle;

   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

       //First fragment
       setFragment(0);
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
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
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
        FragmentManager fragmentManager;
        FragmentTransaction fragmentTransaction;
        switch (position) {
            case 0:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                BalancerPlusFragment balancerPlusFragment = new BalancerPlusFragment();
                fragmentTransaction.replace(R.id.all_fragments, balancerPlusFragment);
                fragmentTransaction.commit();
                restoreActionBar(getString(R.string.app_name));
                break;
            case 1:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                ProfileFragment profileFragment = new ProfileFragment();
                fragmentTransaction.replace(R.id.all_fragments, profileFragment);
                fragmentTransaction.commit();
                restoreActionBar(getString(R.string.title_profile));
                break;
            case 2:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                DyeticaFragment dyeticaFragment = new DyeticaFragment();
                fragmentTransaction.replace(R.id.all_fragments, dyeticaFragment);
                fragmentTransaction.commit();
                restoreActionBar(getString(R.string.title_dyetica));
                break;
            case 3:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                BasesObjectivesFragment basesObjectivesFragment = new BasesObjectivesFragment();
                fragmentTransaction.replace(R.id.all_fragments, basesObjectivesFragment);
                fragmentTransaction.commit();
                restoreActionBar(getString(R.string.title_bases_and_objectives));
                break;
            case 4:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                BlogFragment blogFragment = new BlogFragment();
                fragmentTransaction.replace(R.id.all_fragments, blogFragment);
                fragmentTransaction.commit();
                restoreActionBar(getString(R.string.title_blog));
                break;
            case 5:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                ForoFragment foroFragment = new ForoFragment();
                fragmentTransaction.replace(R.id.all_fragments, foroFragment);
                fragmentTransaction.commit();
                restoreActionBar(getString(R.string.title_foro));
                break;
            case 6:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                HelpFragment helpFragment = new HelpFragment();
                fragmentTransaction.replace(R.id.all_fragments, helpFragment);
                fragmentTransaction.commit();
                restoreActionBar(getString(R.string.title_help));
                break;
        }
    }

    /**
     * Resets the status bar
     */
    public void restoreActionBar(String mTitle) {
        toolbar.setTitle(mTitle);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
