package com.example.admanage.service.Impl;

import com.example.admanage.dao.DataStatisticsDao;
import com.example.admanage.entity.DataStatistics;
import com.example.admanage.entity.User;
import com.example.admanage.service.DataStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DataStatisticsServiceImpl implements DataStatisticsService {

    @Autowired
    private DataStatisticsDao dataStatisticsDao;

    @Override
    @Transactional
    public int insertData(DataStatistics dataStatistics) {
        return dataStatisticsDao.insertData(dataStatistics);
    }

    /**
     * 更新统计数据
     */
    @Override
    @Transactional
    public int updateData(DataStatistics dataStatistics){
        return dataStatisticsDao.updateData(dataStatistics);
    }

    public List<DataStatistics> queryDataBy2Date(String startDate,String endDate,Integer userId){
        return dataStatisticsDao.queryDataBy2Date(startDate,endDate,userId);
    }
}
