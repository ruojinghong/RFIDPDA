package com.bigoffs.rfid.mvp.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author okbuy-001
 * @desc 理货转移货架列表信息
 * @time 2018/12/13 10:16
 */
public class TransferInfo {


    @SerializedName("success_incodes")
    private List<SuccessIncodesBean> successIncodes;
    @SerializedName("fail_incodes")
    private List<FailIncodesBean> failIncodes;

    public List<SuccessIncodesBean> getSuccessIncodes() {
        return successIncodes;
    }

    public void setSuccessIncodes(List<SuccessIncodesBean> successIncodes) {
        this.successIncodes = successIncodes;
    }

    public List<FailIncodesBean> getFailIncodes() {
        return failIncodes;
    }

    public void setFailIncodes(List<FailIncodesBean> failIncodes) {
        this.failIncodes = failIncodes;
    }

    public static class SuccessIncodesBean {
        /**
         * incode : ST39729739
         * rfid_code : 888888888888888888880061
         */

        @SerializedName("incode")
        private String incode;
        @SerializedName("rfid_code")
        private String rfidCode;

        public String getIncode() {
            return incode;
        }

        public void setIncode(String incode) {
            this.incode = incode;
        }

        public String getRfidCode() {
            return rfidCode;
        }

        public void setRfidCode(String rfidCode) {
            this.rfidCode = rfidCode;
        }
    }

    public static class FailIncodesBean {
        /**
         * incode : ST39729709
         * rfid_code : 888888888888888888880001
         */

        @SerializedName("incode")
        private String incode;
        @SerializedName("rfid_code")
        private String rfidCode;

        public String getIncode() {
            return incode;
        }

        public void setIncode(String incode) {
            this.incode = incode;
        }

        public String getRfidCode() {
            return rfidCode;
        }

        public void setRfidCode(String rfidCode) {
            this.rfidCode = rfidCode;
        }
    }
}
