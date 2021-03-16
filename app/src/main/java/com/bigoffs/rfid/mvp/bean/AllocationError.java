package com.bigoffs.rfid.mvp.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @author okbuy-001
 * @desc
 * @time 2020/5/7 10:44
 */
@Entity
public class AllocationError {

    @Id
    private Long id;
    private String boxCode;

    public AllocationError(String boxCode, String rfid,String incode) {
        this.boxCode = boxCode;
        this.rfid = rfid;
        this.incode = incode;
    }

    @Generated(hash = 1887625747)
    public AllocationError(Long id, String boxCode, String rfid, String incode) {
        this.id = id;
        this.boxCode = boxCode;
        this.rfid = rfid;
        this.incode = incode;
    }

    @Generated(hash = 2008438445)
    public AllocationError() {
    }

    private String rfid;

    private String incode;

    public void setIncode(String incode) {
        this.incode = incode;
    }

    public String getIncode() {
        return incode;
    }

    public void setBoxCode(String boxCode) {
        this.boxCode = boxCode;
    }

    public void setRfid(String rfid) {
        this.rfid = rfid;
    }

    public String getRfid() {
        return rfid;
    }

    public String getBoxCode() {
        return boxCode;
    }

    public Long getId() {
        return this.id;
    }



    public void setId(Long id) {
        this.id = id;
    }
}
