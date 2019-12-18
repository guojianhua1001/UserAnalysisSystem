package com.baizhi.update.impl;

import com.baizhi.entity.HistoryData;
import com.baizhi.entity.LoginSuccessData;
import com.baizhi.update.UpdateChain;
import com.baizhi.update.UpdateHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * 修改历史用户特征集合(最多存储最近的10条记录)
 */
public class HistoryVectorsUpdateHandler implements UpdateHandler {
    @Override
    public void invoke(HistoryData historyData, LoginSuccessData loginSuccessData, UpdateChain updateChain) {

        List<double[]> historyVectors = historyData.getHistoryVectors();
        if (historyVectors != null) {
            //如果不为空，再判断数量
            if (historyVectors.size() > 10) {
                historyVectors.remove(0);
            }
            historyVectors.add(loginSuccessData.getInputFeature());
        } else {
            ArrayList<double[]> doubles = new ArrayList<>();
            doubles.add(loginSuccessData.getInputFeature());
            historyData.setHistoryVectors(doubles);
        }

        //更新链调用
        updateChain.doChain(historyData, loginSuccessData);
    }
}
