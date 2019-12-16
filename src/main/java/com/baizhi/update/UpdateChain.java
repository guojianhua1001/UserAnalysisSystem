package com.baizhi.update;

import com.baizhi.entity.HistoryData;
import com.baizhi.entity.LoginSuccessData;

import java.util.ArrayList;
import java.util.List;

/**
 * 更新历史数据处理链
 * 采用模式：责任链模式
 */
public class UpdateChain {

    //更新操作处理者集合
    private List<UpdateHandler> updateHandlers = new ArrayList<>();
    //操作所处的位置
    private int pos;
    //操作总数量
    private int size;

    /**
     * 添加处理者
     *
     * @param handler 处理者
     */
    public UpdateChain addHandler(UpdateHandler handler) {
        updateHandlers.add(handler);
        size++;
        return this;
    }

    /**
     * 链式处理
     *
     * @param historyData      历史数据
     * @param loginSuccessData 登录成功数据
     */
    public void doChain(HistoryData historyData, LoginSuccessData loginSuccessData) {
        if (pos < size) {
            UpdateHandler updateHandler = updateHandlers.get(pos);
            pos++;
            updateHandler.invoke(historyData, loginSuccessData, this);
        }
    }

    public UpdateChain() {
    }

    public UpdateChain(List<UpdateHandler> updateHandlers) {
        this.updateHandlers = updateHandlers;
        this.size = updateHandlers.size();
    }
}
