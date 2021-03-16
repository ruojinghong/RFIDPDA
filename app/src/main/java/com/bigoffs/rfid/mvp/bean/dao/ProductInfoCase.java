package com.bigoffs.rfid.mvp.bean.dao;

import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @author okbuy-001
 * @desc
 * @time 2020/5/26 15:51
 */
@Entity
public class ProductInfoCase {
    @Id
    public Long id;
    @SerializedName("product_id")
    public int productId;
    @SerializedName("incode")
    public String incode;
    @SerializedName("size")
    public String size;
    @SerializedName("barcode")
    public String barcode;
    @SerializedName("brand_name")
    public String brandName;
    //1 未拣  2 已检
    @SerializedName("status")
    public int status;
    @SerializedName("pic_url")
    public String picUrl;
    @SerializedName("detail_id")
    public int detailId;
    @SerializedName("rfid_code")
    public String rfid;
    public String shelf;
    //是不是备胎
    public boolean isBackup;
    @Generated(hash = 632614573)
    public ProductInfoCase(Long id, int productId, String incode, String size,
            String barcode, String brandName, int status, String picUrl,
            int detailId, String rfid, String shelf, boolean isBackup) {
        this.id = id;
        this.productId = productId;
        this.incode = incode;
        this.size = size;
        this.barcode = barcode;
        this.brandName = brandName;
        this.status = status;
        this.picUrl = picUrl;
        this.detailId = detailId;
        this.rfid = rfid;
        this.shelf = shelf;
        this.isBackup = isBackup;
    }
    @Generated(hash = 50823506)
    public ProductInfoCase() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public int getProductId() {
        return this.productId;
    }
    public void setProductId(int productId) {
        this.productId = productId;
    }
    public String getIncode() {
        return this.incode;
    }
    public void setIncode(String incode) {
        this.incode = incode;
    }
    public String getSize() {
        return this.size;
    }
    public void setSize(String size) {
        this.size = size;
    }
    public String getBarcode() {
        return this.barcode;
    }
    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }
    public String getBrandName() {
        return this.brandName;
    }
    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }
    public int getStatus() {
        return this.status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public String getPicUrl() {
        return this.picUrl;
    }
    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }
    public int getDetailId() {
        return this.detailId;
    }
    public void setDetailId(int detailId) {
        this.detailId = detailId;
    }
    public String getRfid() {
        return this.rfid;
    }
    public void setRfid(String rfid) {
        this.rfid = rfid;
    }
    public String getShelf() {
        return this.shelf;
    }
    public void setShelf(String shelf) {
        this.shelf = shelf;
    }
    public boolean getIsBackup() {
        return this.isBackup;
    }
    public void setIsBackup(boolean isBackup) {
        this.isBackup = isBackup;
    }
}
