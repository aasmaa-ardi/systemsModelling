package ee.ut.sm.hw02;

import ee.ut.sm.hw02.enums.RouteType;
import ee.ut.sm.hw02.models.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.util.*;
import java.util.Date;

public class TripPlannerController {
    
    private Map<Long, PublicTransportStop> stops;
    private Map<Long, Trip> trips;
    private Map<String, Route> routes;

    public TripPlannerController() throws IOException {
        stops = new HashMap<>();
        routes = new HashMap<>();
        trips = new HashMap<>();
        if (! (loadStops() && loadRoutes() && loadTrips() && loadStopTimes() && loadCalendar())) {
            throw new IOException("Error while loading files");
        }
        setStopsInfo();

        System.out.println("All data loaded...");
    }

    public Plan getPlanForTrip(Long departure, Long destination, Date departureTime) {

        PublicTransportStop departureStop = stops.get(departure);
        PublicTransportStop destinationStop = stops.get(departure);



        //vertices = stops
        //edges = trip from stop to stop -> uses stop.get

        return null;
    }

    // INITIALIZATION METHODS FOR READING FROM FILES

    private void setStopsInfo() {
        for (Trip trip: trips.values()) {
            List<PublicTransportStop> stops = trip.getStops();
            for (int i = 1; i < stops.size() -1; i++) {
                PublicTransportStop prevStop = stops.get(i-1);
                PublicTransportStop currentStop = stops.get(i);
                Date currentStopTime = currentStop.getTimetable().getTime(trip.getTripId());
                Date prevStopTime = prevStop.getTimetable().getTime(trip.getTripId());
                TravelInfo info = new TravelInfo();

                info.setNextStop(currentStop);
                long travelTime = Math.abs(currentStopTime.getTime() - prevStopTime.getTime());
                info.setTravelTime(TimeHelper.getDate(travelTime));
                prevStop.getTimetable().addInfo(trip.getTripId(), info);
            }
            PublicTransportStop lastStop = trip.getStops().getLast();
            TravelInfo info = new TravelInfo();
            info.setNextStop(null);
            info.setTravelTime(null);
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
                trip.getStops().addLast(stop);
                timetable.addTrip(trip);
                timetable.addTime(tripId, TimeHelper.parseDate(depTime));
            }
        } catch (IOException | ParseException e) {
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
                route.addTrip(trip);
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
