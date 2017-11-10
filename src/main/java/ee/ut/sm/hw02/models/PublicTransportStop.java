package ee.ut.sm.hw02.models;

/**
 * @(#) PublicTransportStop.java
 */

public class PublicTransportStop {

	private Long id;
	private Double latitude;
	private Double longitude;
    private String stopName;
    private Timetable timetable;

    public PublicTransportStop() {
        timetable = new Timetable();
        timetable.setStop(id);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getStopName() {
        return stopName;
    }

    public void setStopName(String stopName) {
        this.stopName = stopName;
    }

    public Timetable getTimetable() {
        return timetable;
    }

    public void setTimetable(Timetable timetable) {
        this.timetable = timetable;
    }
}
