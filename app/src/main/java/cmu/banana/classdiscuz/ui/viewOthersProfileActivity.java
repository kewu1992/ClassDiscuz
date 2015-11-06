package cmu.banana.classdiscuz.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import cmu.banana.classdiscuz.R;

/**
 * Created by WK on 11/6/15.
 */
public class viewOthersProfileActivity extends AppCompatActivity {
    private ImageView avatarImageView;
    private TextView nameTextView;
    private TextView univeristyTextView;
    private TextView majorTextView;
    private TextView focusTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_others_profile);

        avatarImageView = (ImageView) findViewById(R.id.view_profile_avatar_imageView);
        nameTextView = (TextView) findViewById(R.id.view_profile_name_textView);
        univeristyTextView = (TextView) findViewById(R.id.view_profile_university_textView);
        majorTextView = (TextView) findViewById(R.id.view_profile_major_textView);
        focusTextView = (TextView) findViewById(R.id.view_profile_focus_textView);

    }

}
