package com.bigoffs.rfid.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bigoffs.rfid.R;
import com.bigoffs.rfid.mvp.bean.TransferInfo;
import com.bigoffs.rfid.mvp.presenter.ITallyPresenter;
import com.bigoffs.rfid.mvp.presenter.TallyPresenter;
import com.bigoffs.rfid.mvp.view.ITallyView;
import com.bigoffs.rfid.persistence.util.SoundUtils;
import com.bigoffs.rfid.ui.adapter.TallyAdapter;
import com.bigoffs.rfid.ui.custom.SquareRelativeLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author okbuy-001
 * @desc 理货界面
 * @time 2018/12/11 10:23
 */
public class TallyActivity extends ScanBaseActivity implements ITallyView, TextView.OnEditorActionListener {


    @BindView(R.id.rl_open_or_close)
    SquareRelativeLayout mRlOpenOrClose;
    @BindView(R.id.tv_content)
    TextView mTvContent;
    @BindView(R.id.tv_all_scan)
    TextView mTvAllScan;
    //店内码输入框
    private EditText edIncode;
    //货架码输入框
    private EditText edShelfCode;

    //已扫数量
    private TextView countNum;
    //RecyclerView
    private RecyclerView mRecylerView;

    private TextView tvClear;

    private TextView tvUp;
    //光标位置
    private int mPosition = 1;
    //已扫数量
    private int mCountNum = 0;

    //到货单ID
    private String arrivalId;

    private View dialogView;

    private AlertDialog dialog;
    private EditText dialogEt;
    //错误集合
    private List<TransferInfo.FailIncodesBean> mData = new ArrayList<>();
    //扫到的RFID集合
    private List<String> rfids = new ArrayList<>();

    //扫到的店内码集合
    private Map<String, String> incodes = new HashMap<>();

    private ITallyPresenter mPresenter = new TallyPresenter(this, this);

    private TallyAdapter adapter;

