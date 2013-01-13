package com.dabdm.travelplan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.dabdm.travelplan.places.Place;
import com.dabdm.travelplan.places.ShowDetails;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		final String placeReference = "CmRYAAAAciqGsTRX1mXRvuXSH2ErwW-jCINE1aLiwP64MCWDN5vkXvXoQGPKldMfmdGyqWSpm7BEYCgDm-iv7Kc2PF7QA7brMAwBbAcqMr5i1f4PwTpaovIZjysCEZTry8Ez30wpEhCNCXpynextCld2EBsDkRKsGhSLayuRyFsex6JA6NPh9dyupoTH3g";
		Button buttonNext = (Button) findViewById(R.id.button1);
		buttonNext.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// on click, start the Map activity
				// startActivity(new Intent(MainActivity.this, MapActivity.class));
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
	    Place p = new Place();
	    p.setFormatted_address("483 George Street, Sydney NSW, Australia");
	    Log.i("test", p.getFormatted_address());
	    Log.i("test", p.getAddressForRequest());
	}

}
