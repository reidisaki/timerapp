package kalei.com.timerapp;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

/**
 * Created by risaki on 9/24/17.
 */

public class TimerApplication extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
