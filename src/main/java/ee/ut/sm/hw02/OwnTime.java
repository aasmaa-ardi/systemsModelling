package ee.ut.sm.hw02;

public class OwnTime {
    public static final OwnTime MAX = new OwnTime("48:59:00");

    private String time;

    public OwnTime(String time) {
        if (time.matches("([01]?[0-9]|2[0-6]|48):[0-5][0-9]:?[0-5]?[0-9]?")) {
            this.time = time;
        } else {
            throw new IllegalArgumentException("Time is in wrong format");
        }
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
