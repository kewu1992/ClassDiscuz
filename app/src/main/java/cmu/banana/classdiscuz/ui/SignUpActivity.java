package cmu.banana.classdiscuz.ui;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import cmu.banana.classdiscuz.R;

public class SignUpActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText repeatPwdEditText;
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private Button signUpBtn;

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

        String email = emailEditText.getText().toString();
        String password = emailEditText.getText().toString();
        String repeatPwd = emailEditText.getText().toString();
        String firstName = emailEditText.getText().toString();
        String lastName = emailEditText.getText().toString();

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptSignUp();
//                Intent goHomepage = new Intent(SignUpActivity.this, HomePageActivity.class);
//                startActivity(goHomepage);
            }
        });


    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptSignUp() {
//        if (mAuthTask != null) {
//            return;
//        }

        // Reset errors.
        emailEditText.setError(null);
        passwordEditText.setError(null);

        // Store values at the time of the login attempt.
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String repeatPassword = repeatPwdEditText.getText().toString();
        String name = emailEditText.getText().toString() + " " + emailEditText.getText().toString();
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
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
//            showProgress(true);
//            mAuthTask = new UserLoginTask(email, password);
//            mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    private boolean isRepeatPasswordValid(String repeatPassword, String password) {
        //TODO: Replace this with your own logic
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
}
