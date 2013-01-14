package com.dabdm.travelplan.map;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import com.dabdm.travelplan.FtpHelper;
import com.dabdm.travelplan.R;
import com.dabdm.travelplan.StorageHelper;
import com.dabdm.travelplan.Travel;
import com.dabdm.travelplan.places.Place;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Activity showing the map with the calculated itinerary
 */
public class MapActivity extends FragmentActivity implements LocationListener {

    public static final String	  SHARED_PREF_FILE_NAME = "prefFile";
    public static final String	  SHARED_PREF_FILE_KEY  = "fileName";
    public static final String	  SHARED_PREF_INDEX_KEY = "itineraryIndex";
    private int			 POLYLINE_WIDTH	= 5;
    private int			 POLYLINE_COLOR	= Color.BLUE;
    private GoogleMap		   mMap;
    private Travel		      travel;
    private Polyline		    currentItinerary      = null;
    private int			 itineraryIndex	= 0;
    private String		      fileName	      = "";
    private ArrayList<Marker>	   displayedMarkers      = new ArrayList<Marker>();
    private ArrayList<ArrayList<Place>> dayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
	setContentView(R.layout.activity_map);
	setUpMapIfNeeded();

	// Get file name
	Intent intent = getIntent();
	Bundle extras = intent.getExtras();
	fileName = extras.getString("travelFileName");

