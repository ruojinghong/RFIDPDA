package com.bigoffs.rfid.mvp.biz;

import android.content.Context;

import com.bigoffs.rfid.GlobalCfg;
import com.bigoffs.rfid.network.ICommonResult;
import com.bigoffs.rfid.network.PdaHttpUtil;
import com.bigoffs.rfid.util.CodeTypeUtils;

import org.apache.commons.lang3.reflect.TypeUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author okbuy-001
 * @desc
 * @time 2020/4/24 16:11
 */
public class QueryBiz implements IQueryBiz{
    @Override
    public void queryProduct(Context context, String incode, String tag, ICommonResult callback) {
        Map<String, String> params = new HashMap<>();
        if(CodeTypeUtils.isIncodeOrEpc(incode)){
            params.put("product_incode", incode);
        }else {
            params.put("rfid_code", incode);

        }
        PdaHttpUtil.post(context, GlobalCfg.rootUrl + "api/pda/user/product/get_incode", params, tag, callback, false);
    }
    @Override
    public void queryBarCode(Context context, String barcode, String tag, ICommonResult callback) {
        Map<String, String> params = new HashMap<>();
        params.put("barcode", barcode);
        PdaHttpUtil.post(context, GlobalCfg.rootUrl + "api/pda/user/product/get_barcode", params, tag, callback, false);
    }

    @Override
    public void getIncodeFromRfid(Context context, String rfid, String tag, ICommonResult callback) {
        Map<String, String> params = new HashMap<>();
        params.put("rfid_code",rfid);
        PdaHttpUtil.post(context, GlobalCfg.rootUrl + "api/pda/user/product/get_rfid_incode", params, tag, callback, false);
    }
}
