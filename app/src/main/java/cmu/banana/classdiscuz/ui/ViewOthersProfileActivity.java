package cmu.banana.classdiscuz.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import cmu.banana.classdiscuz.R;
import cmu.banana.classdiscuz.entities.Course;
import cmu.banana.classdiscuz.entities.User;
import cmu.banana.classdiscuz.exception.DatabaseException;
import cmu.banana.classdiscuz.util.FocusTranslate;

/**
 * Activity to show other's profile
 */
public class ViewOthersProfileActivity extends AppCompatActivity {

    private int userID;

    private ImageView avatarImageView;
    private TextView nameTextView;
    private TextView collegeTextView;
    private TextView majorTextView;
    private TextView focusTextView;
    private ListView courseListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_others_profile);

        Bundle extras = getIntent().getExtras();
        userID = extras.getInt(ChatPageFragment.USR_ID);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        avatarImageView = (ImageView) findViewById(R.id.view_profile_avatar_imageView);
        nameTextView = (TextView) findViewById(R.id.view_profile_name_textView);
        collegeTextView = (TextView) findViewById(R.id.view_profile_college_textView);
        majorTextView = (TextView) findViewById(R.id.view_profile_major_textView);
        focusTextView = (TextView) findViewById(R.id.view_profile_focus_textView);
        courseListView = (ListView) findViewById(R.id.view_profile_register_course_listView);

        new GetUserInfo().execute(userID);
    }

    // create the Activity's menu from a menu resource XML file
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
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

    private class GetUserInfo extends AsyncTask<Integer, Object, User> {
        @Override
        protected User doInBackground(Integer... arg){
            try{
                return User.getUserById(ViewOthersProfileActivity.this, arg[0], false);
            } catch (DatabaseException e){
                cancel(true);
            }
            return null;
        }

        @Override
        protected void onPostExecute(User user){
            nameTextView.setText(user.getName());
            collegeTextView.setText(user.getCollege());
            majorTextView.setText(user.getMajor());
            focusTextView.setText("Lv. " + FocusTranslate.time2Level(user.getFocus()));

            byte[] imageBytes = user.getAvatar();
            if (imageBytes != null) {
                Bitmap pic = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                avatarImageView.setImageBitmap(pic);
            }

            new GetCourses().execute(user);
        }

        @Override
        protected void onCancelled(){
            super.onCancelled();
            new DatabaseException().promptDialog(ViewOthersProfileActivity.this);
        }
    }

    private class GetCourses extends AsyncTask<User, Object, ArrayList<Course>>{
        @Override
        protected ArrayList<Course> doInBackground(User... arg){
            try{
                return arg[0].getRegisteredCourses(ViewOthersProfileActivity.this, false);
            } catch (DatabaseException e){
                cancel(true);
            }

            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Course> courses){
            ArrayList<String> courseNames = new ArrayList<>(courses.size());
            for (Course course : courses)
                courseNames.add(course.getName());
            courseListView.setAdapter(new ArrayAdapter<String>(ViewOthersProfileActivity.this, android.R.layout.simple_list_item_1, courseNames));
        }

        @Override
        protected void onCancelled(){
            super.onCancelled();
            new DatabaseException().promptDialog(ViewOthersProfileActivity.this);
        }
    }

}
