package com.bigoffs.rfid.service.YBX;



public interface ReadedListener {
    /**
     * 读取的事件
     *
     * @param context
     */
    void onReaded(RfidContext context);
}
