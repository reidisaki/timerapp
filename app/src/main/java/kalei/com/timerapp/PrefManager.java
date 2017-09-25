package kalei.com.timerapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

/**
 * Created by jwong on 02/04/15.
 */
public class PrefManager {

    private final static String SHARED_PREFERENCES_VERSION = "shared_preferences_version";
    public final static String DEVICE_ID = "device_id";

    private static SharedPreferences getPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    private static SharedPreferences.Editor getEditor(Context context) {
        return getPreferences(context).edit();
    }

    /**
     * Helper method to save a boolean to preferences
     */
    private static void putBoolean(Context context, @NonNull String key, boolean value) {
        getEditor(context).putBoolean(key, value).commit();
    }

    /**
     * Helper method to get a boolean value from preferences
     */
    private static boolean getBoolean(Context context, @NonNull String key, boolean defValue) {
        return getPreferences(context).getBoolean(key, defValue);
    }

    /**
     * Helper method to save a boolean to preferences
     */
    private static void putString(Context context, @NonNull String key, String value) {
        getEditor(context).putString(key, value).commit();
    }

    /**
     * Helper method to get a boolean value from preferences
     */
    private static String getString(Context context, @NonNull String key, String defValue) {
        return getPreferences(context).getString(key, defValue);
    }

    /**
     * Initializes shared preferences
     */
    public static void initialize(Context context) {
        // If the version stored on disk is different than what's in build.gradle then the version has changed
        // in which case force the shared preferences data to clear.  The user will lose saved data but
        // at least the application won't crash as a result of JSON parsing errors
        if (!getSharedPreferencesVersion(context).equals(BuildConfig.SHARED_PREFERENCES_VERSION)) {
            clear(context);
        }
    }

    /**
     * Clears all data from preferences
     */
    public static void clear(Context context) {
        getEditor(context).clear().commit();
    }

    /**
     * Gets the current version of data stored in preferences
     */
    public static String getSharedPreferencesVersion(Context context) {
        return getString(context, SHARED_PREFERENCES_VERSION, BuildConfig.SHARED_PREFERENCES_VERSION);
    }

    /**
     * Sets the current version of data stored in preferences
     */
    public static void setSharedPreferencesVersion(Context context, String value) {
        putString(context, SHARED_PREFERENCES_VERSION, value);
    }

    public static String getDeviceId(Context context) {
        return getString(context, DEVICE_ID, null);
    }

    public static void setDeviceId(Context context, String deviceId) {
        putString(context, DEVICE_ID, deviceId);
    }
}