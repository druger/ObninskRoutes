package ru.example.druger.obninskroutes;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

import ru.example.druger.obninskroutes.db.DBHelper;
import ru.example.druger.obninskroutes.search.SearchableActivity;
import ru.example.druger.obninskroutes.timetable.Timetable;


public class ListStops extends AppCompatActivity implements SearchView.OnQueryTextListener {
    //final String LOG_TAG = ListStops.class.getSimpleName();

    public final String TITLE_ROUTE = "title_of_Route";
    public final String NAME_STOP = "name_of_stop";
    public final String ID_ROUTE = "id_route";
    public final String POSITION_STOP = "position_stop";

    ListStopsAdapter listStopsAdapter;
    Intent intent;

    ArrayList<String> titleStops = new ArrayList<>();

    Integer[] iconStops = {
            R.drawable.ic_stop_first,
            R.drawable.ic_stop_middle,
            R.drawable.ic_stop_last
    };
    ListView listStops;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getIntent().getStringExtra(TITLE_ROUTE));
        setContentView(R.layout.activity_list_of_stops);

        new RouteTask().execute();

        if (titleStops != null) {
            listStopsAdapter = new ListStopsAdapter(this, titleStops, iconStops);
            listStops = (ListView) findViewById(R.id.listStops);
            listStops.setAdapter(listStopsAdapter);
        }

        listStops.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intent = new Intent(ListStops.this, Timetable.class);
                intent.putExtra(NAME_STOP, titleStops.get(position));
                intent.putExtra(ID_ROUTE, getIntent().getIntExtra(ID_ROUTE, 0));
                intent.putExtra(POSITION_STOP, position);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list_of_stops, menu);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        ComponentName componentName = new ComponentName(this, SearchableActivity.class);

        searchView.setQueryHint("Поиск маршрута");
        searchView.setOnQueryTextListener(this);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName));
        searchView.setIconifiedByDefault(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()){
            case R.id.action_search:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void returnRoute(View view) {
        Collections.reverse(titleStops);
        listStopsAdapter.notifyDataSetChanged();
    }

    public void showOnMap(View view) {
        intent = new Intent(ListStops.this, MapsActivity.class);
        intent.putExtra(ID_ROUTE, getIntent().getIntExtra(ID_ROUTE, 0));
        startActivity(intent);
    }

    /**
     * @param s - текст, который пользователь ищет
     */
    @Override
    public boolean onQueryTextSubmit(String s) {
        Log.d("QUERY", "Search text is " + s);
        return true;
    }

    /**
     * @param s - текст из поля ввода, когда
     * пользователь набирает очередную букву
     */
    @Override
    public boolean onQueryTextChange(String s) {
        Log.d("QUERY", "New text is " + s);
        return true;
    }

    /**
     * получаем список остановок из БД
     */
     private class RouteTask extends AsyncTask<Void, Void, ArrayList<String>>{

        @Override
        protected ArrayList<String> doInBackground(Void... params) {
            ArrayList<String> routesFromDB;

            DBHelper dbHelper = new DBHelper(getApplicationContext());
            routesFromDB = dbHelper.getBusStops(getIntent().getIntExtra(ID_ROUTE, 0));

            dbHelper.close();
            return routesFromDB;
        }

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        @Override
        protected void onPostExecute(ArrayList<String> result) {
            if (result != null){
                listStopsAdapter.clear();
                listStopsAdapter.addAll(result);
            }
        }
    }

    public class ListStopsAdapter extends ArrayAdapter<String> {

        private final Activity context;
        private final ArrayList<String> itemName;
        private final Integer[] imgId;
        SharedPreference sharedPreference;

        public ListStopsAdapter(Activity context, ArrayList<String> itemName, Integer[] imgId){
            super(context, R.layout.item_list_routes, itemName);

            this.context = context;
            this.itemName = itemName;
            this.imgId = imgId;

            sharedPreference = new SharedPreference();
        }

        class ViewHolder{
            ImageView iconStop; //иконка остановки
            TextView titleStop; //название остановки
        }

        @Override
        public String getItem(int position) {
            return itemName.get(position);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = context.getLayoutInflater();
            View result = convertView;

            final ViewHolder holder;

            if (convertView == null) {
                holder = new ViewHolder();
                result = inflater.inflate(R.layout.list_stops, parent, false);
                holder.iconStop = (ImageView) result.findViewById(R.id.iconStop);
                holder.titleStop = (TextView) result.findViewById(R.id.titleStop);
                result.setTag(holder);
            } else {
                holder = (ViewHolder) result.getTag();
            }

            if (position == 0) holder.iconStop.setImageResource(imgId[0]);
            else if (position == itemName.size() - 1) holder.iconStop.setImageResource(imgId[2]);
            else holder.iconStop.setImageResource(imgId[1]);
            holder.titleStop.setText(itemName.get(position));

            return result;
        }

    }
}
