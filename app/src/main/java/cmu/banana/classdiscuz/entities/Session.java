package cmu.banana.classdiscuz.entities;

import android.content.Context;
import android.content.SharedPreferences;

import cmu.banana.classdiscuz.ws.remote.BackendConnector;

public class Session {
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String UserId = "idKey";
    public static final String Email = "emailKey";
    public static final String Password = "passwordKey";
    private static User currentUser;
    private static Session sSession;
    SharedPreferences pref;
    private Context mAppContext;

    private Session(Context appContext) {
        mAppContext = appContext;

        if (currentUser == null) {
            pref = appContext.getSharedPreferences(MyPREFERENCES,Context.MODE_PRIVATE);
            int uid = pref.getInt(UserId, -1);
            currentUser = BackendConnector.getMemberByID(uid);
        }
    }

    public static Session get(Context c) {
        if (sSession == null) {
            sSession = new Session(c.getApplicationContext());
        }
        return sSession;
    }

    public User getUser() {
        return currentUser;
    }

    public void addLoginInfo(String email, String password, int userid, User user) {
        SharedPreferences sharedpreferences = mAppContext.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt(UserId, userid);
        editor.putString(Email, email);
        editor.putString(Password, password);
        editor.commit();
        int uid = userid;
    }
}
