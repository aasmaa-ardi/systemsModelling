package ee.ut.sm.hw02.filters;

import ee.ut.sm.hw02.models.PublicTransportStop;
import ee.ut.sm.hw02.models.Trip;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class StopCriteria {

    public List<PublicTransportStop> meetsCriteria(List<PublicTransportStop> stops, String stopName) {
        return stops.stream()
                .filter(stop -> stop.getStopName().toUpperCase().equals(stopName.toUpperCase()))
                .collect(Collectors.toList());
    }

    public PublicTransportStop getPublicTransportStopById(List<PublicTransportStop> stops, Long stopId) {
        return stops.stream().filter(stop -> stop.getId().equals(stopId)).findFirst().get();
    }

    public PublicTransportStop getPublicTransportStopByCoordinates(List<PublicTransportStop> stops, Double latitude, Double longitude) {
        return stops.stream().filter(stop -> stop.getLatitude().equals(latitude) && stop.getLongitude().equals(longitude)).findFirst().get();
    }

}
