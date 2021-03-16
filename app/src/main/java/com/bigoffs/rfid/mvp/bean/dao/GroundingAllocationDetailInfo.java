package com.bigoffs.rfid.mvp.bean.dao;

import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @author okbuy-001
 * @desc
 * @time 2020/5/15 17:50
 */
@Entity
public class GroundingAllocationDetailInfo {
    @Id(autoincrement = true)
    public Long id;
    @SerializedName("incode")
    public String inCode;
    @SerializedName("barcode")
    public String barCode;
    @SerializedName("shelf")
    public String shelfCode;
    @SerializedName("rfid_code")
    public String rfid;
    //是否存在于调拨单
    public boolean isExist;
    @Generated(hash = 867584103)
    public GroundingAllocationDetailInfo(Long id, String inCode, String barCode,
            String shelfCode, String rfid, boolean isExist) {
        this.id = id;
        this.inCode = inCode;
        this.barCode = barCode;
        this.shelfCode = shelfCode;
        this.rfid = rfid;
        this.isExist = isExist;
    }
    @Generated(hash = 276159857)
    public GroundingAllocationDetailInfo() {
    }
    public String getInCode() {
        return this.inCode;
    }
    public void setInCode(String inCode) {
        this.inCode = inCode;
    }
    public String getBarCode() {
        return this.barCode;
    }
    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }
    public String getShelfCode() {
        return this.shelfCode;
    }
    public void setShelfCode(String shelfCode) {
        this.shelfCode = shelfCode;
    }
    public String getRfid() {
        return this.rfid;
    }
    public void setRfid(String rfid) {
        this.rfid = rfid;
    }
    public boolean getIsExist() {
        return this.isExist;
    }
    public void setIsExist(boolean isExist) {
        this.isExist = isExist;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
}
