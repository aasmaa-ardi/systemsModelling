package ee.ut.sm.hw02.models;

import ee.ut.sm.hw02.ExtendedTime;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Plan {

    private PublicTransportStop departureStop;
    private PublicTransportStop destinationStop;
    private LocalDate departureDate;
    private ExtendedTime departureTime;
    private ExtendedTime approxArrivalTime;
    private List<TravelLeg> travelLegs;

    public Plan() {
        travelLegs = new ArrayList<>();
    }

    public LocalDate getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(LocalDate departureDate) {
        this.departureDate = departureDate;
    }

    public ExtendedTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(ExtendedTime departureTime) {
        this.departureTime = departureTime;
    }

    public ExtendedTime getApproxArrivalTime() {
        return approxArrivalTime;
    }

    public void setApproxArrivalTime(ExtendedTime approxArrivalTime) {
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
        String response= "RESULT.\nThe Optimal trip plan from departure stop ( id= " + departureStop.getId()
                +", name ="+departureStop.getStopName()+") to destination stop ( id="+departureStop.getId()+", name="+
                destinationStop.getStopName() + ")\nwith departure time ("+departureDate.toString()+" "+departureTime.toString()+
                ") and approximate arrival time (" + approxArrivalTime +
                ")\ncontains following travelLegs:\n";
        for(int i=0; i<travelLegs.size(); i++){
            response = response + (i+1) + "." + travelLegs.get(i).toString()+"\n";
        }
        return response;
    }
}
