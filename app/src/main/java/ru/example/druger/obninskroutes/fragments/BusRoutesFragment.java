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
public class BusRoutesFragment extends Fragment {

    public final String TITLE_ROUTE = "title_of_Route";
    public final String ID_ROUTE = "id_route";

    SharedPreference sharedPreference;

    Activity activity;
    private ArrayList<Route> routesBus = new ArrayList<>();

    private ListRoutesAdapter routesAdapter;

    public BusRoutesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        sharedPreference = new SharedPreference();
        setRoutes();
        routesAdapter = new ListRoutesAdapter(activity, routesBus, Route.iconBusRoutes);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_bus_routes, container, false);
        final RecyclerView listBusRoutes = (RecyclerView) rootView.findViewById(R.id.rvBusRoutes);
        listBusRoutes.setLayoutManager(new LinearLayoutManager(activity));
        listBusRoutes.setHasFixedSize(true);
        listBusRoutes.setAdapter(routesAdapter);

        routesAdapter.setOnItemClickListener(new ClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), ListStops.class);

                Route route = routesBus.get(position);
                intent.putExtra(TITLE_ROUTE, route.getTitle());
                intent.putExtra(ID_ROUTE, position);
                startActivity(intent);
            }
        });

        return rootView;
    }

    private void setRoutes() {
        routesBus.add(new Route(1, "Автостанция - 100 здание завода \"Сигнал\""));
        routesBus.add(new Route(2, "АБЗ - АБЗ"));
        routesBus.add(new Route(3, "АБЗ - АБЗ"));
        routesBus.add(new Route(4, "Вокзал - Вокзал"));
        routesBus.add(new Route(5, "Вокзал - п.Мирный"));
        routesBus.add(new Route(6, "Музыкальная школа - ИАТЭ"));
        routesBus.add(new Route(7, "Вокзал - Вокзал"));
        routesBus.add(new Route(8, "Автостанция - ул.Гагарина"));
        routesBus.add(new Route(9, "Вокзал - Вокзал"));
        routesBus.add(new Route(10, "Вокзал - Автостанция"));
        routesBus.add(new Route(11, "Вокзал - Вокзал"));
    }
}
