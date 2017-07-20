/*
    Author: Rhys Meredith
    Description: The recover pw activity used for when a user wants to recover their password
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

public class RecoverPwActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // Hides the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.hide();

        setContentView(R.layout.activity_recover_pw);
    }

    /**
     * Called when the recover password button is clicked
     * @param v
     */
    public void recPWButton(View v) {

        EditText email = (EditText) findViewById(R.id.text_user_email);

        if(sendEmail(email.getText().toString())){
            confirmation();
        }
        else{
            unsuccesful();
        }

    }

    /**
     * Placeholder event for when a user sends an email TODO: THE SERVER NEEDS TO DO THIS
     * @param email the email address to send the information to
     * @return
     */
    public boolean sendEmail(String email){
        return true;
    }

    /**
     * The email to be sent when the button is pressed
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
     * Called when the email being sent is unccessful
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
     * Returns to the login page
     */
    public void returnToLogin(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
