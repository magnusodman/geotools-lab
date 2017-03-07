package se.mod;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by mod on 2017-03-07.
 */
public class TripTest {

  private final String TEST_VIN = "TESTVIN";
  @Test
  public void newTripInstance() {

    Coordinate startCoordinate = new Coordinate(11.832275, 57.759502);
    Coordinate stopCoordinate = new Coordinate(11.898193, 58.283147);
    int passengerCount = 1;
    new Trip(TEST_VIN, startCoordinate, stopCoordinate, passengerCount);
  }


  @Test
  public void newTripInstance_CheckValues() {

    Coordinate startCoordinate = new Coordinate(11.832275, 57.759502);
    Coordinate stopCoordinate = new Coordinate(11.898193, 58.283147);
    int passengerCount = 1;
    Trip trip = new Trip(TEST_VIN, startCoordinate, stopCoordinate, passengerCount);

    Assert.assertEquals(passengerCount, trip.getPassengerCount());
    Assert.assertEquals(startCoordinate, trip.getStartCoordinate());
    Assert.assertEquals(stopCoordinate, trip.getStopCoordinate());
  }




}
