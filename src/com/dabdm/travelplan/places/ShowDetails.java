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
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ShowDetails extends Activity {

	private String refPicture;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_details);
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		ImageView imageView = (ImageView) findViewById(R.id.imageView1);
		imageView.setVisibility(View.INVISIBLE);
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

			responseString = request.getPlaceDetails(ref);
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
				TextView tv = (TextView) findViewById(R.id.description_place);
				TextView tv2 = (TextView) findViewById(R.id.info_place);
				tv2.setGravity(Gravity.CENTER_VERTICAL
						| Gravity.CENTER_HORIZONTAL);
				tv.setMovementMethod(new ScrollingMovementMethod());
				tv.setText("\n");
				tv2.append("\n	");
				tv2.setTextSize(35);
				tv2.append(details.result.getRating() + " / 5 ");
				tv2.append("  ");
				int i = 0;

				tv2.append("\n");

				tv.append(Html.fromHtml("<h2> " + details.result.getName()
						+ "\n</h2>"));
				if (details.result.getOpening_hours() != null) {
					for (i = 0; i < details.result.getOpening_hours()
							.getPeriods().length; i++) {

						if (details.result.getOpening_hours().getPeriods()[i]
								.getOpen() != null) {
							switch (details.result.getOpening_hours()
									.getPeriods()[i].getOpen().getDay()) {

							case 1:
								tv.append(Html.fromHtml("<h5> MONDAY     "
										+ details.result.getOpening_hours()
												.getPeriods()[i].getOpen()
												.getTime().substring(0, 2)
										+ ":"
										+ details.result.getOpening_hours()
												.getPeriods()[i].getOpen()
												.getTime().substring(2, 4)
										+ "  "
										+ details.result.getOpening_hours()
												.getPeriods()[i].getClose()
												.getTime().substring(0, 2)
										+ ":"
										+ details.result.getOpening_hours()
												.getPeriods()[i].getClose()
												.getTime().substring(2, 4)
										+ "</h5>\n\n"));
								break;
							case 2:
								tv.append(Html.fromHtml("<h5> TUESDAY     "
										+ details.result.getOpening_hours()
												.getPeriods()[i].getOpen()
												.getTime().substring(0, 2)
										+ ":"
										+ details.result.getOpening_hours()
												.getPeriods()[i].getOpen()
												.getTime().substring(2, 4)
										+ " "
										+ details.result.getOpening_hours()
												.getPeriods()[i].getClose()
												.getTime().substring(0, 2)
										+ ":"
										+ details.result.getOpening_hours()
												.getPeriods()[i].getClose()
												.getTime().substring(2, 4)
										+ "</h5>\n\n"));
								break;
							case 3:
								tv.append(Html.fromHtml("<h5> WEDNESDAY "
										+ details.result.getOpening_hours()
												.getPeriods()[i].getOpen()
												.getTime().substring(0, 2)
										+ ":"
										+ details.result.getOpening_hours()
												.getPeriods()[i].getOpen()
												.getTime().substring(2, 4)
										+ " "
										+ details.result.getOpening_hours()
												.getPeriods()[i].getClose()
												.getTime().substring(0, 2)
										+ ":"
										+ details.result.getOpening_hours()
												.getPeriods()[i].getClose()
												.getTime().substring(2, 4)
										+ "</h5>\n\n"));
								break;
							case 4:
								tv.append(Html.fromHtml("<h5> THURSDAY     "
										+ details.result.getOpening_hours()
												.getPeriods()[i].getOpen()
												.getTime().substring(0, 2)
										+ ":"
										+ details.result.getOpening_hours()
												.getPeriods()[i].getOpen()
												.getTime().substring(2, 4)
										+ " "
										+ details.result.getOpening_hours()
												.getPeriods()[i].getClose()
												.getTime().substring(0, 2)
										+ ":"
										+ details.result.getOpening_hours()
												.getPeriods()[i].getClose()
												.getTime().substring(2, 4)
										+ "</h5>\n\n"));
								break;
							case 5:
								tv.append(Html.fromHtml("<h5> FRIDAY     "
										+ details.result.getOpening_hours()
												.getPeriods()[i].getOpen()
												.getTime().substring(0, 2)
										+ ":"
										+ details.result.getOpening_hours()
												.getPeriods()[i].getOpen()
												.getTime().substring(2, 4)
										+ " "
										+ details.result.getOpening_hours()
												.getPeriods()[i].getClose()
												.getTime().substring(0, 2)
										+ ":"
										+ details.result.getOpening_hours()
												.getPeriods()[i].getClose()
												.getTime().substring(2, 4)
										+ "</h5>\n\n"));
								break;
							case 6:
								tv.append(Html.fromHtml("<h5> SATURDAY     "
										+ details.result.getOpening_hours()
												.getPeriods()[i].getOpen()
												.getTime().substring(0, 2)
										+ ":"
										+ details.result.getOpening_hours()
												.getPeriods()[i].getOpen()
												.getTime().substring(2, 4)
										+ " "
										+ details.result.getOpening_hours()
												.getPeriods()[i].getClose()
												.getTime().substring(0, 2)
										+ ":"
										+ details.result.getOpening_hours()
												.getPeriods()[i].getClose()
												.getTime().substring(2, 4)
										+ "</h5>\n\n"));
								break;
							case 7:
								tv.append(Html.fromHtml("<h5> SUNDAY     "
										+ details.result.getOpening_hours()
												.getPeriods()[i].getOpen()
												.getTime().substring(0, 2)
										+ ":"
										+ details.result.getOpening_hours()
												.getPeriods()[i].getOpen()
												.getTime().substring(2, 4)
										+ " Close:"
										+ details.result.getOpening_hours()
												.getPeriods()[i].getClose()
												.getTime().substring(0, 2)
										+ ":"
										+ details.result.getOpening_hours()
												.getPeriods()[i].getClose()
												.getTime().substring(2, 4)
										+ "</h5>\n\n"));
								break;
							}

						}
					}

				}
				tv.append("\n");
				tv.append(Html.fromHtml("\n\n<H3> Phone number: "
						+ details.result.getInternational_phone_number()
						+ "</H3>"));
				if (details.result.getPhotos() != null) {
					refPicture = details.result.getPhotos()[0]
							.getPhoto_reference();
				} else {
					refPicture = null;
				}
			}

			if (refPicture != null)
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
			ImageView imgView = (ImageView) findViewById(R.id.imageView1);
			imgView.setImageBitmap(image);
			imgView.setVisibility(View.VISIBLE);

		}
	}
	
}
