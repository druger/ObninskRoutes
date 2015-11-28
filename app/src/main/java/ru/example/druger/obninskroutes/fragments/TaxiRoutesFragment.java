package ru.example.druger.obninskroutes.fragments;


import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import ru.example.druger.obninskroutes.ClickListener;
import ru.example.druger.obninskroutes.ListStops;
import ru.example.druger.obninskroutes.R;
import ru.example.druger.obninskroutes.Route;
import ru.example.druger.obninskroutes.SharedPreference;
import ru.example.druger.obninskroutes.adapter.ListRoutesAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class TaxiRoutesFragment extends Fragment {

    public final String TITLE_ROUTE = "title_of_Route";
    public final String ID_ROUTE = "id_route";

    SharedPreference sharedPreference;

    Activity activity;
    private ArrayList<Route> routesTaxi = new ArrayList<>();

    private ListRoutesAdapter routesAdapter;

    public TaxiRoutesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        sharedPreference = new SharedPreference();
        setRoutes();
        routesAdapter = new ListRoutesAdapter(activity, routesTaxi, Route.iconTaxiRoutes);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_taxi_routes, container, false);
        final RecyclerView listTaxiRoutes = (RecyclerView) rootView.findViewById(R.id.rvTaxiRoutes);
        listTaxiRoutes.setLayoutManager(new LinearLayoutManager(activity));
        listTaxiRoutes.setHasFixedSize(true);
        listTaxiRoutes.setAdapter(routesAdapter);

        routesAdapter.setOnItemClickListener(new ClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), ListStops.class);

                Route route = routesTaxi.get(position);
                intent.putExtra(TITLE_ROUTE, route.getTitle());
                intent.putExtra(ID_ROUTE, position);
                startActivity(intent);
            }
        });
        return rootView;
    }

    private void setRoutes(){
        routesTaxi.add(new Route(1, "Автостанция - ВНИИСХР"));
        routesTaxi.add(new Route(2, "АБЗ - АБЗ"));
        routesTaxi.add(new Route(3, "АБЗ - АБЗ"));
        routesTaxi.add(new Route(4, "Вокзал - Вокзал"));
        routesTaxi.add(new Route(5, "Вокзал - п.Мирный"));
        routesTaxi.add(new Route(6, "Музыкальная школа - ИАТЭ"));
        routesTaxi.add(new Route(7, "Вокзал - Вокзал"));
        routesTaxi.add(new Route(8, "Автостанция - ул.Гагарина"));
        routesTaxi.add(new Route(9, "Вокзал - Вокзал"));
        routesTaxi.add(new Route(10, "Вокзал - Вокзал"));
        routesTaxi.add(new Route(11, "Вокзал - Автостанция"));
        routesTaxi.add(new Route(12, "Вокзал - Вокзал"));
        routesTaxi.add(new Route(13, "Вокзал - Вокзал"));
        routesTaxi.add(new Route(14, "Вокзал - Вокзал"));
        routesTaxi.add(new Route(15, "Вокзал - Вокзал"));
        routesTaxi.add(new Route(16, "Кончаловские горы - Кончаловские горы"));
        routesTaxi.add(new Route(17, "Кончаловские горы - Кончаловские горы"));
    }
}
