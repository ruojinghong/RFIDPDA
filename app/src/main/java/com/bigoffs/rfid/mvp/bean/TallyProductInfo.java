package com.bigoffs.rfid.mvp.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by okbuy on 17-2-17.
 * 理货时的货品信息
 */

public class TallyProductInfo implements Serializable {
    @SerializedName("product_incode")
    public String incode;
    @SerializedName("shelf_num")
    public String shelfNumber;
    @SerializedName(value = "status_name", alternate = {"stock_status"})
    public String state;
    @SerializedName("check_status")
    public int inventoryState;
}
