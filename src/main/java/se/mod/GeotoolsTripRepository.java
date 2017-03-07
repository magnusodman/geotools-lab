package se.mod;

import com.google.common.base.Strings;
import com.vividsolutions.jts.geom.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import org.geotools.data.DataUtilities;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

public class GeotoolsTripRepository implements TripRepository {

  final SimpleFeatureType TRIP_TYPE;
  List<SimpleFeature> features = new ArrayList<>();
  GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
  final SimpleFeatureBuilder featureBuilder;



  public GeotoolsTripRepository() throws SchemaException {

    TRIP_TYPE = DataUtilities.createType("Trip",
        "start_point:Point:srid=4326," + // <- the geometry attribute: Point type
            "stop_point:Point:srid=4326," + //
            "vin:String," +   // <- a String attribute
            "passenger_count:Integer"   // a number attribute
    );

     featureBuilder = new SimpleFeatureBuilder(TRIP_TYPE);
  }

  @Override
  public void saveTrip(Trip trip) {

    double startLatitude = trip.getStartCoordinate().getLatitude();
    double startLongitude = trip.getStartCoordinate().getLongitude();
    double stopLatitude = trip.getStopCoordinate().getLatitude();
    double stopLongitude = trip.getStopCoordinate().getLongitude();
    String vin = trip.getVin();
    int passengerCount = trip.getPassengerCount();

    /* Longitude (= x coord) first ! */
    Point startPoint = geometryFactory.createPoint(new com.vividsolutions.jts.geom.Coordinate(startLongitude, startLatitude));
    Point stopPoint = geometryFactory.createPoint(new com.vividsolutions.jts.geom.Coordinate(stopLongitude, stopLatitude));

    featureBuilder.add(startPoint);
    featureBuilder.add(stopPoint);
    featureBuilder.add(vin);
    featureBuilder.add(passengerCount);
    String id = UUID.randomUUID().toString();
    SimpleFeature feature = featureBuilder.buildFeature(id);
    features.add(feature);

  }

  @Override
  public Collection<Trip> getTripsForVIN(String searchVin) {
    List<Trip> tripsForVIN = new ArrayList<>();
    for(SimpleFeature feature: features) {
      String vin = feature.getAttribute("vin").toString();
      if(searchVin.equals(vin)) {
        Point startPoint = (Point) feature.getAttribute("start_point");
        Coordinate startCoordinate = new Coordinate(startPoint.getX(), startPoint.getY());
        Point stopPoint = (Point) feature.getAttribute("stop_point");
        Coordinate stopCoordinate = new Coordinate(stopPoint.getX(), stopPoint.getY());
        int passengerCount = (int) feature.getAttribute("passenger_count");

        Trip trip = new Trip(vin, startCoordinate, stopCoordinate, passengerCount);
        tripsForVIN.add(trip);
      }
    }
    return tripsForVIN;
  }
}
