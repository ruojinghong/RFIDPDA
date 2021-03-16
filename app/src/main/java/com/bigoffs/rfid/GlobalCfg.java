package com.bigoffs.rfid;



public class GlobalCfg {
    public static boolean DEBUG = false;

    public static final String VERSION = "2.6";
    private static String rootUrlDebug = "http://10.1.9.156/";
    private static String rootUrlRelease = "http://wms.bigoffs.cn/";
    public static String rootUrl = DEBUG ? rootUrlDebug : rootUrlRelease;



    public static final String PAGE_SIZE = "20";
}
