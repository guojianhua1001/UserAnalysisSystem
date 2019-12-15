package com.baizhi.util;

import com.baizhi.entity.HistoryData;
import com.baizhi.entity.LoginSuccessData;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.*;

/**
 * 修改历史数据
 */
public class UpdateHistoryData {

    /**
     * 根据登录成功日志修改历史数据
     */
    public static HistoryData getNewHistoryData(String input, HistoryData oldHistoryData) throws ParseException {

        LoginSuccessData loginSuccessData = LogParser.parseLoginSuccessData(input);
        HistoryData newHistoryData = new HistoryData();
        //修改当天的评估次数
        newHistoryData.setCurrentDayEvalCounts(oldHistoryData.getCurrentDayEvalCounts() + 1);
        //修改历史登录城市集合
        Set<String> historyCities = oldHistoryData.getHistoryCities();
        historyCities.add(loginSuccessData.getRegion());
        newHistoryData.setHistoryCities(historyCities);
        //修改最后一次登录时间
        newHistoryData.setLastTime(loginSuccessData.getCurrentTime());
        //修改最后一次登录的坐标(经纬度)
        newHistoryData.setLastPoint(loginSuccessData.getGeoPoint());
        //修改历史登录设备
        Set<String> historyDevices = oldHistoryData.getHistoryDevices();
        historyDevices.add(loginSuccessData.getUserAgent());
        newHistoryData.setHistoryDevices(historyDevices);

        //修改历史登录习惯
        //对当前日期格式的处理
        //创建日历对象
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(loginSuccessData.getCurrentTime());
        //提取dayOfWeek
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        //星期取值集合
        String[] WEEKS = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        //提取hourOfDay
        //修正日期格式
        DecimalFormat decimalFormat = new DecimalFormat("00");
        String hourOfDay = decimalFormat.format(calendar.get(Calendar.HOUR_OF_DAY));
        Map<String, Map<String, Integer>> historicalHabits = oldHistoryData.getHistoricalHabits();
        if (historicalHabits.containsKey(WEEKS[dayOfWeek])) {
            //如果该天有登录记录，就添加hourOfDay
            Map<String, Integer> hourOfDayMap = historicalHabits.get(dayOfWeek);
            if (hourOfDayMap.containsKey(hourOfDay)) {
                //如果该时段有登录记录，则数量加1
                hourOfDayMap.put(hourOfDay, hourOfDayMap.get(hourOfDay) + 1);
            } else {
                hourOfDayMap.put(hourOfDay, 1);
            }
        } else {
            //如果该天没有登录记录，还需要添加dayOfWeek
            HashMap<String, Integer> hourOfDayMap = new HashMap<>();
            hourOfDayMap.put(hourOfDay, 1);
            historicalHabits.put(WEEKS[dayOfWeek], hourOfDayMap);
        }
        newHistoryData.setHistoricalHabits(historicalHabits);

        //修改历史乱序密码集合
        Set<String> historyPasswords = oldHistoryData.getHistoryPasswords();
        historyPasswords.add(loginSuccessData.getOrderlessPassword());
        newHistoryData.setHistoryPasswords(historyPasswords);

        //修改用户历史特征集合
        List<double[]> historyVectors = oldHistoryData.getHistoryVectors();
        historyVectors.remove(0);
        historyVectors.add(loginSuccessData.getInputFeature());
        newHistoryData.setHistoryVectors(historyVectors);

        return newHistoryData;
    }

}
