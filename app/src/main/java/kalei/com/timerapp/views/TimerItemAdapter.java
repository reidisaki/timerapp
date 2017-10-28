package kalei.com.timerapp.views;

import com.google.gson.Gson;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import kalei.com.timerapp.MainActivity;
import kalei.com.timerapp.PrefManager;
import kalei.com.timerapp.R;
import kalei.com.timerapp.TimeDifference;
import kalei.com.timerapp.TimerActivityDetail;
import kalei.com.timerapp.TimerItem;
import kalei.com.timerapp.views.TimerItemAdapter.TimerViewHolder;

/**
 * Created by risaki on 9/29/17.
 */

public class TimerItemAdapter extends RecyclerView.Adapter<TimerViewHolder> {
    Context mContext;
    ArrayList<TimerItem> items;
    int id;

    public void updateList(ArrayList<TimerItem> list) {
        if (list != null) {
            int totalSize = getItemCount();
            setItems(list);
            notifyItemRangeRemoved(0, totalSize);
            //tell the recycler view how many new items we added
            notifyItemRangeInserted(0, list.size());
        }
    }

    public TimerItemAdapter(Context context) {
        super();
//        mAffiliateAdapterListener = listener;
        mContext = context;
    }

    @Override
    public TimerViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_main_item, parent, false);
        return new TimerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TimerViewHolder holder, final int position) {
        final TimerItem item = getItem(position);

        holder.titleTextView.setText(item.getName());
        holder.dateTextView.setText(TimeDifference.getFormattedStringDate(item.getDate(), new Date()));
        holder.iconImageView.setImageDrawable(ContextCompat.getDrawable(mContext, setIconImage(item)));
        holder.timerImageView.setAlpha(item.isEnabled() && notBirthday(item) ? 1f : .5f);
        holder.itemView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View view) {
                Intent i = new Intent(mContext, TimerActivityDetail.class);

                i.putExtra(TimerActivityDetail.ID_BUNDLE_NAME, item.getId());
                mContext.startActivity(i);
            }
        });
        holder.timerImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View view) {
                handleTimerEnabledClicked(holder, item, position);
            }
        });
    }

    private boolean notBirthday(TimerItem item) {
        return !item.getCategory().toLowerCase().equals("birthday");
    }

    private void handleTimerEnabledClicked(final TimerViewHolder holder, final TimerItem item, int position) {
        if (item.isEnabled() || notBirthday(item)) {
            item.setDate(new Date());
            items.set(position, item);
            holder.dateTextView.setText("0 days");
            PrefManager.setListOfItems(mContext, new Gson().toJson(items));
            holder.timerImageView.setAlpha(.5f);
        }
    }

    public static int setIconImage(final TimerItem item) {
        int drawableId = -1;
        switch (item.getCategory()) {
            case "Birthday":
                drawableId = R.drawable.ic_bday;
                break;
            case "Health":
                drawableId = R.drawable.ic_health;
                break;

            case "House":
                drawableId = R.drawable.ic_house;
                break;

            case "Medical":
                drawableId = R.drawable.ic_med;
                break;

            case "Pet":
                drawableId = R.drawable.ic_pet;
                break;
        }
        return drawableId;
    }

    public ArrayList<TimerItem> getItems() {
        return items;
    }

    class TimerViewHolder extends RecyclerView.ViewHolder {
        @BindView (R.id.icon_image_view)
        public ImageView iconImageView;
        @BindView (R.id.title_text_view)
        TextView titleTextView;
        @BindView (R.id.date_text_view)
        TextView dateTextView;
        @BindView (R.id.timer_image_view)
        ImageView timerImageView;

        public TimerViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    public void setItems(ArrayList<TimerItem> items) {
        this.items = items;
    }

    public TimerItem getItem(int position) {
        if (items == null) {
            items = new ArrayList<>();
        }
        return items.get(position);
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }
}
