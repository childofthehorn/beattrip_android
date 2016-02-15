package openbrite.com.beattrip;

import android.app.Activity;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.media.MediaRecorder;
import android.content.DialogInterface;
import android.widget.*;
import android.widget.ImageButton;
import android.content.Intent;

import java.text.SimpleDateFormat;
import java.util.Date;


public class GeneralFragmentActivity extends Activity {
    public static MediaRecorder mRecord;
    protected static Context ctx;
    public static View.OnClickListener action_toggleRecord;
    protected static final int QBH_REQUEST_CODE = 0;
    protected static final int RESULT_NOSONGS = 1;
    Integer amp;
    // Timer t can be used to help show amplitude over time if desired
    //Timer t;
    ProgressDialog progressDlg;
    protected static ProgressDialog dlgStopRecording;
    //public static Context context;
    final Context context = this;
    private static final int SELECT_PROFILE = 43;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ctx = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_fragment);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new GenFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class GenFragment extends Fragment {

        public GenFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.home_layout, container, false);
            return rootView;
        }
    }

    // Handle some app lifecycle stuff here
    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onPause() {
        super.onPause();
    }

}
