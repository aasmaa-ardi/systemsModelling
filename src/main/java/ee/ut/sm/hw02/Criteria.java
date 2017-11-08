package ee.ut.sm.hw02;

import ee.ut.sm.hw02.models.Trip;

import java.util.List;

public interface Criteria {

    List<Trip> meetsCriteria(List<Trip> ttrips, int day);
}
