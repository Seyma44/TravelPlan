package com.dabdm.travelplan;

import java.util.ArrayList;

import com.dabdm.travelplan.map.MapActivity;
import com.dabdm.travelplan.places.Place;
import com.google.android.gms.maps.model.LatLng;

import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

/**
 * An activity where you can choose one of the existing itineraries or create a new one
 * 
 */
public class ItinerariesActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_itineraries);
	
	test();
	
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, StorageHelper.getTravels(this.fileList()));
        setListAdapter(adapter);     
	

	Button button = (Button) findViewById(R.id.open_map_button);
	button.setOnClickListener(new View.OnClickListener() {
	    @Override
	    public void onClick(View v) {
		// TODO Auto-generated method stub
		//startActivity(new Intent(ItinerariesActivity.this.getApplicationContext(), DestinationActivity.class));
	    }
	});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	// Inflate the menu; this adds items to the action bar if it is present.
	getMenuInflater().inflate(R.menu.activity_itineraries, menu);
	return true;
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
	String item = (String) getListAdapter().getItem(position);
	Intent intent = new Intent(this, MapActivity.class);
	Log.i("test1", StorageHelper.getTravels(this.fileList())[position]);
	intent.putExtra("travelFileName", StorageHelper.getTravels(this.fileList())[position]);
	startActivity(intent);
    }
    
    public void test() {
	    /*Place p = new Place();
	    p.setFormatted_address("483 George Street, Sydney NSW, Australia");
	    Log.i("test", p.getFormatted_address());
	    Log.i("test", p.getAddressForRequest());*/
	    
	    Travel t = new Travel();
	    t.setDuration(5);
	    t.setLat(-33.86);
	    t.setLng(151.205);
	    t.setPlaceName("Australia");
	    t.setRadius(20000);
	    t.setTransportMode("walking");
	    t.setTravelName("ADM_Australia" + System.currentTimeMillis());
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
	    
	    Place p4 = new Place();
	    p4.setFormatted_address("Upper Level, Overseas Passenger Terminal/5 Hickson Road, The Rocks NSW, Australia");
	    Place.Geometry g4 = new Place.Geometry();
	    Place.Location l4 = new Place.Location();
	    l4.setLat(-33.8583790);
	    l4.setLng(151.2100270);
	    g4.setLocation(l4);
	    p4.setGeometry(g4);
	    p4.setIcon("http://maps.gstatic.com/mapfiles/place_api/icons/cafe-71.png");
	    p4.setId("f181b872b9bc680c8966df3e5770ae9839115440");
	    p4.setName("Quay");
	    p4.setRating((float) 4.1);
	    places.add(p4);
	    
	    Place p3 = new Place();
	    p3.setFormatted_address("483 George Street, Sydney NSW, Australia");
	    Place.Geometry g3 = new Place.Geometry();
	    Place.Location l3 = new Place.Location();
	    l3.setLat(-33.8731950);
	    l3.setLng(151.2063380);
	    g3.setLocation(l3);
	    p3.setGeometry(g3);
	    p3.setIcon("http://maps.gstatic.com/mapfiles/place_api/icons/civic_building-71.png");
	    p3.setId("017049cb4e82412aaf0efbde890e82b7f2987c16");
	    p3.setName("Chinatown Sydney");
	    p3.setRating((float) 4.0);
	    places.add(p3);
	    
	    
	    
	    t.setPlaces(places);
	    
	    StorageHelper.saveTravelObject(getFilesDir(), t.getTravelName(), t);
	}

}
