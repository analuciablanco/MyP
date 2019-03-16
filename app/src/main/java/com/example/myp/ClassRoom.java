package com.example.myp;

import android.os.Parcel;
import android.os.Parcelable;

// FireBase Class to Create ClassRooms
public class ClassRoom implements Parcelable {
    private String ID;
    private String grade;
    private String group;
    private String school_name;
    private String status;

    public ClassRoom(String ID,String grade, String group, String school_name, String status){
        this.ID = ID;
        this.grade = grade;
        this.group = group;
        this.school_name = school_name;
        this.status = status;
    }
    public ClassRoom(){

    }
    protected ClassRoom(Parcel in){
        ID = in.readString();
        grade = in.readString();
        group = in.readString();
        school_name = in.readString();
        status = in.readString();

    }

    public static final Creator<ClassRoom> CREATOR = new Creator<ClassRoom>() {
        @Override
        public ClassRoom createFromParcel(Parcel in) {
            return new ClassRoom(in);
        }

        @Override
        public ClassRoom[] newArray(int size) {
            return new ClassRoom[size];
        }
    };

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

    public String getID() { return ID; }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setStatus(String status){
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString(){
        return "Room{" +
                "ID='" + ID + '\'' +
                ",grade='" + grade + '\'' +
                ", group='" + group + '\'' +
                ", school_name='" + school_name  + '\'' +
                ", status='" + status + '\'' +
                '}';
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
        dest.writeString(status);
    }