	// Get sharedPreferences file
	SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_FILE_NAME, MODE_PRIVATE);
	if (fileName.equals(sharedPreferences.getString(SHARED_PREF_FILE_KEY, ""))) {
	    itineraryIndex = sharedPreferences.getInt(SHARED_PREF_INDEX_KEY, 0);
	}

	mapInit();
	Log.i("test", "filename " + fileName);
	Log.i("oncreate", "end");
    }

    @Override
    protected void onPause() {
	super.onPause();
	// Get sharedPreferences file
	SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_FILE_NAME, MODE_PRIVATE);
	Editor sharedPreferencesEditor = sharedPreferences.edit();
	sharedPreferencesEditor.putString(SHARED_PREF_FILE_KEY, fileName);
	sharedPreferencesEditor.putInt(SHARED_PREF_INDEX_KEY, itineraryIndex);
	// Commit
	sharedPreferencesEditor.commit();
	Log.i("onpause", "end");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	getMenuInflater().inflate(R.menu.activity_map, menu);
	return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	int itemId = item.getItemId();
	if (currentItinerary != null) {
	    currentItinerary.remove();
	}
	Log.i("itemId", itemId + "");
	itineraryIndex += (itemId == R.id.menu_prev_day) ? (-1) : 1;
	Log.i("itiIndex", itineraryIndex + "");
	setTitle(getResources().getString(R.string.title_activity_map) + " " + (itineraryIndex + 1));
	displayItineraries();
	return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
	// Do not show the previous button if you are showing the first day
	if (itineraryIndex == 0) {
	    menu.getItem(0).setEnabled(false).setVisible(false);
	} else {
	    menu.getItem(0).setEnabled(true).setVisible(true);
	}
	// Do not show the next button if you are showing the last day
	if (itineraryIndex == (travel.getDuration() - 1)) {
	    menu.getItem(1).setEnabled(false).setVisible(false);
	} else {
	    menu.getItem(1).setEnabled(true).setVisible(true);
	}

	return super.onPrepareOptionsMenu(menu);
    }

    /**
     * Initialize the map activity
     */
    private void mapInit() {
	// Get the travel from the file
	travel = StorageHelper.getTravelObject(getFilesDir(), fileName);

	dividePlaces();

	setTitle(getResources().getString(R.string.title_activity_map) + " " + (itineraryIndex + 1));

	// Display the itinerary (if there is more than one place per day
	// if(Math.ceil(((double)travel.getPlaces().size())/travel.getDuration()) > 1)
	displayItineraries();

	// Display a marker for each Place
	// addTravelMarkers();

	initLocation();

	// Sets the map type to be "hybrid"
	mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

	// Move the camera to the right place TODO get the correct zoom or map boundaries
	mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(travel.getPlaceCoordinates(), 15));
    }

    /**
     * Instantiate the map only if it does not already exist
     */
    private void setUpMapIfNeeded() {
	// Do a null check to confirm that we have not already instantiated the map.
	if (mMap == null) {
	    mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
	    // Check if we were successful in obtaining the map.
	    if (mMap != null) {
		// The Map is verified. It is now safe to manipulate the map.

	    }
	}
    }

    /**
     * Initialize Location manager
     */
    private void initLocation() {
	// Enabling MyLocation Layer of Google Map
	mMap.setMyLocationEnabled(true);

	// Getting LocationManager object from System Service LOCATION_SERVICE
	LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
	// Creating a criteria object to retrieve provider
	Criteria criteria = new Criteria();
	// Getting the name of the best provider
	String provider = locationManager.getBestProvider(criteria, true);
	// Getting Current Location
	Location location = locationManager.getLastKnownLocation(provider);

	if (location != null) {
	    onLocationChanged(location);
	}

	locationManager.requestLocationUpdates(provider, 20000, 0, this);
    }

    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onProviderDisabled(String provider) {
	// TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
	// TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
	// TODO Auto-generated method stub

    }

    /**
     * Used to add a Marker for each Place in the Travel
     */
    public void addTravelMarkers() {
	ArrayList<Place> places = travel.getPlaces();
	int placesLength = places.size();
	for (int i = 0; i < placesLength; i++) {
	    addPlaceMarker(places.get(i));
	}
    }

    /**
     * Used to add a Marker for each Place in the Travel
     * 
     * @param places
     *            a list of Place for which we want a Marker
     */
    public void addPlacesMarkers(ArrayList<Place> places) {
	// Remove existing Marker
	for (Marker ma : displayedMarkers) {
	    ma.remove();
	}
	displayedMarkers.clear();
	int placesLength = places.size();
	for (int i = 0; i < placesLength; i++) {
	    Marker m = addPlaceMarker(places.get(i));
	    displayedMarkers.add(m);
	}
    }

    /**
     * Used to add a marker to point out a specific place
     * 
     * @param place
     *            the Place object to mark
     * @return the new Marker
     */
    public Marker addPlaceMarker(Place place) {
	LatLng latLng = place.getCoordinates();
	String title = place.getName();
	String snippet = place.getSnippet();
	// TODO get real icon from the icon url
	BitmapDescriptor icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE);
	return addMarker(latLng, title, snippet, icon);
    }

    /**
     * Used to add a marker to the map
     * 
     * @param latLng
     *            the LatLng object for the coordinates of the marker to add
     * @param title
     *            String displayed on top of the bubble displayed when the marker is clicked
     * @param snippet
     *            String displayed as the message in the bubble
     * @param icon
     *            BitmapDescriptor the marker icon
     * @return the new Marker added to the map
     */
    public Marker addMarker(LatLng latLng, String title, String snippet, BitmapDescriptor icon) {
	return mMap.addMarker(new MarkerOptions().position(latLng).title(title).snippet(snippet).icon(icon));
    }

    /**
     * Used to trace a polyline on the map
     * 
     * @param polylineOpt
     *            the PolylineOptions containing all the polyline points
     * @param width
     *            the int for the line width
     * @param color
     *            the int representing the line color
     * @return the new Polyline added to the map
     */
    public Polyline addPolyline(PolylineOptions polylineOpt, int width, int color) {
	return mMap.addPolyline(polylineOpt.width(width).color(color));
    }

    /**
     * Used to trace an encoded polyline
     * 
     * @param encodedPolyline
     *            the String containing the encoded points for the polyline to trace
     * @param width
     *            the int for the line width
     * @param color
     *            the int representing the line color
     * @return the new Polyline added to the map
     * @see addPolyline
     */
    public Polyline addEncodedPolyline(String encodedPolyline, int width, int color) {
	return mMap.addPolyline(OverviewPolyline.decodePoly(encodedPolyline).width(width).color(color));
    }

    /**
     * Divide Place for each day
     */
    private void dividePlaces() {
	int daysNumber = travel.getDuration();
	ArrayList<Place> travelPlaces = travel.getPlaces();
	int travelPlacesLength = travelPlaces.size();
	int placesNumber = Math.round(((float) travelPlacesLength) / daysNumber);
	int j = 0;
	int i = 0;
	int k = 0;

	dayList = new ArrayList<ArrayList<Place>>();
	for (int d = 0; d < daysNumber; d++) {
	    ArrayList<Place> dayPlaces = new ArrayList<Place>();
	    j = (d == daysNumber - 1) ? travelPlacesLength : (j + placesNumber);
	    for (i = k; i < j; i++) {
		dayPlaces.add(travelPlaces.get(i));
	    }
	    dayList.add(dayPlaces);
	    k += placesNumber;
	}
    }

    /**
     * Calculate and display the itinerary for each day
     */
    private void displayItineraries() {

	// TODO check if there is more than 1 place
	ArrayList<String> itineraries = travel.getItineraries();
	int itineraryNumber = itineraries.size();
	// If the itineraries have already been calculated
	if (itineraryNumber == 1) {
	    Log.i("displayItineraries", "1");
	    // For 1 day, just display the places and the itinerary
	    addPlacesMarkers(dayList.get(0));
	    if (!itineraries.get(0).equals("")) {
		currentItinerary = addEncodedPolyline(itineraries.get(0), POLYLINE_WIDTH, POLYLINE_COLOR);
	    }
	} else if (itineraryNumber > 1) {
	    // TODO display for the different days
	    Log.i("displayItineraries", "plusieurs iti " + dayList.get(itineraryIndex).size() + " places");
	    addPlacesMarkers(dayList.get(itineraryIndex));
	    if (!itineraries.get(itineraryIndex).equals("")) {
		currentItinerary = addEncodedPolyline(itineraries.get(itineraryIndex), POLYLINE_WIDTH, POLYLINE_COLOR);
	    }
	} else { // If the itineraries have not been calculated yet
	    Log.i("displayItineraries", "load from internet");
	    // Display progress bar
	    MapActivity.this.setProgressBarIndeterminate(true);
	    MapActivity.this.setProgressBarIndeterminateVisibility(true);
	    // Load itineraries from Internet
	    new LoadDirections().execute();
	}
    }

    /**
     * AsyncTask used to loaded a direction
     */
    private class LoadDirections extends AsyncTask<String, Integer, Boolean> {
	private static final int    CONNECTION_TIMEOUT = 10000;
	private static final String BASE_URL	   = "http://maps.googleapis.com/maps/api/directions/json";

	@Override
	protected Boolean doInBackground(String... params) {

	    for (ArrayList<Place> l : dayList) {
		if (l.size() > 1) {
		    String encodedItinerary = loadItinerary(l);
		    // Add it to the travel
		    travel.addItinerary(encodedItinerary);
		} else {
		    travel.addItinerary("");
		}
	    }
	    // Save the travel in the file
	    StorageHelper.saveTravelObject(getFilesDir(), fileName, travel);

	    // Save the travel on server
	    FtpHelper ftp = new FtpHelper();
	    File file = new File(getFilesDir() + "/TRAVELPLAN" + fileName);
	    ftp.connect();
	    ftp.put(file);
	    ftp.close();

	    return true;
	}

	@Override
	protected void onPostExecute(Boolean result) {
	    super.onPostExecute(result);
	    if (result) {
		displayItineraries();
		// Hide progress bar
		MapActivity.this.setProgressBarIndeterminate(false);
		MapActivity.this.setProgressBarIndeterminateVisibility(false);
	    }
	}

	private String loadItinerary(ArrayList<Place> places) {

	    int placesLength = places.size();

	    String responseString = "";
	    HttpResponse response = null;

	    BasicHttpParams httpParams = new BasicHttpParams();
	    HttpConnectionParams.setConnectionTimeout(httpParams, CONNECTION_TIMEOUT);
	    HttpConnectionParams.setSoTimeout(httpParams, CONNECTION_TIMEOUT);

	    HttpClient client = new DefaultHttpClient();

	    List<NameValuePair> pairs = new ArrayList<NameValuePair>();
	    pairs.add(new BasicNameValuePair("origin", places.get(0).getAddressForRequest()));
	    pairs.add(new BasicNameValuePair("destination", places.get(placesLength - 1).getAddressForRequest()));
	    if (placesLength > 2) {
		String waypoints = "optimize:true";
		for (int i = 1; i < placesLength - 1; i++) {
		    waypoints = waypoints.concat("|" + places.get(i).getAddressForRequest());
		}
		pairs.add(new BasicNameValuePair("waypoints", waypoints));
	    }
	    pairs.add(new BasicNameValuePair("units", "metric"));
	    pairs.add(new BasicNameValuePair("sensor", "true"));
	    pairs.add(new BasicNameValuePair("mode", travel.getTransportMode()));
	    Log.i("url", BASE_URL + "?" + URLEncodedUtils.format(pairs, "utf-8"));
	    HttpGet request = new HttpGet(BASE_URL + "?" + URLEncodedUtils.format(pairs, "utf-8"));
	    request.setHeader("Accept", "application/json");
	    request.setParams(httpParams);
	    try {
		response = client.execute(request);
	    } catch (ClientProtocolException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return "";
	    } catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return "";
	    } catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return "";
	    }
	    HttpEntity entity = response.getEntity();
	    if (entity != null) {
		try {
		    responseString = EntityUtils.toString(entity);
		} catch (ParseException e1) {
		    // TODO Auto-generated catch block
		    e1.printStackTrace();
		} catch (IOException e1) {
		    // TODO Auto-generated catch block
		    e1.printStackTrace();
		}
	    }

	    if (responseString.equals("null")) { return ""; }

	    // Convert from JSON to Direction object
	    GsonBuilder builder = new GsonBuilder();
	    Gson gson = builder.create();
	    JSONObject json = null;
	    try {
		json = new JSONObject(responseString);
	    } catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	    Directions directions = gson.fromJson(json.toString(), Directions.class);
	    Log.i("response", json.toString());
	    return directions.getRoutes().get(0).getOverview_polyline().getPoints();
	}
    }
}
