package com.bigoffs.rfid.mvp.view;



import com.bigoffs.rfid.mvp.bean.GroundingDetailInfo;

import java.util.List;

/**
 * @author okbuy-001
 * @desc
 * @time 2018/12/7 17:50
 */
public interface IGroundingDetailView extends ICommonView {

    void showCode(String code);
    void showErrMessage(String code);
    void showList(List<GroundingDetailInfo> list);
    void getFoucs(int i);
    String getArrivalId();
    void showDialog();
    void scanSwitch(boolean b);
    void playBeep();
    void goDetail(int num);
    void setEdShelf(String shelf);
    void add(String incode);
    void addIncodeError(String incode);
}
