package com.bigoffs.rfid.mvp.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.bigoffs.rfid.mvp.bean.GroundingBillList;
import com.bigoffs.rfid.mvp.bean.GroundingBiz;
import com.bigoffs.rfid.mvp.bean.IGroundingBiz;
import com.bigoffs.rfid.mvp.view.IGroundingView;
import com.bigoffs.rfid.network.CommonViewCommonResult;
import com.bigoffs.rfid.network.ICommonResult;
import com.google.gson.Gson;


import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;

/**
 * @author okbuy-001
 * @desc
 * @time 2018/12/6 10:56
 */
public class GroundingPresenter implements IGroundingPresenter {
    public final  static String TAG ="GroundingActitity";
    private IGroundingView mView;

    private Context mContext;

    private IGroundingBiz mBiz;
    private int type;

    //到货单列表
    private GroundingBillList bill;
    public GroundingPresenter(Context context, IGroundingView groundingView){
        this.mContext = context;
        mView = groundingView;
        mBiz = new GroundingBiz();
    }
    @Override
    public void query() {
        if (TextUtils.isEmpty(mView.getIncode().trim())) {
            mView.showNotice(mView.getChoose()+"不能为空！");
            return;
        }
        mView.showLoading("加载中..");
        mBiz.queryProduct(mContext,getType(),mView.getIncode(),TAG,new CommonViewCommonResult(mView){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                mView.setIncode(mView.getIncode());
                Gson gson = new Gson();
                bill = gson.fromJson(result,GroundingBillList.class);
                mView.showList(bill);
            }

            @Override
            public void onFail(Call call, Exception e) {
                super.onFail(call, e);
            }

            @Override
            public void onInterrupt(int code, String message) {
                super.onInterrupt(code, message);
                mView.clearList();
                mView.setErrIncode(mView.getIncode());
            }
        });
    }

    @Override
    public void receive(final int position, final int type) {
        switch (type) {
            case 0:
            mBiz.receiveTask(mContext, "arrival_id", bill.arrival.get(position).arrivalId , TAG, new ICommonResult() {
                @Override
                public void onSuccess(String result) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        int taskId = jsonObject.getInt("task_id");
                        mView.goDetailActivity( bill.arrival.get(position).arrivalId , bill.arrival.get(position).arrivalCode , taskId + "", type);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFail(Call call, Exception e) {

                }

                @Override
                public void onInterrupt(int code, String message) {

                }
            });
            break;
            case 1:
                mBiz.receiveAllocationTask(mContext,  bill.allocation.get(position).allocationId, TAG, new ICommonResult() {
                    @Override
                    public void onSuccess(String result) {
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            int taskId = jsonObject.getInt("task_id");
                            mView.goDetailActivity( bill.allocation.get(position).allocationId,  bill.allocation.get(position).allocationCode, taskId + "", type);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFail(Call call, Exception e) {

                    }

                    @Override
                    public void onInterrupt(int code, String message) {

                    }
                });
                break;
        }
    }

    private int getType() {
        if (mView.getChoose().equals("店内码")) {
            return 2;

        } else   if (mView.getChoose().equals("条形码")){
            return  1;
        } else {
            return 3;
        }
    }
    String getType(int type){
        if(type == 1){
            return "arrival_id";
        }else{
            return "allocation_id";
        }

    }
}
