package com.baizhi.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 历史数据
 * 确定依据：登录风险评估
 */
@Data
@Accessors(chain = true)
public class HistoryData {
    private Set<String> historyCities;      //历史登录城市集合
    private Long lastTime;                  //上一次登录时间
    private double[] lastPoint;             //上一次登录经纬度
    private Set<String> historyDevices;    //历史登录设备
    private Map<String, Map<String, Integer>> historicalHabits;  //历史登录习惯
    private Integer currentDayEvalCounts;   //当天的评估次数
    private Set<String> historyPasswords;   //历史密码集合
    private List<double[]> historyVectors;  //历史用户特征集合(最多存储最近的10条记录)


}
