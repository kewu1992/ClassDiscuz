package cmu.banana.classdiscuz.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import cmu.banana.classdiscuz.R;

/**
 * Created by WK on 11/6/15.
 */
public class AddCourseActivity extends AppCompatActivity {
    private RadioButton searchIDRadioButton;
    private RadioButton searchCourseRadioButton;
    private EditText departmentIDEditText;
    private EditText courseIDEditText;
    private EditText courseNameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        searchIDRadioButton = (RadioButton) findViewById(R.id.search_by_course_id_radioButton);
        searchCourseRadioButton = (RadioButton) findViewById(R.id.search_by_course_name_radioButton);
        departmentIDEditText = (EditText) findViewById(R.id.department_id_editText);
        courseIDEditText = (EditText) findViewById(R.id.course_id_editText);
        courseNameEditText = (EditText) findViewById(R.id.course_name_editText);

        String departmentID = departmentIDEditText.getText().toString();
        String courseID = courseIDEditText.getText().toString();
        String courseName = courseNameEditText.getText().toString();
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
            case R.id.action_settings:
                Intent myIntent = new Intent(this, SelfprofileActivity.class);
                startActivityForResult(myIntent, 0);
        }

        return super.onOptionsItemSelected(item);

    }
}
