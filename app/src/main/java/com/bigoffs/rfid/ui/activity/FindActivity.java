package com.bigoffs.rfid.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import com.bigoffs.rfid.R;
import com.bigoffs.rfid.mvp.bean.Sku;
import com.bigoffs.rfid.persistence.view.NoScrollViewPager;
import com.bigoffs.rfid.ui.BaseFragment;
import com.bigoffs.rfid.ui.adapter.ViewPagerAdapter;
import com.bigoffs.rfid.ui.fragment.FindEPCFragment;
import com.bigoffs.rfid.ui.fragment.FindSkuFragment;
import com.bigoffs.rfid.util.UserManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.KeyEvent.KEYCODE_DPAD_DOWN;

public class FindActivity extends BaseActivity {

//    @BindView(R.id.tv_common)
//    public TextView tvCommon;
//    @BindView(R.id.tb_common)
//    public Toolbar tbCommon;
    @BindView(R.id.vp_common)
    public NoScrollViewPager vpCommon;
    private String mActivityName;
    public String mBarcode;
    public String epc;
    public String shelfCode;
    public List<Sku> skuList;
    public List<BaseFragment> fragments;
    public ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(@NonNull Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        mActivityName = intent.getStringExtra("Activity");
        mBarcode = intent.getStringExtra("Barcode");
        epc = intent.getStringExtra("rfid");
        shelfCode = intent.getStringExtra("shelf");
//        skuList = intent.getParcelableArrayListExtra("sku");
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (UserManager.isLogin(this))
//            tvCommon.setText(UserManager.getLastUserName(this));
//        tbCommon.setTitle("");
    }

    private void initData() {

//        tbCommon.setNavigationIcon(R.mipmap.back_login);
//        tbCommon.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (vpCommon.getCurrentItem() == 1) {
//                    if (mActivityName.equals("SearchFindProductFragment")) {
//                        vpCommon.setCurrentItem(0);
//                    } else
//                        finish();
//                } else {
//                    finish();
//                }
//            }
//        });

        fragments = new ArrayList<>();
//        fragments.add(new FindSkuFragment());
        fragments.add(new FindEPCFragment());

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments);
        vpCommon.setAdapter(viewPagerAdapter);
        vpCommon.setOffscreenPageLimit(0);
        if (mActivityName != null && mActivityName.equals("SearchFindProductFragment")) {
            vpCommon.setCurrentItem(0);
        } else {
            vpCommon.setCurrentItem(0);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch (keyCode){

            case  523:
                if (event.getRepeatCount() == 0) {
                    fragments.get(vpCommon.getCurrentItem()).readOrClose();
                    return true;
                }
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN :
                if(keyCode == KEYCODE_DPAD_DOWN){
                    ((FindEPCFragment)(fragments.get(0))).nextEpc();
                    return  true;
                }
                break;
            case KeyEvent.KEYCODE_DPAD_UP:
                ((FindEPCFragment)(fragments.get(0))).lastEpc();
                break;

        }


        return super.onKeyDown(keyCode, event);
    }
}
