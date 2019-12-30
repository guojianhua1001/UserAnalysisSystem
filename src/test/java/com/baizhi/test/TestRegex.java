package com.baizhi.test;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 测试正则表达式
 */
public class TestRegex {

    /**
     * 测试日志通过正则表达式转换的结果
     * 给定日志的格式：日志级别 |日期 | 应用名 | 日志类型 | 用户ID | 登录序列 | 乱序密码 | 区域 | 经纬度 | 输入特征 | User-Agent
     * 例如：“INFO 2019-11-25 14:11:00 app1 EVALUATE [zhangsan01] 6ebaf4ac780f40f486359f3ea6934620 "123456bCA" beijing "116.4,39.5" [1000,1300.0,1000.0] "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.87 Safari/537.36"”
     * 相应正则表达式：^(INFO)\s(\d{4}-\d{2}-\d{2}\s\d{2}:\d{2}:\d{2})\s([0-9a-zA-Z]+)\sEVALUATE\s\[([0-9a-zA-Z]+)\]\s([0-9a-z]{32})\s\"([0-9a-zA-Z\.]+)\"\s([0-9a-zA-Z]+)\s\"(\d{1,3}.\d+,\d{1,3}.\d+)\"\s\[(\d+.\d+,\d+.\d+,\d+.\d+)\]\s\"(.*?)\"$
     */
    @Test
    public void testLogParser() {
        Pattern EVAL_PATTERN = Pattern.compile("^(INFO)\\s(\\d{4}-\\d{2}-\\d{2}\\s\\d{2}:\\d{2}:\\d{2})\\s([0-9a-zA-Z]+)\\sEVALUATE\\s\\[([0-9a-zA-Z]+)\\]\\s([0-9a-z]{32})\\s\\\"([0-9a-zA-Z\\.]+)\\\"\\s([0-9a-zA-Z]+)\\s\\\"(\\d{1,3}.\\d+,\\d{1,3}.\\d+)\\\"\\s\\[(\\d+.\\d+,\\d+.\\d+,\\d+.\\d+)\\]\\s\\\"(.*?)\\\"$");
        Matcher matcher = EVAL_PATTERN.matcher("INFO 2019-11-25 14:11:00 app1 EVALUATE [zhangsan01] 6ebaf4ac780f40f486359f3ea6934620 \"123456bCA\" beijing \"116.4,39.5\" [1000,1300.0,1000.0] \"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.87 Safari/537.36\"");
        if (matcher.matches()) {
            for (int i = 0; i < matcher.groupCount(); i++) {
                System.out.println("matcher.group(" + i + ") = " + matcher.group(i));
            }
        } else {
            System.out.println("没有匹配项");
        }


    }
}
