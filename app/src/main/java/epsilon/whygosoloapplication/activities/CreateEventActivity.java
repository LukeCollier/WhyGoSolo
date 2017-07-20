/*
    Author: Luke Collier
    Description: The create event activity, handles everything shown on this part of the app
    Date Created: N/A (I'll look into it)
    Last Edited by: Luke Collier
    Date Edited: 28/04/16
    Edit Note: Added comments
 */

package epsilon.whygosoloapplication.activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TimePicker;

import java.io.IOException;
import java.text.SimpleDateFormat;

import epsilon.whygosoloapplication.R;
import epsilon.whygosoloapplication.activities.json.JsonParams;
import epsilon.whygosoloapplication.activities.json.JsonParser;
import epsilon.whygosoloapplication.activities.objects.Event;
import epsilon.whygosoloapplication.activities.dialogs.DatePickerFragment;
import epsilon.whygosoloapplication.activities.dialogs.TimePickerFragment;
import epsilon.whygosoloapplication.activities.objects.Session;
import epsilon.whygosoloapplication.activities.utility.Validation;

public class CreateEventActivity extends AppCompatActivity {
    EditText fromDate, fromTime, toDate, toTime, eventName, location, description;
    Spinner spinnerCategory;
    Button buttonEvent; ImageButton buttonProfile;

    LinearLayout background;

    ProgressDialog progressDialog;

    Event newEvent;

    /**
     * A android method called when the activity is first created
     * @param savedInstanceState the state it was in last (not used by us)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(null);

        setContentView(R.layout.activity_create_event);

        // TODO: DELETE AND FIX THIS USING ASYNC
        /*
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);*/

        buttonEvent = (Button) findViewById(R.id.button_create);
        buttonProfile = (ImageButton) findViewById(R.id.button_profile_picture);

        newEvent = new Event();

        fromDate = (EditText) findViewById(R.id.text_from_date);
        fromTime = (EditText) findViewById(R.id.text_from_time);
        toDate = (EditText) findViewById(R.id.text_to_date);
        toTime = (EditText) findViewById(R.id.text_to_time);
        eventName = (EditText) findViewById(R.id.text_event_name);
        location = (EditText) findViewById(R.id.text_location_name);
        description = (EditText) findViewById(R.id.text_box_description);

        background = (LinearLayout) this.findViewById(R.id.background_layout);

