package com.bigoffs.rfid.mvp.bean;

import com.bigoffs.rfid.mvp.bean.dao.ProductInfoBackupCase;
import com.bigoffs.rfid.mvp.bean.dao.ProductInfoCase;
import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.util.List;

/**
 * @author okbuy-001
 * @desc
 * @time 2018/12/18 11:35
 */
public class PickingDetailNew {

    @SerializedName("task_id")
    public int taskId;
    @SerializedName("outstock_id")
    public String outstockId;
    @SerializedName("outstock_code")
    public String outstockCode;
    @SerializedName("pick_list")
    public List<ShelfInfo> pickList;
    @SerializedName("barcode_list")
    public List<ShelfInfo> backupList;


    public class ShelfInfo{
        @SerializedName("shelf")
        public String shelf;
        @SerializedName("shelf_incode_num")
        public int shelfIncodeNum;
        @SerializedName("shelf_incode_pick_num")
        public int shelfIncodePickNum;
        @SerializedName("data")
        public List<ProductInfoCase> data;
    }






}
