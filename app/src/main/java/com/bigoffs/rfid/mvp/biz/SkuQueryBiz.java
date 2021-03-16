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
 * @time 2020/4/28 16:55
 */
public class SkuQueryBiz implements ISkuQueryBiz {
    @Override
    public void querySkuInfo(Context context, String sku, String tag, ICommonResult callback) {
        Map<String ,String > params = new HashMap<>();
        params.put("product_id",sku);
        PdaHttpUtil.post(context, GlobalCfg.rootUrl + "api/pda/user/product/get_sku_info", params, tag, callback, false);
    }

    @Override
    public void queryIncodeListByShelf(Context context, String shelf, String tag, ICommonResult callback) {
        Map<String ,String > params = new HashMap<>();
        params.put("shelf",shelf);
        PdaHttpUtil.post(context, GlobalCfg.rootUrl + "api/pda/user/product/get_shelf", params, tag, callback, false);
    }

    @Override
    public void getIncodeFromRfid(Context context, String rfid, String tag, ICommonResult callback) {
            Map<String, String> params = new HashMap<>();
            params.put("rfid_code",rfid);
            PdaHttpUtil.post(context, GlobalCfg.rootUrl + "api/pda/user/product/get_rfid_incode", params, tag, callback, false);
    }


}
