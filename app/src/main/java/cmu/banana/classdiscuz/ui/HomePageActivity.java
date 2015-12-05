package cmu.banana.classdiscuz.ui;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.quickblox.chat.model.QBChatMessage;

import cmu.banana.classdiscuz.R;

/**
 * Created by WK on 11/6/15.
 */
public class HomePageActivity extends ChatBaseActivity implements ChatPageFragment.OnFragmentInteractionListener, SchedulePageFragment.OnFragmentInteractionListener{

    private SchedulePageFragment schedule;
    private ChatPageFragment chat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        setDefaultFragment();
    }

    private void setDefaultFragment()
    {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        schedule = new SchedulePageFragment();
        transaction.replace(R.id.homepage_content, schedule);
        transaction.commit();
    }

    public void onClick(View v) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();

        switch (v.getId())
        {
            //click the schecule
            case R.id.bottom_switch_schedule:
                if (schedule == null)
                {
                    schedule = new SchedulePageFragment();
                }
                //
                transaction.replace(R.id.homepage_content, schedule);
                break;
            case R.id.bottom_switch_chat:
                if (chat == null)
                {
                    chat = ChatPageFragment.newInstance(0);
                }
                transaction.replace(R.id.homepage_content, chat);
                break;
        }
        transaction.commit();
    }

    public void onFragmentInteraction(Uri uri){
        //you can leave it empty
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_settings:
                Intent myIntent = new Intent(this, SelfprofileActivity.class);
                startActivityForResult(myIntent, 0);
                break;
            case R.id.action_focus:
                Intent myIntent2 = new Intent(this, FocusActivity.class);
                startActivityForResult(myIntent2, 0);
                break;
        }

        return super.onOptionsItemSelected(item);

    }

    private void refresh() {
        Fragment fragment = getFragmentManager().findFragmentById(R.id.homepage_content);
        if (fragment instanceof ChatPageFragment) {
            ChatPageFragment chatPageFragment = (ChatPageFragment)fragment;
            chatPageFragment.getDialogs();
        }
    }

    public void showMessage(QBChatMessage message) {
        chat.showMessage(message);
    }

    @Override
    public void onStartSessionRecreation() {

    }

    @Override
    public void onFinishSessionRecreation(final boolean success) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (success) {
                    refresh();
                }
            }
        });
    }

}

