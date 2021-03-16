package com.bigoffs.rfid.mvp.presenter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import android.text.TextUtils;


import com.bigoffs.rfid.database.greendao.ProductInfoCaseDao;
import com.bigoffs.rfid.mvp.bean.PickingDetail;
import com.bigoffs.rfid.mvp.bean.PickingDetailNew;
import com.bigoffs.rfid.mvp.bean.PickingResult;
import com.bigoffs.rfid.mvp.bean.dao.ProductInfoCase;
import com.bigoffs.rfid.mvp.biz.IPickingBiz;
import com.bigoffs.rfid.mvp.biz.PickingBiz;
import com.bigoffs.rfid.mvp.view.IPickingDetailView;
import com.bigoffs.rfid.network.ICommonResult;
import com.bigoffs.rfid.ui.activity.PickingPreviewActivity;
import com.bigoffs.rfid.ui.activity.PickingResultActivity;
import com.bigoffs.rfid.util.DBOperator;

import com.bigoffs.rfid.util.LogUtil;
import com.bigoffs.rfid.util.TakeStockTransaction;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by okbuy on 17-3-30.
 */

public class PickingPresenter implements IPickingPresenter {

    private IPickingBiz mBiz;// Model
    private IPickingDetailView mView;// View
    private List<ProductInfoCase> currentList;

    private Context mContext;

    private int mTaskId;// 任务单id
    private PickingDetail mInfo;// 拣货单详情
    private PickingDetailNew data;
    //扫描的店内码
    private String trueIncode = "";
    //列表显示的店内码
    private String repIncode = "";
    private DBOperator<ProductInfoCaseDao, ProductInfoCase> dbOperator;

    public PickingPresenter(Context context, IPickingDetailView view, PickingDetailNew data) {
        mContext = context;
        mView = view;
        mBiz = new PickingBiz();
        this.data = data;
        dbOperator = TakeStockTransaction.getProductInfoDBOperator();
        dbOperator.mCustomDao.deleteAll();
        initData(data);

    }

    @Override
    public void setTaskId(int taskId) {
        if (taskId != 0) {
            mTaskId = taskId;
//            getPickDetail();
        }
    }

