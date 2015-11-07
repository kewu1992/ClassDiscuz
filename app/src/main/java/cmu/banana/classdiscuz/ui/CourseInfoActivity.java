package cmu.banana.classdiscuz.ui;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import cmu.banana.classdiscuz.R;

public class CourseInfoActivity extends AppCompatActivity {

    private TextView courseTextView;
    private TextView numberTextView;
    private TextView timeTextView;
    private EditText placeEditText;
    private EditText instructorEditText;
    private EditText officehourEditText;

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
        instructorEditText =(EditText)findViewById(R.id.courseinfo_edit_instructor);
        officehourEditText = (EditText)findViewById(R.id.courseinfo_edit_officehour);


        String place = placeEditText.getText().toString();
        String instructor = instructorEditText.getText().toString();
        String officehour = officehourEditText.getText().toString();
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
