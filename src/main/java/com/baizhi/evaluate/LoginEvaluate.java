package com.baizhi.evaluate;

import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Math.*;

/**
 * 登录风险评估
 * 辅助业务系统完成盗号或者账号被窃等风险检测。
 */
public class LoginEvaluate {

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
     * @param lastPoint    上一次登录坐标(经纬度)
     * @return 有风险返回true, 无风险返回false
     */
    public boolean speedOfDisplacementEval(Long currentTime, double[] currentPoint, Long lastTime, double[] lastPoint) {

        //获取位置坐标并转换为正确的格式
        double ALon = currentPoint[0]; //A位置的经度，单位：°
        double ALat = currentPoint[1]; //A位置的纬度，单位：°
        double BLon = lastPoint[0]; //A位置的经度，单位：°
        double BLat = lastPoint[1]; //A位置的纬度，单位：°
        double ALonRad = toRadians(ALon); //A位置的经度(弧度)
        double ALatRad = toRadians(ALat); //A位置的纬度(弧度)
        double BLonRad = toRadians(BLon); //B位置的经度(弧度)
        double BLatRad = toRadians(BLat); //B位置的纬度(弧度)
        //将地球抽象成一个球形，半径约为6371Km,根据   弧长 = 半径 * 弧度
        //弧度 = arccos[cosβ1cosβ2cos(α1-α2) + sinβ1sinβ2] (α为经度， β为纬度)
        //实体类全部封装为
        double rad = acos(cos(ALatRad) * cos(BLatRad) * cos(ALonRad - BLonRad) + sin(ALatRad) * sin(BLatRad));
        double R = 6371; //地球半径，单位：km
        //求出位移
        double radian = rad * R;

        //求出时间
        double time = (currentTime - lastTime) / 1000.0 / 3600; //单位：h

        //求出速度
        double speed = radian / time; //单位：km/h
        //判定
        if (speed > 600) {
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
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        //星期取值集合
        String[] WEEKS = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
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
        if (!historicalHabits.containsKey(WEEKS[dayOfWeek]))
            return true;

        /*
            获取当天的登录记录，并进行分析
         */
        Map<String, Integer> dayHabits = historicalHabits.get(WEEKS[dayOfWeek]);
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
     * @param currentPassword     当前密码
     * @param historyPasswords    历史密码集合
     * @param similarityThreshold 相似度阈值
     * @return 有风险返回true, 无风险返回false
     */
    public boolean wrongPasswordEval(String currentPassword, Set<String> historyPasswords, double similarityThreshold) {

        /*
            获取词袋 --> 确定维度
         */
        HashSet<Character> wordsBag = new HashSet<>();
        for (String historyPassword : historyPasswords) {
            char[] charArray = historyPassword.toCharArray();
            for (char c : charArray) {
                wordsBag.add(c);
            }
        }
        char[] currentPassCharArray = currentPassword.toCharArray();
        for (char c : currentPassCharArray) {
            wordsBag.add(c);
        }
        //将词袋以线性表的方式保存
        List<Character> seqWordsBag = new ArrayList<>(wordsBag);

        /*
            获取currentPassword的特征向量
         */
        //进行currentPassword的单词统计
        HashMap<Character, Integer> currentPassCountMap = new HashMap<>();
        for (char c : currentPassCharArray) {
            if (currentPassCountMap.containsKey(c)) {
                currentPassCountMap.put(c, currentPassCountMap.get(c) + 1);
            }
            currentPassCountMap.put(c, 1);
        }
        //遍历词袋，为currentPassword的特征向量赋值
        int[] currentPassVector = new int[seqWordsBag.size()];
        for (int i = 0; i < seqWordsBag.size(); i++) {
            int count = 0;
            if (currentPassCountMap.containsKey(seqWordsBag.get(i))) {
                count = currentPassCountMap.get(seqWordsBag.get(i));
            }
            currentPassVector[i] = count;
        }

        /*
            遍历历史密码集，生成每个密码的特征向量，并与当前密码的特征向量进行比较，相似度低于阈值则有风险
         */
        for (String historyPassword : historyPasswords) {
            //为每一个特征向量赋值
            char[] historyCharArray = historyPassword.toCharArray();
            HashMap<Character, Integer> historyPassCountMap = new HashMap<>();
            for (char c : historyCharArray) {
                if (historyPassCountMap.containsKey(c)) {
                    historyPassCountMap.put(c, historyPassCountMap.get(c) + 1);
                }
                historyPassCountMap.put(c, 1);
            }
            //遍历词袋，为currentPassword的特征向量赋值
            int[] historyPassVector = new int[seqWordsBag.size()];
            for (int i = 0; i < seqWordsBag.size(); i++) {
                int count = 0;
                if (historyPassCountMap.containsKey(seqWordsBag.get(i))) {
                    count = historyPassCountMap.get(seqWordsBag.get(i));
                }
                historyPassVector[i] = count;
            }

            /*
                进行余弦相似度进行密码的比较
             */
            //分子计算
            double molecule = 0;  //向量的数量积
            for (int i = 0; i < seqWordsBag.size(); i++) {
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
            double similarity = molecule / denominator;
            System.out.println(similarity);
            //根据相似度进行判定，有相似的密码就返回false
            if (similarity >= similarityThreshold)
                return false;
        }
        //若历史密码中没有相似的密码，则判定有风险
        return true;
    }

    /**
     * 采集用户的输入特征（每个控件输入时长）（☆☆☆☆）
     * 评估数据：用户当前登陆的每个控件的输入时长 [1100,1200.0,1800.0] -特征
     * 历史数据：仅仅保存用户最近10次登陆特征
     *
     * @param currentVector  当前特征向量
     * @param historyVectors 历史用户特征集合
     * @return 风险返回true, 无风险返回false
     */
    public boolean inputFeatureEval(double[] currentVector, List<double[]> historyVectors) {

        //如果用户的登录次数少于2次，则无法进行判定，直接返回false
        if (historyVectors.size() < 2) {
            return false;
        }

        /*
            求历史输入特征的平均值作为平均输入特征
         */
        double[] averageVector = new double[currentVector.length];
        //求和
        for (double[] historyVector : historyVectors) {
            for (int i = 0; i < averageVector.length; i++) {
                averageVector[i] += historyVector[i];
            }
        }
        //求平均值
        for (int i = 0; i < averageVector.length; i++) {
            averageVector[i] = averageVector[i] / historyVectors.size();
        }
        
        /*
            计算历史输入特征向量中，各点之间的距离（输入时间差异）
         */
        double[] fluctuations = new double[historyVectors.size() * (historyVectors.size()) / 2];
        int k = 0;
        for (int i = 0; i < historyVectors.size(); i++) {
            for (int j = i + 1; j < historyVectors.size(); j++) {
                //第i个历史向量和第i+1, i+2, ... 之间的距离
                fluctuations[k] = distance(historyVectors.get(i), historyVectors.get(j));
                k++;
            }
        }

        /*
            根据输入时间差异向量，取1/3位置处的值作为判定阈值
         */
        Arrays.sort(fluctuations);
        double threshold = fluctuations[fluctuations.length / 3];

        //计算 averageVector 和 currentVector 之间的距离，如果大于阈值，则判定有风险
        if (distance(averageVector, currentVector) > threshold) {
            return true;
        }

        return false;
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

    /**
     * 计算两点之间的距离
     *
     * @param v1 向量1
     * @param v2 向量2
     * @return 两点之间的距离
     */
    public double distance(double[] v1, double[] v2) {
        //如果维度不同则抛出异常
        if (v1.length != v2.length) {
            throw new RuntimeException("数组长度不一致");
        }

        //求坐标差值的平方和
        double squareSum = 0;
        for (int i = 0; i < v1.length; i++) {
            squareSum += (v1[i] - v2[i]) * (v1[i] - v2[i]);
        }

        return sqrt(squareSum);
    }

}
