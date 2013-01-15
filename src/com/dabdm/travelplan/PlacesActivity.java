package com.dabdm.travelplan;

import com.dabdm.travelplan.StorageHelper;

import java.util.ArrayList;
import org.json.JSONException;
import org.json.JSONObject;
import com.dabdm.travelplan.map.MapActivity;
import com.dabdm.travelplan.places.GoogleRequests;
import com.dabdm.travelplan.places.Place;
import com.dabdm.travelplan.places.PlacesList;
import com.dabdm.travelplan.places.ShowDetails;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

/**
 * 
 * Activity showing a list of the places given by the API GooglePlace
 *
 */
public class PlacesActivity extends FragmentActivity {
	private ListView listPlaces;
	private ListView lvPlaces;
	private Place[] listAllPlaces;
	private Travel travel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_places);
		listPlaces = (ListView) findViewById(R.id.listplaces);
		
		//Get the travel from the DestinationActivity
		travel = (Travel) getIntent().getSerializableExtra("travel");
		
		// Get the list of places (GooglePlace API)
		new LoadPlaces().execute(travel.getLat(), travel.getLng(),
				(double) travel.getRadius());
		
		
		lvPlaces = (ListView) findViewById(R.id.listplaces);
		lvPlaces.setSelector(R.drawable.listview);
		lvPlaces.setLongClickable(true);

		Button button = (Button) findViewById(R.id.button_next_places);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				// Save the travel object and start the map activity (shows the map...)
				Intent i = new Intent(getBaseContext(), MapActivity.class);
				travel.getItineraries().clear();
				StorageHelper.saveTravelObject(getFilesDir(),
						travel.getTravelName(), travel);
				i.putExtra("travelFileName", travel.getTravelName());
				startActivity(i);
			}
		});
		
		// this method allow the user to select/deslect a place
		lvPlaces.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Place chosenPlace = listAllPlaces[arg2];
				int i = 0;
				// We compare the name of the touched place with the names
				// that are already on the list, in order to know if we add it 
				// or delete it (select/deselect). We can't user the contains method
				// because the reference might change.
				boolean found = false;
				while (i < travel.getPlaces().size() && !found) {
					found = travel.getPlaces().get(i).getName()
							.equalsIgnoreCase(chosenPlace.getName());
					i++;
				}
				if (found) {
					i--;
					travel.getPlaces().remove(i);
				} else {
					travel.getPlaces().add(chosenPlace);
				}
			}
		});
		
		// This method allows the user to see the details of a place when he "long-click" on it
		lvPlaces.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				Intent i = new Intent(getBaseContext(), ShowDetails.class);
				i.putExtra("placeReference", listAllPlaces[arg2].getReference());
				startActivity(i);
				return true;
			}
		});
	}

	/**
	 * This method save the travel object when onPause is called
	 */
	@Override
	protected void onPause() {
		super.onPause();
		StorageHelper.saveTravelObject(getFilesDir(), travel.getTravelName(),
				travel);

	}

	/**
	 * This method gets the travel object when onResume is called
	 */
	@Override
	protected void onResume() {
		super.onResume();
		travel = StorageHelper.getTravelObject(getFilesDir(),
				travel.getTravelName());
	}

	/**
	 * AsyncTask used to load a list of interesting places
	 */
	private class LoadPlaces extends AsyncTask<Double, Integer, Boolean> {
		private String responseString = "";

		@Override
		protected Boolean doInBackground(Double... params) {
			GoogleRequests request = new GoogleRequests();
			double x = params[0];
			double y = params[1];
			// call of the method that contacts the https server...
			responseString = request.getPlaces(x, y, (int)(double)params[2]);

			return responseString == null ? false : true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (result) {
				if (responseString.equals("null")) {
					return;
				}

				// Convert from JSON to Direction object
				GsonBuilder builder = new GsonBuilder();
				Gson gson = builder.create();
				JSONObject json = null;
				try {
					json = new JSONObject(responseString);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				PlacesList arListAllPlaces = gson.fromJson(json.toString(),
						PlacesList.class);
				listAllPlaces = arListAllPlaces.getResults();

				ArrayList<String> arrayListAllPlaces = new ArrayList<String>();

				for (int i = 0; i < listAllPlaces.length; i++) {
					arrayListAllPlaces.add(listAllPlaces[i].getName());
				}
				// We need to use an adapter to fill in the view
				ArrayAdapter<String> listAdapte = new ArrayAdapter<String>(
						getApplicationContext(), R.layout.placesrow,
						arrayListAllPlaces);
				listPlaces.setAdapter(listAdapte);
			}
		}

	}
}
