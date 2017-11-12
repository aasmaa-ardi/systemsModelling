package ee.ut.sm.hw02;

import ee.ut.sm.hw02.enums.RouteType;
import ee.ut.sm.hw02.helpers.TimeHelper;
import ee.ut.sm.hw02.models.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.Date;
import java.util.stream.Collectors;

public class TripPlannerController {
    
    private Map<Long, PublicTransportStop> stops;
    private Map<Long, Trip> trips;
    private Map<String, Route> routes;
    //private Map<String, PublicTransportStop> stops2;
    private List<PublicTransportStop> stopsList;
    private List<Trip> tripsList;

    public List<PublicTransportStop> getStopsList() {
        return stopsList;
    }

    public void setStopsList(List<PublicTransportStop> stopsList) {
        this.stopsList = stopsList;
    }

    public List<Trip> getTripsList() {
        return tripsList;
    }

    public void setTripsList(List<Trip> tripsList) {
        this.tripsList = tripsList;
    }

    public TripPlannerController() throws IOException {
        stops = new HashMap<>();
        routes = new HashMap<>();
        trips = new HashMap<>();
        this.stopsList = new ArrayList<>();
        this.tripsList = new ArrayList<>();
        if (! (loadStops() && loadRoutes() && loadTrips() && loadStopTimes() && loadCalendar())) {
            throw new IOException("Error while loading files");
        }
        setStopsInfo();

        System.out.println("All data loaded...");
    }

    //public boolean doesStopExist(String stopName) {
    //    return stops2.containsKey(stopName.toUpperCase());
    //}

    public Plan getPlanForTrip(Long departure, Long destination, Date departureTime) {

        PublicTransportStop departureStop = stops.get(departure);
        PublicTransportStop destinationStop = stops.get(destination);



        //vertices = stops
        //edges = trip from stop to stop -> uses stop.get

        return null;
    }

    public List<Long> getTripsAvailableAfterTime(List<Long> tripIds, PublicTransportStop depStation, LocalTime time){
        Map<Long, LocalTime> timesMap = depStation.getTimetable().getTimesMap();

        //which ones leave after the time we want and go directly to arrival station
        Map<Long, LocalTime> tripsLeavingAfter = timesMap.entrySet()
                .stream().filter(p -> (p.getValue().isAfter(time)) && tripIds.contains(p.getKey()))
                .collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));

        //System.out.println(tripsLeavingAfter);
        //sort the trips by time
        List<Long> sortedTrips = tripsLeavingAfter.entrySet().stream()
                .sorted(Comparator.comparing(Map.Entry::getValue)).map(Map.Entry::getKey)
                .collect(Collectors.toList());
        System.out.println(tripsLeavingAfter.get(sortedTrips.get(0))+" "+tripsLeavingAfter.get(sortedTrips.get(10)));
        return sortedTrips;
    }

    public List<LocalTime> calculateTimes(Long tripId, PublicTransportStop depStation, PublicTransportStop arrStation){
        Trip trip = trips.get(tripId);
        List<Long> stopsLoc = trip.getStops();
        stopsLoc = stopsLoc.subList(stopsLoc.indexOf(depStation.getId()), stopsLoc.indexOf(arrStation.getId()));
        PublicTransportStop nextStop = stops.get(stopsLoc.get(0));

        return null;
    }

    // INITIALIZATION METHODS FOR READING FROM FILES

    private void setStopsInfo() {
        for (Trip trip: trips.values()) {
            List<Long> stopsList = trip.getStops();
            for (int i = 1; i < stopsList.size() -1; i++) {
                PublicTransportStop actualStop = stops.get(stopsList.get(i-1));
                PublicTransportStop nextStop = stops.get(stopsList.get(i));
                TravelInfo info = new TravelInfo(actualStop, nextStop, trip.getTripId());

                actualStop.getTimetable().addInfo(trip.getTripId(), info);
            }
            PublicTransportStop lastStop = stops.get(trip.getStops().getLast());
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
                trip.getStops().addLast(stop.getId());
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
                //stops2.put(stop.getStopName().toUpperCase(), stop);
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
                /*System.out.println(trip.toString());*/
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
