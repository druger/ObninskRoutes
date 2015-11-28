package ru.example.druger.obninskroutes.fragments;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ru.example.druger.obninskroutes.ClickListener;
import ru.example.druger.obninskroutes.ListStops;
import ru.example.druger.obninskroutes.R;
import ru.example.druger.obninskroutes.Route;
import ru.example.druger.obninskroutes.SharedPreference;

public class FavoritesFragment extends Fragment {
    public final String TITLE_ROUTE = "title_of_Route";
    public final String ID_ROUTE = "id_route";

    private ArrayList<Route> favoriteRoutes;
    private SharedPreference sharedPreference;

    private FavoriteListRoutesAdapter routesAdapter;

    public FavoritesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreference = new SharedPreference();
        favoriteRoutes = sharedPreference.getFavorites(getActivity());

        routesAdapter = new FavoriteListRoutesAdapter(getActivity(), favoriteRoutes, Route.iconBusRoutes);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_favorites, container, false);
        RecyclerView favoriteListRoutes = (RecyclerView) rootView.findViewById(R.id.favorite_list_routes);
        favoriteListRoutes.setLayoutManager(new LinearLayoutManager(getActivity()));
        favoriteListRoutes.setHasFixedSize(true);
        favoriteListRoutes.setAdapter(routesAdapter);

        routesAdapter.notifyDataSetChanged();

        routesAdapter.setOnItemClickListener(new ClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), ListStops.class);

                Route route = favoriteRoutes.get(position);
                intent.putExtra(TITLE_ROUTE, route.getTitle());
                intent.putExtra(ID_ROUTE, route.getId() - 1);
                startActivity(intent);
            }
        });
        return rootView;
    }

    public class FavoriteListRoutesAdapter extends RecyclerView.Adapter<FavoriteListRoutesAdapter.ViewHolder> {
        private final Context context;
        private final ArrayList<Route> routes;
        private final Integer[] imgId;

        private ClickListener clickListener;

        public FavoriteListRoutesAdapter(Context context, ArrayList<Route> routes, Integer[] imgId) {
            this.context = context;
            this.routes = routes;
            this.imgId = imgId;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View itemView = inflater.inflate(R.layout.item_list_routes, parent, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            final Route route = routes.get(position);
            holder.icFavoriteRoute.setImageResource(imgId[route.getId() - 1]);
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

        }

        @Override
        public int getItemCount() {
            return routes.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            private ImageView icFavoriteRoute;
            private TextView favoriteTitleRoute;
            private ImageView btnFavorite;

            public ViewHolder(View itemView) {
                super(itemView);
                icFavoriteRoute = (ImageView) itemView.findViewById(R.id.ic_route);
                favoriteTitleRoute = (TextView) itemView.findViewById(R.id.title_route);
                btnFavorite = (ImageView) itemView.findViewById(R.id.btnFavourite);

                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                clickListener.onItemClick(getAdapterPosition(), v);
            }
        }

        public void setOnItemClickListener(ClickListener clickListener) {
            this.clickListener = clickListener;
        }
    }
}
