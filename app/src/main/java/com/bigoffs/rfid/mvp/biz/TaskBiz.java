package com.bigoffs.rfid.mvp.biz;

import android.content.Context;

import com.bigoffs.rfid.GlobalCfg;
import com.bigoffs.rfid.network.ICommonResult;
import com.bigoffs.rfid.network.PdaHttpUtil;


import java.util.HashMap;
import java.util.Map;

/**
 * Created by okbuy on 17-2-21.
 */

public class TaskBiz {
    // 查询任务单列表
    public void query(Context context, int type, int pageId, String tag, ICommonResult callback) {
        Map<String, String> params = new HashMap<>();
        params.put("task_status", type + "");
        params.put("page_id", pageId + "");
        params.put("page_size", GlobalCfg.PAGE_SIZE);
        PdaHttpUtil.post(context, GlobalCfg.rootUrl + "api/pda/task/task/get_tasks", params, tag, callback, false);
    }
    public void queryPickingTask(Context context, String taskId, String tag, ICommonResult callback) {
        Map<String, String> params = new HashMap<>();
        params.put("task_id", taskId);
        PdaHttpUtil.post(context, GlobalCfg.rootUrl + "api/pda/task/picking/get_picking_info", params, tag, callback, false);
    }

    public void queryArrivalTask(Context context, String taskId, String tag, ICommonResult callback) {
        Map<String, String> params = new HashMap<>();
        params.put("task_id", taskId);
        PdaHttpUtil.post(context, GlobalCfg.rootUrl + "api/pda/task/storage/get_instocking", params, tag, callback, false);
    }
    public void queryAllocationTask(Context context, String taskId, String tag, ICommonResult callback) {
        Map<String, String> params = new HashMap<>();
        params.put("task_id", taskId);
        PdaHttpUtil.post(context, GlobalCfg.rootUrl + "api/pda/task/storage/get_allocationing", params, tag, callback, false);
    }
}
