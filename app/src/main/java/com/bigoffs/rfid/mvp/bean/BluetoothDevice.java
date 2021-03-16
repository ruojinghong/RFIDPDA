package com.bigoffs.rfid.mvp.bean;

/**
 * @author okbuy-001
 * @desc
 * @time 2020/4/24 16:19
 */
public class BluetoothDevice {

    private String mPrinterName;
    private String mPrinterAddress;

    public BluetoothDevice(String printerName, String printerAddress) {
        mPrinterName = printerName;
        mPrinterAddress = printerAddress;
    }

    public String getPrinterName() {
        return mPrinterName;
    }

    public String getPrinterAddress() {
        return mPrinterAddress;
    }
}
