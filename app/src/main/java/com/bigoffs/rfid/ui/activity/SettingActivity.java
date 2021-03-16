package com.bigoffs.rfid.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bigoffs.rfid.R;
import com.bigoffs.rfid.persistence.util.SPUtils;
import com.bigoffs.rfid.service.YBX.ScanServiceWithYBX;

import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.widget.Toolbar;

/**
 * @author okbuy-001
 * @desc
 * @time 2020/4/24 15:37
 */
public class SettingActivity extends BaseActivity implements SeekBar.OnSeekBarChangeListener,View.OnClickListener {

    private TextView tvPower;
    private TextView btn_setting;
    private SeekBar sbPower;
    private Map<String,Integer> setting = new HashMap<>();
    private ScanServiceWithYBX mService = ScanServiceWithYBX.getInstance();
    private int currentPower = 30;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_setting);
        tvPower = findViewById(R.id.tv_power);
        btn_setting = findViewById(R.id.btn_setting);
        sbPower = findViewById(R.id.sb_power);
        sbPower.setOnSeekBarChangeListener(this);
        sbPower.setProgress(mService.getPower());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("设置");
        btn_setting.setOnClickListener(this);
        currentPower = (int) SPUtils.get(this, "power", 30) ;
        tvPower.setText(currentPower + "");
        sbPower.setProgress(currentPower-5);
        sbPower.setEnabled(false);
        sbPower.setOnSeekBarChangeListener(this);
        btn_setting.setOnClickListener(this);
    }
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case R.id.sb_power:
                int j  = progress+5;
                currentPower = j;
                setting.put("power", j);
                tvPower.setText(j + "");
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


    @Override
    public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_setting :
                    if(btn_setting.getText().equals("设置")){
                        btn_setting.setText("确定");
                        sbPower.setEnabled(true);

                    }else{
                        btn_setting.setText("设置");
                        sbPower.setEnabled(false);
                        SPUtils.put(this,"power",currentPower);
                        mService.setPower(currentPower);
                        mService.setCurrentSetting(new HashMap<>());
                    }
                    break;
            }
    }


}
