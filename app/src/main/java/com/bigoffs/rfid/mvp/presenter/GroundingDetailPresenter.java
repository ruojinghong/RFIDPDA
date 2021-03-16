package com.bigoffs.rfid.mvp.presenter;

import android.content.Context;
import android.database.sqlite.SQLiteException;
import android.text.TextUtils;

import com.bigoffs.rfid.MApplication;
import com.bigoffs.rfid.database.greendao.GroundingDetailInfoDao;
import com.bigoffs.rfid.mvp.bean.GroundingBiz;
import com.bigoffs.rfid.mvp.bean.GroundingDetailInfo;
import com.bigoffs.rfid.mvp.bean.IGroundingBiz;

import com.bigoffs.rfid.mvp.view.IGroundingDetailView;
import com.bigoffs.rfid.network.ICommonResult;

import com.bigoffs.rfid.util.DBOperator;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import okhttp3.Call;

public class GroundingDetailPresenter implements IGroundingDetailPresenter {

    private long flag;

    private static final String TAG = "GroundingDetailActivity";

    private IGroundingDetailView mView;

    private Context mContext;

    private String mShelf;

    private List<GroundingDetailInfo> list = new Vector<>();


    private DBOperator<GroundingDetailInfoDao,GroundingDetailInfo> caseDBOperator ;
    private IGroundingBiz mBiz;

    public GroundingDetailPresenter(Context context, IGroundingDetailView view){
        mView = view;
        mContext = context;
        mBiz = new GroundingBiz();

        caseDBOperator = DBOperator.getOperator(MApplication.getDaoSession(context).getGroundingDetailInfoDao(), GroundingDetailInfo.class);
    }



    @Override
    public void queryInCode(final String inCode) {
        if (TextUtils.isEmpty(inCode.trim())) {
            mView.showNotice("店内码不能为空！");
            return;
        }
        if(mShelf.equals("")){
            mView.playBeep();
            mView.showNotice("请先扫描货架号！");
            return;
        }
        int position = -1;
        for(int i =0;i<list.size();i++){
           if (list.get(i).inCode.equals(inCode)){
               //扫描到了重复数据，记录位置
               position = i;
               break;
           }
        }
        if(position>=0){
            mView.playBeep();
            mView.showNotice("请勿扫描重复店内码");
            mView.showList(list);
        }else {
            flag = System.currentTimeMillis();
            final long time = System.currentTimeMillis();
            final GroundingDetailInfo info = new GroundingDetailInfo();
            info.flag  = flag;
            info.inCode = "";
            list.add(0,info);
            mView.getFoucs(2);
            mBiz.checkInCode(mContext, mView.getArrivalId(), inCode,time, TAG, new ICommonResult() {
                @Override
                public void onSuccess(String result) {
                    addIncode(inCode, true,time);
                }


                @Override
                public void onFail(Call call, Exception e) {
                   deleteIncode(time);
                    mView.playBeep();
                    mView.scanSwitch(true);
                }

                @Override
                public void onInterrupt(int code, String message) {
                    addIncode(inCode, false,time);
                    mView.showErrMessage(inCode);
                }
            });
        }
    }

    @Override
    public void queryBarCode(final String barCode) {
        if (TextUtils.isEmpty(barCode.trim())) {
            mView.showNotice("条形码不能为空！");
            return;
        }
        addBarCode(barCode.trim(),true,flag);
//        mView.scanSwitch(false);
//        mBiz.checkBarCode(mContext, mView.getArrivalId(), barCode, TAG, new ICommonResult() {
//            @Override
//            public void onSuccess(String result) {
//                addBarCode( barCode,true);
//            }
//
//
//
//            @Override
//            public void onFail(Call call, Exception e) {
//                mView.playBeep();
//                mView.scanSwitch(true);
//        }
//
//            @Override
//            public void onInterrupt(int code, String message) {
//                addBarCode( barCode,false);
//            }
//        });

    }

    @Override
    public void queryShelfCode(final String shelfCode) {
        if (TextUtils.isEmpty(shelfCode.trim())) {
            mView.showNotice("货架号不能为空！");
            return;
        }
        mView.scanSwitch(false);
        mBiz.checkShelfCode(mContext, shelfCode, TAG, new ICommonResult() {
            @Override
            public void onSuccess(String result) {
                addShelf(shelfCode,true);
            }


            @Override
            public void onFail(Call call, Exception e) {
                mView.playBeep();
                mView.scanSwitch(true);
            }

            @Override
            public void onInterrupt(int code, String message) {
                mView.showErrMessage(shelfCode);
            }
        });


    }

    @Override
    public void countNum(int num) {

    }

    @Override
    public void deleteItem(String code) {
            if(list.size()>0){
                Iterator<GroundingDetailInfo> iterator = list.iterator();
                while (iterator.hasNext()){
                    GroundingDetailInfo info = iterator.next();
                    if(info!=null && info.inCode.equals(code)) {
                        try {
                            caseDBOperator.deleteItemsByCondition(GroundingDetailInfoDao.Properties.InCode.eq(code));
                            iterator.remove();
                            mView.showList(list);
                            mView.showNotice("已剔除");
                        }catch (SQLiteException e){

                        }
                        break;
                    }else{
                        mView.showNotice("请输入正确的店内码");
                    }

                }
                if(TextUtils.isEmpty(list.get(0).barCode)){
                    mView.getFoucs(2);
                }else {
                    mView.getFoucs(1);
                }

            }else {
                mView.getFoucs(1);
            }
    }

