package com.example.myp.FireBase.models;

import android.os.Parcel;
import android.os.Parcelable;
import org.apache.commons.lang3.RandomStringUtils;


// FireBase Class to Create ClassRooms
public class Classroom implements Parcelable {
    private String ID;
    private String grade;
    private String group;
    private String school_name;
    private String code_teacher;
    private String code_parent;
    private String status;

    public Classroom(String ID, String grade, String group, String school_name, String status, String code_teacher, String code_parent){
        this.ID = ID;
        this.grade = grade;
        this.group = group;
        this.school_name = school_name;
        this.code_teacher = code_teacher;
        this.code_parent = code_parent;
        this.status = status;
    }

    public Classroom(){

    }

    protected Classroom(Parcel in){
        ID = in.readString();
        grade = in.readString();
        group = in.readString();
        school_name = in.readString();
        code_teacher = in.readString();
        code_parent = in.readString();
        status = in.readString();
    }

    public static final Creator<Classroom> CREATOR = new Creator<Classroom>() {
        @Override
        public Classroom createFromParcel(Parcel in) {
            return new Classroom(in);
        }

        @Override
        public Classroom[] newArray(int size) {
            return new Classroom[size];
        }
    };

    public String getID() { return ID; }
    public void setID(String ID) { this.ID = ID; }

    public String getGrade() {
        return grade;
    }
    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getGroup() {
        return group ;
    }
    public void setGroup(String group) {
        this.group = group;
    }

    public String getSchool_name() {
        return school_name;
    }
    public void setSchool_name(String school_name) {
        this.school_name = school_name;
    }

    public String getCode_teacher() { return code_teacher; }
    public void setCode_teacher(String code_teacher) { this.code_teacher = code_teacher; }

    public String getCode_parent() { return code_parent; }
    public void setCode_parent(String code_parent) { this.code_parent = code_parent; }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status){
        this.status = status;
    }

    @Override
    public String toString(){
        return "Room{" +
                "ID='" + ID + '\'' +
                ",grade='" + grade + '\'' +
                ", group='" + group + '\'' +
                ", school_name='" + school_name  + '\'' +
                ", code_teacher='" + code_teacher + '\'' +
                ", code_parent='" + code_parent + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    // Function to Print FireBase FireStore Data in the RecyclerViews
    public String titleTag(){
        return grade + " " + group + " / " + school_name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ID);
        dest.writeString(grade);
        dest.writeString(group);
        dest.writeString(school_name);
        dest.writeString(code_teacher);
        dest.writeString(code_parent);
        dest.writeString(status);
    }
}