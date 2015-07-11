package ru.example.druger.obninskroutes.timetable;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

import ru.example.druger.obninskroutes.R;
import ru.example.druger.obninskroutes.db.DBHelper;

/**
 * страница расписания остановки
 */
public class TimetablePage extends Fragment {
    //private static final String LOG_TAG = TimetablePage.class.getSimpleName();

    static final String ARGUMENT_PAGE_NUMBER = "arg_page_number";
    //public final String POSITION_ROUTE = "position_route";
    public final String ID_ROUTE = "id_route";
    public final String POSITION_STOP = "position_stop";
    public static final int WORKDAY = 0;
    public static final int HOLIDAY = 1;

    private DBHelper dbHelper;

    AdapterTimetable adapterTimetable;

    int pageNumber;
    int backColor;

    private Map<Integer, String> timetableStop = new LinkedHashMap<>(); //рассписание остановок

    public TimetablePage() {
        // Required empty public constructor
    }

    static TimetablePage newInstance(int page){
        TimetablePage timetablePage = new TimetablePage();
        Bundle arguments = new Bundle();
        arguments.putInt(ARGUMENT_PAGE_NUMBER, page);
        timetablePage.setArguments(arguments);
        return timetablePage;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageNumber = getArguments().getInt(ARGUMENT_PAGE_NUMBER);

        Random rnd = new Random();
        backColor = Color.argb(40, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));

        dbHelper = new DBHelper(getActivity());

        Intent intent = getActivity().getIntent();

        int idRoute = intent.getIntExtra(ID_ROUTE, 0);
        int positionStop = intent.getIntExtra(POSITION_STOP, 0);
        timetableStop = dbHelper.getTimeTableStop(idRoute, pageNumber, positionStop);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.timetable_page, container, false);

        ListView lvTimeTable = (ListView) view.findViewById(R.id.lvTimetable);
        switch (pageNumber){
            case WORKDAY:
                adapterTimetable = new AdapterTimetable(getActivity(), R.layout.item_timetable, timetableStop);
                lvTimeTable.setAdapter(adapterTimetable);
                break;
            case HOLIDAY:
                adapterTimetable = new AdapterTimetable(getActivity(), R.layout.item_timetable, timetableStop);
                lvTimeTable.setAdapter(adapterTimetable);
                break;
        }
        lvTimeTable.setBackgroundColor(backColor);

        return view;
    }

    private static class AdapterTimetable extends ArrayAdapter{
        private final ArrayList data;

        public AdapterTimetable(Context context, int resource, Map<Integer, String> map) {
            super(context, resource);
            data = new ArrayList();
            data.addAll(map.entrySet());
        }

        static class ViewHolder{
            TextView hours;
            TextView minutes;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Map.Entry<Integer, String> getItem(int position) {
            return (Map.Entry<Integer, String>) data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final View result;
            ViewHolder holder; // ViewHolder буферизирует оценку различных полей шаблона элемента

            if (convertView == null){
                result = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_timetable, parent, false);
                holder = new ViewHolder();
                holder.hours = (TextView) result.findViewById(R.id.hours);
                holder.minutes = (TextView) result.findViewById(R.id.minutes);
                result.setTag(holder);

            } else {
                result = convertView;
                holder = (ViewHolder) result.getTag();
            }

            Map.Entry<Integer, String> item = getItem(position);

            holder.hours.setText(Integer.toString(item.getKey()));
            holder.hours.setTextColor(Color.BLUE);
            holder.minutes.setText(item.getValue());
            holder.minutes.setTextColor(Color.BLACK);

            return result;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        dbHelper.close();
    }
}
