package com.bigoffs.rfid.network;

import android.app.Activity;
import android.content.Context;

import com.bigoffs.rfid.util.LogUtil;
import com.bigoffs.rfid.util.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.lang.reflect.Type;

import okhttp3.Call;

public class ResultCallback extends StringCallback {

    private Context mContext;

    private ICommonResult mCallback;

    private boolean mDomyself;

    public ResultCallback(Context context, ICommonResult callback) {
        this(context, callback, false);
    }

    public ResultCallback(Context context, ICommonResult callback, boolean domyself) {
        mContext = context;
        mCallback = callback;
        mDomyself = domyself;
    }

    @Override
    public void onError(Call arg0, Exception arg1, int arg2) {
        if (mContext != null && mContext instanceof Activity) {
            if (((Activity) mContext).isFinishing()) {
                return;
            }
        }
        arg1.getMessage();
        mCallback.onFail(arg0, arg1);
//        ToastUtil.showToast(mContext, "请检查网络连接");
        ToastUtil.showToast(mContext, arg1.getMessage());
    }

    @Override
    public void onResponse(String arg0, int arg1) {
        if (mContext != null && mContext instanceof Activity) {
            if (((Activity) mContext).isFinishing()) {
                return;
            }
        }
        Gson gson = new Gson();
        Type type = new TypeToken<CommonResult>() {
        }.getType();
        CommonResult result;
        try {
            LogUtil.i("------------resp--------",arg0);
            result = gson.fromJson(arg0, type);
            result.json = new JSONObject(arg0).optString("result");
        } catch (Exception e) {
            mCallback.onFail(null, e);
            ToastUtil.showToast(mContext, "解析异常");
            return;
        }
        if (result.isSuccess()) {
            mCallback.onSuccess(result.getResult());
        } else {
            // 从这里判断是不是为1，强制更新
            if (result.force == 1) {
//                new UpdateBiz().getVersionInfo(mContext, new ICommonResult() {
//                    @Override
//                    public void onSuccess(String result) {
//                        Gson gson = new Gson();
//                        VersionInfo info = gson.fromJson(result, VersionInfo.class);
//                        new UpdateManager(mContext, info, (BaseActivity) mContext).check();
//                    }
//
//                    @Override
//                    public void onFail(Call call, Exception e) {
//
//                    }
//
//                    @Override
//                    public void onInterrupt(int code, String message) {
//
//                    }
//                });
            }
            if (mDomyself) {
                // 回调自己处理，错误信息也由回调选择是否提示
                mCallback.onInterrupt(result.error.code, result.getErrorMessage());
            } else {
                ToastUtil.showToast(mContext, result.getErrorMessage());
                mCallback.onInterrupt(result.error.code, result.getErrorMessage());
            }
        }
    }
}

