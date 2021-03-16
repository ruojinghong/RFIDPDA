package com.bigoffs.rfid.ui.custom;

import com.bigoffs.rfid.mvp.bean.dao.ProductInfoCase;

/**
 * @author okbuy-001
 * @desc
 * @time 2020/5/27 11:44
 */
public interface IPickListener {
    void Pickup(ProductInfoCase infoCase);
}
