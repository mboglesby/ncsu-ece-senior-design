package edu.ncsu.walkwars.activity;

import android.app.TabActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;
import edu.ncsu.walkwars.R;
import edu.ncsu.walkwars.util.Util;

public class MainTabActivity extends TabActivity
{
    SharedPreferences prefs = null;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tab);

        
        Resources res = getResources(); // Resource object to get Drawables
        TabHost tabHost = getTabHost(); // The activity TabHost
        TabHost.TabSpec spec;           // Reusable TabSpec for each tab
        Intent intent;                  // Reusable Intent for each tab

        // Set tabs
        intent = new Intent().setClass(this, MapviewActivity.class);
        spec = tabHost.newTabSpec("map").setIndicator("Map", res.getDrawable(R.drawable.icon_location_1)).setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, LeaderboardActivity.class);
        spec = tabHost.newTabSpec("leaderboard").setIndicator("Leaderboard", res.getDrawable(R.drawable.icon_stats_1)).setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, EventsActivity.class);
        spec = tabHost.newTabSpec("events").setIndicator("Events", res.getDrawable(R.drawable.icon_event_1)).setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, SettingsActivity.class);
        spec = tabHost.newTabSpec("settings").setIndicator("Settings", res.getDrawable(R.drawable.icon_settings)).setContent(intent);
        tabHost.addTab(spec);

        // Detech first launch
        Util util = new Util(this.getApplicationContext());
        boolean firstLaunch = util.detectFirstLaunch();
        if(firstLaunch)
        {
            tabHost.setCurrentTab(3); // If first launch, direct user to the settings tab
            util.turnOnAutoUpdate();
        }
        else
        {
            tabHost.setCurrentTab(0); // Else show them the map view
        }
    }
}