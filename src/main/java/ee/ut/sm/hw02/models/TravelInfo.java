package ee.ut.sm.hw02.models;

import ee.ut.sm.hw02.ExtendedTime;

import java.time.Duration;

public class TravelInfo {

    private PublicTransportStop actualStop;
    private PublicTransportStop nextStop;
    private Duration travelTime;
    private ExtendedTime departureTime;
    private ExtendedTime arrivalTime;

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

    public ExtendedTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(ExtendedTime departureTime) {
        this.departureTime = departureTime;
    }

    public ExtendedTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(ExtendedTime arrivalTime) {
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
