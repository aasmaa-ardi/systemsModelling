package ee.ut.sm.hw02.helpers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeHelper {

    public static Date parseDate(String date) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        return dateFormat.parse(date);
    }

    public static Date getDate(long date) {
        Date res = new Date();
        res.setTime(date);
        return res;
    }
}
