package com.bigoffs.rfid.persistence.util;

import android.content.Context;
import android.graphics.drawable.Drawable;

public class ResourcesUtils {

    public static Drawable getDrawable(Context context, int id) {
        return context.getResources().getDrawable(id);
    }
}
