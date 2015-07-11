package ru.example.druger.obninskroutes.search;

import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import ru.example.druger.obninskroutes.R;
import ru.example.druger.obninskroutes.db.DBHelper;

public class SearchableActivity extends ActionBarActivity {
    //static final String LOG_TAG = SearchableActivity.class.getSimpleName();

    private DBHelper dbHelper;

    private ListView searchList;
    private TextView emptyResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);

        dbHelper = new DBHelper(this);
        searchList = (ListView) findViewById(android.R.id.list);
        emptyResult = (TextView) findViewById(android.R.id.empty);

        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())){
            //Берем строку запроса из экстры
            String query = intent.getStringExtra(SearchManager.QUERY);
            //Выполняем поиск
            showResult(query);
        } else if (Intent.ACTION_VIEW.equals(intent.getAction())){
            Intent recordIntent = new Intent(this, RecordActivity.class);
            recordIntent.setData(intent.getData());
            startActivity(recordIntent);
            finish();
        }
    }

    /**
     * Показывает результаты поиска
     * @param query строка запроса
     */
    private void showResult(String query) {
        //Ищем совпадения
//        Cursor cursor = dbHelper.fetchStopsByQuery(query);
//        startManagingCursor(cursor);
        //Запрашиваем у контент-провайдера курсор на записи
        Cursor cursor = managedQuery(SuggestionProvider.CONTENT_URI, null, null,
                new String[]{query}, null);

        String[] from = new String[] {SearchManager.SUGGEST_COLUMN_TEXT_1};
        int[] to = new int[] {android.R.id.text1};

        ListAdapter adapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_1,
                cursor, from, to);
        //Обновляем адаптер
        searchList.setAdapter(adapter);
        searchList.setEmptyView(emptyResult);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_searchable, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close();
    }
}
