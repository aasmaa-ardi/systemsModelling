package ee.ut.sm.hw02.filters;

import ee.ut.sm.hw02.models.PublicTransportStop;
import ee.ut.sm.hw02.models.Trip;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class StopCoordsCriteria {

    public List<PublicTransportStop> meetsCriteria(List<PublicTransportStop> stops, Double latitude, Double longitude) {
        List<PublicTransportStop> meetsCrit = new ArrayList<>();

        for (PublicTransportStop stop: stops) {
            if (stop.getLatitude().equals(latitude) && stop.getLongitude().equals(longitude)) {
                meetsCrit.add(stop);
            }
        }
        return meetsCrit;
    }
}
