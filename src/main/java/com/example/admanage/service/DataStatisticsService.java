package com.example.admanage.service;

import com.example.admanage.entity.DataStatistics;
import com.example.admanage.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

public interface DataStatisticsService {

    /**
     * 插入统计数据
     */
    public int insertData(DataStatistics dataStatistics);

    /**
     * 更新统计数据
     */
    public int updateData(DataStatistics dataStatistics);

    /**
     * 查询统计数据
     */
    public List<DataStatistics> queryDataBy2Date(String startDate,String endDate);
}
