package cmu.banana.classdiscuz.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import cmu.banana.classdiscuz.R;
import cmu.banana.classdiscuz.entities.Course;
import cmu.banana.classdiscuz.entities.Session;
import cmu.banana.classdiscuz.exception.DatabaseException;
import cmu.banana.classdiscuz.exception.InputInvalidException;
import cmu.banana.classdiscuz.exception.NoSuchCourseException;

/**
 * Created by WK on 11/6/15.
 */
public class DeleteCourseActivity  extends AppCompatActivity {
    private ListView courseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_course_list);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        courseList = (ListView) findViewById(R.id.delete_course_listView);

        courseList.setOnItemClickListener(courseListListener);

        new GetCourses().execute((Object)null);
    }

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

    private class CourseAdapter extends ArrayAdapter<Course> {
        public CourseAdapter(ArrayList<Course> courses){
            super(DeleteCourseActivity.this, android.R.layout.simple_list_item_1, courses);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // if we weren't given a view, inflate one
            if (null == convertView) {
                convertView = DeleteCourseActivity.this.getLayoutInflater().inflate(R.layout.list_item_course, null);
            }

            Course course = getItem(position);

            String shownName = course.getNum() + " " + course.getName();

            ((TextView)convertView.findViewById(R.id.course_item_name_text_view)).setText(shownName);

            return convertView;
        }
    }

    AdapterView.OnItemClickListener courseListListener = new AdapterView.OnItemClickListener()
    {
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id)
        {
            new DropCourse().execute((Course)courseList.getAdapter().getItem(position));
        }

    };

    private class GetCourses extends AsyncTask<Object, Object, ArrayList<Course>> {
        @Override
        protected ArrayList<Course> doInBackground(Object... arg){
            try{
                return Session.get(DeleteCourseActivity.this).getUser().getRegisteredCourses(DeleteCourseActivity.this, true);
            } catch (DatabaseException e){
                cancel(true);
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Course> courses){
            CourseAdapter courseAdapter = new CourseAdapter(courses);
            courseList.setAdapter(courseAdapter);
        }

        @Override
        protected void onCancelled(){
            super.onCancelled();
            new DatabaseException().promptDialog(DeleteCourseActivity.this);
        }
    }

    private class DropCourse extends AsyncTask<Course, Object, Object> {
        private int eNum;
        @Override
        protected Object doInBackground(Course... arg){
            try{
                Session.get(DeleteCourseActivity.this).getUser().dropCourse(DeleteCourseActivity.this, arg[0]);
            } catch (DatabaseException e){
                eNum = 1;
                cancel(true);
            } catch(InputInvalidException e){
                eNum = 2;
                cancel(true);
            } catch (NoSuchCourseException e){
                eNum = 3;
                cancel(true);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object obj){
            AlertDialog.Builder builder = new AlertDialog.Builder(DeleteCourseActivity.this);
            // set dialog title & message, and provide Button to dismiss
            builder.setTitle(R.string.dropCourse_success_title);
            builder.setMessage(R.string.dropCourse_success_msg);
            builder.setPositiveButton(R.string.dropCourse_success_button, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int button) {
                    Course.isNeedRefresh = true;
                    DeleteCourseActivity.this.finish();
                }

            });
            builder.show(); // display the Dialog
            DeleteCourseActivity.this.finish();
        }

        @Override
        protected void onCancelled(){
            super.onCancelled();
            if (eNum == 1)
                new DatabaseException().promptDialog(DeleteCourseActivity.this);
            else if (eNum == 2)
                new InputInvalidException().promptDialog(DeleteCourseActivity.this);
            else if (eNum == 3)
                new NoSuchCourseException().promptDialog(DeleteCourseActivity.this);
        }
    }


}
