package com.bigoffs.rfid.mvp.view;

import com.bigoffs.rfid.mvp.bean.dao.GroundingAllocationDetailInfo;
import com.bigoffs.rfid.mvp.bean.dao.PutAwayInfo;

import java.util.List;

/**
 * @author okbuy-001
 * @desc
 * @time 2020/5/15 17:53
 */
public interface IGroundingAllocationDetailView extends ICommonView{
    void showCode(String code);
    void showErrMessage(String code);
    void showList(List<GroundingAllocationDetailInfo> list);
    void getFoucs(int i);
    String getArrivalId();
    void showDialog();
    void scanSwitch(boolean b);
    void playBeep();
    void goDetail(int num);
    void setEdShelf(String shelf);
    void add(String incode);
    void addIncodeError(String incode);
    void initData(PutAwayInfo info);
    void refresh(List<String> data);
    void addError(String  rfid);

    void setItemIncode(String result);
}
