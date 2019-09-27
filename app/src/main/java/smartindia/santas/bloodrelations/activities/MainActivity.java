package smartindia.santas.bloodrelations.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import smartindia.santas.bloodrelations.Constants;
import smartindia.santas.bloodrelations.R;
import smartindia.santas.bloodrelations.adapters.BloodBankRecyclerAdapter;
import smartindia.santas.bloodrelations.adapters.DonorRecyclerAdapter;
import smartindia.santas.bloodrelations.objects.BloodBank;
import smartindia.santas.bloodrelations.objects.Donor;
import smartindia.santas.bloodrelations.utils.ConnectionChecker;

public class MainActivity extends AppCompatActivity {

    private static final int PLACE_PICKER_REQUEST = 1;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Button fab;
    CircleImageView profile_image;
    TextView header_name, header_subtext;

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    RecyclerView.Adapter adapter;
    ArrayList list;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private ChildEventListener mChildEventListener;
    private ValueEventListener mValueEventListener;
    FirebaseUser user;
    SwipeRefreshLayout swipeRefreshLayout;
    SharedPreferences pref;
    boolean bbMode;

    final String requests = "notificationRequests";
    private boolean backPressFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pref = getSharedPreferences(Constants.PREFS, MODE_PRIVATE);
        if (pref.getBoolean(Constants.DARK_THEME, false))
            setTheme(R.style.AppTheme_Dark_Translucent);

        if(!pref.getBoolean(Constants.ISPROFILEFILLED,false)){
            startActivity(new Intent(this, ProfileActivity.class));
        }

        setContentView(R.layout.activity_main);
        bbMode = pref.getBoolean(Constants.ISBLOODBANK, false);

        if(!pref.getBoolean(Constants.ISFORMFILLED,false)&&!bbMode){
            startActivity(new Intent(this, FormActivity.class));
            pref.edit().putBoolean(Constants.ISFORMFILLED, true).apply();
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        user = FirebaseAuth.getInstance().getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("users");

        drawerLayout= (DrawerLayout)findViewById(R.id.drawer_layout) ;
        navigationView = (NavigationView) findViewById(R.id.nvView);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar, R.string.drawer_open,R.string.drawer_close);

        fab = (Button) findViewById(R.id.fab);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        setupDrawer();

        recyclerView = (RecyclerView)findViewById(R.id.recyclerview);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        profile_image = (CircleImageView) navigationView.getHeaderView(0).findViewById(R.id.profile_image);
        header_name = (TextView) navigationView.getHeaderView(0).findViewById(R.id.header_name_view);
        header_subtext = (TextView) navigationView.getHeaderView(0).findViewById(R.id.header_subtext_view);

