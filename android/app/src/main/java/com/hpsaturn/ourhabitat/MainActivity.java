package com.hpsaturn.ourhabitat;

import android.Manifest;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.hpsaturn.ourhabitat.fragments.MapTasksFragment;

/**
 * Created by Antonio Vanegas @hpsaturn on 4/9/15.
 */

public class MainActivity extends BaseActivity implements OnMapReadyCallback{

    private MapTasksFragment mMapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initDrawer();
        showMapFragment();
        initPermissionsFlow();

    }

    private void showMapFragment() {

        if (mMapFragment == null) mMapFragment = new MapTasksFragment();
        if (mMapFragment != null && !mMapFragment.isVisible()) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_default, mMapFragment, MapTasksFragment.TAG);
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
    public void onMapReady(GoogleMap googleMap) {
        mMapFragment.initMap(googleMap);
    }


    public void initPermissionsFlow(){
        loadPermissions(Manifest.permission.ACCESS_FINE_LOCATION, PERMISSIONS_REQUEST_FINE_LOCATION);
    }

}
