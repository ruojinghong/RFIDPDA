package com.bigoffs.rfid.mvp.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

/**
 * @author okbuy-001
 * @desc
 * @time 2020/4/24 16:06
 */
public class ProductInfo {
    @SerializedName("pic_url")
    public String picUrl;
    @SerializedName("incode")
    public String incode;
    @SerializedName("rfid_code")
    public String rfid;
    @SerializedName("product_info")
    public List<Map<String, String>> info;
    @SerializedName("system_logs")
    public List<SystemLog> logs;

    public class SystemLog {
        public String id;
        public String time;
        public String type;
        @SerializedName("user_name")
        public String userName;
    }
}
