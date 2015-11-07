package cmu.banana.classdiscuz.ui;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        avatarImageView = (ImageView) findViewById(R.id.view_profile_avatar_imageView);
        nameTextView = (TextView) findViewById(R.id.view_profile_name_textView);
        univeristyTextView = (TextView) findViewById(R.id.view_profile_university_textView);
        majorTextView = (TextView) findViewById(R.id.view_profile_major_textView);
        focusTextView = (TextView) findViewById(R.id.view_profile_focus_textView);

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
            case R.id.action_settings:
                Intent myIntent = new Intent(this, SelfprofileActivity.class);
                startActivityForResult(myIntent, 0);
        }

        return super.onOptionsItemSelected(item);

    }

}
