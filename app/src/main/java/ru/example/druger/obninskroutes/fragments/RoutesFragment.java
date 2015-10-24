package ru.example.druger.obninskroutes.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.example.druger.obninskroutes.R;
import ru.example.druger.obninskroutes.adapter.TabAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class RoutesFragment extends Fragment {

    BusRoutesFragment busRoutesFragment;
    TaxiRoutesFragment taxiRoutesFragment;

    public RoutesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_routes, container, false);

        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Автобус"));
        tabLayout.addTab(tabLayout.newTab().setText("Маршрутка"));

        final ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.pager);
        TabAdapter tabAdapter = new TabAdapter(getFragmentManager(), 2);

        viewPager.setAdapter(tabAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        busRoutesFragment = (BusRoutesFragment) tabAdapter.getItem(TabAdapter.BUS_ROUTES_FRAGMENT);
        taxiRoutesFragment = (TaxiRoutesFragment) tabAdapter.getItem(TabAdapter.TAXI_ROUTES_FRAGMENT);

        return rootView;
    }
}
