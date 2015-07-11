package ru.example.druger.obninskroutes.db;

import android.app.SearchManager;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.provider.BaseColumns;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import ru.example.druger.obninskroutes.timetable.TimetablePage;

/**
 * Created by druger on 14.05.2015.
 */
public class DBHelper extends SQLiteAssetHelper {
    final String LOG_TAG = DBHelper.class.getSimpleName();

    static String DB_NAME = "db_routes.db";
    static final int DB_VERSION = 10;

    static final String TB_ROUTES = "routes";
    static final String COL_ID_ROUTE = "id_route";
    public static final String COL_STOPS = "bus_stops";
    static final String COL_IC_ROUTE = "ic_route";

    static final String TB_INIT_TIME = "init_time";
    static final String TB_INIT_TIME_HOLIDAY = "init_time_holiday";
    static final String TB_FINAL_TIME = "final_time";
    static final String TB_FINAL_TIME_HOLIDAY = "final_time_holiday";
    static final String[] COL_TIMES = {"hours", "minutes"};

    static final String TB_LAT_LNG_STOPS = "lat_lng_stops";
    static final String TB_LAT_LNG_WAYPOINTS = "lat_lng_waypoints";
    static final String[] COL_LAT_LNG = {"latitude", "longitude"};

    private static final HashMap<String, String> mColumnMap = buildColumnMap();

    SQLiteDatabase myDB;
    Cursor cursor;

