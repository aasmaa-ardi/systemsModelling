package ee.ut.sm.hw02.models; /**
 * @(#) Timetable.java
 */

import ee.ut.sm.hw02.enums.TimetableType;

import java.util.List;
import java.util.Map;

public class Timetable {
	private Map<Integer, List<Integer>> departureTimes;
	private PublicTransportStop stop;
	private TimetableType timetableType;
}
