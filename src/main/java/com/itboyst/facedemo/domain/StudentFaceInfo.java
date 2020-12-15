package com.itboyst.facedemo.domain;

import lombok.Data;

import java.util.Date;

@Data
/*修改*/
public class StudentFaceInfo {

    private Integer stuNum;

    private Integer groupId;

    private String faceId;

    private String name;

    private Date createTime;

    private Date updateTime;

    private byte[] faceFeature;

}
