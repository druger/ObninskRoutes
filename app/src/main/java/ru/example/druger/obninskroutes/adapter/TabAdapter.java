package ru.example.druger.obninskroutes.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;

import ru.example.druger.obninskroutes.fragments.BusRoutesFragment;
import ru.example.druger.obninskroutes.fragments.TaxiRoutesFragment;

/**
 * Created by druger on 14.10.2015.
 */
public class TabAdapter extends FragmentStatePagerAdapter {
    private int numberOfTabs;

    public static final int BUS_ROUTES_FRAGMENT = 0;
    public static final int TAXI_ROUTES_FRAGMENT = 1;

    private BusRoutesFragment routesFragment;
    private TaxiRoutesFragment taxiRoutesFragment;

    public TabAdapter(FragmentManager fm, int numberOfTabs) {
        super(fm);
        this.numberOfTabs = numberOfTabs;
        routesFragment = new BusRoutesFragment();
        taxiRoutesFragment = new TaxiRoutesFragment();
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return routesFragment;
            case 1:
                return taxiRoutesFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }
}
