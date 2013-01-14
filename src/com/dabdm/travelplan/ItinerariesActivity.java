package com.dabdm.travelplan;

import com.dabdm.travelplan.map.MapActivity;

import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

/**
 * An activity where you can choose one of the existing itineraries or create a new one
 * 
 */
public class ItinerariesActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_itineraries);

	String[] array = { "route1", "route2" };

	ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, array);
	setListAdapter(adapter);

	Button button = (Button) findViewById(R.id.open_map_button);
	button.setOnClickListener(new View.OnClickListener() {
	    @Override
	    public void onClick(View v) {
		// TODO Auto-generated method stub
		//startActivity(new Intent(ItinerariesActivity.this.getApplicationContext(), DestinationActivity.class));
	    }
	});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	// Inflate the menu; this adds items to the action bar if it is present.
	getMenuInflater().inflate(R.menu.activity_itineraries, menu);
	return true;
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
	String item = (String) getListAdapter().getItem(position);
	Intent intent = new Intent(this, MapActivity.class);
	intent.putExtra("name", this.fileList()[position]);
	startActivity(intent);
    }

}
