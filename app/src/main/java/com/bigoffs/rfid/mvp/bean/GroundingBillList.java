package com.bigoffs.rfid.mvp.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author okbuy-001
 * @desc
 * @time 2018/12/6 16:10
 */
public class GroundingBillList {

    @SerializedName("arrival")
    public List<ArrivalBill> arrival;
    @SerializedName("allocation")
    public List<AllocationBill> allocation;


    public class ArrivalBill{
       @SerializedName("arrival_id")
        public String arrivalId;
       @SerializedName("arrival_code")
        public String arrivalCode;
       @SerializedName("status")
       public String status;
       @SerializedName("arrival_time")
        public String arrival_time;
       @SerializedName("note")
        public String note;
       @SerializedName("count")
        public int count;
       @SerializedName("instock_count")
        public int instockCount;
       @SerializedName("buyer")
        public String buyer;
       @SerializedName("supplier")
        public String supplier;
        public int type = 0;
    }
    public class AllocationBill{
        @SerializedName("allocation_id")
        public String allocationId;
        @SerializedName("allocation_code")
        public String allocationCode;
        @SerializedName("remark")
        public String remark;
        @SerializedName("allocation_number")
        public String allocationNumber;
        @SerializedName("arrival_time")
        public String arrivalTime;
        @SerializedName("stock_name")
        public String stockName;
        @SerializedName("into_num")
        public String intoNum;
        @SerializedName("status")
        public String status;
        @SerializedName("admin_name")
        public String admin;
        public int type = 1;
    }
}

