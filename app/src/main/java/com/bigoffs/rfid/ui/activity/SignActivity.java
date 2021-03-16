package com.bigoffs.rfid.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bigoffs.rfid.R;
import com.bigoffs.rfid.listener.OnFinishListener;
import com.bigoffs.rfid.mvp.biz.ISignBiz;
import com.bigoffs.rfid.mvp.biz.SignBiz;
import com.bigoffs.rfid.mvp.presenter.ScanPresenter;
import com.bigoffs.rfid.mvp.view.IDataFragmentView;
import com.bigoffs.rfid.network.CommonResult;
import com.bigoffs.rfid.network.ICommonResult;
import com.bigoffs.rfid.persistence.util.SoundUtils;
import com.bigoffs.rfid.util.LogUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

public class SignActivity extends BaseActivity implements IDataFragmentView {

    @BindView(R.id.sp_code)
    Spinner mSpCode;
    @BindView(R.id.et_incode)
    EditText mEtIncode;
    @BindView(R.id.btn_commit)
    Button mBtnCommit;
    private List<String> data_list;
    private ArrayAdapter<String> arr_adapter;
    private ScanPresenter scanPresenter;
    private ISignBiz mBiz;
    private Context mContext;

    public static String TAG = "SignActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_bound);
        ButterKnife.bind(this);
        initView();

    }

    @Override
    protected void onResume() {
        super.onResume();
        initScanPresenter();
    }

    private void initView() {
        mContext =this;
        data_list = new ArrayList<String>();
        data_list.add("箱号");
        data_list.add("调拨单");
        data_list.add("店内码");
        //适配器
        arr_adapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data_list);
        //设置样式
        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        mSpCode.setAdapter(arr_adapter);
        mBtnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( mEtIncode.getText().toString().equals("") ) return;

                go(mEtIncode.getText().toString());
            }
        });
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
                            go(mEtIncode.getText().toString());
                        }
                        return true;
                    }
                    return false;
                }
        });

        mEtIncode.requestFocus();
    }

    private void initScanPresenter() {

        scanPresenter = new ScanPresenter(this);
        scanPresenter.initData();
        scanPresenter.setReadDataModel(0);

        scanPresenter.setCurrentSetting(ScanPresenter.Setting.bindingMore);
        scanPresenter.setListener(new OnFinishListener() {
            @Override
            public void OnFinish(String data) {
                go(data);
            }
        });
    }

    @Override
    public void ShowData(Object o) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        scanPresenter.stopReadRfid();
    }

    private void go(String data){

        SoundUtils.play(1);
        mEtIncode.setText("");
        Intent it = new Intent(mContext,SignDetailActivity.class);
        it.putExtra("type",caseType());
        it.putExtra("code",data);
        startAnimActivity(it);

    }

    public String caseType(){
//        switch (mSpCode.getSelectedItem().toString()){
//            case "箱号":
//                return "box_code";
//            case "调拨单":
//                return "allocation_code";
//            case "店内码":
//                return "incode";
//
//        }
        return mSpCode.getSelectedItem().toString();
    }
}