    @Override
    public void deleteFirstItem() {
            if(list!= null && list.size()>0){
                try {
                    caseDBOperator.deleteItemsByCondition(GroundingDetailInfoDao.Properties.InCode.eq(list.get(0).inCode));
                    list.remove(0);
                    mView.showList(list);
                    mView.showNotice("已剔除");
                }catch (SQLiteException e){

                }
            }
        mView.getFoucs(1);
    }

    @Override
    public void upData(String taskId) {
//            if(TextUtils.isEmpty(list.get(0).shelfCode)){
//                mView.showNotice("请关联货架号");
//                return;
//            }

            Iterator<GroundingDetailInfo> it = list.iterator();
            while (it.hasNext()){
                GroundingDetailInfo info = it.next();
                if(TextUtils.isEmpty(info.shelfCode) || TextUtils.isEmpty(info.inCode ) || TextUtils.isEmpty(info.barCode)){
                    mView.showNotice("数据不完整");
                    mView.showList(list);
                    return;
                }
            }

            Gson gson = new GsonBuilder().setExclusionStrategies(new ExclusionStrategy() {
                @Override
                public boolean shouldSkipField(FieldAttributes f) {
                    //过滤字段
                    return f.getName().contains("iCode")|f.getName().contains("bCode")|f.getName().contains("sCode");
                }

                @Override
                public boolean shouldSkipClass(Class<?> clazz) {
                    return false;
                }
            }).create();

            mView.showLoading("");
            String dataList = gson.toJson(list);
            mBiz.upData(mContext, mView.getArrivalId(), dataList,taskId, TAG, new ICommonResult() {
                @Override
                public void onSuccess(String result) {
                    try {
                        JSONObject object = new JSONObject(result);
                        int num = object.optInt("count");
                        caseDBOperator.mCustomDao.deleteAll();
                        mView.hideLoading();
                      mView.goDetail(num);
                    } catch (SQLiteException e) {
                    }catch (JSONException e){
                        mView.showNotice("上传解析失败");
                    }
                }

                @Override
                public void onFail(Call call, Exception e) {  mView.hideLoading();
                }

                @Override
                public void onInterrupt(int code, String message) {
                    mView.hideLoading();
                }
            });

    }

    @Override
    public void queryHistory() {
        try {
            list.addAll(caseDBOperator.mCustomDao.loadAll());
        }catch (SQLiteException e){

        }
        mView.showList(list);
    }

    @Override
    public void queryAll() {

    }

    @Override
    public void saveError(GroundingDetailInfo error) {

    }

    @Override
    public void deleteError(GroundingDetailInfo groundingDetailInfo) {

    }


    private void addIncode(String inCode , boolean b, long time) {
        if(b){
            mView.showCode(inCode);
        }else {
            mView.showErrMessage(inCode);
        }

        for(int i = 0; i<list.size(); i++){
            if(list.get(i).flag == time){
                list.get(i).inCode = inCode;
                list.get(i).iCode = b;
                list.get(i).shelfCode = mShelf;
                list.get(i).sCode = true;
                break;
            }
        }
        mView.showList(list);
        mView.scanSwitch(true);
    }

    private void deleteIncode(long time){
        Iterator<GroundingDetailInfo> it = list.iterator();
        while (it.hasNext()){
            if(it.next().flag == time){
                it.remove();
                mView.showList(list);
            }
        }
    }

    private void addBarCode(String barCode, boolean b, long time) {
        if(list!= null && list.size()>0) {
            if (b) {
                mView.showCode(barCode);
            } else {
                mView.showErrMessage(barCode);
            }

            for(int i = 0; i<list.size(); i++){
                if(list.get(i).flag == time){
                    GroundingDetailInfo info = list.get(i);
                    info.barCode = barCode;
                    info.bCode = true;
                    mView.showList(list);
                    mView.scanSwitch(true);
                    info.flag = System.currentTimeMillis();
                    try {
                        caseDBOperator.insertData(info);
                    }catch (SQLiteException e){

                    }
                    break;
                }
            }

        }
        mView.getFoucs(1);
    }

    private void addShelf(String shelfCode, boolean b) {
        mShelf = shelfCode;
        if(b){
            mView.showCode(shelfCode);
        }else {
            mView.showErrMessage(shelfCode);
        }
//        Iterator<GroundingDetailInfo> listIt = list.iterator();
//
//        while (listIt.hasNext()){
//            GroundingDetailInfo info = listIt.next();
//            if(info.shelfCode == null){
//                info.shelfCode = shelfCode;
//                info.sCode = b;
//                try {
//                    mHelper.add(info.inCode, info.barCode, info.shelfCode, info.iCode ? "1" : "0", info.bCode ? "1" : "0", info.sCode ? "1" : "0", System.currentTimeMillis());
//                }catch (SQLiteException e){
//                    mHelper.createTable();
//                }
//            }
//        }
//        mView.showList(list);
        mView.setEdShelf(shelfCode);
        mView.getFoucs(1);
        mView.scanSwitch(true);
    }

    public   static List<GroundingDetailInfo> removeDuplicate(List<GroundingDetailInfo> list)  {
        for  ( int  i  =   0 ; i  <  list.size()  -   1 ; i ++ )  {
            for  ( int  j  =  list.size()  -   1 ; j  >  i; j -- )  {
                if  (list.get(j).inCode.equals(list.get(i).inCode))  {
                    list.remove(j);
                    break;
                }
            }
        }
        return list;
    }

}
