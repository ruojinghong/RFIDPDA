package com.bigoffs.rfid.mvp.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author okbuy-001
 * @desc
 * @time 2018/12/24 11:38
 */
public class GroundingListInfo {
    @SerializedName("code")
    public String id;
    @SerializedName("task_id")
    public String taskId;
    @SerializedName("data")
    public List<GroundingListInfo> list;


}
