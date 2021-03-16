package com.bigoffs.rfid.mvp.presenter;

import android.content.Context;
import android.database.sqlite.SQLiteException;
import android.text.TextUtils;


import com.bigoffs.rfid.database.greendao.GroundingAllocationDetailInfoDao;
import com.bigoffs.rfid.database.greendao.GroundingDetailInfoDao;
import com.bigoffs.rfid.mvp.bean.GroundingAllocationBiz;
import com.bigoffs.rfid.mvp.bean.GroundingDetailInfo;
import com.bigoffs.rfid.mvp.bean.IGroundingBiz;
import com.bigoffs.rfid.mvp.bean.dao.GroundingAllocationDetailInfo;
import com.bigoffs.rfid.mvp.bean.dao.PutAwayInfo;
import com.bigoffs.rfid.mvp.view.IGroundingAllocationDetailView;

import com.bigoffs.rfid.network.ICommonResult;

import com.bigoffs.rfid.util.DBOperator;
import com.bigoffs.rfid.util.TakeStockTransaction;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import okhttp3.Call;

public class GroundingAllocationDetailPresenter implements IGroundingAllocationDetailPresenter {



    private static final String TAG = "GroundingAllocationDetailActivity";

    private IGroundingAllocationDetailView mView;

    private Context mContext;
    private String  allocationId;

    private List<GroundingAllocationDetailInfo> list = new ArrayList<>();
    private DBOperator<GroundingAllocationDetailInfoDao, GroundingAllocationDetailInfo> caseDBOperator ;

    private IGroundingBiz mBiz;

    public GroundingAllocationDetailPresenter(Context context, IGroundingAllocationDetailView view){
        mView = view;
        mContext = context;
        mBiz = new GroundingAllocationBiz();
        caseDBOperator = TakeStockTransaction.getGroundingAllocationDetailInforDBOperator();
    }



    @Override
    public void queryInCode(final String inCode) {
        if (TextUtils.isEmpty(inCode.trim())) {
            mView.showNotice("店内码不能为空！");
            return;
        }

        List<GroundingAllocationDetailInfo> cases = caseDBOperator.getItemByParameter(GroundingAllocationDetailInfoDao.Properties.InCode,inCode);

        if(cases.size()>0){
            mView.add(cases.get(0).getInCode()+"");
        }else{
            mView.addIncodeError(inCode+"");
        }


    }

    @Override
    public void queryBarCode(final String barCode) {
        if (TextUtils.isEmpty(barCode.trim())) {
            mView.showNotice("条形码不能为空！");
            return;
        }
        mView.scanSwitch(false);
        mBiz.checkBarCode(mContext, mView.getArrivalId(), barCode, TAG, new ICommonResult() {
            @Override
            public void onSuccess(String result) {
                addBarCode( barCode,true);
            }



            @Override
            public void onFail(Call call, Exception e) {
                mView.playBeep();
                mView.scanSwitch(true);
        }

            @Override
            public void onInterrupt(int code, String message) {
                addBarCode( barCode,false);
            }
        });

    }

    @Override
    public void queryShelfCode(final String shelfCode) {
        if (TextUtils.isEmpty(shelfCode.trim())) {
            mView.showNotice("货架号不能为空！");
            return;
        }

        addShelf(shelfCode,true);



    }

    @Override
    public void countNum(int num) {

    }

