package com.bigoffs.rfid.ui.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.bigoffs.rfid.R;
import com.bigoffs.rfid.listener.OnFinishListener;
import com.bigoffs.rfid.mvp.bean.BarcodeInfo;
import com.bigoffs.rfid.mvp.bean.BluetoothDevice;
import com.bigoffs.rfid.mvp.bean.ProductInfo;
import com.bigoffs.rfid.mvp.bean.Sku;
import com.bigoffs.rfid.mvp.presenter.IQueryPresenter;
import com.bigoffs.rfid.mvp.presenter.QueryPresenter;
import com.bigoffs.rfid.mvp.presenter.ScanPresenter;
import com.bigoffs.rfid.mvp.view.IDataFragmentView;
import com.bigoffs.rfid.mvp.view.IQueryView;
import com.bigoffs.rfid.persistence.util.SoundUtils;
import com.bigoffs.rfid.ui.adapter.BaseListAdapter;
import com.bigoffs.rfid.ui.adapter.ViewHolder;
import com.bigoffs.rfid.util.LogUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author okbuy-001
 * @desc
 * @time 2020/4/24 14:58
 */
public class QueryActivity extends BaseActivity implements IQueryView, View.OnClickListener, IDataFragmentView {
    public static final int PRINTER_LINK_SUCCESS = 0;
    public static final int PRINTER_LINK_FAIL = 1;
    private ScanPresenter scanPresenter;
    private String sku = "";
    private boolean flag = false;
    private Dialog mDialog;
    private BarcodeInfoAdapter barcodeInfoAdapter;
    private List<BarcodeInfo> barcodeInfos = new ArrayList<>();
    private String currentRfid;
    private LinearLayout llBarcode;
    // ?????????????????????
    private EditText mEtIncode;
    //???????????????
    private Button btnPrint;
    private Button btnLink;
    private ProductInfo mInfo;
    private List<BluetoothDevice> bluetoothDevices = new ArrayList<>();
    private DeviceListAdapter deviceListAdapter ;
    private AlertDialog deveiceDialog;
    private ListView decivesListView;
    ArrayAdapter<String> adapter;
    // ????????????
    private Button mBtnCommit;
    // ??????????????????????????????
    private String mIncode;
    // ??????????????????TextView
    private TextView mTvIncode;
    //??????TextView
    private TextView mTvError;
    // ????????????
    private RelativeLayout mRvLog;
    //???????????????
    private ListView mLvBar;
    //?????????????????????view
    private View mHeaderView;
    private ImageView iv;

    private TextView tvScan;

    //????????????
    private Spinner mSp;
    private List<String> data_list;
    private ArrayAdapter<String> arr_adapter;

    private IQueryPresenter mPresenter = new QueryPresenter(this, this);

    private Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case PRINTER_LINK_SUCCESS:
                    onPrinterConnected();
                    break;
                case PRINTER_LINK_FAIL:
                    onPrinterDisconnected();
                    break;
                default:
                    break;


            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_query);
        initViews();
    }

    private void initViews() {
        llBarcode = (LinearLayout) findViewById(R.id.ll_barcode);
        mEtIncode = (EditText) findViewById(R.id.et_incode);
        mBtnCommit = (Button) findViewById(R.id.btn_commit);
        mDialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        mBtnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setEtSku("",false);
            }
        });
        mEtIncode.setImeOptions(EditorInfo.IME_ACTION_SEND);
        mEtIncode.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (event != null)
                    if (event.getAction() == KeyEvent.ACTION_UP) return true;
                if (actionId == EditorInfo.IME_ACTION_SEND || actionId == 0) {
                    setEtSku(mEtIncode.getText().toString(),false);
                    return true;
                }
                return false;
            }
        });
        tvScan = findViewById(R.id.tv_scan);
        tvScan.setOnClickListener(this);
        mTvError = (TextView) findViewById(R.id.tv_error);
        btnPrint = (Button) findViewById(R.id.btn_print);
        btnPrint.setOnClickListener(this);

        btnLink = (Button) findViewById(R.id.btn_link);
        btnLink.setOnClickListener(this);

        mTvIncode = (TextView) findViewById(R.id.tv_incode);
        mRvLog = (RelativeLayout) findViewById(R.id.rl_log);
        mLvBar = (ListView) findViewById(R.id.lv_bar);
        mSp = (Spinner) findViewById(R.id.sp_code);
        data_list = new ArrayList<String>();
        data_list.add("?????????");
        data_list.add("?????????");
        //?????????
        arr_adapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data_list);
        //????????????
        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //???????????????
        mSp.setAdapter(arr_adapter);

        bluetoothDevices = new ArrayList<>();
        deviceListAdapter  = new DeviceListAdapter();
