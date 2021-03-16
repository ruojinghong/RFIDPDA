package com.bigoffs.rfid.mvp.bean;

import android.content.Context;

import com.bigoffs.rfid.network.ICommonResult;

/**
 * @author okbuy-001
 * @desc
 * @time 2018/12/6 11:11
 */
public interface IGroundingBiz {
    void queryProduct(Context context, int type, String code, String tag, ICommonResult callback);
    void receiveTask(Context context, String type, String code, String tag, ICommonResult callback);
    void receiveAllocationTask(Context context, String allocationId, String tag, ICommonResult callback);
    void checkInCode(Context context, String arrivalId, String inCode, long time, String tag, ICommonResult callback);
    void checkBarCode(Context context, String arrivalId, String barCode, String tag, ICommonResult callback);
    void checkShelfCode(Context context, String shelfCode, String tag, ICommonResult callback);
    void upData(Context context, String arrival_id, String incode_list, String taskId, String tag, ICommonResult callback);
    void upAllocationData(Context context, String allocationId, String incodeList, String taskId, String tag, ICommonResult callback);
    void getIncodeFromRfid(Context context, String rfid, String tag, ICommonResult callback);
    void getAllocationData(Context context, String code, int  type, String tag, ICommonResult callback);
    void upSignData(Context context,String shelf,String incodes,String rfids,String allocationCode,String tag,ICommonResult callback);
}
