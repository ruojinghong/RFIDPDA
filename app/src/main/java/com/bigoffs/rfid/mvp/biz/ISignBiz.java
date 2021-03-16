package com.bigoffs.rfid.mvp.biz;

import android.content.Context;

import com.bigoffs.rfid.network.ICommonResult;

/**
 * @author okbuy-001
 * @desc
 * @time 2020/4/28 15:35
 */
public interface ISignBiz {
    void getAllocation(Context context, String type,String incode, String tag, ICommonResult callback);
    void partSign(Context context, String incodes, String rfids,String allocationCode, String tag, ICommonResult callback);
    void signAllocation(Context context, String allocationCode, String tag, ICommonResult callback);
    void getIncodeFromRfid(Context context, String rfid, String tag, ICommonResult callback);

}
