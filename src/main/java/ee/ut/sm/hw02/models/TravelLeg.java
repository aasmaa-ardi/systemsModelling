package ee.ut.sm.hw02.models;

import ee.ut.sm.hw02.enums.TravelType;

public class TravelLeg {
	private TravelType travelType;
    private Route route;
    private PublicTransportStop source;
	private PublicTransportStop destination;

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
}
