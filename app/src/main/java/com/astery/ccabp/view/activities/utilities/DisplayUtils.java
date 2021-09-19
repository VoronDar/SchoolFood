package com.astery.ccabp.view.activities.utilities;

import android.app.Activity;
import android.graphics.Point;
import android.view.Display;

import java.util.Objects;

public class DisplayUtils {
    public static int sizeX(Activity a){
        Point size = new Point();
        Display display = Objects.requireNonNull(a).getWindowManager().getDefaultDisplay();
        display.getSize(size);
        return size.x;
    }
    public static int sizeY(Activity a){
        Point size = new Point();
        Display display = Objects.requireNonNull(a).getWindowManager().getDefaultDisplay();
        display.getSize(size);
        return size.y;
    }
}
