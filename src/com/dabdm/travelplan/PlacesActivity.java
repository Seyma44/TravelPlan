package com.dabdm.travelplan;


import android.annotation.TargetApi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.dabdm.travelplan.R.color;
import com.dabdm.travelplan.places.GoogleRequests;
import com.dabdm.travelplan.places.Place;
import com.dabdm.travelplan.places.PlaceDetailsResponse;
import com.dabdm.travelplan.places.PlacesList;
import com.dabdm.travelplan.places.ShowDetails;
//import com.dabdm.travelplan.places.PlacesListActivity.LoadPlaces;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentTransaction;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabContentFactory;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

public class PlacesActivity extends FragmentActivity { 

		

	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * current dropdown position.
	 */
	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";
	private ListView listPlaces;
	private ArrayAdapter<String> listAdapter ; 
	private ListView lvPlaces;
	private Place[] listAllPlaces;
	private Travel travel;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_places);
		listPlaces = (ListView) findViewById( R.id.listplaces );
//	    String[] planets = new String[] { "Mercury", "Venus", "Earth", "Mars",  
//                "Jupiter", "Saturn", "Uranus", "Neptune"};
//	    ArrayList<String> planetList = new ArrayList<String>();
//	    planetList.addAll( Arrays.asList(planets) );  
//	      
//	    ArrayList<String> placesList = new ArrayList<String>();
	    
	    
	    
	    // Create ArrayAdapter using the planet list.  
	//    listAdapter = new ArrayAdapter<String>(this, R.layout.placesrow, planetList);
	//    listPlaces.setAdapter(listAdapter);
		
	    travel = (Travel)getIntent().getSerializableExtra("travel");
	    new LoadPlaces().execute(travel.getLat(), travel.getLng(), (double)travel.getRadius());
	     lvPlaces = (ListView)findViewById(R.id.listplaces);
	     
	    lvPlaces.setSelector(R.drawable.listview);
	    lvPlaces.setLongClickable(true);
	    // listPlaces.getChil
	     
//			TextView item = (TextView)lvPlaces.getChildAt(2);
//			item.setBackgroundColor(color.AliceBlue);
	     
	     
	    
	    lvPlaces.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
		    	//AdapterView listView = (AdapterView)arg1;
		    	//long selectedId = listView.getSelectedItemId();
		    	//Log.d("aaa", String.valueOf(selectedId));
		    	Log.d("aaa2", String.valueOf(arg2));
		    	Log.d("aaa3", String.valueOf(arg3));
		    	//changeColor(arg2);
		    	//Integer a = lvPlaces.getCheckedItemCount();
		    	Log.d("test", String.valueOf(listAllPlaces.length));
		    	Log.d("test", listAllPlaces[arg2].getName());
		    	Place chosenPlace = listAllPlaces[arg2];
		    	if (travel.getPlaces().contains(chosenPlace)) {
		    		travel.getPlaces().remove(chosenPlace);
		    	}
		    	else {
		    		travel.getPlaces().add(chosenPlace);
		    	}		    	
		    	int i;
		    	for (i=0;i<travel.getPlaces().size();i++) {
		    		Log.d("test", travel.getPlaces().get(i).getName());
		    	}
		    	//Log.d("aaa", a.toString());
			}
		});
	    lvPlaces.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				
				Log.d("yeah", String.valueOf(arg2));
				Intent i = new Intent(getBaseContext(), ShowDetails.class);
				i.putExtra("placeReference",listAllPlaces[arg2].getReference());
				startActivity(i);
				return true;
			}
		});
	    
		// Set up the action bar to show a dropdown list.
//		final ActionBar actionBar = getActionBar();
//		actionBar.setDisplayShowTitleEnabled(false);
//		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);	  


//		// Set up the dropdown list navigation in the action bar.
//		actionBar.setListNavigationCallbacks(
//		// Specify a SpinnerAdapter to populate the dropdown list.
//				new ArrayAdapter<String>(getActionBarThemedContextCompat(),
//						android.R.layout.simple_list_item_1,
//						new String[] {
//								getString(R.string.title_section1),
//								getString(R.string.title_section2),
//								getString(R.string.title_section3), }), this);
	}

	private void changeColor(int position)
	{
		Log.d("debug", String.valueOf(lvPlaces.getFirstVisiblePosition()));
		TextView item = (TextView) lvPlaces.getChildAt(position+lvPlaces.getFirstVisiblePosition());
		item.setBackgroundColor(color.AliceBlue);
		
	}
	
	/**
	 * Backward-compatible version of {@link ActionBar#getThemedContext()} that
	 * simply returns the {@link android.app.Activity} if
	 * <code>getThemedContext</code> is unavailable.
	 */
//	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
//	private Context getActionBarThemedContextCompat() {
//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
//			return getActionBar().getThemedContext();
//		} else {
//			return this;
//		}
//	}

//	@Override
//	public void onRestoreInstanceState(Bundle savedInstanceState) {
		// Restore the previously serialized current dropdown position.
//		if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
//			getActionBar().setSelectedNavigationItem(
//					savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
//		}
//	}

//	@Override
//	public void onSaveInstanceState(Bundle outState) {
//		// Serialize the current dropdown position.
//		outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, getActionBar()
//				.getSelectedNavigationIndex());
//	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_places, menu);
		return true;
	}
	@Override
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
//	@Override
//	public boolean onNavigationItemSelected(int position, long id) {
//		// When the given dropdown item is selected, show its contents in the
//		// container view.
//		Fragment fragment = new DummySectionFragment();
//		Bundle args = new Bundle();
//		args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
//		fragment.setArguments(args);
//		getSupportFragmentManager().beginTransaction()
//				.replace(R.id.container, fragment).commit();
//		return true;
//	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class DummySectionFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

		public DummySectionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			// Create a new TextView and set its text to the fragment's section
			// number argument value.
			
			return null;
		}
	}


    /**
     * AsyncTask used to loaded a list of interesting places
     */
    private class LoadPlaces extends AsyncTask<Double, Integer, Boolean> {
	private String	      responseString     = "";

	@Override
	protected Boolean doInBackground(Double... params) {
		GoogleRequests request = new GoogleRequests();
		double x = params[0];
		double y =  params[1];
		responseString = request.getPlaces(x, y, 5000);
		
		return responseString==null ? false : true;
	}

	@Override
	protected void onPostExecute(Boolean result) {
	    super.onPostExecute(result);
	    if (result) {
		if (responseString.equals("null")) { return; }

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
		/*PlacesList places = gson.fromJson(json.toString(), PlacesList.class);
		Log.i("lol",responseString);
		//Log.i("test", json.toString());
		for(int i = 0; i < places.getResults().length; i++) {
		    Log.i("test", places.getResults()[i].getName());
		    }
		*/
		
		PlacesList arListAllPlaces = gson.fromJson(json.toString(), PlacesList.class);
		listAllPlaces = arListAllPlaces.getResults();
		
		ArrayList<String> arrayListAllPlaces = new ArrayList<String>();
		
		for(int i = 0; i < listAllPlaces.length; i++) {
			arrayListAllPlaces.add(listAllPlaces[i].getName());
		}
		ArrayAdapter<String> listAdapte = new ArrayAdapter<String>(getApplicationContext(), R.layout.placesrow,arrayListAllPlaces);
	    listPlaces.setAdapter(listAdapte);
	    }
	}
    

}
}
