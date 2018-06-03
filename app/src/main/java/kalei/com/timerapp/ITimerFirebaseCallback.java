package kalei.com.timerapp;

import java.util.List;

/**
 * Created by risaki on 6/1/18.
 */

interface ITimerFirebaseCallback {
    public void onGetAllItems(List<TimerItem> items);
}
