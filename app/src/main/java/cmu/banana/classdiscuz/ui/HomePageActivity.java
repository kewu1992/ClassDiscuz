package cmu.banana.classdiscuz.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import cmu.banana.classdiscuz.R;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        courseNameTextView = (TextView) findViewById(R.id.chat_course_name_text_view);
        courseListView = (ListView) findViewById(R.id.courseListView);
        memberListView = (ListView) findViewById(R.id.memberListView);
        memberNumTextView = (TextView) findViewById(R.id.memberNumTextView);
        chatHistoryListView = (ListView) findViewById(R.id.chat_list_view);
        textEditText = (EditText) findViewById(R.id.chat_edit_text);

        String text = textEditText.getText().toString();

    }
}
