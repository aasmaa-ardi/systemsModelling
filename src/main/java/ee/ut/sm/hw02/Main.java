package ee.ut.sm.hw02;

import ee.ut.sm.hw02.filters.StopCriteria;
import ee.ut.sm.hw02.filters.TripCriteria;
import ee.ut.sm.hw02.models.Plan;
import ee.ut.sm.hw02.models.PublicTransportStop;
import ee.ut.sm.hw02.models.Trip;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) throws ParseException {
        //get user input
        try {
            Class.forName("org.hsqldb.jdbcDriver").newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }

        for(String arg:args){
            System.out.println(arg);
        }

        if(isInputValid(args)) {
            //generate plan
            LocalDate date = LocalDate.parse(args[2], DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            Plan tripPlan = null;
            StopCriteria stopCriteria = new StopCriteria();

            try {
                TripPlannerController controller = new TripPlannerController();

                PublicTransportStop departureStop = stopCriteria.getPublicTransportStopById(controller.getStopsList(), Long.valueOf(args[0]));
                PublicTransportStop arrivalStop = stopCriteria.getPublicTransportStopById(controller.getStopsList(), Long.valueOf(args[1]));

                LocalTime requestedTime = LocalTime.parse(args[3], DateTimeFormatter.ofPattern("HH:mm"));
                tripPlan = controller.getPlanForTrip(departureStop.getId(), arrivalStop.getId(), date, requestedTime);
            } catch (Exception e) {
                System.out.println(e);
                System.err.println("DB consistency problem occurred, terminating");
                System.exit(1);
            }

            //return result
            if (tripPlan != null) {
                //TODO print plan of trip to user
            } else {
                System.out.println("No plan can be generated for specified input. :(");
            }
        }
    }

    private static boolean isInputValid(String[] args){
        boolean isValid = true;
        if (args.length != 4) {
           System.out.println("Please use 4 arguments as input for the program: departure stop, arrival stop, date and time.");
           return false;
        }
        try {
            LocalDate date = LocalDate.parse(args[2], DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (DateTimeParseException ex) {
            System.out.println("Argument \"date\" value \""+ args[2] + "\" cannot be parsed as a date. Please use format: yyyy-MM-dd.");
            isValid = false;
        }
        if(!args[3].matches("([01]?[0-9]|2[0-3]):[0-5][0-9]")){
            System.out.println("Argument \"time\" value \""+ args[3] + "\" cannot be parsed as time. Please use format: hh:mm.");
            isValid = false;
        };
        return isValid;
    }
}
