package com.baizhi.update.impl;

import com.baizhi.entity.HistoryData;
import com.baizhi.entity.LoginSuccessData;
import com.baizhi.update.UpdateChain;
import com.baizhi.update.UpdateHandler;

/**
 * 修改上一次登录经纬度
 */
public class LastPointUpdateHandler implements UpdateHandler {
    @Override
    public void invoke(HistoryData historyData, LoginSuccessData loginSuccessData, UpdateChain updateChain) {

        historyData.setLastPoint(loginSuccessData.getGeoPoint());
        //更新链调用
        updateChain.doChain(historyData, loginSuccessData);
    }
}
