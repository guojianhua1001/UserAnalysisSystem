package com.baizhi.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 评估报告实体类
 * 数据库表结构
 * application_id | user_id | region | login_sequence | timestamp | device_replace | login_habit | login_count | region | displacement_speed | wrong_password_component | input_feature
 */
@Data
@Accessors(chain = true)
public class EvalReport implements Serializable {

    private String applicationId;   //应用ID
    private String userId;          //用户ID
    private String region;          //地区
    private String loginSequence;   //登录序列
    private long reportTime;        //报告时间
    private Map<String, Boolean> reportItems = new HashMap<>(); //评估子项

}
