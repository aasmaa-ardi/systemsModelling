package ee.ut.sm.hw02.models;

import ee.ut.sm.hw02.enums.TravelType;

public class TravelLeg {
	private TravelType travelType;
    private Long route;
    private Long source;
	private Long destination;

	public Long getRoute() {
		return route;
	}

	public void setRoute(Long route) {
		this.route = route;
	}

	public TravelType getTravelType() {
		return travelType;
	}

	public void setTravelType(TravelType travelType) {
		this.travelType = travelType;
	}

	public Long getSource() {
		return source;
	}

	public void setSource(Long source) {
		this.source = source;
	}

	public Long getDestination() {
		return destination;
	}

	public void setDestination(Long destination) {
		this.destination = destination;
	}
}
