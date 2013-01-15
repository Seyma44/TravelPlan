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

public class DestinationActivity extends FragmentActivity {
	int value = 1;
	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * current dropdown position.
	 */
	private Context context = this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_destination);
		Button button = (Button) findViewById(R.id.button_next);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				EditText etTravelName = (EditText) findViewById(R.id.name_project);
				EditText etCityAdress = (EditText) findViewById(R.id.name_city);
				TextView lblCoord = (TextView) findViewById(R.id.location_show_label);
				EditText etRadius = (EditText) findViewById(R.id.diametre_number);
				EditText etNbDays = (EditText) findViewById(R.id.duration_number);

				List<Address> address = null;

				Geocoder coder = new Geocoder(context);
				try {
					// get the coordinates of the adress
					String txtAdress = etCityAdress.getText().toString();
					address = coder.getFromLocationName(txtAdress, 5);
				} catch (IOException e) {
					e.printStackTrace();
				}
				Address location = address.get(0);
				double lat = location.getLatitude();
				double lng = location.getLongitude();

				lblCoord.setText("Location : " + String.valueOf(lat) + ", "
						+ String.valueOf(lng));

				Travel travel = new Travel();
				travel.setTravelName(etTravelName.getText().toString());
				travel.setLat(lat);
				travel.setLng(lng);
				travel.setRadius(Integer.valueOf(etRadius.getText().toString()));
				travel.setDuration(Integer.valueOf(etNbDays.getText()
						.toString()));

				StorageHelper.saveTravelObject(getFilesDir(),
						travel.getTravelName(), travel);

				Intent intent = new Intent();
				intent.setClass(DestinationActivity.this.context,
						PlacesActivity.class);
				intent.putExtra("travel", travel);
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_destination, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_settings:
			// app icon in action bar clicked; go home
			Intent intent = new Intent(this, SettingsActivity.class);

			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
