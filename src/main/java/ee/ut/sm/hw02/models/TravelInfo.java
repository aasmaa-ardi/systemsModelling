package ee.ut.sm.hw02.models;

import java.util.Date;

public class TravelInfo {
    private PublicTransportStop nextStop;
    private Date travelTime;

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
}
