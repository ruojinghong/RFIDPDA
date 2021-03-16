package com.bigoffs.rfid.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bigoffs.rfid.R;
import com.bigoffs.rfid.listener.ISignListener;
import com.bigoffs.rfid.mvp.bean.dao.GroundingAllocationDetailInfo;
import com.bigoffs.rfid.mvp.bean.dao.PutAwayInfo;
import com.bigoffs.rfid.mvp.presenter.GroundingAllocationDetailPresenter;
import com.bigoffs.rfid.mvp.presenter.IGroundingAllocationDetailPresenter;
import com.bigoffs.rfid.mvp.view.IGroundingAllocationDetailView;
import com.bigoffs.rfid.persistence.util.SoundUtils;
import com.bigoffs.rfid.ui.adapter.GroundingAllocationDetailInfoAdapter;
import com.bigoffs.rfid.ui.custom.GroundingAllocationDialog;
import com.bigoffs.rfid.ui.custom.SquareRelativeLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author okbuy-001
 * @desc 调拨入库界面
 * @time 2019/1/8 15:25
 */
public class GroundingAllocationDetailActivity extends ScanBaseActivity implements IGroundingAllocationDetailView, TextView.OnEditorActionListener {
    @BindView(R.id.tv_content)
    TextView mTvContent;
    @BindView(R.id.tv_all_scan)
    TextView mTvAllScan;
    @BindView(R.id.rl_open_or_close)
    SquareRelativeLayout mRlOpenOrClose;
    @BindView(R.id.total_num)
    TextView mTotalNum;
    @BindView(R.id.sign_num)
    TextView mSignNum;
    @BindView(R.id.tv_sign_allocation)
    TextView mTvSignAllocation;
    @BindView(R.id.tv_reset)
    TextView mTvReset;
    //调拨单号
    private TextView tvTitle;
    //当前扫描结果
    private TextView tvCode;
    //店内码输入框
    private EditText edIncode;
    //货架码输入框
    private EditText edShelfCode;
    //剔除按钮
    private Button btDelete;
    //已扫数量
    private TextView countNum;
    //RecyclerView
    private RecyclerView mRecylerView;

    private TextView tvClear;

    private TextView tvUp;
    //光标位置
    private int mPosition = 1;

    //到货单ID
    private String arrivalId;
    private String arrivalCode;

    private View dialogView;

    private AlertDialog dialog;
    private GroundingAllocationDialog queryDialog;
    private EditText dialogEt;

    private String taskId;
    //当前货架号
    private String currentShelf;

    //不存在的改调拨单
    private ArrayList<GroundingAllocationDetailInfo> errorList = new ArrayList<>();
    //扫描到的合法的RFID
    private ArrayList<String> legitimateList = new ArrayList<>();
    private GroundingAllocationDetailInfoAdapter adapter = new GroundingAllocationDetailInfoAdapter(errorList);

    private IGroundingAllocationDetailPresenter mPresenter = new GroundingAllocationDetailPresenter(this, this);

    private Context mContext;
    private List<String> legitimateIncodeList = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar();
        setContentView(R.layout.activity_grounding_allocation_detail);
        ButterKnife.bind(this);
        mContext = this;
        initView();
    }

    public static void show(Context context, String taskId, String allcationId, String allocationCode) {
        Intent intent = new Intent(context, GroundingAllocationDetailActivity.class);
        intent.putExtra("taskId", taskId);
        intent.putExtra("code", allocationCode);
        intent.putExtra("id", allcationId);
        context.startActivity(intent);
    }

    void initView() {
        LayoutInflater factory = LayoutInflater.from(this);//提示框
        dialogView = factory.inflate(R.layout.dialog_edittext, null);
        dialog = new AlertDialog.Builder(this)
                .setTitle("请扫描剔除店内码")//提示框标题
                .setView(dialogView)
                .setPositiveButton("剔除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        if (!TextUtils.isEmpty(dialogEt.getText())) {
                            mPresenter.deleteItem(dialogEt.getText().toString());
                        } else {
                            showNotice("请输入要剔除的店内码");
                        }
                        dialogEt.setText("");
                    }
                }).setNegativeButton("取消",
                        null).create();
        queryDialog = new GroundingAllocationDialog(mContext, R.style.selectorDialog, scanPresenter, errorList);
        queryDialog.setListener(new ISignListener() {
            @Override
            public void deleteItem(int position) {
                mPresenter.deleteError(errorList.get(position));
                errorList.remove(position);

                adapter.notifyDataSetChanged();
//        mTvErrorNum.setText("错误数量：" + errorList.size());
                if (queryDialog.isNext) {
                    //TODO 自动切换到下一个
                    if (errorList.size() > 0) {
                        queryDialog.change(0);
                    } else {
                        queryDialog.dismiss();
                    }


                } else {
                    queryDialog.dismiss();
                }
            }

            @Override
            public void setItemIncode(String incode) {
                queryDialog.setCurrenIncode(incode);
            }

            @Override
            public void queryIncode(String rfid) {
                mPresenter.fromRfidgetIncode(rfid);
            }
        });
        dialogEt = (EditText) dialogView.findViewById(R.id.editText);//获得输入框对象
        tvTitle = (TextView) findViewById(R.id.tv_title);
        Intent intent = getIntent();
        arrivalId = intent.getStringExtra("id");
        arrivalCode = intent.getStringExtra("code");
        taskId = intent.getStringExtra("taskId");
        tvTitle.setText("调拨单号:" + arrivalCode);
        tvCode = (TextView) findViewById(R.id.tv_code);
        edIncode = (EditText) findViewById(R.id.et_incode);
        edShelfCode = (EditText) findViewById(R.id.et_shelf_code);
