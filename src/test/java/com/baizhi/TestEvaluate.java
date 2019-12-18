package com.baizhi;

import com.baizhi.entity.EvalReport;
import com.baizhi.entity.EvaluateData;
import com.baizhi.entity.HistoryData;
import com.baizhi.entity.LoginSuccessData;
import com.baizhi.evaluate.EvaluateChain;
import com.baizhi.evaluate.impl.*;
import com.baizhi.update.UpdateChain;
import com.baizhi.update.impl.*;
import com.baizhi.util.LogParser;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;

/**
 * 模拟登录验证
 */
public class TestEvaluate {

    //历史数据
    private HistoryData historyData = new HistoryData();

    //修改历史数据
    @Before
    public void before() throws ParseException {

        //登录成功日志
        String successLog = "INFO 2019-11-25 14:11:00 app1 SUCCESS [zhangsan01] 6ebaf4ac780f40f486359f3ea6934620 \"123456bCA\" beijing \"116.4,39.5\" [1000,1300.0,1000.0] \"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.87 Safari/537.36\"";
        //String successLog = "INFO 2019-11-25 14:11:00 app1 SUCCESS [zhangsan01] 6ebaf4ac780f40f486sdgf3ea6934620 \"123456bCA\" beijing \"116.4,39.5\" [1000,1300.0,1000.0] \"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.87 Safari/537.36\"";
        //String successLog = "INFO 2019-11-25 15:11:00 app2 SUCCESS [zhangsan01] 6ebaf4ac780f40d486359f3ea6934620 \"123456bCA\" beijing \"116.4,39.5\" [1000,1300.0,1000.0] \"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.87 Safari/537.36\"";
        //String successLog = "INFO 2019-11-25 15:11:00 app1 SUCCESS [zhangsan01] 6ebaf4ac780f40fg6359f3ea6934620 \"123456bCA\" beijing \"116.4,39.5\" [1000,1300.0,1000.0] \"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.87 Safari/537.36\"";
        //String successLog = "INFO 2019-11-25 15:11:00 app2 SUCCESS [zhangsan01] 6ebaf4ac780fg86359f3ea6934620 \"123456bCA\" beijing \"116.4,39.5\" [1000,1300.0,1000.0] \"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.87 Safari/537.36\"";
        //String successLog = "INFO 2019-11-25 14:33:00 app1 SUCCESS [zhangsan01] 6ebaf4ac780f40f486359f3ea6934620 \"123456bCA\" beijing \"116.4,39.5\" [1000,1300.0,1000.0] \"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.87 Safari/537.36\"";
        //String successLog = "INFO 2019-11-25 14:33:00 app2 SUCCESS [zhangsan01] 6ebaf4ac780f40f486359f3ea6934620 \"123456bCA\" beijing \"116.4,39.5\" [1000,1300.0,1000.0] \"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.87 Safari/537.36\"";
        //String successLog = "INFO 2019-11-26 09:33:00 app1 SUCCESS [zhangsan01] 6ebaf4ac780f40f486359f3ea6934620 \"123456bCA\" beijing \"116.4,39.5\" [1000,1300.0,1000.0] \"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.87 Safari/537.36\"";
        //String successLog = "INFO 2019-11-26 22:33:00 app2 SUCCESS [zhangsan01] 6ebaf4ac780f40f486359f3ea6934620 \"123456bCA\" beijing \"116.4,39.5\" [1000,1300.0,1000.0] \"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.87 Safari/537.36\"";
        //转化为登录成功数据
        LoginSuccessData loginSuccessData = LogParser.parseLoginSuccessData(successLog);

        System.out.println("=========登录成功数据=========");

        System.out.println(loginSuccessData);

        System.out.println("=========登录成功数据=========");


        //更新历史数据链
        UpdateChain updateChain = new UpdateChain();
        updateChain
                .addHandler(new HistoricalHabitsUpdateHandler())
                .addHandler(new HistoryCitiesUpdateHandler())
                .addHandler(new HistoryDevicesUpdateHandler())
                .addHandler(new HistoryPasswordsUpdateHandler())
                .addHandler(new HistoryVectorsUpdateHandler())
                .addHandler(new LastPointUpdateHandler())
                .addHandler(new LastTimeUpdateHandler())
                .doChain(historyData, loginSuccessData);

    }

    //测试评估
    @Test
    public void testEvaluate() throws ParseException {
        EvalReport evalReport = new EvalReport();
        //登录评估日志
        String evaluateLog = "INFO 2019-11-25 15:20:00 app1 EVALUATE [zhangsan01] 6ebaf4ac780f40f486359f3ea6934620 \"jfdifidffbCA\" henan \"0.0,0.0\" [2000,1800.0,50.0] \"Bozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.87 Safari/537.36\"";


        System.out.println("========打印历史数据==========");
        System.out.println(historyData);
        System.out.println("========打印历史数据==========");

        EvaluateData evaluateData = LogParser.parseEvaluateData(evaluateLog);
        System.out.println("========打印评估数据==========");
        System.out.println(evaluateData);
        System.out.println("========打印评估数据==========");

        EvaluateChain evaluateChain = new EvaluateChain();
        evaluateChain
                .addHandler(new DeviceReplaceEvaluateHandler())
                .addHandler(new InputFeatureEvaluateHandler())
                .addHandler(new LoginCountEvaluateHandler())
                .addHandler(new LoginHabitEvaluateHandler())
                .addHandler(new RegionLoginEvaluateHandler())
                .addHandler(new SpeedEvaluateHandler())
                .addHandler(new WrongPassEvaluateHandler())
                .doChain(evaluateData, historyData, evalReport);
        System.out.println("========打印报表数据======");
        System.out.println(evalReport);
        System.out.println("========打印报表数据======");
    }

}
