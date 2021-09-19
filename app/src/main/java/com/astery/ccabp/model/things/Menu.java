package com.astery.ccabp.model.things;

import android.util.Log;

import com.astery.ccabp.model.cloud_database.pogo.PostMenu;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Menu {
    public int thing;
    List<String> food;

    public int getThing() {
        return thing;
    }
    public Menu(int thing, List<String> food) {
        this.thing = thing;
        this.food = food;
    }

    public Menu(int thing, String first, String second, String third, String fourth, String fifth, String sixth, String seventh) {
        this.thing = thing;
        this.food = new ArrayList<>();
        if (first != null)
            food.add(first);
        if (second != null)
            food.add(second);
        if (third != null)
            food.add(third);
        if (fourth != null)
            food.add(fourth);
        if (fifth != null)
            food.add(fifth);
        if (sixth != null)
            food.add(sixth);
        if (seventh != null)
            food.add(seventh);
    }

    public List<String> getFood() {
        return food;
    }

    public static String DayIntToString(int day){
        switch (day){
            case Calendar.MONDAY:
                return "Понедельник";
            case Calendar.TUESDAY:
                return "Вторник";
            case Calendar.WEDNESDAY:
                return "Среда";
            case Calendar.THURSDAY:
                return "Четверг";
            case Calendar.FRIDAY:
                return "Пятница";
            case Calendar.SATURDAY:
                return "Суббота";
        }
        throw new RuntimeException("group to string - get " + day);
    }

    public static String ComeDayToString(int day){
        switch (day){
            case 0:
                return "Понедельник";
            case 1:
                return "Вторник";
            case 2:
                return "Среда";
            case 3:
                return "Четверг";
            case 4:
                return "Пятница";
            case 5:
                return "Суббота";
        }
        throw new RuntimeException("group to string - get " + day);
    }

    public static String ThingIntToString(int thing){
        switch (thing){
            case 0:
                return "Завтрак";
            case 1:
                return "Завтрак максимум";
            case 2:
                return "Обед";
            case 3:
                return "Обед максимум";
            case 4:
                return "Полдник";
            case 5:
                return "Полдник максимум";
        }
        throw new RuntimeException("thing to string - get " + thing);
    }

    public static ArrayList<Menu> convertFromPost(PostMenu m){
        Log.i("main", m.toString());
        ArrayList<Menu> menu = new ArrayList<>();
        if (m.getBreakfast() != null && m.getBreakfast().size() != 0)
            menu.add(new Menu(0, m.getBreakfast()));
        if (m.getBreakfastMax() != null && m.getBreakfastMax().size() != 0)
            menu.add(new Menu(1, m.getBreakfastMax()));
        if (m.getLanch() != null && m.getLanch().size() != 0)
            menu.add(new Menu(2, m.getLanch()));
        if (m.getLanchMax() != null && m.getLanchMax().size() != 0)
            menu.add(new Menu(3, m.getLanchMax()));
        if (m.getSnack10() != null && m.getSnack10().size() != 0)
            menu.add(new Menu(4, m.getSnack10()));
        if (m.getSnack15() != null && m.getSnack15().size() != 0)
            menu.add(new Menu(5, m.getSnack15()));

        return menu;
    }

}
