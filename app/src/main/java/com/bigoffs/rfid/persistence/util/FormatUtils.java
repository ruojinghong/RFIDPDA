package com.bigoffs.rfid.persistence.util;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lubin on 2016/11/24.
 */

public class FormatUtils {

    public static <T> T resultToBean(String result, Class<T> cl) throws Exception {
        return new Gson().fromJson(result, cl);
    }

    public static List<LinkedTreeMap> arrayFromData(String str) {

        Type listType = new TypeToken<ArrayList<?>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

}
