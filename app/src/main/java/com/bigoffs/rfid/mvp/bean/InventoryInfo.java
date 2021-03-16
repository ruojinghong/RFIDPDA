package com.bigoffs.rfid.mvp.bean;

import com.bigoffs.rfid.mvp.bean.dao.InventoryCase;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author okbuy-001
 * @desc
 * @time 2020/5/21 11:24
 */
public class InventoryInfo {

    @SerializedName("shelf_info")
    private List<InventoryCase> shelfs;
    @SerializedName("shelf_area")
    public String shelfArea;

    public void setShelfs(List<InventoryCase> shelfs) {
        this.shelfs = shelfs;
    }

    public List<InventoryCase> getShelfs() {
        return shelfs;
    }
}
