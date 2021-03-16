package com.bigoffs.rfid.ui.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bigoffs.rfid.R;
import com.bigoffs.rfid.listener.OnFinishListener;
import com.bigoffs.rfid.mvp.presenter.ScanPresenter;
import com.bigoffs.rfid.mvp.view.IDataFragmentView;
import com.bigoffs.rfid.persistence.util.SoundUtils;
import com.bigoffs.rfid.ui.BaseFragment;
import com.bigoffs.rfid.ui.activity.FindActivity;
import com.bigoffs.rfid.ui.adapter.BaseListAdapter;
import com.google.gson.internal.LinkedTreeMap;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author okbuy-001
 * @desc
 * @time 2020/4/23 15:19
 */
public class FindSkuFragment extends BaseFragment implements IDataFragmentView<List<LinkedTreeMap>> {

    @BindView(R.id.lv_epc_container)
    ListView lvEpcContainer;
    @BindView(R.id.btn_read_or_stop)
    Button btnReadOrStop;
    private View mView;
    private FindActivity mActivity;
    private ScanPresenter scanPresenter;

    private List<LinkedTreeMap> mData = new ArrayList<>();
    private List<LinkedTreeMap> containers = new ArrayList<>();
    private FindEpcListAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_find_goods_epc, container, false);
        ButterKnife.bind(this, mView);
        mActivity = (FindActivity) getActivity();
        init();
        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        initScanPresenter();
    }

    @Override
    public void onPause() {
        super.onPause();
        scanPresenter.stopReadRfid();
    }

    public void init() {

        mAdapter = new FindEpcListAdapter(getActivity(), containers);
        lvEpcContainer.setAdapter(mAdapter);
        //单击某个item，跳转到epc搜索界面
        lvEpcContainer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mActivity.vpCommon.setCurrentItem(1);
                ((FindEPCFragment) mActivity.fragments.get(1))
                        .setEpcName(containers.get(position).get("epc").toString());
            }
        });
    }


    private void initScanPresenter() {
        scanPresenter = new ScanPresenter(this);
        scanPresenter.initData();
        //设置标签携带能力值
        scanPresenter.setReadDataModel(0);
        scanPresenter.setCurrentSetting(ScanPresenter.Setting.stockRead);
        scanPresenter.setListener(new OnFinishListener() {
            @Override
            public void OnFinish(String data) {
                //采集到的epc，如果存在于集合中，数量加一，如果不存在与集合中，加入集合，并加一
                Logger.d("????????????????????", "??????????????");
                for (LinkedTreeMap linkedTreeMap : mData) {
                    if (!data.equals(linkedTreeMap.get("epc"))) continue;
                    boolean flag = false;
                    for (LinkedTreeMap container : containers) {
                        if (data.equals(container.get("epc"))) {
                            container.put("num", ((int) container.get("num")) + 1);
                            flag = true;
                        }
                    }
                    if (!flag) {
                        LinkedTreeMap container = new LinkedTreeMap<>();
                        container.put("epc", linkedTreeMap.get("epc"));
                        container.put("barcode", linkedTreeMap.get("barcode"));
                        container.put("num", 1);
                        containers.add(container);
                    }
                    SoundUtils.play(1);
                    linkedTreeMap.put("num", ((int) linkedTreeMap.get("num")) + 1);
                    Collections.sort(containers, new Comparator<LinkedTreeMap>() {
                        @Override
                        public int compare(LinkedTreeMap lhs, LinkedTreeMap rhs) {
                            if (((int) lhs.get("num")) > ((int) rhs.get("num"))) return -1;
                            if (((int) lhs.get("num")) < ((int) rhs.get("num"))) return 1;
                            return 0;
                        }
                    });
                    mAdapter.notifyDataSetChanged();
                    break;
                }
            }
        });
    }

    @Override
    public void readOrClose() {
        if (btnReadOrStop.getText().toString().equals("停止扫描")) {
            scanPresenter.stopReadRfid();
            lvEpcContainer.setEnabled(true);
            btnReadOrStop.setText("开始扫描");
            btnReadOrStop.setBackground(commonBackground);
//            mActivity.tbCommon.setVisibility(View.VISIBLE);
        } else {
            mAdapter.clearAll();
            scanPresenter.startReadRfid();
            lvEpcContainer.setEnabled(false);
            btnReadOrStop.setText("停止扫描");
            btnReadOrStop.setBackground(redBackground);
//            mActivity.tbCommon.setVisibility(View.INVISIBLE);
        }
    }

    public void showDialog(final String result) {
        builder.setMessage(result);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (result.equals("该sku没有绑过epc"))
                    getActivity().finish();
            }
        });
        builder.show();
    }

    @Override
    public void ShowData(List<LinkedTreeMap> linkedTreeMaps) {
        for (LinkedTreeMap linkedTreeMap : linkedTreeMaps) {
            linkedTreeMap.put("num", 0);
        }
        mData.addAll(linkedTreeMaps);
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

    public class FindEpcListAdapter extends BaseListAdapter<LinkedTreeMap> {

        public FindEpcListAdapter(Context context, List<LinkedTreeMap> list) {
            super(context, list);
        }

        @Override
        public View bindView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_find_epc_list, parent, false);
            }


            LinkedTreeMap linkedTreeMap = getList().get(position);


            TextView index = (TextView) convertView.findViewById(R.id.tv_index);
            TextView barcode = (TextView) convertView.findViewById(R.id.tv_barcode);
            TextView epc = (TextView) convertView.findViewById(R.id.et_epc);
            TextView num = (TextView) convertView.findViewById(R.id.tv_num);

            index.setText(String.valueOf(position + 1));
            barcode.setText(linkedTreeMap.get("barcode").toString());
            epc.setText(linkedTreeMap.get("epc").toString());
            num.setText(String.valueOf(linkedTreeMap.get("num")));

            ItemBg(position, convertView, index, barcode, epc, num);

            return convertView;
        }

        void ItemBg(int position, View convertView, TextView index, TextView barcode, TextView epc, TextView num) {
            switch (position) {
                case 0:
                    convertView.setBackgroundResource(R.color.item_bg1);
                    barcode.setTextColor(Color.WHITE);
                    index.setTextColor(Color.WHITE);
                    epc.setTextColor(Color.WHITE);
                    num.setTextColor(Color.WHITE);
                    break;
                case 1:
                    convertView.setBackgroundResource(R.color.item_bg3);
                    barcode.setTextColor(Color.WHITE);
                    index.setTextColor(Color.WHITE);
                    epc.setTextColor(Color.WHITE);
                    num.setTextColor(Color.WHITE);
                    break;
                case 2:
                    convertView.setBackgroundResource(R.color.item_bg5);
                    barcode.setTextColor(Color.WHITE);
                    index.setTextColor(Color.WHITE);
                    epc.setTextColor(Color.WHITE);
                    num.setTextColor(Color.WHITE);
                    break;
                case 3:
                    convertView.setBackgroundResource(R.color.item_bg6);
                    barcode.setTextColor(Color.WHITE);
                    index.setTextColor(Color.WHITE);
                    epc.setTextColor(Color.WHITE);
                    num.setTextColor(Color.WHITE);
                    break;
                case 4:
                    convertView.setBackgroundResource(R.color.item_bg7);
                    barcode.setTextColor(Color.WHITE);
                    index.setTextColor(Color.WHITE);
                    epc.setTextColor(Color.WHITE);
                    num.setTextColor(Color.WHITE);
                    break;
                case 5:
                    convertView.setBackgroundResource(R.color.item_bg8);
                    barcode.setTextColor(Color.WHITE);
                    index.setTextColor(Color.WHITE);
                    epc.setTextColor(Color.WHITE);
                    num.setTextColor(Color.WHITE);
                    break;
                case 6:
                    convertView.setBackgroundResource(R.color.item_bg9);
                    barcode.setTextColor(Color.BLACK);
                    index.setTextColor(Color.BLACK);
                    epc.setTextColor(Color.BLACK);
                    num.setTextColor(Color.BLACK);
                    break;
                default:
                    convertView.setBackgroundResource(R.color.white);
                    barcode.setTextColor(Color.BLACK);
                    index.setTextColor(Color.BLACK);
                    epc.setTextColor(Color.BLACK);
                    num.setTextColor(Color.BLACK);
                    break;

            }
        }

    }

}
