package edu.ncsu.walkwars.model;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextPaint;
import android.util.FloatMath;
import android.util.Log;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

import edu.ncsu.walkwars.util.Util;

// http://stackoverflow.com/questions/5293709/draw-a-circle-on-android-mapview

public class ZoneOverlay extends Overlay
{

    private static final float FONT_SIZE = 20;
    private static final int TITLE_MARGIN = 6;
    
    //private double lat;
    //private double lon;
    private float radius;
    private GeoPoint geoPoint = null;
    
    private int color;
    private int opacity;
    
    private Zone zone;


    private ZONETYPE zoneType;
    
    private int COLOR_REGULAR_ZONE = Color.RED;
    private int COLOR_CURRENT_ZONE = Color.BLUE;//Color.BLUE;
    private int COLOR_PREVIOUS_ZONE = Color.BLUE;//Color.GREEN;

    private int OPACITY_REGULAR_ZONE = 75;
    private int OPACITY_CURRENT_ZONE = 90;
    private int OPACITY_PREVIOUS_ZONE = 45;
    
    
    public enum ZONETYPE
    {
    	REGULAR, PREVIOUS, CURRENT
    }

    // Overloaded constructor by me to use existing GeoPoints
    public ZoneOverlay(Context context, GeoPoint geoPoint, float radius, Zone zone, ZONETYPE zoneType)
    {
        // this.context = _context;
        //this.color = color;
        //this.opacity = opacity;
        
        this.geoPoint = geoPoint;
        this.radius = radius;
        this.zone = zone;
        this.setZoneType(zoneType);
        
        switch (zoneType) {
        
        case REGULAR:
            this.color = COLOR_REGULAR_ZONE;
            this.opacity = OPACITY_REGULAR_ZONE;
            break;
                
        case PREVIOUS:
            this.color = COLOR_PREVIOUS_ZONE;
            this.opacity = OPACITY_PREVIOUS_ZONE;
            break;
                     
        case CURRENT:
            this.color = COLOR_CURRENT_ZONE;
            this.opacity = OPACITY_CURRENT_ZONE;
            break;
                    
        default:
            Log.d(Util.DEBUG, "Default Case. Shouldn't be here");
            break;
        }
    }

    public void draw(Canvas canvas, MapView mapView, boolean shadow)
    {
        super.draw(canvas, mapView, shadow);

        if (shadow)
        {
            return; // Ignore the shadow layer
        }

        Projection projection = mapView.getProjection();
        Point pt = new Point();
        GeoPoint geo = geoPoint;

        // Draw Circle ----------------
        projection.toPixels(geo, pt);
        float circleRadius = projection.metersToEquatorPixels(radius) * (1 / FloatMath.cos((float) Math.toRadians(geo.getLatitudeE6()/1e6)));

        Paint innerCirclePaint = new Paint();;
        innerCirclePaint.setColor(color);
        innerCirclePaint.setAlpha(opacity);
        innerCirclePaint.setAntiAlias(true);
        innerCirclePaint.setStyle(Paint.Style.FILL);

        canvas.drawCircle((float) pt.x, (float) pt.y, circleRadius, innerCirclePaint);

        
        
        // Draw Text --------------------
        
        /* Find the width and height of the title */
        TextPaint paintText = new TextPaint();
        Paint paintRect = new Paint();
        
        String overlayText = "";
        
        if(zone.getZoneName() == null)
        {
            overlayText = "Zone " + String.valueOf(zone.getZoneID());
        }
        else
        {
            overlayText = "Zone " + String.valueOf(zone.getZoneID()) + ": " + zone.getZoneName();
        }
        Rect rect = new Rect();
        paintText.setTextSize(FONT_SIZE);
        paintText.getTextBounds(overlayText, 0, overlayText.length(), rect);

        rect.inset(-TITLE_MARGIN, -TITLE_MARGIN);
        
        int markerHeight = 34;
        //rect.offsetTo(pt.x - rect.width()/2, pt.y - markerHeight  - rect.height());
        rect.offsetTo(pt.x - rect.width()/2, pt.y - markerHeight  - rect.height() - 2); // -2 for some extra

        paintText.setTextAlign(Paint.Align.CENTER);
        paintText.setTextSize(FONT_SIZE);
        paintText.setARGB(255, 255, 255, 255);
        paintRect.setARGB(130, 0, 0, 0);

        canvas.drawRoundRect(new RectF(rect), 2, 2, paintRect);
        canvas.drawText(overlayText, rect.left + rect.width() / 2, rect.bottom - TITLE_MARGIN, paintText);

    }

    public ZONETYPE getZoneType()
    {
        return zoneType;
    }

    public void setZoneType(ZONETYPE zoneType)
    {
        this.zoneType = zoneType;
    }
    
    public Zone getZone()
    {
        return zone;
    }

    public void setZone(Zone zone)
    {
        this.zone = zone;
    }

}