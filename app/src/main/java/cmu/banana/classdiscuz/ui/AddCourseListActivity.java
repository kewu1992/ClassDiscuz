package cmu.banana.classdiscuz.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import cmu.banana.classdiscuz.R;

/**
 * Created by WK on 11/6/15.
 */
public class AddCourseListActivity extends AppCompatActivity {
    private ListView courseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course_list);

        courseList = (ListView) findViewById(R.id.course_add_listView);
    }

}
