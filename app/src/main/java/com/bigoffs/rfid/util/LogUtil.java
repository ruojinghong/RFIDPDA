package com.bigoffs.rfid.util;

import android.util.Log;

/**
 * Log工具类，上线前记得把SHOW_LOG改为false
 * 
 * @author WangJiaming
 * @date 2016-10-10
 */
public class LogUtil {

    /**
     * 是否打印Log的开关
     */
    private static boolean SHOW_LOG = true;

    public static void i(String tag, String message) {
        if (SHOW_LOG) {
            Log.i(tag, message);
        }
    }

    public static void v(String tag, String message) {
        if (SHOW_LOG) {
            Log.v(tag, message);
        }
    }

    public static void d(String tag, String message) {
        if (SHOW_LOG) {
            Log.d(tag, message);
        }
    }

    public static void e(String tag, String message) {
        if (SHOW_LOG) {
            Log.e(tag, message);
        }
    }

    public static void w(String tag, String message) {
        if (SHOW_LOG) {
            Log.w(tag, message);
        }
    }

}
