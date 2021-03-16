package com.bigoffs.rfid.mvp.biz;

import android.content.Context;


import com.bigoffs.rfid.GlobalCfg;
import com.bigoffs.rfid.network.ICommonResult;
import com.bigoffs.rfid.network.PdaHttpUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by okbuy on 17-3-25.
 */

public class PickingBiz implements IPickingBiz {
    @Override
    public void getPickingList(Context context, int page , int pageSize , String tag, ICommonResult callback) {
        Map<String, String> params = new HashMap<>();
        params.put("page_id", page+"");
        params.put("page_size", pageSize+"");

        PdaHttpUtil.post(context, GlobalCfg.rootUrl + "api/pda/task/rfid_picking/pick_list", params, tag, callback, false);
    }

    @Override
    public void collectPickingList(Context context, String code, String tag, ICommonResult callback) {
        Map<String, String> params = new HashMap<>();
        params.put("outstock_id", code);
        PdaHttpUtil.post(context, GlobalCfg.rootUrl + "api/pda/task/rfid_picking/get_task", params, tag, callback, false);
    }

    @Override
    public void getPickingdeatil(Context context, int taskId, String tag, ICommonResult callback) {
        Map<String, String> params = new HashMap<>();
        params.put("task_id", taskId + "");
        PdaHttpUtil.post(context, GlobalCfg.rootUrl + "api/pda/task/picking/task_info", params, tag, callback, false);
    }

    @Override
    public void scanIncodeOrShelf(Context context, int taskId, String incodeOrShelf, String tag, ICommonResult callback) {
        Map<String, String> params = new HashMap<>();
        params.put("task_id", taskId + "");
        params.put("product_incode", incodeOrShelf);
        PdaHttpUtil.post(context, GlobalCfg.rootUrl + "api/pda/task/picking/get_incode", params, tag, callback, false);
    }

    @Override
    public void finishPicking(Context context, int taskId, String rfids,String tag, ICommonResult callback) {
        Map<String, String> params = new HashMap<>();
        params.put("task_id", taskId + "");
        params.put("rfid_codes",rfids);
        PdaHttpUtil.post(context, GlobalCfg.rootUrl + "api/pda/task/rfid_picking/finish_pick", params, tag, callback, false);
    }

    @Override
    public void getPickingPreviewList(Context context, int taskId, String tag, ICommonResult callback) {
        Map<String, String> params = new HashMap<>();
        params.put("task_id", taskId + "");
        PdaHttpUtil.post(context, GlobalCfg.rootUrl + "api/pda/task/picking/preview", params, tag, callback, false);
    }

    @Override
    public void queryPicking(Context context, String outstockCode, String tag, ICommonResult callback) {
        Map<String, String> params = new HashMap<>();
        params.put("outstock_code", outstockCode);
        PdaHttpUtil.post(context, GlobalCfg.rootUrl + "api/pda/task/picking/query_pick", params, tag, callback, false);
    }
    //取消任务
    @Override
    public void cancelTask(Context context, String taskId, String outstockId, String tag, ICommonResult callback) {
        Map<String, String> params = new HashMap<>();
        params.put("task_id", taskId);
        params.put("outstock_id", outstockId);
        PdaHttpUtil.post(context, GlobalCfg.rootUrl + "api/pda/task/picking/cancel_pick", params, tag, callback, false);
    }

    @Override
    public void picking(Context context, String taskId, String productIncode, String tag, ICommonResult callback) {
        Map<String, String> params = new HashMap<>();
        params.put("task_id", taskId);
        params.put("product_incode", productIncode);
        PdaHttpUtil.post(context, GlobalCfg.rootUrl + "api/pda/task/picking/pick_out", params, tag, callback, false);
    }

    @Override
    public void changeIncode(Context context, String taskId, String trueIncode, String repIncode, String tag, ICommonResult callback) {
        Map<String, String> params = new HashMap<>();
        params.put("task_id", taskId);
        params.put("true_incode", trueIncode);
        params.put("rep_incode", repIncode);
        PdaHttpUtil.post(context, GlobalCfg.rootUrl + "api/pda/task/picking/replace_incode", params, tag, callback, false);
    }
    @Override
    public void queryBarCode(Context context, String productId, String barcode, String tag, ICommonResult callback) {
        Map<String, String> params = new HashMap<>();
        params.put("product_id", productId);
        params.put("barcode", barcode);
        PdaHttpUtil.post(context, GlobalCfg.rootUrl + "api/pda/task/picking/barcode_incode", params, tag, callback, false);
    }
}
