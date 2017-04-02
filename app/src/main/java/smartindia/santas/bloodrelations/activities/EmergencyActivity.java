package smartindia.santas.bloodrelations.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v13.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import smartindia.santas.bloodrelations.Constants;
import smartindia.santas.bloodrelations.R;
import smartindia.santas.bloodrelations.widget.WidgetProvider;

public class EmergencyActivity extends AppCompatActivity {


    private static final int EXTERNAL_STORAGE_PERMISSION_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    private boolean sentToSettings = false;
    private SharedPreferences permissionStatus;
    private int mAppWidgetId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSharedPreferences(Constants.PREFS, MODE_PRIVATE).getBoolean(Constants.DARK_THEME, false))
            setTheme(R.style.AppTheme_Dark);
        setContentView(R.layout.activity_emergency);
        permissionStatus = getSharedPreferences("permissionStatus",MODE_PRIVATE);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);

        Intent callIntent = new Intent(Intent.ACTION_CALL);
        String number = getSharedPreferences(Constants.PREFS, MODE_PRIVATE).getString(Constants.CONTACT, "18001801104");
        ((TextView) findViewById(R.id.emergency_number)).setText(number);
        callIntent.setData(Uri.parse("tel:"+number));
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, callIntent, 0);

        // Get the layout for the App Widget and attach an on-click listener to the button
        RemoteViews views = new RemoteViews(this.getPackageName(), R.layout.widget);
        views.setOnClickPendingIntent(R.id.callButton, pendingIntent);
        appWidgetManager.updateAppWidget(mAppWidgetId, views);

        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_OK, resultValue);

        if (ActivityCompat.checkSelfPermission(EmergencyActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(EmergencyActivity.this, Manifest.permission.CALL_PHONE)) {
                //Show Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(EmergencyActivity.this);
                builder.setTitle("Need Phone Permission");
                builder.setMessage("This app needs phone permission to call emergency contacts.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(EmergencyActivity.this, new String[]{Manifest.permission.CALL_PHONE}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else if (permissionStatus.getBoolean(Manifest.permission.CALL_PHONE,false)) {
                //Previously Permission Request was cancelled with 'Dont Ask Again',
                // Redirect to Settings after showing Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(EmergencyActivity.this);
                builder.setTitle("Need Phone Permission");
                builder.setMessage("This app needs phone permission to call emergency contacts.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        sentToSettings = true;
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                        Toast.makeText(getBaseContext(), "Go to Permissions to grant Phone access", Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else {
                //just request the permission
                ActivityCompat.requestPermissions(EmergencyActivity.this, new String[]{Manifest.permission.CALL_PHONE}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);
            }

            SharedPreferences.Editor editor = permissionStatus.edit();
            editor.putBoolean(Manifest.permission.CALL_PHONE,true);
            editor.commit();


        } else {
            //You already have the permission, just go ahead.
        }


    }

    public void emergencyUpdate(View view) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        final EditText EditText = (EditText)dialog.findViewById(R.id.dialog_editText);
        final TextView titletext = (TextView)dialog.findViewById(R.id.title);
        titletext.setText(R.string.your_emergency_contact);

        Button ok = (Button)dialog.findViewById(R.id.password_dialog_ok);
        ok.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String entered_data= EditText.getText().toString();
                        getSharedPreferences(Constants.PREFS, MODE_PRIVATE).edit().putString(Constants.CONTACT, entered_data).apply();
                        ((TextView) findViewById(R.id.emergency_number)).setText(entered_data);
                        dialog.cancel();
                        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(EmergencyActivity.this);
                        ComponentName thisAppWidget = new ComponentName(EmergencyActivity.this,  WidgetProvider.class);

                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        String number = getSharedPreferences(Constants.PREFS, MODE_PRIVATE).getString(Constants.CONTACT, "18001801104");
                        ((TextView) findViewById(R.id.emergency_number)).setText(number);
                        callIntent.setData(Uri.parse("tel:"+number));
                        PendingIntent pendingIntent = PendingIntent.getActivity(EmergencyActivity.this, 0, callIntent, 0);

                        // Get the layout for the App Widget and attach an on-click listener to the button
                        RemoteViews views = new RemoteViews(EmergencyActivity.this.getPackageName(), R.layout.widget);
                        views.setOnClickPendingIntent(R.id.callButton, pendingIntent);
                        int[] ids = appWidgetManager.getAppWidgetIds(thisAppWidget);
                        for (int id : ids) appWidgetManager.updateAppWidget(id, views);

                        Intent resultValue = new Intent();
                        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
                        setResult(RESULT_OK, resultValue);
                    }
                }
        );
        Button cancel = (Button)dialog.findViewById(R.id.password_dialog_cancel);
        cancel.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                }
        );
        dialog.show();
    }


    public void phone(View view)
    {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "100"));
        startActivity(intent);
    }

    public void phone1(View view)
    {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "102"));
        startActivity(intent);
    }

    public void phone2(View view)
    {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "18001801104"));
        startActivity(intent);
    }

    public void phone3(View view)
    {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "108"));
        startActivity(intent);
    }


}