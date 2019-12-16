package com.baizhi.update.impl;

import com.baizhi.entity.HistoryData;
import com.baizhi.entity.LoginSuccessData;
import com.baizhi.update.UpdateChain;
import com.baizhi.update.UpdateHandler;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * 修改历史登录习惯
 */
public class HistoricalHabitsUpdateHandler implements UpdateHandler {
    @Override
    public void invoke(HistoryData historyData, LoginSuccessData loginSuccessData, UpdateChain updateChain) {

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
        Map<String, Map<String, Integer>> historicalHabits = historyData.getHistoricalHabits();
        if (historicalHabits != null) {
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
        } else {
            HashMap<String, Map<String, Integer>> mapHashMap = new HashMap<String, Map<String, Integer>>();
            HashMap<String, Integer> map = new HashMap<>();
            map.put(hourOfDay, 1);
            mapHashMap.put(WEEKS[dayOfWeek], map);
        }

        //责任链调用
        updateChain.doChain(historyData, loginSuccessData);
    }
}
