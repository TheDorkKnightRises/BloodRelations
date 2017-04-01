package smartindia.santas.bloodrelations.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.HashMap;

import smartindia.santas.bloodrelations.Constants;
import smartindia.santas.bloodrelations.R;
import smartindia.santas.bloodrelations.adapters.DonorRecyclerAdapter;
import smartindia.santas.bloodrelations.objects.Donor;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ActionBarDrawerToggle actionBarDrawerToggle;
    FloatingActionButton fab;

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    DonorRecyclerAdapter adapter;
    ArrayList<Donor> donorList;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private ChildEventListener mChildEventListener;
    private ValueEventListener mValueEventListener;
    FirebaseUser user;
    SwipeRefreshLayout swipeRefreshLayout;

    final String requests = "notificationRequests";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSharedPreferences(Constants.PREFS, MODE_PRIVATE).getBoolean(Constants.DARK_THEME, false))
            setTheme(R.style.AppTheme_Dark_Translucent);

        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh_layout);


        user = FirebaseAuth.getInstance().getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("users");

        drawerLayout= (DrawerLayout)findViewById(R.id.drawer_layout) ;
        navigationView = (NavigationView) findViewById(R.id.nvView);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar, R.string.drawer_open,R.string.drawer_close);

        fab = (FloatingActionButton)findViewById(R.id.fab);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        setupDrawer();

        recyclerView = (RecyclerView)findViewById(R.id.donor_recyclerview);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        donorList = new ArrayList<>();
//        for(int i=0;i<10;i++){
//            donorList.add(new Donor("Placeholder Name Here","Placeholder Location Here","Placeholder Blood Group Here","Placeholder Phone Here"));
//        }


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fab_pressed();
            }
        });

        fetchDonorList mFetch = new fetchDonorList();
        mFetch.execute();

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
        detachDatabaseReadListener();
    }

    public void updateUI(){
        adapter=new DonorRecyclerAdapter(donorList);
        recyclerView.setAdapter(adapter);
    }

    private void attachDatabaseReadListener() {
        if (mChildEventListener == null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Donor donor = dataSnapshot.getValue(Donor.class);
                    donorList.add(donor);

                }

                public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    Donor donor = dataSnapshot.getValue(Donor.class);
                    donorList.remove(donor);
                }
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                public void onCancelled(DatabaseError databaseError) {}
            };
        }
    }

    private void detachDatabaseReadListener() {
        if (mChildEventListener != null) {
            databaseReference.removeEventListener(mChildEventListener);
            mChildEventListener = null;
        }
    }

    public class fetchDonorList extends AsyncTask<Void,Void,ArrayList<Donor>> {
        @Override
        protected ArrayList<Donor> doInBackground(Void... params) {

            mValueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    donorList.clear();
                    try {
                        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                            String isBank = (String) snapshot.child("isBloodBank").getValue();
                            if(isBank.equals("false")){
                                String firstname = snapshot.child("details").child("firstname").getValue().toString();
                                String surname = snapshot.child("details").child("surname").getValue().toString();
                                String name = firstname + " " + surname;
                                String location = snapshot.child("details").child("address").getValue().toString();
                                String bloodGroup = snapshot.child("details").child("bloodgroup").getValue().toString();
                                String phone = snapshot.child("details").child("phone").getValue().toString();
                                donorList.add(new Donor(name,location,bloodGroup,phone));
                            }
                        }
                        updateUI();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };

            databaseReference.addValueEventListener(mValueEventListener);
            return donorList;
        }
    }

    private void setupDrawer(){
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.menu_item_profile:
                        Intent i = new Intent(MainActivity.this,ProfileActivity.class);
                        i.putExtra("isfromsignup",false);
                        startActivity(i);
                        break;
                    case R.id.menu_item_locate:
                        startActivity(new Intent(MainActivity.this, LocateActivity.class));
                        break;
                    case R.id.menu_item_form:
                        startActivity(new Intent(MainActivity.this, FormActivity.class));
                        break;
                    case R.id.menu_item_feedback:
                        startActivity(new Intent(MainActivity.this, FeedbackActivity.class));
                        break;
                    case R.id.menu_item_faqs:
                        startActivity(new Intent(MainActivity.this, HelpActivity.class));
                        break;
                    case R.id.menu_item_bloodbank_details:
                        startActivity(new Intent(MainActivity.this, BloodBankDetailsActivity.class));
                        break;
                    case R.id.menu_item_certificate:
                        startActivity(new Intent(MainActivity.this,CertificateActivity.class));
                        break;
                    case R.id.menu_item_settings:
                        startActivity(new Intent(MainActivity.this,SettingsActivity.class));
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    private void fab_pressed(){
        sendNotification("Put in a request for blood","notifs");
    }

    private void sendNotification(final String string, String topic){
        final DatabaseReference root;
        root = FirebaseDatabase.getInstance().getReference();

        FirebaseMessaging.getInstance().subscribeToTopic(topic);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        if(snapshot.getKey().equals(user.getUid())){
                            Log.v("tag",snapshot.getKey());
                            String user_name = snapshot.child("details").child("bloodbankname").getValue().toString();
                            HashMap<String,Object> notification = new HashMap<>();
                            notification.put("username", user_name);
                            notification.put("message", string);

                            String pushKey = root.child(requests).push().getKey();

                            HashMap<String,Object> updateHashmap = new HashMap<>();
                            updateHashmap.put("/"+requests+"/"+pushKey,notification);

                            root.updateChildren(updateHashmap, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                    if (databaseError != null) {
                                        Toast.makeText(getApplicationContext(), "Error: " + databaseError, Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //DatabaseReference notifications = root.child(requests);


    }
}
