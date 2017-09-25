package kalei.com.timerapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by risaki on 9/24/17.
 */

public class MainActivity extends TimerBaseActivity {

    @BindView (R.id.fab)
    FloatingActionButton fab;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        fab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View view) {
                Intent i = new Intent(MainActivity.this, TimerActivityDetail.class);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
