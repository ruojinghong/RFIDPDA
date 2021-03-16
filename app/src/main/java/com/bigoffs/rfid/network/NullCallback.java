package com.bigoffs.rfid.network;

import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
 * Created by okbuy on 17-2-9.
 */

public class NullCallback extends StringCallback {
    @Override
    public void onError(Call call, Exception e, int id) {
        // do nothing
    }

    @Override
    public void onResponse(String response, int id) {
        // do nothing
    }
}
