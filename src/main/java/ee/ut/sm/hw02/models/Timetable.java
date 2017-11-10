package ee.ut.sm.hw02.models;

import java.util.*;

public class Timetable {
    private Long stop;
    private List<Long> trips;
    private Map<Long, Date> timesMap; //key = tripId, value = arrivalTime;
    private Map<Long, TravelInfo> infoMap; //key = tripId, value = Travel info containing previous and next stop

    public Timetable() {
        trips = new ArrayList<>();
        timesMap = new HashMap<>();
        infoMap = new HashMap<>();
    }

	public Long getStop() {
		return stop;
	}

	public void setStop(Long stop) {
		this.stop = stop;
	}

    public List<Long> getTrips() {
        return trips;
    }

    public void setTrips(List<Long> trips) {
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

    public void addTrip(Long trip) {
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
