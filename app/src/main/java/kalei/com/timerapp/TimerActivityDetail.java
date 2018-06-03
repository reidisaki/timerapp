package kalei.com.timerapp;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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

public class TimerActivityDetail extends TimerBaseActivity {

    public static final String ID_BUNDLE_NAME = "bundle_name";
    public static final String EXISTING_ID_BUNDLE_NAME = "exiting_bundle_id";
    private static final String TAG = "TimerActivityDetail";
    TimerItem timerItem = new TimerItem();
    @BindView (R.id.titleEditText)
    EditText titleEditText;
    @BindView (R.id.notesEditText)
    EditText notesEditText;
    @BindView (R.id.startDateSpinner)
    EditText startDateEditText;
    @BindView (R.id.catgorySpinner)
    Spinner categorySpinner;
    @BindView (R.id.categoryIconImageView)
    ImageView iconImageView;
    @BindView (R.id.toolbar)
    Toolbar toolbar;
    @BindView (R.id.dateStringTextView)
    TextView dateStringTextView;
    @BindView (R.id.lock_icon)
    ImageView lockIcon;
    @BindView (R.id.refresh_image_view)
    ImageView refreshImageView;

    TimerItem currentItem = null;
    List<TimerItem> timerItemList = new ArrayList<>();
    String id;
    Context mContext;

    private FirebaseFirestore db = null;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        mContext = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer_detail);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        id = getIntent().getStringExtra(TimerActivityDetail.ID_BUNDLE_NAME);
        Toast.makeText(this, "userId: " + id, Toast.LENGTH_SHORT).show();
        String jsonListString = PrefManager.getListOfItems(this);
        timerItemList = new Gson().fromJson(jsonListString, new TypeToken<ArrayList<TimerItem>>() {
        }.getType());

        if (timerItemList == null) {
            timerItemList = new ArrayList<>();
        }
        for (TimerItem t : timerItemList) {
            if (t.getUserId().equals(id)) {
                currentItem = t;
            }
        }
        toolbar.setTitle("Edit or New");
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.showOverflowMenu();

        toolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View view) {
                onBackPressed();
            }
        });
        final DialogFragment newFragment = new DatePickerFragment();
        startDateEditText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View view) {

                newFragment.show(getFragmentManager(), "datePicker");
                newFragment.getFragmentManager().executePendingTransactions();

                final DatePickerDialog datePickerDialog = (DatePickerDialog) newFragment.getDialog();

//                final DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, 0);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.setTitle(getString(R.string.set_date));
                if (startDateEditText.getText().length() > 0) {
                    Calendar c = Calendar.getInstance();
                    c.setTime(new Date(startDateEditText.getText().toString()));
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
                                startDateEditText.setText(String.format("%s/%s/%s", d.getMonth() + 1, d.getDayOfMonth(), d.getYear()));
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

                setupUI();
            }
        });

        final CustomNoShowFirstItemAdapter adapter = new CustomNoShowFirstItemAdapter(this,              // Use our custom adapter
                android.R.layout.simple_spinner_item, getDefaultTimerItems());//timerItemList.toArray(new TimerItem[timerItemList.size()]));
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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

        if (currentItem != null && currentItem.getUserId() != null) {
            setExistingValues();
        }
    }

    private void setupUI() {
        if (!timerItem.isEnabled()) {
            refreshImageView.setAlpha(.5f);
        }
    }

    @OnClick (R.id.refresh_image_view)
    public void refreshClicked() {

        if (currentItem == null) {
            timerItem.setDateCreated(new Date());
            currentItem = timerItem;
        } else {
            currentItem.setDateCreated(new Date());
        }
        startDateEditText.setText(new SimpleDateFormat("MM/dd/yyyy").format(currentItem.getDateCreated()));
        dateStringTextView.setText(calculateDateDifferenceStringFormat());
    }

    @OnClick ({R.id.lock_icon})
    public void lockIconClicked(ImageView lockIconImage) {
        if (lockIconImage.getDrawable().getConstantState() == getDrawable(R.drawable.ic_lock).getConstantState()) {
            lockIconImage.setImageDrawable(getDrawable(R.drawable.ic_unlock));
            startDateEditText.setEnabled(true);
        } else {
            lockIconImage.setImageDrawable(getDrawable(R.drawable.ic_lock));
            startDateEditText.setEnabled(false);
        }
    }

    public TimerItem[] getDefaultTimerItems() {
        String[] categories = getResources().getStringArray(R.array.category_string_array);
        TimerItem[] items = new TimerItem[categories.length];
        for (int i = 0; i < categories.length; i++) {
            items[i] = new TimerItem(categories[i]);
        }
        return items;
    }

    private void setExistingValues() {
        notesEditText.setText(currentItem.getNote());
        titleEditText.setText(currentItem.getName());
        startDateEditText.setText(new SimpleDateFormat("MM/dd/yyyy").format(currentItem.getDateCreated()));
        categorySpinner.setSelection(getSelectedPositionCategory());
        iconImageView.setImageDrawable(ContextCompat.getDrawable(this, TimerItemAdapter.setIconImage(currentItem)));

        dateStringTextView.setText(calculateDateDifferenceStringFormat());
        lockIcon.setImageDrawable(getDrawable(currentItem.isEnabled ? R.drawable.ic_unlock : R.drawable.ic_lock));
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
        return getFormattedStringDate(currentItem.getDateCreated(), new Date());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        MenuItem saveMenuItem = menu.getItem(0);
        MenuItem deleteMenuItem = menu.getItem(1);
        deleteMenuItem.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final MenuItem menuItem) {

                if (id != null) {
                    Iterator<TimerItem> iter = timerItemList.iterator();

                    while (iter.hasNext()) {
                        TimerItem item = iter.next();

                        if (item.getUserId() == id) {
                            timerItemList.remove(item);
                        }
                    }

                    PrefManager.setListOfItems(mContext, new Gson().toJson(timerItemList));
                    finish();
                }

                return false;
            }
        });

        saveMenuItem.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final MenuItem menuItem) {
                handleSaveItem();
                return false;
            }
        });
        return true;
    }

    private void setup() {
        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);
    }

    public void updateDocument(TimerItem timerItem) {
        // [START update_document]
        DocumentReference ref = db.collection(getString(R.string.fb_items)).document(String.valueOf(timerItem.getUserId()));

//todo: you might need to update each field manually to the new data..
//not just set the item to be something else like this

        /*  this will update the entire item completely
        db.collection("cities").document("LA")
        .set(city)
        .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "DocumentSnapshot successfully written!");
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error writing document", e);
            }
        });
         */
        ref
                .update(String.valueOf(timerItem.getUserId()), new Gson().toJson(timerItem))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
//                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
//                        Log.w(TAG, "Error updating document", e);
                    }
                });
        // [END update_document]
    }

    public void deleteDocument(int iD) {
        // [START delete_document]
        db.collection("items").document(String.valueOf(iD))
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
//                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
//                        Log.w(TAG, "Error deleting document", e);
                    }
                });
        // [END delete_document]
    }

    private void addItem(TimerItem timerItem) {
        Map<String, Object> item = new HashMap<>();
        hydrateTimerItem();
        String timerItemString = new Gson().toJson(timerItem);
        item.put("userId", String.valueOf(timerItem.getUserId()));
        item.put("category", timerItem.getCategory());
        item.put("dateCreated", timerItem.getDateCreated().toString());
        item.put("type", timerItem.getType());
        item.put("dateString", timerItem.getDateString());
        item.put("name", timerItem.getName());
        item.put("note", timerItem.getNote());
        item.put("isEnabled", timerItem.isEnabled());

        // Add a new document with a generated ID
//        DocumentReference users = db.collection("users")
//                .add(item)
//                .addOnSuccessListener(documentReference -> Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getUserId()))
//                .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e))
//                .addOnCanceledListener(() -> Log.w(TAG, "Error adding document"))
//                .addOnCompleteListener(task -> Log.w(TAG, "Error adding document")).getResult();

        Task<DocumentReference> users = db.collection(getString(R.string.fb_users))
                .add(item)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(final DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull final Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                })
                .addOnCanceledListener(new OnCanceledListener() {
                    @Override
                    public void onCanceled() {
                        Log.w(TAG, "Error adding document");
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull final Task<DocumentReference> task) {
                        Log.w(TAG, "Error adding document");
                    }
                });

        //todo:  research how firebase handles airplane mode or when you have no internet
