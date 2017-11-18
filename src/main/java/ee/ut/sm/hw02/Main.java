package ee.ut.sm.hw02;

import ee.ut.sm.hw02.models.Plan;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Main {

    public static void main(String[] args) throws ParseException {

        if(isInputValid(args)) {
            //generate plan
            TripPlannerController controller = null;
            try {
                controller = new TripPlannerController();
            } catch (Exception e) {
                System.exit(1);
            }
            args[3] += ":00";
            Plan tripPlan = controller.getPlanForTrip(args[0], args[1], args[2], args[3]);

            if (tripPlan != null) {
                System.out.println(tripPlan.toString());
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
        }
        return isValid;
    }
}
