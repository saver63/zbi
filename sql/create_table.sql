# 数据库初始化
# @author <a href="https://github.com/liyupi">程序员鱼皮</a>
# @from <a href="https://yupi.icu">编程导航知识星球</a>

-- 创建库
create database if not exists zbi;

-- 切换库
use zbi;


-- 用户表
create table  if not exists user
(
    id           bigint auto_increment comment 'id' primary key,
    userAccount  varchar(256)                           not null comment '账号',
    userPassword varchar(512)                           not null comment '密码',
    userName     varchar(256)                           null comment '用户昵称',
    userAvatar   varchar(1024)                          null comment '用户头像',
    userRole     varchar(256) default 'user'            not null comment '用户角色：user/admin/ban',
    leftNum      int                                    null comment '剩余次数',
    totalNum     int                                    null comment '使用总数',
    createTime   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint      default 0                 not null comment '是否删除',
    index idx_userAccount (userAccount)
) comment '用户' collate = utf8mb4_unicode_ci;

-- 图表信息表
create table if not exists chart
(
    id         bigint auto_increment comment 'id' primary key,
    goal       text                               null comment '分析目标',
    name       varchar(128)                        null comment '图表名称',
    chartData  text                               null comment '图表数据',
    chartType  varchar(128)                       null comment '图表类型',
    genChart   text                               null comment '生成的图表数据',
    genResult  text                               null comment '生成的分析结论',
    status     varchar(128)                       not null default 'wait' comment 'wait,succeed,failed,running',
    execMessage text                              null comment '执行信息',
    userId     bigint                             null comment '创建用户id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除'
) comment '图表信息表' collate = utf8mb4_unicode_ci;

create table  if not exists notification
(
    id           bigint auto_increment comment 'id' primary key,
    title        varchar(256)                           not null comment '公告标题',
    content      varchar(2048)                          not null comment '公告内容',
    startTime    datetime                               null comment '开始时间',
    endTime      datetime                               null comment '结束时间',
    userId       mediumtext                             not null comment '所属用户',
    status       tinyint default 0                      not null comment '0:关闭, 1:启用',
    domain       varchar(255)                           null comment '域名',
    createTime   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint      default 0                 not null comment '是否删除'
) comment '通告表' collate = utf8mb4_unicode_ci;

create table if not exists post(
   id           bigint auto_increment comment 'id' primary key,
   title        varchar(512)                           null comment '标题',
   content      text                          null comment '公告内容',
   tags         varchar(1024)                          null comment '标签列表（Json数组）',
   thumbNum     int          default 0                 not null comment '点赞数',
   favourNum    int          default 0                 not null comment '收藏数',
   userId       bigint                                 not null comment '创建用户id',
   createTime   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
   updateTime   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
   isDelete     tinyint      default 0                 not null comment '是否删除',
   index idx_userId(userId)
) comment '帖子' collate  = utf8mb4_unicode_ci;

-- 用户标签表
DROP TABLE IF EXISTS `user_tag`;
CREATE TABLE `user_tag`
(
    `id`          bigint     NOT NULL AUTO_INCREMENT COMMENT '主键',
    `parentId`   bigint     NOT NULL COMMENT '类别id',
    `name`        varchar(64)         DEFAULT NULL COMMENT '标签名',
    `color`       varchar(8)          DEFAULT NULL COMMENT '颜色',
    `createdBy`  bigint     NOT NULL COMMENT '创建用户',
    createTime     datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime     datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `a_user_tag_pk` (`name`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 17
  DEFAULT CHARSET = utf8mb3;

LOCK TABLES `user_tag` WRITE;
/*!40000 ALTER TABLE `user_tag`
    DISABLE KEYS */;
INSERT INTO `user_tag`
VALUES (1, 1, '双一流', 'red', 2, '2023-03-07 09:04:39', '2023-03-07 09:04:39', 0),
       (2, 1, '985', 'purple', 2, '2023-03-07 15:16:23', '2023-03-07 15:16:23', 0),
       (3, 2, '计算机科学', 'red', 2, '2023-03-07 15:29:06', '2023-03-07 15:29:06', 0),
       (4, 4, '后端工程师', 'orange', 2, '2023-03-07 15:30:01', '2023-03-07 15:30:01', 0),
       (5, 5, '求职', 'cyan', 2, '2023-03-07 15:33:43', '2023-03-07 15:33:43', 0),
       (6, 3, '战地', 'purple', 2, '2023-03-07 15:35:50', '2023-03-07 15:35:50', 0),
       (7, 2, '通信工程', 'green', 2, '2023-03-07 15:38:38', '2023-03-07 15:38:38', 0),
       (8, 1, '研究生', 'orange', 2, '2023-03-07 15:39:56', '2023-03-07 15:39:56', 0),
       (9, 4, '安卓开发', 'orange', 2, '2023-03-07 15:41:33', '2023-03-07 15:41:33', 0),
       (10, 5, '工作', 'cyan', 2, '2023-03-07 15:51:35', '2023-03-07 15:51:35', 0),
       (11, 3, '二次元', 'orange', 2, '2023-03-07 15:52:39', '2023-03-07 15:52:39', 0),
       (12, 2, '人工智能', 'pink', 2, '2023-03-07 15:54:14', '2023-03-07 15:54:14', 0),
       (13, 2, '软件工程', 'green', 2, '2023-03-07 15:57:01', '2023-03-07 15:57:01', 0),
       (14, 5, '躺平', 'purple', 2, '2023-04-15 06:11:46', '2023-04-15 06:11:46', 0),
       (15, 1, '一本', 'pink', 3, '2023-05-27 14:45:14', '2023-05-27 14:45:14', 0),
       (16, 5, '学习', 'pink', 3, '2023-05-27 14:45:41', '2023-05-27 14:45:41', 0);
/*!40000 ALTER TABLE `user_tag`
    ENABLE KEYS */;
UNLOCK TABLES;

-- 帖子点赞表（硬删除）
create table if not exists post_thumb
(
    id         bigint auto_increment comment 'id' primary key,
    postId     bigint                             not null comment '帖子 id',
    userId     bigint                             not null comment '创建用户 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    index idx_postId (postId),
    index idx_userId (userId)
) comment '帖子点赞';


-- 接口信息表
CREATE TABLE if not exists interface_info (
                                  `id` bigint NOT NULL AUTO_INCREMENT comment '主键' primary key ,
                                  `name` VARCHAR(256) NOT NULL COMMENT '接口名称',
                                  `description` varchar(256) null COMMENT '描述',
                                  `url` VARCHAR(512) NOT NULL COMMENT '接口地址',
                                  `method` VARCHAR(256) NOT NULL COMMENT '请求类型',
                                  `requestHeader` TEXT COMMENT '请求头',
                                  `responseHeader` TEXT COMMENT '响应头',
                                  `interfaceStatus` TINYINT NOT NULL COMMENT '接口状态',
                                  'userId' bigint NOT NULL COMMENT '创建人',
                                  'status' int default 0 not null comment '状态，0-正常，1-禁用',
                                  `isDeleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除标识，1表示已删除，0表示未删除',
                                  `createdTime` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                  `updatedTime` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
)  COMMENT='接口信息表';