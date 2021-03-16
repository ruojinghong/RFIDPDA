package com.bigoffs.rfid.mvp.bean;

import com.google.gson.annotations.SerializedName;

/**
 * @author okbuy-001
 * @desc
 * @time 2018/12/18 18:12
 */
public class PickingResult {
    //1为店内码2为货架号
    @SerializedName("status")
    public int status;
    @SerializedName("incode")
    public String incode;
    @SerializedName("detail_id")
    public int detailId;
}
