package ee.ut.sm.hw02.models;

import java.util.*;

public class Timetable {
    private PublicTransportStop stop;
    private List<Trip> trips;
    private Map<Long, Date> timesMap; //key = tripId, value = arrivalTime;
    private Map<Long, TravelInfo> infoMap; //key = tripId, value = Travel info containing previous and next stop

    public Timetable() {
        trips = new ArrayList<>();
        timesMap = new HashMap<>();
        infoMap = new HashMap<>();
    }

	public PublicTransportStop getStop() {
		return stop;
	}

	public void setStop(PublicTransportStop stop) {
		this.stop = stop;
	}

    public List<Trip> getTrips() {
        return trips;
    }

    public void setTrips(List<Trip> trips) {
        this.trips = trips;
    }

    public Map<Long, Date> getTimesMap() {
        return timesMap;
    }

    public void setTimesMap(Map<Long, Date> timesMap) {
        this.timesMap = timesMap;
    }

    public Map<Long, TravelInfo> getInfoMap() {
        return infoMap;
    }

    public void setInfoMap(Map<Long, TravelInfo> infoMap) {
        this.infoMap = infoMap;
    }

    public void addTrip(Trip trip) {
        trips.add(trip);
    }

    public void addTime(Long tripId, Date time) {
        timesMap.put(tripId, time);
    }

    public Date getTime(Long tripId) {
        return timesMap.get(tripId);
    }

    public void addInfo(Long tripId, TravelInfo travelInfo) {
        infoMap.put(tripId, travelInfo);
    }
}
