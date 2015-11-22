package cmu.banana.classdiscuz.ui;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Objects;

import cmu.banana.classdiscuz.R;
import cmu.banana.classdiscuz.entities.Course;
import cmu.banana.classdiscuz.entities.Session;
import cmu.banana.classdiscuz.entities.User;
import cmu.banana.classdiscuz.exception.DatabaseException;
import cmu.banana.classdiscuz.exception.InputInvalidException;

public class SelfprofileActivity extends AppCompatActivity {
    private final static int SELECT_PHOTO_CODE = 9997;

    private ImageView avatarImage;
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

        avatarImage = (ImageView)findViewById(R.id.selfprofile_avatar);
        registerCoursesTextView = (TextView)findViewById(R.id.selfprofile_text_course_value);
        focusTextView = (TextView)findViewById(R.id.selfprofile_text_focus_value);
        nameEditText = (EditText)findViewById(R.id.selfprofile_edit_name_value);
        universityEditText = (EditText)findViewById(R.id.selfprofile_edit_university_value);
        majorEditText = (EditText)findViewById(R.id.selfprofile_edit_major_value);

        avatarImage.setOnClickListener(buttonListen);

        new RefreshUserInfo().execute((Object)null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);

    }

    private View.OnClickListener buttonListen = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, SELECT_PHOTO_CODE);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Here we need to check if the activity that was triggers was the Image Gallery.
        // If it is the requestCode will match the LOAD_IMAGE_RESULTS value.
        // If the resultCode is RESULT_OK and there is some data we know that an image was picked.
        if (requestCode == SELECT_PHOTO_CODE && resultCode == RESULT_OK && data != null) {
            // Let's read picked image data - its URI
            Uri pickedImage = data.getData();
            // Let's read picked image path using content resolver
            String[] filePath = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(pickedImage, filePath, null, null, null);
            cursor.moveToFirst();
            String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);

            avatarImage.setImageBitmap(bitmap);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            Session.get(SelfprofileActivity.this).getUser().setAvatar(stream.toByteArray());

            cursor.close();

            new UpdateProfile().execute((Object) null);
        }
    }

    private class RefreshUserInfo extends AsyncTask<Object, Object, Object> {
        @Override
        protected Object doInBackground(Object... arg){
            Session.get(SelfprofileActivity.this).getUser();
            return null;
        }

        @Override
        protected void onPostExecute(Object arg){
            User user = Session.get(SelfprofileActivity.this).getUser();

            nameEditText.setText(user.getName());
            universityEditText.setText(user.getCollege());
            majorEditText.setText(user.getMajor());

            if (user.getAvatar() != null){
                byte[] imageBytes = user.getAvatar();
                Bitmap pic = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                avatarImage.setImageBitmap(pic);
            }
        }

    }

    private class UpdateProfile extends AsyncTask<Object, Object, Object>{
        private int eNum;

        @Override
        protected void onPreExecute (){
            User user = Session.get(SelfprofileActivity.this).getUser();

            user.setName(nameEditText.getText().toString());
            user.setCollege(universityEditText.getText().toString());
            user.setMajor(majorEditText.getText().toString());
        }

        @Override
        protected Object doInBackground(Object... arg){
            try{
                Session.get(SelfprofileActivity.this).getUser().updateProfile();
            } catch (DatabaseException e){
                eNum = 1;
                cancel(true);
            } catch (InputInvalidException e){
                eNum = 2;
                cancel(true);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object obj){
            AlertDialog.Builder builder = new AlertDialog.Builder(SelfprofileActivity.this);
            // set dialog title & message, and provide Button to dismiss
            builder.setTitle(R.string.updateProfile_success_title);
            builder.setMessage(R.string.updateProfile_success_msg);
            builder.setPositiveButton(R.string.updateProfile_success_button, null);
            builder.show(); // display the Dialog
        }

        @Override
        protected void onCancelled(){
            super.onCancelled();
            if (eNum == 1)
                new DatabaseException().promptDialog(SelfprofileActivity.this);
            else if (eNum == 2)
                new InputInvalidException().promptDialog(SelfprofileActivity.this);
        }
    }
}
