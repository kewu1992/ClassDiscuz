package cmu.banana.classdiscuz.ws.local;

/**
 * Reference: ApplicationSingleton.java from http://quickblox.com/developers/Android#Download_Android_SDK
 */
public interface ApplicationSessionStateCallback {
    void onStartSessionRecreation();
    void onFinishSessionRecreation(boolean success);
}
