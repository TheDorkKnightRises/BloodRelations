package smartindia.santas.bloodrelations.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import smartindia.santas.bloodrelations.R;

/**
 * Created by DELL on 02/04/2017.
 */

public class AboutActivity extends AppCompatActivity {

    //Button github;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        //github =(Button)findViewById(R.id.github);
        /*github.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Github();
            }
        });*/
    }

    /*public void Github(){
        Uri uri = Uri.parse(getResources().getString(R.string.github));
        Intent i = new Intent(Intent.ACTION_VIEW,uri);
        startActivity(i);
    }*/
}
