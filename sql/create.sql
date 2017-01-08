
CREATE DATABASE IF NOT EXISTS weibocrawler DEFAULT CHARSET utf8mb4 COLLATE utf8mb4_general_ci;

USE weibocrawler;

CREATE TABLE blog (
  blog_id VARCHAR(50) NOT NULL COMMENT '΢������id',
  blog_text text COMMENT '΢������',
  img_num INT DEFAULT 0 COMMENT 'ͼƬ����',
  like_num INT DEFAULT 0 COMMENT '������',
  repeat_num INT DEFAULT 0 COMMENT 'ת������',
  comment_num INT DEFAULT 0 COMMENT '��������',
  create_date DATE NOT NULL COMMENT '΢��ʱ��',
  device VARCHAR(100) COMMENT '�����豸',
  place varchar(100) comment '΢��λ��',
  bl varchar(24) comment '��γ��',
  PRIMARY KEY (detail_id)
)ENGINE=InnODB DEFAULT CHARSET=utf8 COMMENT='΢������';

CREATE TABLE image (
  img VARCHAR(50) COMMENT 'ͼƬid',
  blog_id VARCHAR(50) COMMENT 'ͼƬ���ڵ�΢��id',
  img_sort CHAR(3) COMMENT 'ͼƬ��΢���е�����λ��',
  PRIMARY KEY (ima_id)
)ENGINE=InnODB DEFAULT CHARSET=utf8 COMMENT='΢���е�ͼƬ';

