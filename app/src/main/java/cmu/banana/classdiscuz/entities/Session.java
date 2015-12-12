package cmu.banana.classdiscuz.entities;

import android.content.Context;
import android.content.SharedPreferences;

import cmu.banana.classdiscuz.ws.remote.BackendConnector;

/*
 *  Store the current user in this session
 */
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
}
