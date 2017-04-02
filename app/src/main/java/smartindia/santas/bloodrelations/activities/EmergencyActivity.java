package smartindia.santas.bloodrelations.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import smartindia.santas.bloodrelations.R;

public class EmergencyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);
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
