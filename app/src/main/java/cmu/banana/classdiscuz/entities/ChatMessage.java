package cmu.banana.classdiscuz.entities;

import cmu.banana.classdiscuz.exception.InputInvalidException;

/**
 * Created by WK on 11/13/15.
 */
public class ChatMessage implements CheckValid{
    private int userID;
    private long time;
    private byte[] message;

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public byte[] getMessage() {
        return message;
    }

    public void setMessage(byte[] message) {
        this.message = message;
    }

    public ChatMessage(int userID, long time, byte[] message) {

        this.userID = userID;
        this.time = time;
        this.message = message;
    }

    public void checkValid() throws InputInvalidException {

    }
}
