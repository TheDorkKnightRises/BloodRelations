package smartindia.santas.bloodrelations.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import smartindia.santas.bloodrelations.Constants;
import smartindia.santas.bloodrelations.R;

import static smartindia.santas.bloodrelations.R.string.profile;

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

    private Uri imageUrl;

    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefs = getSharedPreferences(Constants.PREFS, MODE_PRIVATE);
        if(!prefs.getBoolean(Constants.ISBLOODBANK,false)){

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
                    startActivity(new Intent(ProfileActivity.this,MainActivity.class));
                }
            });
        }

        else{

            setContentView(R.layout.activity_profilebloodbank);

            profilePicture=(de.hdodenhof.circleimageview.CircleImageView)findViewById(R.id.profile_image);
            birthDateEditText=(EditText) findViewById(R.id.birthday_edittext);
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
                    if(profileBbName.getEditText().getText().toString().equals(null)){
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
                    startActivity(new Intent(ProfileActivity.this,MainActivity.class));
                }
            });
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
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

}
