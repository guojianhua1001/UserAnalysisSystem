package com.baizhi.entity;

import java.io.Serializable;

/**
 * 评估项名称
 */
public interface EvaluateType extends Serializable {

    String REGION_EVAL = "region";                //异地登录风险
    String DEVICE_EVAL = "device";                //更换设备风险
    String LOGIN_COUNT_EVAL = "login_count";      //评估次数风险
    String LOGIN_HABIT_EVAL = "login_habit";      //登录习惯风险
    String SPEED_EVAL = "speed";                  //位移速度风险
    String PASSWORD_EVAL = "password";            //密码错误风险
    String INPUT_FEATURE_EVAL = "input_feature";  //输入特征风险

}
