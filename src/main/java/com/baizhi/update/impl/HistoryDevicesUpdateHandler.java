package com.baizhi.update.impl;

import com.baizhi.entity.HistoryData;
import com.baizhi.entity.LoginSuccessData;
import com.baizhi.update.UpdateChain;
import com.baizhi.update.UpdateHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * 修改历史登录设备(最多存储近期的3台设备)
 */
public class HistoryDevicesUpdateHandler implements UpdateHandler {
    @Override
    public void invoke(HistoryData historyData, LoginSuccessData loginSuccessData, UpdateChain updateChain) {

        List<String> historyDevices = historyData.getHistoryDevices();
        if (historyDevices == null) {
            //如果是第一次登录
            ArrayList<String> arrayList = new ArrayList<>();
            arrayList.add(loginSuccessData.getUserAgent());
            historyData.setHistoryDevices(arrayList);
        } else {
            //如果不是第一次登录，则判断是否已存在，若不存在则去掉index为0的值，并添加新值；已存在则忽略此操作
            if (!historyDevices.contains(loginSuccessData.getUserAgent())) {
                //不存在历史登录记录里
                historyDevices.remove(0);
                historyDevices.add(loginSuccessData.getUserAgent());
            }

        }

        //责任链调用
        updateChain.doChain(historyData, loginSuccessData);
    }
}
