package com.bigoffs.rfid.mvp.biz;

import android.content.Context;

import com.bigoffs.rfid.network.ICommonResult;

/**
 * @author okbuy-001
 * @desc
 * @time 2020/4/23 16:42
 */
public interface IUserBiz {
    void login(Context context, String userName, String password, String tag, ICommonResult callback);
    void logout(Context context);
}
