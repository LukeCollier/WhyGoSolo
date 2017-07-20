/*
    Author: Luke Collier
    Description: A time picker fragment
    Date Created: N/A
    Last Edited by: Luke Colliers
    Date Edited: 07/05/2016
    Edit Note: Added comments
 */
package epsilon.whygosoloapplication.activities.dialogs;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class TimePickerFragment extends DialogFragment
{
    private TimePickerDialog.OnTimeSetListener onTimeSetListener;

    public TimePickerFragment() {}

    /**
     * Set the listener to do something
     * @param onTimeSetListener the listener to update it too
     */
    public void setOnTimeSetListener(TimePickerDialog.OnTimeSetListener onTimeSetListener) {
        this.onTimeSetListener = onTimeSetListener;
    }

    /**
     *
     * @param savedInstanceState
     * @return
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(),onTimeSetListener, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

}
