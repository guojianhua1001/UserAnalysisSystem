package com.baizhi.util;

import com.baizhi.entity.EvaluateData;
import com.baizhi.entity.LoginSuccessData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 日志解析工具类
 */
public class LogParser {

    /*
        合法日志正则表达式:
        ^INFO\s(\d{4}-\d{2}-\d{2}\s\d{2}:\d{2}:\d{2})\s([0-9a-zA-Z]+)\s(SUCCESS|EVALUATE)\s\[([0-9a-zA-Z]+)\]\s([0-9a-z]{32})\s\"([0-9a-zA-Z\.]+)\"\s([0-9a-zA-Z]+)\s\"(\d{1,3}.\d+,\d{1,3}.\d+)\"\s\[(\d+.\d+,\d+.\d+,\d+.\d+)\]\s\"(.*?)\"$
     */
    final static Pattern LEGAL_PATTERN = Pattern.compile("^INFO\\s(\\d{4}-\\d{2}-\\d{2}\\s\\d{2}:\\d{2}:\\d{2})\\s([0-9a-zA-Z]+)\\s(SUCCESS|EVALUATE)\\s\\[([0-9a-zA-Z]+)\\]\\s([0-9a-z]{32})\\s\\\"([0-9a-zA-Z\\.]+)\\\"\\s([0-9a-zA-Z]+)\\s\\\"(\\d{1,3}.\\d+,\\d{1,3}.\\d+)\\\"\\s\\[(\\d+.\\d+,\\d+.\\d+,\\d+.\\d+)\\]\\s\\\"(.*?)\\\"$");
    /*
        登录评估日志正则表达式:
        ^INFO\s(\d{4}-\d{2}-\d{2}\s\d{2}:\d{2}:\d{2})\s([0-9a-zA-Z]+)\sEVALUATE\s\[([0-9a-zA-Z]+)\]\s([0-9a-z]{32})\s\"([0-9a-zA-Z\.]+)\"\s([0-9a-zA-Z]+)\s\"(\d{1,3}.\d+,\d{1,3}.\d+)\"\s\[(\d+.\d+,\d+.\d+,\d+.\d+)\]\s\"(.*?)\"$
    */
    final static Pattern EVAL_PATTERN = Pattern.compile("^INFO\\s(\\d{4}-\\d{2}-\\d{2}\\s\\d{2}:\\d{2}:\\d{2})\\s([0-9a-zA-Z]+)\\sEVALUATE\\s\\[([0-9a-zA-Z]+)\\]\\s([0-9a-z]{32})\\s\\\"([0-9a-zA-Z\\.]+)\\\"\\s([0-9a-zA-Z]+)\\s\\\"(\\d{1,3}.\\d+,\\d{1,3}.\\d+)\\\"\\s\\[(\\d+.\\d+,\\d+.\\d+,\\d+.\\d+)\\]\\s\\\"(.*?)\\\"$");
    /*
        登陆成功日志正则表达式:
        ^INFO\s(\d{4}-\d{2}-\d{2}\s\d{2}:\d{2}:\d{2})\s([0-9a-zA-Z]+)\sSUCCESS\s\[([0-9a-zA-Z]+)\]\s([0-9a-z]{32})\s\"([0-9a-zA-Z\.]+)\"\s([0-9a-zA-Z]+)\s\"(\d{1,3}.\d+,\d{1,3}.\d+)\"\s\[(\d+.\d+,\d+.\d+,\d+.\d+)\]\s\"(.*?)\"$
     */
    final static Pattern SUCCESS_PATTERN = Pattern.compile("^INFO\\s(\\d{4}-\\d{2}-\\d{2}\\s\\d{2}:\\d{2}:\\d{2})\\s([0-9a-zA-Z]+)\\sSUCCESS\\s\\[([0-9a-zA-Z]+)\\]\\s([0-9a-z]{32})\\s\\\"([0-9a-zA-Z\\.]+)\\\"\\s([0-9a-zA-Z]+)\\s\\\"(\\d{1,3}.\\d+,\\d{1,3}.\\d+)\\\"\\s\\[(\\d+.\\d+,\\d+.\\d+,\\d+.\\d+)\\]\\s\\\"(.*?)\\\"$");

    /**
     * 判断当前输入字符串是否合法
     *
     * @param input 输入字符串
     * @return 合法：true  非法：false
     */
    public static boolean isLegal(String input) {

        return LEGAL_PATTERN.matcher(input).matches();

    }

    /**
     * 判断当前输入字符串是否是符合评估日志格式
     *
     * @param input 输入字符串
     * @return 合法：true  非法：false
     */
    public static boolean isEval(String input) {

        return EVAL_PATTERN.matcher(input).matches();

    }

