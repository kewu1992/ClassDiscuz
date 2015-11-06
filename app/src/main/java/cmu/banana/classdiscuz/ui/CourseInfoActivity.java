package cmu.banana.classdiscuz.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
}
