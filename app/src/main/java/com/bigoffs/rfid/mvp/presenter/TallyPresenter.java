package com.bigoffs.rfid.mvp.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.bigoffs.rfid.mvp.bean.TransferInfo;
import com.bigoffs.rfid.mvp.biz.ITallyBiz;
import com.bigoffs.rfid.mvp.biz.TallyBiz;
import com.bigoffs.rfid.mvp.view.ITallyView;
import com.bigoffs.rfid.network.ICommonResult;
import com.bigoffs.rfid.util.LogUtil;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * @author okbuy-001
 * @desc
 * @time 2018/12/11 10:45
 */
public class TallyPresenter implements ITallyPresenter {

    private static final String TAG = "TallActivity";

    private Context mContext;

    private ITallyView mView;

    private ITallyBiz mBiz;

    private List<TransferInfo.FailIncodesBean> list = new ArrayList<>();
    //当前货架号
    private String currentShelfNum;


    public TallyPresenter(Context context, ITallyView view){
        mContext = context;
        mView = view;
        mBiz = new TallyBiz();
    }

    @Override
    public void queryInCode(final String inCode) {
        if (TextUtils.isEmpty(inCode.trim())) {
            mView.showNotice("店内码不能为空！");
            return;
        }
        if(TextUtils.isEmpty(currentShelfNum)) {
            mView.showNotice("请先扫描货架号");
            mView.playBeep();
            mView.getFoucs(1);
            return;
        }

        mBiz.queryIncode(mContext, inCode, TAG, new ICommonResult() {
            @Override
            public void onSuccess(String result) {
                addIncode(inCode, true);
            }

            @Override
            public void onFail(Call call, Exception e) {
                mView.playBeep();
            }

            @Override
            public void onInterrupt(int code, String message) {
//                addIncode(inCode,false);
                mView.showErrMessage(inCode);

            }
        });


//        int position = -1;
//        for(int i =0;i<list.size();i++){
//            if (list.get(i).inCode.equals(inCode)){
//                //扫描到了重复数据，记录位置
//                position = i;
//                break;
//            }
//        }
//        if(position>=0){
//             TransferInfo info =list.get(position);
//            if(info.shelfCode.equals(currentShelfNum)){
//                list.remove(position);
//                list.add(0,info);
//                mView.getFoucs(2);
//                mView.showList(list);
//                mView.scanSwitch(true);
//            }else{
//                final int finalPosition = position;
//                        mBiz.queryIncode(mContext, inCode, TAG, new ICommonResult() {
//                            @Override
//                            public void onSuccess(String result) {
//                                TransferInfo info =list.get(finalPosition);
//                                list.remove(finalPosition);
//                                info.shelfCode = currentShelfNum;
//                                list.add(0,info);
//                                mView.getFoucs(2);
//                                mView.showList(list);
//                                mView.scanSwitch(true);
//                    }
//
//                    @Override
//                    public void onFail(Call call, Exception e) {
//                        mView.playBeep();
//                    }
//
//                    @Override
//                    public void onInterrupt(int code, String message) {
////                addIncode(inCode,false);
//                        mView.showErrMessage(inCode);
//                    }
//                });
//            }
//
//        }else {
//            mBiz.queryIncode(mContext, inCode, TAG, new ICommonResult() {
//                @Override
//                public void onSuccess(String result) {
//                    addIncode(inCode, true);
//                }
//
//                @Override
//                public void onFail(Call call, Exception e) {
//                    mView.playBeep();
//                }
//
//                @Override
//                public void onInterrupt(int code, String message) {
////                addIncode(inCode,false);
//                    mView.showErrMessage(inCode);  addIncode(inCode, true);
//
//                }
//            });
//        }
    }

    @Override
    public void queryShelfCode(final String shelfCode) {
        if (TextUtils.isEmpty(shelfCode.trim())) {
            mView.showNotice("货架号不能为空！");
            mView.playBeep();
            return;
        }
        mView.scanSwitch(false);
        mBiz.queryShelfcode(mContext, shelfCode, TAG, new ICommonResult() {
            @Override
            public void onSuccess(String result) {
                addShelf(shelfCode,true);

            }

            @Override
            public void onFail(Call call, Exception e) {
                mView.playBeep();
                mView.scanSwitch(true);
                mView.getFoucs(1);
            }

            @Override
            public void onInterrupt(int code, String message) {
//                addShelf(shelfCode,false);
                mView.showErrMessage(shelfCode);
                mView.scanSwitch(true);
                mView.getFoucs(1);
            }
        });
    }

    @Override
    public void clearAllItem() {
        currentShelfNum = "";
        list.clear();
        mView.showList(list);
        mView.clearPage();
    }

    @Override
    public void moveShelf() {
        mView.showLoading("");
        ArrayList<String> incodeList = new ArrayList<>();
        for(Map.Entry<String,String> entry:mView.getIncodes().entrySet()){
            incodeList.add(entry.getKey());
        }

        String incodes = new Gson().toJson(incodeList);
        String rfids = new Gson().toJson(mView.getRfids());
       mBiz.transferShelfNew(mContext, currentShelfNum, incodes, rfids, TAG, new ICommonResult() {
           @Override
           public void onSuccess(String result) {
               mView.hideLoading();
               mView.showNotice("转移货架成功");
               currentShelfNum = "";
                TransferInfo info= new Gson().fromJson(result,TransferInfo.class);
               mView.showList(info.getFailIncodes());
                mView.getIncodes().clear();
                mView.getRfids().clear();
                mView.updateListCount();

           }

           @Override
           public void onFail(Call call, Exception e) {
                mView.hideLoading();
                mView.showNotice(e.toString());
           }

           @Override
           public void onInterrupt(int code, String message) {
               mView.hideLoading();
               mView.showNotice(message);
           }
       });


        }

    @Override
    public void deleteFirstItem() {
//        if(list!= null && list.size()>0){
//            list.remove(0);
//            mView.showList(list);
//            mView.showNotice("已剔除");
//        }
    }

    @Override
    public String getCurrentShelf() {
        return currentShelfNum;
    }

    private void addIncode(String inCode , boolean b) {

        mView.getIncodes().put(inCode,null);

//        if(b){
//            mView.showCode(inCode);
//        }else {
//            mView.showErrMessage(inCode);
//        }
//        TransferInfo info = new TransferInfo();
//        info.inCode= inCode;
//        info.iCode = b;
//        info.shelfCode = currentShelfNum;
//        list.add(0,info);
        mView.updateListCount();
        mView.getFoucs(2);
//        mView.showList(list);
    }

    private void addShelf(String shelfCode, boolean b) {
        if(b){
            mView.showCode(shelfCode);
        }else {
            mView.showErrMessage(shelfCode);
        }
        currentShelfNum = shelfCode;

        mView.showCurrentShelf(shelfCode);
        mView.getFoucs(2);
    }



//    public   static List<TransferInfo> removeDuplicate(List<TransferInfo> list)  {
//        for  ( int  i  =   0 ; i  <  list.size()  -   1 ; i ++ )  {
//            for  ( int  j  =  list.size()  -   1 ; j  >  i; j -- )  {
//                if  (list.get(j).inCode.equals(list.get(i).inCode))  {
//                    list.remove(j);
//                    break;
//                }
//            }
//        }
//        return list;
//    }
}
