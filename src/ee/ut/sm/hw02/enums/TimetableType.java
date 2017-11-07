package ee.ut.sm.hw02.enums;

public enum TimetableType {
    WORKDAYS,
    SATURDAY,
    SUNDAY_AND_FESTAL_DAYS;

    public static RouteType parseRouteType(int type) {
        switch (type) {
            case 0: return RouteType.TRAM_ROUTE;
            case 2: return RouteType.RAIL_ROUTE;
            case 3: return RouteType.BUS_ROUTE;
            case 4: return RouteType.FERRY_ROUTE;
            case 800: return RouteType.TROLLEY_ROUTE;
        }
        return null;
    }
}


