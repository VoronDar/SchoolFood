package com.astery.ccabp.view.activities.utilities;

public class FormMake {
    public static String make(String come) {
        // убрать посторонние символы, перевести все, кроме первой буквы в нижний регистр, верхнюю в верхний

        if (come == null || come.isEmpty()) return ""; //или return word;
        come = come.toLowerCase();
        come = come.substring(0, 1).toUpperCase() + come.substring(1);
        return come.replaceAll(" |_|,|\\.|", "");
    }
}