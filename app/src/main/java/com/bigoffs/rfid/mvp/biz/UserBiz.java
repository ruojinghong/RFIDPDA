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
 * @time 2020/4/23 16:44
 */
public class UserBiz implements IUserBiz{
    @Override
    public void login(Context context, String userName, String password, String tag, ICommonResult callback) {
        Map<String, String> params = new HashMap<>();
        params.put("user_name", userName);
        params.put("password", password);
        PdaHttpUtil.post(context, GlobalCfg.rootUrl + "api/pda/user/user/login", params, tag, callback, false);
    }

    @Override
    public void logout(Context context) {
        PdaHttpUtil.postWithoutCallabck(context, GlobalCfg.rootUrl + "api/pda/user/user/logout", new HashMap<String, String>());
    }
}
