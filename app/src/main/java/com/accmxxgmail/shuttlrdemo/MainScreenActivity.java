package com.accmxxgmail.shuttlrdemo;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

public class MainScreenActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private RelativeLayout rlOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(false);
        toggle.setDrawerIndicatorEnabled(false);
        toggle.setHomeAsUpIndicator(R.drawable.icon_toolbar_account);

        toggle.setToolbarNavigationClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });

        ChangeFragment(new NearMeShuttleFragment(), R.id.fragment_near_me_place1);
        ChangeFragment(new NearMeShuttleFragment(), R.id.fragment_near_me_place2);
        ChangeFragment(new NearMeShuttleFragment(), R.id.fragment_near_me_place3);
        ChangeFragment(new NearMeShuttleFragment(), R.id.fragment_near_me_place4);

        rlOverlay = (RelativeLayout) findViewById(R.id.rlOverlay);
        rlOverlay.setVisibility(View.GONE);

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_payment) {
            startActivity(new Intent(this,PaymentActivity.class));
        }
        else if (id == R.id.nav_recent) {
            startActivity(new Intent(this,RecentTripsActivity.class));
        }
        else if (id == R.id.nav_help) {
            rlOverlay.setVisibility(View.VISIBLE);
        }
        else if (id == R.id.nav_settings) {
            startActivity(new Intent(this,SettingsActivity.class));
        }
        else if (id == R.id.nav_logout) {
            finish();
            startActivity(new Intent(this,LoginActivity.class));
        }
        else if (id == R.id.nav_legal) {
            startActivity(new Intent(this,LegalActivity.class));
        }
        else if (id == R.id.nav_about) {
            startActivity(new Intent(this,AboutSidebarActivity.class));
        }
        else if (id == R.id.nav_report) {
            startActivity(new Intent(this,ReportBugActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void toolbarClickMap(View view){
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    public void requestRide(View view){
        Intent intent = new Intent(this, RequestRideActivity.class);
        startActivity(intent);
    }

    public void myRideStatus(View view){

    }

    public void ChangeFragment(Fragment fragment, int resource){
        FragmentManager fmanager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fmanager.beginTransaction();
        fragmentTransaction.replace(resource,fragment);
        fragmentTransaction.commit();
    }


    public void onHelpScreenClick(View view) {
        if (rlOverlay.getVisibility()!=View.GONE) {
            rlOverlay.setVisibility(View.GONE);
        }
    }
}
