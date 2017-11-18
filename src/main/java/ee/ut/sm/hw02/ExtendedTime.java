package ee.ut.sm.hw02;

import java.time.LocalDate;

public class ExtendedTime {
    public static final ExtendedTime MAX = new ExtendedTime(Long.MAX_VALUE);

    private long seconds;

    public ExtendedTime() { }

    public ExtendedTime(long seconds) {
        this.seconds = seconds;
    }

    public ExtendedTime(String time) {
        computeSeconds(time);
    }

    private void computeSeconds(String time) {
        String[] parts = time.split(":");
        long seconds = 0;
        long timePart = 1;
        for (int i = parts.length - 1; i >= 0; i--) {
            String part = parts[i];
            if (part.startsWith("0")) {
                part = part.substring(1, 2);
            }
            Long val = Long.parseLong(part);
            seconds += timePart * val;
            timePart *= 60;
        }
        this.seconds = seconds;
    }

    public ExtendedTime addTime(ExtendedTime timeToAdd) {
        long secsToAdd = timeToAdd.getSeconds();
        ExtendedTime resultTime = new ExtendedTime();
        resultTime.setSeconds(this.seconds + secsToAdd);

        return resultTime;
    }

    public long getSeconds() {
        return seconds;
    }

    public void setSeconds(long seconds) {
        this.seconds = seconds;
    }

    public boolean isBefore(ExtendedTime toCompareWith) {
        long secs = toCompareWith.getSeconds();
        long res = seconds - secs;
        return res < 0;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        long seconds = this.seconds;
        long hours = seconds / 3600;
        seconds -= hours * 3600;
        long minutes = seconds / 60;
        seconds -= minutes * 60;

        if (hours < 10) {
            builder.append("0");
        }
        builder.append(hours);
        builder.append(":");
        if (minutes < 10) {
            builder.append("0");
        }
        builder.append(minutes);
        builder.append(":");
        if (seconds < 10) {
            builder.append("0");
        }
        builder.append(seconds);

        return builder.toString();
    }

    public boolean isValid() {
        boolean valid = true;
        if (! this.toString().matches("([01]?[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]")) {
            valid = false;
        }
        return valid;
    }
}
