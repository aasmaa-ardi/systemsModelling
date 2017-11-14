package ee.ut.sm.hw02.filters;


import ee.ut.sm.hw02.models.PublicTransportStop;
import ee.ut.sm.hw02.models.Trip;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TripCriteria {

    public List<Long> tripsContainingStations(List<Trip> trips, PublicTransportStop arrStation, PublicTransportStop depStation, int weekdayInt) {
        return trips.stream()
                .filter(trip -> trip.getStops().contains(arrStation) && trip.getStops().contains(depStation)
               // && (trip.getStops().indexOf(arrStation)>trip.getStops().indexOf(depStation))
                && trip.getDays()[weekdayInt-1]).map(Trip::getTripId).collect(Collectors.toList());
    }

    public List<Long> sortedTripsContainingStops(List<Trip> trips, PublicTransportStop depStop, PublicTransportStop destStop, LocalDateTime dateTime){
        List<Long> tripIds = trips.stream()
                .filter(trip -> trip.getStops().contains(depStop) && trip.getStops().contains(depStop)
                        && trip.getDays()[dateTime.getDayOfWeek().getValue()-1]).map(Trip::getTripId).collect(Collectors.toList());
        Map<Long, LocalTime> timesMap = depStop.getTimetable().getTimesMap();

        //which ones leave after the time we want and go directly to arrival station
        Map<Long, LocalTime> tripsLeavingAfter = timesMap.entrySet()
                .stream().filter(p -> (p.getValue().isAfter(dateTime.toLocalTime())) && tripIds.contains(p.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        return tripsLeavingAfter.entrySet().stream()
                .sorted(Comparator.comparing(Map.Entry::getValue)).map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public List<Long> tripsContainingDepStation(List<Trip> trips, PublicTransportStop depStation, LocalDateTime dateTime) {

        List<Long> tripIds =trips.stream()
                .filter(trip -> trip.getStops().contains(depStation)
                        && trip.getDays()[dateTime.getDayOfWeek().getValue()-1]).map(Trip::getTripId).collect(Collectors.toList());

        Map<Long, LocalTime> timesMap = depStation.getTimetable().getTimesMap();

        //which ones leave after the time we want and go directly to arrival station
        Map<Long, LocalTime> tripsLeavingAfter = timesMap.entrySet()
                .stream().filter(p -> (p.getValue().isAfter(dateTime.toLocalTime())) && tripIds.contains(p.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        //sort the trips by time
        return tripsLeavingAfter.entrySet().stream()
                .sorted(Comparator.comparing(Map.Entry::getValue)).map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}
