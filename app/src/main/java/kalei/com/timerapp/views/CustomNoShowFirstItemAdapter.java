package kalei.com.timerapp.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import kalei.com.timerapp.R;
import kalei.com.timerapp.TimerItem;

/**
 * Created by risaki on 9/29/17.
 */

public class CustomNoShowFirstItemAdapter extends ArrayAdapter<TimerItem> {
    private Context context;
    private TimerItem[] objects;
    private TypedArray images;
    int[] imageIds;
    public static boolean flag = false;

    public CustomNoShowFirstItemAdapter(Context context, int textViewResourceId,
            TimerItem[] objects) {
        super(context, textViewResourceId, objects);
        this.context = context;
        this.objects = objects;
        this.images = context.getResources().obtainTypedArray(R.array.category_drawable_array);
        int count = images.length();
        imageIds = new int[count];
        for (int i = 0; i < imageIds.length; i++) {
            imageIds[i] = images.getResourceId(i, 0);
        }
//Recycles the TypedArray, to be re-used by a later caller.
//After calling this function you must not ever touch the typed array again.
        images.recycle();
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.category_dropdown_item, parent, false);
        TimerItem item = objects[position];

        TextView textView = (TextView) row.findViewById(R.id.category_dropdown_text);
        textView.setText(item.getCategory());

        //off by one for category with no image
        --position;
        if (position >= 0) {
            ImageView imageView = (ImageView) row.findViewById(R.id.category_dropdown_image);
            imageView.setImageResource(imageIds[position]);
        }
        return row;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public boolean isEnabled(int position) {
        return position != 0;
        // Disable the first item from Spinner
        // First item will be use for hint
    }
}