//        btDelete = (Button) findViewById(R.id.bt_delete);
//        btDelete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mPresenter.deleteFirstItem();
//            }
//        });
        countNum = (TextView) findViewById(R.id.count_num);
        mRecylerView = (RecyclerView) findViewById(R.id.rv_grounding);
        tvClear = (TextView) findViewById(R.id.tv_clear);
        tvClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
        tvUp = (TextView) findViewById(R.id.tv_up);
        tvUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edShelfCode.getText().toString().equals("")) {
                    showNotice("请先扫描货架号");
                    return;
                }

                new AlertDialog.Builder(GroundingAllocationDetailActivity.this)
                        .setTitle("确定要上架吗？")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mPresenter.upData(edShelfCode.getText().toString(), legitimateList, errorList, arrivalCode);
                            }
                        }).setNegativeButton("取消", null).show();

            }
        });
        edIncode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mPosition = 1;
                }
            }
        });

        edShelfCode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mPosition = 3;
                }
            }
        });

        mRecylerView.setLayoutManager(new LinearLayoutManager(this));
        mRecylerView.setAdapter(adapter);
        mPresenter.queryHistory(arrivalCode);

        edIncode.setShowSoftInputOnFocus(false);
        edIncode.setImeOptions(EditorInfo.IME_ACTION_SEND);
        edIncode.setOnEditorActionListener(this);
        edShelfCode.setShowSoftInputOnFocus(false);
        edShelfCode.setImeOptions(EditorInfo.IME_ACTION_SEND);
        edShelfCode.setOnEditorActionListener(this);
        adapter.setOnItemClickListener(new GroundingAllocationDetailInfoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                if (mTvContent.getText().equals("开始扫描")) {
                    queryDialog.show(errorList, position, false);
                } else {
                    ShowToast("请先关闭扫描");
                }
            }
        });
        mTvSignAllocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(GroundingAllocationDetailActivity.this)
                        .setTitle("确定完成签收吗？")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mPresenter.upData(arrivalCode);
                            }
                        }).setNegativeButton("取消", null).show();

            }
        });