//        deveiceDialog = new AlertDialog.Builder(QueryActivity.this).setTitle("??????????????????").setAdapter(new DeviceListAdapter(), new DeviceListItemClicker()).create();
        View view  = getLayoutInflater().inflate(R.layout.controller_volume,null);
        deveiceDialog = new AlertDialog.Builder(this).setTitle("??????????????????????????????")
                .setPositiveButton("????????????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPresenter.setPrintType(QueryPresenter.PRINT_TYPE_TWO);
                    }
                })
                .setNegativeButton("????????????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPresenter.setPrintType(QueryPresenter.PRINT_TYPE_ONE);
                    }
                }).setNeutralButton("????????????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPresenter.setPrintType(QueryPresenter.PRINT_TYPE_THREE);
                    }
                }).setView(view).create();
        decivesListView =  view.findViewById(R.id.listView);
        decivesListView.setAdapter(deviceListAdapter);
        decivesListView.setOnItemClickListener(new DeviceListItemClicker());


    }



    //??????????????????
    @Override
    public void showProductInfo(ProductInfo info) {
        mTvError.setText("");
        mInfo = info;
        currentRfid = info.rfid;
        initHeaderView();
        mRvLog.removeAllViews();
        mRvLog.addView(mHeaderView);
        showLvLog();
    }
    //??????????????????
    @Override
    public void showBarcodeInfo(List<BarcodeInfo> info) {
        mTvError.setText("");
        barcodeInfos.clear();
        barcodeInfos.addAll(info);
        if(barcodeInfoAdapter == null){
            barcodeInfoAdapter = new BarcodeInfoAdapter(this,barcodeInfos);
            mLvBar.setAdapter(barcodeInfoAdapter);
            barcodeInfoAdapter.setOnInViewClickListener(R.id.tv_size, new BaseListAdapter.onInternalClickListener() {
                @Override
                public void OnClickListener(View parentV, View v, Integer position, Object values) {
                    Intent it = new Intent(QueryActivity.this,FindActivity.class);
                    LogUtil.i("barcodeInfos.get(position).shelf.............................",barcodeInfos.get(position).shelf);
                    it.putExtra("shelf",barcodeInfos.get(position).shelf);
                    startAnimActivity(it);
                }
            });
        }else{
            barcodeInfoAdapter.notifyDataSetChanged();
        }

        showLvBar();
        mInfo = null;
    }

    @Override
    public void clearInfo() {
        mRvLog.removeAllViews();
    }

    @Override
    public String getIncode() {
        return mIncode;
    }

    @Override
    public void setIncode(String incode) {
        if (!TextUtils.isEmpty(incode)) {
            // ??????
            mTvIncode.setTextColor(Color.rgb(0, 87, 55));
            mTvIncode.setText(incode);
        }
    }

    @Override
    public void setWrongIncode(String incode) {
        if (!TextUtils.isEmpty(incode)) {
            // ??????
            mTvIncode.setTextColor(Color.RED);
            mTvIncode.setText(incode);
            mTvError.setText("???????????????");
        }
    }

    @Override
    public void playBeep() {
        beep();
    }

    @Override
    public String getChoose() {
        return  mSp.getSelectedItem().toString();
    }

    @Override
    public void openBluetooth() {

        Intent enabler = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enabler, 1);

    }

    @Override
    public void showBuletoothDevicesDialog(List<BluetoothDevice> list) {
        bluetoothDevices = list;
        deviceListAdapter.notifyDataSetChanged();
        showDialog();
    }









    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_print:
                //??????
                if(mPresenter.printLabel(mInfo)) {
                    //??????????????????
                    showNotice("????????????");
                }
                break;
            case R.id.btn_link:
                mPresenter.linkPrinter();
                break;

            case R.id.iv_product:
                ImageView i = new ImageView(this);
                i.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDialog.dismiss();
                    }
                });
                i.setImageDrawable(iv.getDrawable());
                mDialog.setContentView(i);
                mDialog.show();
                break;

            case R.id.tv_scan:
                readOrClose();
                break;
            default:
                break;

        }
    }










    @Override
    protected void onDestroy() {
        super.onDestroy();
        //???????????????
        mPresenter.destory();
//        unregisterReceiver(receiver);
    }

    @Override
    public void ShowData(Object o) {

    }


    // ??????????????????????????????Adapter
    private class DeviceListAdapter extends BaseAdapter {
        private TextView tv_name = null;
        private TextView tv_mac = null;

        @Override
        public int getCount() {
            return bluetoothDevices.size();
        }

        @Override
        public Object getItem(int position) {
            return bluetoothDevices.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(QueryActivity.this).inflate(R.layout.printer_item, null);
            }
            tv_name = (TextView) convertView.findViewById(R.id.tv_device_name);
            tv_mac = (TextView) convertView.findViewById(R.id.tv_macaddress);

            if (bluetoothDevices != null && bluetoothDevices.size() > position) {
                BluetoothDevice printer = bluetoothDevices.get(position);
                tv_name.setText(printer.getPrinterName());
                tv_mac.setText(printer.getPrinterAddress());
            }

            return convertView;
        }
    }

    // ????????????????????????????????????
    private class DeviceListItemClicker implements AdapterView.OnItemClickListener {


        @Override
        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
            dismissDialog();
            BluetoothDevice printer = bluetoothDevices.get(position);
            if (printer != null) {
                showLoading("");
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        if( mPresenter.link(bluetoothDevices.get(position).getPrinterName(), bluetoothDevices.get(position).getPrinterAddress())) {
                            mHandler.sendEmptyMessage(PRINTER_LINK_SUCCESS);
                        }else{
                            mHandler.sendEmptyMessage(PRINTER_LINK_FAIL);
                        }
                    }
                }.start();
