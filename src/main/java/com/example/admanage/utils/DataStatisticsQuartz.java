package com.example.admanage.utils;

import com.example.admanage.SmallsoftwareApplication;
import com.example.admanage.controller.BaseController;
import com.example.admanage.entity.DataStatistics;
import com.example.admanage.entity.UserAcount;
import com.example.admanage.service.DataStatisticsService;
import com.example.admanage.service.UserAcountService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;
import org.thymeleaf.util.DateUtils;

import java.text.SimpleDateFormat;
import java.util.*;

@EnableScheduling
@Component // 此注解必加
public class DataStatisticsQuartz extends BaseController {

    @Autowired
    private UserAcountService userAcountService;

    @Autowired
    private DataStatisticsService dataStatisticsService;

    private String startDate;

    private String endDate;

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    /**
     * 统计前一天的消费的金额
     */
    @Scheduled(cron = "00 00 00 * * ?")
    public void play() throws Exception {
        System.out.println("执行Quartz定时任务 统计前一天的消费金额情况："+new Date());
        Date now=new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        now = calendar.getTime();
        String beforeOneDay = DateUtils.format(now,"yyyy-MM-dd",Locale.CHINESE);
        String today = DateUtils.format(new Date(),"yyyy-MM-dd",Locale.CHINESE);
        if(StringUtils.isNotBlank(this.startDate)){
            beforeOneDay = this.startDate;
            today = this.endDate;
        }
        try {
            List<UserAcount> userAcountList = userAcountService.queryUserAcountBy2Date(beforeOneDay, today, 1);
            if (userAcountList != null && !userAcountList.isEmpty()) {
                //针对前一天充值的还没消费完的自动转入到今天
                //1,获取前一天还没消费完的账户
                List<UserAcount> insertUserAcountList = new ArrayList();
                List<UserAcount> updateUserAcountList = new ArrayList();
                List<UserAcount> dataStatisticsList = new ArrayList();
                UserAcount insertUserAcount;
                UserAcount updateUserAcount;

                //统计数据默认消费金额0元
                DataStatistics dataStatistics = new DataStatistics();
                dataStatistics.setTotalMoney(0f);

                calendar.set(Calendar.HOUR_OF_DAY,23);
                calendar.set(Calendar.MINUTE,59);
                calendar.set(Calendar.SECOND,59);
                for (int i = 0; i < userAcountList.size(); i++) {
                    if (userAcountList.get(i).getIsStart() == 1) {
                        insertUserAcount = new UserAcount();
                        updateUserAcount = new UserAcount();
                        Integer timeSpan = getSecondTimestamp(calendar.getTime()) - getSecondTimestamp(userAcountList.get(i).getUpdateTime()) ;
                        Double usemoney = div((double)timeSpan,userAcountList.get(i).getSpendSpeed(),1);
                        //随着时间变化余额变化情况
                        usemoney = add(userAcountList.get(i).getUseMoney(), usemoney);
                        if(usemoney < userAcountList.get(i).getBlanance()){
                            insertUserAcount.setBlanance((float) sub((double)userAcountList.get(i).getBlanance(),usemoney));
                            insertUserAcount.setUserId(userAcountList.get(i).getUserId());
                            insertUserAcount.setIsStart(1);
                            insertUserAcount.setSpendSpeed(userAcountList.get(i).getSpendSpeed());
                            insertUserAcount.setCreateTime(new Date());
                            insertUserAcount.setUpdateTime(new Date());
                            insertUserAcountList.add(insertUserAcount);
                        }else{
                            usemoney = (double)userAcountList.get(i).getBlanance();
                        }
                        updateUserAcount.setId(userAcountList.get(i).getId());
                        updateUserAcount.setIsStart(0);
                        updateUserAcount.setBlanance(usemoney.floatValue());
                        updateUserAcount.setUseMoney(usemoney.floatValue());
                        updateUserAcount.setUpdateTime(calendar.getTime());
                        updateUserAcountList.add(updateUserAcount);
                    }
                    dataStatistics.setTotalMoney(dataStatistics.getTotalMoney()+userAcountList.get(i).getUseMoney());
                }
                //2，对这些账户进行批量更新
                if(updateUserAcountList != null && !updateUserAcountList.isEmpty()) {
                    userAcountService.batchUpdateUserAcount(updateUserAcountList);
                }
                //3,对未消费完的账户进行重新计算并批量插入到数据库中
                if(insertUserAcountList != null && !insertUserAcountList.isEmpty()) {
                    userAcountService.batchInserteUserAcount(insertUserAcountList);
                }
                //统计数据并同步到数据库中 TODO
                logger.info("开始同步数据到统计表中！！！");
                dataStatistics.setSpendDate(calendar.getTime());
                dataStatistics.setCreateTime(new Date());
                List<DataStatistics> dataStatistics1 = dataStatisticsService.queryDataBy2Date(this.startDate+" 00:00:00",this.endDate+" 00:00:00");
                if(dataStatistics1 != null && dataStatistics1.size() > 0) {
                    dataStatistics.setId(dataStatistics1.get(0).getId());
                    dataStatisticsService.updateData(dataStatistics);
                }else{
                    if(StringUtils.isNotBlank(this.startDate)){
                        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");
                        dataStatistics.setSpendDate(sdf.parse(this.startDate));
                    }
                    dataStatisticsService.insertData(dataStatistics);
                }
            }
        }catch(Exception e){
            e.printStackTrace();

        }
    }


    public static void main(String[] args){
        try {
            Date now=new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(now);
            calendar.set(Calendar.DAY_OF_MONTH, 10);
            now = calendar.getTime();
            String beforeOneDay = DateUtils.format(now,"yyyy-MM-dd",Locale.CHINESE);
            System.out.println(beforeOneDay);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
