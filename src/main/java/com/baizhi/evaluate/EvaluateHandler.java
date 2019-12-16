package com.baizhi.evaluate;

import com.baizhi.entity.EvalReport;
import com.baizhi.entity.EvaluateData;
import com.baizhi.entity.HistoryData;

/**
 * 风险评估
 */
public interface EvaluateHandler {

    //根据评估数据和历史数据完成评估报告
    void invoke(EvaluateData evaluateData, HistoryData historyData, EvalReport evalReport, EvaluateChain evaluateChain);

}
