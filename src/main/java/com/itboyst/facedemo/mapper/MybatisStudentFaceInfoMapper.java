package com.itboyst.facedemo.mapper;

import com.itboyst.facedemo.domain.StudentFaceInfo;

import com.itboyst.facedemo.dto.FaceUserInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/*修改*/
@Mapper
public interface MybatisStudentFaceInfoMapper {



    void insertStudentFaceInfo(StudentFaceInfo studentFaceInfo);

    List<FaceUserInfo> getStudentFaceInfoByGroupId(Integer groupId);


}
