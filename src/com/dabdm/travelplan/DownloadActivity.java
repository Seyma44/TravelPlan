package com.dabdm.travelplan;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class DownloadActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_download);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_download, menu);
		return true;
	}
	
	public void downloadProject() {
		
	}

}
