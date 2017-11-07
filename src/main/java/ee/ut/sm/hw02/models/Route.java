package ee.ut.sm.hw02.models;

import ee.ut.sm.hw02.Trip;
import ee.ut.sm.hw02.enums.RouteType;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class Route {

	private String id;
	private String shortName;
	private String longName;
	private PublicTransportStop startStop;
	private PublicTransportStop lastStop;
	private List<PublicTransportStop> stops;
	private List<Trip> trips;
	private RouteType type;

	public Route() {
        stops = new LinkedList<>();
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

    public PublicTransportStop getStartStop() {
        return startStop;
    }

    public void setStartStop(PublicTransportStop startStop) {
        this.startStop = startStop;
    }

    public PublicTransportStop getLastStop() {
        return lastStop;
    }

    public void setLastStop(PublicTransportStop lastStop) {
        this.lastStop = lastStop;
    }

    public List<PublicTransportStop> getStops() {
        return stops;
    }

    public void setStops(List<PublicTransportStop> stops) {
        this.stops = stops;
    }

    public List<Trip> getTrips() {
        return trips;
    }

    public void setTrips(List<Trip> trips) {
        this.trips = trips;
    }

    public RouteType getType() {
        return type;
    }

    public void setType(RouteType type) {
        this.type = type;
    }

    public void addTrip(Trip trip) {
	    trips.add(trip);
    }
}
