package com.bigoffs.rfid.mvp.bean.dao;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author okbuy-001
 * @desc
 * @time 2020/5/18 14:41
 */
public class PutAwayInfo {
    /**
     * allocation_code : DB-20200512-0668
     * in_incode_number : 2333
     * sign_incode_number : 4000
     * in_incode_list : [{"incode":"ST39729739","rfid_code":"888888888888888888880061"},{"incode":"ST39729738","rfid_code":"888888888888888888880062"}]
     * wait_incode_list : [{"incode":"ST39729749","rfid_code":"888888888888888888880063"},{"incode":"ST39729748","rfid_code":"888888888888888888880064"}]
     */

    @SerializedName("allocation_code")
    private String allocationCode;
    @SerializedName("in_incode_number")
    private int inIncodeNumber;
    @SerializedName("sign_incode_number")
    private int signIncodeNumber;
    @SerializedName("in_incode_list")
    private List<GroundingAllocationDetailInfo> inIncodeList;
    @SerializedName("wait_incode_list")
    private List<GroundingAllocationDetailInfo> waitIncodeList;

    public String getAllocationCode() {
        return allocationCode;
    }

    public void setAllocationCode(String allocationCode) {
        this.allocationCode = allocationCode;
    }

    public int getInIncodeNumber() {
        return inIncodeNumber;
    }

    public void setInIncodeNumber(int inIncodeNumber) {
        this.inIncodeNumber = inIncodeNumber;
    }

    public int getSignIncodeNumber() {
        return signIncodeNumber;
    }

    public void setSignIncodeNumber(int signIncodeNumber) {
        this.signIncodeNumber = signIncodeNumber;
    }

    public List<GroundingAllocationDetailInfo> getInIncodeList() {
        return inIncodeList;
    }

    public void setInIncodeList(List<GroundingAllocationDetailInfo> inIncodeList) {
        this.inIncodeList = inIncodeList;
    }

    public List<GroundingAllocationDetailInfo> getWaitIncodeList() {
        return waitIncodeList;
    }

    public void setWaitIncodeList(List<GroundingAllocationDetailInfo> waitIncodeList) {
        this.waitIncodeList = waitIncodeList;
    }






}
