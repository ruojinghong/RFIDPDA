package com.bigoffs.rfid.mvp.bean;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author okbuy-001
 * @desc
 * @time 2020/4/28 15:47
 */
public class Allocation {

    /**
     * allocation_code : 20170310104025-11228-8431
     * all_incode_number : 10000
     * all_box_number : 10
     * sign_incode_number : 100
     * box_info : [{"box_code":"XH-20200211-140303","sign_incodes":[{"incode":"ST39729739","rfid_code":"888888888888888888880061"},{"incode":"ST39729738","rfid_code":"888888888888888888880062"}],"nosign_incodes":[{"incode":"ST39729709","rfid_code":"888888888888888888880001"},{"incode":"ST39729708","rfid_code":"888888888888888888880002"}]},{"box_code":"XH-20200211-140304","sign_incodes":[{"incode":"ST39721739","rfid_code":"888888888888888888881111"},{"incode":"ST39721738","rfid_code":"888888888888888888880012"}],"nosign_incodes":[{"incode":"ST39721734","rfid_code":"888888888888888888881112"},{"incode":"ST39721732","rfid_code":"888888888888888888881113"}]}]
     */

    @SerializedName("allocation_code")
    private String allocationCode;
    @SerializedName("all_incode_number")
    private int allIncodeNumber;
    @SerializedName("all_box_number")
    private int allBoxNumber;
    @SerializedName("sign_incode_number")
    private int signIncodeNumber;
    @SerializedName("box_info")
    private List<BoxInfoBean> boxInfo;

    public static Allocation objectFromData(String str) {

        return new Gson().fromJson(str, Allocation.class);
    }

    public String getAllocationCode() {
        return allocationCode;
    }

    public void setAllocationCode(String allocationCode) {
        this.allocationCode = allocationCode;
    }

    public int getAllIncodeNumber() {
        return allIncodeNumber;
    }

    public void setAllIncodeNumber(int allIncodeNumber) {
        this.allIncodeNumber = allIncodeNumber;
    }

    public int getAllBoxNumber() {
        return allBoxNumber;
    }

    public void setAllBoxNumber(int allBoxNumber) {
        this.allBoxNumber = allBoxNumber;
    }

    public int getSignIncodeNumber() {
        return signIncodeNumber;
    }

    public void setSignIncodeNumber(int signIncodeNumber) {
        this.signIncodeNumber = signIncodeNumber;
    }

    public List<BoxInfoBean> getBoxInfo() {
        return boxInfo;
    }

    public void setBoxInfo(List<BoxInfoBean> boxInfo) {
        this.boxInfo = boxInfo;
    }

    public static class BoxInfoBean {
        /**
         * box_code : XH-20200211-140303
         * sign_incodes : [{"incode":"ST39729739","rfid_code":"888888888888888888880061"},{"incode":"ST39729738","rfid_code":"888888888888888888880062"}]
         * nosign_incodes : [{"incode":"ST39729709","rfid_code":"888888888888888888880001"},{"incode":"ST39729708","rfid_code":"888888888888888888880002"}]
         */

        @SerializedName("box_code")
        private String boxCode;
        @SerializedName("sign_incodes")
        private List<SignIncodesBean> signIncodes;
        @SerializedName("nosign_incodes")
        private List<NosignIncodesBean> nosignIncodes;

        public String getBoxCode() {
            return boxCode;
        }

        public void setBoxCode(String boxCode) {
            this.boxCode = boxCode;
        }

        public List<SignIncodesBean> getSignIncodes() {
            return signIncodes;
        }

        public void setSignIncodes(List<SignIncodesBean> signIncodes) {
            this.signIncodes = signIncodes;
        }

        public List<NosignIncodesBean> getNosignIncodes() {
            return nosignIncodes;
        }

        public void setNosignIncodes(List<NosignIncodesBean> nosignIncodes) {
            this.nosignIncodes = nosignIncodes;
        }

        public static class SignIncodesBean {
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

        public static class NosignIncodesBean {
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
}
