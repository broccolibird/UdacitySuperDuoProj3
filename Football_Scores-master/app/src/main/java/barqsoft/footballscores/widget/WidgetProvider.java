package barqsoft.footballscores.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.widget.RemoteViews;

import java.text.SimpleDateFormat;
import java.util.Date;

import barqsoft.footballscores.DatabaseContract;
import barqsoft.footballscores.R;
import barqsoft.footballscores.Utilies;
import barqsoft.footballscores.scoresAdapter;

/**
 * Created by Kat on 2/15/16.
 */
public class WidgetProvider extends AppWidgetProvider {
    private static final String TAG = "WidgetProvider";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        final int N = appWidgetIds.length;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String[] selectionArgs = new String[]{sdf.format(new Date())};
        Cursor cursor = context.getContentResolver().query(
                DatabaseContract.scores_table.buildScoreWithDate(),  // The content URI of the words table
                null,                       // The columns to return for each row
                null,                   // Either null, or the word the user entered
                selectionArgs,                    // Either empty, or the string the user entered
                null);

        for (int i = 0; i < N; i++) {

            int appWidgetId = appWidgetIds[i];

            Log.d(TAG, "widget id: " + appWidgetId);

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);

            if (cursor.moveToFirst()) {
                views.setTextViewText(R.id.home_name, cursor.getString(scoresAdapter.COL_HOME));
                views.setTextViewText(R.id.away_name, cursor.getString(scoresAdapter.COL_AWAY));
                views.setTextViewText(R.id.score_textview, Utilies.getScores(cursor.getInt(scoresAdapter.COL_HOME_GOALS), cursor.getInt(scoresAdapter.COL_AWAY_GOALS)));
                views.setTextViewText(R.id.data_textview, cursor.getString(scoresAdapter.COL_MATCHTIME));
                views.setImageViewResource(R.id.home_crest, Utilies.getTeamCrestByTeamName(
                        cursor.getString(scoresAdapter.COL_HOME)));
                views.setImageViewResource(R.id.away_crest, Utilies.getTeamCrestByTeamName(
                        cursor.getString(scoresAdapter.COL_AWAY)));
            } else {
                views.setTextViewText(R.id.home_name, "");
                views.setTextViewText(R.id.away_name, "");
                views.setTextViewText(R.id.score_textview, "");
                views.setTextViewText(R.id.data_textview, "No game today");
                views.setImageViewBitmap(R.id.home_crest, null);
                views.setImageViewBitmap(R.id.away_crest, null);
            }

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }

    }
}
