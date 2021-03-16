package com.bigoffs.rfid.ui.fragment;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bigoffs.rfid.R;
import com.bigoffs.rfid.listener.OnFinishListener;
import com.bigoffs.rfid.mvp.bean.Sku;
import com.bigoffs.rfid.mvp.presenter.QueryShelfPresenter;
import com.bigoffs.rfid.mvp.presenter.ScanPresenter;
import com.bigoffs.rfid.mvp.view.IDataFragmentView;
import com.bigoffs.rfid.persistence.util.SoundUtils;
import com.bigoffs.rfid.ui.BaseFragment;
import com.bigoffs.rfid.ui.activity.FindActivity;
import com.bigoffs.rfid.util.LogUtil;
import com.google.gson.internal.LinkedTreeMap;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author okbuy-001
 * @desc
 * @time 2020/4/23 15:22
 */
//输入框如果没有焦点，就是读写器返回的数据是携带能量值的，数据是用来与输入框要找的epc匹配的，
//如果有焦点，读写器的返回的数据是没有能力值,数据是用来作为目标的
public class FindEPCFragment extends BaseFragment implements IDataFragmentView<String> {
    @BindView(R.id.tv_status)
    TextView tvStatus;
    @BindView(R.id.animation1)
    ImageView animation1;
    @BindView(R.id.btn_read_or_stop)
    Button btnReadOrStop;
    @BindView(R.id.tv_shelf)
    TextView mTvShelf;
    @BindView(R.id.tv_incode)
    TextView mTvIncode;
    @BindView(R.id.tv_change)
    TextView mTvChange;
    private View mView;
    private ScanPresenter scanPresenter;
    private FindActivity mActivity;
    private int currentPoint = 0;
    private QueryShelfPresenter queryShelfPresenter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_show_goods_epc, container, false);
        ButterKnife.bind(this, mView);
        mActivity = ((FindActivity) getActivity());

        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        animation1.setImageResource(R.drawable.icon1);
        initScanPresenter();
        if (mActivity.shelfCode != null) {
            mTvShelf.setText(mActivity.shelfCode);
        }


        if (mActivity.epc != null) {

            animation1.requestFocus();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        scanPresenter.stopReadRfid();
        AudioManager audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        int max = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, max, 0);
    }

    private void initScanPresenter() {
        scanPresenter = new ScanPresenter(this);
        scanPresenter.initData();
        scanPresenter.setCurrentSetting(ScanPresenter.Setting.stockRead);
        scanPresenter.setListener(new OnFinishListener() {
            @Override
            public void OnFinish(String data) {

                    String[] split = data.split("@");
                    String epc = mActivity.epc;

                    if (epc.equals(split[0])) {
                        if (split.length == 1) {
                            icon(0);
                        } else {
                            int rssi = Double.valueOf(split[1]).intValue();
                            icon(rssi);
                        }
                    }
            }
        });
        queryShelfPresenter = new QueryShelfPresenter(this,mContext);
        queryShelfPresenter.queryIncodeByShelf(mActivity.epc);
    }

    @Override
    public void readOrClose() {
        if (btnReadOrStop.getText().toString().equals("停止扫描")) {
            scanPresenter.stopReadRfid();
            animation1.setImageResource(R.drawable.icon1);
            btnReadOrStop.setBackground(commonBackground);
//            mActivity.tbCommon.setVisibility(View.VISIBLE);

            btnReadOrStop.setText("开始扫描");
        } else {

            scanPresenter.setReadDataModel(1);
            scanPresenter.startReadRfid();

            btnReadOrStop.setBackground(redBackground);
//                mActivity.tbCommon.setVisibility(View.INVISIBLE);
            btnReadOrStop.setText("停止扫描");

        }
    }

    public void setEpcName(String epc) {

        animation1.requestFocus();

    }




    public void icon(int iconNum) {
        switch (iconNum) {
            case 0:
                animation1.setImageResource(R.drawable.icon1);
                SoundUtils.playByVolume(1, 0.17f);
                break;
            case 1:
                animation1.setImageResource(R.drawable.icon2);
                SoundUtils.playByVolume(1, 0.33f);
                break;
            case 2:
                animation1.setImageResource(R.drawable.icon3);
                SoundUtils.playByVolume(1, 0.5f);
                break;
            case 3:
                animation1.setImageResource(R.drawable.icon4);
                SoundUtils.playByVolume(1, 0.66f);
                break;
            case 4:
                animation1.setImageResource(R.drawable.icon5);
                SoundUtils.playByVolume(1, 0.78f);
                break;
            case 5:
                animation1.setImageResource(R.drawable.icon6);
                SoundUtils.playByVolume(1, 1);
                break;
        }

    }

    @OnClick(R.id.btn_read_or_stop)
    public void onClick() {
        readOrClose();
    }

    @Override
    public void showLoading(String message) {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void goLoginActivity() {

    }

    @Override
    public void showNotice(String msg) {

    }

    @OnClick(R.id.tv_change)
    public void onViewClicked() {

        nextEpc();
    }

    public void nextEpc(){
        if (mActivity.skuList == null) return;
        currentPoint ++;
        if(currentPoint == mActivity.skuList.size()){
            currentPoint = 0;
        }
        mTvIncode.setText(mActivity.skuList.get(currentPoint).inCode);
        animation1.setImageResource(R.drawable.icon1);
    }
    public void lastEpc(){
        if (mActivity.skuList == null) return;
        currentPoint --;
        if(currentPoint == -1){
            currentPoint = mActivity.skuList.size()-1;
        }
        mTvIncode.setText(mActivity.skuList.get(currentPoint).inCode);
        animation1.setImageResource(R.drawable.icon1);
    }


    @Override
    public void ShowData(String skus) {
        mTvIncode.setText(skus+"");
    }
}
