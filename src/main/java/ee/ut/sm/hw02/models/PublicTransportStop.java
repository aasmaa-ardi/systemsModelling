package ee.ut.sm.hw02.models;

import ee.ut.sm.hw02.Trip;

import java.util.*;

/**
 * @(#) PublicTransportStop.java
 */

public class PublicTransportStop {
	private Long id;
	private Double latitude;
	private Double longitude;
    private String stopName;

    private List<Trip> trips;
    private Map<Long, Date> timesMap; //key = tripId, value = arrivalTime;
    private Map<Long, TravelInfo> infoMap; //key = tripId, value = Travel info containing previous and next stop

    public PublicTransportStop() {
        trips = new ArrayList<>();
        timesMap = new HashMap<>();
        infoMap = new HashMap<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getStopName() {
        return stopName;
    }

    public void setStopName(String stopName) {
        this.stopName = stopName;
    }

    public void addTrip(Trip trip) {
        trips.add(trip);
    }

    public void addTime(Long tripId, Date time) {
        timesMap.put(tripId, time);
    }

    public void addToInfoMap(Long tripId, TravelInfo info) {
        infoMap.put(tripId, info);
    }

    public Date getTimeForTrip(Long tripId) {
        return timesMap.get(tripId);
    }
}
