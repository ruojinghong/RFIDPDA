package com.bigoffs.rfid.mvp.presenter;

import com.bigoffs.rfid.MApplication;
import com.bigoffs.rfid.listener.OnFinishListener;
import com.bigoffs.rfid.mvp.view.IDataFragmentView;
import com.bigoffs.rfid.persistence.util.SPUtils;
import com.bigoffs.rfid.service.IScanService;
import com.bigoffs.rfid.service.ScanServiceControl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author okbuy-001
 * @desc
 * @time 2020/4/22 16:41
 */
public class ScanPresenter {
    int num = 0;
    //已采集的数据
    private static Map<String, String> map;
    //继初始化已采集数据之后，新增的数据都会加入到这个集合中
    private static List<String> out;

    IDataFragmentView view;
    private static Facility currentFacility = Facility.YBX;

    private IScanService mService = ScanServiceControl.getScanService();

    public static List<String> getOut() {
        return out;
    }

    public static Map<String, String> getMap() {
        return map;
    }

    public ScanPresenter(IDataFragmentView view) {
        this.view = view;
        map = new HashMap<>();
        out = new LinkedList<>();
    }
    public void initOut(List<String> datas) {
        out.addAll(datas);
    }
    public void initMap(List<String> datas) {
        for (String data : datas) {
            map.put(data, null);
            num++;
        }
    }

    public void initData() {
        num = 0;
        map.clear();
        out.clear();
    }
    // 1返回@+信号强度
    public void setReadDataModel(int model) {
        mService.setReadDataMode(model);
    }
    //设置模式，0 = 是混合模式 ， 1 = 只读标签 ， 2 = 只扫描条码
    public void setMode(int mode) {
        mService.setMode(mode);
    }

    //初始化已采集的数据
    public void initData(List<String> datas) {
        for (String data : datas) {
            map.put(data, null);
            num++;
        }
    }

    //清理部分已采集的数据
    public void clearPartOfData(List<String> datas) {
        for (String data : datas) {
            map.remove(data);
            out.remove(data);
            num--;
        }
    }
    //开始读标签
    public void startReadRfid() {
        mService.startInventory();
    }
    //停止读标签
    public void stopReadRfid() {
        mService.stopInventory();
    }
    //开始扫条码
    public void startScanBarcode() {
        mService.scanBarcode();
    }
    //停止扫条码
    public void setCurrentSetting(Setting setting) {
        mService.setCurrentSetting(Setting.getSettingMap(setting));
    }
    //设置回调,没有通过内存排重的机制
    public void setListener(OnFinishListener listener) {
        mService.setListener(listener);
    }
    //多增加一个监听
    public void setAnotherListener(OnFinishListener listener){mService.setAnotherListener(listener);};
    public void removeAnotherListener(){mService.removeAnotherListener();};
    //清除回调
    public void removeListener() {
        mService.removeListener();
    }
    //设置回调，通过内存排重
    public void setListenerProtectModel(final OnFinishListener listener) {
        mService.setListener(new OnFinishListener() {
            @Override
            public void OnFinish(String data) {
                map.put(data, null);
                if (num < map.size()) {
                    num++;
                    out.add(data);
                    listener.OnFinish(data);
                }
            }
        });

    }
    //在有携带能量值的标签集合中，找到能量最强的，也是离手持机最近的epc
    public String searchNearestEpc(List<String> tags) {
        Map<Integer, String> treeMap = null;
        ArrayList<Integer> nums = null;
        try {
            treeMap = new TreeMap<>();
            nums = new ArrayList<>();
            for (String data : tags) {
                String[] split = data.split("@");
                String tag = split[0];
                int rssi;
                if (split.length > 1) {
                    rssi = Integer.parseInt(split[1]);
                } else continue;

                treeMap.put(rssi, tag);
                nums.add(rssi);
            }
            Collections.sort(nums, new Comparator<Integer>() {
                @Override
                public int compare(Integer lhs, Integer rhs) {
                    return rhs - lhs;
                }
            });

            return treeMap.get(nums.get(0));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    //返回已采集的数据数
    public int getNum() {
        return num;
    }

    public enum Setting {

        binding, bindingMore, stockRead,;

        public static Map<String, Object> getSettingMap(Setting setting) {
            Map<String, Object> map = new HashMap<>();
            switch (setting) {
                case binding:
                    switch (currentFacility) {
                        case YBX:
                            map.put("power", SPUtils.get(MApplication.getInstance(), "power", 30));
                            break;
                        case CW:
                            map.put("power", 10);
                            break;
                    }
                    break;

                case bindingMore:
                    switch (currentFacility) {
                        case YBX:
                            map.put("power", SPUtils.get(MApplication.getInstance(), "power", 30));
                            break;
                        case CW:
                            map.put("power", 30);
                            break;
                    }
                    break;

                case stockRead:
                    switch (currentFacility) {
                        case YBX:
                            map.put("power", SPUtils.get(MApplication.getInstance(), "power", 30));
                            break;
                        case CW:
                            map.put("power", 30);
                            break;
                    }
                    break;

                default:

                    break;
            }
            return map;
        }
    }

    enum Facility {
        YBX, CW
    }
}
