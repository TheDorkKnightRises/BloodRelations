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



        bloodBankList=new ArrayList<>();

        bloodBankList.add(new BloodBank("Placeholder Name Here","Placeholder Location Here","Placeholder Phone Here"));
        bloodBankList.add(new BloodBank("Placeholder Name Here","Placeholder Location Here","Placeholder Phone Here"));
        bloodBankList.add(new BloodBank("Placeholder Name Here","Placeholder Location Here","Placeholder Phone Here"));
        bloodBankList.add(new BloodBank("Placeholder Name Here","Placeholder Location Here","Placeholder Phone Here"));
        bloodBankList.add(new BloodBank("Placeholder Name Here","Placeholder Location Here","Placeholder Phone Here"));
        bloodBankList.add(new BloodBank("Placeholder Name Here","Placeholder Location Here","Placeholder Phone Here"));
        bloodBankList.add(new BloodBank("Placeholder Name Here","Placeholder Location Here","Placeholder Phone Here"));

        recyclerView=(RecyclerView)findViewById(R.id.blood_bank_recyclerview);
        adapter=new BloodBankRecyclerAdapter(bloodBankList);
        linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
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
                        startActivity(new Intent(BloodBankListActivity.this,BloodBankDetailsActivity.class));
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
