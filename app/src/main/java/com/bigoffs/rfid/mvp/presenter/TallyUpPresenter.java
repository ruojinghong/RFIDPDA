package com.bigoffs.rfid.mvp.presenter;

import android.app.AlertDialog;
import android.content.Context;

import android.content.DialogInterface;
import android.text.TextUtils;


import com.bigoffs.rfid.mvp.bean.TallyProductInfo;
import com.bigoffs.rfid.mvp.bean.TallyTaskInfo;
import com.bigoffs.rfid.mvp.biz.ITallyBiz;
import com.bigoffs.rfid.mvp.biz.TallyBiz;
import com.bigoffs.rfid.mvp.view.ITallyUpView;
import com.bigoffs.rfid.network.ICommonResult;
import com.google.gson.Gson;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by okbuy on 17-2-20.
 */

public class TallyUpPresenter implements ITallyUpPresenter {

    // Model
    private ITallyBiz mBiz;
    // View
    private ITallyUpView mView;

    private Context mContext;
    // 店内码
    private List<String> mIncodes;
    // 货架号
    private String mShelfNumber;
    // 是否已扫描货架号
    private boolean mHasShelfNumber;
    // 理货任务单详情
    private TallyTaskInfo mInfo;

    public TallyUpPresenter(Context context, ITallyUpView view) {
        mContext = context;
        mView = view;
        mBiz = new TallyBiz();
        mIncodes = new ArrayList<>();
    }

    @Override
    public void queryTaskInfo() {
        mView.showLoading("");
        mBiz.queryTaskInfo(mContext, mView.getTallyTaskId(), "TallyUpActivity", new ICommonResult() {
            @Override
            public void onSuccess(String result) {
                mView.hideLoading();
                Gson gson = new Gson();
                mInfo = gson.fromJson(result, TallyTaskInfo.class);
                mView.setTaskInfo(mInfo);
                if (isTaskFinished()) {
                    mView.showNotice("该任务单已完成");
                    mView.finishUp();
                } else {
                    handleClear();
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
    public boolean isTaskFinished() {
        if (mInfo != null) {
            return mInfo.status == 1;
        }
        return true;
    }

    @Override
    public void queryUpIncode(final String incode) {
        if (TextUtils.isEmpty(incode)) {
            mView.showNotice("店内码不能为空");
            return;
        }
        if (mIncodes.contains(incode)) {
            mView.showNotice("该店内码已扫描");
            // 已扫描的店内码也要绿色显示
            mView.setRightIncode(incode);
            // 显示移除按钮
            mView.showOrHideRemoveBtn(true);
            moveToLast(incode);
            mView.clearEdit();
            return;
        }
        mView.showLoading("");
        mBiz.queryUpIncode(mContext, incode, mView.getTallyTaskId(), "TallyUpActivity", new ICommonResult() {
            @Override
            public void onSuccess(String result) {
                mView.hideLoading();
                mIncodes.add(incode);
                // 显示正确的店内码
                mView.setRightIncode(incode);
                // 已扫描数+1
                mView.showCount(mIncodes.size());
                // 显示移除店内码按钮
                mView.showOrHideRemoveBtn(true);
                mView.clearEdit();
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

    // 将该店内码放到店内码集合的最后
    private void moveToLast(String incode) {
        mIncodes.remove(incode);
        mIncodes.add(incode);
    }

    @Override
    public boolean hasShelfNumber() {
        return mHasShelfNumber;
    }

    @Override
    public void setShelfNumber(String shelfNumber) {
        if (TextUtils.isEmpty(shelfNumber)) {
            mView.showNotice("货架号不能为空");
            return;
        }
        mShelfNumber = shelfNumber;
        mHasShelfNumber = true;
        mView.setShelfNumber(shelfNumber);
        mView.initIncodeEdit();
    }

    @Override
    public void clear() {

       new AlertDialog.Builder(mContext).setTitle("确定清空").setMessage("将清除所有已扫描的店内码").setPositiveButton("确定", new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialog, int which) {
               handleClear();
           }
       }).setNegativeButton("取消",null).show();
    }

    private void handleClear() {
        // 清空店内码集合
        mIncodes.clear();
        // 隐藏移除按钮
        mView.showOrHideRemoveBtn(false);
        // 店内码显示重置
        mView.resetIncode();
        // 数量归零
        mView.showCount(0);
        // 输入框清空
        mView.clear();
        // 货架号清空
        mHasShelfNumber = false;
        mShelfNumber = "";
    }

    @Override
    public void remove() {
        // 移除最后一个店内码
        mIncodes.remove(mIncodes.size() - 1);
        if (mIncodes.size() > 0) {
            // 显示移除后上一个的店内码
            mView.setRightIncode(mIncodes.get(mIncodes.size() - 1));
        } else {
            // 隐藏移除按钮
            mView.showOrHideRemoveBtn(false);
            // 重置店内码显示
            mView.resetIncode();
        }
        // 更新数量
        mView.showCount(mIncodes.size());
    }

    @Override
    public void up() {
        if (mIncodes.size() <= 0) {
            mView.showNotice("没有可以上架的店内码");
            return;
        }


        new AlertDialog.Builder(mContext).setTitle("确定清空").setMessage("将清除所有已扫描的店内码").setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                handleUp();
            }
        }).setNegativeButton("取消",null).show();

    }

    @Override
    public void checkDetail() {
        // 查询出已扫描的店内码集合
        Map<String, TallyProductInfo> map = new HashMap<>();
        for (TallyProductInfo info : mInfo.downList
                ) {
            if (containsIgnoreCase(mIncodes, info.incode)) {
                map.put(info.incode.toUpperCase(), info);
            }
        }
        // 根据已扫描的店内码顺序排序
        List<TallyProductInfo> infos = new ArrayList<>();
        for (String incode : mIncodes
                ) {
            infos.add(map.get(incode.toUpperCase()));
        }
        checkIncode(infos);
    }

    private boolean containsIgnoreCase(List<String> list, String string) {
        for (String str : list
                ) {
            if (str.equalsIgnoreCase(string)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void checkDown() {
        checkIncode(mInfo.downList);
    }

    @Override
    public void checkUp() {
        checkIncode(mInfo.upList);
    }

    @Override
    public void checkRemain() {
        checkIncode(mInfo.remainList);
    }

    private void checkIncode(List<TallyProductInfo> infos) {
        if (infos == null || infos.size() == 0) {
            mView.showNotice("没有可以查看的店内码");
            return;
        }
//        TallyDetailActivity.show(mContext, infos);
    }

    // 处理上架货品的点击事件
    private void handleUp() {
        mView.showLoading("");
        mBiz.up(mContext, mShelfNumber, mIncodes, mView.getTallyTaskId(), "TallyUpActivity", new ICommonResult() {
            @Override
            public void onSuccess(String result) {
                mView.hideLoading();
                mView.showNotice("上架成功");
                // 上架成功刷新页面
                queryTaskInfo();
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
}
