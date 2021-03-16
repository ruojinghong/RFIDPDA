package com.bigoffs.rfid.mvp.biz;

import android.content.Context;


import com.bigoffs.rfid.GlobalCfg;
import com.bigoffs.rfid.network.ICommonResult;
import com.bigoffs.rfid.network.PdaHttpUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by okbuy on 17-2-28.
 */

public class InventoryBiz implements IInventoryBiz{

    @Override
    public void getShelfInfo(Context context, String shelfNumber, String tag, ICommonResult callback) {
        Map<String, String> params = new HashMap<>();
        params.put("shelf_num", shelfNumber);
        PdaHttpUtil.post(context, GlobalCfg.rootUrl + "api/pda/task/Inventory/get_shelf", params, tag, callback, false);
    }

    @Override
    public void queryIncode(Context context, int taskId, String incode, String tag, ICommonResult callback) {
        Map<String, String> params = new HashMap<>();
        params.put("task_id", taskId + "");
        params.put("product_incode", incode);
        PdaHttpUtil.post(context, GlobalCfg.rootUrl + "api/pda/task/Inventory/get_incode", params, tag, callback, false);
    }

    @Override
    public void finishInventory(Context context, int taskId, String tag, ICommonResult callback) {
        Map<String, String> params = new HashMap<>();
        params.put("task_id", taskId + "");
        PdaHttpUtil.post(context, GlobalCfg.rootUrl + "api/pda/task/Inventory/task_finish", params, tag, callback, false);
    }

    @Override
    public void getInventoryInfo(Context context, int taskId, String tag, ICommonResult callback) {
        Map<String, String> params = new HashMap<>();
        params.put("task_id", taskId + "");
        PdaHttpUtil.post(context, GlobalCfg.rootUrl + "api/pda/task/Inventory/task_info", params, tag, callback, false);
    }

    @Override
    public void getInventoryIncodes(Context context, int taskId, int status, String tag, ICommonResult callback) {
        Map<String, String> params = new HashMap<>();
        params.put("task_id", taskId + "");
        params.put("check_status", status + "");
        PdaHttpUtil.post(context, GlobalCfg.rootUrl + "api/pda/task/Inventory/task_status", params, tag, callback, false);
    }

    public void getShelfCodeInfo(Context context, String shelf, String tag, ICommonResult callback){
        Map<String, String> params = new HashMap<>();
        params.put("shelf", shelf);
        PdaHttpUtil.post(context, GlobalCfg.rootUrl + "api/pda/task/inventory/check_shelf_info", params, tag, callback, false);
    }
    public void checkShelfCodeInfo(Context context, String shelf, String incode, String tag, ICommonResult callback){
        Map<String, String> params = new HashMap<>();
        params.put("shelf", shelf);
        params.put("incode", incode);
        PdaHttpUtil.post(context, GlobalCfg.rootUrl + "api/pda/task/inventory/check_shelf_incode", params, tag, callback, false);
    }

    @Override
    public void submitInventory(Context context, String inventoryList, String tag, ICommonResult callback) {
        Map<String, String> params = new HashMap<>();
        params.put("inventory_list", inventoryList);
        PdaHttpUtil.post(context, GlobalCfg.rootUrl + "api/pda/task/inventory/finish_inventory", params, tag, callback, false);
    }

    @Override
    public void getInventoryList(Context context, String tag, ICommonResult callback) {
        Map<String, String> params = new HashMap<>();
        PdaHttpUtil.post(context, GlobalCfg.rootUrl + "api/pda/task/rfid_inventory/task_list", params, tag, callback, false);

    }
    @Override
    public void receiveTask(Context context, String taskID, String tag, ICommonResult callback) {
        Map<String, String> params = new HashMap<>();
        params.put("task_id",taskID);
        PdaHttpUtil.post(context, GlobalCfg.rootUrl + "api/pda/task/rfid_inventory/get_task", params, tag, callback, false);
    }

    @Override
    public void finishRfidTask(Context context, String taskId, String rfids, String tag, ICommonResult callback) {
        Map<String, String> params = new HashMap<>();
        params.put("task_id",taskId);
        params.put("rfid_codes",rfids);
        PdaHttpUtil.post(context, GlobalCfg.rootUrl + "api/pda/task/rfid_inventory/finish_inventory", params, tag, callback, false);
    }

}
