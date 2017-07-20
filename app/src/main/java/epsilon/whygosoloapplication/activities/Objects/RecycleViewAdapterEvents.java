/*
    Author: Luke Collier
    Description: Recycle view adapter for events
    Date Created: N/A
    Last Edited by: Luke Colliers
    Date Edited: 07/05/2016
    Edit Note: Added comments
 */
package epsilon.whygosoloapplication.activities.objects;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import epsilon.whygosoloapplication.R;
import epsilon.whygosoloapplication.activities.ViewEventActivity;
import epsilon.whygosoloapplication.activities.json.JsonParams;
import epsilon.whygosoloapplication.activities.tasks.ShowEventTask;

/**
 * Created by Joker on 16/02/16.
 */
public class RecycleViewAdapterEvents
        extends RecyclerView.Adapter<RecycleViewAdapterEvents.EventViewHolder>{
    List<Event> events;
    Context context;

    public RecycleViewAdapterEvents(List<Event> events, Context context){
        this.events = events;
        this.context = context;
    }

    /**
     * Called when the view holder is created
     * @param viewGroup the group of views that are parents to this view
     * @param i the current value of the generated holder
     * @return a view holder
     */
    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view_event_home, viewGroup, false);
        return new EventViewHolder(view);
    }

    /**
     * Called when the holder is bound to the RecyclerView
     * @param holder the holder to bind
     * @param position the position to bind it to
     */
    @Override
    public void onBindViewHolder(EventViewHolder holder, int position) {
        Event event = events.get(position);
        holder.eventName.setText(event.getName());
        Resources res = context.getResources();

        holder.setEvent(event);

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", res.getConfiguration().locale);
        SimpleDateFormat stf = new SimpleDateFormat("HH:mm a", res.getConfiguration().locale);

        String formattedInfo
                = String.format(res.getString(R.string.message_event_formatted),
                "Location",event.getLocation(),
                "Date",sdf.format(event.getFromCalendar().getTime()),
                "Time",stf.format(event.getFromCalendar().getTime())
        );
        holder.eventInfo.setText(formattedInfo);
        holder.eventPhoto.setImageResource(event.getPictureId());
    }

    /**
     * Called when the recycler is attached to the view
     * @param recyclerView the recycler view to attach to
     */
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    /**
     * Get the size of the adapter
     * @return the size of the events
     */
    @Override
    public int getItemCount() {
        if (events.isEmpty()) return 0;
        return events.size();
    }

    /**
     * A simple object to hold the information used by the view
     */
    public static class EventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView cardView;
        TextView eventName;
        TextView eventInfo;
        ImageView eventPhoto;

        Event holderEvent;

        EventViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView)itemView.findViewById(R.id.card_view_events);
            eventName = (TextView)itemView.findViewById(R.id.text_event_name);
            eventInfo = (TextView)itemView.findViewById(R.id.text_event_info);
            eventPhoto = (ImageView)itemView.findViewById(R.id.image_event);
            itemView.setOnClickListener(this);
        }

        /**
         * Sets the event of the holder
         * @param event the event held
         */
        public void setEvent(Event event) {
            holderEvent = event;
        }

        /**
         * Opens up the event view activity when one is selected with the right event
         * @param view the view calling the click event
         */
        @Override
        public void onClick(View view) {
            ShowEventTask showEventTask = new ShowEventTask(view);
            JsonParams params = new JsonParams();
            params.addParam("eventId", String.valueOf(holderEvent.getId()));
            showEventTask.execute(params.parse());

            Log.v("HTTP Request", params.parse());
        }
    }
}
