package com.bigoffs.rfid.mvp.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by okbuy on 17-2-21.
 */

public class TaskInfo {
    @SerializedName("task_id")
    public int id;
    @SerializedName("type_id")
    public int type;
    @SerializedName("type_name")
    public String typeName;
    @SerializedName("create_time")
    public String createTime;
}