//
            }

        }

    }

    // ??????????????????????????????
    private void onPrinterConnected() {
        // ??????????????????????????????????????????????????????????????????
        hideLoading();
        Toast.makeText(QueryActivity.this, this.getResources().getString(R.string.connectprintersuccess), Toast.LENGTH_SHORT).show();
    }

    // ?????????????????????????????????????????????????????????????????????????????????
    private void onPrinterDisconnected() {
        hideLoading();
        Toast.makeText(QueryActivity.this, this.getResources().getString(R.string.connectprinterfailed), Toast.LENGTH_SHORT).show();
    }

    public void showDialog(){

        if(deveiceDialog!= null && !deveiceDialog.isShowing()){
            deveiceDialog.show();
        }
    }

    public void dismissDialog(){
        if(deveiceDialog != null && deveiceDialog.isShowing()){
            deveiceDialog.dismiss();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initScanPresenter();
    }

    private void initScanPresenter() {

        scanPresenter = new ScanPresenter(this);
        scanPresenter.initData();


        scanPresenter.setCurrentSetting(ScanPresenter.Setting.bindingMore);
        scanPresenter.setListener(new OnFinishListener() {
            @Override
            public void OnFinish(String data) {

                SoundUtils.play(1);
                if (mEtIncode.hasFocus()) {
                    //????????????????????????API????????????????????????????????????
//                    mEtIncode.setText(data);
//
//                    mAdapter.clearAll();
//                    tvSum.setText("0");
                    setEtSku(data,false);
                    scanPresenter.initData();
                } else {
                   if (!flag) return;
                    readOrClose();
                    currentRfid = data;

                    setEtSku(data,true);

                    //????????????????????????????????????
//                    LinkedTreeMap map = new LinkedTreeMap<>();
//                    map.put("epc", data);
//                    viableData.add(map);
//                    tvSum.setText(String.valueOf(viableData.size()));
//                    mAdapter.notifyDataSetChanged();


                }
            }
        });
    }
    //    private BroadcastReceiver receiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//            if (android.bluetooth.BluetoothDevice.ACTION_FOUND.equals(action)) {
//                //???????????????
//
//                //?????????????????????????????????
//                if (device != null && isSupported(device.getPrinterName() + "")) {
//                    int position = -1;
//                    for (int i = 0; i < bluetoothDevices.size(); i++) {
//                        if (device.getPrinterName().equals(bluetoothDevices.get(i).getPrinterName())) {
//                            position++;
//                        }
//                    }
//                    if (position < 0) {
//                        addDeveice(device);
//                    }
//                }
//            }else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
//                device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//                switch (device.getBondState()) {
//                    case BluetoothDevice.BOND_BONDING://????????????
//
//                        break;
//                    case BluetoothDevice.BOND_BONDED://????????????
//
//                        break;
//                    case BluetoothDevice.BOND_NONE://????????????/?????????
//
//                    default:
//                        break;
//                }
//            }
//        }
//    };

    public IntentFilter makeFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);//???????????????????????????
        filter.addAction(android.bluetooth.BluetoothDevice.ACTION_FOUND);//?????????????????????
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);//?????????????????????
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);//?????????????????????
        filter.addAction(android.bluetooth.BluetoothDevice.ACTION_BOND_STATE_CHANGED);//????????????
        return filter;
    }

    @Override
    public void onBackPressed() {
        if(mPresenter.getCurrentPage()){
            super.onBackPressed();
        }else{
            showLvBar();
            mPresenter.setCurrentPage(true);
        }
    }

    void  showLvLog(){
        llBarcode.setVisibility(View.GONE);
        mRvLog.setVisibility(View.VISIBLE);
    }

    void showLvBar(){
        llBarcode.setVisibility(View.VISIBLE);
        mRvLog.setVisibility(View.GONE);
    }

    private void setInfo(LinearLayout llInfo) {
        llInfo.removeAllViews();
        int itemCount = mInfo.info.size() / 2 + (mInfo.info.size() % 2 == 0 ? 0 : 1);
        for (int i = 0; i < itemCount; i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.item_product_info, null);
            TextView tvLeft = (TextView) view.findViewById(R.id.tv_product_info_left);
            TextView tvRight = (TextView) view.findViewById(R.id.tv_product_info_right);
            Map<String, String> infoLeft = mInfo.info.get(i * 2);
            Iterator<String> itLeft = infoLeft.keySet().iterator();
            while (itLeft.hasNext()) {
                String key = itLeft.next();
                final String value = infoLeft.get(key);
                tvLeft.setText(key + "???" + value);
                if(key.equals("SKU")){
                    tvLeft.setTextColor(Color.BLUE);
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent it = new Intent(QueryActivity.this,SkuQueryActivity.class);
                            it.putExtra("sku",value);
                            startActivity(it);
                        }
                    });

