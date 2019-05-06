package com.example.myp.FireBase.models;

public class ChatRoom {

    private String ID_Classroom;
    private String gradeGroup;
    private String schoolName;

    public ChatRoom(String ID_Classroom, String gradeGroup, String schoolName) {
        this.ID_Classroom = ID_Classroom;
        this.gradeGroup = gradeGroup;
        this.schoolName = schoolName;
    }

    public String getId() {
        return ID_Classroom;
    }

    public void setId(String ID_Classroom) {
        this.ID_Classroom = ID_Classroom;
    }

    public String getGradeGroup() {
        return gradeGroup;
    }

    public void getGradeGroup(String gradeGroup) {
        this.gradeGroup = gradeGroup;
    }

    public String getName() {
        return schoolName;
    }

    public void setName(String schoolName) {
        this.schoolName = schoolName;
    }
}