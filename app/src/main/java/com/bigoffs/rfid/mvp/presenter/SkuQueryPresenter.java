package com.bigoffs.rfid.mvp.presenter;

import android.content.Context;

import com.bigoffs.rfid.mvp.biz.ISkuQueryBiz;
import com.bigoffs.rfid.mvp.biz.SkuQueryBiz;
import com.bigoffs.rfid.mvp.bean.SkuQueryInfo;
import com.bigoffs.rfid.mvp.view.ISkuQueryView;
import com.bigoffs.rfid.network.ICommonResult;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import okhttp3.Call;

/**
 * @author okbuy-001
 * @desc
 * @time 2020/4/28 16:54
 */
public class SkuQueryPresenter implements IQuerySkuPresenter{

    private Context mContext;

    private ISkuQueryView mView;

    private ISkuQueryBiz mBiz;



    public SkuQueryPresenter(Context context, ISkuQueryView view){
        mContext = context;
        mView = view;
        mBiz = new SkuQueryBiz();
    }



    @Override
    public void load(String sku) {
        mView.showLoading("");
        mBiz.querySkuInfo(mContext, sku, "SkuQueryActivity", new ICommonResult() {
            @Override
            public void onSuccess(String result) {
                mView.hideLoading();
                Gson gson = new Gson();
                List<SkuQueryInfo> data = gson.fromJson(result,new TypeToken<List<SkuQueryInfo>>(){}.getType());
                if(data.size()>0){
                    mView.setAdatper(data);
                }else{
                    mView.showBlank();
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
}

