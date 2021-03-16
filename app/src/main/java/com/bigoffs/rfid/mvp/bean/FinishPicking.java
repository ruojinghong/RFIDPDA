package com.bigoffs.rfid.mvp.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author okbuy-001
 * @desc
 * @time 2018/12/19 10:07
 */
public class FinishPicking {

    @SerializedName("pick_all_num")
    public int total;
    @SerializedName("pick_num")
    public int pickNum;
    @SerializedName("incode_list")
    public List<incode> incodeList;

    public class incode{
        @SerializedName("shelf")
        public String shelf;
        @SerializedName("incode")
        public String incode;
        @SerializedName("barcode")
        public String barcode;
        @SerializedName("status")
        public int status;

    }


}
