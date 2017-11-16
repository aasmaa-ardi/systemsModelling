package ee.ut.sm.hw02;

public class OwnTime {
    public static final OwnTime MAX = new OwnTime("48:59:00");

    private String time;

    public OwnTime(String time) {
        if (time.matches("([01]?[0-9]|2[0-6]|48):[0-5][0-9]:?[0-5]?[0-9]?")) {
            this.time = time;
        } else {
            throw new IllegalArgumentException("Time is in wrong format "+time);
        }
    }

    public String addMinutes(int addedMin) {
        String[] parts = time.split(":");
        int a = 3600;
        int seconds = addedMin * 60;
        for(String part:parts){
            seconds += Integer.valueOf(part) * a;
            a = a / 60;
        }
        int hours = seconds/3600;
        seconds -= hours * 3600;
        int minutes = seconds/60;
        seconds -= minutes * 60;
        String minuteString = minutes < 10 ? "0"+minutes : String.valueOf(minutes);
        String hoursString = hours < 10 ? "0"+hours : String.valueOf(hours);
        String secondsString = seconds < 10 ? "0"+seconds : String.valueOf(seconds);
        return hoursString+":"+minuteString+":"+secondsString;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isBefore(OwnTime toCompareWith) {
        String toCompareWithStr = toCompareWith.getTime();
        int res = time.compareTo(toCompareWithStr);
        return res < 0;
    }

    @Override
    public String toString() {
        return time;
    }
}
