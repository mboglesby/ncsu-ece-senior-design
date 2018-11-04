package edu.ncsu.walkwars.activity;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.Toast;
import edu.ncsu.walkwars.logic.PostJSONDataAsyncTask;
import edu.ncsu.walkwars.model.Player;
import edu.ncsu.walkwars.util.ServerAddresses;
import edu.ncsu.walkwars.util.Util;

public class MainApplication extends Application
{
    private static final long INTERVAL      = 10000; // in ms
    private static final long INITIAL_DELAY = 50;    // in ms


    public boolean getWiFiData = false;
    public MapviewActivity mapViewActivity = null;
    
    private Timer timer = null;
    private WifiManager wifi = null;
    private BroadcastReceiver wifiDataReceiver = null;
    private Player player = null; // Keeps track of player
    private Util util = null;
    private Context baseContext = null;

    @Override
    public void onCreate()
    {
        super.onCreate();
        baseContext = this.getBaseContext();
        
        // Set user name and team name
        util = new Util(baseContext);
        
        // -------------------------------------------------------------------------------
        // Setup wifi manager
        // -------------------------------------------------------------------------------
        wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        if (wifi.isWifiEnabled() == false)
        {
            Toast.makeText(baseContext, "Wi-Fi is disabled. Please enable Wi-Fi", Toast.LENGTH_LONG).show();
            // wifi.setWifiEnabled(true);
        }

        // Register the custom wi-fi receiver
        wifiDataReceiver = new WiFiListener();
        registerReceiver(wifiDataReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        
        // If AutoUpdate is on, start the timer thread
        if( util.autoUpdateIsOn() ){
            Log.d(Util.DEBUG, "In MainApplication Auto update is turned on");
            startTimer();
        }
    }


    @Override
    public void onTerminate()
    {
        Log.d(Util.DEBUG, "Terminate MainApplication");
        super.onTerminate();
        unregisterReceiver(wifiDataReceiver);
        this.cancelTimer();
    }

    public WifiManager getWifiManager()
    {
        return wifi;
    }

    public Player getPlayer()
    {
        return player;
    }

    public void setPlayer(Player player)
    {
        this.player = player;
    }
    
    public void cancelTimer() {
        timer.cancel();
        Log.d(Util.DEBUG, "Auto update timer cancelled");
    }
    
    
    public void startTimer() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new UpdateLocationTask(), INITIAL_DELAY, INTERVAL);
        Log.d(Util.DEBUG, "Auto update timer started");
    }
    
    /**
     * Start Wi-Fi scan if conditions are met. If for any reason
     * the scan didn't start/failed, false is returned
     * 
     * @return success/fail boolean
     */
    public boolean triggerWifiScan () {
        Log.d(Util.DEBUG, "WiFi Scan Triggered");
        if (wifi.isWifiEnabled() && util.isLoggedIn())
        {
            if (wifi.startScan() == true)
            {
                Log.d(Util.DEBUG, "Scan Started");
                return true;
            }
            else
            {
                Log.d(Util.DEBUG, "Did not start scan");
                return false;
            }
        }
        return false;
    }


    // ==================================================================
    // UpdateLocationTask custom class
    // ==================================================================
    private class UpdateLocationTask extends TimerTask
    {
        public void run()
        {
            triggerWifiScan();
        }
    }
    
    // ==================================================================
    // WiFiListener custom class
    // ==================================================================
    private class WiFiListener extends BroadcastReceiver
    {

        @Override
        public void onReceive(final Context context, Intent intent)
        {
            Log.d(Util.DEBUG, "onReceive in WIFI");
            List<ScanResult> results = wifi.getScanResults();
            if (results == null || results.size() == 0) {
                // Log.d(DEBUG, "No results");
                return;
            }
            
            // Log.d(Util.DEBUG, results.toString());

            /*-
            // Remove all entries that aren't cent-mesh nodes for (int i = 0;
            for(int i = 0; i < results.size(); i++) { 
                ScanResult result = results.get(i);
                if( !result.SSID.equalsIgnoreCase("CentMESH") ){
                    results.remove(i); 
                } 
            }
            */

            // Finding the strongest signal
            int signalStrength = -200;
            int strongestSignalIndex = 0;

            for (int i = 0; i < results.size(); i++)
            {
                ScanResult result = results.get(i);

                if (result.level > signalStrength)
                {
                    signalStrength = result.level;
                    strongestSignalIndex = i;
                }
            }

            ScanResult strongestSignal = results.get(strongestSignalIndex);
            String macAddress = strongestSignal.BSSID;
            //macAddress = "06:02:6F:A7:3E:D8";
            
            JSONObject macAddressJSON = new JSONObject();
            try
            {
                macAddressJSON.put("user_name", util.getPlayerName());
                macAddressJSON.put("mac_address", macAddress);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            
            Log.d(Util.DEBUG, macAddressJSON.toString());
            
            // Show strongest signal
            // Toast.makeText(getBaseContext(), strongestSignal.SSID, Toast.LENGTH_SHORT).show();
            
            // ----------------------------------------------
            // CALL Update to server code here            
            // ----------------------------------------------            
            // This is where the we call the php script to update user location in server
            new PostJSONDataAsyncTask(context, macAddressJSON, ServerAddresses.getZoneURL, false)
            {
                // Override the onPostExecute to do whatever you want
                @Override
                protected void onPostExecute(String response)
                {
                    super.onPostExecute(response);
                    
                    if (response != null)
                    {
                        JSONObject jsonObject = Util.convertStringToJSONObject(response);
                        
                        if(jsonObject == null){
                            if(mapViewActivity!=null)
                            {
                                mapViewActivity.updateStatusText("Error parsing server response");
                            }
                            return;
                        }
                        
                        try
                        {
                            if (jsonObject.has("error"))
                            {
                                String errorMessage = jsonObject.getString("error").toString();
                                // Check errors
                                if(errorMessage.equalsIgnoreCase("Player not found."))
                                {
                                    errorMessage += " Make sure you're logged in";
                                }
                                else if(errorMessage.equalsIgnoreCase("MAC Address not found."))
                                {
                                    errorMessage = "Outside of known areas";
                                    // Update function is called here
                                    if(mapViewActivity!=null)
                                    {
                                        mapViewActivity.changeCurrentZoneToPreviousZone();
                                    };
                                }
                                Log.d(Util.DEBUG, errorMessage);
                                // Util.popToast (context,"Update: " + errorMessage);
                                if(mapViewActivity!=null)
                                {
                                    mapViewActivity.updateStatusText("Update: " + errorMessage);
                                }
                                //updateStatusText("Update: " + errorMessage);
                            }
                            else
                            {
                                // TODO: Needs to implement the this on server side
                                int current_zone_id = jsonObject.getInt("current_zone_id");
                                int new_total_distance = jsonObject.getInt("new_total_distance");
                                util.storePlayerScore((float) new_total_distance);
                                
                                // Update function is called here
                                if(mapViewActivity!=null)
                                {
                                    mapViewActivity.updateUserLocationAndScore(current_zone_id, new_total_distance);
                                };
                            }
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                        
                    }
                    else
                    {
                        Toast.makeText(baseContext, "Error connecting to server", Toast.LENGTH_LONG).show();
                    }

                }

            }.execute();           
            
        }
    }
    
    
    
    /*
    public String getPlayerName()
    {
        return playerName;
    }

    public void setPlayerName(String playerName)
    {
        this.playerName = playerName;
    }

    public String getTeamName()
    {
        return teamName;
    }

    public void setTeamName(String teamName)
    {
        this.teamName = teamName;
    }
    */
}