    public DBHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
        setForcedUpgrade();
    }

    /**
     * Создает отображение всевозможных запрашиваемых столбцов.
     * Будет установлено как проекция в SQLiteQueryBuilder.
     * Нужно для того, чтобы назначить для каждой записи уникальные значения SUGGEST_COLUMN_INTENT_DATA_ID
     * которые используются для получения конкретной записи по URI.
     */
    private static HashMap<String, String> buildColumnMap() {
        // This HashMap is used to map table fields to Custom Suggestion fields
        HashMap<String, String> map = new HashMap<>();
        // Unique id for the each Suggestions ( Mandatory )
        map.put(BaseColumns._ID, "rowid AS " + BaseColumns._ID);
        // Text for Suggestions ( Mandatory )
        map.put(SearchManager.SUGGEST_COLUMN_TEXT_1, COL_STOPS + " AS "
                + SearchManager.SUGGEST_COLUMN_TEXT_1);
        // Icon for Suggestions ( Optional )
        map.put(SearchManager.SUGGEST_COLUMN_ICON_1, COL_IC_ROUTE + " AS "
                + SearchManager.SUGGEST_COLUMN_ICON_1);
        // This value will be appended to the Intent data on selecting an item
        // from Search result or Suggestions ( Optional )
        map.put(SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID, " rowid AS "
                + SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID);
        return map;
    }

    /**
     * Получает список остановок
     * @param id маршрута
     * @return список остановок выбранного маршрута
     */
    public ArrayList<String> getBusStops(int id){
        ArrayList<String> listStops = new ArrayList<>();

        myDB = getReadableDatabase();
        try {
            cursor = myDB.query(TB_ROUTES, null, COL_ID_ROUTE + " = " + (id + 1),
                    null, null, null, null);
            if (cursor == null) return null;

            String route;
            cursor.moveToFirst();
            do {
                route = cursor.getString(1);
                listStops.add(route);
            } while (cursor.moveToNext());
            cursor.close();
        } catch (Exception e){
            Log.e(LOG_TAG, e.getMessage());

        }
        return listStops;
    }

    /**
     * Получает расписание остановки
     * @param idRoute - id маршрута
     * @param idDay - id дня(рабочего или выходного)
     * @param positionStop - позиция остановки
     * @return расписание выбранной остановки
     */
    public LinkedHashMap<Integer, String> getTimeTableStop(int idRoute, int idDay, int positionStop){
        LinkedHashMap<Integer, String> timeTableStop = new LinkedHashMap<>();

        myDB = getReadableDatabase();
        try {
            if (positionStop == 0){
                if (idDay == TimetablePage.WORKDAY){
                    cursor = myDB.query(TB_INIT_TIME, COL_TIMES,
                            COL_ID_ROUTE + " = " + (idRoute + 1), null, null, null, null);
                } else if (idDay == TimetablePage.HOLIDAY){
                    cursor = myDB.query(TB_INIT_TIME_HOLIDAY, COL_TIMES,
                            COL_ID_ROUTE + " = " + (idRoute + 1), null, null, null, null);
                }
            } else {
                if (idDay == TimetablePage.WORKDAY){
                    cursor = myDB.query(TB_FINAL_TIME, COL_TIMES,
                            COL_ID_ROUTE + " = " + (idRoute + 1), null, null, null, null);
                } else if (idDay == TimetablePage.HOLIDAY){
                    cursor = myDB.query(TB_FINAL_TIME_HOLIDAY, COL_TIMES,
                            COL_ID_ROUTE + " = " + (idRoute + 1), null, null, null, null);
                }
            }
            //if (cursor == null) return null;

            int hours;
            String minutes;
            cursor.moveToFirst();
            do {
                hours = cursor.getInt(0);
                minutes = cursor.getString(1);
                timeTableStop.put(hours, minutes);
            } while (cursor.moveToNext());
            cursor.close();

        } catch (Exception e){
            Log.e(LOG_TAG, e.getMessage());
        }

        return timeTableStop;
    }

    /**
     * Получает координаты остановок
     * @param id маршрута
     * @return координаты остановок
     */
    public ArrayList<LatLng> getLatLngStops(int id){
        ArrayList<LatLng> latLngStops = new ArrayList<>();

        myDB = getReadableDatabase();
        try{
            cursor = myDB.query(TB_LAT_LNG_STOPS, COL_LAT_LNG,
                    COL_ID_ROUTE + " = " + (id + 1), null, null, null, null);
            if (cursor != null){
                double latitude;
                double longitude;

                cursor.moveToFirst();
                do {
                    latitude = cursor.getDouble(0);
                    longitude = cursor.getDouble(1);
                    latLngStops.add(new LatLng(latitude, longitude));
                } while (cursor.moveToNext());
                cursor.close();
            }
        }catch (Exception e){
            Log.e(LOG_TAG, e.getMessage());
        }
        return latLngStops;
    }

    /**
     * Получает координаты путевых точек
     * @param id маршрута
     * @return координаты путевых точек
     */
    public ArrayList<LatLng> getLatLngWaypoints(int id){
        ArrayList<LatLng> latLngWaypoints = new ArrayList<>();

        myDB = getReadableDatabase();
        try {
            cursor = myDB.query(TB_LAT_LNG_WAYPOINTS, COL_LAT_LNG,
                    COL_ID_ROUTE + " = " + (id + 1), null, null, null, null);
            if (cursor != null){
                double latitude;
                double longitude;

                cursor.moveToFirst();
                do {
                    latitude = cursor.getDouble(0);
                    longitude = cursor.getDouble(1);
                    latLngWaypoints.add(new LatLng(latitude, longitude));

                } while (cursor.moveToNext());
                cursor.close();
            }
        }catch (Exception e){
            Log.e(LOG_TAG, e.getMessage());
        }
        return latLngWaypoints;
    }

    /**
     *
     * @param rowId id возвращаемой записи
     * @param columns возвращаемые столбцы записи; если null, то все
     * @return курсор, указывающий на определенную запись с rowId,
     * null - если запись не найдена
     */
    public Cursor getRecord(String rowId, String[] columns){
        String selection = "rowid = ?";
        String[] selectionArgs = new String[] {rowId};

        return query(selection, selectionArgs, columns);
    }

    /**
     *
     * @param query текст поискового запроса
     * @param columns возвращаемые столбцы записи; если null, то все
     * @return курсор, указывающий на записи, совпадающие с запросом,
     * null - если запись не найдена
     */
    public Cursor getRecordsMatches(String query, String[] columns){
        String selection = COL_STOPS + " LIKE ?";
        String[] selectionArgs = new String[] {"%" + query + "%" };

        return query(selection, selectionArgs, columns);
    }

    /**
     *
     * @param selection оператор выборки
     * @param selectionArgs аргументы, заменяющие "?" в запросе к БД
     * @param columns возвращаемые столбцы записи
     * @return курсор, указывающий на все записи, совпадающие с поисковым запросом
     */
    private Cursor query(String selection, String[] selectionArgs, String[] columns){
        /* SQLiteBuilder предоставляет возможность создания отображения для всех
         * необходимых столбцов БД, что позволяет не сообщать контент-провайдеру
         * настоящие имена столбцов.
         */

        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(TB_ROUTES);
        builder.setProjectionMap(mColumnMap);

        Cursor cursor = builder.query(this.getReadableDatabase(),
                columns, selection, selectionArgs, null, null, null);
        if (cursor == null){
            return null;
        } else if (!cursor.moveToFirst()){
            cursor.close();
            return null;
        }
        return cursor;
    }
}
