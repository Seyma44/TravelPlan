package com.dabdm.travelplan.places;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.dabdm.travelplan.R;
import com.dabdm.travelplan.R.layout;
import com.dabdm.travelplan.R.menu;
import com.dabdm.travelplan.map.Directions;
import com.google.android.gms.maps.model.Polyline;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.util.Log;
import android.view.Menu;

/**
 * Activity showing the list of available places to visit
 */
public class PlacesListActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_places_list);
	new LoadPlaces().execute("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	// Inflate the menu; this adds items to the action bar if it is present.
	getMenuInflater().inflate(R.menu.activity_places_list, menu);
	return true;
    }
    
    /**
     * AsyncTask used to loaded a list of interesting places
     */
    private class LoadPlaces extends AsyncTask<String, Integer, Boolean> {
	private String	      responseString     = "";

	@Override
	protected Boolean doInBackground(String... params) {
		GoogleRequests request = new GoogleRequests();
		double x = -33.8670522;
		double y =  151.1957362;
		//responseString = request.getPlaces(x, y, 2000);
		responseString = request.getPlaceDetails("CmRYAAAAciqGsTRX1mXRvuXSH2ErwW-jCINE1aLiwP64MCWDN5vkXvXoQGPKldMfmdGyqWSpm7BEYCgDm-iv7Kc2PF7QA7brMAwBbAcqMr5i1f4PwTpaovIZjysCEZTry8Ez30wpEhCNCXpynextCld2EBsDkRKsGhSLayuRyFsex6JA6NPh9dyupoTH3g");
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
		
		PlaceDetailsResponse details = gson.fromJson(json.toString(), PlaceDetailsResponse.class);
		Log.i("tes", String.valueOf(details.result.getOpening_hours().getPeriods()[0].getClose().getDay()));
	    }
	}
    }

}