    private TextView tvCount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tally);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initView();
    }

    void initView() {
        LayoutInflater factory = LayoutInflater.from(this);//提示框
        dialogView = factory.inflate(R.layout.dialog_edittext, null);
        dialogEt = (EditText) dialogView.findViewById(R.id.editText);//获得输入框对象
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("code");
        edIncode = (EditText) findViewById(R.id.et_incode);
        edIncode.setShowSoftInputOnFocus(false);
        edIncode.setImeOptions(EditorInfo.IME_ACTION_SEND);
        edIncode.setOnEditorActionListener(this);
        edShelfCode = (EditText) findViewById(R.id.et_shelf_code);
        edShelfCode.setShowSoftInputOnFocus(false);
        edShelfCode.setImeOptions(EditorInfo.IME_ACTION_SEND);
        edShelfCode.setOnEditorActionListener(this);

        countNum = (TextView) findViewById(R.id.count_num);
        mRecylerView = (RecyclerView) findViewById(R.id.rv_grounding);
        tvClear = (TextView) findViewById(R.id.tv_clear);
        tvClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.clearAllItem();
            }
        });
        tvUp = (TextView) findViewById(R.id.tv_up);
        tvUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTvContent.getText().toString().equals("停止扫描")){
                    openOrClose();
                }
                dialog = new AlertDialog.Builder(TallyActivity.this)
                        .setTitle("是否将当前店内码转移货架")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                mPresenter.moveShelf();
                            }
                        }).setNegativeButton("取消", null).show();

            }
        });
        edIncode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mPosition = 2;
                }
            }
        });

        edShelfCode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mPosition = 1;
                }
            }
        });
        adapter = new TallyAdapter(mData);
        mRecylerView.setLayoutManager(new LinearLayoutManager(this));
        mRecylerView.setAdapter(adapter);
        tvCount = (TextView) findViewById(R.id.tv_count);
        mRlOpenOrClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(mPresenter.getCurrentShelf())){
                        playBeep();
                        showNotice("请先扫描货架位");
                }else {
                    openOrClose();
                }

            }
        });
    }

    public void openOrClose() {
        if (mTvContent.getText().toString().equals("停止扫描")) {
            scanPresenter.stopReadRfid();
            mTvAllScan.setText(incodes.size() + rfids.size() + "");
            mRlOpenOrClose.setBackground(getDrawable(R.drawable.bg_circle));
//            mActivity.tbCommon.setVisibility(View.VISIBLE);

            mTvContent.setText("开始扫描");
        } else {


            scanPresenter.startReadRfid();

            mRlOpenOrClose.setBackground(getDrawable(R.drawable.bg_circle_red));
//                mActivity.tbCommon.setVisibility(View.INVISIBLE);
            mTvContent.setText("停止扫描");

        }

    }

    @Override
    public void showCode(String code) {

        // 墨绿
//            tvCode.setTextColor(Color.rgb(0, 87, 55));
//            tvCode.setText(code);

    }

    @Override
    public void showErrMessage(String code) {
        playBeep();
        if (!TextUtils.isEmpty(code)) {
            // 墨绿
//            tvCode.setTextColor(Color.RED);
//            tvCode.setText(code);
            scanSwitch(true);
        }
    }

    protected void onScan(String barcode) {
        onReceiverData(barcode);
    }


    public void onReceiverData(String data) {
        data = data.trim();
        switch (mPosition) {
            //扫描
            case 2:
                if (mTvContent.getText().toString().equals("停止扫描")){
                    openOrClose();
                }
                mPresenter.queryInCode(data);
                edIncode.setText("");
                break;
            //扫描条形码
            case 1:

                mPresenter.queryShelfCode(data);
                edShelfCode.setText("");
                break;
        }


    }

    @Override
    public void getFoucs(int i) {
        switch (i) {
            //扫描店内吗
            case 2:
                edIncode.requestFocus();
                break;
            //扫描货架号
            case 1:
                edShelfCode.requestFocus();
                break;

        }
    }

    @Override
    public void showList(List<TransferInfo.FailIncodesBean> list) {
        mData.clear();
        mData.addAll(list);
        adapter.notifyDataSetChanged();
        setCount(mData.size());
    }

    @Override
    public void scanSwitch(boolean b) {

    }

    @Override
    public void showCurrentShelf(String text) {
        countNum.setText(text);
    }

    @Override
    public void clearPage() {
        if (mTvContent.getText().toString().equals("停止扫描")) {openOrClose();}
        countNum.setText("");
        showCode("");
        edShelfCode.requestFocus();
        scanPresenter.initData();
        incodes.clear();
        rfids.clear();
        mTvAllScan.setText(incodes.size() + rfids.size() + "");
    }

    @Override
    public void playBeep() {
        SoundUtils.play(2);
    }

    @Override
    public void setCount(int count) {
        tvCount.setText("数量：" + count);
    }

    @Override
    public Map<String, String> getIncodes() {
        return incodes;
    }

    @Override
    public void updateListCount() {
        mTvAllScan.setText(incodes.size() + rfids.size() + "");
    }

    @Override
    public  List<String> getRfids() {
        return rfids;
    }

    @Override
    public void OnFinish(String data) {
            SoundUtils.play(1);
            rfids.add(data);
            mTvAllScan.setText(incodes.size() + rfids.size() + "");
    }

    @Override
    public void ShowData(Object o) {

    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        switch (v.getId()) {
            case R.id.et_incode:
                if (event != null)
                    if (event.getAction() == KeyEvent.ACTION_UP) return true;
                if (actionId == EditorInfo.IME_ACTION_SEND || actionId == 0) {
                    if (edIncode.getText().toString().equals("")) {
                        showDialog("请输入标签~");
                    } else {

                        onReceiverData(edIncode.getText().toString());

                    }
                }
                break;
            case R.id.et_shelf_code:
                if (event != null)
                    if (event.getAction() == KeyEvent.ACTION_UP) return true;
                if (actionId == EditorInfo.IME_ACTION_SEND || actionId == 0) {
                    if (edShelfCode.getText().toString().equals("")) {
                        showDialog("请输入标签~");
                    } else {
                        onReceiverData(edShelfCode.getText().toString());

                    }
                }
                break;

        }

        return false;
    }
}
