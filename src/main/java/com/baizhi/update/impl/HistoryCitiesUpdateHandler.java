package com.baizhi.update.impl;

import com.baizhi.entity.HistoryData;
import com.baizhi.entity.LoginSuccessData;
import com.baizhi.update.UpdateChain;
import com.baizhi.update.UpdateHandler;

import java.util.HashSet;
import java.util.Set;

/**
 * 修改历史登录城市
 */
public class HistoryCitiesUpdateHandler implements UpdateHandler {
    @Override
    public void invoke(HistoryData historyData, LoginSuccessData loginSuccessData, UpdateChain updateChain) {
        Set<String> historyCities = historyData.getHistoryCities();
        if (historyCities != null) {
            historyCities.add(loginSuccessData.getRegion());
            historyData.setHistoryCities(historyCities);
        } else {
            Set<String> strings = new HashSet<String>();
            strings.add(loginSuccessData.getRegion());
            historyData.setHistoryCities(strings);
        }

        //责任链调用
        updateChain.doChain(historyData, loginSuccessData);
    }
}
