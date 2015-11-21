package cmu.banana.classdiscuz.entities;

import java.util.ArrayList;

import cmu.banana.classdiscuz.exception.DatabaseException;
import cmu.banana.classdiscuz.exception.InputInvalidException;
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

    public User(int id, String email, String password, String name, byte[] avatar, String college, String major, int focus) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.avatar = avatar;
        this.college = college;
        this.major = major;
        this.focus = focus;
    }

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

    public ArrayList<Course> getRegisteredCourses() throws DatabaseException{
        ArrayList<Course> result =  BackendConnector.getCourses(id);
        if (result == null)
            throw new DatabaseException();
        return result;
    }

    public void registerCourse(Course course) throws InputInvalidException, DatabaseException{
        ArrayList<Course> selectedCourses = getRegisteredCourses();
        for (Course selectedCourse : selectedCourses)
            if (selectedCourse.getId() == course.getId())
                throw new InputInvalidException(); // already selected the course

        if (BackendConnector.regOrDropCourse(id, course.getId()) == -1)
            throw new DatabaseException();
    }

    public void dropCourse(Course course) throws InputInvalidException, DatabaseException{
        ArrayList<Course> selectedCourses = getRegisteredCourses();
        for (Course selectedCourse : selectedCourses)
            if (selectedCourse.getId() == course.getId()){
                if (BackendConnector.regOrDropCourse(id, course.getId()) == -1)
                    throw new DatabaseException();
            }

        // not yet select the course
        throw new InputInvalidException();
    }

    public static User getUserById(int id) throws DatabaseException{
        User result =  BackendConnector.getMemberByID(id);
        if (result == null)
            throw new DatabaseException();
        return result;
    }


}
