package smartindia.santas.bloodrelations.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
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

import java.util.ArrayList;
import java.util.HashMap;

import smartindia.santas.bloodrelations.Constants;
import smartindia.santas.bloodrelations.R;
import smartindia.santas.bloodrelations.adapters.BloodBankRecyclerAdapter;
import smartindia.santas.bloodrelations.objects.BloodBank;

public class BloodBankListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BloodBankRecyclerAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<BloodBank> bloodBankList;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    FloatingActionButton fab;
    Toolbar toolbar;
    ActionBarDrawerToggle actionBarDrawerToggle;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private ChildEventListener mChildEventListener;
    private ValueEventListener mValueEventListener;
    private int PLACE_PICKER_REQUEST = 1;
    final String requests = "notificationRequests";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSharedPreferences(Constants.PREFS, MODE_PRIVATE).getBoolean(Constants.DARK_THEME, false))
            setTheme(R.style.AppTheme_Dark_Translucent);
        setContentView(R.layout.activity_blood_bank_list);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout= (DrawerLayout)findViewById(R.id.drawer_layout) ;
        navigationView = (NavigationView) findViewById(R.id.nvView);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar, R.string.drawer_open,R.string.drawer_close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        setupDrawer();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("users");

        recyclerView=(RecyclerView)findViewById(R.id.blood_bank_recyclerview);
        linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(BloodBankListActivity.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    Toast.makeText(BloodBankListActivity.this, "Google Play Services error", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    Toast.makeText(BloodBankListActivity.this, "Google Play Services is required for this feature", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });

        bloodBankList=new ArrayList<>();

        fetchBloodBankList mFetch = new fetchBloodBankList();
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
        adapter=new BloodBankRecyclerAdapter(bloodBankList);
        recyclerView.setAdapter(adapter);
    }

    private void attachDatabaseReadListener() {
        if (mChildEventListener == null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    BloodBank bloodBank = dataSnapshot.getValue(BloodBank.class);
                    bloodBankList.add(bloodBank);

                }

                public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    BloodBank bloodBank = dataSnapshot.getValue(BloodBank.class);
                    bloodBankList.remove(bloodBank);
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

    public class fetchBloodBankList extends AsyncTask<Void,Void,ArrayList<BloodBank>> {
        @Override
        protected ArrayList<BloodBank> doInBackground(Void... params) {

            mValueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    bloodBankList.clear();
                    try {
                        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                            String isBank = (String) snapshot.child("isBloodBank").getValue();
                            if(isBank.equals("true")){
                                String bbname = snapshot.child("details").child("bloodbankname").getValue().toString();
                                String location = snapshot.child("details").child("address").getValue().toString();
                                String phone = snapshot.child("details").child("phone").getValue().toString();
                                bloodBankList.add(new BloodBank(bbname,location,phone));
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
            return bloodBankList;
        }
    }

    private void setupDrawer(){
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.menu_item_profile:
                        Intent i = new Intent(BloodBankListActivity.this,ProfileActivity.class);
                        i.putExtra("isfromsignup",false);
                        startActivity(i);
                        break;
                    case R.id.menu_item_locate:
                        startActivity(new Intent(BloodBankListActivity.this,LocateActivity.class));
                        break;
                    case R.id.menu_item_form:
                        startActivity(new Intent(BloodBankListActivity.this,FormActivity.class));
                        break;
                    case R.id.menu_item_feedback:
                        startActivity(new Intent(BloodBankListActivity.this,FeedbackActivity.class));
                        break;
                    case R.id.menu_item_faqs:
                        startActivity(new Intent(BloodBankListActivity.this,HelpActivity.class));
                        break;
                    case R.id.menu_item_bloodbank_details:
                        startActivity(new Intent(BloodBankListActivity.this,BloodBankDetailsActivity.class));
                        break;
                    case R.id.menu_item_certificate:
                        startActivity(new Intent(BloodBankListActivity.this,CertificateActivity.class));
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
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

}
