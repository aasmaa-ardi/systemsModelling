package ee.ut.sm.hw02;

import ee.ut.sm.hw02.enums.RouteType;
import ee.ut.sm.hw02.enums.TravelType;
import ee.ut.sm.hw02.filters.StopCriteria;
import ee.ut.sm.hw02.models.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class TripPlannerController {
    
    private Map<Long, PublicTransportStop> stops;
    private Map<Long, Trip> trips;
    private Map<String, Route> routes;
    private List<PublicTransportStop> stopsList;
    private StopCriteria stopCriteria;

    public TripPlannerController() throws IOException {
        stops = new HashMap<>();
        routes = new HashMap<>();
        trips = new HashMap<>();
        stopCriteria = new StopCriteria();
        this.stopsList = new ArrayList<>();
        if (! (loadStops() && loadRoutes() && loadTrips() && loadStopTimes() && loadCalendar())) {
            throw new IOException("Error while loading files");
        }
        setStopsInfo();

        System.out.println("All data loaded...");
    }

    private PublicTransportStop findStop(String param){
        PublicTransportStop result = null;
        try {
            if (param.startsWith("ID")) {
                Long stopId = Long.valueOf(param.substring(2));
                result = stops.get(stopId);
            } else if (param.startsWith("X") && param.contains("Y")) {
                String[] parts = param.substring(1).split("Y");
                result = stopCriteria.getPublicTransportStopByCoordinates(stopsList, Double.valueOf(parts[0]), Double.valueOf(parts[1]));
            }
        } catch(Exception e){
            System.out.println("There was a problem with user input parameters.");
        }
        return result;
    }

    private Plan dijkstra(PublicTransportStop departure, PublicTransportStop destination,
                          OwnTime departureTime, int weekDay) {
        List<PublicTransportStop> unfinishedStops = new ArrayList<>();
        unfinishedStops.addAll(stops.values());

        Map<PublicTransportStop, OwnTime> dist = new HashMap<>();
        Map<PublicTransportStop, PublicTransportStop> prev = new HashMap<>();
        Map<PublicTransportStop, Long> usedTrips = new HashMap<>();

        for (PublicTransportStop stop: unfinishedStops) {
            dist.put(stop, OwnTime.MAX);
            prev.put(stop, null);
            usedTrips.put(stop, null);
        }
        dist.put(departure, departureTime);

        while (! unfinishedStops.isEmpty()) {
            PublicTransportStop nearestStop = getStopWithMinDist(dist, unfinishedStops);
            unfinishedStops.remove(nearestStop);

            for(PublicTransportStop stop: unfinishedStops){
                long walkSeconds = computeWalkInSeconds(stop, nearestStop);
                //max walking time 60 minutes
                if( walkSeconds < 3600){
                    OwnTime walkEnd = dist.get(nearestStop).addTime(new OwnTime(walkSeconds));
                    if( walkEnd.isBefore(dist.get(stop))) {
                        dist.put(stop, walkEnd);
                        prev.put(stop, nearestStop);
                    }
                }
            }

            List<Long> tripsOfStop = nearestStop.getTimetable().getTrips();

            Iterator<Long> iter = tripsOfStop.iterator(); //remove all trips that are not taken on the input day
            while (iter.hasNext()) {
                Trip trip = trips.get(iter.next());
                if (! trip.getDays()[weekDay]) {
                    iter.remove();
                }
            }

            Map<Long, TravelInfo> infoMap = nearestStop.getTimetable().getInfoMap();
            Map<Long, OwnTime> timesMap = nearestStop.getTimetable().getTimesMap();

            for(Long tripId: tripsOfStop) { //try every connection to travel to another stop
                OwnTime tripTime = timesMap.get(tripId); //departure time from nearestStop for actual trip
                if (tripTime.isBefore(dist.get(nearestStop))) { //if trip is before time that we have we cannot take it
                    continue;
                }
                TravelInfo info = infoMap.get(tripId);
                PublicTransportStop nextStop = info.getNextStop();
                if (nextStop == null) { //if stop is last we cannot get further, that means nextStop == null
                    continue;
                }

                OwnTime arrivalTime = info.getArrivalTime(); //arrivalTime to nextStop
                if (arrivalTime.isBefore(dist.get(nextStop))) {
                    dist.put(nextStop, arrivalTime);
                    prev.put(nextStop, nearestStop);
                    usedTrips.put(nextStop, tripId);
                }
            }
        }

        Plan plan = new Plan();

        PublicTransportStop actualStop = destination;
        PublicTransportStop prevStop = prev.get(actualStop);
        LinkedList<TravelLeg> travelLegs = new LinkedList<>();
        while (prevStop != null) {
            //while trip has same id, we can consider it as one travelLeg
            TravelLeg travelLeg = new TravelLeg();
            travelLeg.setDestination(actualStop);

            Long usedTrip = usedTrips.get(actualStop);
            if (usedTrip == null) {
                travelLeg.setTravelType(TravelType.WALK);
            } else {
                travelLeg.setUsedTrip(trips.get(usedTrip));
                travelLeg.setRoute(travelLeg.getUsedTrip().getRoute());
                travelLeg.setTravelType(TravelType.PUBLIC_TRANSPORT);
                while (usedTrip.equals(usedTrips.get(prevStop))) {
                    prevStop = prev.get(prevStop);
                }
            }
            travelLeg.setArrivalTime(dist.get(actualStop));
            //id of used trip has changed, this is the end of the travel leg
            travelLeg.setSource(prevStop);
            travelLeg.setDepartureTime(dist.get(prevStop));
            travelLegs.addFirst(travelLeg);

            actualStop = prevStop;
            prevStop = prev.get(prevStop);
        }
        if (travelLegs.isEmpty()) {
            return null;
        }
        plan.setTravelLegs(travelLegs);
        plan.setDepartureStop(departure);
        plan.setDestinationStop(destination);
        TravelLeg first = travelLegs.getFirst();
        if (first.getTravelType() == TravelType.PUBLIC_TRANSPORT) {
            first.setDepartureTime(first.getSource().getTimetable().getTime(first.getUsedTrip().getTripId()));
        }
        plan.setDepartureTime(travelLegs.getFirst().getDepartureTime());
        plan.setApproxArrivalTime(travelLegs.getLast().getArrivalTime());
        return plan;
    }

    private PublicTransportStop getStopWithMinDist(Map<PublicTransportStop, OwnTime> dist,
                                                   List<PublicTransportStop> unfinishedStops) {
        PublicTransportStop closestStop = null;
        for (PublicTransportStop stop: unfinishedStops) {
            if (closestStop == null || dist.get(stop).isBefore(dist.get(closestStop))) {
                closestStop = stop;
            }
        }
        return closestStop;
    }

    public Plan getPlanForTrip(String departureString, String destinationString, String dateString, String departureTimeString) {
        OwnTime departureTime = new OwnTime(departureTimeString);
        LocalDate date = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        int dayOfWeek = date.getDayOfWeek().getValue();

        PublicTransportStop destinationStop = findStop(destinationString);
        PublicTransportStop departureStop = findStop(departureString);

        return dijkstra(departureStop, destinationStop, departureTime, dayOfWeek - 1);
    }

    private long computeWalkInSeconds(PublicTransportStop fromStop, PublicTransportStop toStop){
        return Math.round(distBetweenTwoStops(fromStop, toStop) / 1.38);
    }

    private double distBetweenTwoStops(PublicTransportStop fromStop, PublicTransportStop toStop){
        double r = 6371; // kiloMetres
        double lat1 = Math.toRadians(fromStop.getLatitude());
        double lat2 = Math.toRadians(toStop.getLatitude());
        double dLat = Math.toRadians(toStop.getLatitude() - fromStop.getLatitude());
        double dLon = Math.toRadians(toStop.getLongitude() - fromStop.getLongitude());

        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(lat1) * Math.cos(lat2) *
                        Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        return r * c * 1000; //metres
    }

    // INITIALIZATION METHODS FOR READING FROM FILES

    private void setStopsInfo() {
        for (Trip trip: trips.values()) {
            List<PublicTransportStop> stopsList = trip.getStops();
            for (int i = 1; i < stopsList.size(); i++) {
                PublicTransportStop actualStop = stopsList.get(i-1);
                PublicTransportStop nextStop = stopsList.get(i);
                TravelInfo info = new TravelInfo(actualStop, nextStop, trip.getTripId());

                actualStop.getTimetable().addInfo(trip.getTripId(), info);
            }
            PublicTransportStop lastStop = trip.getStops().getLast();
            TravelInfo info = new TravelInfo(lastStop, null, trip.getTripId());
            lastStop.getTimetable().addInfo(trip.getTripId(), info);
        }
        System.out.println("Stops info loaded...");
    }

    private boolean loadCalendar() {
        File sourceFile = new File("db_files/calendar.csv");
        try (BufferedReader br = new BufferedReader(new FileReader(sourceFile))) {
            String line;
            StringTokenizer tokenizer;
            while ((line = br.readLine()) != null) {
                tokenizer = new StringTokenizer(line, ",");
                Long serviceId = Long.parseLong(tokenizer.nextToken());
                boolean[] days = new boolean[7];
                for (int i = 0; i < days.length; i++) {
                    days[i] = "true".equals(tokenizer.nextToken());
                }
                for (Trip trip: trips.values()) {
                    if (trip.getServiceId().equals(serviceId)) {
                        trip.setDays(days);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error while loading calendar!");
            return false;
        }
        System.out.println("Calendar loaded...");
        return true;
    }

    private boolean loadStopTimes() {
        File sourceFile = new File("db_files/stop_times.csv");
        try (BufferedReader br = new BufferedReader(new FileReader(sourceFile))) {
            String line;
            StringTokenizer tokenizer;
            while ((line = br.readLine()) != null) {
                tokenizer = new StringTokenizer(line, ",");
                Long tripId = Long.parseLong(tokenizer.nextToken());
                tokenizer.nextToken();
                String depTime = tokenizer.nextToken();
                Long stopId = Long.parseLong(tokenizer.nextToken());
                PublicTransportStop stop = stops.get(stopId);
                Trip trip = trips.get(tripId);
                Timetable timetable = stop.getTimetable();
                trip.getStops().addLast(stops.get(stopId));
                timetable.addTrip(tripId);
                timetable.addTime(tripId, new OwnTime(depTime));
            }
        } catch (IOException|DateTimeParseException e) {
            System.out.println(e);
            System.err.println("Error while loading stop_times!");
            return false;
        }
        System.out.println("Stop_times loaded...");
        return true;
    }

    private boolean loadStops() {
        File sourceFile = new File("db_files/stops.csv");
        try (BufferedReader br = new BufferedReader(new FileReader(sourceFile))) {
            String line;
            StringTokenizer tokenizer;
            while ((line = br.readLine()) != null) {
                tokenizer = new StringTokenizer(line, ",");
                PublicTransportStop stop = new PublicTransportStop();
                stop.setId(Long.parseLong(tokenizer.nextToken()));
                stop.setStopName(tokenizer.nextToken());
                stop.setLatitude(Double.parseDouble(tokenizer.nextToken()));
                stop.setLongitude(Double.parseDouble(tokenizer.nextToken()));
                stops.put(stop.getId(), stop);
            }
        } catch (IOException e) {
            System.err.println("Error while loading stops!");
            return false;
        }
        System.out.println("Stops loaded...");
        return true;
    }

    private boolean loadTrips() {
        File sourceFile = new File("db_files/trips.csv");
        try (BufferedReader br = new BufferedReader(new FileReader(sourceFile))) {
            String line;
            StringTokenizer tokenizer;
            while ((line = br.readLine()) != null) {
                tokenizer = new StringTokenizer(line, ",");
                Trip trip = new Trip();
                String routeId = tokenizer.nextToken();
                Route route = routes.get(routeId);
                trip.setRoute(route);
                route.addTrip(trip.getTripId());
                trip.setServiceId(Long.parseLong(tokenizer.nextToken()));
                trip.setTripId(Long.parseLong(tokenizer.nextToken()));
                trip.setDirectionCode(tokenizer.nextToken());
                trips.put(trip.getTripId(), trip);
            }
        } catch (IOException e) {
            System.err.println("Error while loading trips!");
            return false;
        }
        System.out.println("Trips loaded...");
        return true;
    }

    private boolean loadRoutes() {
        File sourceFile = new File("db_files/routes.csv");
        try (BufferedReader br = new BufferedReader(new FileReader(sourceFile))) {
            String line;
            StringTokenizer tokenizer;
            while ((line = br.readLine()) != null) {
                tokenizer = new StringTokenizer(line, ",");
                Route route = new Route();
                route.setId(tokenizer.nextToken());
                route.setShortName(tokenizer.nextToken());
                route.setLongName(tokenizer.nextToken());
                route.setType(RouteType.parseType(Integer.parseInt(tokenizer.nextToken())));
                routes.put(route.getId(), route);
            }
        } catch (IOException e) {
            System.err.println("Error while loading routes!");
            return false;
        }
        System.out.println("Routes loaded...");
        return true;
    }
}
