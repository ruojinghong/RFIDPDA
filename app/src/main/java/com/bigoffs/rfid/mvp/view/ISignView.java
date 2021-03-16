package com.bigoffs.rfid.mvp.view;

import com.bigoffs.rfid.mvp.bean.Allocation;
import com.bigoffs.rfid.mvp.bean.AllocationError;
import com.bigoffs.rfid.mvp.bean.dao.AllcationCase;

import java.util.List;

/**
 * @author okbuy-001
 * @desc
 * @time 2020/4/29 16:37
 */
public interface ISignView<T> extends ICommonView{
    void initData(Allocation allocation);
    void setSignNum(int num);
    void setSignedNum(int num);
    void openOrClose();
    void setReceiveNum(int num);
    void setErrorNum(int num);
    void showErrorList(T t);
    void close(String s);
    void add(String rfid);
    void addError(String rfid);
    void finishPartSign();
    void finishAllocationSign();
    void setItemIncode(String rfid);


    void addInccodeError(String toString);
    void refresh(List<String> data);
    List<AllocationError> getErrorList();
    void setErrorList(List<AllocationError> errorList);
}
