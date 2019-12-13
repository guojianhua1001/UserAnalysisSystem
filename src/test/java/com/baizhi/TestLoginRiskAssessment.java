package com.baizhi;

import com.baizhi.risk.LoginRiskAssessment;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * 测试登录风险
 */
public class TestLoginRiskAssessment {

    //风险评估对象
    private LoginRiskAssessment loginRiskAssessment = new LoginRiskAssessment();

    /**
     * 异地登录评估测试
     */
    @Test
    public void testOffSiteLoginEval() {
        HashSet<String> historyCities = new HashSet<>();
        historyCities.add("beijing");
        historyCities.add("zhengzhou");
        historyCities.add("hebi");
        String currentCity = "beijing";
        System.out.println(loginRiskAssessment.offSiteLoginEval(currentCity, historyCities));
        System.out.println(loginRiskAssessment.offSiteLoginEval("kaifeng", historyCities));
    }

    /**
     * 登录的位移速度评估测试
     */
    @Test
    public void testSpeedOfDisplacementEval() throws ParseException {
        //北京的经纬度坐标
        Double[] beijing = {116.30, 40.20};
        //郑州的经纬度坐标
        Double[] zhengzhou = {113.35, 34.50};
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long currentTime = dateFormat.parse("2019-12-13 05:00:00").getTime();
        long lastTime = dateFormat.parse("2019-12-13 04:00:00").getTime();
        System.out.println(loginRiskAssessment.speedOfDisplacementEval(currentTime, zhengzhou, lastTime, beijing));
    }

    /**
     * 测试用户的登录习惯
     */
    @Test
    public void testLoginHabitsEval() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long currentTime = dateFormat.parse("2019-12-13 14:05:00").getTime();
        HashMap<String, Integer> hourOfDayMap = new HashMap<>();
        HashMap<String, Map<String, Integer>> dayOfWeekMap = new HashMap<>();
        hourOfDayMap.put("05", 2);
        hourOfDayMap.put("07", 5);
        hourOfDayMap.put("08", 4);
        hourOfDayMap.put("09", 5);
        hourOfDayMap.put("13", 10);
        hourOfDayMap.put("14", 2);
        hourOfDayMap.put("20", 20);
        dayOfWeekMap.put("星期五", hourOfDayMap);
        System.out.println(loginRiskAssessment.loginHabitsEval(currentTime, dayOfWeekMap, 10));
    }

    /**
     * 密码错误成分评估
     */
    @Test
    public void testWrongPasswordEval() {
        HashSet<String> historyPassword = new HashSet<>();
        historyPassword.add("123456");
        historyPassword.add("345678");
        historyPassword.add("186000");
        String currentPassword = "134526";
        System.out.println(loginRiskAssessment.wrongPasswordEval(currentPassword, historyPassword));


    }

}
