package com.itboyst.facedemo.service.impl;

import com.itboyst.facedemo.domain.SignInInfo;
import com.itboyst.facedemo.mapper.MybatisSignInInfoMapper;
import com.itboyst.facedemo.service.SignInInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class SignInInfoServiceImpl implements SignInInfoService {
    @Autowired
    private MybatisSignInInfoMapper signInInfoMapper;

    @Override
    public void signByFaceId(Integer sign, Date signTime, Integer signTotal, String faceId) {
        signInInfoMapper.updateSignByFaceId(sign, signTime, signTotal, faceId);

    }

    @Override
    public SignInInfo querySignInInfoByFaceId(String faceId) {
        return signInInfoMapper.selectSignInInfoByFaceId(faceId);
    }

    @Override
    public void updateSignInInfo(Integer sign, Date todayTime) {
       signInInfoMapper.updateSign(sign,todayTime);
    }
}
