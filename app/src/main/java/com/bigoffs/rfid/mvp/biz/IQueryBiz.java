package com.bigoffs.rfid.mvp.biz;

import android.content.Context;

import com.bigoffs.rfid.network.ICommonResult;

/**
 * @author okbuy-001
 * @desc
 * @time 2020/4/24 16:10
 */
public interface IQueryBiz {
    void queryProduct(Context context, String incode, String tag, ICommonResult callback);
    void queryBarCode(Context context, String barcode, String tag, ICommonResult callback);
    void getIncodeFromRfid(Context context, String rfid, String tag, ICommonResult callback);
}
