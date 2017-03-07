package se.mod;


import java.util.Collection;

public interface TripRepository {

  void saveTrip(Trip trip);

  Collection<Trip> getTripsForVIN(String vin);
}
