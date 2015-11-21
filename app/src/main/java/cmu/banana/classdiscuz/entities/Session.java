package cmu.banana.classdiscuz.entities;

import android.content.Context;
import android.content.SharedPreferences;

import cmu.banana.classdiscuz.ws.remote.BackendConnector;

public class Session {
    private static String PREFNAME = "";
    private static User currentUser;
    private static Session sSession;
    SharedPreferences pref;
    private Context mAppContext;

    private Session(Context appContext) {
        mAppContext = appContext;

        if (currentUser == null) {
            pref = appContext.getSharedPreferences(PREFNAME,Context.MODE_PRIVATE);
            //int uid = pref.getInt("UserId",-1);
            int uid = 1;
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
