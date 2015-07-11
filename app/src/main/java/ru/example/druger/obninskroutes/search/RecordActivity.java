package ru.example.druger.obninskroutes.search;

import android.app.SearchManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import ru.example.druger.obninskroutes.R;

/**
 * Класс для отображения остановки, выбранной из подсказки поиска
 */
public class RecordActivity extends ActionBarActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        //Получаем URI с данными из Intent и запрашиваем данные через контент-провайдер
        Uri uri = getIntent().getData();
        Cursor cursor = managedQuery(uri, null, null, null, null);

        if (cursor == null){
            finish();
        } else {
            //Устанавливаем данные в текстовое поле
            cursor.moveToFirst();

            ImageView icSearchableRoute = (ImageView) findViewById(R.id.ic_searchable_route);
            TextView searchableStop = (TextView) findViewById(R.id.searchable_stop);

            int indexText = cursor.getColumnIndexOrThrow(SearchManager.SUGGEST_COLUMN_TEXT_1);
            int indexIcon = cursor.getInt(cursor.getColumnIndexOrThrow(cursor.getColumnName(1)));

            icSearchableRoute.setImageResource(indexIcon);
            searchableStop.setText(cursor.getString(indexText));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_record, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }
}
