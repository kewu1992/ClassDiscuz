package cmu.banana.classdiscuz.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
        
    }
}
