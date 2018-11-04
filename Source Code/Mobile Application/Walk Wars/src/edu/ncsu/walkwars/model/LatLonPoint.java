package edu.ncsu.walkwars.model;

import com.google.android.maps.GeoPoint;

/**
 *  A way to create a GeoPoint directly from lat/lon coordinates
 */

public class LatLonPoint extends GeoPoint
{
   public LatLonPoint(double latitude, double longitude)
    {
        super((int) (latitude * 1E6), (int) (longitude * 1E6));
    }
}
