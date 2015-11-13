package cmu.banana.classdiscuz.model;

import java.util.ArrayList;

/**
 * Created by WK on 11/13/15.
 */
public class ChatMember {
    private int userID;
    private String name;
    private String college;
    private String major;
    private int focus;
    private byte[] avatar;

    public ChatMember(int userID, String name, String college, String major, int focus, byte[] avatar) {
        this.userID = userID;
        this.name = name;
        this.college = college;
        this.major = major;
        this.focus = focus;
        this.avatar = avatar;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public int getFocus() {
        return focus;
    }

    public void setFocus(int focus) {
        this.focus = focus;
    }

    public byte[] getAvatar() {
        return avatar;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }

}
