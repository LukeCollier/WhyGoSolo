/*
    Author: Rhys Meredith
    Description: The create beacon activity is used to create the beacon
    Date Created: N/A
    Last Edited by: Luke Colliers
    Date Edited: 07/05/2016
    Edit Note: Added comments and changed the activity to work with the new API
 */
package epsilon.whygosoloapplication.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import epsilon.whygosoloapplication.R;
import epsilon.whygosoloapplication.activities.json.JsonParams;
import epsilon.whygosoloapplication.activities.json.JsonParser;
import epsilon.whygosoloapplication.activities.utility.UserError;
import epsilon.whygosoloapplication.activities.utility.Validation;

public class RegisterActivity extends AppCompatActivity {
    // General used over the activity fields
    EditText username, email, password, reenterPassword, biography,firstName,lastName;
    ProgressDialog progressDialog;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hides the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.hide();

        setContentView(R.layout.activity_register);

        username = (EditText) findViewById(R.id.text_user_username);
        email = (EditText) findViewById(R.id.text_user_email);
        password = (EditText) findViewById(R.id.text_user_password);
        reenterPassword = (EditText) findViewById(R.id.text_user_validate_password);
        biography = (EditText) findViewById(R.id.text_user_biography);
        firstName = (EditText) findViewById(R.id.text_user_first_name);
        lastName = (EditText) findViewById(R.id.text_user_last_name);

        intent = new Intent(this, LoginActivity.class);
    }

    /**
     * Used when the user presses the register button, will send register details if validation
     * passes
     * @param v the view attached to the button
     */
    public void registerButton(View v) {
        String validationMessage = validateUserInput();
        if (validationMessage.equals("")) {
            sendRegisterDetails();
        } else {
            showErrorMessage(validationMessage);
        }

    }

    /**
     * Sends the registration details off I honestly have no idea why it returns true I left that
     * incase it was important
     * @return true
     */
    public boolean sendRegisterDetails(){

        String usernameString = username.getText().toString();
        String emailString = email.getText().toString();
        String passwordString = password.getText().toString();
        String biographyString = biography.getText().toString();
        String firstNameString = firstName.getText().toString();
        String lastNameString = lastName.getText().toString();

        SimpleDateFormat stf = new SimpleDateFormat("yyyy-MM-dd",
                getResources().getConfiguration().locale);

        JsonParams params = new JsonParams();
        params.addParam("email",emailString);
        params.addParam("dob",stf.format(new Date()));
        params.addParam("firstName",firstNameString);
        params.addParam("lastName",lastNameString);
        params.addParam("displayName",usernameString);
        params.addParam("home","1");
        params.addParam("passHash",passwordString);
        if (Validation.isEmpty(biography)) {
            params.addParam("biography","No information was given");
        } else {
            params.addParam("biography",biographyString);
        }

        progressDialog = ProgressDialog.show(this, "","Registering. Please wait...", true);

        new CreateUserTask().execute(params.parse());

        return true;
    }

    /**
     * Validates the information given by the user
     * @return true or false depending on if the users information is valid
     */
    private String validateUserInput() {
        String message = Validation.validateLengths(
                username.getText().toString(),"username",
                password.getText().toString(),"password",
                email.getText().toString(),"email"
                );
        String emailText = email.getText().toString();
        if(Validation.isEmpty(username,email,password,lastName,firstName)) {
            message = "You have not filled out some required information";
        } else if(!Validation.isEmailValid(emailText)) {
            message = emailText+" is not a valid email";
        } else if (!password.getText().toString().equals(reenterPassword.getText().toString())){
            message = "Please make sure the passwords are identical";
        }

        return message;
    }

    /**
     * Display an error message on this form
     * @param errorMessage the error message to show
     */
    public void showErrorMessage(String errorMessage){
        UserError.showErrorDialog(errorMessage,this);
    }

    public class CreateUserTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {}

        /**
         * Posts the new user to the server
         * TODO: Check the response to se if it actually added user
         * @param params the params for new user to be registered for
         * @return the response from the server
         */
        @Override
        protected String doInBackground(String... params)  {
            String message = "OK";
            try {
                JsonParser.post("user",params[0]);
            } catch (IOException e) {
                message = "Exception found when posting to create event task " + e;
            }

            return message;
        }

        @Override
        protected void onProgressUpdate(Void... values) {}

        /**
         * Updates information and puts the username and password in to a new login activity
         * @param result the server response
         */
        @Override
        protected void onPostExecute(String result) {
            Log.v("Register Task Result", result);
            intent.putExtra("username", username.getText().toString());
            intent.putExtra("password", password.getText().toString());
            progressDialog.dismiss();
            startActivity(intent);
        }
    }
}
