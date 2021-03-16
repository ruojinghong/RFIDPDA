package com.bigoffs.rfid.mvp.view;



import com.bigoffs.rfid.mvp.bean.BarcodeInfo;
import com.bigoffs.rfid.mvp.bean.BluetoothDevice;
import com.bigoffs.rfid.mvp.bean.ProductInfo;

import java.util.List;

/**
 * @author okbuy-001
 * @desc
 * @time 2020/4/24 15:53
 */
public interface IQueryView extends ICommonView{
    void showProductInfo(ProductInfo info);
    void showBarcodeInfo(List<BarcodeInfo> info);
    void clearInfo();
    String getIncode();
    void setIncode(String incode);
    void setWrongIncode(String incode);
    void playBeep();
    String getChoose();
    void openBluetooth();
    void  showBuletoothDevicesDialog(List<BluetoothDevice> list);
}
