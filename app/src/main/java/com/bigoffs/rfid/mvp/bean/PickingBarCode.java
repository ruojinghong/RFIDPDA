package com.bigoffs.rfid.mvp.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author okbuy-001
 * @desc
 * @time 2018/12/19 10:04
 */
public class PickingBarCode {
    @SerializedName("barcode")
    public String barcode;
    @SerializedName("incode_list")
    public List<incode> incodeList;

    public class incode{
        @SerializedName("shelf")
        public String shelf;
        @SerializedName("incode")
        public String incode;
        @SerializedName("status")
        public int status;
    }
}
