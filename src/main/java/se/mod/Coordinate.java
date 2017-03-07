package se.mod;

import java.util.Objects;

/**
 * Created by mod on 2017-03-07.
 */
public class Coordinate {

  private final double longitude;
  private final double latitude;

  @Override
  public int hashCode() {
    return Objects.hash(longitude, latitude);
  }

  public Coordinate(double longitude, double latitude) {

    this.longitude = longitude;
    this.latitude = latitude;
  }

  public double getLatitude() {
    return latitude;
  }

  public double getLongitude() {
    return longitude;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Coordinate that = (Coordinate) o;
    return Double.compare(that.longitude, longitude) == 0 &&
        Double.compare(that.latitude, latitude) == 0;
  }

}
