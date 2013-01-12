package com.dabdm.travelplan;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.dabdm.travelplan.map.MapActivity;
import com.dabdm.travelplan.places.PlacesListActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Button buttonNext = (Button) findViewById(R.id.button1);
		buttonNext.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// on click, start the Map activity
				// startActivity(new Intent(MainActivity.this, MapActivity.class));
				startActivity(new Intent(MainActivity.this,PlacesListActivity.class));
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
