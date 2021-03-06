package com.example.myapplication.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.example.myapplication.MainActivity;

public class Utils {
    private final static String LOG_TAG = "Android_utils";
    private Utils(){}

//    private static Utils instance = new Utils();
//    public static Utils getInstance(){
//        return instance;
//    }

    /////////////////-------------------------  Utils func Sets   --------------------------------------////////////////
    public static void checkAndReqSelfPermission(@NonNull Object obj, String perMission){
        if (ActivityCompat.checkSelfPermission((Context)obj, perMission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity)obj, new String[]{perMission}, 1);
        }
    }
}
