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
            R.mipmap.route1,
            R.mipmap.route2,
            R.mipmap.route3,
            R.mipmap.route4,
            R.mipmap.route5,
            R.mipmap.route6,
            R.mipmap.route7,
            R.mipmap.route8,
            R.mipmap.route9,
            R.mipmap.route11,
            R.mipmap.route18
    };

    public static Integer[] iconTaxiRoutes = {
            R.mipmap.m_route1,
            R.mipmap.m_route2,
            R.mipmap.m_route3,
            R.mipmap.m_route4,
            R.mipmap.m_route5,
            R.mipmap.m_route6,
            R.mipmap.m_route7,
            R.mipmap.m_route8,
            R.mipmap.m_route9,
            R.mipmap.m_route10,
            R.mipmap.m_route11,
            R.mipmap.m_route12,
            R.mipmap.m_route13,
            R.mipmap.m_route14,
            R.mipmap.m_route15,
            R.mipmap.m_route22,
            R.mipmap.m_route23

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
