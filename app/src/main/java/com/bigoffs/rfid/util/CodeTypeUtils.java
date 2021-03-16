package com.bigoffs.rfid.util;

/**
 * @author okbuy-001
 * @desc
 * @time 2020/4/28 16:23
 */
public class CodeTypeUtils {

    public static boolean isIncodeOrEpc(String s){
        if (s.length()>12){
            return false;
        }
        return true;
    }
}
