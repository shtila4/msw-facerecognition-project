package com.itboyst.facedemo.domain;

import lombok.Data;

import java.util.Date;
@Data
public class SignInInfo {
    private Integer stuNum;
    private String name;
    private String faceId;
    private Integer age;
    private String sex;
    private Integer sign;
    private Date  signTime;
    private Date todayTime;
    private Integer signTotal;
    private String remarks;
}
