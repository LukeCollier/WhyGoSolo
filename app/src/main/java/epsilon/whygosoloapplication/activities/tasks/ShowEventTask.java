/*
    Author: Luke Collier
    Description: A nifty way of making a generic task that will show a event TODO: more tasks like
    this
    Date Created: N/A
    Last Edited by: Luke Colliers
    Date Edited: 07/05/2016
    Edit Note: Added comments
 */
package epsilon.whygosoloapplication.activities.tasks;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;

import epsilon.whygosoloapplication.activities.ViewEventActivity;
import epsilon.whygosoloapplication.activities.json.JsonAdapter;
import epsilon.whygosoloapplication.activities.json.JsonParser;
import epsilon.whygosoloapplication.activities.objects.Event;

/**
 * Project Application
 * Created by Joker
 * Created on 05/05/16
 */
public class ShowEventTask extends AsyncTask<String, Void, String> {
    View view;
    ProgressDialog progressDialog;

    public ShowEventTask(View view) {
        this.view = view;
    }

    @Override
    protected void onPreExecute() {
        progressDialog
                = ProgressDialog.show(view.getContext(), "","Finding Event. Please wait...", true);
    }

    /**
     * Gets event information
     * @param params the event id to get
     * @return the JSON file with the information in
     */
    @Override
    protected String doInBackground(String... params)  {
        String message;
        try {
            message = JsonParser.get("events",params[0]);
        } catch (IOException e) {
            message = "Exception found when posting to view event " + e;
        }

        return message;
    }

    @Override
    protected void onProgressUpdate(Void... values) {}

    /***
     * Open the view with the information
     * @param result the servers JSON file
     */
    @Override
    protected void onPostExecute(String result) {
        try {
            Event event = JsonAdapter.eventAdapter(new JSONObject(result));
            Intent intent = new Intent(view.getContext(), ViewEventActivity.class);

           SimpleDateFormat sdf = new SimpleDateFormat("cccc dd MMMM yyyy\nHH:mm a",
                    view.getResources().getConfiguration().locale);

            intent.putExtra("eventId",event.getId());
            intent.putExtra("eventName",event.getName());
            intent.putExtra("eventLocation",event.getLocation());
            intent.putExtra("eventFromDate",sdf.format(event.getFromCalendar().getTime()));
            intent.putExtra("eventDescription",event.getDescription());

            view.getContext().startActivity(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.v("View Event Task Result", result);
        progressDialog.dismiss();
    }
}
