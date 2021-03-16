package com.bigoffs.rfid.mvp.bean;

import com.google.gson.annotations.SerializedName;

/**
 * @author okbuy-001
 * @desc
 * @time 2020/4/29 11:35
 */
public class SkuQueryInfo {
    @SerializedName("shelf")
    public String shelf;
    @SerializedName("size")
    public String size;
    @SerializedName("incode_num")
    public int incodeNum;
}
