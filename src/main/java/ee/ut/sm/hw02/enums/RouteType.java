package ee.ut.sm.hw02.enums;

public enum RouteType {
    BUS_ROUTE,
    TRAM_ROUTE,
    TROLLEY_ROUTE,
    FERRY_ROUTE,
    RAIL_ROUTE;

    public static RouteType parseType(int type) {
        switch (type) {
            case 0: return TRAM_ROUTE;
            case 2: return RAIL_ROUTE;
            case 3: return BUS_ROUTE;
            case 4: return FERRY_ROUTE;
            case 800: return TROLLEY_ROUTE;
        }
        return null;
    }
}
