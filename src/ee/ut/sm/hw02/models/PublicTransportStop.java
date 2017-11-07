package ee.ut.sm.hw02.models;

/**
 * @(#) PublicTransportStop.java
 */

public class PublicTransportStop {
	private Long id;
	private Route route;
	private Double latitude;
	private Double longitude;
    private String stopName;

    private Timetable workdaysTimetable;
    private Timetable saturdayTimetable;
    private Timetable sundayTimetable;
    private TravelLeg travelLeg;
    private TravelInfo info;

    public PublicTransportStop(Long id, String stopName, Double latitude, Double longitude) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.stopName = stopName;
    }
}
