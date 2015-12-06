package com.hpsaturn.ourhabitat;

import android.Manifest;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.hpsaturn.ourhabitat.fragments.MapHabitatFragment;

/**
 * Created by Antonio Vanegas @hpsaturn on 4/9/15.
 */

public class MainActivity extends BaseActivity implements OnMapReadyCallback{

    private MapHabitatFragment mMapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initDrawer();
        showMapFragment();
        initPermissionsFlow();

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

}
