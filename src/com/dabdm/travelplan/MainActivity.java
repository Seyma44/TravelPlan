package com.dabdm.travelplan;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import com.google.android.gms.maps.SupportMapFragment;

import com.dabdm.travelplan.map.MapActivity;
import com.dabdm.travelplan.places.Place;
import com.dabdm.travelplan.places.ShowDetails;
import com.dabdm.travelplan.places.Place.Geometry;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

public class MainActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		final String placeReference = "CmRYAAAAciqGsTRX1mXRvuXSH2ErwW-jCINE1aLiwP64MCWDN5vkXvXoQGPKldMfmdGyqWSpm7BEYCgDm-iv7Kc2PF7QA7brMAwBbAcqMr5i1f4PwTpaovIZjysCEZTry8Ez30wpEhCNCXpynextCld2EBsDkRKsGhSLayuRyFsex6JA6NPh9dyupoTH3g";
		Button buttonNext = (Button) findViewById(R.id.button1);
		buttonNext.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// on click, start the Map activity
				//startActivity(new Intent(MainActivity.this, MapActivity.class));
				Intent intent = new Intent(MainActivity.this, ShowDetails.class);
				intent.putExtra("placeReference",placeReference); 
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	public void test() {
	    /*Place p = new Place();
	    p.setFormatted_address("483 George Street, Sydney NSW, Australia");
	    Log.i("test", p.getFormatted_address());
	    Log.i("test", p.getAddressForRequest());*/
	    
	    Travel t = new Travel();
	    t.setDuration(2);
	    t.setPlaceCoordinates(new LatLng(-33.86, 151.205));
	    t.setPlaceName("Australia");
	    t.setRadius(20000);
	    t.setTransportMode("walking");
	    t.setTravelName("Australia" + System.currentTimeMillis());
	    ArrayList<Place> places = new ArrayList<Place>();
	    Place p1 = new Place();
	    p1.setFormatted_address("529 Kent Street, Sydney NSW, Australia");
	    Place.Geometry g1 = new Place.Geometry();
	    Place.Location l1 = new Place.Location();
	    l1.setLat(-33.8750460);
	    l1.setLng(151.2052720);
	    g1.setLocation(l1);
	    p1.setGeometry(g1);
	    p1.setIcon("http://maps.gstatic.com/mapfiles/place_api/icons/restaurant-71.png");
	    p1.setId("827f1ac561d72ec25897df088199315f7cbbc8ed");
	    p1.setName("Tetsuya's");
	    p1.setRating((float) 4.3);
	    places.add(p1);
	    
	    Place p2 = new Place();
	    p2.setFormatted_address("107 George Street, The Rocks NSW, Australia");
	    Place.Geometry g2 = new Place.Geometry();
	    Place.Location l2 = new Place.Location();
	    l2.setLat(-33.8597750);
	    l2.setLng(151.2085920);
	    g2.setLocation(l2);
	    p2.setGeometry(g2);
	    p2.setIcon("http://maps.gstatic.com/mapfiles/place_api/icons/restaurant-71.png");
	    p2.setId("7beacea28938ae42bcac04faf79a607bf84409e6");
	    p2.setName("Rockpool");
	    p2.setRating((float) 4.0);
	    places.add(p2);
	    
	    t.setPlaces(places);
	    
	    StorageHelper.saveTravelObject(getFilesDir(), t.getTravelName(), t);
	}


}
