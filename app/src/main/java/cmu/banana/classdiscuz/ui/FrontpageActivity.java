package cmu.banana.classdiscuz.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import cmu.banana.classdiscuz.R;

public class FrontpageActivity extends AppCompatActivity {

    private Button signUpBtn;
    private Button signInBtn;
    private CheckSessioinTask mAuthTask = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frontpage);

        ActionBar actionBar =  getSupportActionBar();
        actionBar.hide();

        signUpBtn = (Button) findViewById(R.id.frontpage_signup);
        signInBtn = (Button) findViewById(R.id.frontpage_login);

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goSignup = new Intent(FrontpageActivity.this, SignUpActivity.class);
                startActivity(goSignup);
            }
        });

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goSignIn = new Intent(FrontpageActivity.this, LoginActivity.class);
                startActivity(goSignIn);
            }
        });

        //check the session
        mAuthTask = new CheckSessioinTask(this);
        mAuthTask.execute((Void) null);
    }

    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        finish();
    }
    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class CheckSessioinTask extends AsyncTask<Void, Void, Boolean> {

        public static final String MyPREFERENCES = "MyPrefs";
        public static final String Email = "emailKey";
        public static final String Password = "passwordKey";
        private String mEmail;
        private String mPassword;
        SharedPreferences sharedpreferences;

        CheckSessioinTask(Context appContext) {
            sharedpreferences = appContext.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            mEmail = sharedpreferences.getString(Email, "null");
            mPassword = sharedpreferences.getString(Password, "null");

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {

        }

        @Override
        protected void onCancelled() {

        }

    }

}
