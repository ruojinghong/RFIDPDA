package com.bigoffs.rfid.mvp.bean;

import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * @author okbuy-001
 * @desc
 * @time 2018/12/7 18:15
 */
@Entity
public class GroundingDetailInfo {

    @Id
    @SerializedName("incode")
    public String inCode;
    @SerializedName("barcode")
    public String barCode;
    @SerializedName("shelf")
    public String shelfCode;

    public boolean iCode;
    public boolean bCode;
    public boolean sCode;

    public long flag = 0;

    @Generated(hash = 1178777520)
    public GroundingDetailInfo(String inCode, String barCode, String shelfCode,
                               boolean iCode, boolean bCode, boolean sCode, long flag) {
        this.inCode = inCode;
        this.barCode = barCode;
        this.shelfCode = shelfCode;
        this.iCode = iCode;
        this.bCode = bCode;
        this.sCode = sCode;
        this.flag = flag;
    }

    @Generated(hash = 454425978)
    public GroundingDetailInfo() {
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

    public boolean getICode() {
        return this.iCode;
    }

    public void setICode(boolean iCode) {
        this.iCode = iCode;
    }

    public boolean getBCode() {
        return this.bCode;
    }

    public void setBCode(boolean bCode) {
        this.bCode = bCode;
    }

    public boolean getSCode() {
        return this.sCode;
    }

    public void setSCode(boolean sCode) {
        this.sCode = sCode;
    }

    public long getFlag() {
        return this.flag;
    }

    public void setFlag(long flag) {
        this.flag = flag;
    }

    @Override
    public boolean equals(Object obj) {
        return this.inCode.equals(((GroundingDetailInfo)obj).inCode);
    }


}
