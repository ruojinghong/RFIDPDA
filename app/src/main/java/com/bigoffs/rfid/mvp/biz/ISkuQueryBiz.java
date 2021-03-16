package com.bigoffs.rfid.mvp.biz;

import android.content.Context;

import com.bigoffs.rfid.network.ICommonResult;

/**
 * @author okbuy-001
 * @desc
 * @time 2020/4/28 16:55
 */
public interface ISkuQueryBiz {
    void querySkuInfo(Context context, String sku, String tag, ICommonResult callback);
    void queryIncodeListByShelf(Context context, String shelf, String tag, ICommonResult callback);

     void getIncodeFromRfid(Context context, String rfid, String tag, ICommonResult callback);
}
