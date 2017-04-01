package smartindia.santas.bloodrelations.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import smartindia.santas.bloodrelations.Constants;
import smartindia.santas.bloodrelations.R;


public class HelpActivity extends AppCompatActivity {

    ExpandableListView faqListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSharedPreferences(Constants.PREFS, MODE_PRIVATE).getBoolean(Constants.DARK_THEME, false))
            setTheme(R.style.AppTheme_Dark);
        setContentView(R.layout.activity_help);

        faqListView  = (ExpandableListView) findViewById(R.id.list_faq);
        String[] groups;
        String[][] children;

        groups = new String[]{"How does the blood donation process work?","Will it hurt when you insert the needle?","How long does a blood donation take?","How often can I donate blood?","Is it safe to give blood?","Can I get HIV from donating blood?","Who can donate blood?","Does the Red Cross pay blood donors?"};

        children = new String[][]
                {{"Donating blood is a simple thing to do, but can make a big difference in the lives of others. The donation process from the time you arrive until the time you leave takes about an hour."},
                        {"Only for a moment. Pinch the fleshy, soft underside of your arm. That pinch is similar to what you will feel when the needle is inserted."}, {"The entire process takes about one hour and 15 minutes; the actual donation of a pint of whole blood unit takes eight to 10 minutes. However, the time varies slightly with each person depending on several factors including the donor’s health history and attendance at the blood drive."},{"You must wait at least eight weeks (56 days) between donations of whole blood and 16 weeks (112 days) between Power Red donations. Platelet apheresis donors may give every 7 days up to 24 times per year. Regulations are different for those giving blood for themselves (autologous donors)."},{"Donating blood is a safe process. Each donor’s blood is collected through a new, sterile needle that is used once and then discarded. Although most people feel fine after donating blood, a small number of people may feel lightheaded or dizzy, have an upset stomach or experience a bruise or pain where the needle was inserted. Extremely rarely, loss of consciousness, nerve damage or artery damage occur."},{"No. Sterile procedures and disposable equipment are used in all Red Cross donor centers. We use a needle only once and then dispose of it. You cannot contract HIV or other viral disease by donating blood."},{"In most states, donors must be age 17 or older. Some states allow donation by 16-year-olds with a signed parental consent form. Donors must weigh at least 110 pounds and be in good health. "},{"No. All Red Cross blood donors are volunteers. In fact, all blood collected for transfusion in the United States must be from volunteer donors."}};

        faqListView.setAdapter(new ExpandableListAdapter(groups, children));
        faqListView.setGroupIndicator(null);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public class ExpandableListAdapter extends BaseExpandableListAdapter {

        private final LayoutInflater inf;
        private String[] groups;
        private String[][] children;

        public ExpandableListAdapter(String[] groups, String[][] children) {
            this.groups = groups;
            this.children = children;
            inf = LayoutInflater.from(getApplicationContext());
        }

        @Override
        public int getGroupCount() {
            return groups.length;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return children[groupPosition].length;
        }

        @Override
        public Object getGroup(int groupPosition) {
            return groups[groupPosition];
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return children[groupPosition][childPosition];
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

            ExpandableListAdapter.ViewHolder holder;
            if (convertView == null) {
                convertView = inf.inflate(R.layout.list_item, parent, false);
                holder = new ExpandableListAdapter.ViewHolder();

                holder.text = (TextView) convertView.findViewById(R.id.lblListItem);
                //holder.text.setBackgroundColor(getResources().getColor(R.color.colorTranslucent));
                convertView.setTag(holder);
            } else {
                holder = (ExpandableListAdapter.ViewHolder) convertView.getTag();
            }

            holder.text.setText(getChild(groupPosition, childPosition).toString());
            if (getSharedPreferences(Constants.PREFS, MODE_PRIVATE).getBoolean(Constants.DARK_THEME, false))
                holder.text.setTextColor(Color.WHITE);
            holder.text.setAutoLinkMask(Linkify.PHONE_NUMBERS);

            return convertView;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            ExpandableListAdapter.ViewHolder holder;

            if (convertView == null) {
                convertView = inf.inflate(R.layout.list_group, parent, false);

                holder = new ExpandableListAdapter.ViewHolder();
                holder.text = (TextView) convertView.findViewById(R.id.lblListHeader);
                convertView.setTag(holder);
            } else {
                holder = (ExpandableListAdapter.ViewHolder) convertView.getTag();
            }

            holder.text.setText(getGroup(groupPosition).toString());
            if (getSharedPreferences(Constants.PREFS, MODE_PRIVATE).getBoolean(Constants.DARK_THEME, false))
                holder.text.setTextColor(Color.WHITE);

            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

        private class ViewHolder {
            TextView text;
        }
    }
}
