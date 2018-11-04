package edu.ncsu.walkwars.activity;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import edu.ncsu.walkwars.R;
import edu.ncsu.walkwars.adapter.LeaderboardListViewAdapter;
import edu.ncsu.walkwars.logic.PostJSONDataAsyncTask;
import edu.ncsu.walkwars.model.Team;
import edu.ncsu.walkwars.util.ServerAddresses;
import edu.ncsu.walkwars.util.Util;

public class LeaderboardActivity extends Activity
{
    private ListView leaderboardListView;
    private ArrayList<Team> teamList = new ArrayList<Team>();
    private LeaderboardListViewAdapter lbAdapater;
    private TextView statusEditText = null;
    
    public void onCreate(Bundle icicle)
    {
        super.onCreate(icicle);
        setContentView(R.layout.activity_leaderboard);
        
        leaderboardListView = (ListView) findViewById(R.id.leaderboardListView);
        statusEditText = (TextView)findViewById(R.id.leaderBoardstatusTextView);
        
        // Pass in the list to the adapter
        lbAdapater = new LeaderboardListViewAdapter(this, 0, teamList);
        
        // Set the adapter
        leaderboardListView.setAdapter(lbAdapater);

        // Set the status textview at the bottom
        updateStatusText("Awaiting Update");
        updateLeaderBoard();
    }

    @Override
    public void onResume(){
        super.onResume();
        updateLeaderBoard();
    }
    
    private void updateLeaderBoard(){

        // This is where the we call the php script to update leaderboard. Second argument is null because
        // we are not POSTing any JSON
        new PostJSONDataAsyncTask(this, null, ServerAddresses.getLeaderBoardURL , false)
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
                    if(jsonObject.length() > 0){
                        parseAndAddTeamsToLeaderboard(jsonObject);
                    }
                    else {
                        //TODO Do something here if no teams have been made yet
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
    }

    /**
     * Method to parse the JSON response from server and update the ListView
     * 
     * @param jsonObject
     */
    private void parseAndAddTeamsToLeaderboard(JSONObject jsonObject){
        // Clear the old teams list
        teamList.clear();
        
        int length = jsonObject.length();
        
        // Starts at one and goes up to whatever
        for (int i = 1; i <= length; i++) {
            String key = Integer.toString(i);
            try
            {
                JSONObject teamObject = (JSONObject) jsonObject.get(key);
                String teamName = teamObject.getString("team_name");
                String distance = teamObject.getString("distance");
                teamList.add(new Team(teamName, Double.valueOf(distance)));
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
        
        // Notify Adapter after data change
        lbAdapater.notifyDataSetChanged();
    }

    // Helper method to set the status text view at the bottom 
    private void updateStatusText(String message) {
        statusEditText.setText("(" + Util.getTimeStamp() + "): " + message);
    }
    
}