package com.baizhi;

import com.baizhi.util.LogParser;
import org.junit.Test;

import java.text.ParseException;

/**
 * 测试日志解析工具类
 */
public class TestLogParser {

    /**
     * 将日志转换为登录评估数据
     */
    @Test
    public void testParseEvaluateData() throws ParseException {
        //准备数据
        String evalLogData = "INFO 2019-11-25 14:11:00 app1 EVALUATE [zhangsan01] 6ebaf4ac780f40f486359f3ea6934620 \"123456bCA\" beijing \"116.4,39.5\" [1000,1300.0,1000.0] \"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.87 Safari/537.36\"";
        System.out.println("LogParser.isLegal() = " + LogParser.isLegal(evalLogData));
        if (LogParser.isLegal(evalLogData)) {
            if (LogParser.isEval(evalLogData)) {
                System.out.println(LogParser.parseEvaluateData(evalLogData));
            }
        } else {
            System.out.println("不是评估数据");
        }
    }

    /**
     * 将日志转换为登录评估数据
     */
    @Test
    public void testParseLoginSuccessData() throws ParseException {
        //准备数据
        String loginSuccessData = "INFO 2019-11-25 14:11:00 app1 SUCCESS [zhangsan01] 6ebaf4ac780f40f486359f3ea6934620 \"123456bCA\" beijing \"116.4,39.5\" [1000,1300.0,1000.0] \"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.87 Safari/537.36\"";
        System.out.println("LogParser.isLegal() = " + LogParser.isLegal(loginSuccessData));
        if (LogParser.isLoginSuccess(loginSuccessData)) {
            if (LogParser.isLoginSuccess(loginSuccessData)) {
                System.out.println(LogParser.parseLoginSuccessData(loginSuccessData));
            }
        } else {
            System.out.println("不是登录成功数据");
        }
    }

}
