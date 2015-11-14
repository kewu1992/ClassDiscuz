package cmu.banana.classdiscuz.entities;

import cmu.banana.classdiscuz.exception.InputInvalidException;

public class User {
    private int id;
    private String email;
    private String password;
    private String name;
    private byte[] avatar;
    private String college;
    private String major;
    private int focus;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public byte[] getAvatar() {
        return avatar;
    }
    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
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

    public void checkValid() throws InputInvalidException {

    }

}
