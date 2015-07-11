package ru.example.druger.obninskroutes.search;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

import ru.example.druger.obninskroutes.db.DBHelper;

/**
 * Created by druger on 11.06.2015.
 */
public class SuggestionProvider extends ContentProvider {
    private DBHelper dbHelper;

    public static String AUTHORITY = "ru.example.druger.obninskroutes.search.SuggestionProvider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/routes");

    //MIME типы для getType()
    public static final String RECORDS_MIME_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE +
            "/vnd.ru.example.druger.obninskroutes.search";
    public static final String RECORD_MIME_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE +
            "/vnd.ru.example.druger.obninskroutes.search";

    //Для матчера разных URI
    private static final int SEARCH_RECORDS = 0;
    private static final int GET_RECORD = 1;
    private static final int SEARCH_SUGGEST = 2;
    private static final UriMatcher URI_MATCHER = makeUriMatcher();

    @Override
    public boolean onCreate() {
        dbHelper = new DBHelper(getContext());
        return true;
    }

    /**
     * Обрабатывает запросы от Search Manager'a.
     * Когда запрашивается конкретный элемент, то требуется только URI.
     * Когда запрашивается поиск по всей таблице, то первый элемент параметра selectionArgs содержит строку запроса.
     * Остальные параметры не нужны.
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        /**
         * Используем UriMatcher, чтобы узнать какой тип запроса получен.
         * Далее формируем соответствующий запрос к БД
         */
        switch (URI_MATCHER.match(uri)){
            case SEARCH_SUGGEST:
                if (selectionArgs == null){
                    throw new IllegalArgumentException(
                            "selectionArgs must be provided for the Uri: " + uri
                    );
                }
                return getSuggestions(selectionArgs[0]);
            case SEARCH_RECORDS:
                if (selectionArgs == null){
                    throw new IllegalArgumentException(
                       "selectionArgs must be provided for the Uri: " + uri
                    );
                }
                return search(selectionArgs[0]);
            case GET_RECORD:
                return getRecord(uri);
            default:
                throw new IllegalArgumentException("Umknown Uri: " + uri);
        }
    }

    private Cursor getSuggestions(String query) {
        String[] columns = new String[]{
                BaseColumns._ID,
                SearchManager.SUGGEST_COLUMN_TEXT_1,
                SearchManager.SUGGEST_COLUMN_ICON_1,
                SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID
        };
        return dbHelper.getRecordsMatches(query, columns);
    }

    private Cursor search(String query) {
        String[] columns = new String[]{
                BaseColumns._ID,
                SearchManager.SUGGEST_COLUMN_TEXT_1,
                SearchManager.SUGGEST_COLUMN_ICON_1
        };
        return dbHelper.getRecordsMatches(query, columns);
    }

    private Cursor getRecord(Uri uri) {
        String rowId = uri.getLastPathSegment();
        String[] columns = new String[]{
                SearchManager.SUGGEST_COLUMN_TEXT_1,
                SearchManager.SUGGEST_COLUMN_ICON_1
        };
        return dbHelper.getRecord(rowId, columns);
    }

    //Требуемые методы (наследуются от класса ContentProvider)
    @Override
    public String getType(Uri uri) {
        switch (URI_MATCHER.match(uri)){
            case SEARCH_RECORDS:
                return RECORDS_MIME_TYPE;
            case SEARCH_SUGGEST:
                return SearchManager.SUGGEST_MIME_TYPE;
            case GET_RECORD:
                return RECORD_MIME_TYPE;
            default:
                throw new IllegalArgumentException("Unknown Uri " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException();
    }

    /**
     * Вспомогательный метод
     * нужен для сопоставления разным URI конкретных значений
     */
    private static UriMatcher makeUriMatcher() {
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        // Для записей
        matcher.addURI(AUTHORITY, "routes", SEARCH_RECORDS);
        matcher.addURI(AUTHORITY, "routes/#", GET_RECORD);
        // Для подсказок
        matcher.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_QUERY, SEARCH_SUGGEST);
        matcher.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_QUERY + "/*", SEARCH_SUGGEST);
        return matcher;
    }
}
