package cmu.banana.classdiscuz.entities;

import java.util.ArrayList;
import java.util.List;

import cmu.banana.classdiscuz.exception.DatabaseException;
import cmu.banana.classdiscuz.exception.InputInvalidException;
import cmu.banana.classdiscuz.ws.remote.BackendConnector;

public class Course {
    private int id;
    private String num;
    private String instructor;
    private String name;
    private String time;
    private String location;

    public Course(int id, String num, String instructor, String name, String time, String location) {
        this.id = id;
        this.num = num;
        this.instructor = instructor;
        this.name = name;
        this.time = time;
        this.location = location;
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
    public void setLocation(String location) {
        this.location = location;
    }
    public void checkValid() throws InputInvalidException {

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

}
