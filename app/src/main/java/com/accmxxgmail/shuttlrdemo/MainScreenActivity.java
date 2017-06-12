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
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainScreenActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ValueEventListener{

    private RelativeLayout rlOverlay;
    TextView profileName, companyName;

    SessionManagement session;

    String url = "https://active-mountain-168417.firebaseio.com/users/" + EncodeEmail(UserDetails.email);
    private DatabaseReference mRootReference = FirebaseDatabase.getInstance().getReferenceFromUrl(url);
    private DatabaseReference mNameReference = mRootReference.child("name");
    private DatabaseReference mAddressReference = mRootReference.child("address");
    private DatabaseReference mCompanyReference = mRootReference.child("company");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        session = new SessionManagement(getApplicationContext());
        session.checkLogin();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header =navigationView.getHeaderView(0);

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

        LinearLayout linearLayout = (LinearLayout)header.findViewById(R.id.nav_header_profile_and_company);
        linearLayout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                startActivity(new Intent(MainScreenActivity.this,FirstTimeUserActivity.class));
            }
        });


        profileName = (TextView)header.findViewById(R.id.nav_header_profile_name);
        companyName = (TextView)header.findViewById(R.id.nav_header_profile_name);

        mNameReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue(String.class)!=null) {
                    String profileTextRetrieved = dataSnapshot.getValue(String.class);
                    if (profileTextRetrieved.equals("")) {
                        Intent intent = new Intent(MainScreenActivity.this, FirstTimeUserActivity.class);
                        startActivity(intent);
                    }
                }
                else{
                    Intent intent = new Intent(MainScreenActivity.this, FirstTimeUserActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
            session.logoutUser();
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

    @Override
    protected void onStart(){
        super.onStart();
        mNameReference.addValueEventListener(this);
        mCompanyReference.addValueEventListener(this);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header =navigationView.getHeaderView(0);
        profileName = (TextView)header.findViewById(R.id.nav_header_profile_name);
        companyName = (TextView)header.findViewById(R.id.nav_header_company_name);

        if(dataSnapshot.getValue(String.class)!=null){
            String key = dataSnapshot.getKey();
            if(key.equals("name")){
                String profileTextRetrieved = dataSnapshot.getValue(String.class);
                profileName.setText(profileTextRetrieved);
            }
            else if(key.equals("company")){
                String companyTextRetrieved = dataSnapshot.getValue(String.class);
                companyName.setText(companyTextRetrieved);
            }
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    public static String EncodeEmail(String string){
        return string.replace(".",",");
    }
}