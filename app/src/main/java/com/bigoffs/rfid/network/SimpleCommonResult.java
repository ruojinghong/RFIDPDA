package com.bigoffs.rfid.network;

import okhttp3.Call;

/**
 * Created by okbuy on 17-4-7.
 */

public class SimpleCommonResult implements ICommonResult{
    @Override
    public void onSuccess(String result) {

    }

    @Override
    public void onFail(Call call, Exception e) {

    }

    @Override
    public void onInterrupt(int code, String message) {

    }
}
