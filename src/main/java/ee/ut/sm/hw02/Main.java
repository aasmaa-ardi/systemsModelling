package ee.ut.sm.hw02;

import ee.ut.sm.hw02.models.Plan;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws ParseException {
        //get user input
        try {
            Class.forName("org.hsqldb.jdbcDriver").newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
        /*
        Scanner reader = new Scanner(System.in);
        System.out.println("Enter ID of departure stop:");
        String line = reader.nextLine();
        Long departureId = Long.parseLong(line);
        System.out.println("Enter ID of destination stop:");
        line = reader.nextLine();
        Long destinationId = Long.parseLong(line);
        System.out.println("Enter the departure date (format DD/MM/YYYY)");
        line = reader.nextLine();
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date date = format.parse(line);
        System.out.println("Enter the departure time (format HH:MM)");
        format = new SimpleDateFormat("HH:mm");
        line = reader.next();
        Date departureTime = format.parse(line);
        long time = departureTime.getTime();
        date.setTime(time);
        */

        //generate plan
        Plan tripPlan = null;
        try {
            TripPlannerController controller = new TripPlannerController();
            tripPlan = controller.getPlanForTrip(null, null, null);
        } catch (Exception e) {
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
