package com.bigoffs.rfid.mvp.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * @author okbuy-001
 * @desc
 * @time 2018/12/14 14:24
 */
public class InventoryInfoNew implements Serializable {
    @SerializedName("shelf")
    public String shelf;
    @SerializedName("incode")
    public String incode;
    @SerializedName("status")
    public int status;
    @SerializedName("old_shelf")
    public String oldShelf;
}
