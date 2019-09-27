package smartindia.santas.bloodrelations.activities;

import android.content.Intent;
import android.media.Rating;
import android.net.Uri;
import android.support.annotation.IdRes;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import smartindia.santas.bloodrelations.Constants;
import smartindia.santas.bloodrelations.R;

public class FeedbackActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    EditText subject;
    EditText description;
    RatingBar rating;
    String item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSharedPreferences(Constants.PREFS, MODE_PRIVATE).getBoolean(Constants.DARK_THEME, false))
            setTheme(R.style.AppTheme_Dark);
        setContentView(R.layout.activity_feedback);


        subject= (TextInputEditText)findViewById(R.id.subject);
        description=(TextInputEditText)findViewById(R.id.description);
        rating=(RatingBar)findViewById(R.id.rating);


        Spinner spinner = (Spinner) findViewById(R.id.spinner1);

        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Feedback");
        categories.add("Suggestion");
        categories.add("Question");
        categories.add("Problem");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter <String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), item +" Selected", Toast.LENGTH_LONG).show();

    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }

    public void nextPage(View view){
        String emailSubject = subject.getText().toString();
        String emailBody = description.getText().toString();

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto","sihbloodbank@gmail.com", null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, emailSubject + "(Rated:" + String.valueOf(rating.getRating()) + ")");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "(" + item + ")\n" + emailBody);
        startActivity(Intent.createChooser(emailIntent, "Send email..."));
    }
    }

