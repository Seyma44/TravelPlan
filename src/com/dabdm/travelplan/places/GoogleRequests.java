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

import android.util.Log;

public class GoogleRequests {
	private static final int    CONNECTION_TIMEOUT = 10000;
	private static final String KEY = "AIzaSyANFlZRQPupu3KWGqF8vAU2JvaTIom8ofM";
	private static final String BASE_URL_PLACES	   = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?key=" + KEY + "&";
	private static final String BASE_URL_DETAILS	   = "https://maps.googleapis.com/maps/api/place/details/json?key=" + KEY + "&";

	
	public String getPlaces(double x, double y,int radius) {
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		pairs.add(new BasicNameValuePair("location", String.valueOf(x) + "," + String.valueOf(y)));
		pairs.add(new BasicNameValuePair("radius", String.valueOf(radius))); // in meters
		pairs.add(new BasicNameValuePair("sensor", "true"));
		return httpRequest(pairs, BASE_URL_PLACES);
	}
	public String getPlaceDetails(String reference) {
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		pairs.add(new BasicNameValuePair("reference", reference));
		pairs.add(new BasicNameValuePair("sensor", "true"));
		return httpRequest(pairs, BASE_URL_DETAILS);
		
	}
	public String httpRequest(List<NameValuePair> pairs, String url) {
		HttpResponse response = null;

		BasicHttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams,
				CONNECTION_TIMEOUT);
		HttpConnectionParams.setSoTimeout(httpParams, CONNECTION_TIMEOUT);

		HttpClient client = new DefaultHttpClient();
		
		HttpGet request = new HttpGet(url
				+ URLEncodedUtils.format(pairs, "utf-8"));
		Log.i("yeah",request.getURI().toString());
		request.setHeader("Accept", "application/json");
		request.setParams(httpParams);
		try {
			response = client.execute(request);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		HttpEntity entity = response.getEntity();
		if (entity != null) {
			try {
				return EntityUtils.toString(entity);
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				return null;
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				return null;
			}
		}
		return null;
	}
}
