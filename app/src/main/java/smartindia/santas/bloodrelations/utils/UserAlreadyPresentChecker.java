package smartindia.santas.bloodrelations.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by DELL on 02/04/2017.
 */

public class UserAlreadyPresentChecker {

    public static String UID;
    public static boolean check ;
    public static ProgressDialog dialog;

    public static boolean check(Context context, String UID){
        dialog = new ProgressDialog(context);
        dialog.show();
        new MyAsyncTask().execute();
        return check;
    }

    static class MyAsyncTask extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            DatabaseReference root = FirebaseDatabase.getInstance().getReference();
            ValueEventListener listener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    check = dataSnapshot.hasChild(UID);
                    dialog.dismiss();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            root.addValueEventListener(listener);
            return null;
        }

    }
}
