package com.baizhi.risk;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 登录风险评估
 * 辅助业务系统完成盗号或者账号被窃等风险检测。
 */
public class LoginRiskAssessment {

    /**
     * 异地登录评估 （☆）
     * 评估数据：当前登陆城市 beijing
     * 历史数据 : 留存用户的历史登陆城市一个Set集合[beijing,shanghai,zhengzhou]
     *
     * @param currentCity
     * @param historyCities
     * @return
     */
    public boolean offSiteLoginEval(String currentCity, Set<String> historyCities) {

        return false;
    }

    /**
     * 登陆的位移速度(距离/时间)  (☆☆☆）
     * 评估数据：当前登陆时间/当前登陆城市的地理坐标
     * 历史数据：最近一次登陆时间/最近一次登陆地理坐标
     *
     * @param currentTime
     * @param currentPoint
     * @param lastTime
     * @param lastPoint
     * @return
     */
    public boolean speedOfDisplacementEval(Long currentTime, Double[] currentPoint, Long lastTime, Double[] lastPoint) {

        return false;
    }


    /**
     * 更换设备存在风险（☆）
     * 评估数据：当前用户的设备信息
     * 历史数据 :  仅仅保存近期的3个有效设备信息(去重)
     *
     * @param currentDevice
     * @param historyDevices
     * @return
     */
    public boolean replaceEquipmentEval(String currentDevice, Set<String> historyDevices) {

        return false;
    }

    /**
     * 用户的登陆时段（习惯）存在异常（☆☆）
     * 评估数据：当前登陆时间,提取 `dayOfWeek` `hourOfDay`
     * 历史数据：留存用户所有的历史登陆信息`dayOfWeek` `hourOfDay` `次数`
     * 注意：计算用户登陆时段习惯`：对用户`dayOfWeek` 所有登陆时段进行`次数`升序排列，
     * 取2/3位置的次数作为阈值，然后更具阈值获取`dayOfWeek`中所有`hourOfDay` 次数大于`阈值`的所有时段作为习惯。
     *
     * @return
     */
    public boolean loginHabitsEval(Long currentTime, Map<String, Map<String, Integer>> historicalHabits) {

        return false;
    }


    /**
     * 登陆次数累积超过n次/每天（☆）
     * 评估数据：-
     * 历史数据：当天上一次累积评估次数
     *
     * @return
     */
    public boolean numberOfLoginEval(Integer threshold, Integer currentDayEvalCounts) {

        return false;
    }

    /**
     * 密码错误评估，成分评估（☆☆☆）
     * 评估数据：乱序明文密码
     * 历史数据：历史明文密码乱序集合Set<String>
     *
     * @return
     */
    public boolean wrongPasswordEval(String currentPassword, Set<String> historyPasswords) {

        return false;
    }

    /**
     * 采集用户的输入特征（每个控件输入时长）（☆☆☆☆）
     * 评估数据：用户当前登陆的每个控件的输入时长 [1100,1200.0,1800.0] -特征
     * 历史数据：仅仅保存用户最近10次登陆特征
     *
     * @return
     */
    public boolean InputFeatureEval(Double[] currentVector, List<Double[]> historyVectors) {

        return false;
    }


}
