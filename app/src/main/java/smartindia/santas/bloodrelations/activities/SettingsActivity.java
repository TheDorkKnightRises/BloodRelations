package smartindia.santas.bloodrelations.activities;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.Switch;

import smartindia.santas.bloodrelations.Constants;
import smartindia.santas.bloodrelations.R;

public class SettingsActivity extends AppCompatActivity {

    Switch themeSwitch, notifSwitch;
    SharedPreferences shPref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shPref = getSharedPreferences(Constants.PREFS, MODE_PRIVATE);

        if (shPref.getBoolean(Constants.DARK_THEME, false))
            setTheme(R.style.AppTheme_Dark);
        setContentView(R.layout.activity_settings);

        themeSwitch = (Switch) findViewById(R.id.theme_switch);
        notifSwitch = (Switch) findViewById(R.id.notif_switch);

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
