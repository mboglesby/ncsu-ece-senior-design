package edu.ncsu.walkwars.activity;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TextView;
import edu.ncsu.walkwars.R;
import edu.ncsu.walkwars.adapter.EventsListViewAdapter;
import edu.ncsu.walkwars.logic.PostJSONDataAsyncTask;
import edu.ncsu.walkwars.model.Event;
import edu.ncsu.walkwars.util.ServerAddresses;
import edu.ncsu.walkwars.util.Util;

public class EventsActivity extends Activity
{
    private ListView eventsListView;
    private Context context;
    private ArrayList<Event> eventList = new ArrayList<Event>();
    private EventsListViewAdapter eventAdapter;
    private TextView statusTextView;
    private Util util;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        context = this;
        util = new Util(context);
        setContentView(R.layout.activity_events);

        eventsListView = (ListView) findViewById(R.id.eventsListView);
        statusTextView = (TextView) findViewById(R.id.eventStatusTextView);
        
        // Pass in the list to the adapter
        eventAdapter = new EventsListViewAdapter(this, 0, eventList);
        
        // Set the adapter
        eventsListView.setAdapter(eventAdapter);
        
        // Set long click listener to start SMS intent
        eventsListView.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int pos, long id)
            {
                Log.d(Util.DEBUG, "Long click on event item " + String.valueOf(pos));
                Event selectedEvent = eventList.get(pos);
                String message = "Walk Wars: " + selectedEvent.getEvent() + " gets your team an additional " + selectedEvent.getPointValue() + " points";

                // Message intent to start SMS app. Does not work with Droid Razr...
                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                sendIntent.setData(Uri.parse("sms:"));
                sendIntent.putExtra("sms_body", message);
                startActivity(sendIntent);
                
                // Call server script
                if(util.isLoggedIn())
                {
                    logMessage();
                }
                return false;
            }
        }); 
    }
    
    /**
     * This method communicates with the server and updates the events list
     */
    private void updateEventList () {
    	// Create JSON object that contains the user name
        JSONObject userNameJSON = new JSONObject();
        try
        {
        	userNameJSON.put("user_name", util.getPlayerName());
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    	
    	
        new PostJSONDataAsyncTask(this, userNameJSON, ServerAddresses.getEventBoardURL , false)
        {
            
            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();
                updateStatusText("Updating...");
            }
            
            // Override the onPostExecute to do whatever you want
            @Override
            protected void onPostExecute(String response)
            {
                super.onPostExecute(response);
                if (response != null)
                {
                    JSONObject jsonObject = Util.convertStringToJSONObject(response);
                    
                    if(jsonObject == null){
                        updateStatusText("Error parsing server response");
                        return;
                    }
                    
                    // If returned object length is 
                    Log.d(Util.DEBUG, jsonObject.toString());
                    if(jsonObject.length() > 0){
                        
                        parseAndAddEventsToList(jsonObject);
                    }
                    else {
                        // TODO Do something here if no events
                    }
                    
                    updateStatusText("Update Success");
                }
                else
                {
                    // Toast.makeText(context, "Error connecting to server", Toast.LENGTH_LONG).show();
                    updateStatusText("Error Connecting to Server");
                }

            }

        }.execute();

        return;
        //eventList.clear();
        //eventList.add(new Event("Bonus Points at zone 3", "60"));
        //eventList.add(new Event("Bonus Points at zone 1", "30"));
        //eventAdapter.notifyDataSetChanged();
    }

    
    /**
     * Method to parse the JSON response from server and update the listview
     * 
     * @param jsonObject
     */
    private void parseAndAddEventsToList(JSONObject jsonObject)
    {
     // Clear the old teams list
        eventList.clear();
        
        int length = jsonObject.length();
        
        // Starts at one and goes up to whatever the length of the object is
        for (int i = 1; i <= length; i++) {
            String key = Integer.toString(i);
            try
            {
                JSONObject teamObject = (JSONObject) jsonObject.get(key);
                String eventName = teamObject.getString("event_name");
                String bonusScore = teamObject.getString("bonus_score");
                boolean completed = teamObject.getBoolean("completed");
                eventList.add(new Event(eventName, bonusScore, completed));
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
        
        // Notify Adapter after data change
        eventAdapter.notifyDataSetChanged();
    }

    // Helper method to set the status text view at the bottom 
    private void updateStatusText(String message) {        
        statusTextView.setText("(" + Util.getTimeStamp() + "): " + message);
    }

    /**
     * This method communicates with the server for logging event communications
     */
    private void logMessage () {
        JSONObject userNameJSON = new JSONObject();
        try
        {
            userNameJSON.put("user_name", util.getPlayerName());
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        // Make the call
        new PostJSONDataAsyncTask(this, userNameJSON, ServerAddresses.logMessageURL , false){}.execute();

        return;
    }
    
    
    
    @Override
    public void onResume(){
        super.onResume();
        updateEventList();
        //eventList.clear();
        //eventList.add(new Event("Bonus Points at zone 3", "60", false));
        //eventList.add(new Event("Bonus Points at zone 1", "30", true));
        //eventAdapter.notifyDataSetChanged();
    }
}
