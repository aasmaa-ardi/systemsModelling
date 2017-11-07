package ee.ut.sm.hw02.models;

import ee.ut.sm.hw02.enums.TimetableType;

import java.util.List;
import java.util.Map;

public class Timetable {
	private Map<Long, List<String>> departureTimes;
	private PublicTransportStop stop;
	private TimetableType timetableType;

	public Map<Long, List<String>> getDepartureTimes() {
		return departureTimes;
	}

	public void setDepartureTimes(Map<Long, List<String>> departureTimes) {
		this.departureTimes = departureTimes;
	}

	public PublicTransportStop getStop() {
		return stop;
	}

	public void setStop(PublicTransportStop stop) {
		this.stop = stop;
	}

	public TimetableType getTimetableType() {
		return timetableType;
	}

	public void setTimetableType(TimetableType timetableType) {
		this.timetableType = timetableType;
	}
}
