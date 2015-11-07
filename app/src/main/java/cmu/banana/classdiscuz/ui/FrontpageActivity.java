package cmu.banana.classdiscuz.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import cmu.banana.classdiscuz.R;

public class FrontpageActivity extends AppCompatActivity {

    private Button signUpBtn;
    private Button signInBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frontpage);

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
    }
}
