package smartindia.santas.bloodrelations.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import smartindia.santas.bloodrelations.adapters.BloodBankRecyclerAdapter;
import smartindia.santas.bloodrelations.Objects.BloodBank;
import smartindia.santas.bloodrelations.R;

public class BloodBankListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BloodBankRecyclerAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<BloodBank> bloodBankList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_bank_list);

        bloodBankList=new ArrayList<>();

        bloodBankList.add(new BloodBank("Placeholder Name Here","Placeholder Location Here","Placeholder Phone Here"));
        bloodBankList.add(new BloodBank("Placeholder Name Here","Placeholder Location Here","Placeholder Phone Here"));
        bloodBankList.add(new BloodBank("Placeholder Name Here","Placeholder Location Here","Placeholder Phone Here"));
        bloodBankList.add(new BloodBank("Placeholder Name Here","Placeholder Location Here","Placeholder Phone Here"));
        bloodBankList.add(new BloodBank("Placeholder Name Here","Placeholder Location Here","Placeholder Phone Here"));
        bloodBankList.add(new BloodBank("Placeholder Name Here","Placeholder Location Here","Placeholder Phone Here"));
        bloodBankList.add(new BloodBank("Placeholder Name Here","Placeholder Location Here","Placeholder Phone Here"));

        recyclerView=(RecyclerView)findViewById(R.id.blood_bank_recyclerview);
        adapter=new BloodBankRecyclerAdapter(bloodBankList);
        linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }
}
