/*
    Author: Rhys Meredith
    Description: The create beacon activity is used to create the beacon
    Date Created: N/A
    Last Edited by: Luke Colliers
    Date Edited: 07/05/2016
    Edit Note: Added comments
 */

package epsilon.whygosoloapplication.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.DatePicker;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import epsilon.whygosoloapplication.R;

public class CreateBeaconActivity extends AppCompatActivity {

    EditText fromDate;
    EditText toDate;
    EditText fromTime;
    EditText toTime;

    /**
     * The on create method is called when the beacon activity is created
     * @param savedInstanceState the saved state of the activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_beacon);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.hide();

        fromDate = (EditText) findViewById(R.id.editFromDate);
        toDate = (EditText) findViewById(R.id.editToDate);
        fromTime = (EditText) findViewById(R.id.editFromTime);
        toTime = (EditText) findViewById(R.id.editToTime);
    }


    Calendar myCalendar = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        }

    };


    /**
     * Creates a date dialog event
     * @param v the view to attach the picker too
     */
    public void dateClick(View v) {
        new DatePickerDialog(CreateBeaconActivity.this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();

        String myFormat = "dd/MM/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);

        switch(v.getId()) {
            case R.id.editFromDate:
                fromDate.setText(sdf.format(myCalendar.getTime()));
            case R.id.editToDate:
                toDate.setText(sdf.format(myCalendar.getTime()));
        }
    }

    /**
     * Opens up a time picker dialog
     * @param v the view to attach the picker too
     */
    public void timeFromClick(View v) {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(CreateBeaconActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour2, int minute2) {
                fromTime.setText(hour2 + ":" + minute2);
            }
        }, hour, minute, true);

        mTimePicker.show();
    }

    /**
     * Opens up a time dialog
     * @param v the view calling it
     */
    public void timeToClick(View v) {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(CreateBeaconActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour2, int minute2) {
                toTime.setText(hour2 + ":" + minute2);
            }
        }, hour, minute, true);

        mTimePicker.show();
    }

    /**
     * Create button clicked
     * @param v the view that summoned the event
     */
    public void create(View v){
        if(submit()){
            finish();
        }
        else{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Unsuccessful attempt, please try again")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }


    }

    /**
     * Will eventually contain validation
     * @return always true right now
     */
    public boolean submit(){
        return true;
    }
}
