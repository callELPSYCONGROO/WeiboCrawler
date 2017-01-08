
CREATE DATABASE IF NOT EXISTS weibocrawler DEFAULT CHARSET utf8mb4 COLLATE utf8mb4_general_ci;

USE weibocrawler;

CREATE TABLE blog (
  blog_id VARCHAR(50) NOT NULL COMMENT '微博详情id',
  blog_text text COMMENT '微博正文',
  img_num INT DEFAULT 0 COMMENT '图片数量',
  like_num INT DEFAULT 0 COMMENT '赞数量',
  repeat_num INT DEFAULT 0 COMMENT '转发数量',
  comment_num INT DEFAULT 0 COMMENT '评论数量',
  create_date DATE NOT NULL COMMENT '微博时间',
  device VARCHAR(100) COMMENT '发送设备',
  place varchar(100) comment '微博位置',
  bl varchar(24) comment '经纬度',
  PRIMARY KEY (detail_id)
)ENGINE=InnODB DEFAULT CHARSET=utf8 COMMENT='微博详情';

CREATE TABLE image (
  img VARCHAR(50) COMMENT '图片id',
  blog_id VARCHAR(50) COMMENT '图片属于的微博id',
  img_sort CHAR(3) COMMENT '图片在微博中的排序位置',
  PRIMARY KEY (ima_id)
)ENGINE=InnODB DEFAULT CHARSET=utf8 COMMENT='微博中的图片';

