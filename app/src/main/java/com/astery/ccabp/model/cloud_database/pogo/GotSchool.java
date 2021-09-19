package com.astery.ccabp.model.cloud_database.pogo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GotSchool {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("homeId")
    @Expose
    private int homeId;

    public GotSchool() {
    }


    public GotSchool(String name, int id, int homeId){
        this.name = name;
        this.id = id;
        this.homeId = homeId;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHomeId() {
        return homeId;
    }

    public void setHomeId(int homeId) {
        this.homeId = homeId;
    }
}