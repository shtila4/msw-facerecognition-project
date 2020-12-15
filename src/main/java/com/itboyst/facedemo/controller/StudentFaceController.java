package com.itboyst.facedemo.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.RandomUtil;
import com.arcsoft.face.FaceInfo;
import com.arcsoft.face.toolkit.ImageFactory;
import com.arcsoft.face.toolkit.ImageInfo;
import com.itboyst.facedemo.base.Result;
import com.itboyst.facedemo.base.Results;
import com.itboyst.facedemo.domain.SignInInfo;
import com.itboyst.facedemo.domain.StudentFaceInfo;

import com.itboyst.facedemo.dto.FaceSearchResDto;
import com.itboyst.facedemo.dto.FaceUserInfo;
import com.itboyst.facedemo.dto.ProcessInfo;
import com.itboyst.facedemo.enums.ErrorCodeEnum;
import com.itboyst.facedemo.service.FaceEngineService;
import com.itboyst.facedemo.service.SignInInfoService;
import com.itboyst.facedemo.service.StudentFaceInfoService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/*修改*/
@Controller
public class StudentFaceController {

    public final static Logger logger = LoggerFactory.getLogger(StudentFaceController.class);


    @Autowired
    FaceEngineService faceEngineService;

    @Autowired
    StudentFaceInfoService studentFaceInfoService;
    @Autowired
    SignInInfoService signInInfoService;

    @RequestMapping(value = "/studemo")
    public String demo() throws ParseException {
        int sign=0;
        Date todayTime=new Date();
        signInInfoService.updateSignInInfo(sign,todayTime);
        return "demo";
    }

    /*
    人脸添加
     */
    @RequestMapping(value = "/stufaceAdd", method = RequestMethod.POST)
    @ResponseBody
    public Result<Object> faceAdd(@RequestParam("file") String file, @RequestParam("groupId") Integer groupId, @RequestParam("name") String name, @RequestParam("stuNum") Integer stuNum) {

        try {
            if (file == null) {
                return Results.newFailedResult("file is null");
            }
            if (groupId == null) {
                return Results.newFailedResult("groupId is null");
            }
            if (name == null) {
                return Results.newFailedResult("name is null");
            }

            byte[] decode = Base64.decode(base64Process(file));
            ImageInfo imageInfo = ImageFactory.getRGBData(decode);

            //人脸特征获取
            byte[] bytes = faceEngineService.extractFaceFeature(imageInfo);
            if (bytes == null) {
                return Results.newFailedResult(ErrorCodeEnum.NO_FACE_DETECTED);
            }


            StudentFaceInfo studentFaceInfo = new StudentFaceInfo();
            studentFaceInfo.setStuNum(stuNum);
            studentFaceInfo.setName(name);
            studentFaceInfo.setGroupId(groupId);
            studentFaceInfo.setFaceFeature(bytes);
            studentFaceInfo.setFaceId(RandomUtil.randomString(10));
            studentFaceInfo.setCreateTime(new Date());
            studentFaceInfo.setUpdateTime(new Date());
            //人脸特征插入到数据库
            studentFaceInfoService.insertSelective(studentFaceInfo);

            logger.info("faceAdd:" + name);
            return Results.newSuccessResult("");
        } catch (Exception e) {
            logger.error("", e);
        }
        return Results.newFailedResult(ErrorCodeEnum.UNKNOWN);
    }

    /*
    人脸识别
     */
    @RequestMapping(value = "/stufaceSearch", method = RequestMethod.POST)
    @ResponseBody
    public Result<FaceSearchResDto> faceSearch(String file, Integer groupId) throws Exception {

        if (groupId == null) {
            return Results.newFailedResult("groupId is null");
        }
        byte[] decode = Base64.decode(base64Process(file));
        BufferedImage bufImage = ImageIO.read(new ByteArrayInputStream(decode));
        ImageInfo imageInfo = ImageFactory.bufferedImage2ImageInfo(bufImage);


        //人脸特征获取
        byte[] bytes = faceEngineService.extractFaceFeature(imageInfo);
        if (bytes == null) {
            return Results.newFailedResult(ErrorCodeEnum.NO_FACE_DETECTED);
        }
        //人脸比对，获取比对结果
        List<FaceUserInfo> userFaceInfoList = faceEngineService.compareFaceFeature(bytes, groupId);

        if (CollectionUtil.isNotEmpty(userFaceInfoList)) {
            FaceUserInfo faceUserInfo = userFaceInfoList.get(0);
            System.out.println(userFaceInfoList.get(0).getFaceId() + userFaceInfoList.get(0).getSimilarValue());
            String faceId = userFaceInfoList.get(0).getFaceId();
            SignInInfo signInInfo = signInInfoService.querySignInInfoByFaceId(faceId);
            int signTotal = signInInfo.getSignTotal();
            int sign = 1;
            int similarValue = userFaceInfoList.get(0).getSimilarValue();
            if (similarValue >= 80) {
                signInInfoService.signByFaceId(sign, new Date(), (signTotal + 1), faceId);
            }
            FaceSearchResDto faceSearchResDto = new FaceSearchResDto();
            BeanUtil.copyProperties(faceUserInfo, faceSearchResDto);
            List<ProcessInfo> processInfoList = faceEngineService.process(imageInfo);
            if (CollectionUtil.isNotEmpty(processInfoList)) {
                //人脸检测
                List<FaceInfo> faceInfoList = faceEngineService.detectFaces(imageInfo);
                int left = faceInfoList.get(0).getRect().getLeft();
                int top = faceInfoList.get(0).getRect().getTop();
                int width = faceInfoList.get(0).getRect().getRight() - left;
                int height = faceInfoList.get(0).getRect().getBottom() - top;

                Graphics2D graphics2D = bufImage.createGraphics();
                graphics2D.setColor(Color.RED);//红色
                BasicStroke stroke = new BasicStroke(5f);
                graphics2D.setStroke(stroke);
                graphics2D.drawRect(left, top, width, height);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                ImageIO.write(bufImage, "jpg", outputStream);
                byte[] bytes1 = outputStream.toByteArray();

                faceSearchResDto.setImage("data:image/jpeg;base64," + Base64Utils.encodeToString(bytes1));
                faceSearchResDto.setAge(processInfoList.get(0).getAge());
                faceSearchResDto.setGender(processInfoList.get(0).getGender().equals(1) ? "女" : "男");

            }

            return Results.newSuccessResult(faceSearchResDto);
        }
        return Results.newFailedResult(ErrorCodeEnum.FACE_DOES_NOT_MATCH);
    }


    @RequestMapping(value = "/studetectFaces", method = RequestMethod.POST)
    @ResponseBody
    public List<FaceInfo> detectFaces(String image) throws IOException {
        byte[] decode = Base64.decode(image);
        InputStream inputStream = new ByteArrayInputStream(decode);
        ImageInfo imageInfo = ImageFactory.getRGBData(inputStream);

        if (inputStream != null) {
            inputStream.close();
        }
        List<FaceInfo> faceInfoList = faceEngineService.detectFaces(imageInfo);

        return faceInfoList;
    }


    private String base64Process(String base64Str) {
        if (!StringUtils.isEmpty(base64Str)) {
            String photoBase64 = base64Str.substring(0, 30).toLowerCase();
            int indexOf = photoBase64.indexOf("base64,");
            if (indexOf > 0) {
                base64Str = base64Str.substring(indexOf + 7);
            }

            return base64Str;
        } else {
            return "";
        }
    }
}
