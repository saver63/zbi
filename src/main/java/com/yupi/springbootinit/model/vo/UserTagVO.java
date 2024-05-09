package com.yupi.springbootinit.model.vo;

import lombok.Data;

@Data
public class UserTagVO {

    private long id;
    private long parentId;

    /**
     * 名称
     */
    private String name;

    /**
     * 颜色
     */
    private String color;

}
