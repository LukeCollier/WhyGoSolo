/*
    Author: Luke Collier
    Description: Used to show the event object in the UI
    Date Created: N/A (I'll look into it)
    Last Edited by: Luke Collier
    Date Edited: 28/04/16
    Edit Note:
 */

package epsilon.whygosoloapplication.activities;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import epsilon.whygosoloapplication.R;
import epsilon.whygosoloapplication.activities.json.JsonAdapter;
import epsilon.whygosoloapplication.activities.json.JsonParams;
import epsilon.whygosoloapplication.activities.json.JsonParser;
import epsilon.whygosoloapplication.activities.objects.Event;
import epsilon.whygosoloapplication.activities.objects.Session;

public class ViewEventActivity extends AppCompatActivity {

    TextView textName, textLocation, textFromDate, textDescription;
    ProgressDialog progressDialog;
    Button attendButton;

    SimpleDateFormat sdf;

    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(null);

        setContentView(R.layout.activity_view_event);

        sdf = new SimpleDateFormat("cccc dd MMMM yyyy\nHH:mm a",
                this.getResources().getConfiguration().locale);

        attendButton = (Button) findViewById(R.id.button_attend);
        textName = (TextView) findViewById(R.id.text_event_title);
        textLocation = (TextView) findViewById(R.id.text_event_location);
        textFromDate = (TextView) findViewById(R.id.text_event_start_date);
        textDescription = (TextView) findViewById(R.id.text_event_description);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id = extras.getInt("eventId");
            String eventName = extras.getString("eventName");
            String eventLocation = extras.getString("eventLocation");
            String eventFromDate = extras.getString("eventFromDate");
            String eventDescription = extras.getString("eventDescription");

            textName.setText(eventName);
            textLocation.setText(eventLocation);
            textFromDate.setText(eventFromDate);
            textDescription.setText(eventDescription);
        }
    }

    public void onAttendClick(View view) {
        progressDialog = ProgressDialog.show(this, "","Attending Event. Please wait...", true);
        new UserAttendEventTask().execute();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    public class UserAttendEventTask extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(Void... params)  {
            String message = "";
            try {
                JsonParams jsonParams = new JsonParams();
                jsonParams.addParam("userId", String.valueOf(Session.getSessionId()));
                jsonParams.addParam("eventId", String.valueOf(id));
                JsonParser.post("userAttendEvent",jsonParams.parse());
            } catch (IOException e) {
                message = "Exception found when making user attend event task " + e;
            }

            return message;
        }

        @Override
        protected void onProgressUpdate(Void... values) {}

        @Override
        protected void onPostExecute(String result) {
            attendButton.setEnabled(false);
            attendButton.setText("Attending");
            Log.v("View Event Task Result", result);
            progressDialog.dismiss();
        }
    }
}
