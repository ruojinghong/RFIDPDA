package com.bigoffs.rfid.mvp.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by okbuy on 17-2-17.
 */

public class SerializeTallyProduct implements Serializable {
    public List<InventoryInfoNew> data;

    public SerializeTallyProduct(List<InventoryInfoNew> data) {
        this.data = data;
    }
}