    /**
     * 判断当前输入字符串是否是符合登录成功日志格式
     *
     * @param input 输入字符串
     * @return 合法：true  非法：false
     */
    public static boolean isLoginSuccess(String input) {

        return SUCCESS_PATTERN.matcher(input).matches();

    }

    /**
     * 根据输入的一行字符串，转换为评估数据
     *
     * @param input
     * @return
     */
    public static EvaluateData parseEvaluateData(String input) throws ParseException {
        Matcher matcher = EVAL_PATTERN.matcher(input);
        matcher.matches();
        /*
            group 1.是日期       e.g. 2019-11-25 14:11:00
            group 2.是应用ID     e.g. app1
            group 3.是用户ID     e.g. Jackson01
            group 4.是登录序列   e.g. 6eaf4ac780f40f486359f3ea6934620
            group 5.是乱序密码   e.g. 167453bCA
            group 6.是地域       e.g. 2019-11-25 14:11:00
            group 7.是经纬度     e.g. 116.4,39.5
            group 8.是输入特征向量   e.g. 1000,1300.0,1000.0
            group 9.是User-Agent    e.g. Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.87 Safari/537.36"
         */
        long currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(matcher.group(1)).getTime();
        String applicationID = matcher.group(2);
        String userID = matcher.group(3);
        String loginSequence = matcher.group(4);
        String orderlessPassword = matcher.group(5);
        String region = matcher.group(6);
        double[] geoPoint = {Double.parseDouble(matcher.group(7).split(",")[0]), Double.parseDouble(matcher.group(7).split(",")[1])};
        String[] inputFeatureTokens = matcher.group(8).split(",");
        double[] inputFeature = new double[inputFeatureTokens.length];
        for (int i = 0; i < inputFeatureTokens.length; i++) {
            inputFeature[i] = Double.parseDouble(inputFeatureTokens[i]);
        }
        String userAgent = matcher.group(9);
        EvaluateData evaluateData = new EvaluateData();
        evaluateData
                .setCurrentTime(currentTime)
                .setApplicationID(applicationID)
                .setUserID(userID)
                .setLoginSequence(loginSequence)
                .setOrderlessPassword(orderlessPassword)
                .setRegion(region)
                .setGeoPoint(geoPoint)
                .setInputFeature(inputFeature)
                .setUserAgent(userAgent);
        return evaluateData;
    }

    /**
     * 根据输入的一行字符串，转换为评估数据
     *
     * @param input
     * @return
     */
    public static LoginSuccessData parseLoginSuccessData(String input) throws ParseException {
        Matcher matcher = SUCCESS_PATTERN.matcher(input);
        matcher.matches();
        /*
            group 1.是日期       e.g. 2019-11-25 14:11:00
            group 2.是应用ID     e.g. app1
            group 3.是用户ID     e.g. Jackson01
            group 4.是登录序列   e.g. 6eaf4ac780f40f486359f3ea6934620
            group 5.是乱序密码   e.g. 167453bCA
            group 6.是地域       e.g. 2019-11-25 14:11:00
            group 7.是经纬度     e.g. 116.4,39.5
            group 8.是输入特征向量   e.g. 1000,1300.0,1000.0
            group 9.是User-Agent    e.g. Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.87 Safari/537.36"
         */
        long currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(matcher.group(1)).getTime();
        String applicationID = matcher.group(2);
        String userID = matcher.group(3);
        String loginSequence = matcher.group(4);
        String orderlessPassword = matcher.group(5);
        String region = matcher.group(6);
        double[] geoPoint = {Double.parseDouble(matcher.group(7).split(",")[0]), Double.parseDouble(matcher.group(7).split(",")[1])};
        String[] inputFeatureTokens = matcher.group(8).split(",");
        double[] inputFeature = new double[inputFeatureTokens.length];
        for (int i = 0; i < inputFeatureTokens.length; i++) {
            inputFeature[i] = Double.parseDouble(inputFeatureTokens[i]);
        }
        String userAgent = matcher.group(9);
        LoginSuccessData loginSuccessData = new LoginSuccessData();
        loginSuccessData
                .setCurrentTime(currentTime)
                .setApplicationID(applicationID)
                .setUserID(userID)
                .setLoginSequence(loginSequence)
                .setOrderlessPassword(orderlessPassword)
                .setRegion(region)
                .setGeoPoint(geoPoint)
                .setInputFeature(inputFeature)
                .setUserAgent(userAgent);
        return loginSuccessData;
    }


}
