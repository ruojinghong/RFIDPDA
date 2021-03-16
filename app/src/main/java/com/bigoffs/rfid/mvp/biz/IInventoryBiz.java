package com.bigoffs.rfid.mvp.biz;

import android.content.Context;

import com.bigoffs.rfid.network.ICommonResult;


/**
 * Created by okbuy on 17-2-28.
 */

public interface IInventoryBiz {
    void getShelfInfo(Context context, String shelfNumber, String tag, ICommonResult callback);
    void queryIncode(Context context, int taskId, String incode, String tag, ICommonResult callback);
    void finishInventory(Context context, int taskId, String tag, ICommonResult callback);
    void getInventoryInfo(Context context, int taskId, String tag, ICommonResult callback);
    void getInventoryIncodes(Context context, int taskId, int status, String tag, ICommonResult callback);
    void getShelfCodeInfo(Context context, String shelf, String tag, ICommonResult callback);
    void checkShelfCodeInfo(Context context, String shelf, String incode, String tag, ICommonResult callback);
    void submitInventory(Context context, String inventoryList, String tag, ICommonResult callback);
    void getInventoryList(Context context,String tag,ICommonResult callback);
    void receiveTask(Context context, String taskId, String tag, ICommonResult callback);
    void finishRfidTask(Context context,String taskId,String rfids,String tag,ICommonResult callback);
}
