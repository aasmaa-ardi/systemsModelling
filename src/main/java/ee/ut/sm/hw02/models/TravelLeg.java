package ee.ut.sm.hw02.models;

import ee.ut.sm.hw02.OwnTime;
import ee.ut.sm.hw02.enums.TravelType;

import java.time.LocalTime;

public class TravelLeg {

	private TravelType travelType;
    private Route route;
    private PublicTransportStop source;
    private PublicTransportStop destination;
    private OwnTime departureTime;
    private OwnTime arrivalTime;
    private Trip usedTrip;

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
		return "TravelLeg{" +
				"travelType=" + travelType +
				", route='" + route + '\'' +
				", trip='" + usedTrip.getTripId() + '\'' +
				", departure Stop Id =" + source +
				", destination Stop Id =" + destination +
				'}';
	}
}
