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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import epsilon.whygosoloapplication.R;
import epsilon.whygosoloapplication.activities.json.JsonParams;
import epsilon.whygosoloapplication.activities.json.JsonParser;
import epsilon.whygosoloapplication.activities.objects.Session;
import epsilon.whygosoloapplication.activities.utility.UserError;
import epsilon.whygosoloapplication.activities.utility.Validation;

public class LoginActivity extends AppCompatActivity {
    EditText username, password;

    ProgressDialog progressDialog;

    /**
     * When the activity is created display this create
     * @param savedInstanceState the saved instance of the activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hides the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.hide();

        setContentView(R.layout.activity_login);

        username = (EditText) findViewById(R.id.text_login_username);
        password = (EditText) findViewById(R.id.text_user_password);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String usernameString = extras.getString("username");
            String passwordString = extras.getString("password");

            username.setText(usernameString);
            password.setText(passwordString);
        }
    }

    /**
     * Go to different pages
     * @param v the view it was called from
     */
    public void loginText(View v) {
        Intent intent = null;

        switch(v.getId()) {
            case R.id.txtAccess:
                intent = new Intent(this, RecoverActivity.class);
                break;
            case R.id.txtRegister:
                intent = new Intent(this, RegisterActivity.class);
                break;
        }

        if (intent != null) {
            startActivity(intent);
        }
    }

    /**
     * Called when the login button is pressed
     * @param v the view that called it
     */
    public void loginButton(View v) {
        String validationMessage = validateUserInput();
        if (validationMessage.equals("")) {
            String usernameString = username.getText().toString();
            String passwordString = password.getText().toString();
            progressDialog = ProgressDialog.show(this, "", "Logging in. Please wait...", true);
            sendCredentials(usernameString, passwordString);
        } else {
            displayUserError(validationMessage);
        }
    }

    /**
     * Send credentials to the server for checking
     * @param username username provided
     * @param password the password provided
     */
    public void sendCredentials(String username, String password){
        JsonParams params = new JsonParams();
        if (Validation.isEmailValid(username)) {
            params.addParam("email",username);
        } else {
            params.addParam("displayName",username);
        }
        params.addParam("passHash",password);

        new LoginTask().execute(params.parse());
    }

    /**
     * Checks the form and returns any relevant error messages
     * @return an error message if one is found
     */
    private String validateUserInput() {
        String message = Validation.validateLengths(username.getText().toString(),"username",
                password.getText().toString(),"password");
        if (Validation.isEmpty(username,password)) {
            message = "You must enter a username and password";
        }
        return message;
    }

    /**
     * Head to the main activity
     */
    public void gotoMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    /**
     * Display an error in the form of a snackbar
     * @param message display an error message
     */
    public void displayUserError(String message) {
        UserError.showErrorDialog(message,this);
    }

    public class LoginTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {}

        /**
         * Get's the the login JSON file from the API server with either a 1 or 0
         * @param params the params for the JSON get request
         * @return the JSON message
         */
        @Override
        protected String doInBackground(String... params)  {
            String message;
            try {
                message = JsonParser.get("login",params[0]);
            } catch (IOException e) {
                message = "Exception found when posting to create event task " + e;
            }

            return message;
        }

        @Override
        protected void onProgressUpdate(Void... values) {}

        /**
         * Either displays an error or starts a new task to get the user data and store it in Session
         * @param result the JSON message
         */
        @Override
        protected void onPostExecute(String result) {
            Log.v("Login Task Result", result);

            if (result.equals("[]")) {
                displayUserError("Your username was not recognised");
                progressDialog.dismiss();
            } else {
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    Log.v("Login Task Result", jsonArray.getJSONObject(0).toString());
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    int loginCode = jsonObject.getInt("loginSucsess");
                    if (loginCode == 1) {
                        JsonParams params = new JsonParams();
                        params.addParam("displayName",username.getText().toString());
                        new GetUserIdTask().execute(params.parse());
                    } else if(loginCode == 0) {
                        displayUserError("You have entered an incorrect password");
                        progressDialog.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public class GetUserIdTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {}

        /**
         * Get the specific user who is logged in putting their details into the Session class
         * @param params the params of the user logged in
         * @return the JSON message
         */
        @Override
        protected String doInBackground(String... params)  {
            String message;
            try {
                message = JsonParser.get("userSearch",params[0]);
            } catch (IOException e) {
                message = "Exception found when attempting to login task " + e;
            }

            return message;
        }

        @Override
        protected void onProgressUpdate(Void... values) {}

        /**
         * Update the Session object with the users details or display an error
         * @param result the users details
         */
        @Override
        protected void onPostExecute(String result) {
            Log.v("Session Id Task Result", result);
            progressDialog.dismiss();

            if (result.equals("[]")) {
                displayUserError("Your username was not recognised");
            } else {
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    Log.v("Session Id Task Result", jsonArray.getJSONObject(0).toString());
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    Session.setSessionId((long) jsonObject.getInt("id"));
                    gotoMain();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}