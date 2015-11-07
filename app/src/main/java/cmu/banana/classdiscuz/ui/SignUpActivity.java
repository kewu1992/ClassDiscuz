package cmu.banana.classdiscuz.ui;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
                Intent goHomepage = new Intent(SignUpActivity.this, HomePageActivity.class);
                startActivity(goHomepage);
            }
        });


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
