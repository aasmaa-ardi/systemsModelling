package ee.ut.sm.hw02.models;

import ee.ut.sm.hw02.OwnTime;

import java.time.Duration;
import java.time.LocalTime;

public class TravelInfo {

    private PublicTransportStop actualStop;
    private PublicTransportStop nextStop;
    private Duration travelTime;
    private OwnTime departureTime;
    private OwnTime arrivalTime;

    public TravelInfo(PublicTransportStop actualStop, PublicTransportStop nextStop, Long tripId) {
        this.actualStop = actualStop;
        this.nextStop = nextStop;
        if (actualStop != null && nextStop != null) {
            departureTime = actualStop.getTimetable().getTime(tripId);
            arrivalTime = nextStop.getTimetable().getTime(tripId);
        } else {
            this.travelTime = null;
        }
    }

    public PublicTransportStop getNextStop() {
        return nextStop;
    }

    public void setNextStop(PublicTransportStop nextStop) {
        this.nextStop = nextStop;
    }

    public Duration getTravelTime() {
        return travelTime;
    }

    public void setTravelTime(Duration travelTime) {
        this.travelTime = travelTime;
    }

    public PublicTransportStop getActualStop() {
        return actualStop;
    }

    public void setActualStop(PublicTransportStop actualStop) {
        this.actualStop = actualStop;
    }

    public OwnTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(OwnTime departureTime) {
        this.departureTime = departureTime;
    }

    public OwnTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(OwnTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    @Override
    public String toString() {
        return "TravelInfo{" +
                "actualStop=" + actualStop +
                ", nextStop=" + nextStop +
                ", travelTime=" + travelTime +
                '}';
    }
}
