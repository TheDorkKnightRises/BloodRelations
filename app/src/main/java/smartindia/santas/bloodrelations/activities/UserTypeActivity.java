package smartindia.santas.bloodrelations.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import smartindia.santas.bloodrelations.Constants;
import smartindia.santas.bloodrelations.R;

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



    }

    public void onClick(Button b) {
        switch (b.getId()) {

            case R.id.Yes:
                isBloodBank = true;
                push();
                setsharedpreference();
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                break;
            case R.id.No:
                isBloodBank = false;
                push();
                setsharedpreference();
                Intent j = new Intent(getApplicationContext(), BloodBankListActivity.class);
                startActivity(j);

        }


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