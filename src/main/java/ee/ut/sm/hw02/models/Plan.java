package ee.ut.sm.hw02.models; /**
 * @(#) Plan.java
 */

import java.util.Date;
import java.util.List;

public class Plan {
	private Date departureTime;
    private PublicTransportStop departureStop;
    private PublicTransportStop destinationStop;

    private List<PublicTransportStop> stopsOnPath;
    private List<TravelLeg> travelLegs;

}
