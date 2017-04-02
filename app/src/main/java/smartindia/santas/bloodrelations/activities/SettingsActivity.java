package smartindia.santas.bloodrelations.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import smartindia.santas.bloodrelations.Constants;
import smartindia.santas.bloodrelations.R;

public class SettingsActivity extends AppCompatActivity {

    Switch themeSwitch, notifSwitch;
    TextView languageSwitch;
    SharedPreferences shPref;
    SharedPreferences.Editor editor;
    TextView logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shPref = getSharedPreferences(Constants.PREFS, MODE_PRIVATE);

        if (shPref.getBoolean(Constants.DARK_THEME, false))
            setTheme(R.style.AppTheme_Dark);
        setContentView(R.layout.activity_settings);

        languageSwitch = (TextView) findViewById(R.id.change_language);
        languageSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SettingsActivity.this, R.string.language_settings, Toast.LENGTH_SHORT).show();
                Intent i = new Intent(android.provider.Settings.ACTION_LOCALE_SETTINGS);
                startActivity(i);
            }
        });
        themeSwitch = (Switch) findViewById(R.id.theme_switch);
        notifSwitch = (Switch) findViewById(R.id.notif_switch);
        logout = (TextView)findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                SharedPreferences pref = getSharedPreferences(Constants.PREFS,MODE_PRIVATE);
                pref.edit().clear().apply();
                startActivity(new Intent(SettingsActivity.this,LoginActivity.class));
                finish();

            }
        });

        editor = shPref.edit();

        if (shPref.getBoolean(Constants.DARK_THEME, false))
            themeSwitch.setChecked(true);
        if (shPref.getBoolean(Constants.NOTIFICATIONS, true))
            notifSwitch.setChecked(true);

        //attach a listener to check for changes in state
        themeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    editor.putBoolean(Constants.DARK_THEME, true);
                    editor.apply();
                } else {
                    editor.putBoolean(Constants.DARK_THEME, false);
                    editor.commit();
                    //Toast.makeText(SettingsActivity.this, String.valueOf(shPref.getBoolean(Constants.DARK_THEME, false)), Toast.LENGTH_SHORT).show();
                }
                AlertDialog.Builder alert = new AlertDialog.Builder(SettingsActivity.this).setMessage("App will now close. Restart to apply theme changes")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                SettingsActivity.this.finishAffinity();
                            }
                        });
                alert.show();
            }
        });

        notifSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    editor.putBoolean(Constants.NOTIFICATIONS, true);
                    editor.apply();
                } else {
                    editor.putBoolean(Constants.NOTIFICATIONS, false);
                    editor.commit();
                }
            }
        });

    }

}
