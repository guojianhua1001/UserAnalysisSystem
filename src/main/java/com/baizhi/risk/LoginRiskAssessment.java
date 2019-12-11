package com.baizhi.risk;

import java.util.*;

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
     * @param currentCity   当前所在城市
     * @param historyCities 历史登录城市集合
     * @return 有风险返回true, 无风险返回false
     */
    public boolean offSiteLoginEval(String currentCity, Set<String> historyCities) {

        //若历史记录的城市中没有当前登录过的城市，则返回false
        //首先应判断该用户是否是第一次登录(若用户为第一次登录，则历史城市为null，此时也应返回true
        if (historyCities == null) {
            //为null说明用户是第一次登录本应用
            return false;
        } else {
            //说明用户不是第一次登录本应用
            if (historyCities.contains(currentCity)) {
                return false;
            } else {
                return true;
            }
        }
    }

    /**
     * 登陆的位移速度(距离/时间)  (☆☆☆）
     * 评估数据：当前登陆时间/当前登陆城市的地理坐标
     * 历史数据：最近一次登陆时间/最近一次登陆地理坐标
     *
     * @param currentTime 当前时间
     * @param currentPoint 当前坐标
     * @param lastTime  上一次登录时间
     * @param lastPoint  上一次登录坐标
     * @return 有风险返回true, 无风险返回false
     */
    public boolean speedOfDisplacementEval(Long currentTime, Double[] currentPoint, Long lastTime, Double[] lastPoint) {

        //若历史记录的城市中没有当前登录过的城市，则返回false
        if (lastPoint == null) {
            return false;
        } else {
            //计算时间间隔
            long timeDifference = (currentTime - lastTime) / 1000 / 3600; //单位：h

            /*
                下面是使用高精度计算方法计算两个城市之间的距离
                地球本身是个不规则的球体，这里将其看着一个规则球体，半径取平均值：6371.393千米
                计算公式采用“球面距离公式”：S=R·arccos[cosβ1cosβ2cos(α1-α2)+sinβ1sinβ2]
                 1. cosβ1cosβ2cos(α1-α2)+sinβ1sinβ2  ——求∠AOB的余弦值
                 2. arccos[∠AOB的余弦值]——求∠AOB的反余弦值，值域为[0,π]，本质是 ∠AOB角度 / 180° * π；
                 3. R·∠AOB的反余弦值，等价于 R·∠AOB角度 / 180° * π
                注意：Java中的Math类提供的sin和con方法的参数是弧度，而不是角度
             */
            // 公式中“cosβ1cosβ2cos（α1-α2）+sinβ1sinβ2”的部分，得到∠AOB的cos值
            double earth_radius = 6371.393;//单位：km
            double cos = Math.cos(currentPoint[1]) * Math.cos(currentPoint[1]) * Math.cos(currentPoint[0] - currentPoint[0])
                    + Math.sin(currentPoint[1]) * Math.sin(currentPoint[1]);
            double acos = Math.acos(cos); // 反余弦值
            double v = earth_radius * acos / timeDifference;  //单位： km/h

            //如果速度超过飞机，则判定有风险
            if (v > 900) {
                return true;
            }
            return false;
        }
    }


    /**
     * 更换设备存在风险（☆）
     * 评估数据：当前用户的设备信息
     * 历史数据 :  仅仅保存近期的3个有效设备信息(去重)
     * @param currentDevice 当前设备
     * @param historyDevices 历史登录设备
     * @return 有风险返回true, 无风险返回false
     */
    public boolean replaceEquipmentEval(String currentDevice, Set<String> historyDevices) {

        //若历史记录的设备中没有当前登录过的设备，则返回false
        //首先应判断该用户是否是第一次登录(若用户为第一次登录，则历史设备为null，此时也应返回true
        if (historyDevices == null) {
            //为null说明用户是第一次登录本应用
            return false;
        } else {
            //说明用户不是第一次登录本应用
            if (historyDevices.contains(currentDevice)) {
                return false;
            } else {
                return true;
            }
        }
    }

    /**
     * 用户的登陆时段（习惯）存在异常（☆☆）
     * 评估数据：当前登陆时间,提取 dayOfWeek hourOfDay
     * 历史数据：留存用户所有的历史登陆信息dayOfWeek hourOfDay 次数
     * 注意：计算用户登陆时段习惯`：对用户dayOfWeek 所有登陆时段进行`次数`升序排列，
     * 取2/3位置的次数作为阈值，然后更具阈值获取dayOfWeek中所有hourOfDay 次数大于阈值的所有时段作为习惯。
     *
     * @param currentTime 当前时间
     * @param historicalHabits 历史登录习惯
     * @return 有风险返回true, 无风险返回false
     */
    public boolean loginHabitsEval(Long currentTime, Map<String, Map<String, Integer>> historicalHabits) {

        //若历史记录中没有该用户的习惯，则返回false
        if (historicalHabits == null) {
            return false;
        } else {
            /*
                由当前的登录时间提取出dayOfWeek和hourOfDay
             */
            Date loginTime = new Date(currentTime);
            String loginDay = Integer.toString(loginTime.getDay());  //dayOfWeek
            int loginHour = loginTime.getHours();  //hourOfDay
            String period = null;//所属时段
            //获取该时间所属的时段
            if (loginHour >= 5 && loginHour < 9) {
                period = "早上";
            } else if (loginHour >= 9 && loginHour < 12) {
                period = "上午";
            } else if (loginHour >= 12 && loginHour < 14) {
                period = "中午";
            } else if (loginHour >= 14 && loginHour < 18) {
                period = "下午";
            } else if (loginHour >= 18 && loginHour <= 22) {
                period = "晚上";
            } else if (loginHour >= 22 || loginHour < 2) {
                period = "夜里";
            } else if (loginHour >= 2 && loginHour < 5) {
                period = "深夜";
            }

            /*
                求出每周的这天登录时段和次数的阈值
             */
            Map<String, Integer> habitsOfDay = historicalHabits.get(loginDay);
            Map.Entry<String, Integer>[] habitsOfDayArray = new Map.Entry[habitsOfDay.size()];
            int i = 0;
            //将每天的 (阶段， 登录次数) 按照登录次数从小到大的顺序排序(利用数组)
            for (Map.Entry<String, Integer> habitsOfDayEntry : habitsOfDay.entrySet()) {
                habitsOfDayArray[i] = habitsOfDayEntry;
                i++;
            }
            Arrays.sort(habitsOfDayArray, new Comparator<Map.Entry<String, Integer>>() {
                public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                    return o1.getValue() - o2.getValue();
                }
            });
            //求出2/3位置处的阈值
            int thresholdOfIndex = habitsOfDayArray.length / 3;

            for (int j = habitsOfDayArray.length; j >= thresholdOfIndex; j--) {
                //将当前所属时段与historicalHabits前 2/3 相匹配
                if (habitsOfDayArray[j].getKey().equals(period)) return true;
            }

            return false;
        }
    }


    /**
     * 登陆次数累积超过n次/每天（☆）
     * 评估数据：-
     * 历史数据：当天上一次累积评估次数
     * @param threshold 阈值
     * @param currentDayEvalCounts 当天的评估次数
     * @return 有风险返回true, 无风险返回false
     */
    public boolean numberOfLoginEval(Integer threshold, Integer currentDayEvalCounts) {

        //若历史评估次数>阈值，则返回true
        if (currentDayEvalCounts >= threshold) {
            return true;
        }
        return false;
    }

    /**
     * 密码错误评估，成分评估（☆☆☆）
     * 评估数据：乱序明文密码
     * 历史数据：历史明文密码乱序集合Set<String>
     * @param currentPassword 当前密码
     * @param historyPasswords 历史密码集合
     * @return 有风险返回true, 无风险返回false
     */
    public boolean wrongPasswordEval(String currentPassword, Set<String> historyPasswords) {

         /*
            将currentPassword拆分为单个字符，并存入Map集合，并统计出现的数量
         */
        Map<Character, Integer> currentPass = new HashMap<Character, Integer>();
        {
            char[] chars = currentPassword.toCharArray();
            for (char aChar : chars) {
                if (currentPass.containsKey(aChar)) {
                    currentPass.put(aChar, currentPass.get(aChar) + 1);
                } else {
                    currentPass.put(aChar, 1);
                }
            }
        }

        /*
            将历史明文密码乱序集合封装为List<Map<Character, Integer>>,即(字符，数量)
            并且进行比较
        */
        for (String historyPassword : historyPasswords) {
            Map<Character, Integer> historyPass = new HashMap<Character, Integer>();
            //将各个历史密码拆分为一个一个字符
            char[] chars = historyPassword.toCharArray();
            //存入Map，并统计出现次数
            for (char aChar : chars) {
                if (historyPass.containsKey(aChar)) {
                    historyPass.put(aChar, historyPass.get(aChar) + 1);
                } else {
                    historyPass.put(aChar, 1);
                }
            }

            /*
                将currentPass和historyPasses进行对比
            */
            //定义碰撞成功的长度
            int hitSize = 0;
            //遍历currentPassword的字符
            for (Character character : currentPass.keySet()) {
                if (historyPass.containsKey(character)) {
                    //如果包含相同字符，则进行深度比较(出现数量比较)
                    hitSize += currentPass.get(character) >= historyPass.get(character) ? historyPass.get(character) : currentPass.get(character);
                }
            }
            //判定
            if (hitSize >= historyPassword.length() - 2)
                return false;
        }
        return true;
    }

    /**
     * 采集用户的输入特征（每个控件输入时长）（☆☆☆☆）
     * 评估数据：用户当前登陆的每个控件的输入时长 [1100,1200.0,1800.0] -特征
     * 历史数据：仅仅保存用户最近10次登陆特征
     * @param currentVector 当前特征向量
     * @param historyVectors 历史特征集合
     * @return 风险返回true, 无风险返回false
     */
    public boolean InputFeatureEval(Double[] currentVector, List<Double[]> historyVectors) {

        //用户第一次登录
        if (historyVectors == null) {
            return false;
        } else {
            //如果如果用户登录次数少于10次，则返回false
            if (historyVectors.size() < 10) {
                return false;
            } else {
                //计算出输入特征的“球”的方程 (x-a)2 + (y-b)2 + (z-c)2 = r2
                double sumA = 0;
                double sumB = 0;
                double sumC = 0;
                double maxA = 0;
                double maxB = 0;
                double maxC = 0;
                for (Double[] historyVector : historyVectors) {
                    sumA += historyVector[0];
                    sumB += historyVector[1];
                    sumC += historyVector[2];
                    maxA = max(maxA, historyVector[0]);
                    maxB = max(maxB, historyVector[1]);
                    maxC = max(maxC, historyVector[2]);
                }
                double A = sumA / historyVectors.size();
                double B = sumB / historyVectors.size();
                double C = sumC / historyVectors.size();
                double dA = (maxA - A) / A;
                double dB = (maxB - B) / B;
                double dC = (maxC - C) / C;
                double R;
                //确定半径并折算到A的上
                if (dA > dB) {
                    if (dA > dC) R = (maxA - A);
                    else R = (maxC - C) * A / C;
                } else {
                    if (dB > dC) R = (maxB - B) * A / B;
                    else R = (maxC - C) * A / C;
                }
                //根据比例调整“球”的形状(折算到A上)
                double threshold = (currentVector[0] - A) * (currentVector[0] - A) + (currentVector[1] - B) * (currentVector[1] - B) * A * A / B / B + (currentVector[2] - C) * (currentVector[2] - C) * A * A / C / C - R * R;
                if (threshold < 0) return true;
                return false;
            }
        }
    }


    /**
     * 求最大值
     *
     * @param d double类型数值
     * @return 最大值
     */
    public double max(double... d) {
        if (d != null) {
            Arrays.sort(d);
            return d[0];
        }
        return 0;
    }


}
