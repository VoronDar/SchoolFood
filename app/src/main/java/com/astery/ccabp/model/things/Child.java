package com.astery.ccabp.model.things;

import androidx.annotation.NonNull;

public class Child {
    private String id;
    private String name;
    private String sName;
    private String pName;
    private String birth;
    private int school;
    private String grade;
    private String schoolName;
    private int classId;
    private int homeId;
    private String classCode;
    private boolean activated;
    private boolean free;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    public int getSchool() {
        return school;
    }

    public void setSchool(int school) {
        this.school = school;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Child(String id, String name, String sName, int school, String grade, int classId, boolean isActivated, String schoolName, int homeId, String pName, boolean free) {
        this.id = id;
        this.name = name;
        this.sName = sName;
        this.school = school;
        this.grade = grade;
        this.classId = classId;
        this.activated = isActivated;
        this.schoolName = schoolName;
        this.homeId = homeId;
        this.free = free;
        this.pName = pName;
    }

    public Child(String name, String sName, int school, String grade, int classId, boolean isActivated) {
        this.name = name;
        this.sName = sName;
        this.school = school;
        this.grade = grade;
        this.classId = classId;
        this.activated = isActivated;
        this.free = false;
    }

    public Child(String id) {
        this.id = id;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public int getHomeId() {
        return homeId;
    }

    public void setHomeId(int homeId) {
        this.homeId = homeId;
    }

    public void setFree(boolean free) {
        this.free = free;
    }

    public boolean isFree() {
        return free;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }


    public String getBirth() {
        return pName;
    }

    public void setBirth(String pName) {
        this.pName = pName;
    }

    @Override
    public String toString() {
        return "Child{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", sName='" + sName + '\'' +
                ", pName='" + pName + '\'' +
                ", birth='" + birth + '\'' +
                ", school=" + school +
                ", grade='" + grade + '\'' +
                ", schoolName='" + schoolName + '\'' +
                ", classId=" + classId +
                ", homeId=" + homeId +
                ", classCode='" + classCode + '\'' +
                ", activated=" + activated +
                ", free=" + free +
                '}';
    }
}