    @Override
    public void deleteItem(String code) {
            if(list.size()>0){
                Iterator<GroundingAllocationDetailInfo> iterator = list.iterator();
                while (iterator.hasNext()){
                    if(iterator.next().inCode.equals(code)) {
                        try {
                            caseDBOperator.deleteItemsByCondition(GroundingAllocationDetailInfoDao.Properties.InCode.eq(code));
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
    }

    @Override
    public void upData(String allocationCode) {


            mView.showLoading("");

            mBiz.upAllocationData(mContext, allocationCode, null,null, TAG, new ICommonResult() {
                @Override
                public void onSuccess(String result) {
                    try {
                        JSONObject object = new JSONObject(result);
                        caseDBOperator.mCustomDao.deleteAll();
                        mView.hideLoading();
                        mView.showNotice("上传完成");
                        //现在只是finish掉
                        mView.goDetail(1);
//                      mView.goDetail(Integer.valueOf(result));

                    } catch (SQLiteException e) {
                        mView.hideLoading();
                    }catch (JSONException e){
//                        mView.showNotice("上传解析失败");
                        mView.hideLoading();
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
    public void upData(String shelf, List<String> incodeList, List<GroundingAllocationDetailInfo> rfidList, String allocationCode) {
        mView.showLoading("");
        List<String> rfids = new ArrayList<>();
        for (GroundingAllocationDetailInfo error:rfidList){
            if(TextUtils.isEmpty(error.getRfid())){
                incodeList.add(error.getInCode());
            }else{
                rfids.add(error.getRfid());
            }
        }
        mBiz.upSignData(mContext, shelf, new Gson().toJson(incodeList), new Gson().toJson(rfids), allocationCode, TAG, new ICommonResult() {
            @Override
            public void onSuccess(String result) {
                mView.hideLoading();
                mView.showNotice("上架成功");
                //现在上架完就直接签收了，不用点击签收按钮来结束流程了，所以直接结束流程
//                queryHistory(allocationCode);
                clearTable();
                mView.goDetail(1);
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
    public void queryHistory(String  allocationCode) {
       mView.showLoading("");
        mBiz.getAllocationData(mContext, allocationCode, 0, TAG, new ICommonResult() {
            @Override
            public void onSuccess(String result) {
               dealDatas(result,true);
            }

            @Override
            public void onFail(Call call, Exception e) {

            }

            @Override
            public void onInterrupt(int code, String message) {

            }
        });
        mView.showList(list);
    }

    @Override
    public void queryAll() {

    }

    @Override
    public void saveError(GroundingAllocationDetailInfo error) {
            caseDBOperator.insertData(error);
    }

    @Override
    public void deleteError(GroundingAllocationDetailInfo groundingDetailInfo) {
        caseDBOperator.deleteData(groundingDetailInfo);
    }

    @Override
    public void clearTable() {
        caseDBOperator.mCustomDao.deleteAll();
    }

    @Override
    public void queryRfid(String data) {
        List<GroundingAllocationDetailInfo> cases = caseDBOperator.getItemByParameter(GroundingAllocationDetailInfoDao.Properties.Rfid,data);

        if(cases.size()==1){
            mView.add(cases.get(0).getInCode());
        }else{
            mView.addError(data);
        }
    }

    @Override
    public void fromRfidgetIncode(String rfid) {
        mBiz.getIncodeFromRfid(mContext, rfid, TAG, new ICommonResult() {
            @Override
            public void onSuccess(String result) {
                mView.setItemIncode(result);
            }

            @Override
            public void onFail(Call call, Exception e) {
                mView.setItemIncode("查询失败");
            }

            @Override
            public void onInterrupt(int code, String message) {
                mView.setItemIncode("查询失败");
            }
        });
    }


    private void addIncode(String inCode , boolean b) {
        if(b){
            mView.showCode(inCode);
        }else {
            mView.showErrMessage(inCode);
        }

        GroundingAllocationDetailInfo info = new GroundingAllocationDetailInfo();
        info.inCode = inCode;
        list.add(0,info);
        mView.getFoucs(2);
        mView.showList(list);
        mView.scanSwitch(true);
    }
    private void addBarCode(String barCode, boolean b) {
        if(list!= null && list.size()>0) {
            if (b) {
                mView.showCode(barCode);
            } else {
                mView.showErrMessage(barCode);
            }
            list.get(0).barCode = barCode;

            mView.getFoucs(1);
            mView.showList(list);
            mView.scanSwitch(true);
        }
    }

    private void addShelf(String shelfCode, boolean b) {
        if(b){
            mView.showCode(shelfCode);
        }else {
            mView.showErrMessage(shelfCode);
        }
        Iterator<GroundingAllocationDetailInfo> listIt = list.iterator();

        while (listIt.hasNext()){
            GroundingAllocationDetailInfo info = listIt.next();
            if(info.shelfCode == null){
                info.shelfCode = shelfCode;


                try {
                    caseDBOperator.insertData(info);
                }catch (SQLiteException e){

                }
            }
        }
        mView.showList(list);
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

    void dealDatas(String result, boolean isRefresh){
        clearTable();
        PutAwayInfo allocation = new Gson().fromJson(result,PutAwayInfo.class);
        allocationId = allocation.getAllocationCode();
        mView.initData(allocation);
        new Thread(()->{
            //忽略列表
            List<String> data = new ArrayList<>();
            for (GroundingAllocationDetailInfo info : allocation.getInIncodeList()) {
                GroundingAllocationDetailInfo allocationDetailInfo = new GroundingAllocationDetailInfo(null,info.inCode, null, null, info.rfid, true);
                caseDBOperator.insertData(allocationDetailInfo);
                if(isRefresh) {
                    data.add(info.getRfid());
                }
            }
            for (GroundingAllocationDetailInfo info : allocation.getWaitIncodeList()) {
                GroundingAllocationDetailInfo allocationDetailInfo = new GroundingAllocationDetailInfo(null,info.inCode, null, null, info.rfid, true);
                caseDBOperator.insertData(allocationDetailInfo);
            }
            mView.hideLoading();
            if(isRefresh) {
                mView.refresh(data);
            }


        }).start();

    }




}
