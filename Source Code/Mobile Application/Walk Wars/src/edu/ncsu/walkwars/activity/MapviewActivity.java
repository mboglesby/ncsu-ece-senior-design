package edu.ncsu.walkwars.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import edu.ncsu.walkwars.R;
import edu.ncsu.walkwars.model.LatLonPoint;
import edu.ncsu.walkwars.model.MapItemizedOverlay;
import edu.ncsu.walkwars.model.Zone;
import edu.ncsu.walkwars.model.ZoneOverlay;
import edu.ncsu.walkwars.model.ZoneOverlay.ZONETYPE;
import edu.ncsu.walkwars.util.Util;

    /*
    List of coordinates for CentMESH nodes
    
    > > > 1   35.768437,-78.678036
    > > > 2   35.76976,-78.678043
    > > > 3   35.770859,-78.677418
    > > > 4   35.771659,-78.67756
    > > > 5   35.772434,-78.677454
    > > > 6   35.773494,-78.677183
    > > > 7   35.774576,-78.672047
    > > > 8   35.775217,-78.67139
    > > > 9   35.771051,-78.676465
    > > > 10  35.771586,-78.673747
    > > > 11  35.770659,-78.674085
    > > > 12  35.772648,-78.673589
    > > > 13  35.772435,-78.674515
    > > > 14  35.770006,-78.676564
    > > > 15  35.770590,-78.673847
    > > > 16  35.770600,-78.673947
    > > > 17  35.770625,-78.674000
    
     */
    
    /*
    New Zones
    
    21. EB3 Computer Lab - 35.771290,-78.673643
    22. EB2 Archway      - 35.771906,-78.67389
    23. EB1 Lobby        - 35.771625,-78.675054
    24. Innovation Cafe  - 35.773606,-78.673376
    
     */
    
public class MapviewActivity extends MapActivity
{
    private MainApplication application = null;
    private MapController mapController = null;
    private List<Overlay> mapOverlays = null;
    private MapView mapView = null;
    
    private Util util = null;
    private ToggleButton autoUpdateToggleButton = null;
        
    private LatLonPoint defaultCenterPoint = new LatLonPoint(35.771342,-78.67432);
    private int defaultZoomLevel = 20;
    private Context context;
        
    private ArrayList<Zone> listOfZones = new ArrayList<Zone>();
    //private ArrayList<ZoneOverlay> listOfZoneOverlays = new ArrayList<ZoneOverlay>();
    private MapItemizedOverlay<OverlayItem> itemizedoverlay;
    private TextView updateLocStatusTextView;
    
    private float ZONE_CIRCLE_RADIUS = 15f;