//                    view.setOnClickListener(this);
                }
                if(key.equals("?????????")){
                    tvLeft.setTextColor(Color.BLUE);
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent it = new Intent(QueryActivity.this,FindActivity.class);
                            it.putExtra("rfid",mInfo.rfid+"");
                            it.putExtra("shelf",value);
                            startActivity(it);
                        }
                    });

//                    view.setOnClickListener(this);
                }
                break;
            }
            if (mInfo.info.size() > i * 2 + 1) {
                Map<String, String> infoRight = mInfo.info.get(i * 2 + 1);
                Iterator<String> itRight = infoRight.keySet().iterator();
                while (itRight.hasNext()) {
                    String key = itRight.next();
                    final String value = infoRight.get(key);
                    tvRight.setText(key + "???" + value);
                    if(key.equals("SKU")){
                        tvRight.setTextColor(Color.BLUE);

//                        view.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                Intent it = new Intent(mContext,SkuQueryActivity.class);
//                                it.putExtra("sku",value);
//                                mContext.startActivity(it);
//                            }
//                        });
//                        view.setOnClickListener(this);
                    }
                    break;
                }
            }
            llInfo.addView(view);
        }
    }

    private void initHeaderView() {
        if(mHeaderView == null){
            mHeaderView = LayoutInflater.from(this).inflate(R.layout.header_product_log, null);
            // ??????
            iv = (ImageView) mHeaderView.findViewById(R.id.iv_product);
            // ??????????????????
            View logTitle = mHeaderView.findViewById(R.id.ll_log_title);
//            logTitle.setVisibility(mInfo.logs.size() > 0 ? View.VISIBLE : View.GONE);
            logTitle.setVisibility(View.GONE);
        }

        LinearLayout llInfo = (LinearLayout) mHeaderView.findViewById(R.id.ll_product_info);
        Glide.with(this).load(mInfo.picUrl).placeholder(R.drawable.image_default).into(iv);
        iv.setOnClickListener(this);
        // ????????????
        setInfo(llInfo);


    }

    class BarcodeInfoAdapter extends BaseListAdapter<BarcodeInfo>{

        public BarcodeInfoAdapter(Context context, List<BarcodeInfo> list) {
            super(context, list);
        }

        @Override
        public View bindView(int position, View convertView, ViewGroup parent) {
            if(convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_query_sku, null);
            }

            BarcodeInfo info = list.get(position);
                 TextView   tvIncode = ViewHolder.get(convertView,R.id.tv_shelf);
            TextView       tvShelf= ViewHolder.get(convertView,R.id.tv_size);
            TextView tvState= ViewHolder.get(convertView,R.id.tv_num);

            tvIncode.setText(String.valueOf(info.incode));
            tvShelf.setText(String.valueOf(info.shelf));
            if(info.isbad == 0){
                tvState.setText(String.valueOf("??????"));
            }else{
                tvState.setText(String.valueOf("??????"));
            }


            return  convertView;
        }


    }
    public void setEtSku(String barcode,boolean isRfid) {

//        int num = Integer.parseInt(mEtIncode.getText().toString());
        if (barcode.isEmpty()) {

            return;
        }
        mIncode = barcode;
        mPresenter.query(isRfid);
        mEtIncode.setText("");
    }

    public void readOrClose() {
        if(mEtIncode.hasFocus())  return;
        if (tvScan.getText().toString().equals("??????")) {
            tvScan.setBackground(getDrawable(R.drawable.bg_circle_red));
            flag = true;

                scanPresenter.setReadDataModel(0);
                scanPresenter.startReadRfid();
                mEtIncode.setEnabled(false);

            tvScan.setText("??????");

        } else {
            flag = false;
            tvScan.setBackground(getDrawable(R.drawable.bg_circle));
            scanPresenter.stopReadRfid();
            mEtIncode.setEnabled(true);
            tvScan.setText("??????");
//            searchEpcInfo();
        }

    }

        private void searchEpcInfo() {
            ArrayList<Sku> skus = new ArrayList<>();
            skus.add(new Sku("111111111","11111111","1111111","ABCDABCDABCDABCDABCD0002"));
            skus.add(new Sku("222222222","11111111","1111111","888888888888888888880025"));
            skus.add(new Sku("333333333","11111111","1111111","888888888888888888880047"));
            skus.add(new Sku("444444444","11111111","1111111","888888888888888888880024"));
            skus.add(new Sku("555555555","11111111","1111111","888888888888888888880077"));
            skus.add(new Sku("6666666666","11111111","1111111","888888888888888888880097"));
            Intent intent = new Intent(this,FindActivity.class);
            intent.putExtra("shelf","FDSF121-FSD");
            intent.putExtra("sku",skus);
            startAnimActivity(intent);
            if (!flag) return;

//            if (mapList.size() == 0) return;
//            List<String> epcList = new ArrayList<>();
//            for (Map<String, String> map : mapList) {
//                epcList.add(map.get("epc"));
//            }

        }
    @Override
    public void onPause() {
        super.onPause();
        scanPresenter.stopReadRfid();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 523 ) {
            if(mEtIncode.hasFocus()) return super.onKeyDown(keyCode, event);;
            if (event.getRepeatCount() == 0) {
               readOrClose();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
