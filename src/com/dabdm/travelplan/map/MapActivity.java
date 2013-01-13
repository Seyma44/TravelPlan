package com.dabdm.travelplan.map;

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
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.dabdm.travelplan.R;
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
 * TODO Calculations should be done in another Class
 */
public class MapActivity extends FragmentActivity {

    public static final String SHARED_PREF_FILE_NAME  = "prefFile";
    public static final String SHARED_PREF_ROUTES_KEY = "polyline";
    private int POLYLINE_WIDTH = 5;
    private int POLYLINE_COLOR = Color.BLUE;
    private GoogleMap	  mMap;
    private Travel travel;
    private Polyline currentItinerary = null;
    private int itineraryIndex = 0;
    private MenuItem	 menuItem0     = null;
    private MenuItem	 menuItem1     = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_map);
	setUpMapIfNeeded();
	
	// Get file name
	Intent intent = getIntent();
	Bundle extras = intent.getExtras();
	String fileName = extras.getString("travelFileName");
	
	// TODO get the travel from the file
	travel = new Travel();
	
	// Display the itinerary (if there is more than one place per day
	if(Math.ceil(((double)travel.getPlaces().size())/travel.getDuration()) > 1) {
	    displayItineraries();
	}
	// Display a marker for each Place
	addTravelMarkers();
	
	// Sets the map type to be "hybrid"
	mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

	// Move the camera to the right place TODO get the correct zoom or map boundaries
	mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(travel.getPlaceCoordinates(), 15));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	getMenuInflater().inflate(R.menu.activity_map, menu);
	menuItem0 = menu.getItem(0);
	menuItem1 = menu.getItem(1);
	return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	int itemId = item.getItemId();
	currentItinerary.remove();
	itineraryIndex += (itemId == 0)?(-1):1;
	displayItineraries();
	return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
	// Do not show the previous button if you are showing the first day
	if(itineraryIndex == 0) {
	    menu.getItem(0).setEnabled(false).setVisible(false);
	} else {
	    menu.getItem(0).setEnabled(true).setVisible(true);
	}
	// Do not show the next button if you are showing the last day
	if(itineraryIndex == (travel.getDuration() - 1)) {
	    menu.getItem(travel.getDuration() - 1).setEnabled(false).setVisible(false);
	} else {
	    menu.getItem(travel.getDuration() - 1).setEnabled(true).setVisible(true);
	}
	
	return super.onPrepareOptionsMenu(menu);
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
     * Used to add a Marker for each Place in the Travel
     */
    public void addTravelMarkers() {
	ArrayList<Place> places = travel.getPlaces();
	int placesLength = places.size();
	for(int i = 0; i < placesLength; i++) {
	    addPlaceMarker(places.get(i));
	}
    }
    
    /**
     * Used to add a marker to point out a specific place
     * @param place the Place object to mark
     * @return the new Marker
     */
    public Marker addPlaceMarker(Place place) {
	// TODO get real coordinate
	double lat = 0.0;
	double lng = 0.0;
	LatLng latLng = new LatLng(lat, lng);
	String title = place.getName();
	String snippet = place.getSnippet();
	// TODO get real icon from the icon url
	BitmapDescriptor icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE);
	return addMarker(latLng, title, snippet, icon);
    }
    
    /**
     * Used to add a marker to the map
     * @param latLng the LatLng object for the coordinates of the marker to add
     * @param title String displayed on top of the bubble displayed when the marker is clicked
     * @param snippet String displayed as the message in the bubble
     * @param icon BitmapDescriptor the marker icon
     * @return the new Marker added to the map
     */
    public Marker addMarker(LatLng latLng, String title, String snippet, BitmapDescriptor icon) {
	return mMap.addMarker(new MarkerOptions().position(latLng).title(title).snippet(snippet).icon(icon));
    }
    
    /**
     * Used to trace a polyline on the map
     * @param polylineOpt the PolylineOptions containing all the polyline points
     * @param width the int for the line width
     * @param color the int representing the line color
     * @return the new Polyline added to the map
     */
    public Polyline addPolyline(PolylineOptions polylineOpt, int width, int color) {
	return mMap.addPolyline(polylineOpt.width(width).color(color));
    }
    
    /**
     * Used to trace an encoded polyline
     * @param encodedPolyline the String containing the encoded points for the polyline to trace
     * @param width the int for the line width
     * @param color the int representing the line color
     * @return the new Polyline added to the map
     * @see addPolyline
     */
    public Polyline addEncodedPolyline(String encodedPolyline, int width, int color) {
	return mMap.addPolyline(OverviewPolyline.decodePoly(encodedPolyline).width(width).color(color));
    }

    /**
     * Calculate and display the itinerary for each day
     */
    private void displayItineraries() {
	// TODO check if there is more than 1 place
	ArrayList<String> itineraries = travel.getItineraries();
	int itineraryNumber = itineraries.size();
	// If the itineraries have already been calculated
	if(itineraryNumber == 1) {
	    // For 1 day, just display the itinerary
	    currentItinerary = addEncodedPolyline(itineraries.get(0), POLYLINE_WIDTH, POLYLINE_COLOR);
	} else if(itineraryNumber > 1) {
	    // TODO display for the different days
	    currentItinerary = addEncodedPolyline(itineraries.get(itineraryIndex), POLYLINE_WIDTH, POLYLINE_COLOR);
	} else { // If the itineraries have not been calculated yet
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
	private int daysNumber = travel.getDuration();
	private ArrayList<Place> travelPlaces = travel.getPlaces();
	private int travelPlacesLength = travelPlaces.size();
	private int placesNumber = Math.round(((float)travelPlacesLength) / daysNumber);

	@Override
	protected Boolean doInBackground(String... params) {
	    int j = 0;
	    int i = 0;
	    int k = 0;
	    for(int d = 0; d < daysNumber; d++) {
		ArrayList<Place> dayPlaces = new ArrayList<Place>();
		j = (d == daysNumber - 1)?travelPlacesLength:(j + placesNumber);
		for(i = k; i < j; i++) {
		    dayPlaces.add(travelPlaces.get(i));
		}
		if(dayPlaces.size() > 1) {
		    String encodedItinerary = loadItinerary(dayPlaces);
		    // Add it to the travel
		    travel.addItinerary(encodedItinerary);
		}
		k += placesNumber;
	    }
	    // TODO save the travel in the file
	    
	    return true;
	}

	@Override
	protected void onPostExecute(Boolean result) {
	    super.onPostExecute(result);
	    if (result) {
		displayItineraries();
	    }
	}
	
	private String loadItinerary(ArrayList<Place> places) {
	    
	    int placesLength = places.size();
	    
	    String responseString     = "";
	    HttpResponse response = null;

	    BasicHttpParams httpParams = new BasicHttpParams();
	    HttpConnectionParams.setConnectionTimeout(httpParams, CONNECTION_TIMEOUT);
	    HttpConnectionParams.setSoTimeout(httpParams, CONNECTION_TIMEOUT);

	    HttpClient client = new DefaultHttpClient();

	    List<NameValuePair> pairs = new ArrayList<NameValuePair>();
	    pairs.add(new BasicNameValuePair("origin", places.get(0).getAddressForRequest()));
	    pairs.add(new BasicNameValuePair("destination", places.get(placesLength - 1).getAddressForRequest()));
	    if(placesLength > 2) {
		String waypoints = "optimize:true";
		for(int i = 1; i < placesLength - 1; i++) {
		    waypoints = waypoints.concat("|" + places.get(i).getAddressForRequest());
		}
		pairs.add(new BasicNameValuePair("waypoints", waypoints));
	    }
	    pairs.add(new BasicNameValuePair("units", "metric"));
	    pairs.add(new BasicNameValuePair("sensor", "true"));
	    pairs.add(new BasicNameValuePair("mode", travel.getTransportMode()));
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
		
		return directions.getRoutes().get(0).getOverview_polyline().getPoints();
	}
    }
}
