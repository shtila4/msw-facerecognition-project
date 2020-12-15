SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for user_face_info
-- ----------------------------
/*人脸信息表*/
CREATE TABLE `student_face_info` (
  `stu_num` int(11) NOT NULL  COMMENT '主键',
	`group_id` int(11) DEFAULT NULL COMMENT '分组id',
  `face_id` varchar(31) DEFAULT NULL COMMENT '人脸唯一Id',
  `name` varchar(63) DEFAULT NULL COMMENT '名字',
  `face_feature` blob COMMENT '人脸特征',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`stu_num`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;
SET FOREIGN_KEY_CHECKS = 1;


/*学生信息表*/
create table  student_info(
stu_num int(11) not null,
name varchar(63),
face_id varchar(31),
age int(11),
sex VARCHAR(4),
phone BIGINT,
address VARCHAR(50),
school VARCHAR(20),
college VARCHAR(20),
grade VARCHAR(20),
class VARCHAR(20),
counselor VARCHAR(20),
remarks VARCHAR(20),
PRIMARY KEY (stu_num)
);


/*签到表*/
create table sign_in_info(
stu_num int(11) not null,
face_id varchar(31),
name varchar(63),
age int(11),
sex VARCHAR(4),
sign int(10),
sign_time timestamp ,
today_time timestamp,
sign_total int(20),
remarks VARCHAR(20),
PRIMARY KEY (stu_num)
)


