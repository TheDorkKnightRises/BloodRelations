package smartindia.santas.bloodrelations.activities;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import smartindia.santas.bloodrelations.Constants;
import smartindia.santas.bloodrelations.R;

public class SettingsActivity extends AppCompatActivity {

    Switch mySwitch;
    SharedPreferences shPref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shPref = getSharedPreferences(Constants.PREFS, MODE_PRIVATE);

        if (shPref.getBoolean(Constants.DARK_THEME, false))
            setTheme(R.style.AppTheme_Dark);
        setContentView(R.layout.activity_settings);




        mySwitch = (Switch) findViewById(R.id.mySwitch);

        editor = shPref.edit();

        if (shPref.getBoolean(Constants.DARK_THEME, false))
            mySwitch.setChecked(true);


        //attach a listener to check for changes in state
        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    editor.putBoolean(Constants.DARK_THEME, true);
                    editor.commit();
                    Toast.makeText(SettingsActivity.this, "Please restart app", Toast.LENGTH_SHORT).show();

                }else{
                    editor.putBoolean(Constants.DARK_THEME, false);
                    editor.commit();
                    //Toast.makeText(SettingsActivity.this, String.valueOf(shPref.getBoolean(Constants.DARK_THEME, false)), Toast.LENGTH_SHORT).show();

                }

            }
        });

    }

}
