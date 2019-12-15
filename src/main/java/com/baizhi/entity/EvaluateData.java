package com.baizhi.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 评估数据
 * 数据来源：登录评估日志
 */
@Data
@Accessors(chain = true)
public class EvaluateData implements Serializable {

    private Long currentTime;       //当前时间
    private String applicationID;   //应用编号
    private String userID;          //用户ID
    private String loginSequence;   //登录序列
    private String orderlessPassword;   //乱序密码
    private String region;          //区域
    private double[] geoPoint;      //经纬度
    private double[] inputFeature;  //输入特征
    private String userAgent;       //User-Agent

}
