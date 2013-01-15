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

public class PlacesActivity extends FragmentActivity {

	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * current dropdown position.
	 */
	private ListView listPlaces;
	private ListView lvPlaces;
	private Place[] listAllPlaces;
	private Travel travel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_places);
		listPlaces = (ListView) findViewById(R.id.listplaces);

		travel = (Travel) getIntent().getSerializableExtra("travel");
		new LoadPlaces().execute(travel.getLat(), travel.getLng(),
				(double) travel.getRadius());
		lvPlaces = (ListView) findViewById(R.id.listplaces);

		lvPlaces.setSelector(R.drawable.listview);
		lvPlaces.setLongClickable(true);

		Button button = (Button) findViewById(R.id.button_next_places);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getBaseContext(), MapActivity.class);
				travel.getItineraries().clear();
				StorageHelper.saveTravelObject(getFilesDir(),
						travel.getTravelName(), travel);
				i.putExtra("travelFileName", travel.getTravelName());
				startActivity(i);
			}
		});

		lvPlaces.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Place chosenPlace = listAllPlaces[arg2];
				int i = 0;
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

	@Override
	protected void onPause() {
		super.onPause();
		StorageHelper.saveTravelObject(getFilesDir(), travel.getTravelName(),
				travel);

	}

	@Override
	protected void onResume() {
		super.onResume();
		travel = StorageHelper.getTravelObject(getFilesDir(),
				travel.getTravelName());
	}

	/**
	 * AsyncTask used to loaded a list of interesting places
	 */
	private class LoadPlaces extends AsyncTask<Double, Integer, Boolean> {
		private String responseString = "";

		@Override
		protected Boolean doInBackground(Double... params) {
			GoogleRequests request = new GoogleRequests();
			double x = params[0];
			double y = params[1];
			responseString = request.getPlaces(x, y, 5000);

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
				/*
				 * PlacesList places = gson.fromJson(json.toString(),
				 * PlacesList.class); Log.i("lol",responseString);
				 * //Log.i("test", json.toString()); for(int i = 0; i <
				 * places.getResults().length; i++) { Log.i("test",
				 * places.getResults()[i].getName()); }
				 */

				PlacesList arListAllPlaces = gson.fromJson(json.toString(),
						PlacesList.class);
				listAllPlaces = arListAllPlaces.getResults();

				ArrayList<String> arrayListAllPlaces = new ArrayList<String>();

				for (int i = 0; i < listAllPlaces.length; i++) {
					arrayListAllPlaces.add(listAllPlaces[i].getName());
				}
				ArrayAdapter<String> listAdapte = new ArrayAdapter<String>(
						getApplicationContext(), R.layout.placesrow,
						arrayListAllPlaces);
				listPlaces.setAdapter(listAdapte);
			}
		}

	}
}
