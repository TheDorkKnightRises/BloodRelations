package smartindia.santas.bloodrelations.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import smartindia.santas.bloodrelations.objects.BloodBank;
import smartindia.santas.bloodrelations.R;

public class BloodBankRecyclerAdapter extends RecyclerView.Adapter<BloodBankRecyclerAdapter.BloodBankHolder> {

    private static ArrayList<BloodBank> bloodBankList;

    public static class BloodBankHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView itemName;
        private TextView itemLocation;
        private TextView itemPhone;
        private BloodBank mBloodBank;

        public BloodBankHolder(View v) {
            super(v);

            itemName = (TextView) v.findViewById(R.id.blood_bank_name_textview);
            itemLocation = (TextView) v.findViewById(R.id.blood_bank_location_textview);
            itemPhone = (TextView) v.findViewById(R.id.blood_bank_phone_textview);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
        }

        public void bindIndustry(BloodBank bloodBank) {
            mBloodBank = bloodBank;
            itemName.setText(bloodBank.getBbName());
            itemLocation.setText(bloodBank.getBbLocation());
            itemPhone.setText(bloodBank.getBbPhone());
        }
    }

    public BloodBankRecyclerAdapter(ArrayList<BloodBank> bloodbanks) {
        bloodBankList = bloodbanks;
    }

    @Override
    public BloodBankRecyclerAdapter.BloodBankHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.blood_bank_item, parent, false);
        return new BloodBankRecyclerAdapter.BloodBankHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(BloodBankRecyclerAdapter.BloodBankHolder holder, int position) {
        BloodBank itemBloodBank = bloodBankList.get(position);
        holder.bindIndustry(itemBloodBank);
    }

    @Override
    public int getItemCount() {
        return bloodBankList.size();
    }
}
