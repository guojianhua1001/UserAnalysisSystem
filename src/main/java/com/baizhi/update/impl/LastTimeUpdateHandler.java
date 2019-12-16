package com.baizhi.update.impl;

import com.baizhi.entity.HistoryData;
import com.baizhi.entity.LoginSuccessData;
import com.baizhi.update.UpdateChain;
import com.baizhi.update.UpdateHandler;

/**
 * 修改最后一次登录时间
 */
public class LastTimeUpdateHandler implements UpdateHandler {
    @Override
    public void invoke(HistoryData historyData, LoginSuccessData loginSuccessData, UpdateChain updateChain) {

        historyData.setLastTime(loginSuccessData.getCurrentTime());

        //更新链调用
        updateChain.doChain(historyData, loginSuccessData);
    }
}
