package smartindia.santas.bloodrelations.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import smartindia.santas.bloodrelations.adapters.BloodBankRecyclerAdapter;
import smartindia.santas.bloodrelations.objects.BloodBank;
import smartindia.santas.bloodrelations.R;

public class BloodBankListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BloodBankRecyclerAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<BloodBank> bloodBankList;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ActionBarDrawerToggle actionBarDrawerToggle;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private ChildEventListener mChildEventListener;
    private ValueEventListener mValueEventListener;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        bloodBankList=new ArrayList<>();

//        bloodBankList.add(new BloodBank("Placeholder Name Here","Placeholder Location Here","Placeholder Phone Here"));
//        bloodBankList.add(new BloodBank("Placeholder Name Here","Placeholder Location Here","Placeholder Phone Here"));
//        bloodBankList.add(new BloodBank("Placeholder Name Here","Placeholder Location Here","Placeholder Phone Here"));
//        bloodBankList.add(new BloodBank("Placeholder Name Here","Placeholder Location Here","Placeholder Phone Here"));
//        bloodBankList.add(new BloodBank("Placeholder Name Here","Placeholder Location Here","Placeholder Phone Here"));
//        bloodBankList.add(new BloodBank("Placeholder Name Here","Placeholder Location Here","Placeholder Phone Here"));
//        bloodBankList.add(new BloodBank("Placeholder Name Here","Placeholder Location Here","Placeholder Phone Here"));


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
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        Boolean isBank = (Boolean)snapshot.child("isBloodBank").getValue();
                        if(isBank){
                            String bbname = (String) snapshot.child("details").child("bloodbankname").getValue().toString();
                            String location = (String) snapshot.child("details").child("address").getValue().toString();
                            String phone = (String) snapshot.child("details").child("phone").getValue().toString();
                            bloodBankList.add(new BloodBank(bbname,location,phone));
                        }
                    }
                    updateUI();
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
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }
    
}
