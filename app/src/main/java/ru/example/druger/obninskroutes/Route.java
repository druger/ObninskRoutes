package ru.example.druger.obninskroutes;

/**
 * Created by druger on 31.05.2015.
 */
public class Route {
    private int id;
    private String title;

    public Route() {
        super();
    }

    public Route(int id, String title) {
        super();
        this.id = id;
        this.title = title;
    }

    public static Integer[] iconBusRoutes = {
            R.drawable.route1,
            R.drawable.route2,
            R.drawable.route3,
            R.drawable.route4,
            R.drawable.route5,
            R.drawable.route6,
            R.drawable.route7,
            R.drawable.route8,
            R.drawable.route9,
            R.drawable.route11,
            R.drawable.route18
    };

    public static Integer[] iconTaxiRoutes = {
            R.drawable.m_route1,
            R.drawable.m_route2,
            R.drawable.m_route3,
            R.drawable.m_route4,
            R.drawable.m_route5,
            R.drawable.m_route6,
            R.drawable.m_route7,
            R.drawable.m_route8,
            R.drawable.m_route9,
            R.drawable.m_route10,
            R.drawable.m_route11,
            R.drawable.m_route12,
            R.drawable.m_route13,
            R.drawable.m_route14,
            R.drawable.m_route15,
            R.drawable.m_route22,
            R.drawable.m_route23

    };

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Route route = (Route) o;

        if (id != route.id) return false;
        return title.equals(route.title);

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + title.hashCode();
        return result;
    }

}
