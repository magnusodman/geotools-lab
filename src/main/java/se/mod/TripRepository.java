package se.mod;


import java.io.IOException;
import java.util.Collection;

public interface TripRepository {

  void saveTrip(Trip trip) throws IOException;

  Collection<Trip> getTripsForVIN(String vin) throws IOException;
}