        if (ConnectionChecker.isConnected(this)) {
            if (bbMode) {
                list = new ArrayList<Donor>();
                FetchDonorList mFetch = new FetchDonorList();
                mFetch.execute();
            } else {
                list = new ArrayList<BloodBank>();
                FetchBloodBankList mFetch = new FetchBloodBankList();
                mFetch.execute();
            }
        } else Snackbar.make(findViewById(R.id.coordinatorLayout), R.string.no_network, Snackbar.LENGTH_LONG).show();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fab_pressed();
            }
        });

        updateUI();

    }

    public void updateUI(){
        String imageUrl = pref.getString(Constants.PROFILE_IMAGE, "image.png");
        Picasso.with(MainActivity.this)
                .load(imageUrl)
                .error(R.drawable.ic_account_circle_black_24dp)
                .into(profile_image);
        header_name.setText(user.getDisplayName());
        header_subtext.setText(user.getEmail());
        if (ConnectionChecker.isConnected(this)) {
            if (bbMode)
                adapter = new DonorRecyclerAdapter(list);
            else adapter = new BloodBankRecyclerAdapter(list);
        } else Snackbar.make(findViewById(R.id.coordinatorLayout), R.string.no_network, Snackbar.LENGTH_LONG).show();
        recyclerView.setAdapter(adapter);
    }

    public class FetchDonorList extends AsyncTask<Void,Void,ArrayList<Donor>> {
        @Override
        protected ArrayList<Donor> doInBackground(Void... params) {

            mValueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    list.clear();
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
                                double lat = Double.parseDouble(snapshot.child("details").child("lat").child("latitude").getValue().toString());
                                double lng = Double.parseDouble(snapshot.child("details").child("lat").child("longitude").getValue().toString());
                                list.add(new Donor(name,location,bloodGroup,phone, lat, lng));
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
            return list;
        }
    }

    public class FetchBloodBankList extends AsyncTask<Void,Void,ArrayList<BloodBank>> {
        @Override
        protected ArrayList<BloodBank> doInBackground(Void... params) {

            mValueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    list.clear();
                    try {
                        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                            String isBank = (String) snapshot.child("isBloodBank").getValue();
                            if(isBank.equals("true")){
                                String bbname = snapshot.child("details").child("bloodbankname").getValue().toString();
                                String location = snapshot.child("details").child("address").getValue().toString();
                                String phone = snapshot.child("details").child("phone").getValue().toString();
                                double lat = Double.parseDouble(snapshot.child("details").child("lat").child("latitude").getValue().toString());
                                double lng = Double.parseDouble(snapshot.child("details").child("lat").child("longitude").getValue().toString());
                                list.add(new BloodBank(bbname,location,phone, lat, lng));
                                if (FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl() != null)
                                    Picasso.with(MainActivity.this).load(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl()).into(profile_image);

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
            return list;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
                sendNotification("request", "put in a request for blood", String.valueOf(place.getLatLng().latitude), String.valueOf(place.getLatLng().longitude), 50);
            }
        }
    }

    private void sendNotification(String topic, final String string, final String latitude, final String longitude, final int qty){
        final DatabaseReference root;
        root = FirebaseDatabase.getInstance().getReference();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseMessaging.getInstance().subscribeToTopic(topic);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    if(snapshot.getKey().equals(user.getUid())){
                        Log.v("tag",snapshot.getKey());
                        String first_name = snapshot.child("details").child("firstname").getValue().toString();
                        String blood_type = snapshot.child("details").child("bloodgroup").getValue().toString();
                        HashMap<String,Object> notification = new HashMap<>();
                        notification.put("username", first_name);
                        notification.put("message", string);
                        notification.put("latitude", latitude);
                        notification.put("longitude", longitude);
                        notification.put("quantity", qty);
                        notification.put("bloodgroup", blood_type);

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
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //DatabaseReference notifications = root.child(requests);

    }

    private void setupDrawer(){
        if (bbMode) {
            navigationView.getMenu().findItem(R.id.menu_item_certificate).setVisible(false);
            navigationView.getMenu().findItem(R.id.menu_item_form).setVisible(false);
        } else navigationView.getMenu().findItem(R.id.menu_item_bloodbank_details).setVisible(false);
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
                        Intent j = new Intent(MainActivity.this, BloodBankDetailsActivity.class);
                        j.putExtra("coordinates","");
                        startActivity(j);
                        break;
                    case R.id.menu_item_certificate:
                        startActivity(new Intent(MainActivity.this, CertificateActivity.class));
                        break;
                    case R.id.menu_item_settings:
                        startActivity(new Intent(MainActivity.this,SettingsActivity.class));
                        break;
                    case R.id.menu_item_emergency:
                        startActivity(new Intent(MainActivity.this,EmergencyActivity.class));
                        break;
                    case R.id.menu_item_about:
                        startActivity(new Intent(MainActivity.this,AboutActivity.class));
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    private void fab_pressed(){
        if (bbMode)
            sendNotification("Put in a request for blood","notifs");
        else {
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            try {
                startActivityForResult(builder.build(MainActivity.this), PLACE_PICKER_REQUEST);
            } catch (GooglePlayServicesRepairableException e) {
                Toast.makeText(MainActivity.this, "Google Play Services error", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            } catch (GooglePlayServicesNotAvailableException e) {
                Toast.makeText(MainActivity.this, "Google Play Services is required for this feature", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
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

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawers();
        } else {
            if (backPressFlag)
                finishAffinity();
            else {
                Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
                backPressFlag = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        backPressFlag = false;
                    }
                }, 2000);
            }
        }
    }
}
