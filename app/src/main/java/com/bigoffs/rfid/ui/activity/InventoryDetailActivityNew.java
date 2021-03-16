package com.bigoffs.rfid.ui.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bigoffs.rfid.R;
import com.bigoffs.rfid.mvp.bean.FinishInventoryInfo;
import com.bigoffs.rfid.ui.fragment.InventoryFragmentNew;
import com.bigoffs.rfid.util.LogUtil;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

public class InventoryDetailActivityNew extends BaseActivity implements View.OnClickListener {

    // 盘点单号
    private String  result;

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;

    private TextView mTvShelfNumber;
    private TextView mTvTaskId;
    private TextView mTvTotalNum;
    private TextView mTvOverNum;
    private TextView mTvLostNum;
    private TextView mTvWrongNum;


    private FinishInventoryInfo data;


    public static void show(Context context, String result) {
        Intent intent = new Intent(context, InventoryDetailActivityNew.class);
        intent.putExtra("result", result);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        result = getIntent().getStringExtra("result");

        setContentView(R.layout.activity_inventory_detail_new);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        initViews();
        getData();
    }

    public void initViews() {
        mTvShelfNumber = (TextView) findViewById(R.id.tv_shelf_number);
        mTvTaskId = (TextView) findViewById(R.id.tv_task_id);
        mTvTotalNum = (TextView) findViewById(R.id.tv_total_num);
        mTvOverNum = (TextView) findViewById(R.id.tv_over_num);
        mTvLostNum = (TextView) findViewById(R.id.tv_lost_num);
        mTvWrongNum = (TextView) findViewById(R.id.tv_wrong_num);
        mTvTotalNum.setOnClickListener(this);
        mTvOverNum.setOnClickListener(this);
        mTvLostNum.setOnClickListener(this);
        mTvWrongNum.setOnClickListener(this);
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mTvTotalNum.setTextColor(Color.BLACK);
                mTvOverNum.setTextColor(Color.BLACK);
                mTvLostNum.setTextColor(Color.BLACK);
                mTvWrongNum.setTextColor(Color.BLACK);
                int selectedColor = getResources().getColor(R.color.colorAccent);
                switch (position) {
                    case 0:
                        mTvTotalNum.setTextColor(selectedColor);
                        break;
                    case 1:
                        mTvOverNum.setTextColor(selectedColor);
                        break;
                    case 2:
                        mTvLostNum.setTextColor(selectedColor);
                        break;
                    case 3:
                        mTvWrongNum.setTextColor(selectedColor);
                        break;
                }
            }
        });
    }

    public void getData() {
        Gson gson = new Gson();
        LogUtil.i("-------------resu",result);
        data = gson.fromJson(result,FinishInventoryInfo.class);

        setData(data);
    }

    private void setData(FinishInventoryInfo info) {
        String shelfs = info.shelfs.toString();
        mTvShelfNumber.setText(shelfs.substring(1,shelfs.length()-1));
        mTvTaskId.setText(info.inventoryId+"");
        mTvTotalNum.setText(info.dbInfos.size() + "");
        mTvOverNum.setText(info.moreInfos.size() + "");
        mTvLostNum.setText(info.loseInfos.size() + "");
        mTvWrongNum.setText(info.wrongInfos.size() + "");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_total_num:
                mViewPager.setCurrentItem(0);
                break;
            case R.id.tv_over_num:
                mViewPager.setCurrentItem(1);
                break;
            case R.id.tv_lost_num:
                mViewPager.setCurrentItem(2);
                break;
            case R.id.tv_wrong_num:
                mViewPager.setCurrentItem(3);
                break;
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            int status = 0;
            switch (position) {
                case 0:
                    status = 5;
                    break;
                case 1:
                    status = 2;
                    break;
                case 2:
                    status = 3;
                    break;
                case 3:
                    status = 4;
                    break;
            }
            return InventoryFragmentNew.newInstance(status, result);
        }

        @Override
        public int getCount() {
            // Show 4 total pages.
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "系统货品数";
                case 1:
                    return "盘盈";
                case 2:
                    return "盘亏";
                case 3:
                    return "串位";
            }
            return null;
        }
    }
}
