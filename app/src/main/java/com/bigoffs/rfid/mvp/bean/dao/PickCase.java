package com.bigoffs.rfid.mvp.bean.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @author okbuy-001
 * @desc
 * @time 2020/5/22 14:48
 */
@Entity
public class PickCase {
    @Id(autoincrement = true)
    public Long id;

    public String incode;
    public String shelf;
    public String rfid;
    //是否被替换过
    public boolean isReplace;
    //是否已拣
    private boolean isPick;
    @Generated(hash = 567968377)
    public PickCase(Long id, String incode, String shelf, String rfid,
            boolean isReplace, boolean isPick) {
        this.id = id;
        this.incode = incode;
        this.shelf = shelf;
        this.rfid = rfid;
        this.isReplace = isReplace;
        this.isPick = isPick;
    }
    @Generated(hash = 265361691)
    public PickCase() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getIncode() {
        return this.incode;
    }
    public void setIncode(String incode) {
        this.incode = incode;
    }
    public String getShelf() {
        return this.shelf;
    }
    public void setShelf(String shelf) {
        this.shelf = shelf;
    }
    public String getRfid() {
        return this.rfid;
    }
    public void setRfid(String rfid) {
        this.rfid = rfid;
    }
    public boolean getIsReplace() {
        return this.isReplace;
    }
    public void setIsReplace(boolean isReplace) {
        this.isReplace = isReplace;
    }
    public boolean getIsPick() {
        return this.isPick;
    }
    public void setIsPick(boolean isPick) {
        this.isPick = isPick;
    }



}
