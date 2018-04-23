package kalei.com.timerapp;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import com.crashlytics.android.Crashlytics;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import io.fabric.sdk.android.Fabric;

public class SplashActivity extends TimerBaseActivity {
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if (!BuildConfig.DEBUG) {
            Fabric.with(this, new Crashlytics());
        } else {

        }
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial));
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                mInterstitialAd.show();
            }

            @Override
            public void onAdClosed() {
                startActivity();
            }
        });
    }

    public static boolean hasInternet(Context context) {

        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private void startActivity() {
        Intent i = new Intent(SplashActivity.this, LoginActivity.class);
        startActivity(i);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!hasInternet(this) || BuildConfig.DEBUG) {
            startActivity();
        } else {
            requestNewInterstitial();
        }
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
//                .addTestDevice("SEE_YOUR_LOGCAT_TO_GET_YOUR_DEVICE_ID").addTestDevice("F20E572DBF816C7D7BA7BAA63AF73CCC")
                .build();

        mInterstitialAd.loadAd(adRequest);
    }
}
