package com.sktelecom.smartfleet.sdk.util;

import android.util.Log;

import com.sktelecom.smartfleet.sdk.define.CONFIGS;


public class LogWrapper {

    private static final String TAG = "LogWrapper";

    static {
        try {
            if(CONFIGS.IS_DEBUG_LOG) {
                Log.d(TAG, "init success");
            }
        } catch (Exception e) {
            if(CONFIGS.IS_DEBUG_LOG) {
                Log.d(TAG, "init failure");
            }
        }
    }

    public static void v(String tag, String msg) {
        if(CONFIGS.IS_DEBUG_LOG) {
            Log.v(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if(CONFIGS.IS_DEBUG_LOG) {
            Log.e(tag, msg);
        }
    }
}