package com.astery.ccabp.model.cloud_database.pogo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GotChild {

    @SerializedName("classId")
    @Expose
    private int classId;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("lastName")
    @Expose
    private String lastName;
    @SerializedName("patronymic")
    @Expose
    private String patronymic;
    @SerializedName("schoolId")
    @Expose
    private int schoolId;
    @SerializedName("homeId")
    @Expose
    private int homeId;
    @SerializedName("daysInCycle")
    @Expose
    private int daysInCycle;
    @SerializedName("startCycle")
    @Expose
    private String startCycle;
    @SerializedName("className")
    @Expose
    private String className;
    @SerializedName("schoolName")
    @Expose
    private String schoolName;
    @SerializedName("isAvailable")
    @Expose
    private boolean isAvailable;
    @SerializedName("isFree")
    @Expose
    private boolean free;

    public GotChild() {
    }

    public GotChild(int parentId, String name, String lastName, int schoolId, String className, boolean isAvailable, String id, String schoolName, int homeId, int daysInCycle) {
        super();
        this.classId = parentId;
        this.name = name;
        this.lastName = lastName;
        this.schoolId = schoolId;
        this.className = className;
        this.isAvailable = isAvailable;
        this.id = id;
        this.schoolName = schoolName;
        this.homeId = homeId;
        this.daysInCycle = daysInCycle;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String classId) {
        this.className = classId;
    }

    public boolean isAvailable() {
        return isAvailable;
    }
    public String getId() {
        return id;
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

    public int getDaysInCycle() {
        return daysInCycle;
    }

    public void setDaysInCycle(int daysInCycle) {
        this.daysInCycle = daysInCycle;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public boolean isFree() {
        return free;
    }

    public void setFree(boolean free) {
        this.free = free;
    }

    public String getStartCycle() {
        return startCycle;
    }

    public void setStartCycle(String startCycle) {
        this.startCycle = startCycle;
    }
}
