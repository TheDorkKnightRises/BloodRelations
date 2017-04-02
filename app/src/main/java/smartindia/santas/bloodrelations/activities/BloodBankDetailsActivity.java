package smartindia.santas.bloodrelations.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import smartindia.santas.bloodrelations.Constants;
import smartindia.santas.bloodrelations.R;

public class BloodBankDetailsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    String item;
    List <String> categories;
    TextView opos, oneg, apos, aneg, bpos, bneg, abpos, abneg, bloodGroupTextView;
    EditText bloodQuantity;
    Button submitBloodQuantity;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    DatabaseReference quantityDatabaseReference;
    ValueEventListener valueEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSharedPreferences(Constants.PREFS, MODE_PRIVATE).getBoolean(Constants.DARK_THEME, false))
            setTheme(R.style.AppTheme_Dark);
        setContentView(R.layout.activity_blood_bank_details);

        opos=(TextView) findViewById(R.id.opos);
        oneg=(TextView) findViewById(R.id.oneg);
        apos=(TextView) findViewById(R.id.apos);
        aneg=(TextView) findViewById(R.id.aneg);
        bpos=(TextView) findViewById(R.id.bpos);
        bneg=(TextView) findViewById(R.id.bneg);
        abpos=(TextView) findViewById(R.id.abpos);
        abneg=(TextView) findViewById(R.id.abneg);

        bloodGroupTextView = (TextView) findViewById(R.id.bloodGroupTextView);

        submitBloodQuantity = (Button) findViewById(R.id.submitBloodQuantity);

        bloodQuantity = (EditText) findViewById(R.id.bloodQuantity);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);

        // Spinner click listener
        spinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);

        // Spinner Drop down elements
        categories = new ArrayList <String>();
        categories.add("O+");
        categories.add("O-");
        categories.add("A+");
        categories.add("A-");
        categories.add("B+");
        categories.add("B-");
        categories.add("AB+");
        categories.add("AB-");

        // Creating adapter for spinner
        final ArrayAdapter<String> dataAdapter = new ArrayAdapter <String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("users");

        final Intent i = getIntent();
        if(!i.getStringExtra("coordinates").equals("")){
            bloodGroupTextView.setVisibility(View.GONE);
            spinner.setVisibility(View.GONE);
            bloodQuantity.setVisibility(View.GONE);
            submitBloodQuantity.setVisibility(View.GONE);

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        if(snapshot.child("isBloodBank").getValue().toString().equals("true")){
                            String coord = (String)snapshot.child("details").child("lat").child("latitude").getValue().toString() + " " + (String)snapshot.child("details").child("lat").child("longitude").getValue().toString();
                            if(coord.equals(i.getStringExtra("coordinates"))){
                                if(snapshot.child("details").hasChild("O+")){
                                    opos.setText(snapshot.child("details").child("O+").getValue().toString());
                                }
                                else{
                                    opos.setText("0");
                                }
                                if(snapshot.child("details").hasChild("O-")){
                                    oneg.setText(snapshot.child("details").child("O-").getValue().toString());
                                }
                                else{
                                    opos.setText("0");
                                }
                                if(snapshot.child("details").hasChild("A+")){
                                    apos.setText(snapshot.child("details").child("A+").getValue().toString());
                                }
                                else{
                                    opos.setText("0");
                                }
                                if(snapshot.child("details").hasChild("A-")){
                                    aneg.setText(snapshot.child("details").child("A-").getValue().toString());
                                }
                                if(snapshot.child("details").hasChild("B+")){
                                    bpos.setText(snapshot.child("details").child("B+").getValue().toString());
                                }
                                else{
                                    opos.setText("0");
                                }
                                if(snapshot.child("details").hasChild("B-")){
                                    bneg.setText(snapshot.child("details").child("B-").getValue().toString());
                                }
                                else{
                                    opos.setText("0");
                                }
                                if(snapshot.child("details").hasChild("AB+")){
                                    abpos.setText(snapshot.child("details").child("AB+").getValue().toString());
                                }
                                else{
                                    opos.setText("0");
                                }
                                if(snapshot.child("details").hasChild("AB-")){
                                    abneg.setText(snapshot.child("details").child("AB-").getValue().toString());
                                }
                                else{
                                    opos.setText("0");
                                }
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        else {
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        if(snapshot.child("isBloodBank").getValue().toString().equals("true")){
                            String coord = (String)snapshot.child("details").child("lat").child("latitude").getValue().toString() + " " + (String)snapshot.child("details").child("lat").child("longitude").getValue().toString();
                            if(coord.equals(i.getStringExtra("coordinates"))){
                                if(snapshot.child("details").hasChild("O+")){
                                    opos.setText(snapshot.child("details").child("O+").getValue().toString());
                                }
                                else{
                                    opos.setText("0");
                                }
                                if(snapshot.child("details").hasChild("O-")){
                                    oneg.setText(snapshot.child("details").child("O-").getValue().toString());
                                }
                                else{
                                    opos.setText("0");
                                }
                                if(snapshot.child("details").hasChild("A+")){
                                    apos.setText(snapshot.child("details").child("A+").getValue().toString());
                                }
                                else{
                                    opos.setText("0");
                                }
                                if(snapshot.child("details").hasChild("A-")){
                                    aneg.setText(snapshot.child("details").child("A-").getValue().toString());
                                }
                                if(snapshot.child("details").hasChild("B+")){
                                    bpos.setText(snapshot.child("details").child("B+").getValue().toString());
                                }
                                else{
                                    opos.setText("0");
                                }
                                if(snapshot.child("details").hasChild("B-")){
                                    bneg.setText(snapshot.child("details").child("B-").getValue().toString());
                                }
                                else{
                                    opos.setText("0");
                                }
                                if(snapshot.child("details").hasChild("AB+")){
                                    abpos.setText(snapshot.child("details").child("AB+").getValue().toString());
                                }
                                else{
                                    opos.setText("0");
                                }
                                if(snapshot.child("details").hasChild("AB-")){
                                    abneg.setText(snapshot.child("details").child("AB-").getValue().toString());
                                }
                                else{
                                    opos.setText("0");
                                }
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }

    public void update_blood_details(View view)
    {
        String units = bloodQuantity.getText().toString();
        String choice=item;
        for(int i=0; i<8; i++)
        {
            if(categories.get(i).equals(choice.toString()))
            {
                quantityDatabaseReference = databaseReference.child(user.getUid()).child("details").child(choice.toString());
                quantityDatabaseReference.setValue(units);

                switch (choice.toString())
                {
                    case "O+":
                        opos.setText(units);
                        break;
                    case "O-":
                        oneg.setText(units);
                        break;
                    case "A+":
                        apos.setText(units);
                        break;
                    case "A-":
                        aneg.setText(units);
                        break;
                    case "B+":
                        bpos.setText(units);
                        break;
                    case "B-":
                        bneg.setText(units);
                        break;
                    case "AB+":
                        abpos.setText(units);
                        break;
                    case "AB-":
                        abneg.setText(units);
                        break;

                }
            }
        }
    }


}
