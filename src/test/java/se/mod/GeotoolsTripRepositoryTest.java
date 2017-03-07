package se.mod;

import java.util.Collection;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by mod on 2017-03-07.
 */
public class GeotoolsTripRepositoryTest {

  private static final String TEST_VIN = "TESTVIN";

  TripRepository tripRepository;

  @Before
  public void setupRepository() throws Exception {
    tripRepository = new GeotoolsTripRepository();
  }

  @Test
  public void saveTrip_NoExceptions() {

    Trip trip = createTestTrip();
    tripRepository.saveTrip(trip);

  }

  @Test
  public void getTripForVIN_OneTripPresent() {

    Trip trip = createTestTripForVIN(TEST_VIN);
    tripRepository.saveTrip(trip);

    Collection<Trip> tripsForVIN = tripRepository.getTripsForVIN(TEST_VIN);
    Assert.assertEquals(1, tripsForVIN.size());

    Trip repositoryTrip = tripsForVIN.iterator().next();

    Assert.assertEquals(trip.getVin(), repositoryTrip.getVin());

    Assert.assertEquals(trip.getStartCoordinate(), repositoryTrip.getStartCoordinate());
    Assert.assertEquals(trip.getStopCoordinate(), repositoryTrip.getStopCoordinate());

    Assert.assertEquals(trip.getPassengerCount(), repositoryTrip.getPassengerCount());

  }

  private Trip createTestTrip() {
    return createTestTripForVIN(TEST_VIN);
  }

  private Trip createTestTripForVIN(String testVin) {
    Coordinate startCoordinate = new Coordinate(11.832275, 57.759502);
    Coordinate stopCoordinate = new Coordinate(11.898193, 58.283147);
    int passengerCount = 1;
    return new Trip(TEST_VIN, startCoordinate, stopCoordinate, passengerCount);
  }

}