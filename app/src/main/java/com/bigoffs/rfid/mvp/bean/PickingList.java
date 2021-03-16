package com.bigoffs.rfid.mvp.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by okbuy on 17-3-25.
 * 可领取拣货单
 */

public class PickingList {
    @SerializedName("outstock_code")
    public String code;
    @SerializedName("create_time")
    public String createTime;
    @SerializedName("count")
    public int num;
    @SerializedName("type_name")
    public String type;
    @SerializedName("outstock_id")
    public int id;
    @SerializedName("remark")
    public String remark;
    //1 销售拣货
    //2 拍照拣货
    //3 测量拣货
    //4 退返拣货
    //5 团购销售
    //6 报损出库
    //7 大宗团购
    //8 调拨拣货
    @SerializedName("type_id")
    public int typeId;
}
