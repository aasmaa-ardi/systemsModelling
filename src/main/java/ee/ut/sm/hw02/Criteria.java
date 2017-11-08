package ee.ut.sm.hw02;

import java.util.List;

public interface Criteria {

    List<Trip> meetsCriteria(List<Trip> ttrips, int day);
}
