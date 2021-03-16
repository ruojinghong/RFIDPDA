package com.bigoffs.rfid.mvp.bean;

import com.google.gson.annotations.SerializedName;

/**
 * @author okbuy-001
 * @desc
 * @time 2020/4/24 16:06
 */
public class BarcodeInfo {
    @SerializedName("incode")
    public String incode;
    @SerializedName("shelf")
    public String shelf;
    //残次状态 0正常 1残次
    @SerializedName("isbad")
    public int isbad;
}
