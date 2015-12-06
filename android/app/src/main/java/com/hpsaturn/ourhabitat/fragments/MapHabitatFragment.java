package com.hpsaturn.ourhabitat.fragments;

import android.graphics.Color;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.hpsaturn.ourhabitat.Config;
import com.hpsaturn.ourhabitat.models.Track;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * Created by Antonio Vanegas @hpsaturn on 4/9/15.
 */
public class MapHabitatFragment extends SupportMapFragment implements OnMapClickListener, OnMarkerClickListener, OnMapLongClickListener {

    public static final String TAG = MapHabitatFragment.class.getSimpleName();
    private static final boolean DEBUG = Config.DEBUG && Config.DEBUG_MAP;

    private static final int ANIM_TIME = 1600;
    private GoogleMap map;

    private HashMap<Track, Marker> hmMarks = new HashMap<Track, Marker>();
    private HashMap<Long, String> hmIDs = new HashMap<Long, String>();

    //    private MGPlatformApi platformApi = new MGPlatformApi();
    private Polyline lastPoline;
    private boolean markerClicked;
    PolygonOptions polygonOptions;
    Polygon polygon;

    public void initMap(GoogleMap map) {

        this.map = map;

        if (DEBUG) Log.d(TAG, "initMap..");
        UiSettings settings = map.getUiSettings();

        settings.setAllGesturesEnabled(true);
        settings.setZoomControlsEnabled(false);
        settings.setMyLocationButtonEnabled(true);
        map.setMyLocationEnabled(true);
        map.setOnMarkerClickListener(this);

        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.setOnMapClickListener(this);

        //Setting on long click listener
        map.setOnMapLongClickListener(this);

//        animToPosition(new LatLng(4.65149, -74.05929)); // Default Bogot?

    }


    public void addPoints(List<Track> data) {
        Iterator<Track> it = data.iterator();
        while (it.hasNext()) {
            Track track = it.next();
            if (!hmIDs.containsKey(track.loc.getTime())) addMark(track);
            else if (DEBUG) Log.d(TAG, "skip add mark");
        }
    }

    public void addMark(Track track) {
        if (DEBUG) Log.d(TAG, "addMark: " + track.toString());
        Marker marker = map.addMarker(new MarkerOptions()
                        .position(new LatLng(track.loc.getLatitude(), track.loc.getLongitude()))
                        .title(track.alias)
        );
        hmIDs.put(track.loc.getTime(), marker.getId());
        hmMarks.put(track, marker);
        animToPosition(new LatLng(track.loc.getLatitude(), track.loc.getLongitude()));
        if (DEBUG) Log.d(TAG, "[MAP_FRAGMENT] addMark: " + marker.getId());
    }

    public void animToPosition(LatLng pos) {
        if (pos != null) {
            if (DEBUG)
                Log.d(TAG, "[MAP_FRAGMENT] animateMark at: " + pos.latitude + "," + pos.longitude);
            CameraUpdate center = CameraUpdateFactory.newLatLngZoom(pos, Config.map_zoom_init);
            map.animateCamera(center, ANIM_TIME, null);
        } else if (DEBUG) Log.d(TAG, "[MAP_FRAGMENT] animateMark SKIP! pos is NULL");

    }

    @Override
    public void onMapClick(LatLng point) {
        if (DEBUG) Log.d(TAG, "[MAP_FRAGMENT] onMapClick: " + point.latitude + "," + point.longitude);
//        tvLocInfo.setText(point.toString());
        map.animateCamera(CameraUpdateFactory.newLatLng(point));
        markerClicked = false;
    }

    @Override
    public void onMapLongClick(LatLng point) {
        if (DEBUG) Log.d(TAG, "[MAP_FRAGMENT] onMapLongClick: " + point.latitude + "," + point.longitude);
//        tvLocInfo.setText("New marker added@" + point.toString());
        map.addMarker(new MarkerOptions().position(point).title(point.toString()));
        markerClicked = false;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (DEBUG) Log.d(TAG, "[MAP_FRAGMENT] onMarkerClick: " + marker.getId());
        if (markerClicked) {

            if (polygon != null) {
                polygon.remove();
                polygon = null;
            }

            polygonOptions.add(marker.getPosition());
            polygonOptions.strokeColor(Color.RED);
            polygonOptions.fillColor(Color.BLUE);
            polygon = map.addPolygon(polygonOptions);
        } else {
            if (polygon != null) {
                polygon.remove();
                polygon = null;
            }

            polygonOptions = new PolygonOptions().add(marker.getPosition());
            markerClicked = true;
        }

        return true;
    }


//
//    public void paintRoute(RouteResponse routeResponse) {
//
//        GeometryList routeLines = routeResponse.geometry;
//        if (routeLines != null) {
//            PolylineOptions rectOptions = new PolylineOptions();
//            for (ArrayList<Double> pairLatLon : routeLines.coordinates) {
//                rectOptions.add(new LatLng(pairLatLon.get(1), pairLatLon.get(0)));
//            }
//
//            if (lastPoline != null) lastPoline.remove();
//            lastPoline = map.addPolyline(rectOptions);
//            fixZoom(lastPoline);
//
//        } else {
//
//            if (DEBUG) Log.d(TAG, "[MAP_FRAGMENT] paintRoute !FAIL!");
//            UITools.showToast(getActivity(), R.string.toast_route_traced_error);
//
//        }
//
//    }


    private void fixZoom(Polyline route) {

        List<LatLng> points = route.getPoints();
        LatLngBounds.Builder bc = new LatLngBounds.Builder();

        for (LatLng item : points) {
            bc.include(item);
        }
        map.moveCamera(CameraUpdateFactory.newLatLngBounds(bc.build(), 50));

    }


    public void addPoints(Map<String, Object> tracks, String trackId, String alias) {
        if (tracks != null) {
            List<Track> data = new ArrayList<>();
            if (DEBUG) Log.d(TAG, "data: " + tracks.toString());
            Iterator<Object> it = tracks.values().iterator();
            while (it.hasNext()) {
                Map<String, Object> fbtrack = (Map<String, Object>) it.next();
                Location loc = new Location("");
                loc.setLatitude(Double.parseDouble(fbtrack.get("latitude").toString()));
                loc.setLongitude(Double.parseDouble(fbtrack.get("longitude").toString()));
                loc.setAccuracy(Float.parseFloat(fbtrack.get("accuracy").toString()));
//                        loc.setBearing(Float.parseFloat(track.get("bearing").toString()));
                loc.setTime(Long.parseLong(fbtrack.get("time").toString()));
                Track track = new Track();
                track.trackId = trackId;
                track.loc = loc;
                track.alias = alias;
                data.add(track);
            }
            addPoints(data);
        } else if (DEBUG) Log.w(TAG, "no data");
    }

    public void clearPolygon() {
        if(polygon!=null)polygon.remove();
        map.clear();
    }
}
