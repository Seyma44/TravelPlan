package com.dabdm.travelplan;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.dabdm.travelplan.map.MapActivity;

public class DownloadActivity extends FragmentActivity {
    
    public static String fileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
	setContentView(R.layout.activity_download);

	final Button buttonDownload = (Button) findViewById(R.id.button_download);
	buttonDownload.setOnClickListener(new View.OnClickListener() {
	    public void onClick(View v) {
		// on click, download the file

		// Show progress bar
		DownloadActivity.this.setProgressBarIndeterminate(true);
		DownloadActivity.this.setProgressBarIndeterminateVisibility(true);

		final EditText fileNameTxt = (EditText) findViewById(R.id.get_project_to_download);
		fileName = fileNameTxt.getText().toString();
		new DownloadFileTask().execute();
	    }
	});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	// Inflate the menu; this adds items to the action bar if it is present.
	getMenuInflater().inflate(R.menu.activity_download, menu);
	return true;
    }

    /**
     * AsyncTask to send a new added friend to the server
     */
    private class DownloadFileTask extends AsyncTask<String, Integer, Boolean> {

	@Override
	protected Boolean doInBackground(String... params) {

	    FtpHelper ftp = new FtpHelper();
	    ftp.connect();
	    FileOutputStream outputStream = null;
	    try {
		outputStream = openFileOutput("TRAVELPLAN" + fileName, Context.MODE_PRIVATE);
	    } catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	    boolean result = ftp.get("TRAVELPLAN" + fileName, outputStream);
	    ftp.close();

	    return result;
	}

	@Override
	protected void onPostExecute(Boolean result) {
	    // TODO Auto-generated method stub
	    super.onPostExecute(result);
	    if (result) {
		// Hide progress bar
		DownloadActivity.this.setProgressBarIndeterminate(false);
		DownloadActivity.this.setProgressBarIndeterminateVisibility(false);
		final EditText fileNameTxt = (EditText) findViewById(R.id.get_project_to_download);
		fileNameTxt.setText("");
		new dlEndDialogFragment().show(getSupportFragmentManager(), ":-)");
	    }
	}
    }
    
    /**
     * Class for the dialog used to inform the player about the end of the download
     */
    public static class dlEndDialogFragment extends DialogFragment {

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	    
	    // Use the Builder class for convenient dialog construction
	    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

	    builder.setMessage(getString(R.string.dl_dialog_msg)).setPositiveButton(getString(R.string.dl_dialog_button1), new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int id) {
		    // Start map activity
		    Intent intent = new Intent(getActivity(), MapActivity.class);
		    intent.putExtra("travelFileName", fileName);
		    startActivity(intent);
		}
	    });

	    builder.setNegativeButton(getString(R.string.dl_dialog_button2), new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int id) {
			// close this activity
			getActivity().finish();
		    }
		});

	    // Create the AlertDialog object and return it
	    return builder.create();
	}
    }

}
