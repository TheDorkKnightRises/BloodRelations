package smartindia.santas.bloodrelations.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by DELL on 01/04/2017.
 */

public class ConnectionChecker {

    public static boolean isConnected(Context context){
        if(context == null){
            return true;
        }
        else{
            ConnectivityManager manager =(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            return (networkInfo!=null && networkInfo.isConnected());
        }
    }
}
