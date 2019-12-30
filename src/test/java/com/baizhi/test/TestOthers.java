package com.baizhi.test;

import com.baizhi.entity.EvaluateData;
import org.junit.Test;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;

import static java.lang.Math.*;

/**
 * 系统基础测试
 */
public class TestOthers {

    /**
     * 测试Date类
     */
    @Test
    public void testDate() {
        //System.out.println(new Date().getHours());
        System.out.println(new Date().getDay());
    }

    /**
     * 测试Calender类
     */
    @Test
    public void testCalender() {
        //创建日历对象
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        //提取dayOfWeek
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        //星期取值集合
        String[] WEEKS = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        System.out.println("dayOfWeek:\t" + WEEKS[dayOfWeek - 1]);
        //提取hourOfDay
        //修正日期格式
        DecimalFormat decimalFormat = new DecimalFormat("00");
        String hourOfDay = decimalFormat.format(calendar.get(Calendar.HOUR_OF_DAY));
        System.out.println("hourOfDay:\t" + hourOfDay);
    }

    /**
     * 测试数学函数
     */
    @Test
    public void testMath() {
        System.out.println(sin(toRadians(30)));
        System.out.println(toDegrees(asin(0.5)));
    }

    @Test
    public void testLombok() {
        EvaluateData evaluateData = new EvaluateData();
        evaluateData.setUserID("1001");
        System.out.println("evaluateData.getUserID() = " + evaluateData.getUserID());
        System.out.println(evaluateData);
    }

    @Test
    public void testLength() {
        System.out.println("INFO 2019-12-28 13:53:34 APP1 EVALUATE [USER1005] c4d1a56a9c734966891c2f481ccd7a94 \"USER1006\" beijing \"40.1,116.5\" [4623.3,4432.3,3780.3] \"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.140 Safari/537.36 Edge/18.17763\"".length());
    }

}
