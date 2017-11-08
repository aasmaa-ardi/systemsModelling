package ee.ut.sm.hw02.models; /**
 * @(#) Plan.java
 */

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Plan {
    private PublicTransportStop departureStop;
    private PublicTransportStop destinationStop;
    private Date departureTime;
    private Date approxArrivalTime;
    private List<TravelLeg> travelLegs;

    public Plan() {
        travelLegs = new ArrayList<>();
    }

    public Date getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Date departureTime) {
        this.departureTime = departureTime;
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

    public Date getApproxArrivalTime() {
        return approxArrivalTime;
    }

    public void setApproxArrivalTime(Date approxArrivalTime) {
        this.approxArrivalTime = approxArrivalTime;
    }
}
