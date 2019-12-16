package com.baizhi.evaluate.impl;

import com.baizhi.entity.EvalReport;
import com.baizhi.entity.EvaluateData;
import com.baizhi.entity.EvaluateType;
import com.baizhi.entity.HistoryData;
import com.baizhi.evaluate.EvaluateChain;
import com.baizhi.evaluate.EvaluateHandler;
import com.baizhi.util.LoginEvaluate;

import java.util.Map;

/**
 * 设备更换登录风险评估
 */
public class DeviceReplaceEvaluateHandler implements EvaluateHandler {
    @Override
    public void invoke(EvaluateData evaluateData, HistoryData historyData, EvalReport evalReport, EvaluateChain evaluateChain) {

        Map<String, Boolean> reportItems = evalReport.getReportItems();
        reportItems.put(EvaluateType.DEVICE_EVAL, LoginEvaluate.replaceEquipmentEval(evaluateData.getUserAgent(), historyData.getHistoryDevices()));
        //评估链的调用
        evaluateChain.doChain(evaluateData, historyData, evalReport);
    }
}
