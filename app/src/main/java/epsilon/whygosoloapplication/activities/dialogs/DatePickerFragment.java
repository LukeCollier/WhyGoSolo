/*
    Author: Luke Collier
    Description: A date picker fragment
    Date Created: N/A
    Last Edited by: Luke Colliers
    Date Edited: 07/05/2016
    Edit Note: Added comments
 */
package epsilon.whygosoloapplication.activities.dialogs;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment
    {
        private DatePickerDialog.OnDateSetListener onDateSetListener;

        public DatePickerFragment() {}

        /**
         * Sets the on date set listener for the dialog
         * @param onDateSetListener the ondatesetlistener to set it too
         */
        public void setOnDateSetListener(DatePickerDialog.OnDateSetListener onDateSetListener) {
            this.onDateSetListener = onDateSetListener;
        }

        /**
         * Called when the dialog is created
         * @param savedInstanceState the stored information on what was previously shown
         * @return a date picker dialog
         */
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog =
                    new DatePickerDialog(getActivity(), onDateSetListener, year, month, day);
            datePickerDialog.getDatePicker().setMinDate(getArguments().getLong("minDate"));
            // Create a new instance of DatePickerDialog and return it
            return datePickerDialog;
        }

    }
