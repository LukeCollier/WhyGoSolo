/*
    Author: Odimmaa and Ajay Kumar
    Description: The profile aspect of the main page
    Date Created: N/A (I'll look into it)
    Last Edited by: Luke Collier
    Date Edited: 28/04/16
    Edit Note: Added comments
 */

package layout;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import epsilon.whygosoloapplication.R;
import epsilon.whygosoloapplication.activities.json.JsonAdapter;
import epsilon.whygosoloapplication.activities.json.JsonParams;
import epsilon.whygosoloapplication.activities.json.JsonParser;
import epsilon.whygosoloapplication.activities.objects.Event;
import epsilon.whygosoloapplication.activities.objects.ListEventAdapter;
import epsilon.whygosoloapplication.activities.objects.Session;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {


    public ProfileFragment() {
        // Required empty public constructor
    }

    ImageView userProfilePicture;
    TextView userFullName,userBio,userBase,userEmail;
    ListView eventList;
    View view;


    /**
     * Used when the fragment is created
     * @param inflater the inflater used to inflate the layout
     * @param container the container the fragment is in
     * @param savedInstanceState the previous information saved for it
     * @return a fragment
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        this.view = view;

        userFullName = (TextView) view.findViewById(R.id.text_user_full_name);
        userBio = (TextView) view.findViewById(R.id.text_user_bio);
        userBase = (TextView) view.findViewById(R.id.text_user_base);
        userEmail = (TextView) view.findViewById(R.id.text_user_contact_email);
        userProfilePicture = (ImageView) view.findViewById(R.id.image_profile_picture);
        eventList = (ListView) view.findViewById(R.id.event_list);

        new GetCurrentUserProfileTask().execute();
        //new GetCurrentUserEventsTask().execute();

        return view;
    }

    /**
     * TODO: Migrate this class to login and store information in Session
     */
    public class GetCurrentUserProfileTask extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
        }

        /**
         * Get all the profile information for the profile
         * @param values void
         * @return the profile information
         */
        @Override
        protected String doInBackground(Void... values) {
            String message;
            try {
                JsonParams params = new JsonParams();
                params.addParam("userId",String.valueOf(Session.getSessionId()));
                params.addParam("currUserId",String.valueOf(Session.getSessionId()));
                message = JsonParser.get("user", params.parse());
            } catch (IOException e) {
                message = "Exception found " + e;
            }

            return message;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }

        /**
         * Update the view to reflect the data acquired by the get
         * @param result the response from the server
         */
        @Override
        protected void onPostExecute(String result) {
            Log.v("Session Id Task Result", result);

            try {
                JSONObject jsonObject = new JSONObject(result);
                Log.v("Session Id Task Result", jsonObject.toString());
                String firstName = jsonObject.getString("firstName");
                String secondName = jsonObject.getString("lastName");
                userFullName.setText(firstName+" "+secondName);
                userBio.setText(jsonObject.getString("biography"));
                userEmail.setText(jsonObject.getString("email"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * TODO: Fix this at some point maybe or don't I'm not your boss
     */
    public class GetCurrentUserEventsTask extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(Void... values) {
            String message;
            try {
                JsonParams params = new JsonParams();
                params.addParam("userId",String.valueOf(Session.getSessionId()));
                message = JsonParser.get("userAttendEventByUser", params.parse());
            } catch (IOException e) {
                message = "Exception found when finding events task " + e;
            }

            return message;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }

        @Override
        protected void onPostExecute(String result) {
            Log.v("Session Id Task Result", result);

            try {
                ArrayList<Event> events = (ArrayList<Event>) JsonAdapter.eventsSearchAdapter(result);
                ListEventAdapter adapter = new  ListEventAdapter(view.getContext(), events);
                eventList.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
