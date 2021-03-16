package com.bigoffs.rfid.network;


import okhttp3.Call;

public interface ICommonResult {
    void onSuccess(String result);

    void onFail(Call call, Exception e);

    void onInterrupt(int code, String message);
}
