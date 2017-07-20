/*
    Author: Luke Collier
    Description: The search activity that's real pretty
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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

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
import epsilon.whygosoloapplication.activities.utility.Validation;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {


    public SearchFragment() {
        // Required empty public constructor
    }

    // A few variables that's scope needed to be class wide
    RecyclerView recyclerView;
    Context context;
    View progressView;
    EditText searchBox;
    AsyncTask task = null;
    Spinner spinnerDistance;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        context = view.getContext();

        progressView = view.findViewById(R.id.view_progress);
        searchBox = (EditText) view.findViewById(R.id.text_search_box);

        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view_events);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        // Sets up the spinner
        spinnerDistance = (Spinner) view.findViewById(R.id.spinner_distance);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterDistance = ArrayAdapter.createFromResource(this.getContext(),
                R.array.array_distances_options, R.layout.spinner_item_search);
        // Specify the layout to use when the list of choices appears
        adapterDistance.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        assert spinnerDistance != null; spinnerDistance.setAdapter(adapterDistance);

        // Sets up the spinner
        Spinner spinnerCategory = (Spinner) view.findViewById(R.id.spinner_category);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterCategory = ArrayAdapter.createFromResource(this.getContext(),
                R.array.array_categories, R.layout.spinner_item_search);
        // Specify the layout to use when the list of choices appears
        adapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        assert spinnerCategory != null; spinnerCategory.setAdapter(adapterCategory);

        assert searchBox != null;
        /**
         * Everytime the user types update the data that is shown
         */
        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                updateList();
            }
        });

        return view;
    }

    /**
     * TODO: Break this down to reusable snippets
     * Updates the list using an async task with parameters from the search
     */
    public void updateList() {
        progressView.setVisibility(View.VISIBLE);
        TextView text = (TextView) progressView.findViewById(R.id.text_progress);
        text.setVisibility(View.VISIBLE);
        if (validateSearch()) {
            JsonParams params = new JsonParams();
            params.addParam("lat", "-30.775");
            params.addParam("long", "185.1126");
            params.addParam("name", searchBox.getText().toString());
            params = getRadius(params);

            progressView.findViewById(R.id.progress_bar).setVisibility(View.VISIBLE);
            text.setText(context.getResources().getText(R.string.message_events_loading));

            if (task != null) {
                task.cancel(true);
            }
            task = new SearchTask().execute(params.parse());
        } else {
            if (task!=null) task.cancel(true);
            progressView.findViewById(R.id.progress_bar).setVisibility(View.GONE);
            text.setText(context.getResources().getText(R.string.message_events_search));
            List<Event> events = new ArrayList<>();
            RecycleViewAdapterEvents recycler_adapter = new RecycleViewAdapterEvents(events,context);
            recyclerView.setAdapter(recycler_adapter);
        }
    }

    public JsonParams getRadius(JsonParams params) {
        Log.v("HONK", String.valueOf(spinnerDistance.getSelectedItemPosition()));
        switch (spinnerDistance.getSelectedItemPosition()) {
            case 0: break;
            case 1: params.addParam("radius","20");
                break;
            case 2: params.addParam("radius","50");
                break;
            case 3: break;
        }
        return params;
    }

    /**
     * Called when no results were found set the view to look as required
     */
    public void noResultsFound() {
        progressView.setVisibility(View.VISIBLE);
        TextView text = (TextView) progressView.findViewById(R.id.text_progress);
        text.setVisibility(View.VISIBLE);
        progressView.findViewById(R.id.progress_bar).setVisibility(View.GONE);
        text.setText(context.getResources().getText(R.string.error_no_results));
    }

    /**
     * A private helper method that validates the search box
     * @return false if the search box is empty
     */
    private boolean validateSearch() {
        return !Validation.isEmpty(searchBox);
    }

    /**
     * The search task performs a search
     */
    public class SearchTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {}

        /**
         * Do this while other things are happening on the screen preventing hanging
         * @param params a varargs of parameters passed through [0] is the JsonParser paramaters
         * @return the result of the get method
         */
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

        /**
         * When the search task executes update the recycler view with the parsed json data
         * @param result the result
         */
        @Override
        protected void onPostExecute(String result) {
            Log.v("Search Task Result", result);
            List<Event> events = new ArrayList<>();
            RecycleViewAdapterEvents recycler_adapter;
            try {
                events = JsonAdapter.eventsSearchAdapter(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            recycler_adapter = new RecycleViewAdapterEvents(events,context);
            recyclerView.setAdapter(recycler_adapter);

            if (events.isEmpty()) {
                noResultsFound();
            } else {
                progressView.setVisibility(View.GONE);
            }

            recycler_adapter.notifyDataSetChanged();
        }
    }
}
