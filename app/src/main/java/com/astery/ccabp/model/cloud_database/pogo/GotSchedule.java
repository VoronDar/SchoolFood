package com.astery.ccabp.model.cloud_database.pogo;

import com.astery.ccabp.model.things.Menu;
import com.astery.ccabp.model.things.Scedule;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GotSchedule {

    @SerializedName("breakfast")
    @Expose
    private String breakfast;
    @SerializedName("breakfastMax")
    @Expose
    private String breakfastMax;
    @SerializedName("lanch")
    @Expose
    private String lanch;
    @SerializedName("lanchMax")
    @Expose
    private String lanchMax;
    @SerializedName("snack10")
    @Expose
    private String snack10;
    @SerializedName("snack15")
    @Expose
    private String snack15;

    /**
     * No args constructor for use in serialization
     *
     */
    public GotSchedule() {
    }

    public GotSchedule(String breakfast,String breakfastMax, String lanch, String lanchMax, String snack10, String snack15) {
        super();
        this.breakfast = breakfast;
        this.breakfastMax = breakfastMax;
        this.lanch = lanch;
        this.lanchMax = lanchMax;
        this.snack10 = snack10;
        this.snack15 = snack15;
    }

    public String getBreakfast() {
        return breakfast;
    }

    public void setBreakfast(String breakfast) {
        this.breakfast = breakfast;
    }

    public String getLanch() {
        return lanch;
    }

    public void setLanch(String lanch) {
        this.lanch = lanch;
    }

    public String getSnack10() {
        return snack10;
    }

    public void setSnack10(String snack10) {
        this.snack10 = snack10;
    }

    public String getSnack15() {
        return snack15;
    }

    public void setSnack15(String snack15) {
        this.snack15 = snack15;
    }

    public String getBreakfastMax() {
        return breakfastMax;
    }

    public String getLanchMax() {
        return lanchMax;
    }


    public static ArrayList<Scedule> convertFromPost(GotSchedule m){
        ArrayList<Scedule> scedules = new ArrayList<>();
        if (m.getBreakfast() != null)
            scedules.add(new Scedule(0, m.getBreakfast()));
        if (m.getBreakfastMax() != null)
            scedules.add(new Scedule(1, m.getBreakfastMax()));
        if (m.getLanch() != null)
            scedules.add(new Scedule(2, m.getLanch()));
        if (m.getLanchMax() != null)
            scedules.add(new Scedule(3, m.getLanchMax()));
        if (m.getSnack10() != null)
            scedules.add(new Scedule(4, m.getSnack10()));
        if (m.getSnack15() != null)
            scedules.add(new Scedule(5, m.getSnack15()));

        return scedules;
    }

    private String makeString(List<String> str){
        if (str == null || str.size() == 0) return "";

        StringBuilder builder = new StringBuilder();
        for (String st: str){
            builder.append(st).append("|");
        }
        return builder.toString().substring(0, builder.length()-1);
    }

    private List<String> makeList(String str){
        List<String> l = new ArrayList<>();
        if (str == null || str.length() < 3) return l;

        String[] ar = str.split("\\|");

        l.addAll(Arrays.asList(ar));
        return l;
    }

    public void setBreakfastMax(String breakfastMax) {
        this.breakfastMax = breakfastMax;
    }

    public void setLanchMax(String lanchMax) {
        this.lanchMax = lanchMax;
    }

    @Override
    public String toString() {
        return "GotSchedule{" +
                "breakfast='" + breakfast + '\'' +
                ", breakfastMax='" + breakfastMax + '\'' +
                ", lanch='" + lanch + '\'' +
                ", lanchMax='" + lanchMax + '\'' +
                ", snack10='" + snack10 + '\'' +
                ", snack15='" + snack15 + '\'' +
                '}';
    }
}