package cmu.banana.classdiscuz.app;

import android.app.Application;
import android.widget.Toast;

import com.quickblox.core.QBSettings;

import org.apache.harmony.javax.security.auth.callback.TextOutputCallback;

import java.util.HashMap;

/**
 * Created by WK on 11/22/15.
 *
 * Reference: ApplicationSingleton.java from http://quickblox.com/developers/Android#Download_Android_SDK
 */
public class ApplicationSingleton extends Application{
    private static final String TAG = ApplicationSingleton.class.getSimpleName();

    public static final String APP_ID = "31318";
    public static final String AUTH_KEY = "tukFM5K2SKpWPEt";
    public static final String AUTH_SECRET = "C6XCdUk3Sy39Or3";
    //public static final String STICKER_API_KEY = "847b82c49db21ecec88c510e377b452c";

    public static final String USER_LOGIN = "kewu@andrew.cmu.edu";
    public static final String USER_PASSWORD = "CMU18641";

    private static ApplicationSingleton instance;
    public static ApplicationSingleton getInstance() {
        return instance;
    }

    private HashMap<Integer, String> userID2Name;
    private HashMap<Integer, byte[]> userID2Avatar;

    private Toast toast;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        // Initialise QuickBlox SDK
        //
        QBSettings.getInstance().fastConfigInit(APP_ID, AUTH_KEY, AUTH_SECRET);
        //StickersManager.initialize(STICKER_API_KEY, this);
    }

    public void setUserHashMap(HashMap<Integer, String> map) {
        userID2Name = map;
    }
    public void setAvatarHashMap(HashMap<Integer, byte[]> map) {
        userID2Avatar = map;
    }

    public String getName(Integer id) {
        return userID2Name.get(id);
    }
    public byte[] getAvatar(Integer id) {
        return userID2Avatar.get(id);
    }

    public void setToast(Toast toast) {
        this.toast = toast;
    }

    public void cancelToast() {
        if (toast != null)
            toast.cancel();
    }

    public Toast getToast() {
        return toast;
    }
}
