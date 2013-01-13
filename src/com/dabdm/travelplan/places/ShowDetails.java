package com.dabdm.travelplan.places;

import org.json.JSONException;
import org.json.JSONObject;

import com.dabdm.travelplan.R;
import com.dabdm.travelplan.places.GoogleRequests;
import com.dabdm.travelplan.places.PlaceDetailsResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

public class ShowDetails extends Activity {
	
	private String refPicture;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_details);
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		
		new LoadPlaces().execute(extras.getString("placeReference"));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_show_details, menu);
		return true;
	}

	private class LoadPlaces extends AsyncTask<String, Integer, Boolean> { 
		private String responseString = "";

		@Override
		protected Boolean doInBackground(String... params) {
			String ref = params[0];
			GoogleRequests request = new GoogleRequests();

			responseString = request
					.getPlaceDetails(ref);
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

				PlaceDetailsResponse details = gson.fromJson(json.toString(),
						PlaceDetailsResponse.class);
				refPicture = details.result.photos[0].photo_reference;
				Log.i("tes",
						String.valueOf(details.result.opening_hours.periods[0].close.day));
				TextView tv = (TextView) findViewById(R.id.Place);
				tv.setText("\n");
				int i = 0;

				for (i = 0; i < details.result.opening_hours.periods.length; i++) {

					if (details.result.opening_hours.periods[i].open != null) {
						switch (details.result.opening_hours.periods[i].open.day) {

						case 1:
							tv.append("Monday : \nOpen: ");
							tv.append(details.result.opening_hours.periods[i].open.time
									.substring(0, 2)
									+ ":"
									+ details.result.opening_hours.periods[i].open.time
											.substring(2, 4) + "     ");
							tv.append("Close"
									+ details.result.opening_hours.periods[i].close.time
											.substring(0, 2)
									+ ":"
									+ details.result.opening_hours.periods[i].close.time
											.substring(2, 4) + "\n\n");
							break;
						case 2:
							tv.append("Tuesday : \nOpen: ");
							tv.append(details.result.opening_hours.periods[i].open.time
									.substring(0, 2)
									+ ":"
									+ details.result.opening_hours.periods[i].open.time
											.substring(2, 4) + "     ");
							tv.append("Close"
									+ details.result.opening_hours.periods[i].close.time
											.substring(0, 2)
									+ ":"
									+ details.result.opening_hours.periods[i].close.time
											.substring(2, 4) + "\n\n");
							break;
						case 3:
							tv.append("Wednesday : \nOpen: ");
							tv.append(details.result.opening_hours.periods[i].open.time
									.substring(0, 2)
									+ ":"
									+ details.result.opening_hours.periods[i].open.time
											.substring(2, 4) + "     ");
							tv.append("Close"
									+ details.result.opening_hours.periods[i].close.time
											.substring(0, 2)
									+ ":"
									+ details.result.opening_hours.periods[i].close.time
											.substring(2, 4) + "\n\n");
							break;
						case 4:
							tv.append("Thursday : \nOpen: ");
							tv.append(details.result.opening_hours.periods[i].open.time
									.substring(0, 2)
									+ ":"
									+ details.result.opening_hours.periods[i].open.time
											.substring(2, 4) + "     ");
							tv.append("Close: "
									+ details.result.opening_hours.periods[i].close.time
											.substring(0, 2)
									+ ":"
									+ details.result.opening_hours.periods[i].close.time
											.substring(2, 4) + "\n\n");
							break;
						case 5:
							tv.append("Friday : \nOpen: ");
							tv.append(details.result.opening_hours.periods[i].open.time
									.substring(0, 2)
									+ ":"
									+ details.result.opening_hours.periods[i].open.time
											.substring(2, 4) + "     ");
							tv.append("Close: "
									+ details.result.opening_hours.periods[i].close.time
											.substring(0, 2)
									+ ":"
									+ details.result.opening_hours.periods[i].close.time
											.substring(2, 4) + "\n\n");
							break;
						case 6:
							tv.append("Saturday : \nOpen: ");
							tv.append(details.result.opening_hours.periods[i].open.time
									.substring(0, 2)
									+ ":"
									+ details.result.opening_hours.periods[i].open.time
											.substring(2, 4) + "     ");
							tv.append("Close: "
									+ details.result.opening_hours.periods[i].close.time
											.substring(0, 2)
									+ ":"
									+ details.result.opening_hours.periods[i].close.time
											.substring(2, 4) + "\n\n");
							break;
						case 7:
							tv.append("Sunday : \nOpen: ");
							tv.append(details.result.opening_hours.periods[i].open.time
									.substring(0, 2)
									+ ":"
									+ details.result.opening_hours.periods[i].open.time
											.substring(2, 4) + "     ");
							tv.append("Close: "
									+ details.result.opening_hours.periods[i].close.time
											.substring(0, 2)
									+ ":"
									+ details.result.opening_hours.periods[i].close.time
											.substring(2, 4) + "\n\n");
							break;
						}

					}

				}
				tv.append("Phone number: "
						+ details.result.international_phone_number);
			}
			new LoadImage().execute();
		}
	}

	private class LoadImage extends AsyncTask<Object, Object, Boolean> {
		private Bitmap image;

		@Override
		protected Boolean doInBackground(Object... arg0) {
			GoogleRequests request = new GoogleRequests();
			image = request.getPicture(refPicture);
			return true;
		}

		@Override
		protected void onPostExecute(Boolean bool) {
			Log.i("lo", "on charge l'iamge");
			ImageView imgView = (ImageView) findViewById(R.id.PlaceImage);
			imgView.setImageBitmap(image);
		}
	}
}
