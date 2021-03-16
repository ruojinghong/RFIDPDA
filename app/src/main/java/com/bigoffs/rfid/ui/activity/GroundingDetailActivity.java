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
import com.bigoffs.rfid.mvp.bean.GroundingDetailInfo;
import com.bigoffs.rfid.mvp.presenter.GroundingDetailPresenter;
import com.bigoffs.rfid.mvp.presenter.IGroundingDetailPresenter;
import com.bigoffs.rfid.mvp.view.IGroundingDetailView;
import com.bigoffs.rfid.ui.adapter.GroundingDetailInfoAdapter;


import java.util.ArrayList;
import java.util.List;

/**
 * @author okbuy-001
 * @desc 到货单界面
 * @time 2018/12/7 11:26
 */
public class GroundingDetailActivity extends BaseActivity implements IGroundingDetailView, TextView.OnEditorActionListener {
    //到货单号
    private TextView tvTitle;
    //当前扫描结果
    private TextView tvCode;
    //店内码输入框
    private EditText edIncode;
    //条形码输入框
    private EditText edBorCode;
    //货架码输入框
    private EditText edShelfCode;
    //剔除按钮
    private Button btDelete;
    //错误信息
    private TextView tvErrMessage;
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
    private EditText dialogEt;

    private String taskId;

    private List<GroundingDetailInfo> mData = new ArrayList<>();

    private GroundingDetailInfoAdapter adapter = new GroundingDetailInfoAdapter(mData);

