package com.bigoffs.rfid.mvp.bean;

import android.content.Context;


import com.bigoffs.rfid.GlobalCfg;
import com.bigoffs.rfid.network.ICommonResult;
import com.bigoffs.rfid.network.PdaHttpUtil;
import com.bigoffs.rfid.util.LogUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author okbuy-001
 * @desc
 * @time 2018/12/6 11:16
 */
public class GroundingBiz implements IGroundingBiz{
    @Override
    public void queryProduct(Context context, int type, String code, String tag, ICommonResult callback) {
        Map<String, String> params = new HashMap<>();
        params.put("type",type+"");
        params.put("code",code);
        PdaHttpUtil.post(context, GlobalCfg.rootUrl + "api/pda/task/storage/in_storage", params, tag, callback, false);
    }

    @Override
    public void receiveTask(Context context, String type, String code, String tag, ICommonResult callback) {
        Map<String, String> params = new HashMap<>();
        params.put(type,code);
        PdaHttpUtil.post(context, GlobalCfg.rootUrl + "api/pda/task/storage/get_task", params, tag, callback, false);
    }

    @Override
    public void receiveAllocationTask(Context context, String allocationId, String tag, ICommonResult callback) {
        Map<String, String> params = new HashMap<>();
        params.put("allocation_id",allocationId+"");
        PdaHttpUtil.post(context, GlobalCfg.rootUrl + "api/pda/task/storage/get_allocation_task", params, tag, callback, false);
    }

    @Override
    public void checkInCode(Context context, String arrivalId, String inCode, long time, String tag, ICommonResult callback) {
        Map<String, String> params = new HashMap<>();
        params.put("arrival_id",arrivalId);
        params.put("incode",inCode);
        PdaHttpUtil.post(context, GlobalCfg.rootUrl + "api/pda/task/storage/check_incode_info", params, tag, callback, false);
    }

    @Override
    public void checkBarCode(Context context, String arrivalId, String barCode, String tag, ICommonResult callback) {
        Map<String, String> params = new HashMap<>();
        params.put("arrival_id",arrivalId);
        params.put("barcode",barCode);
        PdaHttpUtil.post(context, GlobalCfg.rootUrl + "api/pda/task/storage/check_barcode_info", params, tag, callback, false);
    }

    @Override
    public void checkShelfCode(Context context, String shelfCode, String tag, ICommonResult callback) {
        Map<String, String> params = new HashMap<>();
        params.put("shelf",shelfCode);
        PdaHttpUtil.post(context, GlobalCfg.rootUrl + "api/pda/task/storage/check_shelf_info", params, tag, callback, false);

    }

    @Override
    public void upData(Context context, String arrivalId, String incodeList, String taskId, String tag, ICommonResult callback) {
        Map<String, String> params = new HashMap<>();
        params.put("arrival_id",arrivalId);
        params.put("incode_list",incodeList);
        params.put("task_id",taskId);
        PdaHttpUtil.post(context, GlobalCfg.rootUrl + "api/pda/task/storage/finish_batch_task", params, tag, callback, false);
    }

    @Override
    public void upAllocationData(Context context, String allocationId, String incodeList, String taskId, String tag, ICommonResult callback) {

    }

    @Override
    public void getIncodeFromRfid(Context context, String rfid, String tag, ICommonResult callback) {

    }

    @Override
    public void getAllocationData(Context context, String code, int type, String tag, ICommonResult callback) {

    }

    @Override
    public void upSignData(Context context, String shelf, String incodes, String rfids, String allocationCode, String tag, ICommonResult callback) {

    }


    /**
     * 获取签名后的URL
     *
     * @param context
     * @param url
     * @param params
     * @return
     */
    public static String getSignedUrl(Context context, String url,
                                      Map<String, String> params) {
        Map<String, String> signedParams =  params;
        StringBuilder sb = new StringBuilder(url);
        sb.append("?");
        String[] keys = (String[]) signedParams.keySet().toArray(new String[0]);
        Arrays.sort(keys);
        for (String key : keys) {
            sb.append(key + "=" + signedParams.get(key) + "&");
        }
        sb.deleteCharAt(sb.length() - 1);
        LogUtil.i("TAG", sb.toString());
        return sb.toString();
    }
}
