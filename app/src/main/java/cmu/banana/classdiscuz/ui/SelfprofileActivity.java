package cmu.banana.classdiscuz.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import cmu.banana.classdiscuz.R;
import cmu.banana.classdiscuz.entities.Course;
import cmu.banana.classdiscuz.entities.Session;
import cmu.banana.classdiscuz.entities.User;
import cmu.banana.classdiscuz.exception.DatabaseException;
import cmu.banana.classdiscuz.exception.InputInvalidException;
import cmu.banana.classdiscuz.util.BitmapScale;
import cmu.banana.classdiscuz.util.FocusTranslate;

public class SelfprofileActivity extends AppCompatActivity {
    private final static int SELECT_PHOTO_CODE = 9997;
    private final static int TAKE_PHOTO_CODE = 9998;

    private ImageView avatarImage;
    private TextView focusTextView;
    private EditText nameEditText;
    private EditText universityEditText;
    private EditText majorEditText;
    private Button saveEditButton;
    private ListView courseListView;
    private Button logOutButton;

    private Uri fileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selfprofile);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        avatarImage = (ImageView)findViewById(R.id.selfprofile_avatar);
        focusTextView = (TextView)findViewById(R.id.selfprofile_text_focus_value);
        nameEditText = (EditText)findViewById(R.id.selfprofile_edit_name_value);
        universityEditText = (EditText)findViewById(R.id.selfprofile_edit_university_value);
        majorEditText = (EditText)findViewById(R.id.selfprofile_edit_major_value);
        avatarImage.setOnClickListener(buttonListen);
        courseListView = (ListView) findViewById(R.id.view_selfprofile_register_course_listView);
        saveEditButton = (Button)findViewById(R.id.selfprofile_button_edit);
        logOutButton = (Button) findViewById(R.id.selfprofile_button_logout);
        saveEditButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new UpdateProfile().execute((Object) null);

            }
        });

        logOutButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(SelfprofileActivity.this, FrontpageActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
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

            AlertDialog.Builder builder = new AlertDialog.Builder(SelfprofileActivity.this);
            builder.setTitle(R.string.AvatarUploadDialogTitle)
                    .setItems(R.array.uploadAvatarOptions, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == 0) {
                                // create folder
                                File imagesFolder = new File(Environment.getExternalStorageDirectory().getPath() + "/images/");
                                if (!imagesFolder.exists()){
                                    imagesFolder.mkdir();
                                }

                                // create file
                                DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
                                //get current date time with Date()
                                Date date = new Date();
                                File image = new File(imagesFolder,  dateFormat.format(date)  + ".jpg");
                                fileUri = Uri.fromFile(image);

                                Log.i("Debug", fileUri.getPath());

                                // create Intent to take a picture and return control to the calling application
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name
                                // start the image capture Intent
                                startActivityForResult(intent, TAKE_PHOTO_CODE);
                            } else if (which == 1) {
                                Intent intent = new Intent(Intent.ACTION_PICK);
                                intent.setType("image/*");
                                startActivityForResult(intent, SELECT_PHOTO_CODE);
                            } else {
                                dialog.dismiss();
                            }
                        }
                    });
            builder.create().show();
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

            Bitmap scaledBitmap = BitmapScale.bitmapScale(SelfprofileActivity.this, bitmap, avatarImage.getHeight());
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

            Session.get(SelfprofileActivity.this).getUser().setAvatar(stream.toByteArray());

            cursor.close();

        }

        if (requestCode == TAKE_PHOTO_CODE && resultCode == RESULT_OK) {
            /*
            Uri pickedImage = fileUri;
            // Let's read picked image path using content resolver
            String[] filePath = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(pickedImage, filePath, null, null, null);
            cursor.moveToFirst();
            String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));
            */

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(), options);

            avatarImage.setImageBitmap(bitmap);

            Bitmap scaledBitmap = BitmapScale.bitmapScale(SelfprofileActivity.this, bitmap, avatarImage.getHeight());
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

            Session.get(SelfprofileActivity.this).getUser().setAvatar(stream.toByteArray());

            // cursor.close();
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
            focusTextView.setText("Lv. " + FocusTranslate.time2Level(user.getFocus()));

            if (user.getAvatar() != null){
                byte[] imageBytes = user.getAvatar();
                Bitmap pic = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                avatarImage.setImageBitmap(pic);
            }

            new GetCourses().execute(user);
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
                Session.get(SelfprofileActivity.this).getUser().updateProfile(SelfprofileActivity.this);
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

    private class GetCourses extends AsyncTask<User, Object, ArrayList<Course>>{
        @Override
        protected ArrayList<Course> doInBackground(User... arg){
            try{
                return arg[0].getRegisteredCourses(SelfprofileActivity.this, true);
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
            courseListView.setAdapter(new ArrayAdapter<String>(SelfprofileActivity.this, android.R.layout.simple_list_item_1, courseNames));
        }

        @Override
        protected void onCancelled(){
            super.onCancelled();
            new DatabaseException().promptDialog(SelfprofileActivity.this);
        }
    }

}
