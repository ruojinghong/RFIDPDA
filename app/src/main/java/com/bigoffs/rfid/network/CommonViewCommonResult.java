package com.bigoffs.rfid.network;



import com.bigoffs.rfid.mvp.view.ICommonView;

import okhttp3.Call;


/**
 * Created by okbuy on 17-4-7.
 */

public class CommonViewCommonResult extends SimpleCommonResult {

    private ICommonView mView;

    public CommonViewCommonResult(ICommonView view) {
        mView = view;
    }

    @Override
    public void onSuccess(String result) {
        mView.hideLoading();
    }

    @Override
    public void onFail(Call call, Exception e) {
        mView.hideLoading();
    }

    @Override
    public void onInterrupt(int code, String message) {
        mView.hideLoading();
    }
}
