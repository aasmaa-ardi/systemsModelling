package ee.ut.sm.hw02.models;

import ee.ut.sm.hw02.enums.RouteType;

import java.util.List;


public class Route {
	private String id;
	private String shortName;
	private String longName;
	private PublicTransportStop startStop;
	private PublicTransportStop lastStop;
	private List<PublicTransportStop> stops;
	private RouteType type;

    public Route(String id, String shortName, String longName, RouteType type) {
        this.id = id;
        this.shortName = shortName;
        this.longName = longName;
        this.type = type;
    }
}
