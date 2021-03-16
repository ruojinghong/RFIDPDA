package com.bigoffs.rfid.ui.fragment;

import android.content.Context;
import android.content.Intent;
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
import androidx.fragment.app.Fragment;

import com.bigoffs.rfid.MainActivity;
import com.bigoffs.rfid.R;
import com.bigoffs.rfid.listener.OnFinishListener;
import com.bigoffs.rfid.mvp.presenter.ScanPresenter;
import com.bigoffs.rfid.mvp.view.IDataFragmentView;
import com.bigoffs.rfid.persistence.util.SoundUtils;
import com.bigoffs.rfid.ui.BaseFragment;
import com.bigoffs.rfid.ui.activity.FindActivity;
import com.bigoffs.rfid.ui.adapter.BaseListAdapter;
import com.bigoffs.rfid.ui.adapter.ViewHolder;
import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetectEpcListFragment extends BaseFragment implements IDataFragmentView<List<LinkedTreeMap>> {

    @BindView(R.id.tv_succeed_num)
    TextView tvSucceedNum;
    @BindView(R.id.tv_error_num)
    TextView tvErrorNum;
    @BindView(R.id.tv_sum)
    TextView tvSum;
    @BindView(R.id.lv_epc_container)
    ListView lvEpcContainer;
    @BindView(R.id.btn_clear)
    Button btnClear;
    @BindView(R.id.btn_open_close)
    Button btnOpenClose;
    private View mView;
    private ScanPresenter scanPresenter;

    private List<Map<String, String>> mapList = new ArrayList<>();
    private DetectEpcListAdapter mAdapter;

    private boolean flag = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_detect_epc_list, container, false);
        ButterKnife.bind(this, mView);
        initView();
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

    private void initView() {
        mAdapter = new DetectEpcListAdapter(getActivity(), mapList);
        lvEpcContainer.setAdapter(mAdapter);
        lvEpcContainer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), FindActivity.class);
                intent.putExtra("Activity", "FindProductFragment");
                intent.putExtra("epc", mapList.get(position).get("epc"));
                startAnimActivity(intent);
            }
        });
    }

    //初始化读写器
    private void initScanPresenter() {

        scanPresenter = new ScanPresenter(this);
        scanPresenter.initData();
        scanPresenter.setReadDataModel(0);
        scanPresenter.setCurrentSetting(ScanPresenter.Setting.stockRead);
        scanPresenter.setListenerProtectModel(new OnFinishListener() {
            @Override
            public void OnFinish(String data) {
                if (flag) return;
                SoundUtils.play(1);
                Map<String, String> map = new HashMap<>();
                map.put("epc", data);
                map.put("status", "0");
                mAdapter.add(map);
                tvSum.setText(String.valueOf(mapList.size()));
            }
        });
        //如果离开该界面时，读写器会被别的界面占用，返回时，需要把已经采集到的标签加载到内存中
        if (mapList.size() > 0) {
            List<String> datas = new ArrayList<>();
            for (Map<String, String> map : mapList) {
                datas.add(map.get("epc"));
            }
            scanPresenter.initData(datas);
        }

    }

    @Override
    public synchronized void ShowData(final List<LinkedTreeMap> linkedTreeMaps) {
        startProgressDialog("计数中...");
        tvSucceedNum.setText("0");
        tvErrorNum.setText("0");
        runOnWorkThread(new Runnable() {
            @Override
            public void run() {
                boolean flag = false;
                //通过验证信息，更新数据列表的状态
                for (Map<String, String> map : mapList) {
                    for (LinkedTreeMap linkedTreeMap : linkedTreeMaps) {
                        if (map.get("epc").equals(linkedTreeMap.get("epc"))) {
                            if (linkedTreeMap.get("barcode") != null) {
                                map.put("sku", linkedTreeMap.get("barcode").toString());
                                map.put("status", "1");
                            } else {
                                map.put("status", "2");
                            }
                            flag = true;
                            break;
                        }
                    }
                    if (!flag) {
                        if (map.get("status") != null) {
                            map.put("status", "2");
                        }
                    } else {
                        flag = false;
                    }
                }
                //排序
                Collections.sort(mapList, new Comparator<Map<String, String>>() {
                    @Override
                    public int compare(Map<String, String> lhs, Map<String, String> rhs) {

                        if (rhs.get("status").equals(lhs.get("status")))
                            return 0;
                        if (rhs.get("status").equals("2"))
                            return 1;
                        return -1;
                    }
                });
                //刷新界面统计数
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int errorNum = 0;
                        int succeedNum = 0;
                        for (Map<String, String> map : mapList) {
                            if (map.get("status").equals("2")) {
                                errorNum++;
                            } else {
                                succeedNum++;
                            }
                        }
                        tvSucceedNum.setText(String.valueOf(succeedNum));
                        tvErrorNum.setText(String.valueOf(errorNum));
                        mAdapter.notifyDataSetChanged();
                        stopProgressDialog(null);
                    }
                });
            }
        });
    }

    public void readOrClose() {
        if (btnOpenClose.getText().toString().equals("点击开始")) {
            btnOpenClose.setBackground(redBackground);
            lvEpcContainer.setEnabled(false);
            scanPresenter.startReadRfid();
            btnOpenClose.setText("点击结束");
            flag = false;
        } else {
            flag = true;
            btnOpenClose.setBackground(commonBackground);
            scanPresenter.stopReadRfid();
            lvEpcContainer.setEnabled(true);
            btnOpenClose.setText("点击开始");
            searchEpcList();
        }
    }

    private void searchEpcList() {

        if (!flag) return;
        if (mapList.size() == 0) return;
        List<String> epcList = new ArrayList<>();
        for (Map<String, String> map : mapList) {
            epcList.add(map.get("epc"));
        }

    }

    @OnClick({R.id.btn_clear, R.id.btn_open_close})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_clear:
                mAdapter.clearAll();
                scanPresenter.initData();
                tvSum.setText("0");
                tvSucceedNum.setText("0");
                tvErrorNum.setText("0");
                break;
            case R.id.btn_open_close:
                readOrClose();
                break;
        }
    }

    @Override
    public void showLoading(String mssage) {

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

    public class DetectEpcListAdapter extends BaseListAdapter<Map<String, String>> {

        DetectEpcListAdapter(Context context, List<Map<String, String>> list) {
            super(context, list);
        }

        @Override
        public View bindView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_detect_epc, parent, false);
            }
            Map<String, String> map = getList().get(position);
            TextView index = ViewHolder.get(convertView, R.id.tv_index);
            TextView epc = ViewHolder.get(convertView, R.id.tv_tag);
            TextView sku = ViewHolder.get(convertView, R.id.tv_sku);

            switch (Integer.parseInt(map.get("status"))) {
                case 0:
                    index.setText(String.valueOf(position + 1));
                    index.setTextColor(Color.BLACK);
                    epc.setText(map.get("epc"));
                    epc.setTextColor(Color.BLACK);
                    sku.setText("");
                    sku.setTextColor(Color.BLACK);
                    break;
                case 1:
                    index.setText(String.valueOf(position + 1));
                    index.setTextColor(Color.GRAY);
                    epc.setText(map.get("epc"));
                    epc.setTextColor(Color.GRAY);
                    sku.setText(map.get("sku"));
                    sku.setTextColor(Color.GRAY);
                    break;
                case 2:
                    index.setText(String.valueOf(position + 1));
                    index.setTextColor(Color.RED);
                    epc.setText(map.get("epc"));
                    epc.setTextColor(Color.RED);
                    sku.setText("");
                    sku.setTextColor(Color.RED);
                    break;
            }
            return convertView;
        }
    }
}
