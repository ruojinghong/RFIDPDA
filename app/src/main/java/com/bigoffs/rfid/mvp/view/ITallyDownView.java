package com.bigoffs.rfid.mvp.view;

/**
 * Created by okbuy on 17-2-17.
 */

public interface ITallyDownView extends ICommonView{
    void setRightIncode(String incode);
    void setWrongIncode(String incode);
    void resetIncode();
    String getIncode();
    void showOrHideRemoveBtn(boolean show);
    void clear();
    void playBeep();
    void goTallyUp(int taskId);
    void showCount(int count);
    void goTallyDetail();
}
