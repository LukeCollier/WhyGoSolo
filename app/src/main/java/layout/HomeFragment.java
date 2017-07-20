/*
    Author: Luke Collier
    Description: One of the tabs the main activity houses
    Date Created: N/A (I'll look into it)
    Last Edited by: Luke Collier
    Date Edited: 28/04/16
    Edit Note: Added comments
 */

package layout;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import epsilon.whygosoloapplication.R;
import epsilon.whygosoloapplication.activities.json.JsonAdapter;
import epsilon.whygosoloapplication.activities.json.JsonParams;
import epsilon.whygosoloapplication.activities.json.JsonParser;
import epsilon.whygosoloapplication.activities.objects.Event;
import epsilon.whygosoloapplication.activities.objects.RecycleViewAdapterEvents;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


    /**
     * Needs this empty public constructor
     */
    public HomeFragment() {
        // Required empty public constructor
    }

    private View progress;
    private RecyclerView recyclerView;
    private Context context;
    /**
     * Called when the main view is created
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Show a progress bar and update the listings
        progress = view.findViewById(R.id.view_progress);

        // Creates a swipe down list
        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view_events);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        context = view.getContext();

        // Update data
        updateList();

        return view;
    }

    public void updateList() {
        JsonParams params = new JsonParams();
        params.addParam("lat","-30.775");
        params.addParam("long","185.1126");
        progress.setVisibility(View.VISIBLE);
        new UpdateHomeListTask().execute(params.parse());
    }

    public class UpdateHomeListTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {}

        @Override
        protected String doInBackground(String... params)  {
            String message;
            try {
                message = JsonParser.get("eventSearch",params[0]);
            } catch (IOException e) {
                message = "Exception found when refreshing events " + e;
            }

            return message;
        }

        @Override
        protected void onProgressUpdate(Void... values) {}

        @Override
        protected void onPostExecute(String result) {
            Log.v("Home Task Result", result);
            List<Event> events = new ArrayList<>();
            RecycleViewAdapterEvents recycler_adapter;
            try {
                events = JsonAdapter.eventsSearchAdapter(result);
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                recycler_adapter
                        = new RecycleViewAdapterEvents(events,context);
                recyclerView.setAdapter(recycler_adapter);
            }

            progress.setVisibility(View.GONE);
            recycler_adapter.notifyDataSetChanged();
        }
    }
}
