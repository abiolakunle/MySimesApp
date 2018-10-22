package com.software.abiol.simesapp.models;

public class Course {

    public String course_title;
    public String course_code;
    public String course_unit;

    public Course(){

    }

    public String getCourse_title() {
        return course_title;
    }

    public void setCourse_title(String course_title) {
        this.course_title = course_title;
    }

    public String getCourse_code() {
        return course_code;
    }

    public void setCourse_code(String course_code) {
        this.course_code = course_code;
    }

    public String getCourse_unit() {
        return course_unit;
    }

    public void setCourse_unit(String course_unit) {
        this.course_unit = course_unit;
    }

    public Course(String course_title, String course_code, String course_unit) {
        this.course_title = course_title;
        this.course_code = course_code;
        this.course_unit = course_unit;
    }
}
