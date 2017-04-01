package smartindia.santas.bloodrelations.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import smartindia.santas.bloodrelations.Constants;
import smartindia.santas.bloodrelations.R;

public class ProfileActivity extends AppCompatActivity {

    private static final int RC_PHOTO_PICKER = 1;

    private de.hdodenhof.circleimageview.CircleImageView profilePicture;
    private FloatingActionButton profileEditDone;
    private FloatingActionButton locateFab;

    private TextInputLayout profileName;
    private TextInputLayout profileSurname;
    private TextInputLayout profilePhone;
    private TextInputLayout profileBloodGroup;
    private TextInputLayout profileAddress;
    private TextInputLayout profileBbName;
    private EditText birthDateEditText;

    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private FirebaseUser user;

    private FirebaseDatabase fetchFirebaseDatabase;
    private DatabaseReference fetchDatabaseReference;
    private ChildEventListener mChildEventListener;
    private ValueEventListener mValueEventListener;

    private Uri imageUrl;
    boolean t;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        t = i.getBooleanExtra("isfromsignup",false);
        prefs = getSharedPreferences(Constants.PREFS, MODE_PRIVATE);
        if(!prefs.getBoolean(Constants.ISBLOODBANK,false)){
            if (getSharedPreferences(Constants.PREFS, MODE_PRIVATE).getBoolean(Constants.DARK_THEME, false))
                setTheme(R.style.AppTheme_Dark);
            setContentView(R.layout.activity_profile);

            profilePicture=(de.hdodenhof.circleimageview.CircleImageView)findViewById(R.id.profile_image);
            birthDateEditText=(EditText) findViewById(R.id.birthday_edittext);
            profileEditDone=(FloatingActionButton)findViewById(R.id.profile_edit_done_fab);
            profileName = (TextInputLayout)findViewById(R.id.profile_name);
            profileSurname = (TextInputLayout)findViewById(R.id.profile_surname);
            profilePhone = (TextInputLayout)findViewById(R.id.profile_phone);
            profileBloodGroup = (TextInputLayout)findViewById(R.id.profile_blood_group);
            profileAddress = (TextInputLayout)findViewById(R.id.profile_address);

            final Calendar myCalendar = Calendar.getInstance();

            final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                    // TODO Auto-generated method stub
                    myCalendar.set(Calendar.YEAR, year);
                    myCalendar.set(Calendar.MONTH, monthOfYear);
                    myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    updateLabel(myCalendar);
                }

            };

            birthDateEditText.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    new DatePickerDialog(ProfileActivity.this, date, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
            });
            birthDateEditText.setInputType(InputType.TYPE_NULL);
            birthDateEditText.requestFocus();

            firebaseDatabase = FirebaseDatabase.getInstance();
            firebaseStorage = FirebaseStorage.getInstance();
            firebaseAuth = FirebaseAuth.getInstance();
            user = firebaseAuth.getCurrentUser();
            storageReference = firebaseStorage.getReference().child("userPhotos");

            if(!t){
                fetchFirebaseDatabase = FirebaseDatabase.getInstance();
                fetchDatabaseReference = firebaseDatabase.getReference().child("users");

                fetchDatabaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                            if(snapshot.getKey().equals(user.getUid())){
                                profileName.getEditText().setText(snapshot.child("details").child("firstname").getValue().toString());
                                profileSurname.getEditText().setText(snapshot.child("details").child("surname").getValue().toString());
                                profileBloodGroup.getEditText().setText(snapshot.child("details").child("bloodgroup").getValue().toString());
                                profileAddress.getEditText().setText(snapshot.child("details").child("address").getValue().toString());
                                profilePhone.getEditText().setText(snapshot.child("details").child("phone").getValue().toString());
                                birthDateEditText.setText(snapshot.child("details").child("birthdate").getValue().toString());
                                if(snapshot.child("details").hasChild("imageURL")){
                                    Picasso.with(profilePicture.getContext()).load(snapshot.child("details").child("imageURL").getValue().toString()).into(profilePicture);
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            profileEditDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!profileName.getEditText().getText().toString().equals(null)){
                        databaseReference = firebaseDatabase.getReference().child("users").child(user.getUid()).child("details").child("firstname");
                        databaseReference.setValue(profileName.getEditText().getText().toString());
                    }
                    if(!profileSurname.getEditText().getText().toString().equals(null)){
                        databaseReference = firebaseDatabase.getReference().child("users").child(user.getUid()).child("details").child("surname");
                        databaseReference.setValue(profileSurname.getEditText().getText().toString());
                    }
                    if(!profilePhone.getEditText().getText().toString().equals(null)){
                        databaseReference = firebaseDatabase.getReference().child("users").child(user.getUid()).child("details").child("phone");
                        databaseReference.setValue(profilePhone.getEditText().getText().toString());
                    }
                    if(!profileBloodGroup.getEditText().getText().toString().equals(null)){
                        databaseReference = firebaseDatabase.getReference().child("users").child(user.getUid()).child("details").child("bloodgroup");
                        databaseReference.setValue(profileBloodGroup.getEditText().getText().toString());
                    }
                    if(!profileAddress.getEditText().getText().toString().equals(null)){
                        databaseReference = firebaseDatabase.getReference().child("users").child(user.getUid()).child("details").child("address");
                        databaseReference.setValue(profileAddress.getEditText().getText().toString());
                    }
                    if(!birthDateEditText.getText().toString().equals(null)){
                        databaseReference = firebaseDatabase.getReference().child("users").child(user.getUid()).child("details").child("birthdate");
                        databaseReference.setValue(birthDateEditText.getText().toString());
                    }
                    if (imageUrl != null) {
                        databaseReference = firebaseDatabase.getReference().child("users").child(user.getUid()).child("details").child("imageURL");
                        databaseReference.setValue(imageUrl.toString());
                    }
                    if(!profileName.getEditText().getText().toString().equals(null) && !profileSurname.getEditText().getText().toString().equals(null) && !profilePhone.getEditText().getText().toString().equals(null)
                            && !profileBloodGroup.getEditText().getText().toString().equals(null) && !profileAddress.getEditText().getText().toString().equals(null)
                            && !birthDateEditText.getText().toString().equals(null)){
                        startActivity(new Intent(ProfileActivity.this,MainActivity.class));
                        finish();
                    }
                    else{
                        Toast.makeText(ProfileActivity.this, "All fields must be filled", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        else{

            setContentView(R.layout.activity_profilebloodbank);
            profilePicture=(de.hdodenhof.circleimageview.CircleImageView)findViewById(R.id.profile_image);
            profileName = (TextInputLayout)findViewById(R.id.profile_name);
            profileSurname = (TextInputLayout)findViewById(R.id.profile_surname);
            profileBbName = (TextInputLayout)findViewById(R.id.profile_bb_name);
            profilePhone = (TextInputLayout)findViewById(R.id.profile_phone);
            profileAddress = (TextInputLayout)findViewById(R.id.profile_address);
            locateFab = (FloatingActionButton)findViewById(R.id.locateFab);

            firebaseDatabase = FirebaseDatabase.getInstance();
            firebaseStorage = FirebaseStorage.getInstance();
            firebaseAuth = FirebaseAuth.getInstance();
            user = firebaseAuth.getCurrentUser();
            storageReference = firebaseStorage.getReference().child("bloodBankPhotos");

            if(!t){
                fetchFirebaseDatabase = FirebaseDatabase.getInstance();
                fetchDatabaseReference = firebaseDatabase.getReference().child("users");

                fetchDatabaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                            if(snapshot.getKey().equals(user.getUid())){
                                profileName.getEditText().setText(snapshot.child("details").child("firstname").getValue().toString());
                                profileSurname.getEditText().setText(snapshot.child("details").child("surname").getValue().toString());
                                profileBbName.getEditText().setText(snapshot.child("details").child("bloodbankname").getValue().toString());
                                profileAddress.getEditText().setText(snapshot.child("details").child("address").getValue().toString());
                                profilePhone.getEditText().setText(snapshot.child("details").child("phone").getValue().toString());
                                if(snapshot.child("details").hasChild("imageURL")){
                                    Picasso.with(profilePicture.getContext()).load(snapshot.child("details").child("imageURL").getValue().toString()).into(profilePicture);
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            locateFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!profileName.getEditText().getText().toString().equals(null)){
                        databaseReference = firebaseDatabase.getReference().child("users").child(user.getUid()).child("details").child("firstname");
                        databaseReference.setValue(profileName.getEditText().getText().toString());
                    }
                    if(!profileSurname.getEditText().getText().toString().equals(null)){
                        databaseReference = firebaseDatabase.getReference().child("users").child(user.getUid()).child("details").child("surname");
                        databaseReference.setValue(profileSurname.getEditText().getText().toString());
                    }
                    if(!profilePhone.getEditText().getText().toString().equals(null)){
                        databaseReference = firebaseDatabase.getReference().child("users").child(user.getUid()).child("details").child("phone");
                        databaseReference.setValue(profilePhone.getEditText().getText().toString());
                    }
                    if(!profileBbName.getEditText().getText().toString().equals(null)){
                        databaseReference = firebaseDatabase.getReference().child("users").child(user.getUid()).child("details").child("bloodbankname");
                        databaseReference.setValue(profileBbName.getEditText().getText().toString());
                    }
                    if(!profileAddress.getEditText().getText().toString().equals(null)){
                        databaseReference = firebaseDatabase.getReference().child("users").child(user.getUid()).child("details").child("address");
                        databaseReference.setValue(profileAddress.getEditText().getText().toString());
                    }
                    if(imageUrl!=null){
                        databaseReference = firebaseDatabase.getReference().child("users").child(user.getUid()).child("details").child("imageURL");
                        databaseReference.setValue(imageUrl.toString());
                    }
                    if(!profileName.getEditText().getText().toString().equals(null) && !profileSurname.getEditText().getText().toString().equals(null) && !profilePhone.getEditText().getText().toString().equals(null)
                            && !profileBbName.getEditText().getText().toString().equals(null) && !profileAddress.getEditText().getText().toString().equals(null)){
                        startActivity(new Intent(ProfileActivity.this,MainActivity.class));
                        finish();
                    }
                    else{
                        Toast.makeText(ProfileActivity.this, "All fields must be filled", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    @Override
    public void onBackPressed() {
        if(t){
            return;
        }
        else {
            Log.e("tag",Boolean.toString(t));
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                if (!t)
                onBackPressed();
                break;

            case R.id.menu_save:
                onSavePressed();
                break;
        }
        return true;
    }

    private void onSavePressed() {

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.profile_menu,menu);
        return true;
    }

    public void getPicture(View v){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/jpeg");
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);
    }


    private void updateLabel(Calendar myCalendar) {

        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        birthDateEditText.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_PHOTO_PICKER){
            if(resultCode == RESULT_OK){
                final Uri selectedImageUri = data.getData();
                StorageReference photoRef = storageReference.child(selectedImageUri.getLastPathSegment());

                photoRef.putFile(selectedImageUri)
                        .addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                imageUrl = taskSnapshot.getDownloadUrl();
                                Picasso.with(ProfileActivity.this).load(selectedImageUri).into(profilePicture);
                            }
                        });
            }
        }

    }

    /*
    private void postThisEvent(EventData eventData) {

        HashMap<String,Object> eventHashmap = eventData.getHashMap();

        root = FirebaseDatabase.getInstance().getReference();

        String eventKey = root.child(Constants.EVENT).push().getKey();

        HashMap<String,Object> updateHashMap = new HashMap<>();

        updateHashMap.put("/"+Constants.EVENT+"/" + eventKey,eventHashmap);

        root.updateChildren(updateHashMap, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError!= null){
                    Toast.makeText(getApplicationContext(),"Error: "+databaseError,Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_SHORT).show();
                }
            }
        });

        finish();

    }
     */



}
