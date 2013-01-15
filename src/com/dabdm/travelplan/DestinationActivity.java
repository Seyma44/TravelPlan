package com.dabdm.travelplan;

import java.io.IOException;
import java.util.List;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.dabdm.travelplan.PlacesActivity.DummySectionFragment;
import com.dabdm.travelplan.map.MapActivity;
import android.location.Geocoder;

public class DestinationActivity extends FragmentActivity implements
		ActionBar.OnNavigationListener {
	int value = 1;

	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * current dropdown position.
	 */
	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";
	private Context context = this;
	private int daysNumber;
	private int range;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_destination);
//		String[] strings = getResources().getStringArray(R.array.action_list);
//		// Set up the action bar to show a dropdown list.
//		final ActionBar actionBar = getActionBar();
//		actionBar.setDisplayShowTitleEnabled(false);
//		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
//		
//		
		Button button = (Button) findViewById(R.id.button_next);
        button.setOnClickListener(new View.OnClickListener() {@Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
        		EditText etTravelName = (EditText)findViewById(R.id.name_project);
        		EditText etCityAdress = (EditText)findViewById(R.id.name_city);
        		TextView lblCoord = (TextView)findViewById(R.id.location_show_label);
        		EditText etRadius = (EditText)findViewById(R.id.diametre_number);
        		EditText etNbDays = (EditText)findViewById(R.id.duration_number);
        		
        		List<Address> address = null;
        		
        		Geocoder coder = new Geocoder(context);
        		try {
        			// get the coordinates of the adress
        			String txtAdress = etCityAdress.getText().toString();
					address = coder.getFromLocationName(txtAdress,5);
				} catch (IOException e) {
					e.printStackTrace();
				}
        		Address location = address.get(0);
                double lat = location.getLatitude();
                double lng = location.getLongitude();
                
                lblCoord.setText("Location : " + String.valueOf(lat) + ", " + String.valueOf(lng));
                
        		Travel travel = new Travel();
        		travel.setTravelName(etTravelName.getText().toString());
        		travel.setLat(lat);
        		travel.setLng(lng);
        		travel.setRadius(Integer.valueOf(etRadius.getText().toString()));
        		travel.setDuration(Integer.valueOf(etNbDays.getText().toString()));
        		
        		StorageHelper.saveTravelObject(getFilesDir(), travel.getTravelName(), travel);
        		
        		Intent intent = new Intent();
        		intent.setClass(DestinationActivity.this.context, PlacesActivity.class);
        		intent.putExtra("travel", travel);
        		startActivity(intent);
        		
        		//startActivity(new Intent(DestinationActivity.this.context, ItinerariesActivity.class));

        	}
        });
//		// Set up the dropdown list navigation in the action bar.
//		actionBar.setListNavigationCallbacks(
//		// Specify a SpinnerAdapter to populate the dropdown list.
//				new ArrayAdapter<String>(getActionBarThemedContextCompat(),
//						android.R.layout.simple_list_item_1,
//						strings), this);
	}

//	/**
//	 * Backward-compatible version of {@link ActionBar#getThemedContext()} that
//	 * simply returns the {@link android.app.Activity} if
//	 * <code>getThemedContext</code> is unavailable.
//	 */
//	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
//	private Context getActionBarThemedContextCompat() {
//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
//			return getActionBar().getThemedContext();
//		} else {
//			return this;
//		}
//	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		// Restore the previously serialized current dropdown position.
//		if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
//			getActionBar().setSelectedNavigationItem(
//					savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
//		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// Serialize the current dropdown position.
//		outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, getActionBar()
//				.getSelectedNavigationIndex());
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

	@Override
	public boolean onNavigationItemSelected(int arg0, long arg1) {
		// TODO Auto-generated method stub
		return false;
	}

}
