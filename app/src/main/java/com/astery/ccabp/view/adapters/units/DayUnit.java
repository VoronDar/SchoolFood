package com.astery.ccabp.view.adapters.units;

public class DayUnit {
    public final int day;
    public final boolean enabled;
    public final DayState dayState;
    public final boolean accepted;

    public DayUnit(int day, boolean enabled, DayState dayState, boolean accepted) {
        this.day = day;
        this.enabled = enabled;
        this.dayState = dayState;
        this.accepted = accepted;
    }

    public enum DayState{
        no,
        plan,
        task
    }

    @Override
    public String toString() {
        return "DayUnit{" +
                "day=" + day +
                ", enabled=" + enabled +
                ", dayState=" + dayState +
                ", accepted=" + accepted +
                '}';
    }
}
