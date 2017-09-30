package kalei.com.timerapp.views;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by risaki on 9/29/17.
 */

public class CustomNoShowFirstItemAdapter extends ArrayAdapter {
    private Context context;
    private int textViewResourceId;
    private String[] objects;
    public static boolean flag = false;

    public CustomNoShowFirstItemAdapter(Context context, int textViewResourceId,
            String[] objects) {
        super(context, textViewResourceId, objects);
        this.context = context;
        this.textViewResourceId = textViewResourceId;
        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = View.inflate(context, textViewResourceId, null);
        }
        TextView tv = (TextView) convertView;
        if (position != 0) {
            tv = (TextView) convertView;
            tv.setText(objects[position]);
            tv.setTextColor(Color.BLACK);
        } else {
            tv.setTextColor(Color.GRAY);
        }
        return convertView;
    }

    @Override
    public boolean isEnabled(int position) {
        return position != 0;
        // Disable the first item from Spinner
        // First item will be use for hint
    }
}
