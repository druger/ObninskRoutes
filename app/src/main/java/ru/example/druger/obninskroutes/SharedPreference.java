package ru.example.druger.obninskroutes;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by druger on 27.05.2015.
 */
public class SharedPreference {
    public static final String PREFS_NAME = "ROUTES_APP";
    public static final String FAVORITES = "Favorite";

    public SharedPreference() {
        super();
    }

    public void saveFavorites(Context context, List<Route> favorites){
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        editor = settings.edit();

        Gson gson = new Gson();
        String jsonFavorites = gson.toJson(favorites);

        editor.putString(FAVORITES, jsonFavorites);
        editor.apply();
    }

    public ArrayList<Route> getFavorites(Context context){
        SharedPreferences settings;
        List<Route> favorites;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        if (settings.contains(FAVORITES)){
            String jsonFavorites = settings.getString(FAVORITES, null);
            Gson gson = new Gson();
            Route[] favoriteItem = gson.fromJson(jsonFavorites, Route[].class);
            favorites = Arrays.asList(favoriteItem);
            favorites = new ArrayList<>(favorites);
        } else {
            return null;
        }

        return (ArrayList<Route>) favorites;
    }

    public void addFavorite(Context context, Route route){
        List<Route> favorites = getFavorites(context);
        if (favorites == null){
            favorites = new ArrayList<>();
        }
        favorites.add(route);
        saveFavorites(context, favorites);
    }

    public void removeFavorite(Context context, Route route){
        ArrayList<Route> favorites = getFavorites(context);
        if (favorites != null){
            favorites.remove(route);
            saveFavorites(context, favorites);
        }
    }
}