    @Override
    public void getPickDetail() {
        mView.showLoading("");
        mBiz.getPickingdeatil(mContext, mTaskId, "PickingDetailActivityNew", new ICommonResult() {
            @Override
            public void onSuccess(String result) {
                mView.hideLoading();
                getPickingInfo(result);
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
    public void queryIncodeOrShelf(final String incodeOrShelf) {
        if (TextUtils.isEmpty(incodeOrShelf)) {
            mView.showNotice("货架号或店内码不能为空");
            return;
        }
        mView.showLoading("");

        int position = -1;
        all:for (int i = 0; i < data.pickList.size(); i++) {
            if (data.pickList.get(i).shelf.equals(incodeOrShelf)) {
                position = i;
                break;
            }
//            for(int j =0;j<data.pickList.get(i).data.size();j++){
//                if (data.pickList.get(i).data.get(j).incode.equals(incodeOrShelf)){
//                    if(data.pickList.get(i).data.get(j).status == 3){
//                        mView.hideLoading();
//                        mView.playBeep();
//                        mView.showNotice("该商品已取消");
//                        mView.scanSwitch(true);
//                        mView.setWrongIncode(incodeOrShelf);
//                        return;
//                    }else {
//                        break all;
//                    }
//
//                }
//            }
        }


        if (position >= 0) {
            //大于零说明扫描到了列表里的货架号  i表示货架号位置
            PickingDetailNew.ShelfInfo item = data.pickList.get(position);
            data.pickList.remove(position);
            data.pickList.add(0, item);
            mView.changShelf(data);
            mView.hideLoading();
            mView.scanSwitch(true);
            mView.setRightIncode(incodeOrShelf);
        } else {
            mView.scanSwitch(false);
            //扫描的数据不是列表里的货架号
            mBiz.picking(mContext, data.taskId + "", incodeOrShelf, "PickingDetailActivityNew", new ICommonResult() {
                @Override
                public void onSuccess(String result) {

                    mView.hideLoading();
                    Gson gson = new Gson();
                    PickingResult pick = gson.fromJson(result, PickingResult.class);
                    if (pick.incode.equals(incodeOrShelf)) {
                        int position = 0;
                        //拣货成功 更改状态
                        findIncode:for (int i = 0; i < data.pickList.size(); i++) {
                            for (int j = 0; j < data.pickList.get(i).data.size(); j++) {
                                if (data.pickList.get(i).data.get(j).detailId == (pick.detailId)) {
                                    data.pickList.get(i).data.get(j).status = pick.status;
                                    data.pickList.get(i).data.get(j).incode = pick.incode;
                                    position = i;
                                   break findIncode;
                                }
                            }
                        }

                            if(isToBottom(data,position)){

                            }else{
                                toTop(data,position);
                        }
                        mView.changShelf(data);
                        mView.scanSwitch(true);
                        if(pick.status == 3){
                            mView.setWrongIncode(incodeOrShelf);
                            mView.playBeep();
                            mView.showNotice("该商品已取消");
                        }else {
                            mView.setRightIncode(incodeOrShelf);
                        }
                    } else {
                        //扫到了同一批的店内码，显示是否替换
                        mView.playBeep();
                        trueIncode = incodeOrShelf;
                        repIncode = pick.incode;
                        mView.scanSwitch(true);
                        mView.setRightIncode(incodeOrShelf);
//                        changeIncode(pick);
                    }


                }

                @Override
                public void onFail(Call call, Exception e) {
                    mView.hideLoading();
                    mView.playBeep();
                    mView.scanSwitch(true);
                    mView.setWrongIncode(incodeOrShelf);
                }

                @Override
                public void onInterrupt(int code, String message) {

                        mView.hideLoading();
                        mView.playBeep();
                        mView.scanSwitch(true);
                        mView.setWrongIncode(incodeOrShelf);

                }
            });
        }


//        mBiz.scanIncodeOrShelf(mContext, mTaskId, incodeOrShelf, "PickingDetailActivity", new ICommonResult() {
//            @Override
//            public void onSuccess(String result) {
//                mView.hideLoading();
//                getPickingInfo(result);
//                mView.setRightIncode(incodeOrShelf);
//                checkLastPicking();
//            }
//
//            @Override
//            public void onFail(Call call, Exception e) {
//                mView.hideLoading();
//                mView.setWrongIncode(incodeOrShelf);
//            }
//
//            @Override
//            public void onInterrupt(int code, String message) {
//                mView.hideLoading();
//                mView.setWrongIncode(incodeOrShelf);
//            }
//        });
    }

    @Override
    public void getPickingInfo(String result) {
        Gson gson = new Gson();
        mInfo = gson.fromJson(result, PickingDetail.class);
        mView.setPickInfo(mInfo);
    }

    // 检查是否已捡完全部货品
    @Override
    public void checkLastPicking() {
        if (mInfo.finishNum == mInfo.totalNum) {
            new AlertDialog.Builder(mContext).setMessage("您已拣完全部货品，是否完成拣货？").setNegativeButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finishPicking();
                }
            }).setNegativeButton("取消",null).show();
        }
    }

    @Override
    public void previewPickingList() {
        PickingPreviewActivity.show(mContext, mTaskId);
    }

    @Override
    public void showFinishDialog() {

        new AlertDialog.Builder(mContext).setMessage("确定完成拣货？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finishPicking();
            }
        }).setNegativeButton("取消",null).show();

    }

    @Override
    public void finishPicking() {
        mView.showLoading("");

        List<String> rfids = new ArrayList<>();
        for(int i = 0; i<data.pickList.size();i++){
            for(int j = 0; j<data.pickList.get(i).data.size();j++){
                if(data.pickList.get(i).data.get(j).status == 2){
                    rfids.add(data.pickList.get(i).data.get(j).rfid);
                }
            }
//
        }


        mBiz.finishPicking(mContext, mTaskId,  new Gson().toJson(rfids),"PickingDetailActivityNew", new ICommonResult() {
            @Override
            public void onSuccess(String result) {
                mView.hideLoading();
                PickingResultActivity.show(mContext, result, 1);
                mView.closeActivity();

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
    public void cancleTask(String taskId, String outstockId) {
        Boolean isCanCancel = true;
        for (int i = 0; i < data.pickList.size(); i++) {
            for (int j = 0; j < data.pickList.get(i).data.size(); j++) {
                if (data.pickList.get(i).data.get(j).status == 2) {
                    mView.showNotice("已有已拣商品，无法取消任务");
                    return;
                }
            }
        }
        mBiz.cancelTask(mContext, taskId, outstockId, "PickingDetailActivityNew", new ICommonResult() {
            @Override
            public void onSuccess(String result) {
                mView.finishPicking();
            }

            @Override
            public void onFail(Call call, Exception e) {

            }

            @Override
            public void onInterrupt(int code, String message) {

            }
        });
    }

    @Override
    public void changeIncode() {
        int position = 0;
        all:
        for (int i = 0; i < data.pickList.size(); i++) {
            for (int j = 0; j < data.pickList.get(i).data.size(); j++) {
                if (data.pickList.get(i).data.get(j).incode.equals(repIncode)) {
                    data.pickList.get(i).data.get(j).status = 2;
                    data.pickList.get(i).data.get(j).incode = trueIncode;
                    position = i;
                    break all;
                }
            }
        }
        if(position==0){

        }else{
            if(isToBottom(data,position)){
                toTop(data,position-1);
            }else{
                toTop(data,position);
            }
        }
        mView.changShelf(data);
        mView.hideLoading();

    }

    @Override
    public void queryBarCode(int groupPosition, int childPosition) {
        mView.showLoading("");
        mBiz.queryBarCode(mContext, data.pickList.get(groupPosition).data.get(childPosition).productId + "", data.pickList.get(groupPosition).data.get(childPosition).barcode, "PickingDetailActivityNew", new ICommonResult() {
            @Override
            public void onSuccess(String result) {
                mView.hideLoading();
                PickingResultActivity.show(mContext, result, 2);
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
    //当前货架号的店内码都已扫完 移动到底部
    private boolean isToBottom(PickingDetailNew pick,int position) {
        int num = 0;
        PickingDetailNew.ShelfInfo item = pick.pickList.get(position);
        for (int i = 0; i < item.data.size(); i++) {
            switch (item.data.get(i).status) {
                case 2:
                    num++;
                    break;
                case 3:
                    num++;
                    break;
            }
        }
        if(num == item.data.size()) {
            pick.pickList.remove(position);
            pick.pickList.add(item);
            return true;
        }

        return false;
    }

    //移动到顶部
    private void toTop(PickingDetailNew pick,int position) {
        PickingDetailNew.ShelfInfo item  = pick.pickList.get(position);
        data.pickList.remove(position);
        data.pickList.add(0,item);
    }

    @Override
    public void initData(PickingDetailNew data){

        List<PickingDetailNew.ShelfInfo> pickList = data.pickList;
        List<PickingDetailNew.ShelfInfo> backList = data.backupList;
        mView.showLoading("");

        new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i<pickList.size();i++){
                    for(int j = 0; j<pickList.get(i).data.size();j++){

                        pickList.get(i).data.get(j).setShelf(pickList.get(i).shelf);
                        pickList.get(i).data.get(j).setIsBackup(false);
                        dbOperator.insertData(pickList.get(i).data.get(j));
                    }
//                    dbOperator.insertDatas(pickList.get(i).data);
                }

                for(int i = 0; i<backList.size();i++){
                    for(int j = 0; j<backList.get(i).data.size();j++){

                        backList.get(i).data.get(j).setShelf(backList.get(i).shelf);
                        backList.get(i).data.get(j).setIsBackup(true);
                        dbOperator.insertData(backList.get(i).data.get(j));
                    }
//                    dbOperator.insertDatas(pickList.get(i).data);

                }

                mView.finishData();
            }
        }).start();




    }

    @Override
    public void query(String data) {
        currentList = dbOperator.getItemsByParameters(null,ProductInfoCaseDao.Properties.Rfid.eq(data),ProductInfoCaseDao.Properties.Status.notEq(2));
        if(currentList.size()>0){
            ProductInfoCase infoCase = currentList.get(0);
            mView.showFindDialog(infoCase);

        }else{

        }
    }

    @Override
    public void pickup(ProductInfoCase pick) {

        if(pick.isBackup){
            String incode = pick.getIncode();
            List<ProductInfoCase> list = dbOperator.getItemsByParameters(null,ProductInfoCaseDao.Properties.Barcode.eq(pick.barcode)
            ,ProductInfoCaseDao.Properties.IsBackup.eq(false)
            ,ProductInfoCaseDao.Properties.Status.notEq(2));

            if(list.size()>0){
                //扫到了同一批的店内码，显示是否替换
                mView.playBeep();
                trueIncode = pick.incode;
                repIncode = list.get(0).incode;
                mView.scanSwitch(true);
                mView.setRightIncode(pick.incode);

                int position = 0;
                all:
                for (int i = 0; i < data.pickList.size(); i++) {
                    for (int j = 0; j < data.pickList.get(i).data.size(); j++) {
                        if (data.pickList.get(i).data.get(j).incode.equals(repIncode)) {
                            data.pickList.get(i).data.get(j).status = 2;
                            data.pickList.get(i).data.get(j).incode = trueIncode;
                            data.pickList.get(i).data.get(j).rfid = pick.rfid;
                            position = i;
                            pick.status =2 ;
                            dbOperator.updateData(pick);
                            break all;
                        }
                    }
                }
                if(position==0){

                }else{
                    if(isToBottom(data,position)){
//                        toTop(data,position-1);
                    }else{
//                        toTop(data,position);
                    }
                }
                mView.changShelf(data);
                mView.hideLoading();

            }

        }else{

                int position = 0;
                //拣货成功 更改状态
                findIncode:
                for (int i = 0; i < data.pickList.size(); i++) {
                    for (int j = 0; j < data.pickList.get(i).data.size(); j++) {
                        if (data.pickList.get(i).data.get(j).detailId == (pick.detailId)) {
                            pick.setStatus(2);
                            data.pickList.get(i).data.get(j).status = pick.status;
                            data.pickList.get(i).data.get(j).incode = pick.incode;
                            dbOperator.updateData(pick);
                            position = i;
                            break findIncode;
                        }
                    }
                }

                if (isToBottom(data, position)) {

                } else {
                    toTop(data, position);
                }
                mView.changShelf(data);
                mView.scanSwitch(true);
                if (pick.status == 3) {
//                    mView.setWrongIncode(incodeOrShelf);
                    mView.playBeep();
                    mView.showNotice("该商品已取消");
                } else {
//                    mView.setRightIncode(incodeOrShelf);
                }
            }




    }

    @Override
    public void queryIncode(String data) {
        currentList = dbOperator.getItemsByParameters(null,ProductInfoCaseDao.Properties.Incode.eq(data),ProductInfoCaseDao.Properties.Status.notEq(2));
        if(currentList.size()>0){
            ProductInfoCase pick = currentList.get(0);
            if(pick.isBackup){
                String incode = pick.getIncode();
                List<ProductInfoCase> list = dbOperator.getItemsByParameters(null,ProductInfoCaseDao.Properties.Barcode.eq(pick.barcode)
                        ,ProductInfoCaseDao.Properties.IsBackup.eq(false)
                        ,ProductInfoCaseDao.Properties.Status.notEq(2));

                if(list.size()>0){
                    //扫到了同一批的店内码，显示是否替换
                    mView.playBeep();
                    trueIncode = pick.incode;
                    repIncode = list.get(0).incode;
                    mView.scanSwitch(true);
                    mView.setRightIncode(pick.incode);

                    int position = 0;
                    all:
                    for (int i = 0; i < this.data.pickList.size(); i++) {
                        for (int j = 0; j < this.data.pickList.get(i).data.size(); j++) {
                            if (this.data.pickList.get(i).data.get(j).incode.equals(repIncode)) {
                                this.data.pickList.get(i).data.get(j).status = 2;
                                this.data.pickList.get(i).data.get(j).incode = trueIncode;
                                this.data.pickList.get(i).data.get(j).rfid = pick.rfid;
                                position = i;
                                pick.status =2 ;
                                dbOperator.updateData(pick);
                                break all;
                            }
                        }
                    }
                    if(position==0){

                    }else{
                        if(isToBottom(this.data,position)){
//                        toTop(data,position-1);
                        }else{
//                        toTop(data,position);
                        }
                    }
                    mView.changShelf(this.data);
                    mView.hideLoading();

                }

            }else{

                int position = 0;
                //拣货成功 更改状态
                findIncode:
                for (int i = 0; i < this.data.pickList.size(); i++) {
                    for (int j = 0; j < this.data.pickList.get(i).data.size(); j++) {
                        if (this.data.pickList.get(i).data.get(j).detailId == (pick.detailId)) {
                            pick.setStatus(2);
                            this.data.pickList.get(i).data.get(j).status = pick.status;
                            this.data.pickList.get(i).data.get(j).incode = pick.incode;
                            dbOperator.updateData(pick);
                            position = i;
                            break findIncode;
                        }
                    }
                }

                if (isToBottom(this.data, position)) {

                } else {
                    toTop(this.data, position);
                }
                mView.changShelf(this.data);
                mView.scanSwitch(true);
                if (pick.status == 3) {
//                    mView.setWrongIncode(incodeOrShelf);
                    mView.playBeep();
                    mView.showNotice("该商品已取消");
                } else {
//                    mView.setRightIncode(incodeOrShelf);
                }
            }




        }else{

        }
    }
}
