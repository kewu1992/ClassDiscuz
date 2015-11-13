package cmu.banana.classdiscuz.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

import cmu.banana.classdiscuz.R;
import cmu.banana.classdiscuz.model.Course;
import cmu.banana.classdiscuz.util.BackendConnector;

/**
 * Created by WK on 11/6/15.
 */
public class AddCourseActivity extends AppCompatActivity {
    private RadioButton searchIDRadioButton;
    private RadioButton searchCourseRadioButton;
    private AutoCompleteTextView courseIDEditText;
    private AutoCompleteTextView courseNameEditText;
    private ImageButton addCourseButton;
    private TextView courseNameTextView;
    private TextView courseIDTextView;
    private TextView courseInstructorTextView;

    private ArrayList<Course> courses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        searchIDRadioButton = (RadioButton) findViewById(R.id.search_by_course_id_radioButton);
        searchCourseRadioButton = (RadioButton) findViewById(R.id.search_by_course_name_radioButton);
        courseIDEditText = (AutoCompleteTextView) findViewById(R.id.course_id_editText);
        courseNameEditText = (AutoCompleteTextView) findViewById(R.id.course_name_editText);
        addCourseButton = (ImageButton) findViewById(R.id.imageButton);
        courseNameTextView = (TextView) findViewById(R.id.add_course_course_name_text_view);
        courseIDTextView = (TextView) findViewById(R.id.add_course_course_id_text_view);
        courseInstructorTextView = (TextView) findViewById(R.id.add_course_instructor_text_view);

        searchIDRadioButton.setChecked(true);
        searchIDRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchCourseRadioButton.setChecked(false);
                courseNameEditText.setText("");
                courseIDEditText.requestFocus();
            }
        });

        searchCourseRadioButton.setChecked(false);
        searchCourseRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchIDRadioButton.setChecked(false);
                courseIDEditText.setText("");
                courseNameEditText.requestFocus();
            }
        });

        courseNameEditText.requestFocus();
        courseNameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    searchIDRadioButton.setChecked(false);
                    searchCourseRadioButton.setChecked(true);
                } else {
                    searchIDRadioButton.setChecked(true);
                    searchCourseRadioButton.setChecked(false);
                }
            }
        });

        courseNameEditText.setOnItemClickListener(courseNameTextListener);
        courseIDEditText.setOnItemClickListener(courseIDTextListener);

        addCourseButton.setOnClickListener(addCourseButtonListener);

        new GetCourses().execute((Object)null);
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

    private View.OnClickListener addCourseButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int courseID;
            if (searchIDRadioButton.isChecked()){
                for (Course course : courses){
                    if (course.getCourseNum().equals(courseIDEditText.getText().toString())){
                        courseID = course.getCourseID();
                        break;
                    }
                }
            } else {
                for (Course course : courses){
                    if (course.getCourseName().equals(courseNameEditText.getText().toString())){
                        courseID = course.getCourseID();
                        break;
                    }
                }
            }
            // TODO: add course to database
        }
    };

    private AdapterView.OnItemClickListener courseIDTextListener = new AdapterView.OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView<?> parent, View arg1, int pos, long id) {
            for (Course course : courses){
                if (course.getCourseNum().equals(courseIDEditText.getText().toString())){
                    courseNameTextView.setText(course.getCourseName());
                    courseIDTextView.setText(course.getCourseNum());
                    courseInstructorTextView.setText(course.getCourseInstructor());
                    break;
                }
            }
        }
    };

    private AdapterView.OnItemClickListener courseNameTextListener = new AdapterView.OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView<?> parent, View arg1, int pos, long id) {
            for (Course course : courses){
                if (course.getCourseName().equals(courseNameEditText.getText().toString())){
                    courseNameTextView.setText(course.getCourseName());
                    courseIDTextView.setText(course.getCourseNum());
                    courseInstructorTextView.setText(course.getCourseInstructor());
                    break;
                }
            }
        }
    };

    private class GetCourses extends AsyncTask<Object, Object, ArrayList<Course>> {
        @Override
        protected ArrayList<Course> doInBackground(Object... arg){
            // TODO: get all courses
        }

        @Override
        protected void onPostExecute(ArrayList<Course> courses){
            AddCourseActivity.this.courses = courses;
            String IDs[] = new String[courses.size()];
            String names[] = new String[courses.size()];
            for (int i = 0; i < courses.size(); i++){
                names[i] = courses.get(i).getCourseName();
                IDs[i] = courses.get(i).getCourseNum();
            }

            ArrayAdapter<String> idAdapter = new ArrayAdapter<String>(AddCourseActivity.this, android.R.layout.simple_dropdown_item_1line, IDs);
            courseIDEditText.setAdapter(idAdapter);

            ArrayAdapter<String> nameAdapter = new ArrayAdapter<String>(AddCourseActivity.this, android.R.layout.simple_dropdown_item_1line, names);
            courseNameEditText.setAdapter(nameAdapter);
        }
    }
}
