package smartindia.santas.bloodrelations.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import smartindia.santas.bloodrelations.Constants;
import smartindia.santas.bloodrelations.R;
import smartindia.santas.bloodrelations.objects.User;


public class UserTypeActivity extends AppCompatActivity {
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    boolean isBloodBank = false;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usertype);

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        final Button b = (Button) findViewById(R.id.Yes);
        b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                isBloodBank = true;
                push();
                setsharedpreference();
                Intent i = new Intent(UserTypeActivity.this, ProfileActivity.class);
                startActivity(i);
                finish();

            }

        });

        final Button b1 = (Button) findViewById(R.id.No);
        b1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                isBloodBank = false;
                push();
                setsharedpreference();
                Intent j = new Intent(UserTypeActivity.this, ProfileActivity.class);
                j.putExtra("isfromsignup",true);
                startActivity(j);
                finish();

            }
        });
    }

    public void setsharedpreference() {
        prefs = getSharedPreferences(Constants.PREFS, MODE_PRIVATE);
        editor = prefs.edit();
        editor.putBoolean(Constants.ISBLOODBANK, isBloodBank);
        editor.apply();

    }

    public void push(){
        databaseReference = firebaseDatabase.getReference().child("users").child(user.getUid()).child("isBloodBank");
        databaseReference.setValue(isBloodBank);

    }
}


