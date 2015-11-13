package cmu.banana.classdiscuz.ui;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import cmu.banana.classdiscuz.R;

/**
 * Created by WK on 11/6/15.
 */
public class HomePageActivity extends AppCompatActivity implements ChatPageFragment.OnFragmentInteractionListener, SchedulePageFragment.OnFragmentInteractionListener{
    private TextView courseNameTextView;
    private ListView courseListView;
    private ListView memberListView;
    private TextView memberNumTextView;
    private ListView chatHistoryListView;
    private EditText textEditText;
    private TextView scheduleClick;
    private TextView chatClick;

    private SchedulePageFragment schedule;
    private ChatPageFragment chat;

    private int userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

 //       requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_homepage);

        courseNameTextView = (TextView) findViewById(R.id.chat_course_name_text_view);
        courseListView = (ListView) findViewById(R.id.courseListView);
        memberListView = (ListView) findViewById(R.id.memberListView);
        memberNumTextView = (TextView) findViewById(R.id.memberNumTextView);
        chatHistoryListView = (ListView) findViewById(R.id.chat_list_view);
        textEditText = (EditText) findViewById(R.id.chat_edit_text);

        scheduleClick = (TextView) findViewById(R.id.bottom_switch_schedule);
        chatClick = (TextView) findViewById(R.id.bottom_switch_chat);

//        String text = textEditText.getText().toString();

        setDefaultFragment();

    }

    private void setDefaultFragment()
    {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        schedule = new SchedulePageFragment();
        transaction.replace(R.id.homepage_content, schedule);
        transaction.commit();
    }

    public void onClick(View v) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();

        switch (v.getId())
        {
            //click the schecule
            case R.id.bottom_switch_schedule:
                if (schedule == null)
                {
                    schedule = new SchedulePageFragment();
                }
                //
                transaction.replace(R.id.homepage_content, schedule);
                break;
            case R.id.bottom_switch_chat:
                if (chat == null)
                {
                    chat = ChatPageFragment.newInstance(0, userID);
                }
                transaction.replace(R.id.homepage_content, chat);
                break;
        }
        // transaction.addToBackStack();
        transaction.commit();
    }
    public void onFragmentInteraction(Uri uri){
        //you can leave it empty
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

    public int getUserID(){
        return userID;
    }

}

