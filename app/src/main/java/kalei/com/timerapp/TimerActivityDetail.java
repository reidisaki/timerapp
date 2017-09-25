package kalei.com.timerapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import butterknife.BindView;

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

    @BindView (R.id.titleEditText)
    EditText titleEditText;
    @BindView (R.id.notesEditText)
    EditText notesEditText;
    @BindView (R.id.startDateSpinner)
    Spinner startDateSpinner;
    @BindView (R.id.catgorySpinner)
    Spinner categorySpinner;
    @BindView (R.id.categoryIconImageView)
    ImageView iconImageView;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer_detail);
    }
}
