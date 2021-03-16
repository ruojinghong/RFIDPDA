package com.bigoffs.rfid.mvp.view;


import com.bigoffs.rfid.mvp.bean.TallyTaskInfo;

/**
 * Created by okbuy on 17-2-20.
 */

public interface ITallyUpView extends ICommonView{
    void setRightIncode(String incode);
    void setWrongIncode(String incode);
    void setShelfNumber(String shelfNumber);
    void setTaskInfo(TallyTaskInfo info);
    void initIncodeEdit();
    void resetIncode();
    int getTallyTaskId();
    String getIncode();
    void showOrHideRemoveBtn(boolean show);
    void clear();
    void playBeep();
    void clearEdit();
    void showCount(int count);
    void goTallyDetail();
    void finishUp();
}
