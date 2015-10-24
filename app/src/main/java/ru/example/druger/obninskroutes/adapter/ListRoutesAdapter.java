package ru.example.druger.obninskroutes.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.example.druger.obninskroutes.R;
import ru.example.druger.obninskroutes.Route;
import ru.example.druger.obninskroutes.SharedPreference;

public class ListRoutesAdapter extends ArrayAdapter<Route> {

    private final Activity context;
    final ArrayList<Route> routes;
    final Integer[] imgId;
    SharedPreference sharedPreference;

    public ListRoutesAdapter(Activity context, ArrayList<Route> routes, Integer[] imgId) {
        super(context, R.layout.list_routes, routes);

        this.context = context;
        this.routes = routes;
        this.imgId = imgId;

        sharedPreference = new SharedPreference();
    }



    private class ViewHolder {
        ImageView iconRoute; //иконка маршрута
        ImageView btnFavourite; // кнопка добавления в Избранное
        TextView titleRoute; //название маршрута

    }

    @Override
    public Route getItem(int position) {
        return routes.get(position);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View result = convertView;

        final ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            holder = new ViewHolder();
            result = inflater.inflate(R.layout.list_routes, parent, false);
            holder.iconRoute = (ImageView) result.findViewById(R.id.ic_favorite_route);
            holder.titleRoute = (TextView) result.findViewById(R.id.favorite_title_route);
            holder.btnFavourite = (ImageView) result.findViewById(R.id.btnFavourite_in_favorites);
            result.setTag(holder);

        } else {
            holder = (ViewHolder) result.getTag();
        }

        final Route route = getItem(position);
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
        return result;
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

    @Override
    public void add(Route route) {
        super.add(route);
        routes.add(route);
        notifyDataSetChanged();
    }

    @Override
    public void remove(Route route) {
        super.remove(route);
        routes.remove(route);
        notifyDataSetChanged();
    }
}
