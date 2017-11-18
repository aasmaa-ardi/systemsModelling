package ee.ut.sm.hw02.models;

import ee.ut.sm.hw02.ExtendedTime;
import ee.ut.sm.hw02.enums.TravelType;

import static ee.ut.sm.hw02.enums.TravelType.WALK;

public class TravelLeg {

	private TravelType travelType;
    private Route route;
    private Trip usedTrip;
    private PublicTransportStop source;
    private PublicTransportStop destination;
    private ExtendedTime departureTime;
    private ExtendedTime arrivalTime;

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public TravelType getTravelType() {
		return travelType;
	}

	public void setTravelType(TravelType travelType) {
		this.travelType = travelType;
	}

    public PublicTransportStop getSource() {
        return source;
    }

    public void setSource(PublicTransportStop source) {
        this.source = source;
    }

    public PublicTransportStop getDestination() {
        return destination;
    }

    public void setDestination(PublicTransportStop destination) {
        this.destination = destination;
    }

    public Trip getUsedTrip() {
        return usedTrip;
    }

    public void setUsedTrip(Trip usedTrip) {
        this.usedTrip = usedTrip;
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
        String response = "TravelLeg: "+ departureTime +" - "+ arrivalTime + " from ("
                + source.getId()+", "+source.getStopName() +") to ("+ destination.getId()+ ", "
                + destination.getStopName()+") travel type: " + travelType;
        if(!travelType.equals(WALK)){
            response = response + " " + route.getType() + " route: " + route.getShortName()
                    + " trip:" + usedTrip.getTripId();
        }
		return response;
	}
}