//        try {
//            DocumentReference result = users.getResult();
//        } catch (IllegalStateException e) {
//            //hack for airplane mode
//            saveItemLocal();
//        }
    }

    private void saveItemLocal() {
        if (startDateEditText.getText().length() > 0) {
            hydrateTimerItem();

            //existing item
            if (id != null) {

                Iterator<TimerItem> iter = timerItemList.iterator();

                while (iter.hasNext()) {
                    TimerItem item = iter.next();

                    if (item.getUserId().equals(id)) {
                        iter.remove();
                        timerItem.setUserId(id);
                    }
                }
            } else {
                timerItemList.add(timerItem);
            }
            PrefManager.setListOfItems(this, new Gson().toJson(timerItemList));
        }
    }

    private void hydrateTimerItem() {
        timerItem.setUserId(FirebaseAuth.getInstance().getCurrentUser() != null ? FirebaseAuth.getInstance().getCurrentUser().getUid() :
                String.valueOf(timerItemList.size() == 0 ? 1 : timerItemList.get(timerItemList.size() - 1).getUserId() + 1));
        timerItem.setName(titleEditText.getText().toString());
        timerItem.setCategory(((TimerItem) categorySpinner.getSelectedItem()).getCategory());
        timerItem.setDateString(startDateEditText.getText().toString());
        timerItem.setNote(notesEditText.getText().toString());
        timerItem.setDateCreated(new Date());
        timerItem.setEnabled(isEnabled());
    }

    private void handleSaveItem() {

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            setup();
            if (id != null) {
                updateDocument(timerItem);
            } else {
                addItem(timerItem);
            }
            //depending on what the user did edit/add/delete item
            timerItemList.add(timerItem);
        } else {
            saveItemLocal();
        }
        finish();
    }

//    @Override
//    public void onDateSet(final DatePicker datePicker, final int year, final int month, final int day) {
//        startDateEditText.setText(String.format("%s/%s/%s", month + 1, day, year));
//    }

    public boolean isEnabled() {
        return lockIcon.getDrawable().getConstantState() != getDrawable(R.drawable.ic_lock).getConstantState();
    }

    /**
     * Create a DatePickerFragment class that extends DialogFragment.
     * Define the onCreateDialog() method to return an instance of DatePickerDialog
     */
    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog
                    = new DatePickerDialog(getActivity(), android.R.style.Theme_Holo_Dialog, this, year, month, day);

            datePickerDialog.getDatePicker().setCalendarViewShown(false);
            return datePickerDialog;
        }

        @Override
        public void onDateSet(final DatePicker datePicker, final int i, final int i1, final int i2) {

        }
    }
}
