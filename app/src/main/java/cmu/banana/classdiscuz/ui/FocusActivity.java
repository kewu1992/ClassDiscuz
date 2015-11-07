package cmu.banana.classdiscuz.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.RadioButton;

import cmu.banana.classdiscuz.R;

/**
 * Created by WK on 11/6/15.
 */
public class FocusActivity extends AppCompatActivity {
    private Chronometer chronometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_focus);

        chronometer = (Chronometer) findViewById(R.id.focus_chronomer);

    }
}
