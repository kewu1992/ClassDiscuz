package cmu.banana.classdiscuz.entities;

import android.content.Context;

import java.io.Serializable;
import java.util.ArrayList;

import cmu.banana.classdiscuz.exception.DatabaseException;
import cmu.banana.classdiscuz.ws.local.DatabaseHelper;
import cmu.banana.classdiscuz.ws.remote.BackendConnector;

/*
 *  Model class for course
 */
public class Course implements Serializable{
    private int id;
    private String num;
    private String instructor;
    private String name;
    private String time;
    private String location;
    private String dialogId;

    public static boolean isNeedRefresh = false;

    public Course(int id, String num, String instructor, String name, String time, String location, String dialogId) {
        this.id = id;
        this.num = num;
        this.instructor = instructor;
        this.name = name;
        this.time = time;
        this.location = location;
        this.dialogId = dialogId;
    }

    public String getDialogId() {
        return dialogId;
    }
    public void setDialogId(String dialogId) {
        this.dialogId = dialogId;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getNum() {
        return num;
    }
    public void setNum(String num) {
        this.num = num;
    }
    public String getInstructor() {
        return instructor;
    }
    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public String getLocation() {
        return location;
    }

    public ArrayList<User> getStudents() throws DatabaseException{
        ArrayList<User> result =  BackendConnector.getMembersByCourse(id);
        if (result == null)
            throw new DatabaseException();
        return result;
    }

    public static ArrayList<Course> getAllCourses() throws DatabaseException{
        ArrayList<Course> result =  BackendConnector.getAllCourse();
        if (result == null)
            throw new DatabaseException();
        return result;
    }

    public String getOfficeHour(Context context) {
        DatabaseHelper db = new DatabaseHelper(context, null, null, 1);
        return db.getOfficeHour(num);
    }

    public void setOfficeHour(Context context, String officeHour) {
        DatabaseHelper db = new DatabaseHelper(context, null, null, 1);
        db.updateOfficeHour(num, officeHour);
    }


}