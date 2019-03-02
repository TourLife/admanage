package com.example.admanage.service.Impl;

import com.example.admanage.dao.AdManageDao;
import com.example.admanage.entity.Ad;
import com.example.admanage.service.AdManageService;
import com.example.admanage.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
public class AdManageServiceImpl implements AdManageService {

    @Autowired
    private AdManageDao adManageDao;

    @Override
    public List<Ad> queryAdList(Ad ad) {
        List<Ad> adList = new ArrayList();
        if(ad != null && ad.getPlanIdStr() != null){
            adList = adManageDao.queryAdList(ad);
        }
        return adList;
    }

    @Override
    public Ad queryAd(Integer adId) {
        return adManageDao.queryAd(adId);
    }

    @Override
    public Ad addAd(Ad ad) {
        return adManageDao.addAd(ad);
    }

    @Override
    public Ad updateAd(Ad ad) {
        return adManageDao.updateAd(ad);
    }
}
