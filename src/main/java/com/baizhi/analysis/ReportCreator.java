package com.baizhi.analysis;

import com.baizhi.entity.EvaluateData;
import com.baizhi.entity.HistoryData;

public interface ReportCreator {

    /**
     * 根据历史数据和评估数据生成评估报告
     *
     * @param historyData  历史数据
     * @param evaluateData 评估数据
     */
    public void createEvalReport(HistoryData historyData, EvaluateData evaluateData);

}
