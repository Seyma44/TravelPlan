package com.dabdm.travelplan;

import java.io.IOException;
import java.util.List;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * This activity allows the user to set up the configuration of its project : =>
 * Choose the name of the project => Type the adress of the place he will stay
 * => Type the radius he's interested in (the radius will determine how far the
 * user is willing to go to visit places) => Type the trip duration
 * 
 */
public class DestinationActivity extends FragmentActivity {
	private Context context = this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_destination);
		Button button = (Button) findViewById(R.id.button_next);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				EditText etTravelName = (EditText) findViewById(R.id.name_project);
				EditText etCityAdress = (EditText) findViewById(R.id.name_city);
				EditText etRadius = (EditText) findViewById(R.id.diametre_number);
				EditText etNbDays = (EditText) findViewById(R.id.duration_number);

				List<Address> address = null;

				Geocoder coder = new Geocoder(context);
				try {
					// get the coordinates of the address the user typed
					String txtAdress = etCityAdress.getText().toString();
					address = coder.getFromLocationName(txtAdress, 5);
				} catch (IOException e) {
					e.printStackTrace();
				}
				Address location = address.get(0);
				double lat = location.getLatitude();
				double lng = location.getLongitude();

				Travel travel = new Travel();
				travel.setTravelName(etTravelName.getText().toString());
				travel.setLat(lat);
				travel.setLng(lng);
				String toastEmptyString = "You must fill in the radius and duration field";
				if (etRadius.getText().toString().isEmpty()
						|| etNbDays.getText().toString().isEmpty()) {
					Toast toast = Toast.makeText(context, toastEmptyString,
							Toast.LENGTH_SHORT);
					toast.show();
				} else {
					int radius = Integer.valueOf(etRadius.getText().toString());
					int duration = Integer.valueOf(etNbDays.getText()
							.toString());
					String toastInvalid = "Radius must be between 100 and 50000, Duration must be between 1 and 20";
					if (radius < 100 || radius > 50000 || duration < 1
							|| duration > 20) {
						Toast toast = Toast.makeText(context, toastInvalid,
								Toast.LENGTH_SHORT);
						toast.show();
					} else {
						travel.setRadius(radius);
						travel.setDuration(duration);
						// Save the object travel
						StorageHelper.saveTravelObject(getFilesDir(),
								travel.getTravelName(), travel);

						Intent intent = new Intent();
						intent.setClass(DestinationActivity.this.context,
								PlacesActivity.class);
						intent.putExtra("travel", travel);
						startActivity(intent);
					}
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_destination, menu);
		return true;
	}

}
