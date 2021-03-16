package com.bigoffs.rfid.mvp.presenter;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

import com.bigoffs.rfid.database.greendao.InventoryCaseDao;
import com.bigoffs.rfid.mvp.bean.InventoryInfo;
import com.bigoffs.rfid.mvp.bean.dao.InventoryCase;
import com.bigoffs.rfid.mvp.biz.IInventoryBiz;
import com.bigoffs.rfid.mvp.biz.InventoryBiz;
import com.bigoffs.rfid.mvp.view.IInventoryView;
import com.bigoffs.rfid.network.ICommonResult;
import com.bigoffs.rfid.util.DBOperator;
import com.bigoffs.rfid.util.LogUtil;
import com.bigoffs.rfid.util.TakeStockTransaction;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * @author okbuy-001
 * @desc
 * @time 2020/5/21 10:31
 */
public class InventoryPresenter {

    private IInventoryView mview;
    private IInventoryBiz mBiz;
    private Context mContext;
    public static final String TAG = "InventoryActivity";

    private List<InventoryCase> datas = new ArrayList<>();
    private List<InventoryCase> normalList = new ArrayList<>();

    private DBOperator<InventoryCaseDao,InventoryCase> dbOperator ;
    private String taskId;

    public InventoryPresenter(Context context,IInventoryView view,String id) {
        mview = view;
        mBiz = new InventoryBiz();
        mContext =context;
        taskId = id;
        dbOperator = TakeStockTransaction.getInventoryDBOperator();
    }

    public List<InventoryCase> getList(){
        return datas;
    }

    public void loadData(String taskId){
        mview.showLoading("");
        mBiz.receiveTask(mContext, taskId, TAG, new ICommonResult() {
            @Override
            public void onSuccess(String result) {

                insertData(result);

            }



            @Override
            public void onFail(Call call, Exception e) {
                mview.hideLoading();
                mview.showNotice("");
            }

            @Override
            public void onInterrupt(int code, String message) {
                mview.hideLoading();
                mview.showNotice(message);
            }
        });

    }


    public void query(String data) {
        List<InventoryCase> cases = dbOperator.getItemByParameter(InventoryCaseDao.Properties.RfidCode,data);
        if(cases.size()>0){
            normalList.add(cases.get(0));
            mview.addScanNum();

        }else{
            datas.add(new InventoryCase(null,"","",data));
            mview.updataErrorList();
        }
    }

    public void clearTable(){
        dbOperator.mCustomDao.deleteAll();
    }
    private void insertData(String result) {
        clearTable();
        InventoryInfo inventoryInfo= new Gson().fromJson(result,InventoryInfo.class);
        mview.setShelfArea(inventoryInfo.shelfArea);
        new Thread(()->{
            for(int i = 0; i<inventoryInfo.getShelfs().size();i++){
                    dbOperator.insertData(inventoryInfo.getShelfs().get(i));
            }

            mview.initData(dbOperator.getAll().size());


        }).start();
    }

    public void reset() {
        normalList.clear();
        datas.clear();
    }

    public void upLoad() {

            new AlertDialog.Builder(mContext).setMessage("确定完成盘点任务并记录差异？").setNegativeButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                        toUpLoad();
                }
            }).setPositiveButton("取消",null).show();
    }

     void toUpLoad(){

            mview.showLoading("");
        List<String> rfids = new ArrayList<>();
         for (InventoryCase inventoryCase:
              datas) {
             rfids.add(inventoryCase.getRfidCode());
         }

         for (InventoryCase inventoryCase:
                 normalList){
             rfids.add(inventoryCase.getRfidCode());
         }

         mBiz.finishRfidTask(mContext, taskId + "", new Gson().toJson(rfids), TAG, new ICommonResult() {
             @Override
             public void onSuccess(String result) {
                 mview.hideLoading();
                    mview.finishTask(result);
             }

             @Override
             public void onFail(Call call, Exception e) {
                    mview.hideLoading();
                    mview.showNotice(e.toString());
             }

             @Override
             public void onInterrupt(int code, String message) {
                 mview.hideLoading();
                 mview.showNotice(message);
             }
         });






     }

}
