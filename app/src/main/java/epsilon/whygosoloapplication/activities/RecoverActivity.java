/*
    Author: Rhys Meredith
    Description: Creates a recovery activity for recovering either a password or username
    Date Created: N/A
    Last Edited by: Luke Colliers
    Date Edited: 07/05/2016
    Edit Note: Added comments
 */
package epsilon.whygosoloapplication.activities;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

import epsilon.whygosoloapplication.R;

public class RecoverActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hides the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.hide();

        setContentView(R.layout.activity_recover);
    }

    /**
     * Decides which activity to open
     * @param v the view to use
     */
    public void recoverWhich(View v) {
        Intent intent = null;
        RadioButton password = (RadioButton) findViewById(R.id.radioButton);
        RadioButton username = (RadioButton) findViewById(R.id.radioButton2);

        if (password.isChecked()) {
            intent = new Intent(this, RecoverPwActivity.class);
        }
        else if (username.isChecked()) {
            intent = new Intent(this, RecoverUsernameActivity.class);
        }

        if (intent != null) {
            startActivity(intent);
        }
    }
}
