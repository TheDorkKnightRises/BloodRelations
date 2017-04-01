package smartindia.santas.bloodrelations.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import java.util.ArrayList;

import smartindia.santas.bloodrelations.Objects.Donor;
import smartindia.santas.bloodrelations.R;
import smartindia.santas.bloodrelations.adapters.DonorRecyclerAdapter;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ActionBarDrawerToggle actionBarDrawerToggle;

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    DonorRecyclerAdapter adapter;
    ArrayList<Donor> donorList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout= (DrawerLayout)findViewById(R.id.drawer_layout) ;
        navigationView = (NavigationView) findViewById(R.id.nvView);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar, R.string.drawer_open,R.string.drawer_close);

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
    }

    private void setupDrawer(){
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.menu_item_home:
                        startActivity(new Intent(MainActivity.this,BloodBankListActivity.class));
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

                }
                return true;
            }
        });
    }
}
