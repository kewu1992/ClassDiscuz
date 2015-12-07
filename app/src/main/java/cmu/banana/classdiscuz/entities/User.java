package cmu.banana.classdiscuz.entities;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

import cmu.banana.classdiscuz.exception.DatabaseException;
import cmu.banana.classdiscuz.exception.InputInvalidException;
import cmu.banana.classdiscuz.exception.NoSuchCourseException;
import cmu.banana.classdiscuz.ws.local.DatabaseHelper;
import cmu.banana.classdiscuz.ws.remote.BackendConnector;

public class User {
    private int id;
    private String email;
    private String password;
    private String name;
    private byte[] avatar;
    private String college;
    private String major;
    private int focus;
    private int chatId;

    public User(int id, String email, String password, String name, byte[] avatar, String college, String major, int focus, int chatId) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.avatar = avatar;
        this.college = college;
        this.major = major;
        this.focus = focus;
        this.chatId = chatId;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getChatId() {
        return chatId;
    }
    public void setChatId(int chatId) {
        this.chatId = chatId;
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

    public ArrayList<Course> getRegisteredCourses(Context context, boolean is_self) throws DatabaseException{
        ArrayList<Course> result = null;
        if (is_self){
            DatabaseHelper db = new DatabaseHelper(context, null, null, 1);
            result = db.getAllCourses();
            for (Course course : result)
                Log.i("Course", course.getName());
        }
        else
            result =  BackendConnector.getCourses(id);
        if (result == null)
            throw new DatabaseException();
        return result;
    }

    public void registerCourse(Context context, Course course) throws InputInvalidException, DatabaseException, NoSuchCourseException {
        if (course == null)
            throw new NoSuchCourseException();
        ArrayList<Course> selectedCourses = getRegisteredCourses(context, true);
        for (Course selectedCourse : selectedCourses)
            if (selectedCourse.getId() == course.getId())
                throw new InputInvalidException(); // already selected the course

        DatabaseHelper db = new DatabaseHelper(context, null, null, 1);
        if (db.addCourse(course, id) == -1)
            throw new DatabaseException();
    }

    public void dropCourse(Context context, Course course) throws InputInvalidException, DatabaseException, NoSuchCourseException{
        if (course == null)
            throw new NoSuchCourseException();
        ArrayList<Course> selectedCourses = getRegisteredCourses(context, true);
        for (Course selectedCourse : selectedCourses)
            if (selectedCourse.getId() == course.getId()){
                DatabaseHelper db = new DatabaseHelper(context, null, null ,1);
                if (db.dropCourse(course.getId(), id) == -1)
                    throw new DatabaseException();
                else
                    return;
            }

        // not yet select the course
        throw new InputInvalidException();
    }

    public void updateProfile(Context context) throws InputInvalidException, DatabaseException {
        DatabaseHelper db = new DatabaseHelper(context, null, null, 1);
        int ret = db.updateProfile(id, this);
        if (ret == -1)
            throw new DatabaseException();
    }

    public void updateFocus(Context context, int focus) {
        DatabaseHelper db = new DatabaseHelper(context, null, null, 1);
        db.updateFocus(id, focus + 10);
    }

    public static User getUserById(Context context, int id, boolean is_self) throws DatabaseException{
        DatabaseHelper db = new DatabaseHelper(context, null, null, 1);
        User result =  db.getMemberByID(id, is_self);
        if (result == null)
            throw new DatabaseException();
        return result;
    }

}
