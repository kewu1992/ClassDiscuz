package cmu.banana.classdiscuz.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import cmu.banana.classdiscuz.R;
import cmu.banana.classdiscuz.entities.Course;
import cmu.banana.classdiscuz.entities.Session;
import cmu.banana.classdiscuz.exception.DatabaseException;
import cmu.banana.classdiscuz.util.ParseTime;
import cmu.banana.classdiscuz.ws.remote.BackendConnector;

/**
 * Created by WK on 11/6/15.
 */
public class FocusActivity extends AppCompatActivity {
    private TimeCount time;
    TextView textView_time;
    long time_minute;
    int week;/*sunday 1 monday 2 ... saturday 7*/
    ArrayList<Course> coursesList = new ArrayList<Course>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_focus);



        textView_time = (TextView)findViewById(R.id.textView_time);

        Calendar rightNow = Calendar.getInstance();
        time_minute = rightNow.get(Calendar.HOUR_OF_DAY)*60 + rightNow.get(Calendar.MINUTE);
        week = rightNow.get(Calendar.DAY_OF_WEEK);
        /*set the timer and start count*/
//        time = new TimeCount(60000, 1000);
//        time.start();
        new RefreshCourses().execute((Object) null);
    }


    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }
        @Override
        public void onFinish() {//counter ok.
            new UpdateFocusPoint().execute(1);

 //           time_view.setClickable(true);
        }
        @Override
        public void onTick(long millisUntilFinished){//计时过程显示
   //         time_view.setClickable(false);
            String time = Objects.toString(millisUntilFinished / 1000 / 60 / 60) +
                    ":" + Objects.toString(millisUntilFinished % (1000 * 60 * 60) / 1000 / 60) +
                    ":" + Objects.toString(millisUntilFinished % (1000 * 60) / 1000);
            textView_time.setText(time);

        }
    }

    private class RefreshCourses extends AsyncTask<Object, Object, ArrayList<Course>> {
        @Override
        protected ArrayList<Course> doInBackground(Object... arg){
            try{
                return Session.get(getApplicationContext()).getUser().getRegisteredCourses();
            } catch (DatabaseException e){
                cancel(true);
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Course> courses){
            Course course;
            coursesList = courses;
            String course_time;
            boolean isClassTime = false;
            ParseTime parsetime = new ParseTime();
            for (int i = 0; i < coursesList.size(); i++) {
                course = coursesList.get(i);
                course_time = course.getTime();
                if (parsetime.checkWeek(course_time, week)) {
                    if (time_minute < parsetime.getEndTime(course_time)
                            && time_minute > parsetime.getStartTime(course_time)) {
                        time = new TimeCount((parsetime.getEndTime(course_time) - time_minute)*60*1000, 1000);
                        //time = new TimeCount(10000, 1000);
                        time.start();
                        isClassTime = true;
                        break;
                    }
                }
            }
            if (!isClassTime) {
                AlertDialog.Builder builder = new AlertDialog.Builder(FocusActivity.this);
                // set dialog title & message, and provide Button to dismiss
                builder.setTitle("Oh no");
                builder.setMessage("It's not course time");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int button) {
                        Course.isNeedRefresh = true;
                        FocusActivity.this.finish();
                    }

                });
                builder.show(); // display the Dialog
            }
        }

        @Override
        protected void onCancelled(){
            super.onCancelled();
            new DatabaseException().promptDialog(getApplicationContext());
        }

    }

    public class UpdateFocusPoint extends AsyncTask<Integer, Void, Integer> {
        protected Integer doInBackground(Integer... para) {
            if (para[0] == 1) {
                int focus = Session.get(getApplicationContext()).getUser().getFocus();
                BackendConnector.updateFocus(Session.get(getApplicationContext()).getUser().getId(), focus + 10);
                return 1;

            }
            else {
                int focus = Session.get(getApplicationContext()).getUser().getFocus();
                BackendConnector.updateFocus(Session.get(getApplicationContext()).getUser().getId(), focus - 10);
                return 0;
            }
        }

        protected void onPostExecute(Integer result) {
            if (result == 1) {
                AlertDialog.Builder builder = new AlertDialog.Builder(FocusActivity.this);
                // set dialog title & message, and provide Button to dismiss
                builder.setTitle("Great");
                builder.setMessage("You made it!\nFocus Point +10");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int button) {
                        Course.isNeedRefresh = true;
                        FocusActivity.this.finish();
                    }

                });

                builder.show(); // display the Dialog
            }
            else {
                time.cancel();
                FocusActivity.this.finish();
                return;
            }

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK )
        {
            AlertDialog.Builder isExit = new AlertDialog.Builder(this);
            isExit.setTitle("Attention");
            isExit.setMessage("Are you sure you want to quit? You point will be deducted");
            isExit.setPositiveButton("Quit", listener);
            isExit.setNegativeButton("Stay", listener);
            // show dialog
            isExit.show();

        }

        return false;

    }
    /**button listener*/
    DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener()
    {
        public void onClick(DialogInterface dialog, int which)
        {
            switch (which)
            {
                case AlertDialog.BUTTON_POSITIVE:// stay
                    new UpdateFocusPoint().execute(0);
                    break;
                case AlertDialog.BUTTON_NEGATIVE:// quit
                    break;
                default:
                    break;
            }
        }
    };

}
