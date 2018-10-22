package com.software.abiol.simesapp.models;

public class TimetableDayModel {

    TimetableDayModel(){

    }

    private String course_title;
    private String course_code;
    private String course_unit;
    private String course_lecturer;
    private String course_time;

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

    public String getCourse_lecturer() {
        return course_lecturer;
    }

    public void setCourse_lecturer(String course_lecturer) {
        this.course_lecturer = course_lecturer;
    }

    public String getCourse_time() {
        return course_time;
    }

    public void setCourse_time(String course_time) {
        this.course_time = course_time;
    }

    public TimetableDayModel(String course_title, String course_code, String course_unit, String course_lecturer, String course_time) {
        this.course_title = course_title;
        this.course_code = course_code;
        this.course_unit = course_unit;
        this.course_lecturer = course_lecturer;
        this.course_time = course_time;
    }
}
