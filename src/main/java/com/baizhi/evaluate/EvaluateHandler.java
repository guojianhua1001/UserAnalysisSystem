package com.baizhi.evaluate;

import com.baizhi.entity.EvalReport;
import com.baizhi.entity.EvaluateData;
import com.baizhi.entity.HistoryData;

import java.io.Serializable;

/**
 * 风险评估
 */
public interface EvaluateHandler extends Serializable {

    //根据评估数据和历史数据完成评估报告
    void invoke(EvaluateData evaluateData, HistoryData historyData, EvalReport evalReport, EvaluateChain evaluateChain);

}
