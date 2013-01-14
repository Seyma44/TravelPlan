package com.dabdm.travelplan.places;

import java.util.ArrayList;

import com.dabdm.travelplan.R;
import com.dabdm.travelplan.R.layout;
import com.dabdm.travelplan.R.menu;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;

public class PlacesMapActivity extends FragmentActivity {
    
    private GoogleMap placesMap;
    private ArrayList<Place> placesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_places_map);
	setUpMapIfNeeded();
	
	// TODO get placesList from previous activity
	
	for(Place p : placesList) {
	    LatLng latLng = p.getCoordinates();
	    String title = p.getName();
	    String snippet = p.getSnippet();
	    BitmapDescriptor icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE);
	    placesMap.addMarker(new MarkerOptions().position(latLng).title(title).snippet(snippet).icon(icon));
	}
    }

    
    /**
     * Instantiate the map only if it does not already exist
     */
    private void setUpMapIfNeeded() {
	// Do a null check to confirm that we have not already instantiated the map.
	if (placesMap == null) {
	    placesMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapPlaces)).getMap();
	    // Check if we were successful in obtaining the map.
	    if (placesMap != null) {
		// The Map is verified. It is now safe to manipulate the map.

	    }
	}
    }

}
