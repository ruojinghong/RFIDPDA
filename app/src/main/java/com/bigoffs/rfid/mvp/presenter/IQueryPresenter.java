package com.bigoffs.rfid.mvp.presenter;

import com.bigoffs.rfid.mvp.bean.ProductInfo;

/**
 * @author okbuy-001
 * @desc
 * @time 2020/4/24 16:08
 */
public interface IQueryPresenter {
    void load();
    void query(boolean isRfid);
    void destory();
    boolean printLabel(ProductInfo info);
    boolean linkPrinter();
    boolean  link(String name,String address);
    void itemQuery(String incode);
    boolean getCurrentPage();
    void setCurrentPage(boolean b);
    void setPrintType(int type);

    void queryRfid2Incode(String data);
}
