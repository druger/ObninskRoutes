package ru.example.druger.obninskroutes.menu_navigation_drawer;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import ru.example.druger.obninskroutes.R;
import ru.example.druger.obninskroutes.Route;
import ru.example.druger.obninskroutes.SharedPreference;

/**
 * Экран Избранное
 */
public class Favorites extends Fragment {
    private ListView favoriteListRoutes;
    private ArrayList<Route> favoriteRoutes;
    SharedPreference sharedPreference;

    static FavoriteListRoutesAdapter routesAdapter;

    final String LOG_TAG = Favorites.class.getSimpleName();

    public Favorites() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreference = new SharedPreference();
        try {
            favoriteRoutes = sharedPreference.getFavorites(getActivity());
        } catch (NullPointerException e){
            e.printStackTrace();
        }
//        routesAdapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.favorites, container, false);
        favoriteListRoutes = (ListView) rootView.findViewById(R.id.favorite_list_routes);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(LOG_TAG, "onResume Called");
        if (favoriteRoutes != null){
            try {
                routesAdapter = new FavoriteListRoutesAdapter(getActivity(),
                        favoriteRoutes, Route.iconBusRoutes);
                favoriteListRoutes.setAdapter(routesAdapter);
            } catch (NullPointerException e){
                e.printStackTrace();
            }
            routesAdapter.notifyDataSetChanged();
        }
    }

    public class FavoriteListRoutesAdapter extends ArrayAdapter<Route>{
        private final Context context;
        private final ArrayList<Route> routes;
        private final Integer[] imgId;

        public FavoriteListRoutesAdapter(Context context, ArrayList<Route> routes, Integer[] imgId) {
            super(context, R.layout.favorite_list_routes, routes);

            this.context = context;
            this.routes = routes;
            this.imgId = imgId;
        }

        class ViewHolder{
            ImageView icFavoriteRoute;
            TextView favoriteTitleRoute;
            ImageView btnFavorite;
        }

        @Override
        public Route getItem(int position) {
            return routes.get(position);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            if (convertView == null){
                convertView = inflater.inflate(R.layout.favorite_list_routes, parent, false);
                holder = new ViewHolder();
                holder.icFavoriteRoute = (ImageView) convertView.findViewById(R.id.ic_favorite_route);
                holder.favoriteTitleRoute = (TextView) convertView.findViewById(R.id.favorite_title_route);
                holder.btnFavorite = (ImageView) convertView.findViewById(R.id.btnFavourite_in_favorites);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final Route route = getItem(position);
            holder.icFavoriteRoute.setImageResource(imgId[route.getId() -  1]);
            holder.favoriteTitleRoute.setText(route.getTitle());
            holder.btnFavorite.setImageResource(android.R.drawable.star_big_on);

            holder.btnFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sharedPreference.removeFavorite(context, route);
                    routes.remove(route);
                    notifyDataSetChanged();
                }
            });

            return convertView;
        }
    }

}
