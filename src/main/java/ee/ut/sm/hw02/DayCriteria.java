package ee.ut.sm.hw02;

import java.util.ArrayList;
import java.util.List;

public class DayCriteria implements Criteria {

    @Override
    public List<Trip> meetsCriteria(List<Trip> trips, int day) {
        List<Trip> onDay = new ArrayList<>();
        for (Trip trip: trips) {
            if (trip.getDays()[day]) {
                onDay.add(trip);
            }
        }
        return onDay;
    }
}
