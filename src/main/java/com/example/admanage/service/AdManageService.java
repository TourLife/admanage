package com.example.admanage.service;

import com.example.admanage.entity.Ad;

import java.util.List;

public interface AdManageService {

    public List<Ad> queryAdList(Ad ad);

    public Ad queryAd(Integer adId);

    public Ad addAd(Ad ad);

    public Ad updateAd(Ad ad);

}
