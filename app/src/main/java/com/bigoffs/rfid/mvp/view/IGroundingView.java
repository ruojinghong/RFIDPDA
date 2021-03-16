package com.bigoffs.rfid.mvp.view;

import com.bigoffs.rfid.mvp.bean.GroundingBillList;

/**
 * @author okbuy-001
 * @descincode
 * @time 2018/12/6 10:53
 */
public interface IGroundingView extends ICommonView{
    void showList(GroundingBillList list);
    String getIncode();
    void setIncode(String incode);
    void setErrIncode(String incode);
    String getChoose();
    void clearList();
    void goDetailActivity(String arrivalId, String arrivalCode, String taskId, int type);
}
