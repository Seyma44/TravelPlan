package com.dabdm.travelplan;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.dabdm.travelplan.map.MapActivity;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class DownloadActivity extends Activity {

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
		new DownloadFileTask().execute(fileNameTxt.getText().toString());
	    }
	});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	// Inflate the menu; this adds items to the action bar if it is present.
	getMenuInflater().inflate(R.menu.activity_download, menu);
	return true;
    }

    public void downloadProject() {
	FtpHelper ftp = new FtpHelper();
	File file = new File("path");
	ftp.connect();
	ftp.put(file);
	ftp.close();
    }

    /**
     * AsyncTask to send a new added friend to the server
     */
    private class DownloadFileTask extends AsyncTask<String, Integer, Boolean> {

	@Override
	protected Boolean doInBackground(String... params) {
	    String fileName = params[0];

	    FtpHelper ftp = new FtpHelper();
	    ftp.connect();
	    FileOutputStream outputStream = null;
	    try {
		outputStream = openFileOutput("TRAVELPLAN" + fileName, Context.MODE_PRIVATE);
	    } catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	    ftp.get("TRAVELPLAN" + fileName, outputStream);
	    ftp.close();

	    return true;
	}

	@Override
	protected void onPostExecute(Boolean result) {
	    // TODO Auto-generated method stub
	    super.onPostExecute(result);
	    if (result) {
		// Hide progress bar
		DownloadActivity.this.setProgressBarIndeterminate(false);
		DownloadActivity.this.setProgressBarIndeterminateVisibility(false);
	    }
	}
    }

}
