package ru.example.druger.obninskroutes.menu_navigation_drawer;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.example.druger.obninskroutes.ListStops;
import ru.example.druger.obninskroutes.R;
import ru.example.druger.obninskroutes.Route;
import ru.example.druger.obninskroutes.SharedPreference;
import ru.example.druger.obninskroutes.view.SlidingTabLayout;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListRoutes extends Fragment  {
    //private static final String LOG_TAG = ListRoutes.class.getSimpleName();
    public final String TITLE_ROUTE = "title_of_Route";
    public final String ID_ROUTE = "id_route";

    SharedPreference sharedPreference;

    String[] tabs = {"Автобус", "Маршрутка"};

    Activity activity;
    private ArrayList<Route> routesBus = new ArrayList<>();
    private ArrayList<Route> routesTaxi = new ArrayList<>();

    public ListRoutes() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        sharedPreference = new SharedPreference();
        setRoutes();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.routes, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        viewPager.setAdapter(new SamplePagerAdapter());

        // Give the SlidingTabLayout the ViewPager, this must be
        // done AFTER the ViewPager has had it's PagerAdapter set.
        SlidingTabLayout slidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.sliding_tabs);
        slidingTabLayout.setViewPager(viewPager);
    }

    private void setRoutes(){
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

    class SamplePagerAdapter extends PagerAdapter{

        /**
         * Return the number of pages to display
         */
        @Override
        public int getCount() {
            return 2;
        }

        /**
         * Return true if the value returned from is the same object as the View
         * added to the ViewPager.
         */
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return object == view;
        }

        /**
         * Return the title of the item at position. This is important as what
         * this method returns is what is displayed in the SlidingTabLayout.
         */
        @Override
        public CharSequence getPageTitle(int position) {
            return tabs[position];
        }

        /**
         * Instantiate the View which should be displayed at position. Here we
         * inflate a layout from the apps resources and then change the text
         * view to signify the position.
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // Inflate a new layout from our resources
            View view = getActivity().getLayoutInflater().inflate(R.layout.pager_item,
                    container, false);

            // Add the newly created View to the ViewPager
            container.addView(view);

            ListRoutesAdapter routesBusAdapter;
            ListRoutesAdapter routesTaxiAdapter;
            ListView listRoutes = (ListView) view.findViewById(R.id.listRoutes);

            switch (position){
                case 0:
                    routesBusAdapter = new ListRoutesAdapter(getActivity(), routesBus,
                            Route.iconBusRoutes);
                    listRoutes.setAdapter(routesBusAdapter);
                    break;
                case 1:
                    routesTaxiAdapter = new ListRoutesAdapter(getActivity(), routesTaxi,
                            Route.iconTaxiRoutes);
                    listRoutes.setAdapter(routesTaxiAdapter);
                    break;
            }

            listRoutes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getActivity().getApplicationContext(), ListStops.class);

                    Route route = (Route) parent.getItemAtPosition(position);
                    intent.putExtra(TITLE_ROUTE, route.getTitle());
                    intent.putExtra(ID_ROUTE, position);
                    startActivity(intent);
                }
            });

            return view;
        }

        /**
         * Destroy the item from the ViewPager. In our case this is simply
         * removing the View.
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    public class ListRoutesAdapter extends ArrayAdapter<Route> {

        private final Activity context;
        final ArrayList<Route> routes;
        final Integer[] imgId;
        SharedPreference sharedPreference;

        public ListRoutesAdapter(Activity context, ArrayList<Route> routes, Integer[] imgId) {
            super(context, R.layout.list_of_routes, routes);

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
                result = inflater.inflate(R.layout.list_of_routes, parent, false);
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
}
