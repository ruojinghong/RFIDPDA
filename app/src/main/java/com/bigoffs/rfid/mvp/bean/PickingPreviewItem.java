package com.bigoffs.rfid.mvp.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by okbuy on 17-4-1.
 */

public class PickingPreviewItem {
    @SerializedName("product_incode")
    public String incode;
    @SerializedName("shelf_num")
    public String shelfNumber;
    @SerializedName("finish_num")
    public int finishNum;
    @SerializedName("total_num")
    public int totalNum;
}
