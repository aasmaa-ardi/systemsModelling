package ee.ut.sm.hw02.models;

import ee.ut.sm.hw02.helpers.TimeHelper;

import java.util.Date;

public class TravelInfo {
    private PublicTransportStop actualStop;
    private PublicTransportStop nextStop;
    private Date travelTime;

    public TravelInfo(PublicTransportStop actualStop, PublicTransportStop nextStop, Long tripId) {
        this.actualStop = actualStop;
        this.nextStop = nextStop;
        if (actualStop != null && nextStop != null) {
            Date actualStopTime = actualStop.getTimetable().getTime(tripId);
            Date nextStopTime = nextStop.getTimetable().getTime(tripId);
            this.travelTime = TimeHelper.getDate(Math.abs(nextStopTime.getTime() - actualStopTime.getTime()));
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

    public Date getTravelTime() {
        return travelTime;
    }

    public void setTravelTime(Date travelTime) {
        this.travelTime = travelTime;
    }

    public PublicTransportStop getActualStop() {
        return actualStop;
    }

    public void setActualStop(PublicTransportStop actualStop) {
        this.actualStop = actualStop;
    }
}
