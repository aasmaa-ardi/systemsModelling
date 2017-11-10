package ee.ut.sm.hw02.filters;


import ee.ut.sm.hw02.models.Trip;
import java.util.List;
import java.util.stream.Collectors;

public class TripCriteria {

    public List<Trip> meetsCriteria(List<Trip> trips, Long arrStationId, Long depStationId, int weekdayInt) {
        return trips.stream()
                .filter(trip -> trip.getStops().contains(arrStationId) && trip.getStops().contains(arrStationId)
                && (trip.getStops().indexOf(arrStationId)>trip.getStops().indexOf(depStationId))
                && trip.getDays()[weekdayInt-1])
                .collect(Collectors.toList());
    }
}