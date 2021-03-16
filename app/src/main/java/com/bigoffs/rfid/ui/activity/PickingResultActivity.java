package com.bigoffs.rfid.ui.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bigoffs.rfid.R;
import com.bigoffs.rfid.mvp.bean.FinishPicking;
import com.bigoffs.rfid.mvp.bean.PickingBarCode;
import com.bigoffs.rfid.ui.adapter.PickingBarCodeAdapter;
import com.bigoffs.rfid.ui.adapter.PickingResultAdapter;
import com.google.gson.Gson;

public class PickingResultActivity extends BaseActivity{

    private LinearLayout llBarCode;

    private LinearLayout llNum;

    private TextView tvBarCode;
    private TextView tvSum,tvNum;
    private TextView tvState;

    private ListView listView;

    private int type;

    private String data;

    public static void show(Context context, String result, int type) {
        Intent intent = new Intent(context, PickingResultActivity.class);
        intent.putExtra("type",type);
        intent.putExtra("data", result);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.picking_result);
        type = getIntent().getIntExtra("type",0);
        data = getIntent().getStringExtra("data");
        initView();
    }

    private void initView() {
        llBarCode = (LinearLayout) findViewById(R.id.ll_barcode);
        llNum = (LinearLayout) findViewById(R.id.ll_num);
        tvBarCode = (TextView) findViewById(R.id.tv_barcode);
        tvSum = (TextView) findViewById(R.id.tv_sum);
        tvNum = (TextView) findViewById(R.id.tv_num);
        tvState = (TextView) findViewById(R.id.tv_status);
        listView = (ListView) findViewById(R.id.lv);
        if(type ==1){
            Gson gson = new Gson();
            FinishPicking picking = gson.fromJson(data,FinishPicking.class);
            tvSum.setText("应拣："+picking.total);
            tvNum.setText("已拣："+picking.pickNum);
            llNum.setVisibility(View.VISIBLE);
            if(picking.incodeList!= null) {
                listView.setAdapter(new PickingResultAdapter(this, picking.incodeList));
            }
        }else if(type == 2){
            Gson gson = new Gson();
            PickingBarCode picking = gson.fromJson(data,PickingBarCode.class);
            tvBarCode.setText("条形码："+picking.barcode);
            llBarCode.setVisibility(View.VISIBLE);
            if(picking.incodeList!= null) {
                listView.setAdapter(new PickingBarCodeAdapter(this, picking.incodeList));
            }
        }

    }
}