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

import com.dabdm.travelplan.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;

/**
 * Test activity for the Google Map API V2
 * TODO Calculations should be done in another Class
 */
public class MapActivity extends Activity {

    public static final String SHARED_PREF_FILE_NAME  = "prefFile";
    public static final String SHARED_PREF_ROUTES_KEY = "polyline";
    private GoogleMap	  mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_map);
	setUpMapIfNeeded();
	// Sets the map type to be "hybrid"
	mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

	// Add a marker
	final LatLng MELBOURNE = new LatLng(-37.81319, 144.96298);
	Marker melbourne = mMap.addMarker(new MarkerOptions().position(MELBOURNE).title("Melbourne").snippet("Population: 4,137,400")
		.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

	// Add a polyline
	Polyline line = mMap.addPolyline(new PolylineOptions().add(new LatLng(-37.81319, 144.96298), new LatLng(-31.95285, 115.85734))
		.width(15).color(Color.BLUE));

	// Get sharedPreferences file
	SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_FILE_NAME, MODE_PRIVATE);
	String encodedPolyline = sharedPreferences.getString(SHARED_PREF_ROUTES_KEY, "");
	if (encodedPolyline.equals("")) {
	    // Load routes from internet
	    new LoadDirections().execute();
	} else {
	    PolylineOptions polylineOptions = decodePoly(encodedPolyline);
	    Polyline line2 = mMap.addPolyline(polylineOptions.width(5).color(Color.BLUE));
	}
    }

    private void setUpMapIfNeeded() {
	// Do a null check to confirm that we have not already instantiated the map.
	if (mMap == null) {
	    mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
	    // Check if we were successful in obtaining the map.
	    if (mMap != null) {
		// The Map is verified. It is now safe to manipulate the map.

	    }
	}
    }

    /**
     * AsyncTask used to loaded a direction
     */
    private class LoadDirections extends AsyncTask<String, Integer, Boolean> {
	private static final int    CONNECTION_TIMEOUT = 10000;
	private static final String BASE_URL	   = "http://maps.googleapis.com/maps/api/directions/json";
	private String	      responseString     = "";

	@Override
	protected Boolean doInBackground(String... params) {
	    HttpResponse response = null;

	    BasicHttpParams httpParams = new BasicHttpParams();
	    HttpConnectionParams.setConnectionTimeout(httpParams, CONNECTION_TIMEOUT);
	    HttpConnectionParams.setSoTimeout(httpParams, CONNECTION_TIMEOUT);

	    HttpClient client = new DefaultHttpClient();

	    List<NameValuePair> pairs = new ArrayList<NameValuePair>();
	    pairs.add(new BasicNameValuePair("origin", "Valencia+Cathedral,Valencia,Spain"));
	    pairs.add(new BasicNameValuePair("destination", "Ciudad+de+las+Artes+y+las+Ciencias,Valencia,Spain"));
	    pairs.add(new BasicNameValuePair(
		    "waypoints",
		    "optimize:true|estadio+mestalla,Valencia,Spain|Mercat+central,Valencia,Spain|Universitat+Politecnica+de+Valencia,Valencia,Spain|Torres+de+Serrano,Valencia,Spain"));
	    pairs.add(new BasicNameValuePair("units", "metric"));
	    pairs.add(new BasicNameValuePair("sensor", "true"));
	    pairs.add(new BasicNameValuePair("mode", "walking"));
	    HttpGet request = new HttpGet(BASE_URL + "?" + URLEncodedUtils.format(pairs, "utf-8"));
	    request.setHeader("Accept", "application/json");
	    request.setParams(httpParams);
	    try {
		response = client.execute(request);
	    } catch (ClientProtocolException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return false;
	    } catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return false;
	    } catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return false;
	    }
	    HttpEntity entity = response.getEntity();
	    if (entity != null) {
		try {
		    responseString = EntityUtils.toString(entity);
		    return true;
		} catch (ParseException e1) {
		    // TODO Auto-generated catch block
		    e1.printStackTrace();
		    return false;
		} catch (IOException e1) {
		    // TODO Auto-generated catch block
		    e1.printStackTrace();
		    return false;
		}
	    }
	    return false;
	}

	@Override
	protected void onPostExecute(Boolean result) {
	    super.onPostExecute(result);
	    if (result) {
		if (responseString.equals("null")) { return; }

		// Convert from JSON to HighScoreList object
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

		// Save the String TODO save it in a datebase
		// Get sharedPreferences file
		SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_FILE_NAME, MODE_PRIVATE);
		Editor sharedPreferencesEditor = sharedPreferences.edit();
		sharedPreferencesEditor.putString(SHARED_PREF_ROUTES_KEY, directions.getRoutes().get(0).getOverview_polyline().getPoints());
		// Commit
		sharedPreferencesEditor.commit();

		PolylineOptions polylineOptions = decodePoly(directions.getRoutes().get(0).getOverview_polyline().getPoints());
		Polyline line = mMap.addPolyline(polylineOptions.width(5).color(Color.BLUE));
	    }
	}
    }

    private PolylineOptions decodePoly(String encoded) {

	PolylineOptions poly = new PolylineOptions();
	int len = encoded.length();
	int index = 0;
	long lat = 0;
	long lng = 0;

	while (index < len) {
	    int b;
	    int shift = 0;
	    int result = 0;
	    do {
		b = encoded.charAt(index++) - 63;
		result |= (b & 0x1f) << shift;
		shift += 5;
	    } while (b >= 0x20);
	    long dlat = (((result & 1) != 0) ? ~(result >> 1) : (result >> 1));
	    lat += dlat;

	    shift = 0;
	    result = 0;
	    do {
		b = encoded.charAt(index++) - 63;
		result |= (b & 0x1f) << shift;
		shift += 5;
	    } while (b >= 0x20);
	    long dlng = (((result & 1) != 0) ? ~(result >> 1) : (result >> 1));
	    lng += dlng;
	    LatLng nextPoint = new LatLng(lat * 0.00001, lng * 0.00001); // Convert the lat and lng to microdegrees.
	    poly.add(nextPoint);
	}

	return poly;
    }

}
