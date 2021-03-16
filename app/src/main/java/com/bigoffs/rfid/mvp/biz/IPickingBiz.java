package com.bigoffs.rfid.mvp.biz;

import android.content.Context;

import com.bigoffs.rfid.network.ICommonResult;


/**
 * Created by okbuy on 17-3-25.
 */

public interface IPickingBiz {
    void getPickingList(Context context, int page, int pageSize, String tag, ICommonResult callback);
    void collectPickingList(Context context, String code, String tag, ICommonResult callback);
    void getPickingdeatil(Context context, int taskId, String tag, ICommonResult callback);
    void scanIncodeOrShelf(Context context, int taskId, String incodeOrShelf, String tag, ICommonResult callback);
    void finishPicking(Context context, int taskId, String rfids,String tag, ICommonResult callback);
    void getPickingPreviewList(Context context, int taskId, String tag, ICommonResult callback);
    void queryPicking(Context context, String outstockCode, String tag, ICommonResult callback);

    void cancelTask(Context context, String taskId, String outstockId, String tag, ICommonResult callback);
    void picking(Context context, String taskId, String productIncode, String tag, ICommonResult callback);
    void changeIncode(Context context, String taskId, String trueIncode, String repIncode, String tag, ICommonResult callback);
    void queryBarCode(Context context, String product_id, String barcode, String tag, ICommonResult callback);
}
