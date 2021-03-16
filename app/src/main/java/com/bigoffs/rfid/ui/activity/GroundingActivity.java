package com.bigoffs.rfid.ui.activity;

import android.graphics.Color;
import android.os.Bundle;

import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;


import androidx.annotation.Nullable;

import com.bigoffs.rfid.R;
import com.bigoffs.rfid.mvp.bean.GroundingBillList;
import com.bigoffs.rfid.mvp.presenter.GroundingPresenter;
import com.bigoffs.rfid.mvp.presenter.IGroundingPresenter;
import com.bigoffs.rfid.mvp.view.IGroundingView;
import com.bigoffs.rfid.ui.adapter.GroundingListAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * @author okbuy-001
 * @desc
 * @time 2018/12/6 9:33
 */
public class GroundingActivity extends BaseActivity implements IGroundingView,GroundingListAdapter.AdapterBtnCallBack {
    // 店内码的输入框
    private EditText mEtIncode;
    // 提交按钮
    private Button mBtnCommit;
    // 扫描或者输入的店内码
    private String mIncode;
    // 显示店内码的TextView
    private TextView mTvIncode;
    //错误TextView
    private TextView mTvError;
    //下拉列表
    private Spinner mSp;
    private List<String> data_list;
    private ArrayAdapter<String> arr_adapter;

    private ListView mListView;
    private IGroundingPresenter mPresenter = new GroundingPresenter(this,this);
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_grounding);
        initViews();
    }
    private void initViews() {
        mListView = (ListView) findViewById(R.id.lv_list);
        mEtIncode = (EditText) findViewById(R.id.et_incode);
        mBtnCommit = (Button) findViewById(R.id.btn_commit);
        mBtnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIncode = mEtIncode.getText().toString().trim();
                mPresenter.query();
            }
        });
        mTvError = (TextView) findViewById(R.id.tv_error);
        mTvIncode = (TextView) findViewById(R.id.tv_incode);

        mSp = (Spinner) findViewById(R.id.sp_code);
        data_list = new ArrayList<String>();
        data_list.add("条形码");
        data_list.add("店内码");
        data_list.add("调拨单");
        //适配器
        arr_adapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data_list);
        //设置样式
        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        mSp.setAdapter(arr_adapter);
        mEtIncode.setShowSoftInputOnFocus(false);
        mEtIncode.setImeOptions(EditorInfo.IME_ACTION_SEND);
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
                    }
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void showList(GroundingBillList list) {
        mTvError.setText("");
        mListView.setAdapter(new GroundingListAdapter(this,list,this));
    }

    @Override
    public String getIncode() {
        return mIncode;
    }

    @Override
    public void setIncode(String incode) {
        if (!TextUtils.isEmpty(incode)) {
            // 墨绿
            mTvIncode.setTextColor(Color.rgb(0, 87, 55));
            mTvIncode.setText(incode);
            mTvError.setText("");
        }
    }

    @Override
    public void setErrIncode(String incode) {
        mTvIncode.setTextColor(Color.RED);
        mTvIncode.setText(incode);
        mTvError.setText("货品不存在");
    }



    @Override
    public String getChoose() {
        return mSp.getSelectedItem().toString();
    }

    @Override
    public void clearList() {
        mListView.setAdapter(null);
    }

    @Override
    public void goDetailActivity(String arrivalId, String arrivalCode, String taskId, int type) {
        /**
         * 根据adapter里的 是否有arrival 数组来判断TYPE type = 0 时跳转新品入库，否则是 调拨入库
         */
        if(type == 0) {
            GroundingDetailActivity.show(this,taskId,arrivalId,arrivalCode);

        }else{
            GroundingAllocationDetailActivity.show(this,taskId,arrivalId,arrivalCode);

        }
    }




    public void onReceiverData(String data) {
        mIncode = data;
        setIncode(mIncode);
        mPresenter.query();
        mEtIncode.setText("");
    }

    @Override
    public void ClickCallBack(int position, int type) {
        mPresenter.receive(position,type);
    }
}
