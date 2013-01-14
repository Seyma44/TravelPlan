package com.dabdm.travelplan;

import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ItinerariesActivity extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_itineraries);
		
	        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
	            android.R.layout.simple_list_item_1, this.fileList());
	        setListAdapter(adapter);
	        
	        //Storage.testSerialize(getFilesDir(), "aaa");
	        //Storage.testDeserialize(getFilesDir(), "aaa");
//	        testSerialize();
//	        testDeserialize();
	        
	}
	
	  @Override
	  protected void onListItemClick(ListView l, View v, int position, long id) {
	    String item = (String) getListAdapter().getItem(position);

	//	Intent intent = new Intent(this, TestActivity.class);
	//	intent.putExtra("name", this.fileList()[position]);
	//	startActivity(intent);
	  }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_itineraries, menu);

		return true;
	}

}
