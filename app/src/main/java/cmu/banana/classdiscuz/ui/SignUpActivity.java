package cmu.banana.classdiscuz.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.users.model.QBUser;

import java.util.List;

import cmu.banana.classdiscuz.R;
import cmu.banana.classdiscuz.entities.Session;
import cmu.banana.classdiscuz.entities.User;
import cmu.banana.classdiscuz.exception.SignUpException;
import cmu.banana.classdiscuz.ws.local.ChatService;
import cmu.banana.classdiscuz.ws.remote.BackendConnector;

/**
 * Activity to sign up a new user
 */
public class SignUpActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText repeatPwdEditText;
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private Button signUpBtn;
    private boolean isDatabaseError = false;
    private UserSignupTask mAuthTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        emailEditText = (EditText)findViewById(R.id.signup_email);
        passwordEditText = (EditText)findViewById(R.id.signup_password);
        repeatPwdEditText = (EditText)findViewById(R.id.signup_repeatPwd);
        firstNameEditText = (EditText)findViewById(R.id.signup_firstname);
        lastNameEditText = (EditText)findViewById(R.id.signup_lastname);
        signUpBtn = (Button)findViewById(R.id.signup_button);
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptSignUp();
            }
        });
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptSignUp() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        emailEditText.setError(null);
        passwordEditText.setError(null);

        // Store values at the time of the login attempt.
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String repeatPassword = repeatPwdEditText.getText().toString();
        String name = firstNameEditText.getText().toString() + " " + lastNameEditText.getText().toString();
        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            passwordEditText.setError(getString(R.string.error_invalid_password));
            focusView = passwordEditText;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            emailEditText.setError(getString(R.string.error_field_required));
            focusView = emailEditText;
            cancel = true;
        } else if (!isEmailValid(email)) {
            emailEditText.setError(getString(R.string.error_invalid_email));
            focusView = emailEditText;
            cancel = true;
        }

        if (!isRepeatPasswordValid(repeatPassword, password)) {
            repeatPwdEditText.setError(getString(R.string.error_uncatch_password));
            focusView = repeatPwdEditText;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            mAuthTask = new UserSignupTask(email, password, name);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 8;
    }

    private boolean isRepeatPasswordValid(String repeatPassword, String password) {
        return repeatPassword.equals(password);
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

    private class UserSignupTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;
        private final String nName;
        User user = null;
        UserSignupTask(String email, String password, String name) {
            mEmail = email;
            mPassword = password;
            nName = name;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            isDatabaseError = false;
            //get user from back end
            try {
                user = BackendConnector.signUp(mEmail, mPassword, nName);
            } catch (SignUpException e) {
                int errorno = e.getErrorno();
                if (errorno == 1 || errorno == 2) {
                    isDatabaseError = true;
                }
                return false;
            }

            SharedPreferences sharedpreferences = getSharedPreferences(Session.MyPREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString(Session.Email, mEmail);
            editor.putString(Session.Password, mPassword);
            editor.putInt(Session.UserId, user.getId());
            editor.commit();

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;

            if (success) {
                final QBUser user = new QBUser();
                user.setLogin(mEmail);
                user.setPassword(mPassword);

                ChatService.initIfNeed(SignUpActivity.this);
                ChatService.getInstance().login(user, new QBEntityCallbackImpl() {

                    @Override
                    public void onSuccess() {
                        Intent goHomePage = new Intent(SignUpActivity.this, HomePageActivity.class);
                        goHomePage.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(goHomePage);
                        finish();
                    }

                    @Override
                    public void onError(List errors) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(SignUpActivity.this);
                        dialog.setMessage("chat login errors: " + errors).create().show();
                    }
                });

            }
            else if (isDatabaseError) {
                Context context = getApplicationContext();
                CharSequence text = "Database Error!";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
            else {
                emailEditText.setError("The email already exits");
                emailEditText.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }
    }
}
