package smartindia.santas.bloodrelations.activities;

import android.content.Intent;
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
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.HashMap;

import smartindia.santas.bloodrelations.Objects.Donor;
import smartindia.santas.bloodrelations.R;
import smartindia.santas.bloodrelations.adapters.DonorRecyclerAdapter;

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
    FirebaseUser user;

    final String requests = "notificationRequests";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        user = FirebaseAuth.getInstance().getCurrentUser();

        drawerLayout= (DrawerLayout)findViewById(R.id.drawer_layout) ;
        navigationView = (NavigationView) findViewById(R.id.nvView);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar, R.string.drawer_open,R.string.drawer_close);

        fab = (FloatingActionButton)findViewById(R.id.fab);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        setupDrawer();

        donorList = new ArrayList<>();
        for(int i=0;i<10;i++){
            donorList.add(new Donor("Placeholder Name Here","Placeholder Location Here","Placeholder Blood Group Here","Placeholder Phone Here"));
        }

        recyclerView = (RecyclerView)findViewById(R.id.donor_recyclerview);
        linearLayoutManager = new LinearLayoutManager(this);
        adapter = new DonorRecyclerAdapter(donorList);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fab_pressed();
            }
        });
    }

    private void setupDrawer(){
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.menu_item_home:
                        break;
                    case R.id.menu_item_profile:
                        startActivity(new Intent(MainActivity.this,ProfileActivity.class));
                        break;
                    case R.id.menu_item_locate:
                        startActivity(new Intent(MainActivity.this,BloodBankDetailsActivity.class));
                        break;
                    case R.id.menu_item_form:
                        startActivity(new Intent(MainActivity.this,FormActivity.class));
                        break;
                    case R.id.menu_item_feedback:
                        startActivity(new Intent(MainActivity.this,FeedbackActivity.class));
                        break;
                    case R.id.menu_item_faqs:
                        startActivity(new Intent(MainActivity.this,HelpActivity.class));
                        break;
                    case R.id.menu_item_bloodbank_details:
                        startActivity(new Intent(MainActivity.this,BloodBankDetailsActivity.class));
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    private void fab_pressed(){
        sendNotification("Hello","notifs");
    }

    private void sendNotification(String string, String topic){
        DatabaseReference root;
        root = FirebaseDatabase.getInstance().getReference();

        FirebaseMessaging.getInstance().subscribeToTopic(topic);


        String user_name = user.getDisplayName();
        //DatabaseReference notifications = root.child(requests);


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
