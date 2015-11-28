package ru.example.druger.obninskroutes.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.example.druger.obninskroutes.ClickListener;
import ru.example.druger.obninskroutes.R;
import ru.example.druger.obninskroutes.Route;
import ru.example.druger.obninskroutes.SharedPreference;

public class ListRoutesAdapter extends RecyclerView.Adapter<ListRoutesAdapter.ViewHolder> {

    private final Activity context;
    private final ArrayList<Route> routes;
    private final Integer[] imgId;
    private SharedPreference sharedPreference;

    private ClickListener clickListener;

    public ListRoutesAdapter(Activity context, ArrayList<Route> routes, Integer[] imgId) {

        this.context = context;
        this.routes = routes;
        this.imgId = imgId;

        sharedPreference = new SharedPreference();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_list_routes, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Route route = routes.get(position);
        holder.iconRoute.setImageResource(imgId[route.getId() - 1]);
        holder.titleRoute.setText(route.getTitle());

        //If route exists in SharedPreference then set star_big_on drawable
        if (checkFavoriteItem(route)) {
            holder.btnFavourite.setImageResource(android.R.drawable.star_big_on);
            holder.btnFavourite.setTag("active");
        } else {
            holder.btnFavourite.setImageResource(android.R.drawable.star_big_off);
            holder.btnFavourite.setTag("deactive");
        }
        holder.btnFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tag = holder.btnFavourite.getTag().toString();

                if (tag.equalsIgnoreCase("deactive")) {
                    sharedPreference.addFavorite(context, routes.get(position));
                    holder.btnFavourite.setTag("active");
                    holder.btnFavourite.setImageResource(android.R.drawable.star_big_on);
                } else {
                    sharedPreference.removeFavorite(context, routes.get(position));
                    holder.btnFavourite.setTag("deactive");
                    holder.btnFavourite.setImageResource(android.R.drawable.star_big_off);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return routes.size();
    }

     class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView iconRoute;
        private ImageView btnFavourite;
        private TextView titleRoute;

        public ViewHolder(View itemView) {
            super(itemView);
            iconRoute = (ImageView) itemView.findViewById(R.id.ic_route);
            titleRoute = (TextView) itemView.findViewById(R.id.title_route);
            btnFavourite = (ImageView) itemView.findViewById(R.id.btnFavourite);

            itemView.setOnClickListener(this);
        }

         @Override
         public void onClick(View v) {
             clickListener.onItemClick(getAdapterPosition(), v);
         }
     }

    /**
     * Checks whether a particular route exists in SharedPreferences
     *
     * @param checkRoute - route for check
     * @return true if exists
     */
    public boolean checkFavoriteItem(Route checkRoute) {
        boolean check = false;
        List<Route> favorites = sharedPreference.getFavorites(context);
        if (favorites != null) {
            for (Route route : favorites) {
                if (route.equals(checkRoute)) {
                    check = true;
                }
            }
        }
        return check;
    }

    public void setOnItemClickListener(ClickListener clickListener){
        this.clickListener = clickListener;
    }
}
