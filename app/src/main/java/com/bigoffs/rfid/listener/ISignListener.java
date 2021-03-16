package com.bigoffs.rfid.listener;

/**
 * @author okbuy-001
 * @desc
 * @time 2020/5/6 16:49
 */
public interface ISignListener {
    void deleteItem(int position);
    void setItemIncode(String rfid);
    void queryIncode(String rfid);
}
