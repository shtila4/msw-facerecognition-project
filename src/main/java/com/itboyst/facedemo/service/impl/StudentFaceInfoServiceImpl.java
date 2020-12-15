package com.itboyst.facedemo.service.impl;

import com.itboyst.facedemo.domain.StudentFaceInfo;

import com.itboyst.facedemo.mapper.MybatisStudentFaceInfoMapper;

import com.itboyst.facedemo.service.StudentFaceInfoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class StudentFaceInfoServiceImpl implements StudentFaceInfoService {

@Autowired
private MybatisStudentFaceInfoMapper studentFaceInfoMapper;
    @Override
    public void insertSelective(StudentFaceInfo studentFaceInfo) {
        studentFaceInfoMapper.insertStudentFaceInfo(studentFaceInfo);
    }
}
