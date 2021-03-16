package com.bigoffs.rfid.network;

import com.bigoffs.rfid.util.LogUtil;
import com.zhy.http.okhttp.callback.Callback;

import okhttp3.Response;

/**
 * @author okbuy-001
 * @desc
 * @time 2018/12/10 11:48
 */
public abstract class MyStringCallback extends Callback<String> {
    @Override
    public boolean validateReponse(Response response, int id) {
        return true;
    }

    @Override
    public String parseNetworkResponse(Response response, int id) throws Exception {
        LogUtil.i("fsdfsafafd","code is:"+response.code()+"\n"+response.body().string());
        if(response.code()>=200 && response.code()<300){
            return response.body().string();
        }else{
            throw new Exception("code is:"+response.code()+"\n"+response.body().string());
        }

    }

}

