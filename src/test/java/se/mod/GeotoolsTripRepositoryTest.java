package se.mod;

import java.util.Collection;
import java.util.Date;
import org.geotools.data.DataStore;
import org.geotools.data.DataUtilities;
import org.geotools.data.memory.MemoryDataStore;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * Created by mod on 2017-03-07.
 */
public class GeotoolsTripRepositoryTest {

  private static final String TEST_VIN = "TESTVIN";

  private TripRepository tripRepository;

  @Before
  public void setupRepository() throws Exception {
    DataStore datastore = new MemoryDataStore();

    SimpleFeatureType simpleFeatureType = DataUtilities.createType("Trip",
        GeotoolsTripRepository.TRIP_TYPE_SPEC);
    datastore.createSchema(simpleFeatureType);


    tripRepository = new GeotoolsTripRepository(datastore);

  }

  @Test
  public void saveTrip_NoExceptions() {

    Trip trip = createTestTrip();
    tripRepository.saveTrip(trip);

  }

  @Test
  public void getTripForVIN_OneTripPresent() throws Exception {

    Trip trip = createTestTripForVIN(TEST_VIN);
    tripRepository.saveTrip(trip);

    Collection<Trip> tripsForVIN = tripRepository.getTripsForVIN(TEST_VIN);
    Assert.assertEquals(1, tripsForVIN.size());

    Trip repositoryTrip = tripsForVIN.iterator().next();

    Assert.assertEquals(trip.getVin(), repositoryTrip.getVin());

    Assert.assertEquals(trip.getStartCoordinate(), repositoryTrip.getStartCoordinate());
    Assert.assertEquals(trip.getStopCoordinate(), repositoryTrip.getStopCoordinate());

    Assert.assertEquals(trip.getPassengerCount(), repositoryTrip.getPassengerCount());

    Assert.assertEquals(trip.getLogDate(), repositoryTrip.getLogDate());
  }

  @Test
  public void getTripForVIN_DifferOnVIN_NoResult() throws Exception {

    Trip trip = createTestTrip();
    tripRepository.saveTrip(trip);

    Collection<Trip> tripsForVIN = tripRepository.getTripsForVIN("OTHERVIN");
    Assert.assertEquals(0, tripsForVIN.size());

  }

  private Trip createTestTrip() {
    return createTestTripForVIN(TEST_VIN);
  }

  private Trip createTestTripForVIN(String testVin) {
    Coordinate startCoordinate = new Coordinate(11.832275, 57.759502);
    Coordinate stopCoordinate = new Coordinate(11.898193, 58.283147);
    int passengerCount = 1;
    return new Trip(TEST_VIN, startCoordinate, stopCoordinate, passengerCount, new Date());
  }

}