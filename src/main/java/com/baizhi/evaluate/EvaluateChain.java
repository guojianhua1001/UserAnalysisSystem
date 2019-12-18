package com.baizhi.evaluate;

import com.baizhi.entity.EvalReport;
import com.baizhi.entity.EvaluateData;
import com.baizhi.entity.HistoryData;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 评估处理链
 */
public class EvaluateChain implements Serializable {

    //评估处理程序集合
    private List<EvaluateHandler> evaluateHandlers = new ArrayList<>();
    //评估链当前位置
    private int pos;
    //评估链的长度
    private int size;

    /**
     * 添加处理者
     *
     * @param handler 处理者
     */
    public EvaluateChain addHandler(EvaluateHandler handler) {
        evaluateHandlers.add(handler);
        size++;
        return this;
    }

    public void doChain(EvaluateData evaluateData, HistoryData historyData, EvalReport report) {

        if (pos < size) {
            EvaluateHandler evaluateHandler = evaluateHandlers.get(pos);
            pos++;
            evaluateHandler.invoke(evaluateData, historyData, report, this);
        } else {
            report.setApplicationId(evaluateData.getApplicationID());
            report.setLoginSequence(evaluateData.getLoginSequence());
            report.setRegion(evaluateData.getRegion());
            report.setUserId(evaluateData.getUserID());
            report.setReportTime(evaluateData.getCurrentTime());
        }

    }

    public EvaluateChain() {
    }

    public EvaluateChain(List<EvaluateHandler> evaluateHandlers) {
        this.evaluateHandlers = evaluateHandlers;
        this.size = evaluateHandlers.size();
    }
}
