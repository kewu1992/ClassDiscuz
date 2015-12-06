package cmu.banana.classdiscuz.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import cmu.banana.classdiscuz.R;
import cmu.banana.classdiscuz.entities.Course;
import cmu.banana.classdiscuz.entities.Session;
import cmu.banana.classdiscuz.exception.DatabaseException;
import cmu.banana.classdiscuz.exception.InputInvalidException;
import cmu.banana.classdiscuz.exception.NoSuchCourseException;
import cmu.banana.classdiscuz.ws.remote.BackendConnector;

public class CourseInfoActivity extends AppCompatActivity implements OnMapReadyCallback {

    private TextView courseTextView;
    private TextView numberTextView;
    private TextView timeTextView;
    private EditText placeEditText;
    private TextView instructorTextView;
    private EditText officehourEditText;
    private Button   dropButton;
    private Button   editButton;
    private Course course;
    private GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_info);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        courseTextView = (TextView) findViewById(R.id.courseinfo_text_coursename);
        numberTextView = (TextView)findViewById(R.id.courseinfo_text_coursenum);
        timeTextView = (TextView)findViewById(R.id.courseinfo_text_time);
        placeEditText = (EditText)findViewById(R.id.courseinfo_edit_place);
        instructorTextView =(TextView)findViewById(R.id.courseinfo_text_instructor);
        officehourEditText = (EditText)findViewById(R.id.courseinfo_edit_officehour);
        dropButton = (Button)findViewById(R.id.courseinfo_button_drop);
        editButton = (Button)findViewById(R.id.courseinfo_button_edit);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        course = (Course)bundle.getSerializable(SchedulePageFragment.EXTRA_MESSAGE);

        courseTextView.setText(course.getName());
        timeTextView.setText(course.getTime());
        numberTextView.setText(course.getNum());
        instructorTextView.setText(course.getInstructor());

        String place = placeEditText.getText().toString();
        String officehour = officehourEditText.getText().toString();

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*save place and office hour*/

            }
        });

        dropButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DropCourse().execute(course);
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    // create the Activity's menu from a menu resource XML file
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    } // end method onCreateOptionsMenu

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_settings:
                Intent myIntent = new Intent(this, SelfprofileActivity.class);
                startActivityForResult(myIntent, 0);
                break;
            case R.id.action_focus:
                Intent myIntent2 = new Intent(this, FocusActivity.class);
                startActivityForResult(myIntent2, 0);
                break;
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        new MarkLocation().execute(course);
    }

    private class DropCourse extends AsyncTask<Course, Object, Object> {
        private int eNum;
        @Override
        protected Object doInBackground(Course... arg){
            try{
                Session.get(CourseInfoActivity.this).getUser().dropCourse(arg[0]);
            } catch (DatabaseException e){
                eNum = 1;
                cancel(true);
            } catch(InputInvalidException e){
                eNum = 2;
                cancel(true);
            } catch(NoSuchCourseException e){
                eNum = 3;
                cancel(true);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object obj){
            AlertDialog.Builder builder = new AlertDialog.Builder(CourseInfoActivity.this);
            // set dialog title & message, and provide Button to dismiss
            builder.setTitle(R.string.dropCourse_success_title);
            builder.setMessage(R.string.dropCourse_success_msg);
            builder.setPositiveButton(R.string.dropCourse_success_button, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int button) {
                    Course.isNeedRefresh = true;
                    CourseInfoActivity.this.finish();
                }

            });
            builder.show(); // display the Dialog

        }


        public void onFragmentInteraction(Uri uri){

        }

        @Override
        protected void onCancelled(){
            super.onCancelled();
            if (eNum == 1)
                new DatabaseException().promptDialog(CourseInfoActivity.this);
            else if (eNum == 2)
                new InputInvalidException().promptDialog(CourseInfoActivity.this);
            else if (eNum == 3)
                new NoSuchCourseException().promptDialog(CourseInfoActivity.this);
        }
    }


    private class MarkLocation extends AsyncTask<Course, Object, String> {
        Course course;
        @Override
        protected String doInBackground(Course... arg){
            course = arg[0];
            String loc = BackendConnector.getGeolocation(course.getLocation());
            return loc;
        }

        @Override
        protected void onPostExecute(String arg){
            if (arg != null) {
                String[] coord = arg.split(",");
                LatLng curr = new LatLng(Double.parseDouble(coord[0]), Double.parseDouble(coord[1]));
                googleMap.addMarker(new MarkerOptions().position(curr).title(course.getName()));
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(curr));
                googleMap.animateCamera(CameraUpdateFactory.zoomIn());
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(18), 2000, null);
            } else {
                //If location is null them use CMU LatLng
                LatLng curr = new LatLng(40.4433, -79.9436);
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(curr));
                googleMap.animateCamera(CameraUpdateFactory.zoomIn());
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
            }
        }
    }
}
