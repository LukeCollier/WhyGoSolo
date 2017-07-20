/*
    Author: Luke Collier
    Description: The main activity of the app used for showing all the different tabs
    Date Created: N/A (I'll look into it)
    Last Edited by: Luke Collier
    Date Edited: 28/04/16
    Edit Note: Added comments
 */


package epsilon.whygosoloapplication.activities;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import epsilon.whygosoloapplication.R;
import epsilon.whygosoloapplication.activities.objects.Session;
import layout.HomeFragment;
import layout.ProfileFragment;
import layout.SearchFragment;
import layout.SettingsFragment;

public class MainActivity extends AppCompatActivity {

    // The number of tabs used
    static private final int NUM_TABS = 4;

    // 1 when fab is showing additional options 0 when fab is not showing additional options
    private int fabToggle = 0;
    private int dropDownToggle = 0;

    // The pager, layout and adapter to make the tab for the main activity
    private ViewPager tabsViewPager;
    private TabAdapter tabAdapter;
    private TabLayout tabLayout;

    // Couldn't figure out a way to do this through attaching to the activities so...
    private FloatingActionButton fabBottom;
    private FloatingActionButton fabMiddle;
    private FloatingActionButton fabTop;

    // The tab icons when they are not selected
    final int[] TAB_ICON = {
            R.drawable.ic_tab_home,
            R.drawable.ic_tab_search,
            R.drawable.ic_tab_profile,
            R.drawable.ic_tab_settings
    };

    // The tab icons when they are selected
    final int[] TAB_ICON_SELECTED = {
            R.drawable.ic_tab_home_selected,
            R.drawable.ic_tab_search_selected,
            R.drawable.ic_tab_profile_selected,
            R.drawable.ic_tab_settings_selected
    };

    /**
     * When the activity is created call this method
     * @param savedInstanceState the saved state for the activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v("UserId ", String.valueOf(Session.getSessionId()));

        // Hides the action bar if an action bar is found
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.hide();

        // Sets the content view to our main activity layout
        setContentView(R.layout.activity_main);

        // Get the content pager that will house additional fragments
        tabsViewPager = (ViewPager) findViewById(R.id.pager_content);

        // Get the tab bar for the activity
        tabLayout = (TabLayout) findViewById(R.id.tabView);


        // Get the 3 fabs.
        fabBottom = (FloatingActionButton) findViewById(R.id.fab_bottom);
        fabMiddle = (FloatingActionButton) findViewById(R.id.fab_middle);
        fabTop = (FloatingActionButton) findViewById(R.id.fab_top);

        // Setup a tab adapter to do some fancy adapting
        tabAdapter = new TabAdapter(getSupportFragmentManager());
        tabsViewPager.setAdapter(tabAdapter);

        // If the tabLayout exists setup with the view pager (gives the tabs fragment pages)
        if (tabLayout != null) {
            tabLayout.setupWithViewPager(tabsViewPager);
            setupIcons();
        }

        // Setup a a custom listener for when the tab changes pages to do specific aesthetic purposes
        tabsViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            private int previousTabId = 0;

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.getTabAt(position).setIcon(TAB_ICON_SELECTED[position]);
                tabLayout.getTabAt(previousTabId).setIcon(TAB_ICON[previousTabId]);
                previousTabId = position;

                // Play animations and reset the button
                if (position == 0 || position == 1) {
                    if (!fabBottom.isShown()) { fabBottom.show(); } else
                    {
                        playFabDeathBirth(fabBottom);
                        playFabDeath(fabMiddle);
                        playFabDeath(fabTop);
                    }
                    resetFabs();
                    hideFabTopMiddle();
                    fabToggle =0;
                } else {
                    resetFabs();
                    hideFabs();
                    fabToggle =0;
                }
                changeFabBackground(R.color.generic_transparent);

            }
        });
    }

    /**
     * When the fab is pressed handle that
     * @param view
     */
    public void onFabBottomClick(View view) {
        if (fabToggle == 0) {
            handleFabSelected();
        } else {
            handleFabNotSelected();
        }
    }

    /**
     * Helper methods for handling the fab being toggled
     */
    public void handleFabSelected() {
        fabToggle = 1;
        playFabRotateForward();
        fabMiddle.show();
        playFabBirth(fabMiddle);
        fabTop.show();
        playFabBirth(fabTop);
        changeFabBackground(R.color.generic_shaded);
    }

    /**
     * Helper methods for handling the fab being toggled
     */
    public void handleFabNotSelected() {
        fabToggle = 0;
        playFabRotateBackward();
        fabMiddle.hide();
        playFabDeath(fabMiddle);
        fabTop.hide();
        playFabDeath(fabTop);
        changeFabBackground(R.color.generic_transparent);
    }

    // Called when the top fab is selected
    public void onFabTopClick(View view) {
        Intent intent = new Intent(MainActivity.this, CreateEventActivity.class);
        startActivity(intent);
    }

    // Called when the top fab is selected
    public void onFabMiddleClick(View view) {
        Intent intent = new Intent(MainActivity.this, CreateBeaconActivity.class);
        startActivity(intent);
    }


    /**
     * Change the fabs background space colour
     * @param color the color int to change it to
     */
    public void changeFabBackground(int color) {
        View foregroundView =
                getWindow().getDecorView().getRootView().findViewById(R.id.foreground);
        foregroundView.setBackgroundColor(ContextCompat.getColor(getBaseContext(), color));
    }

    // Resets a fab to it's standard position
    public void resetFabs() {
        playFabReset(fabBottom);
        playFabReset(fabTop);
        playFabReset(fabMiddle);
    }

    // Hides the top and middle fabs playing their animation (android is magic)
    public void hideFabTopMiddle() {
        fabMiddle.hide();
        fabTop.hide();
    }

