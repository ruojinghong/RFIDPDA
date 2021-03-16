package com.bigoffs.rfid.mvp.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @author okbuy-001
 * @desc
 * @time 2020/4/27 14:51
 */
@Entity
public class Sku implements Parcelable {
    @Id
    @SerializedName("incode")
    public String inCode;
    @SerializedName("shelf")
    public String shelfCode;
    @SerializedName("barcode")
    public String barCode;
    @SerializedName("rfid_code")
    public String epc;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.inCode);
        dest.writeString(this.shelfCode);
        dest.writeString(this.barCode);
        dest.writeString(this.epc);
    }

    public String getInCode() {
        return this.inCode;
    }

    public void setInCode(String inCode) {
        this.inCode = inCode;
    }

    public String getShelfCode() {
        return this.shelfCode;
    }

    public void setShelfCode(String shelfCode) {
        this.shelfCode = shelfCode;
    }

    public String getBarCode() {
        return this.barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getEpc() {
        return this.epc;
    }

    public void setEpc(String epc) {
        this.epc = epc;
    }

   public static final Creator<Sku> CREATOR = new Creator<Sku>() {
       @Override
       public Sku createFromParcel(Parcel source) {
           return new Sku(source);
       }

       @Override
       public Sku[] newArray(int size) {
           return new Sku[0];
       }
   };

   Sku(Parcel source){
        this.inCode = source.readString();
        this.shelfCode = source.readString();
        this.barCode = source.readString();
        this.epc = source.readString();
   }

@Generated(hash = 1007416480)
public Sku(String inCode, String shelfCode, String barCode, String epc) {
    this.inCode = inCode;
    this.shelfCode = shelfCode;
    this.barCode = barCode;
    this.epc = epc;
}

@Generated(hash = 1652649089)
public Sku() {
}


}
