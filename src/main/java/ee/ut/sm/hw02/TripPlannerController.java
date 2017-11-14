package ee.ut.sm.hw02;

import ee.ut.sm.hw02.enums.RouteType;
import ee.ut.sm.hw02.enums.TravelType;
import ee.ut.sm.hw02.filters.StopCriteria;
import ee.ut.sm.hw02.filters.TripCriteria;
import ee.ut.sm.hw02.models.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

public class TripPlannerController {
    
    private Map<Long, PublicTransportStop> stops;
    private Map<Long, Trip> trips;
    private Map<String, Route> routes;
    private List<PublicTransportStop> stopsList;
    private List<Trip> tripsList;
    private Plan plan;
    private StopCriteria stopCriteria;
    private TripCriteria tripCriteria;

    public TripPlannerController() throws IOException {
        stops = new HashMap<>();
        routes = new HashMap<>();
        trips = new HashMap<>();
        stopCriteria = new StopCriteria();
        tripCriteria = new TripCriteria();
        this.stopsList = new ArrayList<>();
        this.tripsList = new ArrayList<>();
        plan = new Plan();
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

    public Plan getPlanForTrip(String departureString, String destinationString, String dateString, String departureTimeString) {
        LocalTime departureTime = LocalTime.parse(departureTimeString, DateTimeFormatter.ofPattern("HH:mm"));
        LocalDate date = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        int dayOfWeek = date.getDayOfWeek().getValue();
        plan.setDestinationStop(findStop(destinationString));
        plan.setDepartureStop(findStop(departureString));
        plan.setDepartureTime(LocalDateTime.of(date, departureTime));
        //all trip ids, that have both of these stations in right order and are active on the right week day
        List<Long> tripsLoc = tripCriteria.tripsContainingStations(tripsList, plan.getDepartureStop(), plan.getDestinationStop(), dayOfWeek);
        List<Long> tripsAfterDepTime = getTripsAvailableAfterTime(tripsLoc, plan.getDepartureStop(), departureTime);

        if(tripsAfterDepTime.size() < 1){
            getNotDirectRoute();
            return plan;
        }

        Long selectedTripId = tripsAfterDepTime.get(0);

        //tripsAfterDepTime will contain all direct trips ordered by departureTime so get(0) will give the first departure
        List<LocalTime> times = calculateTimes(selectedTripId, plan.getDepartureStop(), plan.getDestinationStop());
        Route selectedRoute = routes.get(trips.get(selectedTripId).getRouteId());
        ArrayList<TravelLeg> legs = new ArrayList<>();
        TravelLeg leg = new TravelLeg();
        leg.setRoute(selectedRoute.getId());
        leg.setTravelType(TravelType.PUBLIC_TRANSPORT);
        leg.setSource(plan.getDepartureStop().getId());
        leg.setDestination(plan.getDestinationStop().getId());
        legs.add(leg);
        plan.setTravelLegs(legs);
        plan.setApproxArrivalTime(plan.getDepartureTime().plusMinutes(minutesToDestStop(leg, selectedTripId, plan.getDepartureTime())));
        //temporary solution for part a
        //System.out.println("The next "+selectedRoute.getType()+" "+selectedRoute.getShortName()
        //        +" is leaving at "+times.get(0)+" and arriving at "+times.get(times.size()-1));
        return plan;
    }

    private void getNotDirectRoute(){
        List<Long> sortedTripIds = tripCriteria.tripsContainingDepStation(tripsList, plan.getDepartureStop(), plan.getDepartureTime());
        List<PublicTransportStop> allDirectlyAccStops = new ArrayList<>();
        for(Long tripId:sortedTripIds){
            allDirectlyAccStops.addAll(trips.get(tripId).getStops().stream().filter(astop -> astop.getTimetable().getTimesMap().get(tripId).isAfter(plan.getDepartureTime().toLocalTime())).collect(Collectors.toList()));
        }
        PublicTransportStop closestStop = closestStopToDestination(allDirectlyAccStops);
        List<Long> lTrips = tripCriteria.sortedTripsContainingStops(tripsList, plan.getDestinationStop(), closestStopToDestination(allDirectlyAccStops), plan.getDepartureTime());
        List<TravelLeg> travelLegs = new ArrayList<>();
        TravelLeg travelLeg = new TravelLeg();
        travelLeg.setDestination(closestStop.getId());
        travelLeg.setSource(plan.getDepartureStop().getId());
        travelLeg.setTravelType(TravelType.PUBLIC_TRANSPORT);
        travelLeg.setRoute(trips.get(lTrips.get(0)).getRouteId());
        travelLegs.add(travelLeg);
        long addMinutes = minutesToDestStop(travelLeg, lTrips.get(0), plan.getDepartureTime());
        TravelLeg walkToDest = new TravelLeg();
        walkToDest.setDestination(plan.getDestinationStop().getId());
        walkToDest.setSource(closestStop.getId());
        walkToDest.setTravelType(TravelType.WALK);
        walkToDest.setRoute("Walking from "+closestStop.getStopName()+" to "+plan.getDestinationStop().getStopName());
        travelLegs.add(walkToDest);
        addMinutes += distBetweenTwoStopsWalkingMin(stops.get(closestStop.getId()), stops.get(plan.getDestinationStop().getId()));
        plan.setTravelLegs(travelLegs);
        plan.setApproxArrivalTime(plan.getDepartureTime().plusMinutes(addMinutes));
    }

    private long distBetweenTwoStopsWalkingMin(PublicTransportStop fromStop, PublicTransportStop toStop){
        return Math.round(distBetweenTwoStops(fromStop, toStop)*110*10);
    }

    private double distBetweenTwoStops(PublicTransportStop fromStop, PublicTransportStop toStop){
        return Math.abs(Math.sqrt(Math.pow(fromStop.getLongitude()-toStop.getLongitude(), 2.0) + Math.pow(fromStop.getLatitude()-toStop.getLatitude(), 2.0)));
    }

    private PublicTransportStop closestStopToDestination(List<PublicTransportStop> allDirectlyAccStops) {
        PublicTransportStop closestSoFar = null;
        Double minDistSoFar = null;
        for(PublicTransportStop publicTransportStop: allDirectlyAccStops){
            Double dist = distBetweenTwoStops(plan.getDestinationStop(), publicTransportStop);
            if(minDistSoFar == null || dist < minDistSoFar){
                minDistSoFar = dist;
                closestSoFar = publicTransportStop;
            }
        }
        return closestSoFar;
    }

    private Long findClosestStopToCoordinates(Double xCoord, Double yCoord){
        Long closestSoFar = null;
        Double minSoFar = null;
        PublicTransportStop result = stopCriteria.getPublicTransportStopByCoordinates(stopsList, xCoord, yCoord);
        if(result!=null){
            return result.getId();
        }
        for(PublicTransportStop stop: stopsList){
            Double dist = Math.sqrt(Math.pow(stop.getLongitude()-xCoord, 2.0) + Math.pow(stop.getLatitude()-yCoord, 2.0));
            if(minSoFar == null || dist < minSoFar){
                minSoFar = dist;
                closestSoFar = stop.getId();
            }
        }
        return closestSoFar;
    }

    private List<Long> getTripsAvailableAfterTime(List<Long> tripIds, PublicTransportStop depStation, LocalTime time){
        Map<Long, LocalTime> timesMap = depStation.getTimetable().getTimesMap();

        //which ones leave after the time we want and go directly to arrival station
        Map<Long, LocalTime> tripsLeavingAfter = timesMap.entrySet()
                .stream().filter(p -> (p.getValue().isAfter(time)) && tripIds.contains(p.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        //sort the trips by time
        List<Long> sortedTrips = tripsLeavingAfter.entrySet().stream()
                .sorted(Comparator.comparing(Map.Entry::getValue)).map(Map.Entry::getKey)
                .collect(Collectors.toList());
        return sortedTrips;
    }

    private List<LocalTime> calculateTimes(Long tripId, PublicTransportStop depStation, PublicTransportStop arrStation){
        List<LocalTime> times = new ArrayList<>();
        PublicTransportStop actualStop = depStation;
        while (! actualStop.equals(arrStation)) {
            times.add(actualStop.getTimetable().getTime(tripId));
            actualStop = actualStop.getTimetable().getInfoMap().get(tripId).getNextStop();
        }
        return times;
    }

    private long minutesToDestStop(TravelLeg travelLeg, Long tripId, LocalDateTime timeNoW){
        List<LocalTime> times = new ArrayList<>();
        PublicTransportStop actualStop = stops.get(travelLeg.getDestination());
        while (! actualStop.equals(stops.get(travelLeg.getDestination()))) {
            times.add(actualStop.getTimetable().getTime(tripId));
            actualStop = actualStop.getTimetable().getInfoMap().get(tripId).getNextStop();
        }
        if(times.size()<2){
            System.out.println("ERROR minutesToDestStop should have contained times");
            return 0;
        }
        return Duration.between(timeNoW.toLocalTime(), times.get(times.size()-1)).toMinutes();
    }

    // INITIALIZATION METHODS FOR READING FROM FILES

    private void setStopsInfo() {
        for (Trip trip: trips.values()) {
            List<PublicTransportStop> stopsList = trip.getStops();
            for (int i = 1; i < stopsList.size() -1; i++) {
                PublicTransportStop actualStop = stopsList.get(i-1);
                PublicTransportStop nextStop = stopsList.get(i);
                TravelInfo info = new TravelInfo(actualStop, nextStop, trip.getTripId());

                actualStop.getTimetable().addInfo(trip.getTripId(), info);
            }
            PublicTransportStop lastStop = trip.getStops().getLast();
            TravelInfo info = new TravelInfo(lastStop, null, null);
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
                timetable.addTrip(tripId); //05:39:00
                if(depTime.startsWith("24")){
                    depTime = "00"+depTime.substring(2);
                }
                if(depTime.startsWith("25")){
                    depTime = "01"+depTime.substring(2);
                }
                if(depTime.startsWith("26")){
                    depTime = "02"+depTime.substring(2);
                }
                timetable.addTime(tripId, LocalTime.parse(depTime, DateTimeFormatter.ofPattern("HH:mm:ss")));
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
                stopsList.add(stop);
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
                trip.setRouteId(routeId);
                route.addTrip(trip.getTripId());
                trip.setServiceId(Long.parseLong(tokenizer.nextToken()));
                trip.setTripId(Long.parseLong(tokenizer.nextToken()));
                trip.setDirectionCode(tokenizer.nextToken());
                trips.put(trip.getTripId(), trip);
                tripsList.add(trip);
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
