package com.rohan90.quagmire.utils;

import android.util.Log;

import com.rohan90.quagmire.Constants;

/**
 * Created by rohan on 14/11/17.
 */

public class Logger {
    private static boolean isLoggerOn = true;
    private static final String TAG = Constants.LOGGER_TAG;

    public static void logError(String message) {
        if (isLoggerOn)
            Log.e(TAG, message);
    }

    public static void logInfo(String message) {
        if (isLoggerOn)
            Log.d(TAG, message);
    }

    public static void setLogging(boolean enabled) {
        isLoggerOn = enabled;
    }
}
