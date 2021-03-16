package com.bigoffs.rfid.mvp.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author okbuy-001
 * @desc
 * @time 2020/5/26 16:22
 */
public class FinishInventoryInfo {

    @SerializedName("inventory_id")
    public int inventoryId;
    @SerializedName("shelfs")
    public List<String> shelfs;
    @SerializedName("db_info")
    public List<dbInfo> dbInfos;
    @SerializedName("more_info")
    public List<moreInfo> moreInfos;
    @SerializedName("lose_info")
    public List<loseInfo> loseInfos;
    @SerializedName("dis_info")
    public List<wrongInfo> wrongInfos;

    public class dbInfo{
        @SerializedName("shelf")
        public String shelf;
        @SerializedName("incode")
        public String incode;
    }

    public class wrongInfo{
        @SerializedName("shelf")
        public String shelf;
        @SerializedName("incode")
        public String incode;
        @SerializedName("correct_shelf")
        public String correctShelf;
    }

    public class moreInfo{
        @SerializedName("shelf")
        public String shelf;
        @SerializedName("incode")
        public String incode;
    }

    public class loseInfo{
        @SerializedName("shelf")
        public String shelf;
        @SerializedName("incode")
        public String incode;
    }
}
