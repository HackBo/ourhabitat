package com.hpsaturn.ourhabitat;

import android.Manifest;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.hpsaturn.ourhabitat.fragments.AddDialogFragment;
import com.hpsaturn.ourhabitat.fragments.MapHabitatFragment;

/**
 * Created by Antonio Vanegas @hpsaturn on 4/9/15.
 */

public class MainActivity extends BaseActivity implements OnMapReadyCallback{

    public static final String TAG = MainActivity.class.getSimpleName();
    private static final boolean DEBUG = Config.DEBUG;


    private MapHabitatFragment mMapFragment;
    private Location lastLocation;
    private AddDialogFragment addDialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initDrawer();
        showMapFragment();
        initPermissionsFlow();
        fabSetOnClickListener(onAddButtonListener);

    }

    private View.OnClickListener onAddButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(DEBUG) Log.d(TAG, "onAddButtonListener:");
            showAddDialog();
        }
    };


    public void showAddDialog() {
        addDialogFragment = new AddDialogFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(addDialogFragment, AddDialogFragment.TAG);
        ft.show(addDialogFragment);
        ft.commitAllowingStateLoss();
    }

    private void showMapFragment() {

        if (mMapFragment == null) mMapFragment = new MapHabitatFragment();
        if (mMapFragment != null && !mMapFragment.isVisible()) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_default, mMapFragment, MapHabitatFragment.TAG);
            ft.commitAllowingStateLoss();
            mMapFragment.getMapAsync(this);
        }

    }

    @Override
    void showMap() {
        popBackLastFragment();
    }

    @Override
    void showRings() {

    }

    @Override
    void showMain() {

    }

    @Override
    void showHelp() {

    }

    @Override
    void showAbout() {

    }

    @Override
    void menuClearMap() {
        if(mMapFragment!=null)mMapFragment.clearPolygon();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMapFragment.initMap(googleMap);
    }


    public void initPermissionsFlow(){
        loadPermissions(Manifest.permission.ACCESS_FINE_LOCATION, PERMISSIONS_REQUEST_FINE_LOCATION);
    }

    public Location getLastLocation() {
        return lastLocation;
    }

    public AddDialogFragment getAddDialogFragment() {
        return addDialogFragment;
    }
}
