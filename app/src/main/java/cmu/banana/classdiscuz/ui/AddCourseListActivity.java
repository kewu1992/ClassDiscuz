package cmu.banana.classdiscuz.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        courseList = (ListView) findViewById(R.id.course_add_listView);
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

}
