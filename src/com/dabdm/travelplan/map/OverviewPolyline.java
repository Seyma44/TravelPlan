package com.dabdm.travelplan.map;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

/**
 * Represent the overview_polyline attribute of a route in the object return by the Google Directions API
 * Used to get a Java object from the JSON response
 */
public class OverviewPolyline {
    
    private String points;

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }
    
    public static PolylineOptions decodePoly(String encoded) {

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