    private IGroundingDetailPresenter mPresenter = new GroundingDetailPresenter(this,this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_grounding_detail);
        initView();
    }

    public static void show(Context context, String taskId, String allcationId, String allocationCode ) {
        Intent intent = new Intent(context, GroundingDetailActivity.class);
        intent.putExtra("taskId", taskId);
        intent.putExtra("code",allocationCode);
        intent.putExtra("id",allcationId);
        context.startActivity(intent);
    }

    void initView(){
        LayoutInflater factory = LayoutInflater.from(this);//提示框
        dialogView = factory.inflate(R.layout.dialog_edittext, null);
        dialog = new AlertDialog.Builder(this)
                .setTitle("请扫描剔除店内码")//提示框标题
                .setView(dialogView)
                .setPositiveButton("剔除",new android.content.DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        if(!TextUtils.isEmpty(dialogEt.getText())){
                            mPresenter.deleteItem(dialogEt.getText().toString());
                        }else{
                            showNotice("请输入要剔除的店内码");
                        }
                        dialogEt.setText("");
                    }
                }).setNegativeButton("取消",
                null).create();
        dialogEt=(EditText)dialogView.findViewById(R.id.editText);//获得输入框对象
        tvTitle = (TextView) findViewById(R.id.tv_title);
        Intent intent = getIntent();
        arrivalId = intent.getStringExtra("id");
        arrivalCode = intent.getStringExtra("code");
        taskId = intent.getStringExtra("taskId");
        tvTitle.setText("到货单号:"+arrivalCode);
        tvCode = (TextView) findViewById(R.id.tv_code);
        edIncode = (EditText) findViewById(R.id.et_incode);
        edBorCode = (EditText) findViewById(R.id.et_bar_code);
        edShelfCode = (EditText) findViewById(R.id.et_shelf_code);
        btDelete = (Button) findViewById(R.id.bt_delete);
        btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.deleteFirstItem();
            }
        });
        tvErrMessage = (TextView) findViewById(R.id.tv_error_message);
        countNum = (TextView) findViewById(R.id.count_num);
        mRecylerView = (RecyclerView) findViewById(R.id.rv_grounding);
        tvClear  = (TextView) findViewById(R.id.tv_clear);
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
                new AlertDialog.Builder(GroundingDetailActivity.this)
                        .setTitle("确定要上架吗？")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mPresenter.upData(taskId);
                            }
                        }).setNegativeButton("取消",null).show();

            }
        });
        edIncode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus){
                        mPosition =1;
                    }
            }
        });
        edBorCode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    mPosition =2;
                }
            }
        });
        edShelfCode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    mPosition = 3;
                }
            }
        });

        mRecylerView.setLayoutManager(new LinearLayoutManager(this));
        mRecylerView.setAdapter(adapter);
        mPresenter.queryHistory();

        edIncode.setShowSoftInputOnFocus(false);
        edIncode.setImeOptions(EditorInfo.IME_ACTION_SEND);
        edIncode.setOnEditorActionListener(this);
        edBorCode.setShowSoftInputOnFocus(false);
        edBorCode.setImeOptions(EditorInfo.IME_ACTION_SEND);
        edBorCode.setOnEditorActionListener(this);
        edShelfCode.setShowSoftInputOnFocus(false);
        edShelfCode.setImeOptions(EditorInfo.IME_ACTION_SEND);
        edShelfCode.setOnEditorActionListener(this);
    }


    public void onReceiverData(String data) {
        if(dialog.isShowing()) {
            dialogEt.setText(data);
            }else{
            switch (mPosition) {
                //扫描店内吗
                case 1:
                    mPresenter.queryInCode(data);
                    break;
                //扫描条形码
                case 2:
                    mPresenter.queryBarCode(data);
                    break;
                //扫描条形码
                case 3:
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
            tvErrMessage.setVisibility(View.GONE);
        }
    }

    @Override
    public void showErrMessage(String code) {
        if (!TextUtils.isEmpty(code)) {
            beep();
            tvCode.setTextColor(Color.RED);
            tvCode.setText(code);
            tvErrMessage.setVisibility(View.VISIBLE);
            scanSwitch(true);
        }
    }


    @Override
    public void showList(List<GroundingDetailInfo> list) {
        countNum.setText(list.size()+"");
        mData.clear();
        mData.addAll(list);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void getFoucs(int i) {

            switch (i){
                //扫描店内吗
                case 1:

                    edIncode.requestFocus();
                    break;
                //扫描条形码
                case 2:

                   edBorCode.requestFocus();
                    break;
                //扫描条形码
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
        dialog.show();
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
            Intent it = new Intent(this,FinishGroundingActitivy.class);
            it.putExtra("num",num);
            startActivity(it);
            finish();
    }

    @Override
    public void setEdShelf(String shelf) {
        edShelfCode.setText(shelf);
    }

    @Override
    public void add(String incode) {

    }

    @Override
    public void addIncodeError(String incode) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() ==  android.R.id.home){
            new AlertDialog.Builder(GroundingDetailActivity.this)
                    .setTitle("未关联条形码的数据不会被保存，确定退出？")
                    .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).setNegativeButton("取消",null).show();

        }
        return true;
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(GroundingDetailActivity.this)
                .setTitle("未关联条形码的数据不会被保存，确定退出？")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).setNegativeButton("取消",null).show();
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (event != null)
            if (event.getAction() == KeyEvent.ACTION_UP) return true;
        if (actionId == EditorInfo.IME_ACTION_SEND || actionId == 0) {
            switch (v.getId()){
                case R.id.et_incode:
                    if (edIncode.getText().toString().equals("")) {
                        showDialog("请输入标签~");
                    } else {
                        onReceiverData(edIncode.getText().toString());
                    }
                    break;
                case R.id.et_bar_code:
                    if (edBorCode.getText().toString().equals("")) {
                        showDialog("请输入标签~");
                    } else {
                        onReceiverData(edBorCode.getText().toString());
                    }
                    break;
                case R.id.et_shelf_code:
                    if (edShelfCode.getText().toString().equals("")) {
                        showDialog("请输入标签~");
                    } else {
                        onReceiverData(edShelfCode.getText().toString());
                    }
                    break;

            }


                return true;
        }

        return false;
    }
}
