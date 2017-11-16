package ee.ut.sm.hw02.models;

import ee.ut.sm.hw02.OwnTime;

import java.time.LocalTime;
import java.util.*;

public class Timetable {

    private PublicTransportStop stop;
    private List<Long> trips;
    private Map<Long, OwnTime> timesMap; //key = tripId, value = arrivalTime;
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

    public List<Long> getTrips() {
        List<Long> copy = new ArrayList<>();
        copy.addAll(trips);
        return copy;
    }

    public void setTrips(List<Long> trips) {
        this.trips = trips;
    }

    public Map<Long, OwnTime> getTimesMap() {
        return timesMap;
    }

    public void setTimesMap(Map<Long, OwnTime> timesMap) {
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

    public void addTime(Long tripId, OwnTime time) {
        timesMap.put(tripId, time);
    }

    public OwnTime getTime(Long tripId) {
        return timesMap.get(tripId);
    }

    public void addInfo(Long tripId, TravelInfo travelInfo) {
        infoMap.put(tripId, travelInfo);
    }
}
