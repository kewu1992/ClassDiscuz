package cmu.banana.classdiscuz.ui;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Text;

import cmu.banana.classdiscuz.R;

public class SelfprofileActivity extends AppCompatActivity {

    private ImageButton avatarImageButton;
    private TextView registerCoursesTextView;
    private TextView focusTextView;
    private EditText nameEditText;
    private EditText universityEditText;
    private EditText majorEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selfprofile);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        avatarImageButton = (ImageButton)findViewById(R.id.selfprofile_avatar);
        registerCoursesTextView = (TextView)findViewById(R.id.selfprofile_text_course_value);
        focusTextView = (TextView)findViewById(R.id.selfprofile_text_focus_value);
        nameEditText = (EditText)findViewById(R.id.selfprofile_edit_name_value);
        universityEditText = (EditText)findViewById(R.id.selfprofile_edit_university_value);
        majorEditText = (EditText)findViewById(R.id.selfprofile_edit_major_value);

        String name = nameEditText.getText().toString();
        String university = universityEditText.getText().toString();
        String major = majorEditText.getText().toString();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);

    }
}
