/*
    Author: Rhys Meredith
    Description: The recover usename activity recovers the username for a user
    Date Created: N/A
    Last Edited by: Luke Colliers
    Date Edited: 07/05/2016
    Edit Note: Added comments
 */
package epsilon.whygosoloapplication.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import epsilon.whygosoloapplication.R;

public class RecoverUsernameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // Hides the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.hide();

        setContentView(R.layout.activity_recover_username);
    }

    /**
     * Used when the recUN button is pressed
     * @param v the view the button is used from
     */
    public void recUNButton(View v) {

        EditText email = (EditText) findViewById(R.id.text_user_email);

        if(sendEmail(email.getText().toString())){
            confirmation();
        }
        else{
            unsuccesful();
        }

    }

    /**
     * Sends an email to the users address
     * @param email the email to send too
     * @return if the email sent successfully
     */
    public boolean sendEmail(String email){
        return true;
    }

    /**
     * Displays a message to the user showing it's been done
     */
    public void confirmation(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Email sent!")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        returnToLogin();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }

    /**
     * Called when the email was unsuccessful alerting the user
     */
    public void unsuccesful(){
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

    /**
     * Returns the user to the login screen
     */
    public void returnToLogin(){
        Intent intent = null;
        intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