    /**
     * The idea is to first add all the marker overlays to mapOverlay, then add radius overlays to mapOverlay.
     * The last item of mapOverlay will be the most recent zone which will be shown in a different color.
     * Remove the last item of mapOverlay when updating, and add a new one for the new zone
     */
    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapview);
        context = this;
        
        // Setup map view
        mapView = (MapView) findViewById(R.id.mapview);
        mapView.setBuiltInZoomControls(false);
        mapView.setSatellite(false);

        // Setup map controller
        mapController = mapView.getController();
        mapController.setZoom(defaultZoomLevel);
        mapController.setCenter(defaultCenterPoint);
        mapOverlays = mapView.getOverlays();
        
        application = (MainApplication) MapviewActivity.this.getApplication();
        application.mapViewActivity = this;

        
        updateLocStatusTextView = (TextView) findViewById(R.id.updateLocStatusTextView);
        autoUpdateToggleButton = (ToggleButton) findViewById(R.id.autoUpdateToggleButton);
        
        util = new Util(context);
        
        // Set the autoUpdateToggleButton state based on previous setting
        if( util.autoUpdateIsOn() ){
            Log.d(Util.DEBUG, "Auto update is turned on");
            autoUpdateToggleButton.setChecked(true);
        }
        else {
            Log.d(Util.DEBUG, "Auto update is turned off");
            autoUpdateToggleButton.setChecked(false);
        }
        
        // Adding individual markers
        Drawable drawable = this.getResources().getDrawable(R.drawable.icon_marker);
        itemizedoverlay = new MapItemizedOverlay<OverlayItem>(drawable, this);
        mapOverlays.add(itemizedoverlay);

        // Add the zones to map view
        getListOfZones(null);
        displayListOfZonesOnMapView();

        // Set up the manual update button
        setupManualUpdateButton();
        
        // Log for 
        Log.d(Util.DEBUG, "Length of mapOverlay list: " + String.valueOf(mapOverlays.size()));
        updateStatusText("Awaiting Valid Update");

    }
    
    // --------------------------------------------------------------------------------
    // CREATE ALL THE ZONES HERE
    // --------------------------------------------------------------------------------
    private void getListOfZones(Object zoneInfo){ 
        ArrayList<GeoPoint> listOfNodes = new ArrayList<GeoPoint>();
        
        /*
        // Just add all the zones' coordinates here to have an ArrayList of all the points. Easier this way since ItemizedOverlay handles setting markers
        //  Currently there are just 6 zones based on CENTMESH. I've commented out the rest since the other nodes aren't working (supposedly)
        listOfNodes.add(new LatLonPoint(35.768437,-78.678036)); // 1
        //listOfNodes.add(new LatLonPoint(35.76976,-78.678043));  // 2
        //listOfNodes.add(new LatLonPoint(35.770859,-78.677418)); // 3
        listOfNodes.add(new LatLonPoint(35.771659,-78.67756));  // 4
        listOfNodes.add(new LatLonPoint(35.772434,-78.677454)); // 5
        listOfNodes.add(new LatLonPoint(35.773494,-78.677183)); // 6
        //listOfNodes.add(new LatLonPoint(35.774576,-78.672047)); // 7
        //listOfNodes.add(new LatLonPoint(35.775217,-78.67139));  // 8
        listOfNodes.add(new LatLonPoint(35.771051,-78.676465)); // 9
        //listOfNodes.add(new LatLonPoint(35.771586,-78.673747)); // 10
        //listOfNodes.add(new LatLonPoint(35.770659,-78.674085)); // 11
        //listOfNodes.add(new LatLonPoint(35.772648,-78.673589)); // 12
        //listOfNodes.add(new LatLonPoint(35.772435,-78.674515)); // 13
        listOfNodes.add(new LatLonPoint(35.770006,-78.676564)); // 14 35770590,-78673847

        //listOfNodes.add(new LatLonPoint(35.770590,-78.673847)); // 15
        //listOfNodes.add(new LatLonPoint(35.770600,-78.673947)); // 16
        //listOfNodes.add(new LatLonPoint(35.770625,-78.674000)); // 17
        */
        
        listOfNodes.add(new LatLonPoint(35.77129,-78.673643));   // 21
        listOfNodes.add(new LatLonPoint(35.771906, -78.67389));  // 22
        listOfNodes.add(new LatLonPoint(35.771625, -78.675054)); // 23
        listOfNodes.add(new LatLonPoint(35.773606, -78.673376)); // 24
        listOfNodes.add(new LatLonPoint(35.782713,-78.685123)); // 24

        

        // Create the new zones here
        Zone eb3 = new Zone(21, "EB3 Computer Lab", new LatLonPoint(35.77129,-78.673643));
        Zone eb2 = new Zone(22, "EB2 Archway/Lobby", new LatLonPoint(35.771906, -78.67389));
        Zone eb1 = new Zone(23, "EB1 Lobby", new LatLonPoint(35.771625, -78.675054));
        Zone ic = new Zone(24, "Innovation Cafe", new LatLonPoint(35.773606, -78.673376));
        Zone sd = new Zone(30, "ECE Senior Design Day", new LatLonPoint(35.782713,-78.685123));

        // Adding zones to the list
        listOfZones.add(eb3);
        listOfZones.add(eb2);
        listOfZones.add(eb1);
        listOfZones.add(ic);;
        listOfZones.add(sd);
        
        // Adding the list of nodes to itemized overlay
        for(int i=1; i<=listOfNodes.size(); i++)
        {
            // Add a marker to the center of zones
            GeoPoint geo = listOfNodes.get(i-1);
            OverlayItem overlayitem = new OverlayItem(geo, "", "");
            itemizedoverlay.addOverlay(overlayitem);
        }
        
        // Create CENTMESH zones here
        /*-
        for(int i=1; i<=listOfNodes.size(); i++)
        {
            GeoPoint geo = listOfNodes.get(i-1);
            Zone newZone = new Zone(i, null, geo);
            listOfZones.add(newZone);            
        }
        */
        
        /*
        // Create a script to automatically generate this code
        Zone zone1 = new Zone(1, null, new LatLonPoint(35.768437,-78.678036));
        Zone zone4 = new Zone(4, null, new LatLonPoint(35.771659,-78.67756));
        Zone zone5 = new Zone(5, null, new LatLonPoint(35.772434,-78.677454));
        Zone zone6 = new Zone(6, null, new LatLonPoint(35.773494,-78.677183));
        Zone zone9 = new Zone(9, null, new LatLonPoint(35.771051,-78.676465));
        Zone zone14 = new Zone(14, null, new LatLonPoint(35.770006,-78.676564));
        
        listOfZones.add(zone1);
        listOfZones.add(zone4);
        listOfZones.add(zone5);
        listOfZones.add(zone6);
        listOfZones.add(zone9);
        listOfZones.add(zone14);
        
        */
        

    }

    // --------------------------------------------------------------------------------
    // Add the list of zones to the mapview
    // --------------------------------------------------------------------------------
    private void displayListOfZonesOnMapView(){
        
        // Create and add individual map overlays to the map that shows a radius and display the zone name above the marker
        for(Zone zone: listOfZones){
            ZoneOverlay zoneOverlay = new ZoneOverlay(context, zone.getGeoPoint(), ZONE_CIRCLE_RADIUS, zone, ZoneOverlay.ZONETYPE.REGULAR);
            //listOfZoneOverlays.add(zoneOverlay);
            mapOverlays.add(zoneOverlay);
        }
    }
    
    private void setupManualUpdateButton()
    {
        Button buttonOne = (Button) findViewById(R.id.manualUpdateButton);
        buttonOne.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                boolean success = application.triggerWifiScan();
                if (!success) {
                   Toast.makeText(getBaseContext(), "Wi-Fi scan unsuccessful. Please be sure that Wi-Fi is enabled and you are logged in", Toast.LENGTH_LONG).show();
                   updateStatusText("Check if Wi-Fi is enabled and you are logged in");
                }
            }
        });
    }

    @Override
    protected boolean isRouteDisplayed()
    {
        return false;
    }
    
    /**
     * Handle toggle of the auto update button
     * @param view
     */
    public void onToggleClicked(View view) {
        // Get Toggle button state
        boolean on = ((ToggleButton) view).isChecked();
        
        if (on) {
            // Enable auto update
            application.startTimer();
            util.turnOnAutoUpdate();
            Log.d(Util.DEBUG, "Turning on auto update");
            Toast.makeText(context, "Turning on auto update", Toast.LENGTH_SHORT).show();
        } 
        else {
            // Disable auto update
            application.cancelTimer();
            util.turnOffAutoUpdate();
            Log.d(Util.DEBUG, "Turning off auto update");
            Toast.makeText(context, "Turning off auto update", Toast.LENGTH_SHORT).show();
        }
    }
    
    public void updateStatusText(String message){
        updateLocStatusTextView.setText("(" + Util.getTimeStamp() + "): " + message);
    }

    /**
     * Return a Zone object based on a zone id
     * 
     * @param id
     * @return
     */
    private Zone getZoneFromId(int id)
    {
        Zone mZone = null;
        for(Zone zone: listOfZones)
        {
            if(zone.getZoneID() == id)
            {
                mZone = zone;
                break;
            }
        }
        
        if(mZone == null)
        {
            Log.d("DEBUG", "Unknown zone ID");
            return null;
        }
        else
        {
            return mZone;
        }
    }
    
    public void changeCurrentZoneToPreviousZone()
    {
        // Find the "previous" zone type and old "current" zone type
        ZoneOverlay previousZoneOverlay = null;
        ZoneOverlay oldCurrentZone = null;
        
        for(Overlay overlay: mapOverlays)
        {
            if(overlay.getClass().getSimpleName().equals("ZoneOverlay"))
            {
                ZoneOverlay zoneOverlay = (ZoneOverlay) overlay;
                if (zoneOverlay.getZoneType() == ZONETYPE.PREVIOUS)
                {
                    previousZoneOverlay = zoneOverlay;
                }
                if (zoneOverlay.getZoneType() == ZONETYPE.CURRENT)
                {
                    oldCurrentZone = zoneOverlay;
                }
            }
        }
        

        // 1. Remove the previous zone type
        if(previousZoneOverlay != null)
        {
            mapOverlays.remove(previousZoneOverlay);
        }
        
        
        
        // 2. Create a "previous" zone out of it the old current zone
        if(oldCurrentZone != null)
        {
            // 2. If there was a currentZone, add a previous zone based on it
            Zone oldCZone =  oldCurrentZone.getZone();
            mapOverlays.remove(oldCurrentZone);

            ZoneOverlay zoneOverlay = new ZoneOverlay(context, oldCZone.getGeoPoint(), ZONE_CIRCLE_RADIUS, oldCZone, ZoneOverlay.ZONETYPE.PREVIOUS);
            mapOverlays.add(zoneOverlay);
            
            mapView.invalidate();
        }
    }
    

    
    public void updateUserLocationAndScore(int currentZoneID, int score)
    {

        // 1. Set score
        util.storePlayerScore((float) score);
        updateStatusText("Update success: Zone " + Integer.toString(currentZoneID));
        
        // 2. First check if the last current zone is the same as this latest zone update
        for(Overlay overlay: mapOverlays)
        {
            if(overlay.getClass().getSimpleName().equals("ZoneOverlay"))
            {
                ZoneOverlay zoneOverlay = (ZoneOverlay) overlay;

                if (zoneOverlay.getZoneType() == ZONETYPE.CURRENT)
                {
                    if(currentZoneID == zoneOverlay.getZone().getZoneID())
                    {
                        // This means the last current zone is the same as the latest current zone
                        // Simply return to do nothing here
                        mapController.animateTo(zoneOverlay.getZone().getGeoPoint());
                        Log.d(Util.DEBUG, "Last current zone is the same as the latest current zone. Nothing is done.");
                        return;
                    }
                }
            }
        }
        
        // 3. Change the "Current" zone to a "Previous" zone if applicable
        changeCurrentZoneToPreviousZone();
        
        // 4. Add new overlay for current zone
        Zone newCurrentZone = getZoneFromId(currentZoneID);
        if(newCurrentZone != null)
        {
            ZoneOverlay zoneOverlay = new ZoneOverlay(context, newCurrentZone.getGeoPoint(), ZONE_CIRCLE_RADIUS, newCurrentZone, ZoneOverlay.ZONETYPE.CURRENT);
            mapOverlays.add(zoneOverlay);
            mapController.animateTo(newCurrentZone.getGeoPoint());
        }
        else
        {
            Log.d(Util.DEBUG, "The returned zone ID is not recognized."); 
            updateStatusText("Update Failed: Unknown Zone ID");
        }
        
        
        /*
        ZoneOverlay zoneOverlay = new ZoneOverlay(context, currentZone.getGeoPoint(), ZONE_CIRCLE_RADIUS, currentZone, ZoneOverlay.ZONETYPE.CURRENT);
        mapOverlays.add(zoneOverlay);
        mapView.invalidate();
        mapController.animateTo(currentZone.getGeoPoint());
        
        
        updateStatusText("Update success: in zone " + Integer.toString(zoneID));
       */
    }

    
}
