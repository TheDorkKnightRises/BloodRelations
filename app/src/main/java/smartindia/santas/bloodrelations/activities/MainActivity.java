package smartindia.santas.bloodrelations.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.internal.NavigationMenu;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import smartindia.santas.bloodrelations.R;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ActionBarDrawerToggle actionBarDrawerToggle;

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


    }

    private void setupDrawer(){
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.menu_item_home:
                        startActivity(new Intent(MainActivity.this,MainActivity.class));
                        break;
                    case R.id.menu_item_profile:
                        //startActivity(new Intent(MainActivity.this,Form.class));
                        break;
                    case R.id.menu_item_locate:
                        //startActivity(new Intent(MainActivity.this,Form.class));
                        break;
                    case R.id.menu_item_form:
                        startActivity(new Intent(MainActivity.this,Form.class));
                        break;
                    case R.id.menu_item_language:
                        //startActivity(new Intent(MainActivity.this,Form.class));
                        break;
                    case R.id.menu_item_feedback:
                        //startActivity(new Intent(MainActivity.this,Form.class));
                        break;
                }
                return true;
            }
        });
    }
}
