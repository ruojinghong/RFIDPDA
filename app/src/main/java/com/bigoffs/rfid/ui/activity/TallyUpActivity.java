package com.bigoffs.rfid.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.bigoffs.rfid.R;
import com.bigoffs.rfid.mvp.bean.TallyTaskInfo;
import com.bigoffs.rfid.mvp.presenter.ITallyUpPresenter;
import com.bigoffs.rfid.mvp.presenter.TallyUpPresenter;
import com.bigoffs.rfid.mvp.view.ITallyUpView;


/**
 * Created by okbuy on 17-2-20.
 */

public class TallyUpActivity extends ScanBaseActivity implements ITallyUpView {

    // 任务单号
    private int mTaskId;
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
    // 上架按钮
    private TextView mTvUp;
    // 当前货架号
    private TextView mTvCurShelf;
    // 任务单号
    private TextView mTvTaskId;
    // 下架数量
    private TextView mTvDownNum;
    private LinearLayout mLlDownNum;
    // 上架数量
    private TextView mTvUpNum;
    private LinearLayout mLlUpNum;
    // 剩余数量
    private TextView mTvRemainNum;
    private LinearLayout mLlRemainNum;

    private ITallyUpPresenter mPresenter;


    public static void show(Context context, int taskId) {
        Intent intent = new Intent(context, TallyUpActivity.class);
        intent.putExtra("taskId", taskId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_tally_up);
        mTaskId = getIntent().getIntExtra("taskId", 0);
        mPresenter = new TallyUpPresenter(this, this);
        initViews();
        mPresenter.queryTaskInfo();
    }

    private void initViews() {
        mEtIncode = (EditText) findViewById(R.id.et_incode);
        mBtnCommit = (Button) findViewById(R.id.btn_commit);
        mTvIncode = (TextView) findViewById(R.id.tv_incode);
        mTvCheckDetail = (TextView) findViewById(R.id.tv_check_detail);
        mTvCount = (TextView) findViewById(R.id.tv_count);
        mTvRemove = (TextView) findViewById(R.id.tv_remove);
        mTvClear = (TextView) findViewById(R.id.tv_clear);
        mTvUp = (TextView) findViewById(R.id.tv_up);
        mTvCurShelf = (TextView) findViewById(R.id.tv_shelf_number);
        mTvTaskId = (TextView) findViewById(R.id.tv_task_id);
        mTvDownNum = (TextView) findViewById(R.id.tv_down_num);
        mTvUpNum = (TextView) findViewById(R.id.tv_up_num);
        mTvRemainNum = (TextView) findViewById(R.id.tv_remain_num);
        mLlDownNum = (LinearLayout) findViewById(R.id.ll_down_num);
        mLlUpNum = (LinearLayout) findViewById(R.id.ll_up_num);
        mLlRemainNum = (LinearLayout) findViewById(R.id.ll_remain_num);

        mBtnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIncode = mEtIncode.getText().toString().trim();
                if (mPresenter.hasShelfNumber()) {
                    mPresenter.queryUpIncode(mIncode);
                } else {
                    mPresenter.setShelfNumber(mIncode);
                }
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
                mPresenter.checkDetail();
            }
        });
        mLlDownNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.checkDown();
            }
        });
        mLlUpNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.checkUp();
            }
        });
        mLlRemainNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.checkRemain();
            }
        });
        mTvClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.clear();
            }
        });
        mTvUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.up();
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
    public void setShelfNumber(String shelfNumber) {
        setRightIncode(shelfNumber);
        mTvCurShelf.setText(shelfNumber);
    }

    @Override
    public void setTaskInfo(TallyTaskInfo info) {
        mTvTaskId.setText(info.taskId + "");
        mTvDownNum.setText(info.downNum + "");
        mTvUpNum.setText(info.upNum + "");
        mTvRemainNum.setText(info.remainNum + "");
    }

    @Override
    public void initIncodeEdit() {
        mEtIncode.getText().clear();
        mEtIncode.setHint("扫描店内码");
    }

    @Override
    public void resetIncode() {
        mTvIncode.setTextColor(Color.parseColor("#aaaaaa"));
        mTvIncode.setText(getString(R.string.text_no_scan));
    }

    @Override
    public int getTallyTaskId() {
        return mTaskId;
    }

    @Override
    public String getIncode() {
        return null;
    }

    @Override
    public void showOrHideRemoveBtn(boolean show) {
        mTvRemove.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void clear() {
        mEtIncode.setHint("扫描货架号");
        mEtIncode.getText().clear();
        mTvCurShelf.setText("无");
    }

    @Override
    public void playBeep() {
        beep();
    }

    @Override
    public void clearEdit() {
        mEtIncode.getText().clear();
    }

    @Override
    public void showCount(int count) {
        mTvCount.setText(count + "");
    }

    @Override
    public void goTallyDetail() {

    }

    @Override
    public void finishUp() {
        finish();
    }


    public void onReceiverData(String data) {
        if (mPresenter.hasShelfNumber()) {
            mPresenter.queryUpIncode(data);
        } else {
            mPresenter.setShelfNumber(data);
        }
    }

    protected void onScan(String barcode) {
        onReceiverData(barcode);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(TallyUpActivity.this)
                .setTitle("是否将当前店内码转移货架")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        TallyUpActivity.super.onBackPressed();
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
