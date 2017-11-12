package ee.ut.sm.hw02.models;

import java.time.Duration;
import java.time.LocalTime;

public class TravelInfo {
    private PublicTransportStop actualStop;
    private PublicTransportStop nextStop;
    private Duration travelTime;

    public TravelInfo(PublicTransportStop actualStop, PublicTransportStop nextStop, Long tripId) {
        this.actualStop = actualStop;
        this.nextStop = nextStop;
        if (actualStop != null && nextStop != null) {
            LocalTime actualStopTime = actualStop.getTimetable().getTime(tripId);
            LocalTime nextStopTime = nextStop.getTimetable().getTime(tripId);
            this.travelTime =  Duration.between(actualStopTime, nextStopTime);
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

    @Override
    public String toString() {
        return "TravelInfo{" +
                "actualStop=" + actualStop +
                ", nextStop=" + nextStop +
                ", travelTime=" + travelTime +
                '}';
    }
}
