package ee.ut.sm.hw02.models;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Trip {

    private Long tripId;
    private Long serviceId;
    private String directionCode;
    private Route route;
    private LinkedList<PublicTransportStop> stops;
    private boolean[] days;

    public Trip() {
        stops = new LinkedList<>();
    }

    public Long getTripId() {
        return tripId;
    }

    public void setTripId(Long tripId) {
        this.tripId = tripId;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public String getDirectionCode() {
        return directionCode;
    }

    public void setDirectionCode(String directionCode) {
        this.directionCode = directionCode;
    }

    public LinkedList<PublicTransportStop> getStops() {
        return stops;
    }

    public void setStops(LinkedList<PublicTransportStop> stops) {
        this.stops = stops;
    }

    public boolean[] getDays() {
        return days;
    }

    public void setDays(boolean[] days) {
        this.days = days;
    }

    @Override
    public String toString() {
        return "Trip{" +
                "tripId=" + tripId +
                ", serviceId=" + serviceId +
                ", directionCode='" + directionCode + '\'' +
                ", routeId=" + route.getId() +
                ", days=" + Arrays.toString(days) +
                '}';
    }
}
