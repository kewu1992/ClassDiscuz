package cmu.banana.classdiscuz.entities;

import cmu.banana.classdiscuz.exception.InputInvalidException;

public class Course {
    private int id;
    private String num;
    private String instructor;
    private String name;
    private String time;

    public Course(int id, String num, String instructor, String name, String time) {
        this.id = id;
        this.num = num;
        this.instructor = instructor;
        this.name = name;
        this.time = time;
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

    public void checkValid() throws InputInvalidException {

    }

}
