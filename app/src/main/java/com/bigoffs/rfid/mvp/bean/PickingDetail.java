package com.bigoffs.rfid.mvp.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by okbuy on 17-3-30.
 * 拣货单详情
 */

public class PickingDetail {
    @SerializedName("task_id")
    public int taskId;
    @SerializedName("total_num")
    public int totalNum;
    @SerializedName("finish_num")
    public int finishNum;
    @SerializedName("shelf_list")
    public List<ShelfInfo> shelfInfos;

    public class ShelfInfo {
        @SerializedName("shelf_num")
        public String shelfNumber;
        @SerializedName("total_num")
        public int totalNum;
        @SerializedName("finish_num")
        public int finishNum;
        @SerializedName("incode_list")
        public List<PickingIncode> incodes;
    }

    public class PickingIncode {
        @SerializedName("product_incode")
        public String incode;
        public String category;
        public String color;
        public String brand;
        public String size;
        public int status;// 1已拣 2待拣
        @SerializedName("total_num")
        public int totalNum;
        @SerializedName("finish_num")
        public int finishNum;
        @SerializedName("incode_type")
        public int type;// 1店内码，2条码
    }
}

