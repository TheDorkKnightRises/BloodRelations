package smartindia.santas.bloodrelations.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import smartindia.santas.bloodrelations.Constants;
import smartindia.santas.bloodrelations.R;

public class BloodBankDetailsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    String item;
    List <String> categories;
    TextView opos, oneg, apos, aneg, bpos, bneg, abpos, abneg;
    EditText bloodQuantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSharedPreferences(Constants.PREFS, MODE_PRIVATE).getBoolean(Constants.DARK_THEME, false))
            setTheme(R.style.AppTheme_Dark);
        setContentView(R.layout.activity_blood_bank_details);

        bloodQuantity = (EditText) findViewById(R.id.bloodQuantity);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);

        // Spinner click listener
        spinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);

        // Spinner Drop down elements
        categories = new ArrayList <String>();
        categories.add("O+");
        categories.add("O-");
        categories.add("A+");
        categories.add("A-");
        categories.add("B+");
        categories.add("B-");
        categories.add("AB+");
        categories.add("AB-");

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
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }

    public void update_blood_details(View view)
    {
        String units = bloodQuantity.getText().toString();
        String choice=item;
        for(int i=0; i<8; i++)
        {
            if(categories.get(i).equals(choice.toString()))
            {
                switch (choice.toString())
                {
                    case "O+":
                        opos=(TextView) findViewById(R.id.opos);
                        opos.setText(units);
                        break;
                    case "O-":
                        oneg=(TextView) findViewById(R.id.oneg);
                        oneg.setText(units);
                        break;
                    case "A+":
                        apos=(TextView) findViewById(R.id.apos);
                        apos.setText(units);
                        break;
                    case "A-":
                        aneg=(TextView) findViewById(R.id.aneg);
                        aneg.setText(units);
                        break;
                    case "B+":
                        bpos=(TextView) findViewById(R.id.bpos);
                        bpos.setText(units);
                        break;
                    case "B-":
                        bneg=(TextView) findViewById(R.id.bneg);
                        bneg.setText(units);
                        break;
                    case "AB+":
                        abpos=(TextView) findViewById(R.id.abpos);
                        abpos.setText(units);
                        break;
                    case "AB-":
                        abneg=(TextView) findViewById(R.id.abneg);
                        abneg.setText(units);
                        break;

                }
            }
        }
    }


}
