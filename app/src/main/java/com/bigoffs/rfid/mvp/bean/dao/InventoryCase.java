package com.bigoffs.rfid.mvp.bean.dao;

import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @author okbuy-001
 * @desc
 * @time 2020/5/21 11:22
 */
@Entity
public class InventoryCase {


    /**
     * shelf_code : SC-18A-402
     * incode : ST39729739
     * rfid_code : 888888888888888888880061
     */
    @Id(autoincrement = true)
    private Long id;
    @SerializedName("shelf_code")
    private String shelfCode;
    @SerializedName("incode")
    private String incode;
    @SerializedName("rfid_code")
    private String rfidCode;

    @Generated(hash = 99061760)
    public InventoryCase(Long id, String shelfCode, String incode,
            String rfidCode) {
        this.id = id;
        this.shelfCode = shelfCode;
        this.incode = incode;
        this.rfidCode = rfidCode;
    }

    @Generated(hash = 195346679)
    public InventoryCase() {
    }

    public String getShelfCode() {
        return shelfCode;
    }

    public void setShelfCode(String shelfCode) {
        this.shelfCode = shelfCode;
    }

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

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
