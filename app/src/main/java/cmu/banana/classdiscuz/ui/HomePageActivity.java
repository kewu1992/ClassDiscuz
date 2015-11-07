package cmu.banana.classdiscuz.ui;

import android.app.ActionBar;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import cmu.banana.classdiscuz.ChatPageFragment;
import cmu.banana.classdiscuz.R;
import cmu.banana.classdiscuz.SchedulePageFragment;

/**
 * Created by WK on 11/6/15.
 */
public class HomePageActivity extends AppCompatActivity {
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

        scheduleClick = (TextView) findViewById(R.id.textView8);
        chatClick = (TextView) findViewById(R.id.textView9);

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
            case R.id.textView8:
                if (schedule == null)
                {
                    schedule = new SchedulePageFragment();
                }
                //
                transaction.replace(R.id.homepage_content, schedule);
                break;
            case R.id.textView9:
                if (chat == null)
                {
                    chat = new ChatPageFragment();
                }
                transaction.replace(R.id.homepage_content, chat);
                break;
        }
        // transaction.addToBackStack();
        // 事务提交
        transaction.commit();
    }
    public void onNavFragmentInteraction(String string) {
        // Do stuff
    }



}

