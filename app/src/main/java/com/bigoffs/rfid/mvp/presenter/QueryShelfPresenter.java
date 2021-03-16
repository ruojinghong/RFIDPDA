package com.bigoffs.rfid.mvp.presenter;

import android.content.Context;

import com.bigoffs.rfid.mvp.bean.Sku;
import com.bigoffs.rfid.mvp.bean.SkuQueryInfo;
import com.bigoffs.rfid.mvp.biz.ISkuQueryBiz;
import com.bigoffs.rfid.mvp.biz.SkuQueryBiz;
import com.bigoffs.rfid.mvp.view.IDataFragmentView;
import com.bigoffs.rfid.network.ICommonResult;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * @author okbuy-001
 * @desc
 * @time 2020/4/28 18:07
 */
public class QueryShelfPresenter {
    IDataFragmentView view;
    List<String> incodeList;
    ISkuQueryBiz mBiz;
    Context mContext;
    public QueryShelfPresenter(IDataFragmentView view, Context context){
        this.view  = view;
        mBiz = new SkuQueryBiz();
        mContext = context;
    }

    public void queryIncodeByShelf(String epc){
        view.showLoading("");
        mBiz.getIncodeFromRfid(mContext, epc, "FindActivity", new ICommonResult() {
            @Override
            public void onSuccess(String result) {




                   view.ShowData(result);
                view.hideLoading();

            }

            @Override
            public void onFail(Call call, Exception e) {
                view.showNotice(e.getMessage()); view.hideLoading();
            }

            @Override
            public void onInterrupt(int code, String message) {
                view.showNotice(message); view.hideLoading();
            }
        });
    }


}
