package kalei.com.timerapp;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApiNotAvailableException;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by risaki on 4/27/18.
 */

public class FireBaseHelper {
    private static FirebaseFirestore db;

    private Context mContext = FirebaseApp.getInstance().getApplicationContext();
    private String email;
    private String TAG = "FireBaseHelper1";
    private static FireBaseHelper instance;

    public FireBaseHelper() {
        if (db == null) {
            setup();
        }
    }

    public static synchronized FireBaseHelper getInstance() {
        if (instance == null) {
            instance = new FireBaseHelper();
        }
        return instance;
    }

    //save one item
    public void SaveOrUpdateItem(TimerItem item, boolean isUpdate) {

        Map<String, Object> timerItem = new HashMap<>();
        timerItem.put("userId", new Gson().toJson(item));
        if (isUpdate) {
            db.collection(mContext.getString(R.string.fb_users)).document(String.valueOf(item.getUserId()))
                    .set(timerItem)
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
        } else {
            //add item new
            db.collection(mContext.getString(R.string.fb_users))
                    .add(timerItem)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error adding document", e);
                        }
                    });
        }
    }

    /*
    //for getting data for a user
     public void simpleQueries() {
        // [START simple_queries]
        // Create a reference to the cities collection
        CollectionReference citiesRef = db.collection("cities");

        // Create a query against the collection.
        Query query = citiesRef.whereEqualTo("state", "CA");
        // [END simple_queries]

        // [START simple_query_capital]
        Query capitalCities = db.collection("cities").whereEqualTo("capital", true);
        // [END simple_query_capital]

        // [START example_filters]
        citiesRef.whereEqualTo("state", "CA");
        citiesRef.whereLessThan("population", 100000);
        citiesRef.whereGreaterThanOrEqualTo("name", "San Francisco");
        // [END example_filters]
    }
     */

    public void syncWithCloud() {
        //get list of items from local cache
        List<TimerItem> items = getLocalItems();

        // push each item up to the cloud
        if (items != null && items.size() > 0) {
            addItemToCloud(items);
        }

        //delete local cache
        deleteLocalItems();
    }

    public List<TimerItem> getLocalItems() {
        String jsonListString = PrefManager.getListOfItems(mContext);
        return new Gson().fromJson(jsonListString, new TypeToken<List<TimerItem>>() {
        }.getType());
    }

    public void deleteLocalItems() {
        PrefManager.clear(mContext);
    }

    public boolean isAuthenticated() {
        return FirebaseAuth.getInstance().getCurrentUser() != null;
    }

    public void addItemToCloud(List<TimerItem> timerItems) {

        ArrayList<Map<String, String>> listMap = new ArrayList<>();
        for (TimerItem timerItem : timerItems) {
            Map<String, String> item = new HashMap<>();
            String timerItemString = new Gson().toJson(timerItem);
            try {
                item.put("userId", FirebaseApp.getInstance().getUid());
            } catch (FirebaseApiNotAvailableException e) {
                e.printStackTrace();
            }
            item.put("category", timerItem.getCategory());
//            item.put("dateCreated", timerItem.getDateCreated().toString());
            item.put("type", timerItem.getType());
            item.put("dateString", timerItem.getDateString());
            item.put("name", timerItem.getName());
            item.put("note", timerItem.getNote());
            item.put("isEnabled", String.valueOf(timerItem.isEnabled()));

            Task<DocumentReference> users = db.collection(mContext.getString(R.string.fb_users))
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
        }
    }

    public void setup() {
        // [START get_firestore_instance]
        db = FirebaseFirestore.getInstance();
        // [END get_firestore_instance]

        // [START set_firestore_settings]
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);
        // [END set_firestore_settings]

        //collection of timer items are tied to this user
//        email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
    }

    public void getAllItems(final List<TimerItem> itemList, final ITimerFirebaseCallback callback) throws FirebaseApiNotAvailableException {
        db.collection(mContext.getString(R.string.fb_users))
                .whereEqualTo("userId", FirebaseApp.getInstance().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                JSONObject json = new JSONObject((document.getData()));
                                Gson g = new GsonBuilder().setDateFormat("EEE MMM dd k:mm:ss z yyyy").create();
                                TimerItem item = g.fromJson(json.toString(), TimerItem.class);

                                itemList.add(item);
                            }
                            callback.onGetAllItems(itemList);
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }
}
