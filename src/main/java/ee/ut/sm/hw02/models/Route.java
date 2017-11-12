package ee.ut.sm.hw02.models;

import ee.ut.sm.hw02.enums.RouteType;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class Route {

	private String id;
	private String shortName;
	private String longName;
    private RouteType type;
    private List<Long> trips;

	public Route() {
        trips = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getLongName() {
        return longName;
    }

    public void setLongName(String longName) {
        this.longName = longName;
    }

    public List<Long> getTrips() {
        return trips;
    }

    public void setTrips(List<Long> trips) {
        this.trips = trips;
    }

    public RouteType getType() {
        return type;
    }

    public void setType(RouteType type) {
        this.type = type;
    }

    public void addTrip(Long trip) {
	    trips.add(trip);
    }

    @Override
    public String toString() {
        return "Route{" +
                "id='" + id + '\'' +
                ", shortName='" + shortName + '\'' +
                ", longName='" + longName + '\'' +
                ", type=" + type +
                '}';
    }
}
