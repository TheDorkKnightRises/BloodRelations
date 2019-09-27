package smartindia.santas.bloodrelations.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.transition.ChangeBounds;
import android.support.transition.Scene;
import android.support.transition.Transition;
import android.support.transition.TransitionManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import smartindia.santas.bloodrelations.Constants;
import smartindia.santas.bloodrelations.R;

/**
 * Created by DELL on 31/03/2017.
 */

public class LoginActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    Boolean backPressFlag = false;
    SharedPreferences pref;
    String email;

    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressText;
    private View mProgressView;
    private View mLoginFormView;
    GoogleApiClient googleApiClient;
    SignInButton googleButton;


    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private ProgressDialog dialog;
    boolean check;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListener;
    private FirebaseUser user;
    private String isBank;

    private static final int RC_SIGN_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null){
                    //Toast.makeText(getApplicationContext(),"onAuthStateChanged:signed_in:" + user.getUid(),Toast.LENGTH_LONG).show();
                    // Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    //startActivity(new Intent(Login.this, MainActivity.class));
                    //finish();
                    Toast.makeText(LoginActivity.this,user.getUid().toString(),Toast.LENGTH_SHORT).show();
                    Toast.makeText(LoginActivity.this,"Signed in",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                    finish();
                } else {
                    //Toast.makeText(getApplicationContext(), "onAuthStateChanged:signed_out", Toast.LENGTH_SHORT).show();
                }
            }
        };


        firebaseAuth =FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user != null){
            Toast.makeText(LoginActivity.this,"Signed in",Toast.LENGTH_SHORT).show();
            SharedPreferences prefs = getSharedPreferences(Constants.PREFS,MODE_PRIVATE);
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
            finish();
        }
        else{
            Toast.makeText(LoginActivity.this,"Not Signed in",Toast.LENGTH_SHORT).show();
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    pref = getSharedPreferences(Constants.PREFS, MODE_PRIVATE);
                    if (!pref.getBoolean(Constants.LOGGED_IN, false)) {
                        ViewGroup sceneRoot = (ViewGroup) findViewById(R.id.sceneroot);
                        Scene endScene = Scene.getSceneForLayout(sceneRoot, R.layout.activity_login, LoginActivity.this);
                        Transition transition = new ChangeBounds().setDuration(750);
                        TransitionManager.go(endScene, transition);
                        // Set up the login form.
                        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
                        mEmailView.setDropDownBackgroundResource(R.color.white);

                        mPasswordView = (EditText) findViewById(R.id.password);
                        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                            @Override
                            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                                    attemptLogin();
                                    return true;
                                }
                                return false;
                            }
                        });

                        final Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
                        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                attemptLogin();
                            }
                        });

                        Button register = (Button) findViewById(R.id.email_register_button);
                        register.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
                                finish();
                            }
                        });

                        //TODO : Delete bypass
                    /*Button button = (Button)findViewById(R.id.bypass);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(LoginActivity.this,MainActivity.class));
                            finish();
                        }
                    });*/

                        mLoginFormView = findViewById(R.id.login_form);
                        mProgressView = findViewById(R.id.login_progress);
                        mProgressText = findViewById(R.id.progress_text);


                        googleButton = (SignInButton)findViewById(R.id.googleButton);
                        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                .requestIdToken(getString(R.string.default_web_client_id))
                                .requestEmail()
                                .build();

                        googleApiClient = new GoogleApiClient.Builder(LoginActivity.this)
                                .enableAutoManage(LoginActivity.this,
                                        new GoogleApiClient.OnConnectionFailedListener(){
                                            @Override
                                            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                                                Toast.makeText(LoginActivity.this,"onConnectionFailed "+ connectionResult,Toast.LENGTH_LONG).show();
                                            }
                                        })
                                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                                .build();


                        googleButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                googleSignIn();
                            }
                        });


                    } else {
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    }
                    handler.removeCallbacks(this);
                }
            }, 750);


        }

    }

    private void googleSignIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent,RC_SIGN_IN);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(result.isSuccess()){
                //Toast.makeText(getApplicationContext(),"result.isSuccess",Toast.LENGTH_SHORT).show();
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            }
            else{
                Toast.makeText(getApplicationContext(),"Google SignIn Failed",Toast.LENGTH_SHORT).show();
                showProgress(false);
            }
        }
    }


    private void firebaseAuthWithGoogle(GoogleSignInAccount account){

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        //new MyAsyncTask(user.getUid());
                        startActivity(new Intent(LoginActivity.this,UserTypeActivity.class));
                        //Toast.makeText(getApplicationContext(),"SignInWithCredential Complete "+task.isSuccessful(),Toast.LENGTH_SHORT).show();


                    }
                });
    }


    private class FetchTask extends AsyncTask<Void,Void,String>{

        DatabaseReference root,isBloodBank_root;
        ValueEventListener listener;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(LoginActivity.this);
            dialog.show();
            String uid = firebaseAuth.getCurrentUser().getUid();

            root = FirebaseDatabase.getInstance().getReference();

            isBloodBank_root  = root.child("users").child(uid).child("isBloodBank");


        }

        @Override
        protected String doInBackground(Void... voids) {

            listener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String value = dataSnapshot.getValue().toString();
                    updatePrefs(value);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };

            isBloodBank_root.addValueEventListener(listener);

            return null;
        }

    }

    private class MyAsyncTask extends AsyncTask<Void,Void,Void>{

        String UID;
        MyAsyncTask(String UID){
            this.UID = UID;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            DatabaseReference root = FirebaseDatabase.getInstance().getReference();
            ValueEventListener listener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    check = dataSnapshot.hasChild(UID);
                    dialog.dismiss();
                    if(check){
                        String value = dataSnapshot.child(UID).child(Constants.ISBLOODBANK).toString();
                        boolean isBloodBank = value.equals("true");
                        SharedPreferences preferences = getSharedPreferences(Constants.PREFS,MODE_PRIVATE);
                        SharedPreferences.Editor editor=preferences.edit();
                        editor.putBoolean(Constants.ISBLOODBANK,isBloodBank);
                        editor.putBoolean(Constants.ISPROFILEFILLED,true);
                        editor.apply();
                        startActivity(new Intent(LoginActivity.this,MainActivity.class));
                    }
                    else{
                        startActivity(new Intent(LoginActivity.this,UserTypeActivity.class));
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

    private void updatePrefs(String value){
        boolean isBloodBank;
        if(value.equals("true")) isBloodBank = true;
        else isBloodBank=false;

        pref = getSharedPreferences(Constants.PREFS,MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(Constants.ISBLOODBANK,isBloodBank);
        editor.apply();

        dialog.dismiss();
        //startActivity(new Intent(LoginActivity.this,UserTypeActivity.class));

    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        email = mEmailView.getText().toString().trim();
        String password = mPasswordView.getText().toString().trim();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            //true
            showProgress(true);
            login();

        }

    }

    public void login(){

        firebaseAuth.signInWithEmailAndPassword(
                mEmailView.getText().toString(),
                mPasswordView.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //Toast.makeText(getApplicationContext(),"signinUserWithEmail:onComplete:" + task.isSuccessful(),Toast.LENGTH_SHORT).show();
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener
                        if (!task.isSuccessful()) {
                            showProgress(false);
                            try {
                                String msg = task.getResult().toString();
                                Toast.makeText(getApplicationContext(), "Unsuccessful" + msg, Toast.LENGTH_SHORT).show();
                            }
                            catch(Exception e){e.printStackTrace();}
                        }
                    }
                });
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
        mProgressText.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseAuth =FirebaseAuth.getInstance();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authStateListener != null) {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }

    @Override
    public void onBackPressed() {
        if (backPressFlag)
            finishAffinity();
        else {
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
            backPressFlag = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    backPressFlag = false;
                }
            }, 2000);
        }
    }
}
