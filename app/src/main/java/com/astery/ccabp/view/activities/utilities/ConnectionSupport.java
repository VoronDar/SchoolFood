package com.astery.ccabp.view.activities.utilities;

import android.util.Log;

import androidx.fragment.app.Fragment;

import com.astery.ccabp.view.activities.MainActivity;

public class ConnectionSupport {
    private static MainActivity.Steps fragment;

    public static void setFragment(MainActivity.Steps fragment){
        ConnectionSupport.fragment = fragment;
    }

    public static boolean isNowFragmentAvailable(MainActivity.Steps fragment){
        return (fragment.equals(ConnectionSupport.fragment));
    }
    public static boolean isNowFragmentAvailable(MainActivity.Steps fragment, Fragment fr){
        if (fr.getView() == null) return false;
        return (fragment.equals(ConnectionSupport.fragment));
    }
}
