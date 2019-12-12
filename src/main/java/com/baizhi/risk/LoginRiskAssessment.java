package com.baizhi.risk;

import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Math.*;

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
        if (historyCities == null || historyCities.size() == 0) {
            //为null说明用户是第一次登录本应用
            return false;
        }
        //说明用户不是第一次登录本应用
        if (historyCities.contains(currentCity)) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 登陆的位移速度(距离/时间)  (☆☆☆）
     * 评估数据：当前登陆时间/当前登陆城市的地理坐标
     * 历史数据：最近一次登陆时间/最近一次登陆地理坐标
     *
     * @param currentTime  当前时间
     * @param currentPoint 当前坐标
     * @param lastTime     上一次登录时间
     * @param lastPoint    上一次登录坐标
     * @return 有风险返回true, 无风险返回false
     */
    public boolean speedOfDisplacementEval(Long currentTime, Double[] currentPoint, Long lastTime, Double[] lastPoint) {

        //获取位置坐标并转换为正确的格式
        double ALon = currentPoint[0]; //A位置的经度，单位：°
        double ALat = currentPoint[1]; //A位置的纬度，单位：°
        double BLon = currentPoint[0]; //A位置的经度，单位：°
        double BLat = currentPoint[1]; //A位置的纬度，单位：°
        double ALonRad = toRadians(ALon); //A位置的经度(弧度)
        double ALatRad = toRadians(ALat); //A位置的纬度(弧度)
        double BLonRad = toRadians(BLon); //B位置的经度(弧度)
        double BLatRad = toRadians(BLat); //B位置的纬度(弧度)

        //将地球抽象成一个球形，半径约为6371Km,根据   弧长 = 半径 * 弧度
        //弧度 = arccos[cosβ1cosβ2cos(α1-α2) + sinβ1sinβ2] (α为经度， β为纬度)
        double rad = acos(cos(ALatRad) * cos(BLatRad) * cos(ALonRad - BLonRad) + sin(ALatRad) * sin(BLatRad));
        double R = 6371; //地球半径，单位：km
        //求出位移
        double radian = rad * R;

        //求出时间
        double time = (currentTime - lastTime) / 1000 / 3600; //单位：h

        //求出速度
        double speed = radian / time;

        //判定
        if (speed > 800) {
            return true;
        }
        return false;
    }


    /**
     * 更换设备存在风险（☆）
     * 评估数据：当前用户的设备信息
     * 历史数据 :  仅仅保存近期的3个有效设备信息(去重)
     *
     * @param currentDevice  当前设备
     * @param historyDevices 历史登录设备
     * @return 有风险返回true, 无风险返回false
     */
    public boolean replaceEquipmentEval(String currentDevice, Set<String> historyDevices) {

        //若历史记录的设备中没有当前登录过的设备，则返回false
        //首先应判断该用户是否是第一次登录(若用户为第一次登录，则历史设备为null，此时也应返回true
        if (historyDevices == null || historyDevices.size() == 0) {
            //为null说明用户是第一次登录本应用
            return false;
        }
        //说明用户不是第一次登录本应用
        if (historyDevices.contains(currentDevice)) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 用户的登陆时段（习惯）存在异常（☆☆）
     * 评估数据：当前登陆时间,提取 dayOfWeek hourOfDay
     * 历史数据：留存用户所有的历史登陆信息dayOfWeek hourOfDay 次数
     * 注意：计算用户登陆时段习惯`：对用户dayOfWeek 所有登陆时段进行`次数`升序排列，
     * 取2/3位置的次数作为阈值，然后更具阈值获取dayOfWeek中所有hourOfDay 次数大于阈值的所有时段作为习惯。
     *
     * @param currentTime      当前时间
     * @param historicalHabits 历史登录习惯
     * @param threshold        登录习惯达成(登录次数)阈值
     * @return 有风险返回true, 无风险返回false
     */
    public boolean loginHabitsEval(Long currentTime, Map<String, Map<String, Integer>> historicalHabits, Integer threshold) {


        if (historicalHabits == null || historicalHabits.size() == 0) {
            //若历史习惯没有，则直接返回false
            return false;
        }
        /*
            对当前日期格式的处理
         */
        //创建日历对象
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(currentTime);
        //提取dayOfWeek
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        //星期取值集合
        String[] WEEKS = {"星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日"};
        //提取hourOfDay
        //修正日期格式
        DecimalFormat decimalFormat = new DecimalFormat("00");
        String hourOfDay = decimalFormat.format(calendar.get(Calendar.HOUR_OF_DAY));

        /*
            登录习惯判定
         */
        //计算总记录数
        Integer historyCounts = historicalHabits
                .entrySet()
                .stream()
                .map(m -> m.getValue())
                .map(m -> m.entrySet())
                .map(entry -> entry.stream())
                .map(entry -> entry
                        .map(en -> en.getValue())
                        .reduce((v1, v2) -> v1 + v2)
                        .get())
                .reduce((v1, v2) -> v1 + v2)
                .get();

        //若用户历史记录里没有生成该习惯或者习惯的记录数少于规定阈值，则返回false
        if (historyCounts < threshold)
            return false;
        //若用户在该天无登录记录，则返回true
        if (!historicalHabits.containsKey(dayOfWeek))
            return true;

        /*
            获取当天的登录记录，并进行分析
         */
        Map<String, Integer> dayHabits = historicalHabits.get(dayOfWeek);
        //若历史记录该天该时段无登录记录，则返回true
        if (!dayHabits.containsKey(hourOfDay))
            return true;
        //对有登录记录的时段进行判定，根据 2/3位置处的阈值 判定是否有异常
        List<Integer> habitsCountsOfDay = dayHabits.entrySet()
                .stream()
                .map(entry -> entry.getValue())
                .sorted()
                .collect(Collectors.toList());
        Integer habitsSize = habitsCountsOfDay.size();
        //获取每个时段登录次数的阈值
        Integer thresholdCount = habitsCountsOfDay.get(habitsSize * 2 / 3);
        //根据阈值获取新的评估数据
        List<String> evalDate = dayHabits.entrySet()
                .stream()
                .filter(entry -> entry.getValue() >= thresholdCount)
                .map(entry -> entry.getKey())
                .collect(Collectors.toList());
        //根据评估数据评判
        if (evalDate.contains(hourOfDay)) {
            return false;
        }
        return true;

    }


    /**
     * 登陆次数累积超过n次/每天（☆）
     * 评估数据：-
     * 历史数据：当天上一次累积评估次数
     *
     * @param threshold            阈值
     * @param currentDayEvalCounts 当天的评估次数
     * @return 有风险返回true, 无风险返回false
     */
    public boolean numberOfLoginEval(Integer threshold, Integer currentDayEvalCounts) {

        //如果当天的评估次数为0(或没有记录），返回false
        if (currentDayEvalCounts == null || currentDayEvalCounts == 0) {
            return false;
        }
        return currentDayEvalCounts > threshold;
    }

    /**
     * 密码错误评估，成分评估（☆☆☆）
     * 评估数据：乱序明文密码
     * 历史数据：历史明文密码乱序集合Set<String>
     *
     * @param currentPassword  当前密码
     * @param historyPasswords 历史密码集合
     * @return 有风险返回true, 无风险返回false
     */
    public boolean wrongPasswordEval(String currentPassword, Set<String> historyPasswords) {
        //提取密码的特征
        //维度：[0, 1, 2, 3, 4, 5, 6, 7, 8, 9]
        int[] currentPassVector = new int[10];

        /*
            将currentPassword进行字符维度统计
         */
        char[] currentPassCharArr = currentPassword.toCharArray();
        HashMap<Character, Integer> currentPassCount = new HashMap<>();
        for (char c : currentPassCharArr) {
            if (currentPassCount.containsKey(c)) {
                currentPassCount.put(c, currentPassCount.get(c) + 1);
            } else {
                currentPassCount.put(c, 1);
            }
        }
        //将单词统计的结果存入数组
        for (Map.Entry<Character, Integer> entry : currentPassCount.entrySet()) {
            currentPassVector[entry.getKey()] = entry.getValue();
        }

        /*
            将historyPasswords进行字符维度统计,并和当前的密码进行比较，若维度大于0.95，则认为无异常
         */
        HashMap<Character, Integer> historyPassCount = new HashMap<>();
        for (String historyPassword : historyPasswords) {
            //密码字符统计
            char[] historyPassCharArr = historyPassword.toCharArray();
            for (char c : historyPassCharArr) {
                if (currentPassCount.containsKey(c)) {
                    currentPassCount.put(c, currentPassCount.get(c) + 1);
                } else {
                    currentPassCount.put(c, 1);
                }
            }
            //构造维度
            int[] historyPassVector = new int[10];
            for (Map.Entry<Character, Integer> entry : historyPassCount.entrySet()) {
                historyPassVector[entry.getKey()] = entry.getValue();
            }
            /*
                利用余弦相似度比较密码成分。
             */
            //分子计算
            double molecule = 0;  //向量的数量积
            for (int i = 0; i < historyPassVector.length; i++) {
                molecule += historyPassVector[i] * currentPassVector[i];
            }
            //分母计算
            double denominator = 0; //向量的模乘积
            double hMode2 = 0; //历史密码特征向量的模
            double cMode2 = 0; //当前密码特征向量的模
            for (int i : historyPassVector) {
                hMode2 += i * i;
            }
            for (int i : currentPassVector) {
                cMode2 += i * i;
            }
            denominator = sqrt(hMode2) * sqrt(cMode2);
            double similar = molecule / denominator;

            //判定
            if (similar > 0.95) return false;

            historyPassCount.clear();
        }

        //若历史密码无匹配，就判定有风险。
        return true;
    }

    /**
     * 采集用户的输入特征（每个控件输入时长）（☆☆☆☆）
     * 评估数据：用户当前登陆的每个控件的输入时长 [1100,1200.0,1800.0] -特征
     * 历史数据：仅仅保存用户最近10次登陆特征
     *
     * @param currentVector  当前特征向量
     * @param historyVectors 历史特征集合
     * @return 风险返回true, 无风险返回false
     */
    public boolean InputFeatureEval(Double[] currentVector, List<Double[]> historyVectors) {

        //用户第一次登录
        if (historyVectors == null) {
            return false;
        }
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
