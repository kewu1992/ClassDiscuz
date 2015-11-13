package cmu.banana.classdiscuz.model;

import java.util.ArrayList;

/**
 * Created by WK on 11/13/15.
 */
public class Course {
    private int courseID;
    private String courseNum;
    private String courseName;
    private String courseInstructor;
    private String time;

    public Course(int courseID, String courseNum, String courseName, String courseInstructor, String time) {
        this.courseID = courseID;
        this.courseNum = courseNum;
        this.courseName = courseName;
        this.courseInstructor = courseInstructor;
        this.time = time;
    }

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    public String getCourseNum() {
        return courseNum;
    }

    public void setCourseNum(String courseNum) {
        this.courseNum = courseNum;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseInstructor() {
        return courseInstructor;
    }

    public void setCourseInstructor(String courseInstructor) {
        this.courseInstructor = courseInstructor;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
