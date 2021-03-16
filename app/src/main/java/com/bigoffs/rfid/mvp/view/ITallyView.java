package com.bigoffs.rfid.mvp.view;

import com.bigoffs.rfid.mvp.bean.TransferInfo;

import java.util.List;
import java.util.Map;

/**
 * @author okbuy-001
 * @desc
 * @time 2018/12/11 10:46
 */
public interface ITallyView extends ICommonView {

    void showCode(String code);
    void showErrMessage(String code);
    void getFoucs(int i);
    void showList(List<TransferInfo.FailIncodesBean> list);
    void scanSwitch(boolean b);
    void showCurrentShelf(String text);
    void clearPage();
    void playBeep();
    void setCount(int count);
    List<String> getRfids();
    Map<String,String> getIncodes();

    void updateListCount();

}
