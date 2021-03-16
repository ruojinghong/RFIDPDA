package com.bigoffs.rfid.mvp.view;


import com.bigoffs.rfid.mvp.bean.PickingDetail;
import com.bigoffs.rfid.mvp.bean.PickingDetailNew;
import com.bigoffs.rfid.mvp.bean.dao.ProductInfoCase;

/**
 * Created by okbuy on 17-3-30.
 */

public interface IPickingDetailView extends ICommonView{

    String getEditString();
    void clearEdit();
    int getPickingTaskId();
    void setRightIncode(String incode);
    void setWrongIncode(String incode);
    void setPickInfo(PickingDetail info);
    void finishPicking();
    void changShelf(PickingDetailNew info);
    void scanSwitch(boolean b);
    void playBeep();
    void goListViewTop();
    void closeActivity();

    void finishData();

    void showFindDialog(ProductInfoCase infoCase);

    void startScan();

    void colseChildOnclick();
}
