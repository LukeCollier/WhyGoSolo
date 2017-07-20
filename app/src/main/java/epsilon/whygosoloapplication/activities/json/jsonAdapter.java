/*
    Author: Luke Collier
    Description: This will convert the other teams json objects into friendlier java objects
    Date Created: 28/04/16
    Last Edited by: Luke Collier
    Date Edited: 28/04/16
    Edit Note:
 */

package epsilon.whygosoloapplication.activities.json;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import epsilon.whygosoloapplication.R;
import epsilon.whygosoloapplication.activities.objects.Event;

/**
 * Created by Joker on 28/04/16.
 */
public class JsonAdapter {
    /**
     * Maps a json array of events to a events list.
     * @param jsonString the string to map
     * @return a list of events
     * @throws JSONException
     */
    public static List<Event> eventsSearchAdapter(String jsonString) throws JSONException {
        List<Event> events = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(jsonString);
        for (int i=0;i<jsonArray.length();i++) {
            JSONObject jsonObject = new JSONObject(jsonArray.get(i).toString());
            Event event = eventAdapter(jsonObject);
            events.add(event);
        }
        return events;
    }

    /**
     * Adapts an event from json to a java object
     * @param jsonObject the json object to be converted
     * @return the event object
     * @throws JSONException
     */
    public static Event eventAdapter(JSONObject jsonObject) throws JSONException {
        Event event = new Event();
        event.setId(jsonObject.getInt("id"));
        event.setName(jsonObject.getString("name"));
        event.setDescription(jsonObject.getString("description"));
        event.setLocation(jsonObject.getJSONObject("location").getString("name"));
        event.setFromCalendar(new Date(jsonObject.getLong("from")));
        event.setToCalendar(new Date(jsonObject.getLong("to")));
        event.setPictureId(R.mipmap.ic_launcher);
        event.setCategory("");
        return event;
    }
}
