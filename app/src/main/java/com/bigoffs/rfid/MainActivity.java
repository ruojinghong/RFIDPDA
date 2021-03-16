package com.bigoffs.rfid;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.bigoffs.rfid.persistence.util.SPUtils;
import com.bigoffs.rfid.persistence.view.NoScrollViewPager;
import com.bigoffs.rfid.ui.BaseFragment;
import com.bigoffs.rfid.ui.activity.BaseActivity;
import com.bigoffs.rfid.ui.adapter.ViewPagerAdapter;
import com.bigoffs.rfid.ui.fragment.DetectEpcListFragment;
import com.bigoffs.rfid.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {
    public List<BaseFragment> mFragments = new ArrayList<>();
    public ViewPagerAdapter viewPagerAdapter;
    @BindView(R.id.tv_common)
    TextView tvCommon;
    @BindView(R.id.tb_common)
    Toolbar tbCommon;
    @BindView(R.id.vp_common)
    NoScrollViewPager vpCommon;
    private boolean flag = false;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common);
        ButterKnife.bind(this);
        mContext =  this;
        startUHFWithCW();
        openBarCodeReader();
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        tvCommon.setText("检测");
//        if (Utils.getCurrentUser() != null)
//            tvCommon.setText("检测(" + Utils.getCurrentUser().getWarehouseCode() + ")");
        tbCommon.setTitle(null);
    }

    private void initView() {
        setSupportActionBar(tbCommon);

        tbCommon.setNavigationIcon(R.mipmap.back_login);
        tbCommon.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mFragments.add(new DetectEpcListFragment());
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), mFragments);
        vpCommon.setAdapter(viewPagerAdapter);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 139 || keyCode == 120 || keyCode == 138) {
            if (event.getRepeatCount() == 0) {
                mFragments.get(0).readOrClose();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public void setCurrentView(int currentView) {
        vpCommon.setCurrentItem(currentView);
    }

    public void openBarCodeReader() {

        SPUtils.put(this, "barCodeReaderMode", 2);

        if (flag) {
            boolean close = scanService.closeBarcodeReader();
            if (close) {
                flag = false;
            } else {
                ToastUtil.showToast(mContext, "关闭条码扫描失败");
            }
        } else {

            boolean open = scanService.openBarcodeReader(this);
            if (open) {
                flag = true;
            } else {
                ToastUtil.showToast(mContext, "开启条码扫描失败");
            }

        }
    }

    private void startUHFWithCW() {
        try {
            scanService.init(this);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        new InitTask().execute();
    }
    public class InitTask extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            return scanService.openReader();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (!result) {
                Toast.makeText(MainActivity.this, "启动扫描头失败", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
