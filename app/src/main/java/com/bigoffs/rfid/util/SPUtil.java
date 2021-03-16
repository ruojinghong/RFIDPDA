package com.bigoffs.rfid.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by okbuy on 17-3-10.
 */

public class SPUtil {

    public static void storeDownloadId(Context context, long id) {
        SharedPreferences sp = context.getSharedPreferences("DownloadInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong("downloadId", id);
        editor.commit();

    }

    public static long getDownloadId(Context context) {
        SharedPreferences sp = context.getSharedPreferences("DownloadInfo", Context.MODE_PRIVATE);
        return sp.getLong("downloadId", 0);
    }
    public static void setCurrentAllocationId(Context context, String id) {
        SharedPreferences sp = context.getSharedPreferences("dbsp", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("allocation_id", id);
        editor.commit();

    }
    public static String getCurrentAllocationId(Context context) {
        SharedPreferences sp = context.getSharedPreferences("dbsp", Context.MODE_PRIVATE);
        return sp.getString("","");

    }

}
