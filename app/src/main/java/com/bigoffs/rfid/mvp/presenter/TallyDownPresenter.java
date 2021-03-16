package com.bigoffs.rfid.mvp.presenter;

import android.app.AlertDialog;
import android.content.Context;

import android.content.DialogInterface;
import android.text.TextUtils;


import com.bigoffs.rfid.mvp.bean.TallyProductInfo;
import com.bigoffs.rfid.mvp.biz.ITallyBiz;
import com.bigoffs.rfid.mvp.biz.TallyBiz;
import com.bigoffs.rfid.mvp.view.ITallyDownView;
import com.bigoffs.rfid.network.ICommonResult;
import com.google.gson.Gson;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by okbuy on 17-2-17.
 */

public class TallyDownPresenter implements ITallyDownPresenter {
    // Model
    private ITallyBiz mBiz;
    // View
    private ITallyDownView mView;
    // Data
    private List<TallyProductInfo> mData;
    private List<String> mIncodes;
    private Context mContext;

    public TallyDownPresenter(Context context, ITallyDownView view) {
        mContext = context;
        mView = view;
        mBiz = new TallyBiz();
        mData = new ArrayList<>();
        mIncodes = new ArrayList<>();
    }

    @Override
    public void query(final String incode) {
        if (TextUtils.isEmpty(incode)) {
            mView.showNotice("店内码不能为空");
            return;
        }
        if (mIncodes.contains(incode)) {
            mView.showNotice("该店内码已扫描");
            // 已扫描的店内码也要绿色显示
            mView.setRightIncode(incode);
            // 显示移除按钮，并将该店内码放到店内码集合的最后
            mView.showOrHideRemoveBtn(true);
            moveToLast(incode);
            mView.clear();
            return;
        }
        mView.showLoading("");
        mBiz.queryDownIncode(mContext, incode, "TallyDownActivity", new ICommonResult() {
            @Override
            public void onSuccess(String result) {
                mView.hideLoading();
                Gson gson = new Gson();
                TallyProductInfo info = gson.fromJson(result, TallyProductInfo.class);
                mData.add(info);
                mIncodes.add(incode);
                // 显示正确的店内码
                mView.setRightIncode(incode);
                // 已扫描数+1
                mView.showCount(mData.size());
                // 显示移除店内码按钮
                mView.showOrHideRemoveBtn(true);
                // 清空输入框
                mView.clear();
            }

            @Override
            public void onFail(Call call, Exception e) {
                mView.hideLoading();
            }

            @Override
            public void onInterrupt(int code, String message) {
                mView.hideLoading();
                // 红色显示店内码
                mView.setWrongIncode(incode);
                // 隐藏移除店内码的按钮
                mView.showOrHideRemoveBtn(false);
                // 播放错误声
                mView.playBeep();
            }
        });
    }

    @Override
    public void clear() {
        new AlertDialog.Builder(mContext).setTitle("确定清空").setMessage("将清除所有已扫描的店内码").setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mData.clear();
                mIncodes.clear();
                // 隐藏移除按钮
                mView.showOrHideRemoveBtn(false);
                // 店内码显示重置
                mView.resetIncode();
                // 数量归零
                mView.showCount(0);
                // 输入框清空
                mView.clear();
            }
        }).setNegativeButton("取消",null).show();
    }

    // 将该店内码放到店内码集合的最后
    private void moveToLast(String incode) {
        mIncodes.remove(incode);
        mIncodes.add(incode);
        TallyProductInfo tallyProductInfo = null;
        for (TallyProductInfo info : mData
                ) {
            // 忽略大小写
            if (incode.equalsIgnoreCase(info.incode)) {
                tallyProductInfo = info;
            }
        }
        mData.remove(tallyProductInfo);
        mData.add(tallyProductInfo);
    }

    @Override
    public void remove() {
        // 移除最后一个店内码
        mData.remove(mData.size() - 1);
        mIncodes.remove(mIncodes.size() - 1);
        if (mData.size() > 0) {
            // 显示移除后上一个的店内码
            mView.setRightIncode(mData.get(mData.size() - 1).incode);
        } else {
            // 隐藏移除按钮
            mView.showOrHideRemoveBtn(false);
            // 重置店内码显示
            mView.resetIncode();
        }
        // 更新数量
        mView.showCount(mData.size());
    }

    @Override
    public void down() {
        if (mData.size() <= 0) {
            mView.showNotice("没有可以下架的店内码");
            return;
        }

        new AlertDialog.Builder(mContext).setTitle("确定清空").setMessage("将清除所有已扫描的店内码").setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                handleDown();
            }
        }).setNegativeButton("取消",null).show();


    }

    // 处理下架货品的点击事件
    private void handleDown() {
        List<String> incodes = new ArrayList<>();
        for (TallyProductInfo info :
                mData) {
            incodes.add(info.incode);
        }
        mView.showLoading("");
        mBiz.down(mContext, incodes, "TallyDownActivity", new ICommonResult() {
            @Override
            public void onSuccess(String result) {
                mView.hideLoading();
                try {
                    mView.showNotice("下架成功");
                    JSONObject json = new JSONObject(result);
                    mView.goTallyUp(json.optInt("task_id"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(Call call, Exception e) {
                mView.hideLoading();
            }

            @Override
            public void onInterrupt(int code, String message) {
                mView.hideLoading();
            }
        });
    }

    @Override
    public void goTallyDetail() {
        if (mData.size() > 0) {
            mView.goTallyDetail();
        } else {
            mView.showNotice("没有可以查看的店内码");
        }
    }

    @Override
    public List<TallyProductInfo> getData() {
        return mData;
    }
}
