package epsilon.whygosoloapplication.activities.objects;
/*
    Author: Luke Collier
    Description: The list array for events on the profile screen
    Date Created: N/A
    Last Edited by: Luke Colliers
    Date Edited: 07/05/2016
    Edit Note: Added comments
 */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import epsilon.whygosoloapplication.R;

/**
 * Project Application
 * Created by Joker
 * Created on 05/05/16
 */
public class ListEventAdapter extends ArrayAdapter<Event> {
    public ListEventAdapter(Context context, ArrayList<Event> events) {
        super(context, 0, events);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Event event = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_event_small, parent, false);
        }
        // Lookup view for data population
        TextView eventName = (TextView) convertView.findViewById(R.id.text_event_name);
        TextView eventDate = (TextView) convertView.findViewById(R.id.text_event_start_date);
        TextView eventLocation = (TextView) convertView.findViewById(R.id.text_event_location);
        // Populate the data into the template view using the data object
        eventName.setText(event.getName());
        eventDate.setText(event.getName());
        eventLocation.setText(event.getLocation());
        // Return the completed view to render on screen
        return convertView;
    }
}
