package com.example.myp.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Classroom implements Parcelable {

    private String title;
    private String classroom_id;
    private String grade;
    private String group;
    private String school_name;
    private String code_teacher;
    private String code_parent;
    private String status;

    public Classroom(String title, String classroom_id) {
        this.title = title;
        this.classroom_id = classroom_id;
    }

    public Classroom() {

    }

    protected Classroom(Parcel in) {
        title = in.readString();
        classroom_id = in.readString();
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getClassroom_id() {
        return classroom_id;
    }

    public void setClassroom_id(String classroom_id) {
        this.classroom_id = classroom_id;
    }

    @Override
    public String toString() {
        return "Classroom{" +
                "title='" + title + '\'' +
                ", classroom_id='" + classroom_id + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(classroom_id);
    }
}
