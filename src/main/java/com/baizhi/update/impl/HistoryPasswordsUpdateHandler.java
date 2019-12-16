package com.baizhi.update.impl;

import com.baizhi.entity.HistoryData;
import com.baizhi.entity.LoginSuccessData;
import com.baizhi.update.UpdateChain;
import com.baizhi.update.UpdateHandler;

import java.util.HashSet;
import java.util.Set;

/**
 * 修改历史密码(乱序)集合
 */
public class HistoryPasswordsUpdateHandler implements UpdateHandler {
    @Override
    public void invoke(HistoryData historyData, LoginSuccessData loginSuccessData, UpdateChain updateChain) {

        Set<String> historyPasswords = historyData.getHistoryPasswords();
        if (historyPasswords != null) {
            historyPasswords.add(loginSuccessData.getOrderlessPassword());
            historyData.setHistoryPasswords(historyPasswords);
        } else {
            HashSet<String> strings = new HashSet<>();
            strings.add(loginSuccessData.getOrderlessPassword());
            historyData.setHistoryPasswords(strings);
        }

        //跟新链调用
        updateChain.doChain(historyData, loginSuccessData);

    }
}
