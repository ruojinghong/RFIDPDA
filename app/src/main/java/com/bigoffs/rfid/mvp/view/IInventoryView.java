package com.bigoffs.rfid.mvp.view;

/**
 * @author okbuy-001
 * @desc
 * @time 2020/5/21 10:27
 */
public interface IInventoryView extends ICommonView  {

        void updataErrorList();


        void initData(int size);

    void addScanNum();

    void finishTask(String result);

    void setShelfArea(String shelfArea);
}
