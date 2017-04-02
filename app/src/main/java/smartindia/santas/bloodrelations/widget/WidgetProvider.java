package smartindia.santas.bloodrelations.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import smartindia.santas.bloodrelations.Constants;
import smartindia.santas.bloodrelations.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by samri_000 on 4/2/2017.
 */

public class WidgetProvider extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d("WIDGET", "onUpdate(): ");
        for (int appWidgetId : appWidgetIds) {

            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:"+context.getSharedPreferences(Constants.PREFS, MODE_PRIVATE).getString(Constants.CONTACT, "18001801104")));
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, callIntent, 0);

            // Get the layout for the App Widget and attach an on-click listener to the button
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
            views.setOnClickPendingIntent(R.id.callButton, pendingIntent);

            // Tell the AppWidgetManager to perform an update on the current App Widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.e("WIDGET", "onReceive");
    }
}

