package barqsoft.footballscores;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by alecmedina on 1/8/16.
 */
public class MyApplication extends Application {
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}
