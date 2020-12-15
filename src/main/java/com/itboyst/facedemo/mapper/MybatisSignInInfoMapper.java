package com.itboyst.facedemo.mapper;

import com.itboyst.facedemo.domain.SignInInfo;

import java.util.Date;

public interface MybatisSignInInfoMapper {
    public void updateSignByFaceId(Integer sign, Date signTime, Integer signTotal, String faceId);

    public void updateSign(Integer sign, Date todayTime);

    public SignInInfo selectSignInInfoByFaceId(String faceId);

    public SignInInfo selectSignInInfo();
}
