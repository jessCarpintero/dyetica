package com.dyetica.app.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TextView;

import com.dyetica.app.R;
import com.dyetica.app.utils.MethodsUtil;

public class DieteticsProfileFragment extends Fragment {
    private static final String TAB_BALANCER_PLUS = "tab_balancer_plus";
    private static final String TAB_DIETETIC_PROFILE = "tab_dietetic_profile";
    private static final String TAB_DIETETIC_PROFILE_2 = "tab_dietetic_profile_2";
    private static final String PROFILE = "PROFILE";
    private FragmentTabHost tabHost;
    private int idDieteticProfile;
    private int currentFragment;
    private Toolbar toolbar;
    private DieteticProfileFragment dieteticProfileFragment;
    private DieteticProfile2Fragment dieteticProfile2Fragment;
    private static final String ID_EXTENSIONS_PROFILE = "idExtensionsProfile";
    private static final String ID_EXTENSIONS_BALANCER_PLUS = "idExtensionsBalancerPlus";
    private static final String ID_USER = "idUser";
    private SharedPreferences prefs;
    private View rootView;

    public DieteticsProfileFragment() {
        // Required empty public constructor
    }

    public static DieteticsProfileFragment newInstance() {
        DieteticsProfileFragment fragment = new DieteticsProfileFragment();
        Bundle args = new Bundle();
       // fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(null != getArguments()) {
            idDieteticProfile = getArguments().getInt(PROFILE);
        }
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_dietetics_profile, container, false);
        prefs = getActivity().getSharedPreferences("myPreferences", Context.MODE_PRIVATE);
        createTabHost();
        return rootView;
    }

    private void createTabHost(){
        tabHost= (FragmentTabHost) rootView.findViewById(android.R.id.tabhost);
        tabHost.setup(getActivity(),
                getChildFragmentManager(), android.R.id.tabcontent);
        tabHost.addTab(tabHost.newTabSpec(TAB_DIETETIC_PROFILE).setIndicator(getString(R.string.dietetic_profile)),
                DieteticProfileFragment.class, null);
        tabHost.addTab(tabHost.newTabSpec(TAB_DIETETIC_PROFILE_2).setIndicator(getString(R.string.dietetic_profile_2)),
                DieteticProfile2Fragment.class, null);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        if (MethodsUtil.isTablet(getContext())) {
            if (tabHost.getTabWidget().getChildAt(0).getLayoutParams().height < 90) {
                tabHost.getTabWidget().getChildAt(0).getLayoutParams().height = (int) (45 * this.getResources().getDisplayMetrics().density);
            }
            tabHost.getTabWidget().getChildAt(0).setPadding(0,0,0,0);
            TextView x =(TextView) tabHost.getTabWidget().getChildAt(0).findViewById(android.R.id.title);
            x.setTextSize(15);

            if (tabHost.getTabWidget().getChildAt(1).getLayoutParams().height < 90) {
                tabHost.getTabWidget().getChildAt(1).getLayoutParams().height = (int) (45 * this.getResources().getDisplayMetrics().density);
            }
            tabHost.getTabWidget().getChildAt(1).setPadding(0,0,0,0);
            TextView x2 =(TextView) tabHost.getTabWidget().getChildAt(1).findViewById(android.R.id.title);
            x2.setTextSize(15);
        } else {
            tabHost.getTabWidget().getChildAt(0).getLayoutParams().height = (int) (40 * this.getResources().getDisplayMetrics().density);
            tabHost.getTabWidget().getChildAt(0).setPadding(0,0,0,0);

            tabHost.getTabWidget().getChildAt(1).getLayoutParams().height = (int) (40 * this.getResources().getDisplayMetrics().density);
            tabHost.getTabWidget().getChildAt(1).setPadding(0,0,0,0);
        }

        if (idDieteticProfile == 0) {
            tabHost.setCurrentTabByTag(TAB_DIETETIC_PROFILE);
            setFragment(7);
        } else {
            setFragment((1 == idDieteticProfile) ? 7 : 8);
            tabHost.setCurrentTabByTag((1 == idDieteticProfile) ? TAB_DIETETIC_PROFILE : TAB_DIETETIC_PROFILE_2);
        }

        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tab) {
                switch (tab){
                    case TAB_DIETETIC_PROFILE:
                        tabHost.setCurrentTabByTag(TAB_DIETETIC_PROFILE);
                        setFragment(7);
                        break;
                    case TAB_DIETETIC_PROFILE_2:
                        tabHost.setCurrentTabByTag(TAB_DIETETIC_PROFILE_2);
                        setFragment(8);
                        break;
                }
            }
        });
    }

    public void setFragment(int position) {
        Bundle bundle = getActivity().getIntent().getExtras();
        Log.d("MainActivity", "Valor de position: " + position);

        if (null == bundle) {
            bundle = new Bundle();
        }

        //Necessary to save the state
        currentFragment = position;

        switch (position) {
            case 7:
                if (null == dieteticProfileFragment) dieteticProfileFragment = new DieteticProfileFragment();
                bundle.putInt(ID_USER, prefs.getInt(ID_USER, 0));
                bundle.putLong(ID_EXTENSIONS_PROFILE, prefs.getLong(ID_EXTENSIONS_PROFILE, 0));
                bundle.putLong(ID_EXTENSIONS_BALANCER_PLUS, prefs.getLong(ID_EXTENSIONS_BALANCER_PLUS, 0));
                replaceFragment(dieteticProfileFragment, bundle);
                tabHost.setVisibility(View.VISIBLE);
                break;
            case 8:
                if (null == dieteticProfile2Fragment) dieteticProfile2Fragment = new DieteticProfile2Fragment();
                bundle.putInt(ID_USER, prefs.getInt(ID_USER, 0));
                bundle.putLong(ID_EXTENSIONS_PROFILE, prefs.getLong(ID_EXTENSIONS_PROFILE, 0));
                bundle.putLong(ID_EXTENSIONS_BALANCER_PLUS, prefs.getLong(ID_EXTENSIONS_BALANCER_PLUS, 0));
                replaceFragment(dieteticProfile2Fragment, bundle);
                tabHost.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void replaceFragment(Fragment newFragment, Bundle bundle) {
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (!newFragment.isVisible()) {

            if (bundle != null) {
                newFragment.setArguments(bundle);
                Bundle clearBundle = null;
                getActivity().getIntent().replaceExtras(clearBundle);
            }
            fragmentTransaction.replace(R.id.all_fragments_dietetics, newFragment);
            fragmentTransaction.commit();
        }
    }

 }
