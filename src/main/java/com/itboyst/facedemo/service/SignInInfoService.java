package com.itboyst.facedemo.service;

import com.itboyst.facedemo.domain.SignInInfo;

import java.util.Date;

public interface SignInInfoService {

    public void signByFaceId(Integer sign, Date signTime, Integer signTotal, String faceId);

    public SignInInfo querySignInInfoByFaceId(String faceId);

    public void updateSignInInfo(Integer sign,Date todayTime);
}
