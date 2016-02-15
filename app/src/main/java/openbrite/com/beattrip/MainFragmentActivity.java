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


public class MainFragmentActivity extends Activity {
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
                    .add(R.id.container, new PlaceholderFragment())
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
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            //New Project Button
            Button newProjButton = (Button) rootView.findViewById(R.id.newButton);
            newProjButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Start New Fragment Activity
                    Intent intent = new Intent(getActivity(),GeneralFragmentActivity.class);
                    startActivity(intent);
                }
            });


            // Open Existing Project
            Button openProjButton = (Button) rootView.findViewById(R.id.openButton);
            openProjButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    //Start New Fragment Activity
                        Intent intent = new Intent(getActivity(),FileSelect.class);
                        intent.putExtra(FileSelect.NO_INLINE_SELECTION, true);
                        intent.putExtra(FileSelect.WINDOW_TITLE, R.string.import_configuration_file);
                        startActivityForResult(intent, SELECT_PROFILE);
                    }

            });

            // Onclick listener for the Record button

            ImageButton record = (ImageButton) rootView.findViewById(R.id.recordButton);
            record.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Initialize the recording equipment
                    mRecord = new MediaRecorder();
                    mRecord.setAudioSource(MediaRecorder.AudioSource.MIC);
                    mRecord.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                    mRecord.setAudioEncoder(1);		// 2 = AMR_WB codec (MediaRecorder.AudioEncoder)
                    mRecord.setAudioChannels(1);
                    mRecord.setAudioSamplingRate(8000);
                    mRecord.setAudioEncodingBitRate(16);
                    Date now = new Date();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-hhmmss");
                    mRecord.setOutputFile(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Beat_" + dateFormat.format(now) + ".mp4");
                    Log.d("BeatTrip", "File is saved to " + Environment.getExternalStorageDirectory().getAbsolutePath());
                    // Tell the user what's going on
                    dlgStopRecording = new ProgressDialog(ctx);
                    dlgStopRecording.setTitle("Recording...");
                    dlgStopRecording.setCancelable(true);
                    dlgStopRecording.setMessage("Recording now.  Watch the yellow VU graph below to monitor your volume level.  Press \"Finished\" to Stop, or the Back button to cancel.");
                    dlgStopRecording.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    dlgStopRecording.setProgress(0);
                    dlgStopRecording.setMax(32767);
                    // Now set the action for when the user is finished recording: first, using the Finished button
                    dlgStopRecording.setButton(ProgressDialog.BUTTON_POSITIVE, "Finished", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            // the user has now instructed the device to stop recording; save the sample
                            stopRecording();
                        }
                    });
                    // Now set the action for when the user hits the back button -- dismiss the progress dialog
                    dlgStopRecording.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            dlgStopRecording.dismiss();
                            //t.cancel();
                            mRecord.stop();
                            mRecord.reset();
                            mRecord.release();
                            mRecord = null;
                        }
                    });
                    // Now start the recording equipment
                    startRecording();
                }
            });

            return rootView;
        }
    }

    // Handle some app lifecycle stuff here
    @Override
    public void onDestroy() {
        if (mRecord != null) {
            mRecord.release();
            mRecord = null;
        }
        super.onDestroy();
    }


    @Override
    public void onPause() {
        if (mRecord != null) {
            mRecord.release();
            mRecord = null;
        }
        super.onPause();
    }

    // Function that consolidates "start recording" activities
    public static void startRecording(){
        // Start a new timer that fires every 100 ms to sample the amplitude coming into the mic
        try {
            // timer "t" takes occasional samples to provide a VU meter; engage it
            //t = new Timer("sampleVU");
            //t.schedule(new tt(), 100, 100);
            mRecord.prepare();
            mRecord.start();
            dlgStopRecording.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Function that consolidates "stop recording" activities
    public static void stopRecording() {
        // timer "t" has been updating VU meter; release it
        //t.cancel();
        mRecord.stop();
        mRecord.reset();
        mRecord.release();
        mRecord = null;
    }
}
