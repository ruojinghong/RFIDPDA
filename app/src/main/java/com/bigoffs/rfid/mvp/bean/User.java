package com.bigoffs.rfid.mvp.bean;

import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @author okbuy-001
 * @desc
 * @time 2020/4/22 10:46
 */
@Entity
public class User {
    @SerializedName("user_id")
    public String userId;
    @SerializedName("true_name")
    public String userName;
    @SerializedName("access_token")
    public String accessToken;
    @Generated(hash = 1603771601)
    public User(String userId, String userName, String accessToken) {
        this.userId = userId;
        this.userName = userName;
        this.accessToken = accessToken;
    }
    @Generated(hash = 586692638)
    public User() {
    }
    public String getUserId() {
        return this.userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getUserName() {
        return this.userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getAccessToken() {
        return this.accessToken;
    }
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
