package com.dabdm.travelplan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.dabdm.travelplan.places.Place;
import com.dabdm.travelplan.places.PlacesListActivity;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//test();
		Button buttonNext = (Button) findViewById(R.id.button1);
		buttonNext.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// on click, start the Map activity
				// startActivity(new Intent(MainActivity.this, MapActivity.class));
				startActivity(new Intent(MainActivity.this,PlacesListActivity.class));
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
	    Place p = new Place();
	    p.setFormatted_address("483 George Street, Sydney NSW, Australia");
	    Log.i("test", p.getFormatted_address());
	    Log.i("test", p.getAddressForRequest());
	}

}
