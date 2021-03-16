package com.bigoffs.rfid.ui.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.bigoffs.rfid.R;
import com.bigoffs.rfid.listener.ISignListener;
import com.bigoffs.rfid.listener.OnFinishListener;
import com.bigoffs.rfid.mvp.bean.PickingDetail;
import com.bigoffs.rfid.mvp.bean.PickingDetailNew;
import com.bigoffs.rfid.mvp.bean.dao.ProductInfoCase;
import com.bigoffs.rfid.mvp.presenter.IPickingPresenter;
import com.bigoffs.rfid.mvp.presenter.PickingPresenter;
import com.bigoffs.rfid.mvp.view.IPickingDetailView;
import com.bigoffs.rfid.ui.adapter.PinkingDetailAdapter;
import com.bigoffs.rfid.ui.custom.IPickListener;
import com.bigoffs.rfid.ui.custom.PickDialog;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class PickingDetailActivityNew extends ScanBaseActivity implements IPickingDetailView {
    //整页数据
    private PickingDetailNew data;

    private TextView tv_picking;
    private PinkingDetailAdapter adapter;
    private TextView tv_pick_code;
    private PickDialog dialog;

    private EditText mEtIncode;// 货架号或店内码的输入框
    private Button mBtnCancel;// 取消按钮
    private TextView mTvIncode;// 显示扫描的店内码或者货架号
    private TextView mTvShelfNumber;// 当前货架号
    private TextView mTvShelfProgress;// 当前货架扫描进度
    private ExpandableListView listView;// 当前货架号的店内码列表
    private TextView mTvShelf1;// 待拣货架1
    private TextView mTvShelf2;// 待拣货架2
    private TextView mTvPickingProgress;// 拣货单进度
    private TextView mTvFinishPicking;// 完成拣货按钮

    private IPickingPresenter mPresenter;// Presenter

    public static void show(Context context, int taskId, String outstockCode) {
        Intent intent = new Intent(context, PickingDetailActivityNew.class);
        intent.putExtra("taskId", taskId);
        intent.putExtra("outstockCode",outstockCode);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_picking_detail_new);

        getData();
        mPresenter = new PickingPresenter(this, this,data);
        initViews();
        mPresenter.setTaskId(getPickingTaskId());
        scanPresenter.setListener(new OnFinishListener() {
            @Override
            public void OnFinish(String data) {
                if(!dialog.isShowing()){
                    mPresenter.query(data);
                }
            }
        });
    }

    private void initViews() {
        tv_picking = (TextView) findViewById(R.id.tv_picking);
        tv_picking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //TODO 开始拣货咯
                openOrClose();
            }
        });
        tv_pick_code = findViewById(R.id.tv_pick_code);
        mEtIncode = (EditText) findViewById(R.id.et_incode);
        mBtnCancel = (Button) findViewById(R.id.btn_cancel);
        mTvIncode = (TextView) findViewById(R.id.tv_incode);
        mTvShelfNumber = (TextView) findViewById(R.id.tv_shelf_number);
        mTvShelfProgress = (TextView) findViewById(R.id.tv_shelf_picking_progress);
        listView = (ExpandableListView) findViewById(R.id.listview);
        dialog = new PickDialog(this, R.style.ActionSheetDialogStyle, scanPresenter, new ArrayList<>());
        dialog.setListener(new IPickListener() {
            @Override
            public void Pickup(ProductInfoCase infoCase) {
                mPresenter.pickup(infoCase);
            }
        });
        adapter = new PinkingDetailAdapter(this,data);
        listView.setAdapter(adapter);
        if(data.backupList.size() > 0) {


            listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                    mPresenter.queryBarCode(groupPosition, childPosition);
                    return false;

                }
            });
        }
        mEtIncode.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (event != null)
                    if (event.getAction() == KeyEvent.ACTION_UP) return true;
                if (actionId == EditorInfo.IME_ACTION_SEND || actionId == 0) {
                    if (mEtIncode.getText().toString().equals("")) {
                        showDialog("请输入标签~");
                    } else {

                        onReceiverData(mEtIncode.getText().toString());
                        mEtIncode.setText("");
                    }
                    return true;
                }
                return false;
            }
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

        mTvShelf1 = (TextView) findViewById(R.id.tv_shelf1);
        mTvShelf2 = (TextView) findViewById(R.id.tv_shelf2);
        mTvPickingProgress = (TextView) findViewById(R.id.tv_picking_progress);
        mTvFinishPicking = (TextView) findViewById(R.id.tv_finish_picking);
        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(PickingDetailActivityNew.this).setMessage("确定取消任务？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPresenter.cancleTask(data.taskId+"",data.outstockId);
                    }
                }).setNegativeButton("返回",null).show();
            }
        });
        mTvFinishPicking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv_picking.getText().toString().equals("停止拣货")) {openOrClose();}
                mPresenter.showFinishDialog();
            }
        });
        if(data!= null) {
            updateCountNum(data);
            tv_pick_code.setText(data.outstockCode);
        }


    }

    @Override
    public String getEditString() {
        return mEtIncode.getText().toString().trim().toUpperCase();
    }

    @Override
    public void clearEdit() {
        mEtIncode.getText().clear();
    }

    @Override
    public int getPickingTaskId() {
        return getIntent().getIntExtra("taskId", 0);
    }

    @Override
    public void setRightIncode(String incode) {
        mTvIncode.setTextColor(Color.rgb(0, 87, 55));
        mTvIncode.setText(incode);
    }

    @Override
    public void setWrongIncode(String incode) {
        mTvIncode.setTextColor(Color.RED);
        mTvIncode.setText(incode);
    }




    @Override
    public void setPickInfo(PickingDetail info) {
        if (info.shelfInfos.size() > 0) {
            // 设置当前货架信息
            PickingDetail.ShelfInfo curShelfInfo = info.shelfInfos.get(0);
            mTvShelfNumber.setText("货架号：" + curShelfInfo.shelfNumber);
            mTvShelfProgress.setText(curShelfInfo.finishNum + "/" + curShelfInfo.totalNum);
            // 店内码列表
//            listView.setAdapter(new PickingIncodeAdapter(curShelfInfo.incodes));
            if (info.shelfInfos.size() > 1) {
                // 设置待拣货架1
                PickingDetail.ShelfInfo shelfInfo1 = info.shelfInfos.get(1);
                mTvShelf1.setText(shelfInfo1.shelfNumber + "   " + (shelfInfo1.totalNum - shelfInfo1.finishNum));
            } else {
                mTvShelf1.setText(null);
            }
            if (info.shelfInfos.size() > 2) {
                // 设置待拣货架2
                PickingDetail.ShelfInfo shelfInfo2 = info.shelfInfos.get(2);
                mTvShelf2.setText(shelfInfo2.shelfNumber + "   " + (shelfInfo2.totalNum - shelfInfo2.finishNum));
            } else {
                mTvShelf2.setText(null);
            }
        }
        // 拣货单进度
        mTvPickingProgress.setText(info.finishNum + "/" + info.totalNum);
    }

    @Override
    public void finishPicking() {
        finish();
    }

    @Override
    public void changShelf(PickingDetailNew info) {
        data = info;
        adapter.notifyDataSetChanged();
        goListViewTop();
        for (int i =0;i<data.pickList.size();i++){
            listView.collapseGroup(i);
        }
        listView.expandGroup(0);
        mTvShelfNumber.setText("当前货架号：" + data.pickList.get(0).shelf);
        updateCountNum(info);
    }

    @Override
    public void scanSwitch(boolean b) {

    }

    private  void  updateCountNum(PickingDetailNew info){
        int count = 0;
        int pick = 0;
        for (int i =0;i<info.pickList.size();i++){
            for(int j = 0;j<info.pickList.get(i).data.size();j++){
                count++;
                switch (info.pickList.get(i).data.get(j).status){
                    //待拣
                    case 2:
                        pick++;
                        break;
                    //统计已取消
                    case 3:
                        count--;
                        break;
                }
            }
        }
        mTvShelfProgress.setText(pick + "/" + count);
    }



    @Override
    public void playBeep() {
        beep();
    }

    @Override
    public void goListViewTop() {
        listView.smoothScrollToPosition(0);
    }

    @Override
    public void closeActivity() {
        finish();
    }

    @Override
    public void finishData() {
        mTvIncode.post(()->{
           hideLoading();
        });
    }

    @Override
    public void showFindDialog(ProductInfoCase infoCase) {
        dialog.show(infoCase);
    }

    @Override
    public void startScan() {

        scanPresenter.setReadDataModel(0);
        scanPresenter.startReadRfid();
    }

    @Override
    public void colseChildOnclick() {

    }


    protected void onScan(String barcode) {
        onReceiverData(barcode);
    }


    public void onReceiverData(String data) {
        if(!dialog.isShowing()){
            mPresenter.queryIncode(data);
            clearEdit();
        }
    }

    public void getData(){
        Gson gson = new Gson();
        data = gson.fromJson(getIntent().getStringExtra("outstockCode"),PickingDetailNew.class);

        Collections.sort(data.pickList, new Comparator<PickingDetailNew.ShelfInfo>() {
            @Override
            public int compare(PickingDetailNew.ShelfInfo o1, PickingDetailNew.ShelfInfo o2) {
                return o1.shelf.compareTo(o2.shelf);
            }
        });
        Iterator<PickingDetailNew.ShelfInfo> it = data.pickList.iterator();
        List<PickingDetailNew.ShelfInfo> offList = new ArrayList<>();
        while (it.hasNext()){
            PickingDetailNew.ShelfInfo shelfInfo = it.next();
            int i = 0;
            for (ProductInfoCase productInfo : shelfInfo.data) {
                if(  productInfo.status == 2){
                    i++;
                }
            }
            if(i == shelfInfo.data.size()) {
                offList.add(shelfInfo);
                it.remove();
            }
        }

        data.pickList.addAll(offList);



    }


    @Override
    public void OnFinish(String data) {


        if(!dialog.isShowing()){
            mPresenter.query(data);
        }

    }

    @Override
    public void ShowData(Object o) {

    }


    public void openOrClose() {
        if (tv_picking.getText().toString().equals("停止拣货")) {
            scanPresenter.stopReadRfid();
//            mTvAllSignMember.setText(legitimateList.size()+errorList.size() + "");
//            mRlOpenOrClose.setBackground(getDrawable(R.drawable.bg_circle));
//            mActivity.tbCommon.setVisibility(View.VISIBLE);

            tv_picking.setText("开始拣货");
        } else {


            scanPresenter.setReadDataModel(0);
            scanPresenter.startReadRfid();

//            mRlOpenOrClose.setBackground(getDrawable(R.drawable.bg_circle_red));
//                mActivity.tbCommon.setVisibility(View.INVISIBLE);
            tv_picking.setText("停止拣货");

        }

    }
}