//        mTvReset.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

    }


    public void onReceiverData(String data) {
        if (dialog.isShowing()) {

        } else {
            switch (mPosition) {
                //扫描店内吗
                case 1:
                    if(mTvContent.getText().toString().equals("停止扫描")){
                        openOrClose();
                    }
                    mPresenter.queryInCode(data);
                    break;

                //扫描条形码
                case 3:
                    if(mTvContent.getText().toString().equals("停止扫描")){
                        openOrClose();
                    }
                    mPresenter.queryShelfCode(data);
                    break;
            }
        }

    }


    protected void onScan(String barcode) {
        onReceiverData(barcode);
    }


    @Override
    public void showCode(String code) {
        if (!TextUtils.isEmpty(code)) {
            // 墨绿
            scanSwitch(true);
            tvCode.setTextColor(Color.rgb(0, 87, 55));
            tvCode.setText(code);

        }
    }

    @Override
    public void showErrMessage(String code) {
        if (!TextUtils.isEmpty(code)) {
            beep();
            tvCode.setTextColor(Color.RED);
            tvCode.setText(code);

            scanSwitch(true);
        }
    }


    @Override
    public void showList(List<GroundingAllocationDetailInfo> list) {
        countNum.setText(list.size() + "");
        errorList.clear();
        errorList.addAll(list);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void getFoucs(int i) {

        switch (i) {
            //扫描店内吗
            case 1:

                edIncode.requestFocus();
                break;


            //扫描货架号
            case 3:

                edShelfCode.requestFocus();
                break;

        }


    }

    @Override
    public String getArrivalId() {
        return arrivalId;
    }

    @Override
    public void showDialog() {
        if (errorList.size() <= 0) {
            return;
        }
        queryDialog.show(errorList, 0, true);
    }

    @Override
    public void scanSwitch(boolean b) {

    }

    @Override
    public void playBeep() {
        beep();
    }

    @Override
    public void goDetail(int num) {
//        Intent it = new Intent(this, FinishGroundingActitivy.class);
//        it.putExtra("num", num);
//        startActivity(it);

        finish();
    }

    @Override
    public void setEdShelf(String shelf) {

    }

    @Override
    public void add(String incode) {
        edIncode.setText("");
        if (!dialog.isShowing()) {
            legitimateList.add(incode);
//            mTvAllSignMember.setText(legitimateList.size() + "");
            mTvAllScan.setText(legitimateList.size() + errorList.size() + "");
        }
    }

    @Override
    public void addIncodeError(String incode) {
        edIncode.setText("");
        for (int i = 0; i < errorList.size(); i++) {
            if ((errorList.get(i).getInCode() + "").equals(incode)) {
                return;
            }
        }
        GroundingAllocationDetailInfo error = new GroundingAllocationDetailInfo(null, incode, "", "", "", false);
        errorList.add(error);
        mPresenter.saveError(error);
        adapter.notifyDataSetChanged();
        countNum.setText("错误数量：" + errorList.size());
        new AlertDialog.Builder(mContext).setTitle(incode).setMessage("店内码异常").setPositiveButton("剔除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mPresenter.deleteError(errorList.get(errorList.size() - 1));
                errorList.remove(errorList.size() - 1);
                adapter.notifyDataSetChanged();
                countNum.setText("错误数量：" + errorList.size());
                mTvAllScan.setText(legitimateList.size() + errorList.size() + "");
            }
        }).setNegativeButton("确定", null).show();
        mTvAllScan.setText(legitimateList.size() + errorList.size() + "");
    }

    @Override
    public void initData(PutAwayInfo info) {
        mTotalNum.setText(info.getSignIncodeNumber() + "");
        mSignNum.setText(info.getInIncodeNumber() + "");
    }

    @Override
    public void refresh(List<String> data) {
        scanPresenter.initMap(data);
        tvTitle.post(() -> {
            edShelfCode.setText("");
            legitimateList.clear();
            errorList.clear();
            adapter.notifyDataSetChanged();
            mTvAllScan.setText(legitimateList.size() + errorList.size() + "");

//            mPresenter.clearTable();
//            finish();
        });

    }

    @Override
    public void addError(String rfid) {
        for (int i = 0; i < errorList.size(); i++) {
            if (errorList.get(i).getRfid().equals(rfid)) {
                SoundUtils.play(2);
                return;
            }
        }
        GroundingAllocationDetailInfo error = new GroundingAllocationDetailInfo(null, null, null, null, rfid, false);
        errorList.add(error);
        mPresenter.saveError(error);
        adapter.notifyDataSetChanged();
//        mTvErrorNum.setText("错误数量：" + errorList.size());
        mTvAllScan.setText(legitimateList.size() + errorList.size() + "");
    }

    @Override
    public void setItemIncode(String result) {
        queryDialog.setCurrenIncode(result);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            new AlertDialog.Builder(GroundingAllocationDetailActivity.this)
                    .setTitle("未关联条形码的数据不会被保存，确定退出？")
                    .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).setNegativeButton("取消", null).show();

        }
        return true;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (event != null)
            if (event.getAction() == KeyEvent.ACTION_UP) return true;
        if (actionId == EditorInfo.IME_ACTION_SEND || actionId == 0) {
            switch (v.getId()) {
                case R.id.et_incode:
                    if (edIncode.getText().toString().equals("")) {
                        showDialog("请输入标签~");
                    } else {
                        onReceiverData(edIncode.getText().toString());
                    }
                    break;
                case R.id.et_shelf_code:
                    if (edShelfCode.getText().toString().equals("")) {
                        showDialog("请输入标签~");
                    } else {
//                        onReceiverData(edShelfCode.getText().toString());
                    }
                    break;

            }


            return true;
        }

        return false;
    }

    public void openOrClose() {
        if (mTvContent.getText().toString().equals("停止扫描")) {
            scanPresenter.stopReadRfid();
//            mTvAllSignMember.setText(scanPresenter.getNum() + "");
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

    @OnClick(R.id.rl_open_or_close)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_open_or_close:
                openOrClose();
                break;
        }
    }

    @Override
    public void OnFinish(String data) {
        if (!queryDialog.isShowing()) {
            SoundUtils.play(1);

            mPresenter.queryRfid(data);
        }
    }

    @Override
    public void ShowData(Object o) {

    }


    @Override
    protected void onDestroy() {
        mPresenter.clearTable();
        super.onDestroy();
    }
}