    // Hides all the fabs playing their animation (beautifully magical)
    public void hideFabs() {
        fabBottom.hide();
        fabTop.hide();
        fabMiddle.hide();
    }

    /**
     * Helper method to setup all the icons used by the tab layout
     */
    public void setupIcons() {
        // Sets up the initial icon for the home, it will be the selected varient
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_tab_home_selected);

        // Sets up the initial icons for the tabs that aren't selected
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_tab_search);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_tab_profile);
        tabLayout.getTabAt(3).setIcon(R.drawable.ic_tab_settings);
    }

    /**
     * Play a fabs death then birth animation using animators
     * @param fab the fab to perform the animations on.
     */
    public void playFabDeathBirth(FloatingActionButton fab) {
        AnimatorSet close = (AnimatorSet) AnimatorInflater.loadAnimator(this,
                R.animator.fab_close);
        AnimatorSet open = (AnimatorSet) AnimatorInflater.loadAnimator(this,
                R.animator.fab_open);

        AnimatorSet closeOpen = new AnimatorSet();
        closeOpen.playSequentially(close, open);
        closeOpen.setTarget(fab);
        closeOpen.start();
    }

    /**
     * Play the fabs death animation which can be replicated by using fab.hide();
     * @param fab the fab to perform the animation on
     */
    public void playFabDeath(FloatingActionButton fab) {
        AnimatorSet closeSet = (AnimatorSet) AnimatorInflater.loadAnimator(this,
                R.animator.fab_close);
        closeSet.setTarget(fab);
        closeSet.start();
    }

    /**
     * Play the fabs birth animation
     * @param fab the fab to perform the animation on
     */
    public void playFabBirth(FloatingActionButton fab) {
        AnimatorSet openSet = (AnimatorSet) AnimatorInflater.loadAnimator(this,
                R.animator.fab_open);
        openSet.setTarget(fab);
        openSet.start();
    }

    /**
     * Reset a fab to it's original rotation
     * @param fab resets a fabs rotation
     */
    public void playFabReset(FloatingActionButton fab) {
        AnimatorSet resetSet = (AnimatorSet) AnimatorInflater.loadAnimator(this,
                R.animator.fab_rotate_backwards);
        resetSet.setTarget(fab);
        resetSet.start();
    }

    /**
     * Rotates the bottom fab by 45 degrees
     */
    public void playFabRotateForward() {
        AnimatorSet rotateForwardSet = (AnimatorSet) AnimatorInflater.loadAnimator(this,
                R.animator.fab_rotate_forward);
        rotateForwardSet.setTarget(fabBottom);
        rotateForwardSet.start();
    }

    /**
     * Rotate the fab backwards by 45 degrees
     */
    public void playFabRotateBackward() {
        AnimatorSet rotateBackwardSet = (AnimatorSet) AnimatorInflater.loadAnimator(this,
                R.animator.fab_rotate_backwards);
        rotateBackwardSet.setTarget(fabBottom);
        rotateBackwardSet.start();
    }

    /**
     * Called when the create button is used
     * @param view the view calling the button
     */
    public void onArrowDropDown(View view) {
        if (dropDownToggle == 0) {
            dropDownToggle=1;
            playDropDownRotateForward();
            findViewById(R.id.view_advance_search).setVisibility(View.VISIBLE);
        } else {
            dropDownToggle=0;
            playDropDownRotateBackward();
            findViewById(R.id.view_advance_search).setVisibility(View.GONE);
        }

    }

    /**
     * Plays the dropdown rotate forward animation on the drop down button
     */
    public void playDropDownRotateForward() {
        AnimatorSet rotateForwardSet = (AnimatorSet) AnimatorInflater.loadAnimator(this,
                R.animator.button_drop_down_forward);
        ImageButton dropDown = (ImageButton) findViewById(R.id.button_drop_down);
        rotateForwardSet.setTarget(dropDown);
        rotateForwardSet.start();
    }

    /**
     * Plays the dropdown animation backwards
     */
    public void playDropDownRotateBackward() {
        AnimatorSet rotateBackwardSet = (AnimatorSet) AnimatorInflater.loadAnimator(this,
                R.animator.button_drop_down_backward);
        ImageButton dropDown = (ImageButton) findViewById(R.id.button_drop_down);
        rotateBackwardSet.setTarget(dropDown);
        rotateBackwardSet.start();
    }

    /**
     * Logs the user out of the system
     * @param v the view that's calling it
     */
    public void logout(View v)
    {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    /**
     * Opens up the privacy activity
     * @param v the view that's calling it
     */
    public void privacyInfo(View v)
    {
        Intent intent = new Intent(this, PrivacyActivity.class);
        startActivity(intent);
    }

    /**
     * Opens up the safety info activity
     * @param v the view that's calling it
     */
    public void safetyInfo(View v)
    {
        Intent intent = new Intent(this, SafetyActivity.class);
        startActivity(intent);
    }

    public static class TabAdapter extends FragmentPagerAdapter {

        public TabAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * Called the get the different fragments the view pager holds
         * @param position the page to be given
         * @return the fragment required
         */
        @Override
        public Fragment getItem(int position) {
            Fragment frag;
            switch (position) {
                case 0:
                    frag = new HomeFragment();
                    break;
                case 1:
                    frag = new SearchFragment();
                    break;
                case 2:
                    frag = new ProfileFragment();
                    break;
                case 3:
                    frag = new SettingsFragment();
                    break;
                default:
                    frag = null;
                    break;
            }
            return frag;
        }

        /**
         * Gets the number of tabs on the tabs
         * @return the number of tabs
         */
        @Override
        public int getCount() {
            return NUM_TABS;
        }
    }
}
