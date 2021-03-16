package com.bigoffs.rfid.ui.activity;

import android.os.Bundle;

import com.bigoffs.rfid.listener.OnFinishListener;
import com.bigoffs.rfid.mvp.presenter.ScanPresenter;
import com.bigoffs.rfid.mvp.view.IDataFragmentView;
import com.bigoffs.rfid.persistence.util.SoundUtils;

/**
 * @author okbuy-001
 * @desc
 * @time 2020/5/11 10:32
 */
public  abstract  class ScanBaseActivity extends BaseActivity implements IDataFragmentView,OnFinishListener {
    protected  ScanPresenter scanPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initScanPresenter();
    }


    private void    initScanPresenter() {
        scanPresenter = new ScanPresenter(this);
        scanPresenter.initData();
        scanPresenter.setReadDataModel(0);
        scanPresenter.setMode(1);
        scanPresenter.setCurrentSetting(ScanPresenter.Setting.stockRead);
        scanPresenter.setListenerProtectModel(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        scanPresenter.stopReadRfid();
    }
}
