package kalei.com.timerapp;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import kalei.com.timerapp.views.CustomNoShowFirstItemAdapter;
import kalei.com.timerapp.views.TimerItemAdapter;

import static kalei.com.timerapp.TimeDifference.getFormattedStringDate;

/**
 * Created by risaki on 9/24/17.
 * <p>
 * example
 *
 * @BindView(R.id.pass) EditText password;
 * @BindString(R.string.login_error) String loginErrorMessage;
 * @OnClick(R.id.submit) void submit() { // TODO call server... }
 * @Override public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); setContentView(R.layout.simple_activity);
 * ButterKnife.bind(this); // TODO Use fields... }
 */

public class TimerActivityDetail extends TimerBaseActivity implements DatePickerDialog.OnDateSetListener {

    public static final String ID_BUNDLE_NAME = "bundle_name";
    public static final String EXISTING_ID_BUNDLE_NAME = "exiting_bundle_id";
    TimerItem timerItem = new TimerItem();
    @BindView (R.id.titleEditText)
    EditText titleEditText;
    @BindView (R.id.notesEditText)
    EditText notesEditText;
    @BindView (R.id.startDateSpinner)
    EditText startDateChooser;
    @BindView (R.id.catgorySpinner)
    Spinner categorySpinner;
    @BindView (R.id.categoryIconImageView)
    ImageView iconImageView;
    @BindView (R.id.toolbar)
    Toolbar toolbar;
    @BindView (R.id.dateStringTextView)
    TextView dateStringTextView;

    TimerItem currentItem = null;
    List<TimerItem> timerItemList = new ArrayList<>();
    int id;
    Context mContext;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        mContext = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer_detail);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        id = getIntent().getIntExtra(TimerActivityDetail.ID_BUNDLE_NAME, 0);
        String jsonListString = PrefManager.getListOfItems(this);
        timerItemList = new Gson().fromJson(jsonListString, new TypeToken<ArrayList<TimerItem>>() {
        }.getType());

        if (timerItemList == null) {
            timerItemList = new ArrayList<>();
        }
        for (TimerItem t : timerItemList) {
            if (t.getId() == id) {
                currentItem = t;
            }
        }
        toolbar.setTitle("Edit or New");
        toolbar.setNavigationIcon(R.drawable.quantum_ic_play_arrow_grey600_36);
        toolbar.showOverflowMenu();

        toolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View view) {
                onBackPressed();
            }
        });

        startDateChooser.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View view) {

                final DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, android.R.style.Theme_Holo_Dialog);
                if (startDateChooser.getText().length() > 0) {
                    Calendar c = Calendar.getInstance();
                    c.setTime(new Date(startDateChooser.getText().toString()));
                    int year = c.get(Calendar.YEAR);
                    int month = c.get(Calendar.MONTH);
                    int day = c.get(Calendar.DAY_OF_MONTH);
                    datePickerDialog.getDatePicker().updateDate(year, month, day);
                }
                datePickerDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "clear", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialogInterface, final int which) {
                        Calendar c = Calendar.getInstance();
                        int year = Calendar.getInstance().get(Calendar.YEAR);
                        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
                        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

                        datePickerDialog.getDatePicker().updateDate(year, month, day);
                    }
                });
                datePickerDialog.setButton(DialogInterface.BUTTON_POSITIVE,
                        "set", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DatePicker d = datePickerDialog.getDatePicker();
                                startDateChooser.setText(String.format("%s/%s/%s", d.getMonth() + 1, d.getDayOfMonth(), d.getYear()));
                            }
                        });
                datePickerDialog.setButton(android.content.DialogInterface.BUTTON_NEGATIVE,
                        "cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // todo on click
                            }
                        });
                datePickerDialog.show();
//                datePicker.setVisibility(View.VISIBLE);
//                DialogFragment picker = new DatePickerFragment();
//
//                picker.show(getSupportFragmentManager(), "datePicker");
            }
        });

        final CustomNoShowFirstItemAdapter adapter = new CustomNoShowFirstItemAdapter(this,              // Use our custom adapter
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.category_string_array));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);
        categorySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                    int pos, long id) {
                if (pos != 0) {
                    iconImageView.setVisibility(View.VISIBLE);
                    switch (pos) {
                        case 1:
                            iconImageView.setImageResource(R.drawable.ic_bday);
                            break;
                        case 2:
                            iconImageView.setImageResource(R.drawable.ic_health);
                            break;
                        case 3:
                            iconImageView.setImageResource(R.drawable.ic_house);
                            break;
                        case 4:
                            iconImageView.setImageResource(R.drawable.ic_med);
                            break;
                        case 5:
                            iconImageView.setImageResource(R.drawable.ic_pet);
                            break;
                    }
                } else {
                    iconImageView.setVisibility(View.INVISIBLE);
                }
            }
        });

        if (currentItem != null && currentItem.getId() > 0) {
            setExistingValues();
        }
    }

    private void setExistingValues() {
        notesEditText.setText(currentItem.getNote());
        titleEditText.setText(currentItem.getName());
        startDateChooser.setText(currentItem.getDateString());
        categorySpinner.setSelection(getSelectedPositionCategory());
        iconImageView.setImageDrawable(ContextCompat.getDrawable(this, TimerItemAdapter.setIconImage(currentItem)));
        dateStringTextView.setText(calculateDateDifferenceStringFormat());
    }

    private int getSelectedPositionCategory() {
        int returnValue = 0;
        switch (currentItem.getCategory()) {
            case "Birthday":
                returnValue = 1;
                break;
            case "Health":
                returnValue = 2;
                break;

            case "House":
                returnValue = 3;
                break;

            case "Medical":
                returnValue = 4;
                break;

            case "Pet":
                returnValue = 5;
                break;
        }
        return returnValue;
    }

    private String calculateDateDifferenceStringFormat() {
        return getFormattedStringDate(currentItem.getDate(), new Date());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        MenuItem saveMenuItem = menu.getItem(0);
        saveMenuItem.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final MenuItem menuItem) {
                handleSaveItem();
                return false;
            }
        });
        return true;
    }

    private void handleSaveItem() {

        timerItem.setName(titleEditText.getText().toString());
        timerItem.setCategory(categorySpinner.getSelectedItem().toString());
        timerItem.setDateString(startDateChooser.getText().toString());
        timerItem.setNote(notesEditText.getText().toString());
        timerItem.setDate(new Date(startDateChooser.getText().toString()));

        //existing item
        if (id > 0) {

            Iterator<TimerItem> iter = timerItemList.iterator();

            while (iter.hasNext()) {
                TimerItem item = iter.next();

                if (item.getId() == id) {
                    iter.remove();
                    timerItem.setId(id);
                }
            }
        } else {
            timerItem.setId(timerItemList.size() == 0 ? 1 : timerItemList.get(timerItemList.size() - 1).getId() + 1);
        }

        timerItemList.add(timerItem);

        PrefManager.setListOfItems(this, new Gson().toJson(timerItemList));
        finish();
    }

    @Override
    public void onDateSet(final DatePicker datePicker, final int year, final int month, final int day) {
        startDateChooser.setText(String.format("%s/%s/%s", month + 1, day, year));
    }

    /**
     * Create a DatePickerFragment class that extends DialogFragment.
     * Define the onCreateDialog() method to return an instance of DatePickerDialog
     */
    public static class DatePickerFragment extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog picker = new DatePickerDialog(getActivity(),
                    (DatePickerDialog.OnDateSetListener)
                            getActivity(), year, month, day);
            picker.getDatePicker().setCalendarViewShown(false);
            return picker;
        }
    }
}
