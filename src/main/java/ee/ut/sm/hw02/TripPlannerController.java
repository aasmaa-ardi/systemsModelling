package ee.ut.sm.hw02;

import ee.ut.sm.hw02.enums.RouteType;
import ee.ut.sm.hw02.filters.StopCriteria;
import ee.ut.sm.hw02.filters.TripCriteria;
import ee.ut.sm.hw02.models.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
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
                result = stopCriteria.getPublicTransportStopById(stopsList, Long.valueOf(param.substring(2)));
            } else if (param.startsWith("X") && param.contains("Y")) {
                String[] parts = param.substring(1).split("Y");
                result = stopCriteria.getPublicTransportStopByCoordinates(stopsList, Double.valueOf(parts[0]), Double.valueOf(parts[1]));
            } else {
                //aadress or name?
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

        PublicTransportStop departureStop = findStop(departureString);

        //if there is no exact match find closest station
        if(departureStop==null && departureString.startsWith("X")){
            String[] coords = departureString.substring(1).split("Y");
            Long id = findClosestStopToCoordinates(Double.valueOf(coords[0]), Double.valueOf(coords[1]));
            departureStop = stops.get(id);
            //first leg walking from input coordinates to closest stop
        }

        PublicTransportStop destinationStop = findStop(destinationString);

        //all trip ids, that have both of these stations in right order and are active on the right week day
        List<Long> tripsLoc = tripCriteria.tripsContainingStations(tripsList, departureStop, destinationStop, dayOfWeek);

        List<Long> tripsAfterDepTime = getTripsAvailableAfterTime(tripsLoc, departureStop, departureTime);

        System.out.println(tripsAfterDepTime.size());

        if(tripsAfterDepTime.size() < 1){
            //PART A would return null there, because there will be no direct trips
            System.out.println("There are no direct trips from departure stop to arrival stop that are available on requested time.");
            return null;
        }

        Long selectedTripId = tripsAfterDepTime.get(0);
        //tripsAfterDepTime will contain all direct trips ordered by departureTime so get(0) will give the first departure
        List<LocalTime> times = calculateTimes(selectedTripId, departureStop, destinationStop);
        Route selectedRoute = routes.get(trips.get(selectedTripId).getRouteId());

        //temporary solution for part a
        System.out.println("The next "+selectedRoute.getType()+" "+selectedRoute.getShortName()
                +" is leaving at "+times.get(0)+" and arriving at "+times.get(times.size()-1));
        return null;
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

        //System.out.println(tripsLeavingAfter);
        //sort the trips by time
        List<Long> sortedTrips = tripsLeavingAfter.entrySet().stream()
                .sorted(Comparator.comparing(Map.Entry::getValue)).map(Map.Entry::getKey)
                .collect(Collectors.toList());
        System.out.println(tripsLeavingAfter.get(sortedTrips.get(0))+" "+tripsLeavingAfter.get(sortedTrips.get(10)));
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
