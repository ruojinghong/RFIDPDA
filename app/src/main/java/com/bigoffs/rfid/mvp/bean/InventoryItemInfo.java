package com.bigoffs.rfid.mvp.bean;

import com.google.gson.annotations.SerializedName;

/**
 * @author okbuy-001
 * @desc
 * @time 2020/5/20 18:01
 */
public class InventoryItemInfo {


    /**
     * task_id : 123
     * shelf_area : ZA,ZB,ZC
     * create_time : 2020-05-20 19:00:00
     * all_number : 10000
     * already_number : 300
     */

    @SerializedName("task_id")
    private String taskId;
    @SerializedName("shelf_area")
    private String shelfArea;
    @SerializedName("create_time")
    private String createTime;
    @SerializedName("all_number")
    private int allNumber;
    @SerializedName("already_number")
    private int alreadyNumber;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getShelfArea() {
        return shelfArea;
    }

    public void setShelfArea(String shelfArea) {
        this.shelfArea = shelfArea;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getAllNumber() {
        return allNumber;
    }

    public void setAllNumber(int allNumber) {
        this.allNumber = allNumber;
    }

    public int getAlreadyNumber() {
        return alreadyNumber;
    }

    public void setAlreadyNumber(int alreadyNumber) {
        this.alreadyNumber = alreadyNumber;
    }
}
