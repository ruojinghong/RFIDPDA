package com.bigoffs.rfid.ui.activity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.bigoffs.rfid.R;
import com.bigoffs.rfid.mvp.presenter.ITallyDownPresenter;
import com.bigoffs.rfid.mvp.presenter.TallyDownPresenter;
import com.bigoffs.rfid.mvp.view.ITallyDownView;


/**
 * Created by okbuy on 17-2-17.
 * 理货-下架页面
 */

public class TallyDownActivity extends ScanBaseActivity implements ITallyDownView {

    // 店内码输入框
    private EditText mEtIncode;
    // 提交按钮
    private Button mBtnCommit;
    // 店内码
    private TextView mTvIncode;
    // 查看明细
    private TextView mTvCheckDetail;
    // 店内码数量
    private TextView mTvCount;

    private String mIncode;
    // 移除按钮
    private TextView mTvRemove;
    // 清空按钮
    private TextView mTvClear;
    // 下架按钮
    private TextView mTvDown;

    private ITallyDownPresenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_tally_down);
        mPresenter = new TallyDownPresenter(this, this);
        initViews();
    }

    private void initViews() {
        mEtIncode = (EditText) findViewById(R.id.et_incode);
        mBtnCommit = (Button) findViewById(R.id.btn_commit);
        mTvIncode = (TextView) findViewById(R.id.tv_incode);
        mTvCheckDetail = (TextView) findViewById(R.id.tv_check_detail);
        mTvCount = (TextView) findViewById(R.id.tv_count);
        mTvRemove = (TextView) findViewById(R.id.tv_remove);
        mTvClear = (TextView) findViewById(R.id.tv_clear);
        mTvDown = (TextView) findViewById(R.id.tv_down);

        mBtnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIncode = mEtIncode.getText().toString().trim();
                mPresenter.query(mIncode);
            }
        });
        mTvRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.remove();
            }
        });
        mTvCheckDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.goTallyDetail();
            }
        });
        mTvClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.clear();
            }
        });
        mTvDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.down();
            }
        });
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
    public void resetIncode() {
        mTvIncode.setTextColor(Color.parseColor("#aaaaaa"));
        mTvIncode.setText(getString(R.string.text_no_scan));
    }

    @Override
    public String getIncode() {
        return mIncode;
    }

    @Override
    public void showOrHideRemoveBtn(boolean show) {
        mTvRemove.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void clear() {
        mEtIncode.getText().clear();
    }

    @Override
    public void playBeep() {
        beep();
    }

    @Override
    public void goTallyUp(int taskId) {
        TallyUpActivity.show(this, taskId);
        finish();
    }

    @Override
    public void showCount(int count) {
        mTvCount.setText(count + "");
    }

    @Override
    public void goTallyDetail() {
//        TallyDetailActivity.show(this, mPresenter.getData());
    }


    public void onReceiverData(String data) {
        mIncode = data;
        mPresenter.query(mIncode);
    }


    protected void onScan(String barcode) {
        onReceiverData(barcode);
    }

    @Override
    public void onBackPressed() {
         new AlertDialog.Builder(TallyDownActivity.this)
                .setTitle("是否将当前店内码转移货架")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        TallyDownActivity.super.onBackPressed();
                    }
                }).setNegativeButton("取消",null).show();

    }

    @Override
    public void OnFinish(String data) {

    }

    @Override
    public void ShowData(Object o) {

    }
}
