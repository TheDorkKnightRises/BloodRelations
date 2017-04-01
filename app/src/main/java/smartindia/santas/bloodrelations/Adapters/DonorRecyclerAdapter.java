package smartindia.santas.bloodrelations.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

import smartindia.santas.bloodrelations.Objects.BloodBank;
import smartindia.santas.bloodrelations.Objects.Donor;
import smartindia.santas.bloodrelations.R;

/**
 * Created by adityadesai on 01/04/17.
 */

public class DonorRecyclerAdapter extends RecyclerView.Adapter<DonorRecyclerAdapter.DonorHolder> {

    private static ArrayList<Donor> donorList;

    public static class DonorHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView itemName;
        private TextView itemLocation;
        private TextView itemBloodGroup;
        private Donor mDonor;

        public DonorHolder(View v) {
            super(v);

            itemName = (TextView) v.findViewById(R.id.donor_name_textview);
            itemLocation = (TextView) v.findViewById(R.id.donor_location_textview);
            itemBloodGroup = (TextView) v.findViewById(R.id.donor_blood_group_textview);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            CheckBox cb = (CheckBox)v.findViewById(R.id.select_donor);
            if(cb.isChecked()){
                cb.setChecked(false);
            }
            else{
                cb.setChecked(true);
            }
        }

        public void bindIndustry(Donor donor) {
            mDonor = donor;
            itemName.setText(donor.getDonorName());
            itemLocation.setText(donor.getDonorLocation());
            itemBloodGroup.setText(donor.getDonorBloodGroup());
        }
    }

    public DonorRecyclerAdapter(ArrayList<Donor> donors) {
        donorList = donors;
    }

    @Override
    public DonorRecyclerAdapter.DonorHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.donor_item, parent, false);
        return new DonorRecyclerAdapter.DonorHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(DonorRecyclerAdapter.DonorHolder holder, int position) {
        Donor itemDonor = donorList.get(position);
        holder.bindIndustry(itemDonor);
    }

    @Override
    public int getItemCount() {
        return donorList.size();
    }
}
