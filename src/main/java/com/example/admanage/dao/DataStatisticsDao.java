package com.example.admanage.dao;

import com.example.admanage.entity.DataStatistics;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DataStatisticsDao {

    /**
     * 插入统计数据
     */
    public int insertData(DataStatistics dataStatistics);

    /**
     * 插入统计数据
     */
    public int updateData(DataStatistics dataStatistics);

    /**
     * 查询统计数据
     */
    public List<DataStatistics> queryDataBy2Date(@Param("startDate") String startDate, @Param("endDate") String endDate);
}
