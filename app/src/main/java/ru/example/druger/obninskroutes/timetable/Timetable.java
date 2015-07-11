package ru.example.druger.obninskroutes.timetable;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import ru.example.druger.obninskroutes.R;
import ru.example.druger.obninskroutes.search.SearchableActivity;

/**
 * экран рсписания остановки
 */
public class Timetable extends ActionBarActivity implements SearchView.OnQueryTextListener {
    public final String NAME_STOP = "name_of_stop";

    String[] days = {"Рабочие дни", "Выходные"};
    
    static final int PAGE_COUNT = 2;
    
    ViewPager pager;
    PagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getIntent().getStringExtra(NAME_STOP));
        setContentView(R.layout.activity_timetable);

        pager = (ViewPager) findViewById(R.id.pager);
        pagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_timetable, menu);

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

    private class MyFragmentPagerAdapter extends FragmentPagerAdapter{

        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return TimetablePage.newInstance(position);
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return days[position];
        }
    }
}