        // Sets up the spinner
        spinnerCategory = (Spinner) findViewById(R.id.spinner_category);
        // Create an ArrayAdapter using the string array and a default spinner layout
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.array_categories, R.layout.spinner_item_search);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        assert spinnerCategory != null; spinnerCategory.setAdapter(adapter);

        // All these simply update the event object after a part of the ui is updated
        // TODO: Find a more efficient way to write these out
        assert eventName != null;
        eventName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                newEvent.setName(eventName.getText().toString());
            }
            @Override
            public void afterTextChanged(Editable s) {
                newEvent.setName(eventName.getText().toString());
            }
        });
        assert location != null;
        location.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                newEvent.setName(eventName.getText().toString());
            }
            @Override
            public void afterTextChanged(Editable s) {
                newEvent.setLocation(location.getText().toString());
            }
        });
        // Handles the spinner slightly different but same principle
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                newEvent.setCategory((String) adapter.getItem(position));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        assert description != null;
        description.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                newEvent.setName(eventName.getText().toString());
            }
            @Override
            public void afterTextChanged(Editable s) {
                newEvent.setDescription(description.getText().toString());
            }
        });
    }

    /**
     * Sets the from date for the new event
     * @param view the view to use
     */
    public void onSetFromDate(View view) {
        DatePickerDialog.OnDateSetListener dateSetListener =
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view,
                                          int year, int monthOfYear, int dayOfMonth) {
                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy",
                                getResources().getConfiguration().locale);
                        newEvent.setFromDate(dayOfMonth,monthOfYear,year);
                        fromDate.setText(sdf.format(newEvent.getFromCalendar().getTime()));
                        if (newEvent.getToCalendar().getTimeInMillis()
                                < newEvent.getFromCalendar().getTimeInMillis()) {
                            newEvent.setToDate(dayOfMonth,monthOfYear,year);
                            toDate.setText(sdf.format(newEvent.getToCalendar().getTime()));
                        }
                    }
                };
        Bundle datePickerBundle = new Bundle();
        datePickerBundle.putLong("minDate",System.currentTimeMillis());
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.setArguments(datePickerBundle);
        datePickerFragment.setOnDateSetListener(dateSetListener);
        datePickerFragment.show(getSupportFragmentManager(), "datePicker");
    }

    /**
     * Sets the to date for the new event
     * @param view the view to use
     */
    public void onSetToDate(View view) {
        DatePickerDialog.OnDateSetListener dateSetListener =
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view,
                                          int year, int monthOfYear, int dayOfMonth) {
                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy",
                                getResources().getConfiguration().locale);
                        newEvent.setToDate(dayOfMonth,monthOfYear,year);
                        toDate.setText(sdf.format(newEvent.getToCalendar().getTime()));
                        background.requestFocus();
                    }
                };

        Bundle datePickerBundle = new Bundle();
        datePickerBundle.putLong("minDate",newEvent.getFromCalendar().getTimeInMillis());
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.setArguments(datePickerBundle);
        datePickerFragment.setOnDateSetListener(dateSetListener);
        datePickerFragment.show(getSupportFragmentManager(), "datePicker");
    }

    /**
     * Sets the to date for the new event
     * @param view the view to use
     */
    public void onSetToTime(View view) {
        TimePickerDialog.OnTimeSetListener timeSetListener =
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        SimpleDateFormat stf = new SimpleDateFormat("HH:mm a",
                                getResources().getConfiguration().locale);
                        newEvent.setToTime(hourOfDay,minute);
                        toTime.setText(stf.format(newEvent.getToCalendar().getTime()));
                        background.requestFocus();
                    }
                };
        TimePickerFragment timePickerFragment = new TimePickerFragment();
        timePickerFragment.setOnTimeSetListener(timeSetListener);
        timePickerFragment.show(getSupportFragmentManager(), "timePicker");
    }

    /**
     * Sets the to date for the new event
     * @param view the view to use
     */
    public void onSetFromTime(View view) {
        TimePickerDialog.OnTimeSetListener timeSetListener =
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        SimpleDateFormat stf = new SimpleDateFormat("HH:mm a",
                                getResources().getConfiguration().locale);
                        newEvent.setFromTime(hourOfDay,minute);
                        fromTime.setText(stf.format(newEvent.getFromCalendar().getTime()));
                        background.requestFocus();
                    }
                };
        TimePickerFragment timePickerFragment = new TimePickerFragment();
        timePickerFragment.setOnTimeSetListener(timeSetListener);
        timePickerFragment.show(getSupportFragmentManager(), "timePicker");
    }

    /**
     * Called when the picture button is clicked
     * TODO: Allow user to select from new picture or gallery
     * @param view the current view the button is being clicked from
     */
    public void onProfileButtonClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle(R.string.title_profile)
                .setItems(R.array.array_profile_options, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch(which) {
                            case 0: Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(takePicture, 0);
                                break;
                            case 1: Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(pickPhoto , 1);
                                break;
                        }
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Gets the result of the dialog created by the profile button being clicked
     * @param requestCode the code used for the activity
     * @param resultCode the result of the activity
     * @param imageReturnedIntent the image that was selected/taken
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch(requestCode) {
            case 0:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    assert buttonProfile != null;
                    buttonProfile.setImageURI(selectedImage);
                }

                break;
            case 1:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    assert buttonProfile != null;
                    buttonProfile.setImageURI(selectedImage);
                }
                break;
        }
    }

    /**
     * Called when the create button is used
     * @param view the view calling the button
     */
    public void onCreateEvent(View view) {
        String validationMessage = validateUserInput();
        if (validationMessage.equals("")) {
            SimpleDateFormat stf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                    getResources().getConfiguration().locale);

            JsonParams params = new JsonParams();
            params.addParam("name",newEvent.getName());
            params.addParam("from",stf.format(newEvent.getFromCalendar().getTime()));
            params.addParam("to",stf.format(newEvent.getToCalendar().getTime()));
            params.addParam("locId","1");
            params.addParam("userId", String.valueOf(Session.getSessionId()));
            params.addParam("description",newEvent.getDescription());

            new CreateEventTask().execute(params.parse());
            newEvent.setPictureId(R.drawable.ic_tab_profile);
            progressDialog = ProgressDialog.show(this, "","Creating Event. Please wait...", true);
        } else {
            displayUserError(validationMessage);
        }
    }

    /**
     * Display an error in the form of a snackbar
     * @param message display an error message
     */
    public void displayUserError(String message) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        android.app.AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     * Validate that the user has entered all required information eventName, description;
     * @return the validation message
     */
    private String validateUserInput() {
        String message = Validation.validateLengths(newEvent.getName(),"name",
                newEvent.getDescription(),"description");

        if (newEvent.getLocation()==null || newEvent.getName()==null
                || Validation.isEmpty(toTime,fromDate,fromTime,toDate)) {
            message = "Some required fields were not filled out";
        }

        long fromTimeMillis = newEvent.getFromCalendar().getTimeInMillis();
        long toTimeMillis = newEvent.getToCalendar().getTimeInMillis();
        if (fromTimeMillis > toTimeMillis) {
            message = "The event cannot start after it ends";
        }
        return message;
    }

    /**
     * Helper method to handle title bar button control
     * @param item the item that was selected
     * @return true if successful
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    /**
     * Posts to events with the parameters provided
     */
    public class CreateEventTask extends AsyncTask<String, Void, String> {
        /**
         * Happens before the task is executed
         */
        @Override
        protected void onPreExecute() {}

        /**
         * Submit a post event with the params provided
         * @param params the params to use for the post
         * @return a message String filled with a JSON file
         */
        @Override
        protected String doInBackground(String... params)  {
            String message = "OK";
            try {
                JsonParser.post("events",params[0]);
            } catch (IOException e) {
                message = "Exception found when posting to create event task " + e;
            }

            return message;
        }

        /**
         * Called when the progress updates
         * @param values the values received
         */
        @Override
        protected void onProgressUpdate(Void... values) {}

        /**
         * After the task is done remove the progress dialog and end the activity
         * @param result the JSON script for the response
         */
        @Override
        protected void onPostExecute(String result) {
            Log.v("Post Task Result", result);
            progressDialog.dismiss();
            finish();
        }
    }
}
