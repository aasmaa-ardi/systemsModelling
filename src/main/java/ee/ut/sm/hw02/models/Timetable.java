package ee.ut.sm.hw02.models;

import java.time.LocalTime;
import java.util.*;

public class Timetable {
    private Long stopId;
    private List<Long> trips;
    private Map<Long, LocalTime> timesMap; //key = tripId, value = arrivalTime;
    private Map<Long, TravelInfo> infoMap; //key = tripId, value = Travel info containing previous and next stop

    public Timetable() {
        trips = new ArrayList<>();
        timesMap = new HashMap<>();
        infoMap = new HashMap<>();
    }

	public Long getStop() {
		return stopId;
	}

	public void setStop(Long stopId) {
		this.stopId = stopId;
	}

    public List<Long> getTrips() {
        return trips;
    }

    public void setTrips(List<Long> trips) {
        this.trips = trips;
    }

    public Map<Long, LocalTime> getTimesMap() {
        return timesMap;
    }

    public void setTimesMap(Map<Long, LocalTime> timesMap) {
        this.timesMap = timesMap;
    }

    public Map<Long, TravelInfo> getInfoMap() {
        return infoMap;
    }

    public void setInfoMap(Map<Long, TravelInfo> infoMap) {
        this.infoMap = infoMap;
    }

    public void addTrip(Long tripId) {
        trips.add(tripId);
    }

    public void addTime(Long tripId, LocalTime time) {
        timesMap.put(tripId, time);
    }

    public LocalTime getTime(Long tripId) {
        return timesMap.get(tripId);
    }

    public void addInfo(Long tripId, TravelInfo travelInfo) {
        infoMap.put(tripId, travelInfo);
    }
}
