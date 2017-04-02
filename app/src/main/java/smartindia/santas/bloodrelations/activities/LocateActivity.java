package smartindia.santas.bloodrelations.activities;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.PermissionChecker;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import smartindia.santas.bloodrelations.Constants;
import smartindia.santas.bloodrelations.R;
import smartindia.santas.bloodrelations.objects.BloodBank;
import smartindia.santas.bloodrelations.objects.Donor;

public class LocateActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final int LOCATION_REQUEST_CODE = 123;
    private GoogleMap mMap;
    ArrayList<Donor> donorArrayList;
    ArrayList<BloodBank> bankArrayList;
    DatabaseReference root;
    SharedPreferences prefs ;

    public static boolean isLocationEnabled(Context context) {
        int locationMode;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locate);

        root = FirebaseDatabase.getInstance().getReference();

        prefs = getSharedPreferences(Constants.PREFS,MODE_PRIVATE);
        if(prefs.getBoolean(Constants.ISBLOODBANK,false)){
            donorArrayList = new ArrayList<>();
            new DonorsFetch().execute();
        }
        else{
            bankArrayList = new ArrayList<>();
            new FetchBloodBankList().execute();
        }


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i("Map", "Place: " + place.getName());
                addMarkers(place.getLatLng());
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i("Map", "An error occurred: " + status);
            }
        });
    }

    private void onDonorFetchComplete(ArrayList<Donor> arrayList){

    }

    private void onBankFetchComplete(ArrayList<BloodBank> arrayList){

    }


    class DonorsFetch extends AsyncTask<Void,Void,ArrayList<Donor>>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            donorArrayList.clear();
        }

        @Override
        protected ArrayList<Donor> doInBackground(Void... voids) {
            ValueEventListener listener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {
                        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                            String isBank = (String) snapshot.child("isBloodBank").getValue();
                            if(isBank.equals("false")){
                                String firstname = snapshot.child("details").child("firstname").getValue().toString();
                                String surname = snapshot.child("details").child("surname").getValue().toString();
                                String name = firstname + " " + surname;
                                String location = snapshot.child("details").child("address").getValue().toString();
                                String bloodGroup = snapshot.child("details").child("bloodgroup").getValue().toString();
                                String phone = snapshot.child("details").child("phone").getValue().toString();
                                donorArrayList.add(new Donor(name,location,bloodGroup,phone));
                            }
                        }
                        //updateUI();
                        onDonorFetchComplete(donorArrayList);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            root.addValueEventListener(listener);
            return null;
        }
    }

    class FetchBloodBankList extends AsyncTask<Void,Void,ArrayList<BloodBank>> {
        @Override
        protected ArrayList<BloodBank> doInBackground(Void... params) {

            ValueEventListener valueEventListener= new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    bankArrayList.clear();
                    try {
                        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                            String isBank = (String) snapshot.child("isBloodBank").getValue();
                            if(isBank.equals("true")){
                                String bbname = snapshot.child("details").child("bloodbankname").getValue().toString();
                                String location = snapshot.child("details").child("address").getValue().toString();
                                String phone = snapshot.child("details").child("phone").getValue().toString();
                                bankArrayList.add(new BloodBank(bbname,location,phone));

                            }
                        }
                        onBankFetchComplete(bankArrayList);
                        //updateUI();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };

            root.addValueEventListener(valueEventListener);
            return bankArrayList;
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (Build.VERSION.SDK_INT >= 23 && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PermissionChecker.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
        } else mMap.setMyLocationEnabled(true);

        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                if (!isLocationEnabled(LocateActivity.this))
                    Toast.makeText(LocateActivity.this, "Turn on Location Services", Toast.LENGTH_LONG).show();
                else if (mMap.isMyLocationEnabled() && mMap.getMyLocation() != null) {
                    LatLng myLocation = new LatLng(mMap.getMyLocation().getLatitude(), mMap.getMyLocation().getLongitude());
                    addMarkers(myLocation);
                }
                return true;
            }
        });

        if (mMap.isMyLocationEnabled() && mMap.getMyLocation() != null) {
            LatLng myLocation = new LatLng(mMap.getMyLocation().getLatitude(), mMap.getMyLocation().getLongitude());
            addMarkers(myLocation);
        }

    }

    private void addMarkers(LatLng location) {
        mMap.clear();
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 14f));
        LatLng loc1 = new LatLng(location.latitude - 0.0030, location.longitude + 0.0087);
        mMap.addMarker(new MarkerOptions().position(loc1).title("Blood Bank"));
        LatLng loc2 = new LatLng(location.latitude + 0.0057, location.longitude + 0.0092);
        mMap.addMarker(new MarkerOptions().position(loc2).title("Blood Bank"));
        LatLng loc3 = new LatLng(location.latitude - 0.0009, location.longitude - 0.0025);
        mMap.addMarker(new MarkerOptions().position(loc3).title("Blood Bank"));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults[0] == PermissionChecker.PERMISSION_GRANTED)
                try {
                    mMap.setMyLocationEnabled(true);
                } catch (SecurityException e) {
                    Toast.makeText(this, "Location permission is required to show your location on the map", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            else
                Toast.makeText(this, "Location permission is required to show your location on the map", Toast.LENGTH_LONG).show();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
