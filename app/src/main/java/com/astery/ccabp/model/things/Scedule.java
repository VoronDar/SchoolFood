package com.astery.ccabp.model.things;

public class Scedule {
    public int thing;
    public String info;
    public String group;

    public int getThing() {
        return thing;
    }

    public String getInfo() {
        return info;
    }

    public void setThing(int thing) {
        this.thing = thing;
    }

    public void setInfo(String info) {
        this.info = info;
    }


    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public Scedule(int thing, String info, String group) {
        this.thing = thing;
        this.info = info;
        this.group = group;
    }

    public Scedule(int thing, String info) {
        this.thing = thing;
        this.info = info;
    }
}
