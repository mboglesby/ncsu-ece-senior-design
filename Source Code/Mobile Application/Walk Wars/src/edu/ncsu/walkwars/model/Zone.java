package edu.ncsu.walkwars.model;

import com.google.android.maps.GeoPoint;

public class Zone
{

    private int zoneID = 0;
    private String zoneName = null;
    private GeoPoint geoPoint = null;

    public Zone(int id)
    {
        this.setZoneID(id);
    }
    
    public Zone(int id, String zoneName)
    {
        this.setZoneID(id);
        this.setZoneName(zoneName);
    }
    
    public Zone(int id, String zoneName, GeoPoint geoPoint)
    {
        this.setZoneID(id);
        this.setZoneName(zoneName);
        this.setGeoPoint(geoPoint);
    }

    public int getZoneID()
    {
        return zoneID;
    }

    public void setZoneID(int zoneID)
    {
        this.zoneID = zoneID;
    }

    public String getZoneName()
    {
        return zoneName;
    }

    public void setZoneName(String zoneName)
    {
        this.zoneName = zoneName;
    }

    public GeoPoint getGeoPoint()
    {
        return geoPoint;
    }

    public void setGeoPoint(GeoPoint geoPoint)
    {
        this.geoPoint = geoPoint;
    }
}
