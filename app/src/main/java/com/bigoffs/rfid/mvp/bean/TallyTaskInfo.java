package com.bigoffs.rfid.mvp.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by okbuy on 17-2-20.
 * 理货任务单
 */

public class TallyTaskInfo {
    @SerializedName("task_id")
    public int taskId;
    @SerializedName("off_num")
    public int downNum;
    @SerializedName("on_num")
    public int upNum;
    @SerializedName("differ_num")
    public int remainNum;
    public int status;
    @SerializedName("off_list")
    public List<TallyProductInfo> downList;
    @SerializedName("on_list")
    public List<TallyProductInfo> upList;
    @SerializedName("differ_list")
    public List<TallyProductInfo> remainList;
}
