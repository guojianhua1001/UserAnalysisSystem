package com.baizhi.update;

import com.baizhi.entity.HistoryData;
import com.baizhi.entity.LoginSuccessData;

/**
 * 更新历史数据处理程序
 */
public interface UpdateHandler {

    //根据登录成功数据修改历史数据
    void invoke(HistoryData historyData, LoginSuccessData loginSuccessData, UpdateChain updateChain);
}
