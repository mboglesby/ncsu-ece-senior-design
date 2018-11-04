package edu.ncsu.walkwars.model;


import java.util.ArrayList;

import android.graphics.drawable.Drawable;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

import edu.ncsu.walkwars.activity.MapviewActivity;

@SuppressWarnings("hiding")
public class MapItemizedOverlay<MapItemizedOverlay> extends ItemizedOverlay<OverlayItem> {

    private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();


    public MapItemizedOverlay(Drawable defaultMarker, MapviewActivity mapViewActivity) {
        super(boundCenterBottom(defaultMarker));
      }


    @Override
    protected OverlayItem createItem(int i)
    {
        return mOverlays.get(i);
    }

    @Override
    public int size()
    {
        return mOverlays.size();

    }

    public void addOverlay(OverlayItem overlay) {
        mOverlays.add(overlay);
        populate();
    }
}