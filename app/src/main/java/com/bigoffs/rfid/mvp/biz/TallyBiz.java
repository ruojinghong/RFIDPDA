package com.bigoffs.rfid.mvp.biz;

import android.content.Context;

import com.bigoffs.rfid.GlobalCfg;
import com.bigoffs.rfid.network.ICommonResult;
import com.bigoffs.rfid.network.PdaHttpUtil;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by okbuy on 17-2-17.
 */

public class TallyBiz implements ITallyBiz {
    @Override
    public void queryDownIncode(Context context, String incode, String tag, ICommonResult callback) {
        Map<String, String> params = new HashMap<>();
        params.put("product_incode", incode);
        PdaHttpUtil.post(context, GlobalCfg.rootUrl + "api/pda/task/transfer/get_incode", params, tag, callback, false);
    }

    @Override
    public void down(Context context, List<String> incodes, String tag, ICommonResult callback) {
        Map<String, String> params = new HashMap<>();
        params.put("product_incodes", incodes.toString());
        PdaHttpUtil.post(context, GlobalCfg.rootUrl + "api/pda/task/transfer/incode_off", params, tag, callback, false);
    }

    @Override
    public void queryTaskInfo(Context context, int taskId, String tag, ICommonResult callback) {
        Map<String, String> params = new HashMap<>();
        params.put("task_id", taskId + "");
        PdaHttpUtil.post(context, GlobalCfg.rootUrl + "api/pda/task/transfer/task_info", params, tag, callback, false);
    }

    @Override
    public void queryUpIncode(Context context, String incode, int taskId, String tag, ICommonResult callback) {
        Map<String, String> params = new HashMap<>();
        params.put("product_incode", incode);
        params.put("task_id", taskId + "");
        PdaHttpUtil.post(context, GlobalCfg.rootUrl + "api/pda/task/transfer/on_get_incode", params, tag, callback, false);
    }

    @Override
    public void up(Context context, String shelfNumber, List<String> incodes, int taskId, String tag, ICommonResult callback) {
        Map<String, String> params = new HashMap<>();
        params.put("shelf_num", shelfNumber);
        params.put("product_incodes", incodes.toString());
        params.put("task_id", taskId + "");
        PdaHttpUtil.post(context, GlobalCfg.rootUrl + "api/pda/task/transfer/incode_on", params, tag, callback, false);
    }

    @Override
    public void queryShelfcode(Context context, String shelf, String tag, ICommonResult callback) {
        Map<String, String> params = new HashMap<>();
        params.put("shelf", shelf);
        PdaHttpUtil.post(context, GlobalCfg.rootUrl + "/api/pda/task/transfer/check_shelf_info", params, tag, callback, false);
    }

    @Override
    public void queryIncode(Context context, String inCode, String tag, ICommonResult callback) {
        Map<String, String> params = new HashMap<>();
        params.put("incode", inCode);
        PdaHttpUtil.post(context, GlobalCfg.rootUrl + "/api/pda/task/transfer/get_incode", params, tag, callback, false);
    }

    @Override
    public void transferShelf(Context context, String shelfIncodes, String tag, ICommonResult callback) {
        Map<String, String> params = new HashMap<>();
        params.put("shelf_incodes", shelfIncodes);
        PdaHttpUtil.post(context, GlobalCfg.rootUrl + "/api/pda/task/transfer/finish_tansfer", params, tag, callback, false);
    }

    @Override
    public void transferShelfNew(Context context, String shelf, String incodes, String rfids, String tag, ICommonResult callback) {
        Map<String, String> params = new HashMap<>();
        params.put("shelf_code", shelf);
        params.put("incodes", incodes);
        params.put("rfid_codes", rfids);
        PdaHttpUtil.post(context, GlobalCfg.rootUrl + "api/pda/task/rfid_transfer/finish_tansfer", params, tag, callback, false);
    }

}
