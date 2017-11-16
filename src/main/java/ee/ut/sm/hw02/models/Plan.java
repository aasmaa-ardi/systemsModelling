package ee.ut.sm.hw02.models;


import ee.ut.sm.hw02.OwnTime;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Plan {

    private PublicTransportStop departureStop;
    private PublicTransportStop destinationStop;
    private Date departureDate;
    private OwnTime departureTime;
    private OwnTime approxArrivalTime;
    private List<TravelLeg> travelLegs;

    public Plan() {
        travelLegs = new ArrayList<>();
    }

    public Date getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(Date departureDate) {
        this.departureDate = departureDate;
    }

    public OwnTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(OwnTime departureTime) {
        this.departureTime = departureTime;
    }

    public OwnTime getApproxArrivalTime() {
        return approxArrivalTime;
    }

    public void setApproxArrivalTime(OwnTime approxArrivalTime) {
        this.approxArrivalTime = approxArrivalTime;
    }

    public PublicTransportStop getDepartureStop() {
        return departureStop;
    }

    public void setDepartureStop(PublicTransportStop departureStop) {
        this.departureStop = departureStop;
    }

    public PublicTransportStop getDestinationStop() {
        return destinationStop;
    }

    public void setDestinationStop(PublicTransportStop destinationStop) {
        this.destinationStop = destinationStop;
    }

    public List<TravelLeg> getTravelLegs() {
        return travelLegs;
    }

    public void setTravelLegs(List<TravelLeg> travelLegs) {
        this.travelLegs = travelLegs;
    }

    @Override
    public String toString() {
        return "Plan{" +
                "departureStop=" + departureStop.getStopName() +
                ", destinationStop=" + destinationStop.getStopName() +
                ", departureTime=" + departureTime +
                ", approxArrivalTime=" + approxArrivalTime +
                ", travelLegs=" + travelLegs +
                '}';
    }
}
