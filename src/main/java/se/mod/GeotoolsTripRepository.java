package se.mod;

import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.geotools.data.DataStore;
import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.FeatureStore;
import org.geotools.data.Transaction;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GeotoolsTripRepository implements TripRepository {

  private final static Logger logger = LoggerFactory.getLogger(GeotoolsTripRepository.class);

  public static final String TRIP_TYPE_NAME = "Trip";
  public static final String TRIP_TYPE_SPEC = "start_point:Point:srid=4326,stop_point:Point:srid=4326,vin:String,passenger_count:Integer,log_date:Date";
  final SimpleFeatureType TRIP_TYPE;
  private final SimpleFeatureSource featureSource;

  GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
  final private DataStore dataStore;
  final private SimpleFeatureBuilder featureBuilder;


  public GeotoolsTripRepository(DataStore dataStore) throws Exception {

    TRIP_TYPE = DataUtilities.createType(TRIP_TYPE_NAME,
        TRIP_TYPE_SPEC);
    featureBuilder = new SimpleFeatureBuilder(TRIP_TYPE);
    this.dataStore = dataStore;
    this.featureSource = dataStore.getFeatureSource(TRIP_TYPE_NAME);
  }

  @Override
  public void saveTrip(Trip trip) {

    double startLatitude = trip.getStartCoordinate().getLatitude();
    double startLongitude = trip.getStartCoordinate().getLongitude();
    double stopLatitude = trip.getStopCoordinate().getLatitude();
    double stopLongitude = trip.getStopCoordinate().getLongitude();
    Date logDate = trip.getLogDate();
    String vin = trip.getVin();
    int passengerCount = trip.getPassengerCount();

    /* Longitude (= x coord) first ! */
    Point startPoint = geometryFactory
        .createPoint(new com.vividsolutions.jts.geom.Coordinate(startLongitude, startLatitude));
    Point stopPoint = geometryFactory
        .createPoint(new com.vividsolutions.jts.geom.Coordinate(stopLongitude, stopLatitude));

    featureBuilder.add(startPoint);
    featureBuilder.add(stopPoint);
    featureBuilder.add(vin);
    featureBuilder.add(passengerCount);
    featureBuilder.add(logDate);
    String id = UUID.randomUUID().toString();
    SimpleFeature feature = featureBuilder.buildFeature(id);

    if (featureSource instanceof FeatureStore) {
      FeatureStore featureStore = (FeatureStore) featureSource;
      Transaction transaction = new DefaultTransaction("create");
      SimpleFeatureCollection collection = new ListFeatureCollection(TRIP_TYPE, Collections.singletonList(feature));

      try {
        featureStore.addFeatures(collection);
        transaction.commit();
      } catch (IOException e) {
        e.printStackTrace();
      }
    } else {
      String message = String.format("Failed to save Trip. %s is not an instance of %s", featureSource.getClass().getName(), FeatureStore.class.getName());
      logger.error(message);
      throw new RuntimeException(message);
    }

  }

  @Override
  public Collection<Trip> getTripsForVIN(String searchVin) throws IOException {
    String[] typeNames = dataStore.getTypeNames();
    SimpleFeatureSource featureSource1 = dataStore.getFeatureSource(typeNames[0]);

    /*
    FeatureSource	View
    FeatureStore	Table
    FeatureCollection	PreparedStatement
    FeatureIterator	ResultSet
    */
    SimpleFeatureSource source = dataStore.getFeatureSource(TRIP_TYPE_NAME);
    FilterFactory ff = CommonFactoryFinder.getFilterFactory( null );
    Filter filter = ff.equals(ff.property( "vin"), ff.literal( searchVin ) );

    SimpleFeatureCollection collection = source.getFeatures(filter);


    List<Trip> tripsForVIN = new ArrayList<>();

    SimpleFeatureIterator it = null;
    try {
      for (it = collection.features(); it.hasNext(); ) {
        SimpleFeature feature = it.next();
          String vin = feature.getAttribute("vin").toString();
          Point startPoint = (Point) feature.getAttribute("start_point");
          Coordinate startCoordinate = new Coordinate(startPoint.getX(), startPoint.getY());
          Point stopPoint = (Point) feature.getAttribute("stop_point");
          Coordinate stopCoordinate = new Coordinate(stopPoint.getX(), stopPoint.getY());
          int passengerCount = (int) feature.getAttribute("passenger_count");
          Date logDate = (Date) feature.getAttribute("log_date");

          Trip trip = new Trip(vin, startCoordinate, stopCoordinate, passengerCount, logDate);
          tripsForVIN.add(trip);
        //}
      }
    } finally {
      if (it != null) {
        it.close();
      }
    }

    return tripsForVIN;
  }
}
