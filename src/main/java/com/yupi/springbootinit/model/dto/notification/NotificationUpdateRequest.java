package com.yupi.springbootinit.model.dto.notification;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 更新请求
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@Data
public class NotificationUpdateRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 公告标题
     */
    private String title;

    /**
     * 公告内容
     */
    private String content;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 通知类型
     */
    private String type;

    /**
     * 0:关闭,1:启用
     */
    private Integer status;

    /**
     * 域名
     */
    private List<String> domain;

    private static final long serialVersionUID = 1L;
}