package com.example.admanage.dao;

import com.example.admanage.entity.Ad;
import com.example.admanage.entity.Plan;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AdManageDao {

    public List<Ad> queryAdList(Ad ad);

    public Ad queryAd(@Param("adId")Integer adId);

    public Ad addAd(Ad ad);

    public Ad updateAd(Ad ad);

}
