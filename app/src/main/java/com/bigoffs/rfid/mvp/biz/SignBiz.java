package com.bigoffs.rfid.mvp.biz;

import android.content.Context;

import com.bigoffs.rfid.GlobalCfg;
import com.bigoffs.rfid.network.ICommonResult;
import com.bigoffs.rfid.network.PdaHttpUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @author okbuy-001
 * @desc
 * @time 2020/4/28 15:39
 */
public class SignBiz implements ISignBiz{
    @Override
    public void getAllocation(Context context, String type, String code, String tag, ICommonResult callback) {
        Map<String, String> params = new HashMap<>();
        switch (type){
            case "箱号":
                params.put("box_code",code);
            break;
            case "调拨单":
                params.put("allocation_code",code);
                break;
            case "店内码":
                params.put("incode",code);
                break;
        }

        PdaHttpUtil.post(context, GlobalCfg.rootUrl + "api/pda/task/allocation/get_allocation", params, tag, callback, false);
    }

    @Override
    public void partSign(Context context, String incodes, String rfids,String allocationCode, String tag, ICommonResult callback) {
        Map<String, String> params = new HashMap<>();
        params.put("incodes",incodes);
        params.put("allocation_code",allocationCode);
        params.put("rfid_codes",rfids);
        PdaHttpUtil.post(context, GlobalCfg.rootUrl + "api/pda/task/allocation/sign_incode", params, tag, callback, false);
    }

    @Override
    public void signAllocation(Context context, String allocationCode, String tag, ICommonResult callback) {
        Map<String, String> params = new HashMap<>();
        params.put("allocation_code",allocationCode);
        PdaHttpUtil.post(context, GlobalCfg.rootUrl + "api/pda/task/allocation/sign_allocation", params, tag, callback, false);
    }

    @Override
    public void getIncodeFromRfid(Context context, String rfid, String tag, ICommonResult callback) {
        Map<String, String> params = new HashMap<>();
        params.put("rfid_code",rfid);
        PdaHttpUtil.post(context, GlobalCfg.rootUrl + "api/pda/user/product/get_rfid_incode", params, tag, callback, false);
    }

}
