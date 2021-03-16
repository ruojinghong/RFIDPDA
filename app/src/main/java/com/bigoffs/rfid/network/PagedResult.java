package com.bigoffs.rfid.network;

import com.bigoffs.rfid.GlobalCfg;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by okbuy on 17-2-22.
 */

public class PagedResult {

    private int counts;

    private String results;

    private int pageSize;

    public PagedResult(String json) {
        this(json, Integer.parseInt(GlobalCfg.PAGE_SIZE));
    }

    public PagedResult(String json, int pageSize) {
        try {
            JSONObject object = new JSONObject(json);
            counts = object.optInt("counts");
            results = object.optString("results");
            this.pageSize = counts / pageSize
                    + (counts % pageSize == 0 ? 0 : 1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getCount() {
        return counts;
    }

    public int getPageSize() {
        return pageSize;
    }

    public String getResults() {
        return results;
    }

}
