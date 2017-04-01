package smartindia.santas.bloodrelations.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import smartindia.santas.bloodrelations.R;

/**
 * Created by DELL on 31/03/2017.
 */

public class SignUpActivity extends AppCompatActivity{

    private static final int RC_SIGN_IN = 9001;

    EditText emailEdiText ,passwordEditText;
    Button signupButton;//loginButton,;
    private FirebaseAuth firebaseAuth;
    private  FirebaseAuth.AuthStateListener authStateListener;
    GoogleApiClient googleApiClient;
    SignInButton googleButton;
    //Button signout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        emailEdiText = (EditText)findViewById(R.id.email_editText);
        passwordEditText =(EditText)findViewById(R.id.password_editText);
        signupButton= (Button)findViewById(R.id.signinButton);
        //loginButton = (Button)findViewById(R.id.loginButton);
        googleButton = (SignInButton)findViewById(R.id.googleButton);
        //signout = (Button)findViewById(R.id.signout);
        /*signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
            }
        });*/

        firebaseAuth =FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null){
                    //Toast.makeText(getApplicationContext(),"onAuthStateChanged:signed_in:" + user.getUid(),Toast.LENGTH_LONG).show();
                    // Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    //startActivity(new Intent(Login.this, MainActivity.class));
                    //finish();
                    Toast.makeText(SignUpActivity.this,user.getUid().toString(),Toast.LENGTH_SHORT).show();
                }
                else {
                    //Toast.makeText(getApplicationContext(), "onAuthStateChanged:signed_out", Toast.LENGTH_SHORT).show();
                }
            }
        };

        /*loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });*/

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUP();
            }
        });


        /*Google SignUp*/

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,
                        new GoogleApiClient.OnConnectionFailedListener(){
                            @Override
                            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                                Toast.makeText(getApplicationContext(),"onConnectionFailed "+ connectionResult,Toast.LENGTH_LONG).show();
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
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account){

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Toast.makeText(getApplicationContext(),"SignInWithCredential Complete "+task.isSuccessful(),Toast.LENGTH_SHORT).show();
                        /*if(!task.isSuccessful()){

                        }*/
                    }
                });
    }



    public void signUP(){

        firebaseAuth.createUserWithEmailAndPassword(
                emailEdiText.getText().toString(),
                passwordEditText.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Toast.makeText(getApplicationContext(),"createUserWithEmail:onComplete:" + task.isSuccessful(),Toast.LENGTH_SHORT).show();
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(),"Unsuccessful",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(authStateListener != null){
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }

}
