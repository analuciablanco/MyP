package com.example.myp;

import android.os.Parcel;
import android.os.Parcelable;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;


// FireBase Class to Create ClassRooms
public class ClassRoom implements Parcelable {
    private String ID;
    private String grade;
    private String group;
    private String school_name;
    private String code_teacher;
    private String code_parent;
    private String status;

    public ClassRoom(String ID,String grade, String group, String school_name, String status, String code_teacher, String code_parent){
        this.ID = ID;
        this.grade = grade;
        this.group = group;
        this.school_name = school_name;
        this.code_teacher = code_teacher;
        this.code_parent = code_parent;
        this.status = status;
    }

    public ClassRoom(){

    }

    protected ClassRoom(Parcel in){
        ID = in.readString();
        grade = in.readString();
        group = in.readString();
        school_name = in.readString();
        code_teacher = in.readString();
        code_parent = in.readString();
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
    public void setID(String ID) { this.ID = ID; }

    public String getCodeTeacher() { return code_teacher; }
    public void setCodeTeacher() { this.code_teacher = RandomStringUtils.random(6, true, true); }

    public String getCodeParent() { return code_parent; }
    public void setCodeParent() { this.code_parent = RandomStringUtils.random(6, true, true); }

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
                ", code_teacher='" + code_teacher + '\'' +
                ", code_parent='" + code_parent + '\'' +
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
        dest.writeString(code_teacher);
        dest.writeString(code_parent);
        dest.writeString(status);
    }
}