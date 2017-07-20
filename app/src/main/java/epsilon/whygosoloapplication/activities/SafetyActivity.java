/*
    Author: Kate Leather
    Description: Information page for safety
    Date Created: N/A
    Last Edited by: Luke Colliers
    Date Edited: 07/05/2016
    Edit Note: Added comments
 */
package epsilon.whygosoloapplication.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import epsilon.whygosoloapplication.R;

public class SafetyActivity extends AppCompatActivity {

    /**
     * Called when the activity is first created to set everything up
     * @param savedInstanceState any stored information already in the activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(null);

        setContentView(R.layout.activity_safety);
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
}
