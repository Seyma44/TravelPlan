package com.dabdm.travelplan;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.NumberPicker;

import com.dabdm.travelplan.PlacesActivity.DummySectionFragment;
import com.dabdm.travelplan.map.MapActivity;


public class DestinationActivity extends FragmentActivity implements
		ActionBar.OnNavigationListener {
	int value = 1;

	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * current dropdown position.
	 */
	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_destination);
		String[] strings = getResources().getStringArray(R.array.action_list);
		// Set up the action bar to show a dropdown list.
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		createDaysPicker();
		createRadiusPicker();

		
		Button button = (Button) findViewById(R.id.button_next);
        button.setOnClickListener(new View.OnClickListener() {@Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
        		startActivity(new Intent(DestinationActivity.this.getApplicationContext(), PlacesActivity.class));
            }
        });
		// Set up the dropdown list navigation in the action bar.
		actionBar.setListNavigationCallbacks(
		// Specify a SpinnerAdapter to populate the dropdown list.
				new ArrayAdapter<String>(getActionBarThemedContextCompat(),
						android.R.layout.simple_list_item_1,
						strings), this);
	}

	/**
	 * Backward-compatible version of {@link ActionBar#getThemedContext()} that
	 * simply returns the {@link android.app.Activity} if
	 * <code>getThemedContext</code> is unavailable.
	 */
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	private Context getActionBarThemedContextCompat() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			return getActionBar().getThemedContext();
		} else {
			return this;
		}
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		// Restore the previously serialized current dropdown position.
		if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
			getActionBar().setSelectedNavigationItem(
					savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// Serialize the current dropdown position.
		outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, getActionBar()
				.getSelectedNavigationIndex());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_destination, menu);
		return true;
	}
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
	@Override
	public boolean onNavigationItemSelected(int position, long id) {
		// When the given dropdown item is selected, show its contents in the
		// container view.
		Fragment fragment = new DummySectionFragment();
		Bundle args = new Bundle();
		args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
		fragment.setArguments(args);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.container, fragment).commit();
		return true;
	  }
	

	

	public int createDaysPicker(){
		int value = 0;
		final Context context = this;
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.activity_picker, null);
        AlertDialog.Builder renamedialog = new AlertDialog.Builder(context);
        renamedialog.setTitle(R.string.duration_text);
        renamedialog.setView(promptsView);
     
        
        int minValue = 1;
        int maxValue = 60;
        int currentValue = 3;
        final NumberPicker uiCapacity = (NumberPicker) promptsView.findViewById(R.id.picker);
        uiCapacity.setMinValue(minValue);
        uiCapacity.setMaxValue(maxValue);
        uiCapacity.setValue(currentValue);
        
        renamedialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {}
        });
        renamedialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to invoke NO event
                dialog.cancel();
            }
        });
        final AlertDialog alertdialog = renamedialog.create();
        Button button = (Button) findViewById(R.id.open_pick_days);
        button.setOnClickListener(new View.OnClickListener() {@Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                alertdialog.show();
            }
        });
		return value;
	}
	
	
	public int createRadiusPicker(){
		int value = 0;
		final Context context = this;
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.activity_picker, null);
        AlertDialog.Builder renamedialog = new AlertDialog.Builder(context);
        renamedialog.setTitle(R.string.select_diametre_label);
        renamedialog.setView(promptsView);
     
        
        int minValue = 1;
        int maxValue = 2000;
        int currentValue = 5;
        final NumberPicker uiCapacity = (NumberPicker) promptsView.findViewById(R.id.picker);
        uiCapacity.setMinValue(minValue);
        uiCapacity.setMaxValue(maxValue);
        uiCapacity.setValue(currentValue);
        
        renamedialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {}
        });
        renamedialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to invoke NO event
                dialog.cancel();
            }
        });
        final AlertDialog alertdialog = renamedialog.create();
        Button button = (Button) findViewById(R.id.open_pick_radius);
        button.setOnClickListener(new View.OnClickListener() {@Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                alertdialog.show();
            }
        });
		return value;
	}
	
	

}
