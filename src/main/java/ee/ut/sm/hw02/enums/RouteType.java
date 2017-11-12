package ee.ut.sm.hw02.enums;

public enum RouteType {
    BUS_ROUTE{
        @Override
        public String toString() {
            return "bus";
        }
    },
    TRAM_ROUTE{
        @Override
        public String toString() {
            return "tram";
        }
    },
    TROLLEY_ROUTE{
        @Override
        public String toString() {
            return "trolley";
        }
    },
    FERRY_ROUTE{
        @Override
        public String toString() {
            return "ferry";
        }
    },
    RAIL_ROUTE{
        @Override
        public String toString() {
            return "train";
        }
    };

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
