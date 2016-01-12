package barqsoft.footballscores;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.text.SimpleDateFormat;
import java.util.Date;

import barqsoft.footballscores.service.myFetchService;

/**
 * Created by alecmedina on 1/6/16.
 */
public class ScoresWidgetService extends RemoteViewsService {

    //Service that returns a remoteViewFactory
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewsFactory(this.getApplicationContext(), intent);
    }

    //Factory that is used to get relevant data and populate remote view
    public class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

        private Context context;
        private int widgetId;
        private Cursor cursor;
        public static final int COL_HOME = 3;
        public static final int COL_AWAY = 4;
        public static final int COL_HOME_GOALS = 6;
        public static final int COL_AWAY_GOALS = 7;
        public static final int COL_DATE = 1;

        public ListRemoteViewsFactory(Context context, Intent intent) {
            this.context = context;
            widgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        @Override
        public void onCreate() {
            cursor = queryDatabase();
        }

        @Override
        public void onDataSetChanged() {
            update_scores();
            cursor = queryDatabase();
        }

        @Override
        public void onDestroy() {
        }

        @Override
        public int getCount() {
            if (cursor == null) {
                return 0;
            }
            return cursor.getCount();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_item);
            cursor.moveToPosition(position);
            remoteViews.setTextViewText(R.id.home_name_widget, cursor.getString(COL_HOME));
            remoteViews.setTextViewText(R.id.away_name_widget, cursor.getString(COL_AWAY));
            remoteViews.setTextViewText(R.id.data_textview_widget, cursor.getString(COL_DATE));
            remoteViews.setTextViewText(R.id.score_textview_widget,
                    Utilies.getScores(cursor.getInt(COL_HOME_GOALS), cursor.getInt(COL_AWAY_GOALS)));
            remoteViews.setImageViewResource(R.id.home_crest_widget, Utilies.getTeamCrestByTeamName(
                    cursor.getString(COL_HOME)));
            remoteViews.setImageViewResource(R.id.away_crest_widget, Utilies.getTeamCrestByTeamName(
                    cursor.getString(COL_AWAY)));
            return remoteViews;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }

    private void update_scores() {
        Intent service_start = new Intent(this, myFetchService.class);
        this.startService(service_start);
    }

    private Cursor queryDatabase(){
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat mformat = new SimpleDateFormat("yyyy-MM-dd");
        String formatDate = mformat.format(date);
        Cursor cursor = getContentResolver().query(DatabaseContract.scores_table.buildScoreWithDate(),
                null, null, new String[] { formatDate }, null);
        return cursor;
    }
}
