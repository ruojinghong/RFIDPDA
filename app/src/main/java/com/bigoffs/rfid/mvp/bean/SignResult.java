package com.bigoffs.rfid.mvp.bean;

/**
 * @author okbuy-001
 * @desc
 * @time 2020/5/8 18:03
 */
public class SignResult {

    public String box;
    public int receiveNum;
    public int totalNum;

    public SignResult(String box, int receiveNum, int totalNum) {
        this.box = box;
        this.receiveNum = receiveNum;
        this.totalNum = totalNum;
    }
}
