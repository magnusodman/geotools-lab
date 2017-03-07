package se.mod;

import java.util.Date;

/**
 * Created by mod on 2017-03-07.
 */
public class Trip {

  private final Date logDate;
  private final String vin;
  private final Coordinate startCoordinate;
  private final Coordinate stopCoordinate;
  private final int passengerCount;

  public Trip(String vin, Coordinate startCoordinate, Coordinate stopCoordinate, int passengerCount, Date logDate) {
    this.vin = vin;
    this.startCoordinate = startCoordinate;
    this.stopCoordinate = stopCoordinate;
    this.passengerCount = passengerCount;
    this.logDate = logDate;
  }

  public Coordinate getStartCoordinate() {
    return startCoordinate;
  }

  public int getPassengerCount() {
    return passengerCount;
  }

  public Coordinate getStopCoordinate() {
    return stopCoordinate;
  }

  public String getVin() {
    return vin;
  }

  public Date getLogDate() {
    return logDate;
  }
}
